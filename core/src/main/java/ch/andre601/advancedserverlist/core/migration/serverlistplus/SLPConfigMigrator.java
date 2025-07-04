/*
 * MIT License
 *
 * Copyright (c) 2022-2024 Andre_601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ch.andre601.advancedserverlist.core.migration.serverlistplus;

import ch.andre601.advancedserverlist.api.objects.NullBool;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileSerializer;
import net.minecrell.serverlistplus.core.ServerListPlusCore;
import net.minecrell.serverlistplus.core.config.PersonalizedStatusConf;
import net.minecrell.serverlistplus.core.config.ServerStatusConf;
import net.minecrell.serverlistplus.core.util.BooleanOrList;
import net.minecrell.serverlistplus.core.util.IntegerRange;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SLPConfigMigrator{
    
    private static final Pattern RGB_COLOR_PATTERN = Pattern.compile("&(?<color>#[a-fA-F0-9]{6})");
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("&(?<color>[abcdefklmnorABCDEFKLMNOR0123456789])");
    private static final Pattern RGB_GRADIENT_PATTERN = Pattern.compile(
        "%gradient" +
        "(?<start>#[a-fA-F0-9]{6})" + // Starting color
        "(?<end>#[a-fA-F0-9]{6})" +   // Ending color
        "%" +
        "(?<text>.+?)" +              // Text
        "%gradient%"
    );
    
    private static final Map<String, String> STATIC_REPLACEMENTS = Map.of(
        "%player%", "${player name}",
        "%uuid%", "${player uuid}",
        "%_uuid_%", "${player uuid}",
        "%online%", "${server playersOnline}",
        "%max%", "${server playersMax}"
    );
    private static final Map<String, String> COLOR_TO_MINIMESSAGE_TAG = new HashMap<>(){{
        // Color Codes.
        put("a", "<green>");
        put("b", "<aqua>");
        put("c", "<red>");
        put("d", "<light_purple>");
        put("e", "<yellow>");
        put("f", "<white>");
        put("0", "<black>");
        put("1", "<dark_blue>");
        put("2", "<dark_green>");
        put("3", "<dark_aqua>");
        put("4", "<dark_red>");
        put("5", "<dark_purple>");
        put("6", "<gold>");
        put("7", "<grey>");
        put("8", "<dark_grey>");
        put("9", "<blue>");
        
        // Formatting Codes
        put("k", "<obfuscated>");
        put("l", "<bold>");
        put("m", "<strikethrough>");
        put("n", "<underlined>");
        put("o", "<italic>");
        put("r", "<reset>");
    }};
    
    public static int migrate(AdvancedServerList<?> core, CmdSender sender){
        ServerListPlusCore slpCore = ServerListPlusCore.getInstance();
        PluginLogger logger = core.plugin().pluginLogger();
        if(slpCore == null){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Cannot migrate ServerListPlus config. ServerListPlus is not active!");
            return 0;
        }
        
        ServerStatusConf conf = slpCore.getConf(ServerStatusConf.class);
        if(conf == null){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Cannot migrate ServerListPlus configuration. Received configuration was null.");
            return 0;
        }
        
        PersonalizedStatusConf.StatusConf defConf = conf.Default;
        PersonalizedStatusConf.StatusConf personalizedConf = conf.Personalized;
        PersonalizedStatusConf.StatusConf banned = conf.Banned;
        
        sender.sendPrefixedMsg("Migrating <white>Default</white>...");
        int defConfParsed = parseConf(core, sender, defConf, Type.DEFAULT);
        
        sender.sendPrefixedMsg("Migrating <white>Personalized</white>...");
        int personalizedParsed = parseConf(core, sender, personalizedConf, Type.PERSONALIZED);
        
        sender.sendPrefixedMsg("Migrating <white>Banned</white>...");
        int bannedParsed = parseConf(core, sender, banned, Type.BANNED);
        
        return defConfParsed + personalizedParsed + bannedParsed;
    }
    
    private static int parseConf(AdvancedServerList<?> core, CmdSender sender, PersonalizedStatusConf.StatusConf conf, Type type){
        PluginLogger logger = core.plugin().pluginLogger();
        if(conf == null){
            logger.info("[<white>Migrator - ServerListPlus</white>] No StatusConf found for type <white>%s</white>. Skipping...", type.getName());
            sender.sendPrefixedMsg(" -> Not found! Skipping...");
            
            return 0;
        }
        
        ProfileEntry.Builder builder = ProfileEntry.empty().builder();
        List<ProfileEntry.Builder> profiles = new ArrayList<>();
        
        List<List<String>> motds = resolveMotds(conf.Description);
        List<String> favicons = resolveFavicons(conf.Favicon);
        List<List<String>> hovers = resolveHover(conf.Players);
        NullBool hidePlayers = conf.Players != null ? NullBool.resolve(conf.Players.Hidden) : NullBool.NOT_SET;
        List<Integer> maxPlayers = resolveMaxPlayers(conf.Players);
        List<String> playerCount = resolvePlayerCounts(conf.Players);
        
        int size = getMaxListSize(motds, favicons, hovers, maxPlayers, playerCount);
        // Only add profiles entries when there are more than 2 entries of an option.
        // Avoids a profiles option with a single entry that has no options whatsoever.
        if(size > 1){
            for(int i = 0; i < size; i++){
                profiles.add(ProfileEntry.empty().builder());
            }
        }
        
        apply(motds, builder, profiles, ProfileEntry.Builder::motd);
        apply(favicons, builder, profiles, ProfileEntry.Builder::favicon);
        apply(hovers, builder, profiles, ProfileEntry.Builder::players);
        apply(maxPlayers, builder, profiles, (build, count) -> build.maxPlayersEnabled(NullBool.TRUE).maxPlayersCount(String.valueOf(count)));
        apply(playerCount, builder, profiles, ProfileEntry.Builder::playerCountText);
        
        builder.hidePlayersEnabled(hidePlayers);
        
        ProfileEntry entry = builder.build();
        List<ProfileEntry> profileEntries = profiles.stream()
            .map(ProfileEntry.Builder::build)
            .toList();
        
        boolean profilesInvalid = profileEntries.isEmpty() || profileEntries.stream().anyMatch(ProfileEntry::isInvalid);
        if(entry.isInvalid() && profilesInvalid){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Unable to parse ServerListPlus configuration of type <white>%s</white>. Generated ProfileEntry was invalid.", type.getName());
            sender.sendErrorMsg(" -> <red>Received invalid Configuration.");
            
            return 0;
        }
        
        Path profile = core.plugin().folderPath().resolve("profiles").resolve(type.getFile());
        if(Files.exists(profile)){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Cannot create new file <white>%s</white>. One with the same name is already present", type.getFile());
            sender.sendErrorMsg(" -> <red>File</red> %s <red>already present.", type.getFile());
            
            return 0;
        }
        
        try{
            Files.createFile(profile);
        }catch(IOException ex){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Encountered an IOException while trying to create file <white>%s</white>.", ex, type.getFile());
            sender.sendErrorMsg(" -> <red>File creation error.");
            
            return 0;
        }
        
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .path(profile)
            .indent(2)
            .nodeStyle(NodeStyle.BLOCK)
            .defaultOptions(opt -> opt.serializers(build -> build.register(ProfileEntry.class, ProfileSerializer.INSTANCE)))
            .build();
        
        ConfigurationNode node;
        try{
            node = loader.load();
        }catch(IOException ex){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Encountered an IOException while trying to load file <white>%s</white>.", ex, type.getFile());
            sender.sendErrorMsg(" -> <red>File loading error.");
            
            return 0;
        }
        
        if(node == null){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Cannot migrate Configuration of type <white>%s</white>. ConfigurationNode was null.", type.getName());
            sender.sendErrorMsg(" -> <red>File loading error.");
            
            return 0;
        }
        
        try{
            node.node("priority")
                .set(type != Type.DEFAULT ? 1 : 0);
            
            if(type == Type.PERSONALIZED){
                node.node("condition")
                    .set("${player name} != \"" + core.fileHandler().getString("Anonymous", "unknownPlayer", "name") + "\"");
            }else
            if(type == Type.BANNED){
                node.node("condition")
                    .set("${player isBanned}");
            }
            
            if(!profiles.isEmpty()){
                node.node("profiles").setList(ProfileEntry.class, profileEntries);
            }
            
            node.set(entry);
        }catch(SerializationException ex){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Encountered a SerializationException while setting values.", ex);
            sender.sendErrorMsg(" -> <red>Error while updating file</red> %s<red>.", type.getFile());
            
            return 0;
        }
        
        try{
            loader.save(node);
            sender.sendPrefixedMsg(" -> <green>Completed!");
            
            return 1;
        }catch(IOException ex){
            logger.warn("[<white>Migrator - ServerListPlus</white>] Encountered an IOException while trying to save new file <white>%s</white>.", ex, type.getFile());
            sender.sendErrorMsg(" -> <red>File saving error.");
            
            return 0;
        }
    }
    
    private static int getMaxListSize(List<?>... lists){
        int size = 0;
        for(List<?> list : lists){
            if(list.size() > size)
                size = list.size();
        }
        
        return size;
    }
    
    private static List<List<String>> resolveMotds(List<String> descriptions){
        if(descriptions == null || descriptions.isEmpty())
            return Collections.emptyList();
        
        List<List<String>> motds = new ArrayList<>();
        for(String line : descriptions){
            motds.add(getList(line));
        }
        
        return motds;
    }
    
    private static List<String> resolveFavicons(PersonalizedStatusConf.StatusConf.FaviconConf faviconConf){
        if(faviconConf == null || faviconConf.Disabled != null && faviconConf.Disabled)
            return Collections.emptyList();
        
        List<String> list = new ArrayList<>();
        
        if(faviconConf.Heads != null){
            for(String head : faviconConf.Heads){
                list.add(
                    "https://mc-heads.net/avatar/" +
                        replacePlaceholders(head) +
                        "/64/nohelm"
                );
            }
        }
        
        if(faviconConf.Helms != null){
            for(String helm : faviconConf.Helms){
                list.add(replacePlaceholders(helm));
            }
        }
        
        if(faviconConf.Files != null){
            for(String file : faviconConf.Files){
                if(!file.toLowerCase(Locale.ROOT).endsWith(".png"))
                    continue;
                
                list.add(replacePlaceholders(file));
            }
        }
        
        return list;
    }
    
    private static List<List<String>> resolveHover(PersonalizedStatusConf.StatusConf.PlayersConf playersConf){
        if(playersConf == null)
            return Collections.emptyList();
        
        BooleanOrList<String> booleanOrList = playersConf.Hover;
        if(booleanOrList == null)
            return Collections.emptyList();
        
        if(booleanOrList.getList() != null){
            List<List<String>> hovers = new ArrayList<>();
            for(String line : booleanOrList.getList()){
                if(line == null)
                    continue;
                
                hovers.add(getList(line));
            }
            
            return hovers;
        }
        
        return Collections.emptyList();
    }
    
    private static List<Integer> resolveMaxPlayers(PersonalizedStatusConf.StatusConf.PlayersConf playersConf){
        if(playersConf == null)
            return Collections.emptyList();
        
        List<IntegerRange> maxPlayers = playersConf.Max;
        if(maxPlayers == null)
            return Collections.emptyList();
        
        List<Integer> values = new ArrayList<>();
        for(IntegerRange range : maxPlayers){
            if(range == null)
                continue;
            
            values.add(range.to());
        }
        
        return values;
    }
    
    private static List<String> resolvePlayerCounts(PersonalizedStatusConf.StatusConf.PlayersConf playersConf){
        if(playersConf == null)
            return Collections.emptyList();
        
        if(playersConf.Slots == null)
            return Collections.emptyList();
        
        return playersConf.Slots.stream()
            .map(SLPConfigMigrator::replacePlaceholders)
            .toList();
    }
    
    private static <T> void apply(List<T> list, ProfileEntry.Builder builder, List<ProfileEntry.Builder> profiles,
                                  BiConsumer<ProfileEntry.Builder, T> builderConsumer){
        if(list.size() == 1){
            builderConsumer.accept(builder, list.get(0));
        }else
        if(list.size() > 1){
            for(int i = 0; i < list.size(); i++){
                builderConsumer.accept(profiles.get(i), list.get(i));
            }
        }
    }
    
    private static List<String> getList(String line){
        return Arrays.asList(replacePlaceholders(line).split("\n"));
    }
    
    private static String replacePlaceholders(String input){
        StringBuilder parsed = new StringBuilder(input);
        StringBuilder builder = new StringBuilder();
        
        Matcher rgbColorMatcher = RGB_COLOR_PATTERN.matcher(parsed.toString());
        if(rgbColorMatcher.find()){
            do{
                rgbColorMatcher.appendReplacement(builder, "<" + rgbColorMatcher.group("color") + ">");
            }while(rgbColorMatcher.find());
            
            rgbColorMatcher.appendTail(builder);
            parsed = builder;
        }
        
        Matcher colorCodeMatcher = COLOR_CODE_PATTERN.matcher(parsed.toString());
        if(colorCodeMatcher.find()){
            builder.setLength(0);
            do{
                for(Map.Entry<String, String> colorCodes : COLOR_TO_MINIMESSAGE_TAG.entrySet()){
                    if(!colorCodeMatcher.group("color").equalsIgnoreCase(colorCodes.getKey()))
                        continue;
                    
                    colorCodeMatcher.appendReplacement(builder, colorCodes.getValue());
                }
            }while(colorCodeMatcher.find());
            
            colorCodeMatcher.appendTail(builder);
            parsed = builder;
        }
        
        Matcher rgbGradientMatcher = RGB_GRADIENT_PATTERN.matcher(parsed.toString());
        if(rgbGradientMatcher.find()){
            builder.setLength(0);
            do{
                String start = rgbGradientMatcher.group("start");
                String end = rgbGradientMatcher.group("end");
                String text = rgbGradientMatcher.group("text");
                
                rgbGradientMatcher.appendReplacement(builder, "<gradient:" + start + ":" + end + ">" + text + "</gradient>");
            }while(rgbGradientMatcher.find());
            
            rgbGradientMatcher.appendTail(builder);
            parsed = builder;
        }
        
        return replaceStaticPlaceholders(parsed);
    }
    
    private static String replaceStaticPlaceholders(StringBuilder input){
        if(input.isEmpty())
            return "";
        
        for(Map.Entry<String, String> entry : STATIC_REPLACEMENTS.entrySet()){
            int index = 0;
            while((index = input.indexOf(entry.getKey(), index)) != -1){
                input.replace(index, index + entry.getKey().length(), entry.getValue());
                index += entry.getValue().length();
            }
        }
        
        return input.toString();
    }
    
    private enum Type{
        DEFAULT("Default", "slp_default.yml"),
        PERSONALIZED("Personalized", "slp_personalized.yml"),
        BANNED("Banned", "slp_banned.yml");
        
        private final String name, file;
        
        Type(String name, String file){
            this.name = name;
            this.file = file;
        }
        
        public String getName(){
            return name;
        }
        
        public String getFile(){
            return file;
        }
    }
}

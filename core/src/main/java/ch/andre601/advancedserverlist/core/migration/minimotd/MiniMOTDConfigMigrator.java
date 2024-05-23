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

package ch.andre601.advancedserverlist.core.migration.minimotd;

import ch.andre601.advancedserverlist.api.objects.NullBool;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.migration.minimotd.serializing.MiniMOTDConfig;
import ch.andre601.advancedserverlist.core.migration.minimotd.serializing.MiniMOTDSerializer;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileSerializer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniMOTDConfigMigrator{
    
    public static boolean migrate(AdvancedServerList<?> core, CmdSender sender){
        core.getPlugin().downloadLibrary("org.spongepowered", "configurate-hocon", "4.1.2");
        
        PluginLogger logger = core.getPlugin().getPluginLogger();
        boolean isVelocity = core.getPlugin().getLoader().equals("velocity");
        
        Path mainConf = core.getPlugin().getFolderPath().getParent().resolve(isVelocity ? "minimotd-velocity" : "MiniMOTD").resolve("main.conf");
        if(!Files.exists(mainConf)){
            logger.warn("[Migrator - MiniMOTD] Cannot find a main.conf file in /plugins/%s/ folder.", isVelocity ? "minimotd-velocity" : "MiniMOTD");
            sender.sendErrorMsg(" -> <red>No main.conf file found.");
            return false;
        }
        
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .path(mainConf)
            .defaultOptions(options -> options.serializers(builder -> 
                builder.register(MiniMOTDConfig.class, MiniMOTDSerializer.MINI_MOTD_SERIALIZER)
                    .register(MiniMOTDConfig.Motd.class, MiniMOTDSerializer.MOTD_SERIALIZER)
                    .register(MiniMOTDConfig.PlayerCount.class, MiniMOTDSerializer.PLAYER_COUNT_SERIALIZER)
            ))
            .build();
        
        MiniMOTDConfig config;
        try{
            ConfigurationNode node = loader.load();
            
            config = node.get(MiniMOTDConfig.class);
        }catch(IOException ex){
            logger.warn("[Migrator - MiniMOTD] Encountered IOException while loading main.conf file.", ex);
            sender.sendErrorMsg(" -> <red>IOException while loading</red> main.conf <red>file.");
            return false;
        }
        
        if(config == null){
            logger.warn("[Migrator - MiniMOTD] Retrieved main.conf file returned null.");
            sender.sendErrorMsg(" -> <red>Received invalid</red> main.conf <red>file.");
            return false;
        }
        
        ProfileEntry.Builder profileBuilder = ProfileEntry.empty().builder();
        List<ProfileEntry.Builder> profilesBuilders = new ArrayList<>();
        
        if(config.motdEnabled() && config.motds().size() > 1){
            for(int i = 0; i < config.motds().size(); i++){
                profilesBuilders.add(ProfileEntry.empty().builder());
            }
        }
        
        if(config.motdEnabled()){
            if(config.motds().size() == 1){
                applyMotd(profileBuilder, config.motds().get(0));
            }else
            if(config.motds().size() > 1){
                for(int i = 0; i < config.motds().size(); i++){
                    applyMotd(profilesBuilders.get(i), config.motds().get(i));
                }
            }
        }
        
        if(config.iconEnabled()){
            if(config.motds().size() == 1){
                MiniMOTDConfig.Motd motd = config.motds().get(0);
                if(!motd.icon().isEmpty() && !motd.icon().equalsIgnoreCase("random")){
                    profileBuilder.favicon(motd.icon());
                }
            }else
            if(config.motds().size() > 1){
                for(int i = 0; i < config.motds().size(); i++){
                    MiniMOTDConfig.Motd motd = config.motds().get(i);
                    if(!motd.icon().isEmpty() && !motd.icon().equalsIgnoreCase("random")){
                        profilesBuilders.get(i).favicon(motd.icon());
                    }
                }
            }
        }
        
        if(config.playerCount() != null){
            MiniMOTDConfig.PlayerCount playerCount = config.playerCount();
            
            NullBool hidePlayers = NullBool.resolve(playerCount.hidePlayers());
            NullBool xMoreEnabled = NullBool.resolve(playerCount.xMoreEnabled());
            int xMore = playerCount.xMore();
            NullBool maxPlayersEnabled = NullBool.resolve(playerCount.maxPlayersEnabled());
            int maxPlayers = playerCount.maxPlayers();
            
            profileBuilder.hidePlayersEnabled(hidePlayers)
                .extraPlayersEnabled(xMoreEnabled)
                .extraPlayersCount(xMore)
                .maxPlayersEnabled(maxPlayersEnabled)
                .maxPlayersCount(maxPlayers);
        }
        
        ProfileEntry entry = profileBuilder.build();
        List<ProfileEntry> profiles = profilesBuilders.stream()
            .map(ProfileEntry.Builder::build)
            .toList();
        
        boolean profilesInvalid = profiles.isEmpty() || profiles.stream().anyMatch(ProfileEntry::isInvalid);
        if(entry.isInvalid() && profilesInvalid){
            logger.warn("[Migrator - MiniMOTD] Main ProfileEntry was invalid and profiles were empty.");
            sender.sendErrorMsg(" -> <red>Received invalid configuration.");
            return false;
        }
        
        Path profile = core.getPlugin().getFolderPath().resolve("profiles").resolve("minimotd_migrated.yml");
        if(Files.exists(profile)){
            logger.warn("[Migrator - MiniMOTD] Cannot create new file minimotd_migrated.yml. One with the same name is already present.");
            sender.sendErrorMsg(" -> <red>File</red> minimotd_migrated.yml <red>already present.");
            
            return false;
        }
        
        try{
            Files.createFile(profile);
        }catch(IOException ex){
            logger.warn("[Migrator - MiniMOTD] Encountered an IOException while trying to create file minimotd_migrated.yml.", ex);
            sender.sendErrorMsg(" -> <red>File creation error.");
            
            return false;
        }
        
        YamlConfigurationLoader yamlLoader = YamlConfigurationLoader.builder()
            .path(profile)
            .indent(2)
            .nodeStyle(NodeStyle.BLOCK)
            .defaultOptions(option -> option.serializers(builder -> builder.register(ProfileEntry.class, ProfileSerializer.INSTANCE)))
            .build();
        
        ConfigurationNode migrated;
        try{
            migrated = yamlLoader.load();
        }catch(IOException ex){
            logger.warn("[Migrator - MiniMOTD] Encountered an IOException while trying to load file minimotd_migrated.yml.", ex);
            sender.sendErrorMsg(" -> <red>File loading error.");
            
            return false;
        }
        
        if(migrated == null){
            logger.warn("[Migrator - MiniMOTD] Cannot migrate Configuration. ConfiguationNode was null.");
            sender.sendErrorMsg(" -> <red>File loading error.");
            
            return false;
        }
        
        try{
            migrated.node("priority").set(0);
            
            if(!profiles.isEmpty()){
                migrated.node("profiles").setList(ProfileEntry.class, profiles);
            }
            
            migrated.set(entry);
        }catch(SerializationException ex){
            logger.warn("[Migrator - MiniMOTD] Encountered a SerializationException while setting values.", ex);
            sender.sendErrorMsg(" -> <red>Error while updating file</red> minimotd_migrated.yml<red>.");
            
            return false;
        }
        
        try{
            yamlLoader.save(migrated);
            sender.sendPrefixedMsg(" -> <green>Completed!");
            
            return true;
        }catch(IOException ex){
            logger.warn("[Migrator - MiniMOTD] Encountered an IOException while trying to save new file minimotd_migrated.yml.", ex);
            sender.sendErrorMsg(" -> <red>File saving error.");
            
            return false;
        }
    }
    
    private static void applyMotd(ProfileEntry.Builder builder, MiniMOTDConfig.Motd motd){
        if(motd.line1().isEmpty() && !motd.line2().isEmpty()){
            builder.motd(List.of("<grey>", motd.line2()));
        }else
        if(!motd.line1().isEmpty() && motd.line2().isEmpty()){
            builder.motd(Collections.singletonList(motd.line1()));
        }else
        if(!motd.line1().isEmpty()){ // Line2 is by logic not empty here. So this check is not required.
            builder.motd(List.of(motd.line1(), motd.line2()));
        }
    }
}

/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Andre_601
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
 */

package ch.andre601.advancedserverlist.core.commands;

import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.migration.minimotd.MiniMOTDConfigMigrator;
import ch.andre601.advancedserverlist.core.migration.serverlistplus.SLPConfigMigrator;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileSerializer;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandInput;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Command("advancedsercerlist|asl")
@CommandDescription("Main command of the plugin.")
public class CommandHandler<F, S extends CmdSender>{
    
    private final AdvancedServerList<F, S> core;
    
    public CommandHandler(AdvancedServerList<F, S> core){
        this.core = core;
    }
    
    @Command("reload")
    @CommandDescription("Reloads the config.yml and all existing profiles.")
    public void reload(CmdSender sender){
        sender.sendPrefixedMsg("Reloading plugin...");
        
        if(core.getFileHandler().reloadConfig()){
            sender.sendPrefixedMsg("<green>Reloaded </green>config.yml<green>!");
        }else{
            sender.sendErrorMsg("<red>Error while reloading </red>config.yml<red>.");
            sender.sendErrorMsg("<red>Check console for details.");
        }
        
        if(core.getFileHandler().reloadProfiles()){
            sender.sendPrefixedMsg("<green>(Re)loaded </green>%d<green> Profile(s)!", core.getFileHandler().getProfiles().size());
        }else{
            sender.sendErrorMsg("<red>Errpr while (re)loading profiles.");
            sender.sendErrorMsg("<red>Check console for details.");
        }
        
        sender.sendPrefixedMsg("<green>Reload complete!");
    }
    
    @Command("clearcache")
    @CommandDescription("Clears the Favicon and Player cache of the plugin.")
    public void clearCache(CmdSender sender){
        sender.sendPrefixedMsg("Clearing cache...");
        
        core.clearFaviconCache();
        sender.sendPrefixedMsg("Cleared Favicon cache.");
        
        core.clearPlayerCache();
        sender.sendPrefixedMsg("Cleared Player cache.");
        
        sender.sendPrefixedMsg("<green>Cache clearing complete!");
    }
    
    @Command("migrate <plugin>")
    @CommandDescription("Migrates Profiles from other plugins to itself.")
    public void migrate(
        CmdSender sender,
        @Nonnull @Argument(description = "The plugin to migrate from.", suggestions = "plugin") String plugin
    ){
        if(plugin.isEmpty()){
            sender.sendErrorMsg("<red>Insufficient arguments. Please provide a supported plugin to migrate from.");
            sender.sendMsg("<grey>[Click the plugin name for the command]");
            sender.sendMsg();
            sender.sendMsg(" <grey>-</grey> <click:suggest_command:/asl migrate serverlistplus>ServerListPlus</click>");
            sender.sendMsg(" <grey>-</grey> <click:suggest_command:/asl migrate minimotd>MiniMOTD</click>");
            return;
        }
        
        if(plugin.equalsIgnoreCase("serverlistplus")){
            if(!core.getPlugin().isPluginEnabled("ServerListPlus")){
                sender.sendErrorMsg("<red>ServerListPlus is not enabled.");
                sender.sendErrorMsg("<red>The plugin is required for the migration to work.");
                return;
            }
            
            sender.sendPrefixedMsg("Migrating from ServerListPlus...");
            
            int migrated = SLPConfigMigrator.migrate(core, sender);
            if(migrated == 0){
                sender.sendErrorMsg("<red>Unable to migrate any profiles from ServerListPlus.");
                sender.sendErrorMsg("<red>Check console for details.");
            }else{
                sender.sendPrefixedMsg("<green>Successfully migrated </green>%d<green> Profile(s)", migrated);
            }
        }else
        if(plugin.equalsIgnoreCase("minimotd")){
            sender.sendPrefixedMsg("Migrating from MiniMOTD...");
            
            if(MiniMOTDConfigMigrator.migrate(core, sender)){
                sender.sendPrefixedMsg("<green>Successfully migrated from MiniMOTD!");
            }else{
                sender.sendErrorMsg("<red>Unable to migrate from MiniMOTD.");
                sender.sendErrorMsg("<red>Check console for details.");
            }
        }else{
            sender.sendErrorMsg("<red>Received invalid Plugin name </red>%s<red>.", plugin);
            sender.sendMsg("<grey>Supported plugins [Click for command]:");
            sender.sendMsg();
            sender.sendMsg(" <grey>-</grey> <click:suggest_command:/asl migrate serverlistplus>ServerListPlus</click>");
            sender.sendMsg(" <grey>-</grey> <click:suggest_command:/asl migrate minimotd>MiniMOTD</click>");
        }
    }
    
    @Suggestions("plugin")
    public List<String> pluginsSuggestions(CommandInput commandInput){
        String plugin = commandInput.readString();
        if(plugin.isEmpty())
            return List.of("ServerListPlus", "MiniMOTD");
        
        List<String> matches = new ArrayList<>(2);
        for(String str : List.of("ServerListPlus", "MiniMOTD")){
            if(str.length() >= plugin.length() && str.regionMatches(0, plugin, 0, plugin.length()))
                matches.add(str);
        }
        
        return matches;
    }
    
    @Command("profiles")
    @CommandDescription("Subcommand to list, add and copy profiles.")
    public record Profiles<F, S extends CmdSender>(AdvancedServerList<F, S> core){
        @Command("list")
        @CommandDescription("Lists all available profiles with their priority, conditions and if they are valid.")
        public void list(CmdSender sender){
            sender.sendPrefixedMsg("Available Profiles");
            sender.sendMsg("<grey>[Hover for details]");
            sender.sendMsg();
            
            core.getFileHandler().getProfiles().stream()
                .sorted(Comparator.comparingInt(ServerListProfile::priority).reversed())
                .forEach(
                    profile -> sender.sendMsg(" <grey>-</grey> <hover:show_text:\"%s\">%s</hover>", hover(profile), profile.file())
                );
        }
        
        @Command("add <name>")
        @CommandDescription("Creates a new profile with default values applied.")
        public void add(
            CmdSender sender,
            @Nonnull  @Argument(description = "Name of the profile to create.") String name
        ){
            if(name.isEmpty()){
                sender.sendErrorMsg("<red>Insufficient arguments.");
                sender.sendErrorMsg("<red>Please provide a proper file name.");
                return;
            }
            
            String fileName = getProfileName(name);
            
            if(core.getFileHandler().getProfiles().stream().anyMatch(profile -> profile.file().equalsIgnoreCase(fileName))){
                sender.sendErrorMsg("<red>A profile with name </red>%s<red> already exists.", name);
                return;
            }
            
            if(core.getFileHandler().createFile(fileName)){
                sender.sendPrefixedMsg("<green>Successfully created </green>%s<green>!", fileName);
                sender.sendPrefixedMsg("<green>Use </green><click:suggest_command:/asl reload>/asl reload</click><green> to load the file.");
            }else{
                sender.sendErrorMsg("<red>Error while creating new profile.");
                sender.sendErrorMsg("<red>Check console for details.");
            }
        }
        
        @Command("copy <profile> <name>")
        @CommandDescription("Creates a copy of an existing profile.")
        public void copy(
            CmdSender sender,
            @Nonnull @Argument(description = "Name of the profile to copy.", suggestions = "profile") String profile,
            @Nonnull @Argument("Name of the copy.") String name
        ){
            if(profile.isEmpty() || name.isEmpty()){
                sender.sendErrorMsg("<red>Insufficient arguments.");
                sender.sendErrorMsg("<red>Please provide a profile name and name for the copy.");
                return;
            }
            
            String profileName = getProfileName(profile);
            String copyName = getProfileName(name);
            
            ServerListProfile targetProfile = core.getFileHandler().getProfiles().stream()
                .filter(p -> p.file().equalsIgnoreCase(profileName))
                .findFirst()
                .orElse(null);
            boolean copyExists = core.getFileHandler().getProfiles().stream()
                .anyMatch(p -> p.file().equalsIgnoreCase(copyName));
            
            if(targetProfile == null){
                sender.sendErrorMsg("<red>No profile with name </red>%s<red> exists.", profile);
                return;
            }
            
            if(copyExists){
                sender.sendErrorMsg("<red>A file with name </red>%<red> already exists!", copyName);
                return;
            }
            
            Path profilePath = core.getPlugin().getFolderPath().resolve("profiles").resolve(copyName);
            if(!Files.exists(profilePath)){
                try{
                    Files.createFile(profilePath);
                }catch(IOException ex){
                    sender.sendErrorMsg("<red>Encountered IOException while creating file </red>%s<red>.", copyName);
                    sender.sendErrorMsg("<red>Check console for details.");
                    
                    core.getPlugin().getPluginLogger().warn("Encountered IOException while creating file <white>%s</white>", ex, copyName);
                    return;
                }
            }
            
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(profilePath)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(opt -> opt.serializers(builder -> builder.register(ProfileEntry.class, ProfileSerializer.INSTANCE)))
                .build();
            
            ConfigurationNode node;
            try{
                node = loader.load();
            }catch(IOException ex){
                sender.sendErrorMsg("<red>There was an IOException while loading file </red>%s<red>!", copyName);
                sender.sendErrorMsg("<red>Check console for details.");
                
                core.getPlugin().getPluginLogger().warn("Encountered IOException while loading file <white>%s</white>", ex, copyName);
                return;
            }
            
            if(node == null){
                sender.sendErrorMsg("<red>Cannot create ConfigurationNode for file </red>%s<red>", copyName);
                return;
            }
            
            try{
                node.node("priority").set(targetProfile.priority());
                node.node("condition").set(targetProfile.condition());
                
                node.node("profiles").setList(ProfileEntry.class, targetProfile.profiles());
                
                node.set(ProfileEntry.class, targetProfile.defaultProfile());
            }catch(SerializationException ex){
                sender.sendErrorMsg("<red>There was a SerializationException while copying values from </red>%s<red>.", profile);
                sender.sendErrorMsg("<red>Check console for details.");
                
                core.getPlugin().getPluginLogger().warn("Encountered IOException while copying data from <white>%s</white> to <white>%s</white>.", ex, profileName, copyName);
                return;
            }
            
            try{
                loader.save(node);
                sender.sendPrefixedMsg("<green>Successfully made copy of </green>%s<green> as </green>%s<green>!", profile, copyName);
                sender.sendPrefixedMsg("<green>Use </green><click:suggest_command:/asl reload>/asl reload</click><green> to load the file.");
            }catch(IOException ex){
                sender.sendErrorMsg("<red>There was an IOException while loading file </red>%s<red>!", copyName);
                sender.sendErrorMsg("<red>Check console for details.");
                
                core.getPlugin().getPluginLogger().warn("Encountered IOException while loading file <white>%s</white>", ex, copyName);
            }
        }
        
        @Suggestions("profile")
        public List<String> profilesSuggestions(CommandInput input){
            String name = input.readString();
            List<String> profiles = core.getFileHandler().getProfiles().stream()
                .map(profile -> profile.file().substring(0, profile.file().lastIndexOf('.')))
                .toList();
            if(name.isEmpty())
                return profiles;
            
            List<String> matches = new ArrayList<>(profiles.size());
            for(String str : profiles){
                if(str.length() >= name.length() && str.regionMatches(true, 0, name, 0, name.length()))
                    matches.add(str);
            }
            
            return matches;
        }
        
        private String hover(ServerListProfile profile){
            return """
                <grey>Priority: <white>%d</white>
                Condition: <white>%s</white>
                
                Is valid? %s
                """.formatted(
                    profile.priority(),
                    profile.condition() == null || profile.condition().isEmpty() ? "<i>None</i>" : profile.condition(),
                    profile.isInvalidProfile() ? "<red>x</red>" : "<green>✓</green>"
                );
        }
        
        private String getProfileName(String name){
            if(name == null || name.isEmpty())
                return "";
            
            String filename = name.toLowerCase(Locale.ROOT);
            
            return filename.endsWith(".yml") ? filename : filename + ".yml";
        }
    }
}

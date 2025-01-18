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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.execution.CommandExecutionHandler;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Locale;

public class CommandHandler{
    
    public static <F, S extends CmdSender> Reload<F, S> initReload(AdvancedServerList<F, S> core){
        return new Reload<>(core);
    }
    
    public static <F, S extends CmdSender> ClearCache<F, S> initClearCache(AdvancedServerList<F, S> core){
        return new ClearCache<>(core);
    }
    
    public static <F, S extends CmdSender> Migrate<F, S> initMigrate(AdvancedServerList<F, S> core){
        return new Migrate<>(core);
    }
    
    public static <F, S extends CmdSender> ProfilesList<F, S> initProfilesList(AdvancedServerList<F, S> core){
        return new ProfilesList<>(core);
    }
    
    public static <F, S extends CmdSender> ProfilesAdd<F, S> initProfilesAdd(AdvancedServerList<F, S> core){
        return new ProfilesAdd<>(core);
    }
    
    public static <F, S extends CmdSender> ProfilesCopy<F, S> initProfilesCopy(AdvancedServerList<F, S> core){
        return new ProfilesCopy<>(core);
    }
    
    private static String getProfileName(String name){
        if(name == null || name.isEmpty())
            return "";
        
        String filename = name.toLowerCase(Locale.ROOT);
        
        return filename.endsWith(".yml") ? filename : filename + ".yml";
    }
    
    private record Reload<F, S extends CmdSender>(AdvancedServerList<F, S> core) implements CommandExecutionHandler<S>{
        @Override
        public void execute(@NonNull CommandContext<S> commandContext){
            CmdSender sender = commandContext.sender();
            
            sender.sendPrefixedMsg("Reloading plugin...");
            
            if(core.getFileHandler().reloadConfig()){
                sender.sendPrefixedMsg("<green>Reloaded</green> config.yml</green>!");
            }else{
                sender.sendErrorMsg("<red>Error while reloading</red> config.yml<red>!");
            }
            
            if(core.getFileHandler().reloadProfiles()){
                sender.sendPrefixedMsg("<green>(Re)loaded</green> %d <green>Profile(s)!", core.getFileHandler().getProfiles().size());
            }else{
                sender.sendErrorMsg("<red>Error while (re)loading profile(s)!");
            }
            
            sender.sendPrefixedMsg("<green>Reload complete!");
        }
    }
    
    private record ClearCache<F, S extends CmdSender>(AdvancedServerList<F, S> core) implements CommandExecutionHandler<S>{
        @Override
        public void execute(@NonNull CommandContext<S> commandContext){
            CmdSender sender = commandContext.sender();
            
            sender.sendPrefixedMsg("Clearing cache...");
            
            core.clearFaviconCache();
            sender.sendPrefixedMsg("<green>Successfully cleared Favicon cache!");
            
            
            core.clearPlayerCache();
            sender.sendPrefixedMsg("<green>Successfully cleared Player Cache!");
            
            sender.sendPrefixedMsg("<green>Cache clearing complete!");
        }
    }
    
    private record Migrate<F, S extends CmdSender>(AdvancedServerList<F, S> core) implements CommandExecutionHandler<S>{
        @Override
        public void execute(@NonNull CommandContext<S> commandContext){
            CmdSender sender = commandContext.sender();
            String plugin = commandContext.get("plugin");
            
            if(plugin.isEmpty()){
                sender.sendErrorMsg("<red>Insufficient arguments. Please provide a supported plugin to migrate from.");
                sender.sendErrorMsg("[Click the name for the command]");
                sender.sendErrorMsg("");
                sender.sendErrorMsg(" - <click:suggest_command:/asl migrate serverlistplus>ServerListPlus</click>");
                sender.sendErrorMsg(" - <click:suggest_command:/asl migrate minimotd>MiniMOTD</click>");
                return;
            }
            
            if(plugin.equalsIgnoreCase("serverlistplus")){
                if(!core.getPlugin().isPluginEnabled("ServerListPlus")){
                    sender.sendErrorMsg("<red>ServerListPlus is not enabled!");
                    sender.sendErrorMsg("<red>The plugin is required for the migration to work.");
                    return;
                }
                
                sender.sendPrefixedMsg("Migrating from ServerListPlus...");
                
                int migrated = SLPConfigMigrator.migrate(core, sender);
                if(migrated == 0){
                    sender.sendErrorMsg("<red>Unable to migrate any profiles from ServerListPlus.");
                    sender.sendErrorMsg("<red>Check console for errors.");
                }else{
                    sender.sendPrefixedMsg("<green>Successfully migrated</green> %d <green>Profile(s)!", migrated);
                }
            }else
            if(plugin.equalsIgnoreCase("minimotd")){
                sender.sendPrefixedMsg("Migrating from MiniMOTD...");
                
                if(MiniMOTDConfigMigrator.migrate(core, sender)){
                    sender.sendPrefixedMsg("<green>Successfully migrated from MiniMOTD!");
                }else{
                    sender.sendErrorMsg("<red>Unable to migrate from MiniMOTD.");
                    sender.sendErrorMsg("<red>Check console for errors.");
                }
            }else{
                sender.sendErrorMsg("<red>Unknown plugin</red> %s<red>.");
                sender.sendErrorMsg("<red>Supported options</red> [Click name for command]");
                sender.sendErrorMsg("");
                sender.sendErrorMsg(" - <click:suggest_command:/asl migrate serverlistplus>ServerListPlus</click>");
                sender.sendErrorMsg(" - <click:suggest_command:/asl migrate minimotd>MiniMOTD</click>");
            }
        }
    }
    
    private record ProfilesList<F, S extends CmdSender>(AdvancedServerList<F, S> core) implements CommandExecutionHandler<S>{
        @Override
        public void execute(@NonNull CommandContext<S> commandContext){
            CmdSender sender = commandContext.sender();
            
            sender.sendPrefixedMsg("Available Profiles [Hover for details]:");
            core.getFileHandler().getProfiles().stream()
                .sorted(Comparator.comparingInt(ServerListProfile::priority).reversed())
                .forEach(
                    profile -> sender.sendMsg("<grey>- <white><hover:show_text:\"%s\">%s</hover></white>", getHover(profile), profile.file())
                );
        }
        
        private String getHover(ServerListProfile profile){
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
    }
    
    private record ProfilesAdd<F, S extends CmdSender>(AdvancedServerList<F, S> core) implements CommandExecutionHandler<S>{
        @Override
        public void execute(@NonNull CommandContext<S> commandContext){
            CmdSender sender = commandContext.sender();
            String name = getProfileName(commandContext.get("name"));
            
            if(name.isEmpty()){
                sender.sendErrorMsg("<red>Insufficient arguments.");
                sender.sendErrorMsg("<red>Please provide a proper name to use.");
                return;
            }
            
            if(core.getFileHandler().getProfiles().stream().anyMatch(profile -> profile.file().equalsIgnoreCase(name))){
                sender.sendErrorMsg("<red>A profile with file name</red> %s <red>already exists!", name);
                return;
            }
            
            if(core.getFileHandler().createFile(name)){
                sender.sendPrefixedMsg("<green>Successfully created</green> %s<green>!", name);
                sender.sendPrefixedMsg("<green>Load the file using</green> <click:suggest_command:/asl reload>/asl reload</click>");
            }else{
                sender.sendErrorMsg("<red>Error while trying to create file</red> %s<red>!", name);
                sender.sendErrorMsg("<red>Check console for details.");
            }
        }
    }
    
    private record ProfilesCopy<F, S extends CmdSender>(AdvancedServerList<F, S> core) implements CommandExecutionHandler<S>{
        @Override
        public void execute(@NonNull CommandContext<S> commandContext){
            CmdSender sender = commandContext.sender();
            
            String name = getProfileName("profile");
            String copy = getProfileName("name");
            
            if(name.isEmpty() || copy.isEmpty()){
                sender.sendErrorMsg("<red>Insufficient arguments!");
                sender.sendErrorMsg("<red>Please provide a proper file name and name for the copy.");
                return;
            }
            
            ServerListProfile targetProfile = core.getFileHandler().getProfiles().stream()
                .filter(profile -> profile.file().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
            boolean copyExists = core.getFileHandler().getProfiles().stream()
                .anyMatch(profile -> profile.file().equalsIgnoreCase(copy) && !commandContext.flags().hasFlag("override"));
            
            if(targetProfile == null){
                sender.sendErrorMsg("<red>No file with name</red> %s <red>found!", name);
                return;
            }
            
            if(copyExists){
                sender.sendErrorMsg("<red>A file with name</red> %s <red>already exists!");
                sender.sendErrorMsg("<red>To force an override, add</red> --override <red>to the command!");
                return;
            }
            
            Path profilePath = core.getPlugin().getFolderPath().resolve("profiles").resolve(copy);
            if(!Files.exists(profilePath)){
                try{
                    Files.createFile(profilePath);
                }catch(IOException ex){
                    sender.sendErrorMsg("<red>There was an IOException while creating file</red> %s<red>!", copy);
                    sender.sendErrorMsg("<red>See console for details.");
                    
                    core.getPlugin().getPluginLogger().warn("Encountered IOException while creating file <white>%s</white>", ex, copy);
                    return;
                }
            }
            
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(profilePath)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options -> options.serializers(builder -> builder.register(ProfileEntry.class, ProfileSerializer.INSTANCE)))
                .build();
            
            ConfigurationNode node;
            try{
                node = loader.load();
            }catch(IOException ex){
                sender.sendErrorMsg("<red>There was an IOException while loading file</red> %s<red>!", copy);
                sender.sendErrorMsg("<red>See console for details.");
                
                core.getPlugin().getPluginLogger().warn("Encountered IOException while loading file <white>%s</white>", ex, copy);
                return;
            }
            
            if(node == null){
                sender.sendErrorMsg("<red>Cannot copy</red> %s <red>to</red> %s<red>. Retrieved ConfigurationNode was null.", name, copy);
                return;
            }
            
            try{
                loader.save(node);
                sender.sendPrefixedMsg("<green>Successfully created copy</green> %s <green>of</green> %s<green>!", copy, name);
                sender.sendPrefixedMsg("<green>Execute</green> <click:suggest_command:/asl reload>/asl reload</click> <green>to load the file.");
            }catch(IOException ex){
                sender.sendErrorMsg("<red>There was an IOException while loading file</red> %s<red>!", copy);
                sender.sendErrorMsg("<red>See console for details.");
                
                core.getPlugin().getPluginLogger().warn("Encountered IOException while loading file <white>%s</white>", ex, copy);
            }
        }
    }
}

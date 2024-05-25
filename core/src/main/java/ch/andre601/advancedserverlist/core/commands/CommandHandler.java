/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
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

package ch.andre601.advancedserverlist.core.commands;

import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.interfaces.commands.PluginCommand;
import ch.andre601.advancedserverlist.core.migration.minimotd.MiniMOTDConfigMigrator;
import ch.andre601.advancedserverlist.core.migration.serverlistplus.SLPConfigMigrator;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileSerializer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CommandHandler{
    
    private final List<PluginCommand> subCommands;
    
    public CommandHandler(AdvancedServerList<?> core){
        subCommands = List.of(
            new Help(core),
            new Reload(core),
            new ClearCache(core),
            new Migrate(core),
            new Profiles(core)
        );
    }
    
    public List<PluginCommand> getSubCommands(){
        return subCommands;
    }
    
    public void handle(CmdSender sender, String[] args){
        if(args.length == 0){
            sender.sendErrorMsg("<red>Too few arguments! Use <grey>/asl help</grey> for a list of commands.");
            return;
        }
        
        for(PluginCommand subCommand : subCommands){
            if(subCommand.name().equalsIgnoreCase(args[0])){
                if(!sender.hasPermission(subCommand.permission())){
                    sender.sendErrorMsg(
                        "<red>You do not have the permission <grey>%s</grey> to execute this command.",
                        subCommand.permission()
                    );
                    return;
                }
                
                subCommand.handle(sender, Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }
        
        sender.sendErrorMsg("<red>Unknown subcommand <grey>%s</grey>. Use <grey>/asl help</grey> for a list of commands.", args[0]);
    }
    
    private static class Help extends PluginCommand{
        
        private final AdvancedServerList<?> core;
        
        public Help(AdvancedServerList<?> core){
            super("help");
            
            this.core = core;
        }
    
        @Override
        public void handle(CmdSender sender, String[] args){
            sender.sendPrefixedMsg("- Commands. Hover for details.");
            for(PluginCommand command : core.getCommandHandler().getSubCommands()){
                sender.sendPrefixedMsg("");
                sender.sendPrefixedMsg(
                    "<aqua><hover:show_text:\"%s\">" +
                    "<click:suggest_command:/asl %s>" +
                    "/asl <white>%s</white>" +
                    "</click>" +
                    "</hover></aqua>",
                    getCommandHover(command),
                    command.name(),
                    command.name()
                );
            }
        }
        
        @Override
        public String usage(){
            return "<aqua>/asl <white>help</white></aqua>";
        }
        
        @Override
        public String description(){
            return "Shows all available commands.";
        }
        
        private String getCommandHover(PluginCommand command){
            return String.format(
                """
                <grey>Usage: %s
                Permission: <white>%s</white>
                
                <white>%s</white>
                """,
                command.usage(),
                command.permission(),
                command.description()
            );
        }
    }
    
    private static class Reload extends PluginCommand{
        
        private final AdvancedServerList<?> core;
        
        public Reload(AdvancedServerList<?> core){
            super("reload");
            
            this.core = core;
        }
        
        @Override
        public void handle(CmdSender sender, String[] args){
            sender.sendPrefixedMsg("Reloading plugin...");
            
            if(core.getFileHandler().reloadConfig()){
                sender.sendPrefixedMsg("<green>Reloaded <grey>config.yml</grey>!");
            }else{
                sender.sendErrorMsg("<red>Error while reloading <grey>config.yml</grey>!");
            }
            
            if(core.getFileHandler().reloadProfiles()){
                sender.sendPrefixedMsg("<green>Loaded <grey>%d profile(s)</grey>!", core.getFileHandler().getProfiles().size());
            }else{
                sender.sendErrorMsg("<red>Error while loading profile(s)!");
            }
            
            sender.sendPrefixedMsg("<green>Reload complete!");
        }
        
        @Override
        public String usage(){
            return "<aqua>/asl <white>reload</white></aqua>";
        }
        
        @Override
        public String description(){
            return "Reloads the main config and all profiles.";
        }
    }
    
    private static class ClearCache extends PluginCommand{
        
        private final AdvancedServerList<?> core;
        
        public ClearCache(AdvancedServerList<?> core){
            super("clearcache");
            
            this.core = core;
        }
    
        @Override
        public void handle(CmdSender sender, String[] args){
            sender.sendPrefixedMsg("Clearing caches...");
            
            core.clearFaviconCache();
            sender.sendPrefixedMsg("<green>Successfully cleared Favicon Cache!");
            
            core.clearPlayerCache();
            sender.sendPrefixedMsg("<green>Successfully cleared Player Cache!");
            
            sender.sendPrefixedMsg("<green>Cache clearing complete!");
        }
        
        @Override
        public String usage(){
            return "<aqua>/asl <white>clearcache</white></aqua>";
        }
        
        @Override
        public String description(){
            return "Clears the Favicon and Player Cache.";
        }
    }
    
    private static class Migrate extends PluginCommand{
        
        private final AdvancedServerList<?> core;
        
        public Migrate(AdvancedServerList<?> core){
            super("migrate");
            
            this.core = core;
        }
        
        @Override
        public void handle(CmdSender sender, String[] args){
            if(args.length == 0){
                sender.sendErrorMsg("<red>Insufficient arguments! Please provide a plugin to migrate from.");
                sender.sendErrorMsg("<red>Available options:");
                sender.sendErrorMsg(" - ServerListPlus");
                sender.sendErrorMsg(" - MiniMOTD");
                return;
            }
            
            if(args[0].equalsIgnoreCase("serverlistplus")){
                if(!core.getPlugin().isPluginEnabled("ServerListPlus")){
                    sender.sendErrorMsg("<red>Plugin ServerListPlus is not enabled.");
                    sender.sendErrorMsg("<red>It needs to be active for the migration to work!");
                    return;
                }
                
                sender.sendPrefixedMsg("Migrating ServerListPlus configuration file...");
                
                int migrated = SLPConfigMigrator.migrate(core, sender);
                if(migrated == 0){
                    sender.sendErrorMsg("<red>Unable to migrate any ServerListPlus configuration. Check console for details!");
                }else{
                    sender.sendPrefixedMsg("<green>Migration complete!");
                }
            }else
            if(args[0].equalsIgnoreCase("minimotd")){
                sender.sendPrefixedMsg("Migrating MiniMOTD configuration file...");
                
                if(MiniMOTDConfigMigrator.migrate(core, sender)){
                    sender.sendPrefixedMsg("<green>Migration complete!");
                }else{
                    sender.sendErrorMsg("<red>Unable to migrate MiniMOTD configuration. Check console for details!");
                }
            }else{
                sender.sendErrorMsg("<red>Unknown plugin <grey>%s</grey>. Available Options:", args[0]);
                sender.sendErrorMsg(" - ServerListPlus");
                sender.sendErrorMsg(" - MiniMOTD");
            }
        }
        
        @Override
        public String usage(){
            return "<aqua>/asl <white>migrate <grey>\\<</grey>Plugin<grey>></grey></white></aqua>";
        }
        
        @Override
        public String description(){
            return "Migrates the configuration from supported Plugins to AdvancedServerList.";
        }
    }
    
    private static class Profiles extends PluginCommand{
        
        private final AdvancedServerList<?> core;
        
        public Profiles(AdvancedServerList<?> core){
            super("profiles");
            
            this.core = core;
        }
        
        @Override
        public void handle(CmdSender sender, String[] args){
            if(args.length == 0){
                sender.sendErrorMsg("<red>Invalid command usage.");
                sender.sendErrorMsg("<red>Usage:</red> %s", usage());
                return;
            }
            
            switch(args[0].toLowerCase(Locale.ROOT)){
                case "list" -> {
                    List<ServerListProfile> profiles = core.getFileHandler().getProfiles().stream()
                        .sorted(Comparator.comparing(ServerListProfile::priority).reversed())
                        .toList();
                    
                    sender.sendPrefixedMsg("Available Profiles:");
                    for(ServerListProfile profile : profiles){
                        sender.sendPrefixedMsg("- <white><hover:show_text:\"%s\">%s</hover></white>", getHover(profile), profile.file());
                    }
                }
                case "add" -> {
                    if(args.length == 1){
                        sender.sendErrorMsg("<red>Insufficient arguments.");
                        sender.sendErrorMsg("<red>Usage:</red> /asl profiles add <name>");
                        return;
                    }
                    
                    String name = getProfileName(args[1]);
                    
                    if(core.getFileHandler().getProfiles().stream().anyMatch(profile -> profile.file().equalsIgnoreCase(name))){
                        sender.sendErrorMsg("<red>A profile with file name</red> %s <red>already exists!");
                        return;
                    }
                    
                    if(core.getFileHandler().createFile(name)){
                        sender.sendPrefixedMsg("<green>Successfully created</green> %s<green>!", name);
                        sender.sendPrefixedMsg("<green>Load the file using</green> /asl reload");
                    }else{
                        sender.sendErrorMsg("<red>Error while trying to create</red> %s<red>!", name);
                        sender.sendErrorMsg("<red>Check console for further details.");
                    }
                }
                case "copy" -> {
                    if(args.length < 3){
                        sender.sendErrorMsg("<red>Insufficient arguments.");
                        sender.sendErrorMsg("<red>Usage:</red> /asl profiles copy <profile> <name>");
                        return;
                    }
                    
                    String name = getProfileName(args[1]);
                    String copy = getProfileName(args[2]);
                    
                    ServerListProfile targetProfile = core.getFileHandler().getProfiles().stream()
                        .filter(file -> file.file().equalsIgnoreCase(name))
                        .findFirst()
                        .orElse(null);
                    boolean copyExists = core.getFileHandler().getProfiles().stream()
                        .anyMatch(profile -> profile.file().equals(copy));
                    
                    if(targetProfile == null){
                        sender.sendErrorMsg("<red>There is no file with name</red> %s<red>!", name);
                        return;
                    }
                    
                    if(copyExists){
                        sender.sendErrorMsg("<red>A profile with file name</red> %s <red>already exists!", copy);
                        return;
                    }
                    
                    Path profilePath = core.getPlugin().getFolderPath().resolve("profiles").resolve(copy);
                    try{
                        Files.createFile(profilePath);
                    }catch(IOException ex){
                        sender.sendErrorMsg("<red>There was an error while creating the file</red> %s<red>!");
                        sender.sendErrorMsg("<red>Check console for further details.");
                        
                        core.getPlugin().getPluginLogger().warn("Encountered IOException while creating file %s", ex, copy);
                        return;
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
                        sender.sendErrorMsg("<red>There was an error while loading the file</red> %s<red>!");
                        sender.sendErrorMsg("<red>Check console for further details.");
                        
                        core.getPlugin().getPluginLogger().warn("Encountered IOException while loading file %s", ex, copy);
                        return;
                    }
                    
                    if(node == null){
                        sender.sendErrorMsg("Cannot copy</red> %s <red>to</red> %s<red>. ConfigurationNode was null.", name, copy);
                        return;
                    }
                    
                    try{
                        node.node("priority").set(targetProfile.priority());
                        node.node("condition").set(targetProfile.condition());
                        
                        node.node("profiles").setList(ProfileEntry.class, targetProfile.profiles());
                        
                        node.set(ProfileEntry.class, targetProfile.defaultProfile());
                    }catch(SerializationException ex){
                        sender.sendErrorMsg("<red>There was an Error while copying values from</red> %s <red>to</red> %s<red>.", name, copy);
                        sender.sendErrorMsg("<red>Check console for details.");
                        return;
                    }
                    
                    try{
                        loader.save(node);
                        sender.sendPrefixedMsg("<green>Successfully created copy of</green> %s <green>as</green> %s<green>!", name, copy);
                        sender.sendPrefixedMsg("<green>Load the file using</green> /asl reload");
                    }catch(IOException ex){
                        sender.sendErrorMsg("<red>There was an Error while trying to save</red> %s<red>!", copy);
                        sender.sendErrorMsg("<red>Check console for details.");
                    }
                }
                default -> {
                    sender.sendErrorMsg("<red>Unknown argument</red> %s<red>!", args[0]);
                    sender.sendErrorMsg("<red>Usage:</red> %s", usage());
                }
            }
        }
        
        @Override
        public String usage(){
            return "<aqua>/asl <white>profiles</white> <grey><" +
                "<white>add</white> <<white>name</white>> | " +
                "<white>copy</white> <<white>profile</white>> <<white>name</white>> | " +
                "<white>list</white>" +
                "></grey></aqua>";
        }
        
        @Override
        public String description(){
            return "Creates a new profile, copies one from an existing file, or lists all available profiles.";
        }
        
        private String getHover(ServerListProfile profile){
            return String.format(
                """
                <grey>Priority: <white>%s</white>
                Condition: <white>%s</white>
                
                Valid profile: %s
                """,
                profile.priority(),
                profile.condition() == null || profile.condition().isEmpty() ? "<i>None</i>" : profile.condition(),
                profile.isInvalidProfile() ? "<red>No</red>" : "<green>Yes</green>"
            );
        }
        
        private String getProfileName(String name){
            String fileName = name.toLowerCase(Locale.ROOT);
            
            return fileName.endsWith(".yml") ? fileName : fileName + ".yml";
        }
    }
}

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

package ch.andre601.advancedserverlist.core;

import ch.andre601.advancedserverlist.api.AdvancedServerListAPI;
import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.core.check.UpdateChecker;
import ch.andre601.advancedserverlist.core.commands.CommandHandler;
import ch.andre601.advancedserverlist.core.compat.maintenance.MaintenancePlaceholder;
import ch.andre601.advancedserverlist.core.file.FileHandler;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.parsing.TextCenterUtil;
import ch.andre601.advancedserverlist.core.profiles.conditions.ProfileConditionParser;
import ch.andre601.advancedserverlist.core.profiles.handlers.PlayerHandler;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.injection.ParameterInjector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AdvancedServerList<F>{
    
    public static <F> AdvancedServerList<F> init(PluginCore<F> plugin, PlaceholderProvider... placeholders){
        return new AdvancedServerList<>(plugin, Arrays.asList(placeholders));
    }
    
    private final PluginCore<F> plugin;
    private final FileHandler fileHandler;
    private final PlayerHandler playerHandler;
    private final TextCenterUtil textCenterUtil;
    
    private static final AdvancedServerListAPI api = AdvancedServerListAPI.get();
    private final ProfileConditionParser parser = ProfileConditionParser.create();
    
    private UpdateChecker updateChecker;
    
    private String version;
    
    private AdvancedServerList(PluginCore<F> plugin, List<PlaceholderProvider> placeholders){
        this.plugin = plugin;
        this.fileHandler = new FileHandler(this);
        this.playerHandler = new PlayerHandler(this);
        this.textCenterUtil = new TextCenterUtil(this);
        
        plugin.getPluginLogger().info("Registering internal Placeholders...");
        
        placeholders.forEach(this::addPlaceholder);
        
        if(plugin.isPluginEnabled("Maintenance")){
            addPlaceholder(new MaintenancePlaceholder(this));
        }
        
        resolveVersion();
        getPlugin().getPluginLogger().info("Starting <white>AdvancedServerList v%s</white>...", version);
        load();
    }
    
    public static AdvancedServerListAPI getApi(){
        return api;
    }
    
    public PluginCore<F> getPlugin(){
        return plugin;
    }
    
    public FileHandler getFileHandler(){
        return fileHandler;
    }
    
    public PlayerHandler getPlayerHandler(){
        return playerHandler;
    }
    
    public String getVersion(){
        return version;
    }
    
    public ProfileConditionParser getParser(){
        return parser;
    }
    
    public UpdateChecker getUpdateChecker(){
        return updateChecker;
    }
    
    public TextCenterUtil getTextCenterUtil(){
        return textCenterUtil;
    }
    
    public void disable(){
        getPlugin().getPluginLogger().info("[-] Saving <white>playercache.json</white> file...");
        getPlayerHandler().save();
        
        if(updateChecker != null){
            getPlugin().getPluginLogger().info("[-] Disabling Update Checker...");
            updateChecker.disable();
        }
        
        getPlugin().getPluginLogger().success("AdvancedServerList disabled!");
    }
    
    public void clearFaviconCache(){
        plugin.clearFaviconCache();
    }
    
    public void clearPlayerCache(){
        getPlayerHandler().clearCache();
    }
    
    private void load(){
        if(!getFileHandler().loadConfig()){
            getPlugin().getPluginLogger().failure("Unable to load <white>config.yml</white>! Check previous lines for errors.");
            return;
        }
        
        if(getFileHandler().isOldConfig()){
            getPlugin().getPluginLogger().info("[-] Detected old <white>config.yml</white>. Attempting to migrate...");
            if(getFileHandler().migrateConfig()){
                getPlugin().getPluginLogger().success("Migration completed successfully!");
            }else{
                getPlugin().getPluginLogger().failure("Couldn't migrate <white>config.yml</white>! Check previous lines for errors.");
                return;
            }
        }
        
        if(getFileHandler().getBool(true, "printBanner")){
            printBanner();
        }
        
        getPlugin().getPluginLogger().info("Platform: <white>%s</white>", plugin.getPlatformInfo());
        getPlugin().getPluginLogger().info("");
        
        if(getFileHandler().loadProfiles()){
            getPlugin().getPluginLogger().success("Successfully loaded <white>%d</white> profile(s)!", getFileHandler().getProfiles().size());
        }else{
            getPlugin().getPluginLogger().failure("Unable to load profiles! Check previous lines for errors.");
            return;
        }
        
        getPlugin().loadFaviconHandler(this);
        
        CommandManager<CmdSender> commandManager = plugin.getCommandManager();
        
        commandManager.parameterInjectorRegistry().registerInjector(AdvancedServerList.class, ParameterInjector.constantInjector(this));
        commandManager.createHelpHandler();
        
        AnnotationParser<CmdSender> annotationParser = new AnnotationParser<>(commandManager, CmdSender.class);
        annotationParser.parse(new CommandHandler());
        
        plugin.loadEvents();
        getPlayerHandler().load();
        plugin.loadMetrics();
        plugin.startScheduler();
    
        getPlugin().getPluginLogger().success("<green>AdvancedServerList is ready!");
        
        if(getFileHandler().getBoolean("checkUpdates"))
            this.updateChecker = new UpdateChecker(this);
    }
    
    private void printBanner(){
        getPlugin().getPluginLogger().info("<#3b90ff>     __      _______ ___");
        getPlugin().getPluginLogger().info("<#3b90ff>    /\\ \\    / ____|_| | |");
        getPlugin().getPluginLogger().info("<#3b90ff>   /  \\ \\  | (_(___ | | |");
        getPlugin().getPluginLogger().info("<#3b90ff>  / /\\ \\ \\  \\___ \\ \\| | |");
        getPlugin().getPluginLogger().info("<#3b90ff> / ____ \\ \\ ____) | | |_|____");
        getPlugin().getPluginLogger().info("<#3b90ff>/_/_/  \\_\\_\\_____/_/|______|_|");
        getPlugin().getPluginLogger().info("");
    }
    
    private void resolveVersion(){
        try(InputStream is = getClass().getResourceAsStream("/version.properties")){
            Properties properties = new Properties();
            
            properties.load(is);
            
            version = properties.getProperty("version");
        }catch(IOException ex){
            version = "UNKNOWN";
        }
    }
    
    private void addPlaceholder(PlaceholderProvider placeholderProvider){
        AdvancedServerList.getApi().addPlaceholderProvider(placeholderProvider);
    }
}

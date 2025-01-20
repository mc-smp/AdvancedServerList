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

package ch.andre601.advancedserverlist.paper;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.profiles.handlers.FaviconHandler;
import ch.andre601.advancedserverlist.paper.commands.PaperCommandHandler;
import ch.andre601.advancedserverlist.paper.listeners.LoadEvent;
import ch.andre601.advancedserverlist.paper.logging.PaperLogger;
import ch.andre601.advancedserverlist.paper.objects.WorldCache;
import ch.andre601.advancedserverlist.paper.objects.placeholders.PAPIPlaceholders;
import ch.andre601.advancedserverlist.paper.objects.placeholders.PaperPlayerPlaceholders;
import ch.andre601.advancedserverlist.paper.objects.placeholders.PaperServerPlaceholders;
import com.alessiodp.libby.Library;
import com.alessiodp.libby.PaperLibraryManager;
import io.papermc.paper.ServerBuildInfo;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;
import org.incendo.cloud.CommandManager;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.logging.Level;

public class PaperCore extends JavaPlugin implements PluginCore<CachedServerIcon>{
    
    private final PluginLogger logger = new PaperLogger(this);
    
    private PaperCommandHandler commandHandler;
    private AdvancedServerList<CachedServerIcon> core;
    private FaviconHandler<CachedServerIcon> faviconHandler = null;
    private PAPIPlaceholders papiPlaceholders = null;
    private WorldCache worldCache = null;
    
    private PaperLibraryManager libraryManager = null;
    
    private boolean successfulLoad = false;
    
    @Override
    public void onLoad(){
        getLogger().info("Loading Libraries. This may take a while...");
        successfulLoad = loadLibraries();
        if(successfulLoad){
            getLogger().info("Library Loading complete!");
        }else{
            getLogger().warning("There were issues while loading libraries.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onEnable(){
        if(!successfulLoad){
            getLogger().warning("Libraries couldn't be loaded during load. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        this.commandHandler = new PaperCommandHandler(this);
        this.core = AdvancedServerList.init(this, new PaperPlayerPlaceholders(), new PaperServerPlaceholders(this));
        
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            papiPlaceholders = new PAPIPlaceholders(this);
    }
    
    @Override
    public void onDisable(){
        if(papiPlaceholders != null){
            papiPlaceholders.unregister();
            papiPlaceholders = null;
        }
        
        if(worldCache != null)
            worldCache = null;
        
        getCore().disable();
    }
    
    @Override
    public void loadEvents(){
        new LoadEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        new Metrics(this, 15584).addCustomChart(new SimplePie("profiles",
            () -> String.valueOf(core.getFileHandler().getProfiles().size())
        ));
    }
    
    @Override
    public void loadFaviconHandler(AdvancedServerList<CachedServerIcon> core){
        faviconHandler = new FaviconHandler<>(core);
    }
    
    @Override
    public void clearFaviconCache(){
        if(faviconHandler == null)
            return;
        
        faviconHandler.cleanCache();
    }
    
    @Override
    public void downloadLibrary(String groupId, String artifactId, String version){
        if(libraryManager == null){
            libraryManager = new PaperLibraryManager(this);
            libraryManager.addRepository("https://repo.papermc.io/repository/maven-public");
        }
        
        Library lib = Library.builder()
            .groupId(groupId)
            .artifactId(artifactId)
            .version(version)
            .resolveTransitiveDependencies(true)
            .build();
        
        libraryManager.loadLibrary(lib);
    }
    
    @Override
    public void startScheduler(){
        // Unused
    }
    
    @Override
    public AdvancedServerList<CachedServerIcon> getCore(){
        return core;
    }
    
    @Override
    public Path getFolderPath(){
        return getDataFolder().toPath();
    }
    
    @Override
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public FaviconHandler<CachedServerIcon> getFaviconHandler(){
        if(faviconHandler == null)
            faviconHandler = new FaviconHandler<>(core);
        
        return faviconHandler;
    }
    
    @Override
    public CommandManager<CmdSender> getCommandManager(){
        return commandHandler.commandHandler();
    }
    
    @Override
    public String getPlatformInfo(){
        try{
            Class.forName("io.papermc.paper.ServerBuildInfo");
            return getNewVersion();
        }catch(ClassNotFoundException ignored){
            // Old Paper versions (Before 16th of May 2024)
            return getOldVersion();
        }
    }
    
    @Override
    public String getLoader(){
        return "paper";
    }
    
    @Override
    public boolean isPluginEnabled(String plugin){
        return Bukkit.getPluginManager().isPluginEnabled(plugin);
    }
    
    @Override
    public CachedServerIcon createFavicon(BufferedImage image) throws Exception{
        return Bukkit.loadServerIcon(image);
    }
    
    public WorldCache getWorldCache(){
        if(worldCache != null)
            return worldCache;
        
        return (worldCache = new WorldCache());
    }
    
    public int getPlayersOnline(World world){
        List<? extends Player> players = new ArrayList<>(world == null ? getServer().getOnlinePlayers() : world.getPlayers());
        
        players.removeIf(player -> {
            for(MetadataValue metadata : player.getMetadata("vanished")){
                if(metadata.asBoolean())
                    return true;
            }
            
            return false;
        });
        
        return players.size();
    }
    
    private boolean loadLibraries(){
        try{
            if(libraryManager == null){
                libraryManager = new PaperLibraryManager(this);
                libraryManager.addRepository("https://repo.papermc.io/repository/maven-public");
            }
            
            libraryManager.configureFromJSON("cloud-dependencies.json");
            return true;
        }catch(Exception ex){
            getLogger().log(Level.WARNING, "Encountered an issue while loading dependencies.", ex);
            return false;
        }
    }
    
    private String getNewVersion(){
        ServerBuildInfo info = ServerBuildInfo.buildInfo();
        
        String name = info.brandName();
        String id = info.brandId().asString();
        String version = info.minecraftVersionName();
        
        OptionalInt build = info.buildNumber();
        if(build.isEmpty())
            return String.format("%s (ID: %s, Version: %s)", name, id, version);
        
        return String.format("%s (ID: %s, Version: %s, Build: %d)", name, id, version, build.getAsInt());
    }
    
    private String getOldVersion(){
        return getServer().getName() + " " + getServer().getVersion();
    }
}

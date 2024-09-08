package ch.andre601.placeholderapi;

import ch.andre601.placeholderapi.messenger.ASLPluginMessageListener;
import ch.andre601.placeholderapi.messenger.BungeeCordMessageListener;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;
import java.util.logging.Level;

public final class ASLPlaceholderAPI extends JavaPlugin{
    
    private String serverName = null;
    
    @Override
    public void onEnable(){
        // Own Plugin Channel
        getServer().getMessenger().registerOutgoingPluginChannel(this, pluginChannel());
        getServer().getMessenger().registerIncomingPluginChannel(this, pluginChannel(), new ASLPluginMessageListener(this));
        
        // BungeeCord Plugin Channel
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordMessageListener(this));
        
        requestServerName();
    }
    
    @Override
    public void onDisable(){
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
    
    public void serverName(String serverName){
        this.serverName = serverName;
    }
    
    public String pluginChannel(){
        return "advancedserverlist:action";
    }
    
    public String serverName(){
        return serverName;
    }
    
    public void waitUntil(Callable<Boolean> condition, Runnable action){
        Bukkit.getScheduler().runTaskTimer(this, (task) -> {
            try{
                if(condition.call()){
                    action.run();
                    task.cancel();
                }
            }catch(Exception ex){
                getLogger().log(Level.WARNING, "Encountered Exception while waiting for a task to finish.", ex);
            }
        }, 0L, 1L);
    }
    
    private void requestServerName(){
        waitUntil(() -> !getServer().getOnlinePlayers().isEmpty(), () -> {
            Player plmSender = Bukkit.getOnlinePlayers().iterator().next();
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetServer");
            plmSender.sendPluginMessage(this, "BungeeCord", out.toByteArray());
        });
    }
}

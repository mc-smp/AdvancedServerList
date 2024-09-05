package ch.andre601.placeholderapi.messenger;

import ch.andre601.placeholderapi.ASLPlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.logging.Level;

public class BungeeCordMessageListener implements PluginMessageListener{
    
    private final ASLPlaceholderAPI plugin;
    
    public BungeeCordMessageListener(ASLPlaceholderAPI plugin){
        this.plugin = plugin;
    }
    
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message){
        if(!channel.equals("BungeeCord"))
            return;
        
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
        try{
            String subChannel = stream.readUTF();
            if("GetServer".equals(subChannel))
                plugin.serverName(stream.readUTF());
        }catch(Exception ex){
            plugin.getLogger().log(Level.WARNING, "Encountered Exception while reading plugin message from Bungeecord channel.", ex);
        }
    }
}

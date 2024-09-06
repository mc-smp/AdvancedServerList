package ch.andre601.placeholderapi.messenger;

import ch.andre601.placeholderapi.ASLPlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;
import java.util.logging.Level;

public class ASLPluginMessageListener implements PluginMessageListener{
    
    private final ASLPlaceholderAPI plugin;
    
    public ASLPluginMessageListener(ASLPlaceholderAPI plugin){
        this.plugin = plugin;
    }
    
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message){
        if(!channel.equals(plugin.pluginChannel()))
            return;
        
        if(plugin.serverName() == null)
            return;
        
        ByteArrayInputStream stream = new ByteArrayInputStream(message);
        DataInputStream in = new DataInputStream(stream);
        try{
            String action = in.readUTF();
            switch(action){
                case "findPlugins" -> sendPluginMessage("hasPlugin", plugin.serverName());
                case "parsePlaceholder" -> {
                    UUID playerUuid = UUID.fromString(in.readUTF());
                    String text = in.readUTF();
                    if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
                        sendPluginMessage("parsed", playerUuid.toString(), text);
                        return;
                    }
                    
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(playerUuid);
                    String parsed = PlaceholderAPI.setPlaceholders(targetPlayer, text);
                    sendPluginMessage("parsed", playerUuid.toString(), parsed);
                }
                default -> plugin.getLogger().warning("Received unknown plugin Message sub channel: " + action);
            }
        }catch(Exception ex){
            plugin.getLogger().log(Level.WARNING, "Encountered an Exception while handling a plugin message.", ex);
        }
    }
    
    private void sendPluginMessage(String channel, String... values){
        plugin.waitUntil(
            () -> !Bukkit.getOnlinePlayers().isEmpty() && plugin.serverName() != null,
            () -> {
                Player plmSender = Bukkit.getOnlinePlayers().iterator().next();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                DataOutputStream dataOut = new DataOutputStream(outStream);
                try{
                    dataOut.writeUTF(channel);
                    for(String value : values){
                        dataOut.writeUTF(value);
                    }
                }catch(Exception ex){
                    plugin.getLogger().log(Level.WARNING, "Encountered an Exception while writiing data for plugin message.", ex);
                }
                plmSender.sendPluginMessage(plugin, plugin.pluginChannel(), outStream.toByteArray());
            }
        );
    }
}

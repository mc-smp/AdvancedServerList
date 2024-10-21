package ch.andre601.advancedserverlist.bungeecord.listeners;

import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.core.objects.PluginMessageUtil;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageListener implements Listener{
    
    public PluginMessageListener(BungeeCordCore plugin){
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }
    
    @EventHandler
    public void onPluginMessage(PluginMessageEvent event){
        if(!event.getTag().equals(BungeeCordCore.ASL_PLUGIN_CHANNEL))
            return;
        
        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        String subChannel = input.readUTF();
        switch(subChannel){
            case "hasPlugin" -> {
                String serverName = input.readUTF();
                PluginMessageUtil.get().addServer(serverName);
            }
            case "parsed" -> {
                String uuid = input.readUTF();
                String parsed = input.readUTF();
                
                PluginMessageUtil.get().queue(uuid, parsed, true);
            }
        }
    }
}

package ch.andre601.advancedserverlist.velocity.listeners;

import ch.andre601.advancedserverlist.core.objects.PluginMessageUtil;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;

public class PluginMessageListener{
    
    public PluginMessageListener(VelocityCore plugin){
        plugin.getProxy().getEventManager().register(plugin, this);
    }
    
    @Subscribe
    public void onPluginMessage(PluginMessageEvent event){
        if(!(event.getSource() instanceof ServerConnection))
            return;
        
        if(event.getIdentifier() != VelocityCore.ASL_IDENTIFIER)
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

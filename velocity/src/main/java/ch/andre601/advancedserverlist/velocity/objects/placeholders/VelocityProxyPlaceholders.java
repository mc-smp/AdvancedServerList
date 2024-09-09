package ch.andre601.advancedserverlist.velocity.objects.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.velocity.VelocityCore;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VelocityProxyPlaceholders extends PlaceholderProvider{
    
    private final Map<String, Boolean> serverStates = new ConcurrentHashMap<>();
    private final VelocityCore plugin;
    
    private VelocityProxyPlaceholders(VelocityCore plugin){
        super("proxy");
        
        this.plugin = plugin;
        plugin.getProxy().getScheduler().buildTask(plugin, this::fetchServerStates)
            .repeat(Duration.ofSeconds(10))
            .schedule();
    }
    
    public static VelocityProxyPlaceholders init(VelocityCore plugin){
        return new VelocityProxyPlaceholders(plugin);
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server){
        String[] args = placeholder.split("\\s", 2);
        if(args.length < 2)
            return null;
        
        if(!args[0].equals("status"))
            return null;
        
        Boolean bool = serverStates.get(args[1]);
        return bool == null ? "Pinging..." : bool.toString();
    }
    
    private void fetchServerStates(){
        serverStates.clear();
        plugin.getProxy().getAllServers().forEach(server -> server.ping().whenComplete((ping, throwable) -> {
            if(throwable != null){
                plugin.getPluginLogger().warn("Encountered an exception while fetching status of Server %s", throwable, server.getServerInfo().getName());
                return;
            }
            
            serverStates.put(server.getServerInfo().getName(), ping != null);
        }));
    }
}

package ch.andre601.advancedserverlist.bungeecord.objects.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BungeeProxyPlaceholders extends PlaceholderProvider{
    
    private final Map<String, Boolean> serverStates = new ConcurrentHashMap<>();
    private final BungeeCordCore plugin;
    
    private BungeeProxyPlaceholders(BungeeCordCore plugin){
        super("proxy");
        
        this.plugin = plugin;
        plugin.getProxy().getScheduler().schedule(plugin, this::fetchServerStates, 0, 10, TimeUnit.SECONDS);
    }
    
    public static BungeeProxyPlaceholders init(BungeeCordCore plugin){
        return new BungeeProxyPlaceholders(plugin);
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
        plugin.getProxy().getServers().forEach((name, server) -> server.ping((ping, throwable) -> {
            if(throwable != null){
                plugin.getPluginLogger().warn("Encountered an exception while fetching status of Server %s", throwable, name);
                return;
            }
            
            serverStates.put(name, ping != null);
        }));
    }
}

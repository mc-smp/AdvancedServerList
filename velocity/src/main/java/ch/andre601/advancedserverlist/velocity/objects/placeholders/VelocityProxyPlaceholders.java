package ch.andre601.advancedserverlist.velocity.objects.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VelocityProxyPlaceholders extends PlaceholderProvider{
    
    private final Map<String, ServerPing> serverStates = new ConcurrentHashMap<>();
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
        
        ServerPing ping = serverStates.get(args[1]);
        return switch(args[0]){
            case "status" -> ping == null ? "offline" : "online";
            case "motd" -> {
                if(ping == null)
                    yield "none";
                
                yield ComponentParser.toMMString(ping.getDescriptionComponent());
            }
            case "players" -> {
                if(ping == null)
                    yield "0";
                
                ServerPing.Players players = ping.getPlayers().orElse(null);
                if(players == null)
                    yield "0";
                
                yield String.valueOf(players.getOnline());
            }
            case "maxPlayers" -> {
                if(ping == null)
                    yield "0";
                
                ServerPing.Players players = ping.getPlayers().orElse(null);
                if(players == null)
                    yield "0";
                
                yield String.valueOf(players.getMax());
            }
            default -> null;
        };
    }
    
    private void fetchServerStates(){
        serverStates.clear();
        plugin.getProxy().getAllServers().forEach(server -> server.ping().whenComplete((ping, throwable) -> {
            if(throwable != null){
                plugin.getPluginLogger().warn("Encountered an exception while fetching status of Server %s", throwable, server.getServerInfo().getName());
                return;
            }
            
            serverStates.put(server.getServerInfo().getName(), ping);
        }));
    }
}

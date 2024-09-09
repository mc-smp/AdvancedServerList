package ch.andre601.advancedserverlist.bungeecord.objects.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BungeeProxyPlaceholders extends PlaceholderProvider{
    
    private final Map<String, ServerPing> serverStates = new ConcurrentHashMap<>();
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
        
        ServerPing ping = serverStates.get(args[1]);
        
        return switch(args[0]){
            case "status" -> ping == null ? "offline" : "online";
            case "motd" -> {
                if(ping == null)
                    yield "none";
                
                BaseComponent[] components = new BaseComponent[]{ping.getDescriptionComponent()};
                Component component = BungeeComponentSerializer.get().deserialize(components);
                
                yield ComponentParser.toMMString(component);
            }
            case "players" -> {
                if(ping == null)
                    yield "0";
                
                yield String.valueOf(ping.getPlayers().getOnline());
            }
            case "maxPlayers" -> {
                if(ping == null)
                    yield "0";
                
                yield String.valueOf(ping.getPlayers().getMax());
            }
            default -> null;
        };
    }
    
    private void fetchServerStates(){
        serverStates.clear();
        plugin.getProxy().getServers().forEach((name, server) -> server.ping((ping, throwable) -> {
            if(throwable != null){
                plugin.getPluginLogger().warn("Encountered an exception while fetching status of Server %s", throwable, name);
                return;
            }
            
            serverStates.put(name, ping);
        }));
    }
}

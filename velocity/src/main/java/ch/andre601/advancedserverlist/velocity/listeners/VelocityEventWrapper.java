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

package ch.andre601.advancedserverlist.velocity.listeners;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.compat.maintenance.MaintenanceUtil;
import ch.andre601.advancedserverlist.core.compat.papi.PAPIUtil;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import ch.andre601.advancedserverlist.velocity.objects.impl.VelocityPlayerImpl;
import ch.andre601.advancedserverlist.velocity.objects.impl.VelocityProxyImpl;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class VelocityEventWrapper implements GenericEventWrapper<Favicon, VelocityPlayerImpl>{
    
    private final VelocityCore plugin;
    private final ProxyPingEvent event;
    private final ServerPing.Builder builder;
    private final ServerPing.Version protocol;
    
    public VelocityEventWrapper(VelocityCore plugin, ProxyPingEvent event){
        this.plugin = plugin;
        this.event = event;
        this.builder = event.getPing().asBuilder();
        this.protocol = event.getPing().getVersion();
    }
    
    @Override
    public GenericServerListEvent callEvent(ProfileEntry entry){
        PreServerListSetEventImpl event = new PreServerListSetEventImpl(entry);
        try{
            plugin.getProxy().getEventManager().fire(event).get();
        }catch(InterruptedException | ExecutionException ignored){
            return null;
        }
        
        return event;
    }
    
    @Override
    public void maxPlayers(int maxPlayers){
        builder.maximumPlayers(maxPlayers);
    }
    
    @Override
    public void onlinePlayers(int onlinePlayers){
        builder.onlinePlayers(onlinePlayers);
    }
    
    @Override
    public void motd(Component component){
        builder.description(component);
    }
    
    @Override
    public void hidePlayers(){
        builder.nullPlayers();
    }
    
    @Override
    public void playerCount(String name){
        builder.version(new ServerPing.Version(-1, name));
    }
    
    @Override
    public void players(List<String> lines, VelocityPlayerImpl player, GenericServer server){
        ServerPing.SamplePlayer[] players = new ServerPing.SamplePlayer[lines.size()];
        
        for(int i = 0; i < players.length; i++){
            String parsed = ComponentParser.text(lines.get(i))
                .modifyText(text -> StringReplacer.replace(text, player, server))
                .modifyText(text -> parsePAPIPlaceholders(text, player))
                .toString();
            
            players[i] = new ServerPing.SamplePlayer(parsed, UUID.randomUUID());
        }
        
        if(players.length > 0)
            builder.clearSamplePlayers().samplePlayers(players);
    }
    
    @Override
    public void playersHidden(){
        builder.clearSamplePlayers();
    }
    
    @Override
    public void favicon(Favicon favicon){
        builder.favicon(favicon);
    }
    
    // Not used in Velocity
    @Override
    public void defaultFavicon(){}
    
    @Override
    public void updateEvent(){
        event.setPing(builder.build());
    }
    
    @Override
    public boolean isInvalidProtocol(){
        return protocol == null;
    }
    
    @Override
    public boolean isMaintenanceModeActive(){
        if(!plugin.getProxy().getPluginManager().isLoaded("maintenance"))
            return false;
        
        return MaintenanceUtil.get().isMaintenanceEnabled();
    }
    
    @Override
    public int protocolVersion(){
        return event.getConnection().getProtocolVersion().getProtocol();
    }
    
    @Override
    public int onlinePlayers(){
        int players = plugin.getOnlinePlayers(null);
        if(players == -1)
            return builder.getOnlinePlayers();
        
        return players;
    }
    
    @Override
    public int maxPlayers(){
        return builder.getMaximumPlayers();
    }
    
    @Override
    public String playerIP(){
        return event.getConnection().getRemoteAddress().getHostString();
    }
    
    @Override
    public String parsePAPIPlaceholders(String text, VelocityPlayerImpl player){
        if(!plugin.getProxy().getPluginManager().isLoaded("papiproxybridge"))
            return text;
        
        if(!PAPIUtil.get().isCompatible())
            return text;
        
        String server = PAPIUtil.get().getServer();
        if(server == null || server.isEmpty())
            return text;
        
        RegisteredServer registeredServer = plugin.getProxy().getServer(server).orElse(null);
        if(registeredServer == null || registeredServer.getPlayersConnected().isEmpty())
            return text;
        
        Player carrier = PAPIUtil.get().getPlayer(registeredServer.getPlayersConnected());
        if(carrier == null)
            return text;
        
        return PAPIUtil.get().parse(text, carrier.getUniqueId(), player.getUUID());
    }
    
    @Override
    public String virtualHost(){
        return this.resolveHost(event.getConnection().getVirtualHost().orElse(null));
    }
    
    @Override
    public PluginCore<Favicon> plugin(){
        return plugin;
    }
    
    @Override
    public VelocityPlayerImpl createPlayer(CachedPlayer player, int protocol){
        return new VelocityPlayerImpl(player, protocol);
    }
    
    @Override
    public GenericServer createServer(int playersOnline, int playersMax, String host){
        Map<String, RegisteredServer> servers = new HashMap<>();
        plugin.getProxy().getAllServers().forEach(server -> servers.put(server.getServerInfo().getName(), server));
        
        return new VelocityProxyImpl(servers, playersOnline, playersMax, host);
    }
}

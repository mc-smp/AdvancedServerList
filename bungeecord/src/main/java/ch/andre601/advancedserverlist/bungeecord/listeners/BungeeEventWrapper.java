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

package ch.andre601.advancedserverlist.bungeecord.listeners;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.bungeecord.objects.impl.BungeePlayerImpl;
import ch.andre601.advancedserverlist.bungeecord.objects.impl.BungeeProxyImpl;
import ch.andre601.advancedserverlist.core.compat.maintenance.MaintenanceUtil;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.objects.CacheUtil;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.objects.PluginMessageUtil;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BungeeEventWrapper implements GenericEventWrapper<Favicon, BungeePlayerImpl>{
    
    private final CacheUtil<Integer> knownServerCache = new CacheUtil<>(Duration.ofMinutes(1));
    
    private final BungeeCordCore plugin;
    private final ProxyPingEvent event;
    private final ServerPing.Protocol protocol;
    private final ServerPing ping;
    
    public BungeeEventWrapper(BungeeCordCore plugin, ProxyPingEvent event){
        this.plugin = plugin;
        this.event = event;
        this.protocol = event.getResponse().getVersion();
        this.ping = event.getResponse();
    }
    
    @Override
    public GenericServerListEvent callEvent(ProfileEntry entry){
        PreServerListSetEventImpl event = new PreServerListSetEventImpl(entry);
        plugin.getProxy().getPluginManager().callEvent(event);
        
        return event;
    }
    
    @Override
    public void setMaxPlayers(int maxPlayers){
        ping.getPlayers().setMax(maxPlayers);
    }
    
    @Override
    public void setMotd(Component component){
        ping.setDescriptionComponent(new TextComponent(BungeeComponentSerializer.get().serialize(component)));
    }
    
    @Override
    public void hidePlayers(){
        ping.setPlayers(null);
    }
    
    @Override
    public void setPlayerCount(String name){
        protocol.setName(name);
        protocol.setProtocol(-1);
    }
    
    @Override
    public void setPlayers(List<String> lines, BungeePlayerImpl player, GenericServer server){
        ServerPing.PlayerInfo[] players = new ServerPing.PlayerInfo[lines.size()];
        
        for(int i = 0; i < players.length; i++){
            String parsed = ComponentParser.text(lines.get(i))
                .modifyText(text -> StringReplacer.replace(text, player, server))
                .toString();
            
            ServerPing.PlayerInfo pi = new ServerPing.PlayerInfo(parsed, UUID.randomUUID());
            players[i] = pi;
        }
        
        if(players.length > 0)
            ping.getPlayers().setSample(players);
    }
    
    @Override
    public void setFavicon(Favicon favicon){
        ping.setFavicon(favicon);
    }
    
    @Override
    public void setDefaultFavicon(){
        ping.setFavicon(ping.getFaviconObject());
    }
    
    @Override
    public void updateEvent(){
        this.ping.setVersion(this.protocol);
        this.event.setResponse(this.ping);
    }
    
    @Override
    public boolean isInvalidProtocol(){
        return protocol == null;
    }
    
    @Override
    public boolean isMaintenanceModeActive(){
        if(plugin.getProxy().getPluginManager().getPlugin("Maintenance") == null)
            return false;
        
        return MaintenanceUtil.get().isMaintenanceEnabled();
    }
    
    @Override
    public int getProtocolVersion(){
        return event.getConnection().getVersion();
    }
    
    @Override
    public int getOnlinePlayers(){
        return plugin.getOnlinePlayers(null);
    }
    
    @Override
    public int getMaxPlayers(){
        return ping.getPlayers().getMax();
    }
    
    @Override
    public String getPlayerIP(){
        return ((InetSocketAddress)event.getConnection().getSocketAddress()).getHostString();
    }
    
    @Override
    public String parsePAPIPlaceholders(String text, BungeePlayerImpl player){
        knownServerCache.get(() -> {
            plugin.getProxy().getServers().values().forEach(serverInfo -> {
                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF("findPlugins");
                
                serverInfo.sendData("advancedserverlist:action", output.toByteArray());
            });
            // We return some random dummy value here
            return 1;
        });
        
        List<String> knownServers = PluginMessageUtil.get().getKnownServers();
        if(knownServers.isEmpty())
            return text;
        
        String serverName = knownServers.get(0);
        ServerInfo server = plugin.getProxy().getServerInfo(serverName);
        if(server == null)
            return text;
        
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("parse");
        output.writeUTF(player.getUUID().toString());
        output.writeUTF(text);
        
        server.sendData("advancedserverlist:action", output.toByteArray());
        PluginMessageUtil.get().putInQueue(player.getUUID().toString(), text);
        
        if(PluginMessageUtil.get().hasParsed(player.getUUID().toString())){
            return PluginMessageUtil.get().getAndRemoveParsed(player.getUUID().toString());
        }else{
            return PluginMessageUtil.get().getQueuedValue(player.getUUID().toString());
        }
    }
    
    @Override
    public String getVirtualHost(){
        return this.resolveHost(event.getConnection().getVirtualHost());
    }
    
    @Override
    public PluginCore<Favicon> getPlugin(){
        return plugin;
    }
    
    @Override
    public BungeePlayerImpl createPlayer(CachedPlayer player, int protocol){
        return new BungeePlayerImpl(player, protocol);
    }
    
    @Override
    public GenericServer createGenericServer(int playersOnline, int playersMax, String host){
        Map<String, ServerInfo> servers = plugin.getProxy().getServers();
        
        return new BungeeProxyImpl(servers, playersOnline, playersMax, host);
    }
}

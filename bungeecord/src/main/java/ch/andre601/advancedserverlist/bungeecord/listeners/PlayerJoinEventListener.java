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

import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.bungeecord.commands.BungeeCmdSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public class PlayerJoinEventListener implements Listener{
    private final BungeeCordCore plugin;
    
    public PlayerJoinEventListener(BungeeCordCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }
    
    @EventHandler
    public void onJoin(PostLoginEvent event){
        InetSocketAddress address = (InetSocketAddress)event.getPlayer().getPendingConnection().getSocketAddress();
        ProxiedPlayer player = event.getPlayer();
        
        plugin.core().playerHandler().addPlayer(address.getHostString(), player.getName(), player.getUniqueId());
        
        if(player.hasPermission("advancedserverlist.admin") || player.hasPermission("advancedserverlist.updatecheck")){
            if(plugin.getAudiences() == null || plugin.core().updateChecker() == null)
                return;
            
            BungeeCmdSender sender = new BungeeCmdSender(player, plugin.getAudiences());
            
            plugin.core().updateChecker().performCachedUpdateCheck(sender);
        }
    }
}

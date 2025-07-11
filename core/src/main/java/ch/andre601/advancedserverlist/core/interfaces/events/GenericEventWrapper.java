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

package ch.andre601.advancedserverlist.core.interfaces.events;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import net.kyori.adventure.text.Component;

import java.net.InetSocketAddress;
import java.util.List;

public interface GenericEventWrapper<F, P extends GenericPlayer>{
    
    GenericServerListEvent callEvent(ProfileEntry entry);
    
    void maxPlayers(int maxPlayers);
    
    void onlinePlayers(int onlinePlayers);
    
    void motd(Component component);
    
    void hidePlayers();
    
    void playerCount(String name);
    
    void players(List<String> players, P player, GenericServer server);
    
    void playersHidden();
    
    void favicon(F favicon);
    
    void defaultFavicon();
    
    void updateEvent();
    
    boolean isInvalidProtocol();
    
    boolean isMaintenanceModeActive();
    
    int protocolVersion();
    
    int onlinePlayers();
    
    int maxPlayers();
    
    String playerIP();
    
    String parsePAPIPlaceholders(String text, P player);
    
    String virtualHost();
    
    PluginCore<F> plugin();
    
    P createPlayer(CachedPlayer player, int protocol);
    
    GenericServer createServer(int playersOnline, int playersMax, String host);
    
    default String resolveHost(InetSocketAddress address){
        return address == null ? null : address.getHostString();
    }
}

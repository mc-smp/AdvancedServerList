/*
 * MIT License
 *
 * Copyright (c) 2022-2024 Andre_601
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

package ch.andre601.advancedserverlist.core.migration.minimotd.serializing;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MiniMOTDSerializer implements TypeSerializer<MiniMOTDConfig>{
    
    public static final MiniMOTDSerializer MINI_MOTD_SERIALIZER = new MiniMOTDSerializer();
    public static final MotdSerializer MOTD_SERIALIZER = new MotdSerializer();
    public static final PlayerCountSerializer PLAYER_COUNT_SERIALIZER = new PlayerCountSerializer();
    
    @Override
    public MiniMOTDConfig deserialize(Type type, ConfigurationNode node) throws SerializationException{
        boolean iconsEnabled = node.node("icon-enabled").getBoolean();
        boolean motdEnabled = node.node("motd-enabled").getBoolean();
        
        List<MiniMOTDConfig.Motd> motds = node.node("motds").getList(MiniMOTDConfig.Motd.class);
        MiniMOTDConfig.PlayerCount playerCount = node.node("player-count-settings").get(MiniMOTDConfig.PlayerCount.class);
        
        return new MiniMOTDConfig(iconsEnabled, motdEnabled, motds, playerCount);
    }
    
    @Override
    public void serialize(Type type, @Nullable MiniMOTDConfig obj, ConfigurationNode node) throws SerializationException{
        if(obj == null){
            node.raw(null);
            return;
        }
        
        node.node("icon-enabled").set(obj.iconEnabled());
        node.node("motd-enable").set(obj.motdEnabled());
        node.node("motds").setList(MiniMOTDConfig.Motd.class, obj.motds());
        node.node("player-count-settings").set(MiniMOTDConfig.PlayerCount.class, obj.playerCount());
    }
    
    public static class MotdSerializer implements TypeSerializer<MiniMOTDConfig.Motd>{
        
        @Override
        public MiniMOTDConfig.Motd deserialize(Type type, ConfigurationNode node) throws SerializationException{
            String icon = node.node("icon").getString("");
            String line1 = node.node("line1").getString("");
            String line2 = node.node("line2").getString("");
            
            return new MiniMOTDConfig.Motd(icon, line1, line2);
        }
        
        @Override
        public void serialize(Type type, MiniMOTDConfig.@Nullable Motd obj, ConfigurationNode node) throws SerializationException{
            if(obj == null){
                node.raw(null);
                return;
            }
            
            node.node("icon").set(obj.icon());
            node.node("line1").set(obj.line1());
            node.node("line2").set(obj.line2());
        }
    }
    
    public static class PlayerCountSerializer implements TypeSerializer<MiniMOTDConfig.PlayerCount>{
        
        @Override
        public MiniMOTDConfig.PlayerCount deserialize(Type type, ConfigurationNode node) throws SerializationException{
            boolean hidePlayerCount = node.node("hide-player-count").getBoolean();
            boolean xMoreEnabled = node.node("just-x-more-settings", "just-x-more-enabled").getBoolean();
            int xMore = node.node("just-x-more-settings", "x-value").getInt();
            boolean maxPlayersEnabled = node.node("max-players-enabled").getBoolean();
            int maxPlayers = node.node("max-players").getInt();
            
            
            return new MiniMOTDConfig.PlayerCount(hidePlayerCount, xMoreEnabled, xMore, maxPlayersEnabled, maxPlayers);
        }
        
        @Override
        public void serialize(Type type, MiniMOTDConfig.@Nullable PlayerCount obj, ConfigurationNode node) throws SerializationException{
            if(obj == null){
                node.raw(null);
                return;
            }
            
            node.node("allow-exceeding-maximum").set(false);
            node.node("disable-player-list-hover").set(false);
            
            node.node("fake-players", "fake-players").set("25%");
            node.node("fake-players", "fake-players-enabled").set(false);
            
            node.node("hide-player-count").set(obj.hidePlayers());
            
            node.node("just-x-more-settings", "just-x-more-enabled").set(obj.xMoreEnabled());
            node.node("just-x-more-settings", "x-value").set(obj.xMore());
            
            node.node("max-players").set(obj.maxPlayers());
            node.node("max-players-enabled").set(obj.maxPlayersEnabled());
            
            node.node("servers").setList(String.class, Collections.emptyList());
        }
    }
}

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

package ch.andre601.advancedserverlist.paper.objects;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public record FakePlayerProfile(String name, UUID uuid) implements PlayerProfile{
    
    public static FakePlayerProfile create(String name){
        return new FakePlayerProfile(name, UUID.randomUUID());
    }
    
    @Override
    public @Nullable UUID getUniqueId(){
        return uuid();
    }
    
    @Override
    public @Nullable String getName(){
        return name();
    }
    
    @SuppressWarnings("removal")
    @Override
    public @NotNull String setName(@Nullable String name){
        return name;
    }
    
    @Override
    public @Nullable UUID getId(){
        return null;
    }
    
    @SuppressWarnings("removal")
    @Override
    public @Nullable UUID setId(@Nullable UUID uuid){
        return null;
    }
    
    @Override
    public @NotNull PlayerTextures getTextures(){
        return new FakePlayeerTextures();
    }
    
    @Override
    public void setTextures(@Nullable PlayerTextures textures){
        
    }
    
    @Override
    public @NotNull Set<ProfileProperty> getProperties(){
        return Collections.emptySet();
    }
    
    @Override
    public boolean hasProperty(@Nullable String property){
        return false;
    }
    
    @Override
    public void setProperty(@NotNull ProfileProperty property){
        
    }
    
    @Override
    public void setProperties(@NotNull Collection<ProfileProperty> properties){
        
    }
    
    @Override
    public boolean removeProperty(@Nullable String property){
        return false;
    }
    
    @Override
    public void clearProperties(){
        
    }
    
    @Override
    public boolean isComplete(){
        return false;
    }
    
    @Override
    public boolean completeFromCache(){
        return false;
    }
    
    @Override
    public boolean completeFromCache(boolean onlineMode){
        return false;
    }
    
    @Override
    public boolean completeFromCache(boolean lookupUUID, boolean onlineMode){
        return false;
    }
    
    @Override
    public boolean complete(boolean textures){
        return false;
    }
    
    @Override
    public boolean complete(boolean textures, boolean onlineMode){
        return false;
    }
    
    @Override
    public @NotNull CompletableFuture<PlayerProfile> update(){
        return null;
    }
    
    @Override
    public org.bukkit.profile.@NotNull PlayerProfile clone(){
        return null;
    }
    
    @Override
    public @NotNull Map<String, Object> serialize(){
        return Collections.emptyMap();
    }
    
    private static record FakePlayeerTextures() implements PlayerTextures{
        
        @Override
        public boolean isEmpty(){
            return false;
        }
        
        @Override
        public void clear(){
            
        }
        
        @Override
        public @Nullable URL getSkin(){
            return null;
        }
        
        @Override
        public void setSkin(@Nullable URL skinUrl){
            
        }
        
        @Override
        public void setSkin(@Nullable URL skinUrl, @Nullable SkinModel skinModel){
            
        }
        
        @Override
        public @NotNull SkinModel getSkinModel(){
            return SkinModel.CLASSIC;
        }
        
        @Override
        public @Nullable URL getCape(){
            return null;
        }
        
        @Override
        public void setCape(@Nullable URL capeUrl){
            
        }
        
        @Override
        public long getTimestamp(){
            return 0;
        }
        
        @Override
        public boolean isSigned(){
            return false;
        }
    }
}

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

package ch.andre601.advancedserverlist.banplugins.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.banplugins.providers.LiteBansProvider;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class LiteBansPlaceholders extends PlaceholderProvider{
    
    private LiteBansProvider provider = null;
    
    public LiteBansPlaceholders(){
        super("litebans");
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server){
        if(provider == null)
            provider = new LiteBansProvider();
        
        String[] args = placeholder.split("\\s", 2);
        
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> 
            switch(args[0]){
                // Mute-related placeholders
                case "isMuted" -> provider.muted(player);
                case "muteReason" -> provider.muteReason(player);
                case "muteDuration" -> provider.muteDuration(player);
                case "muteExpiration" -> {
                    if(args.length == 1)
                        yield provider.muteExpirationDate(player);
                    
                    yield provider.muteExpirationDate(player, args[1]);
                }
                
                // Ban-related placeholders
                case "isBanned" -> provider.banned(player);
                case "banReason" -> provider.banReason(player);
                case "banDuration" -> provider.banDuration(player);
                case "banExpiration" -> {
                    if(args.length == 1)
                        yield provider.banExpirationDate(player);
                    
                    yield provider.banExpirationDate(player, args[1]);
                }
                
                //Unknown/Invalid placeholder
                default -> null;
            }
        );
        
        try{
            return future.join();
        }catch(CancellationException | CompletionException ex){
            return null;
        }
    }
}

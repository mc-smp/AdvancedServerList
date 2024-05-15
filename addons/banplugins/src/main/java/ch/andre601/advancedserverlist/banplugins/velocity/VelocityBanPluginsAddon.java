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

package ch.andre601.advancedserverlist.banplugins.velocity;

import ch.andre601.advancedserverlist.api.AdvancedServerListAPI;
import ch.andre601.advancedserverlist.banplugins.BanPluginsList;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class VelocityBanPluginsAddon{
    
    private final Logger logger = LoggerFactory.getLogger("ASLBanPluginsAddon");
    
    private final ProxyServer proxy;
    
    @Inject
    public VelocityBanPluginsAddon(ProxyServer proxy){
        this.proxy = proxy;
    }
    
    @Subscribe
    public void init(ProxyInitializeEvent event){
        if(!proxy.getPluginManager().isLoaded("advancedserverlist")){
            logger.warn("AdvancedServerList is not enabled. This plugin requires it to function!");
            return;
        }
        
        int loadedPlaceholders = loadPlaceholderProviders();
        if(loadedPlaceholders == 0){
            logger.warn("No compatible Ban plugin was found to register placeholders for.");
        }else{
            logger.info("Loaded {} Placeholder Set(s) for AdvancedServerList!", loadedPlaceholders);
        }
    }
    
    private int loadPlaceholderProviders(){
        int loaded = 0;
        AdvancedServerListAPI api = AdvancedServerListAPI.get();
        
        for(BanPluginsList entry : BanPluginsList.values()){
            if(!entry.supportsVelocity())
                continue;
            
            if(proxy.getPluginManager().isLoaded(entry.getName().toLowerCase(Locale.ROOT))){
                logger.info("Registering Placeholders for " + entry.getName() + "...");
                api.addPlaceholderProvider(entry.getPlaceholderProvider());
                logger.info("Placeholders successfully registered!");
                
                loaded++;
            }
        }
        
        return loaded;
    }
}

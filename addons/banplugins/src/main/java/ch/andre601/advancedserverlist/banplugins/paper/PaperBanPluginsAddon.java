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

package ch.andre601.advancedserverlist.banplugins.paper;

import ch.andre601.advancedserverlist.api.AdvancedServerListAPI;
import ch.andre601.advancedserverlist.banplugins.BanPluginsList;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperBanPluginsAddon extends JavaPlugin{
    
    @Override
    public void onEnable(){
        if(!getServer().getPluginManager().isPluginEnabled("AdvancedServerList")){
            getLogger().warning("AdvancedServerList is not enabled. This plugin requires it to function!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        int loadedPlaceholders = loadPlaceholderProviders();
        if(loadedPlaceholders == 0){
            getLogger().warning("No compatible Ban plugin was found to register placeholders for. Disabling plugin...");
            
            getServer().getPluginManager().disablePlugin(this);
        }else{
            getLogger().info("Loaded " + loadedPlaceholders + " Placeholder Set(s) for AdvancedServerList!");
        }
    }
    
    private int loadPlaceholderProviders(){
        int loaded = 0;
        AdvancedServerListAPI api = AdvancedServerListAPI.get();
        
        for(BanPluginsList entry : BanPluginsList.values()){
            if(!entry.supportsPaper())
                continue;
            
            if(getServer().getPluginManager().isPluginEnabled(entry.getName())){
                getLogger().info("Registering Placeholders for " + entry.getName() + "...");
                api.addPlaceholderProvider(entry.getPlaceholderProvider());
                getLogger().info("Placeholders successfully registered!");
                
                loaded++;
            }
        }
        
        return loaded;
    }
}

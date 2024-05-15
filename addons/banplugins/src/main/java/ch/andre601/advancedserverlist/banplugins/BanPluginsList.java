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

package ch.andre601.advancedserverlist.banplugins;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.banplugins.placeholders.AdvancedBanPlaceholders;
import ch.andre601.advancedserverlist.banplugins.placeholders.LibertyBansPlaceholders;
import ch.andre601.advancedserverlist.banplugins.placeholders.LiteBansPlaceholders;

public enum BanPluginsList{
    
    ADVANCED_BAN("AdvancedBan", new AdvancedBanPlaceholders(), true, true, false),
    LIBERTY_BANS("LibertyBans", new LibertyBansPlaceholders(), true, true, true),
    LITE_BANS("LiteBans", new LiteBansPlaceholders(), true, true, true);
    
    private final String name;
    private final PlaceholderProvider placeholderProvider;
    private final boolean paper, bungeecord, velocity;
    
    BanPluginsList(String name, PlaceholderProvider placeholderProvider, boolean paper, boolean bungeecord, boolean velocity){
        this.name = name;
        this.placeholderProvider = placeholderProvider;
        this.paper = paper;
        this.bungeecord = bungeecord;
        this.velocity = velocity;
    }
    
    public String getName(){
        return name;
    }
    
    public PlaceholderProvider getPlaceholderProvider(){
        return placeholderProvider;
    }
    
    public boolean supportsPaper(){
        return paper;
    }
    
    public boolean supportsBungeeCord(){
        return bungeecord;
    }
    
    public boolean supportsVelocity(){
        return velocity;
    }
}

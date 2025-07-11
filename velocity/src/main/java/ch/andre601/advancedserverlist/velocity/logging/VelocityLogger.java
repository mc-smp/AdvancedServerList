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

package ch.andre601.advancedserverlist.velocity.logging;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class VelocityLogger implements PluginLogger{
    
    private final VelocityCore plugin;
    private final ComponentLogger logger;
    
    public VelocityLogger(VelocityCore plugin, ComponentLogger logger){
        this.plugin = plugin;
        this.logger = logger;
    }
    
    @Override
    public void debug(Class<?> clazz, String msg, Object... args){
        if(plugin.isDebugEnabled())
            info("[DEBUG] [" + clazz.getSimpleName() + "] " + msg, args);
    }
    
    @Override
    public void debugWarn(Class<?> clazz, String msg, Object... args){
        if(plugin.isDebugEnabled())
            warn("[DEBUG] [" + clazz.getSimpleName() + "] " + msg, args);
    }
    
    @Override
    public void info(String msg, Object... args){
        logger.info(ComponentParser.textFormatted("<grey>" + msg, args).toComponent());
    }
    
    @Override
    public void warn(String msg, Object... args){
        logger.warn(ComponentParser.textFormatted(msg, args).toComponent());
    }
    
    @Override
    public void warn(String msg, Throwable throwable){
        logger.warn(ComponentParser.text(msg).toComponent(), throwable);
    }
    
    @Override
    public void warn(String msg, Throwable throwable, Object... args){
        logger.warn(ComponentParser.textFormatted(msg, args).toComponent(), throwable);
    }
}

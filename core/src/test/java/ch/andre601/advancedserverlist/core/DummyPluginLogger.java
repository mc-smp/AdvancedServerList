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

package ch.andre601.advancedserverlist.core;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;

public class DummyPluginLogger implements PluginLogger{
    @Override
    public void debug(Class<?> clazz, String msg, Object... args){
        info("[DEBUG] [" + clazz.getSimpleName() + "] " + msg, args);
    }
    
    @Override
    public void debugWarn(Class<?> clazz, String msg, Object... args){
        warn("[DEBUG] [" + clazz.getSimpleName() + "] " + msg, args);
    }
    
    @Override
    public void info(String msg, Object... args){
        System.out.printf((msg) + "%n", args);
    }
    
    @Override
    public void warn(String msg, Object... args){
        System.out.printf((msg) + "%n", args);
    }
    
    @Override
    public void warn(String msg, Throwable throwable){
        System.out.printf((msg) + "%n", throwable);
    }
    
    @Override
    public void warn(String msg, Throwable throwable, Object... args){
        System.out.printf((msg) + "%n", args);
    }
}

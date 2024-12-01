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

package ch.andre601.advancedserverlist.core.objects;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class CacheUtil{
    
    protected final Duration duration;
    
    public CacheUtil(Duration duration){
        this.duration = duration;
    }
    
    public static class SingletonCache<T> extends CacheUtil{
        
        private CacheValue<T> cacheValue = null;
        
        public SingletonCache(Duration duration){
            super(duration);
        }
        
        public T get(Supplier<T> supplier){
            if(cacheValue == null || cacheValue.expired())
                cacheValue = new CacheValue<>(System.currentTimeMillis(), duration, supplier.get());
            
            return cacheValue.value();
        }
    }
    
    public static class IdentifiableCache<K, V> extends CacheUtil{
        
        private final Map<K, CacheValue<V>> cacheValues = new HashMap<>();
        
        public IdentifiableCache(Duration duration){
            super(duration);
        }
        
        public V get(K key, Supplier<V> supplier){
            CacheValue<V> value = cacheValues.get(key);
            if(value == null || value.expired()){
                value = new CacheValue<>(System.currentTimeMillis(), duration, supplier.get());
                cacheValues.put(key, value);
            }
            
            return cacheValues.get(key).value();
        }
        
        public void clear(){
            cacheValues.clear();
        }
    }
    
    public record CacheValue<T>(long timestamp, Duration duration, T value){
        public boolean expired(){
            return (timestamp < 0L) || System.currentTimeMillis() - timestamp >= duration.toMillis();
        }
    }
}

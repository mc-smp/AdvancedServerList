package ch.andre601.advancedserverlist.core.objects;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;

public class PluginMessageUtil{
    
    private static PluginMessageUtil instance;
    
    private final List<String> knownServers = new CopyOnWriteArrayList<>();
    private final Map<String, ParsedText> text = new ConcurrentHashMap<>();
    
    private PluginMessageUtil(){}
    
    public static PluginMessageUtil get(){
        if(instance != null)
            return instance;
        
        return (instance = new PluginMessageUtil());
    }
    
    public void addServer(String name){
        this.knownServers.add(name);
    }
    
    public List<String> getKnownServers(){
        return knownServers;
    }
    
    public void clear(){
        knownServers.clear();
    }
    
    public String getOrExecute(String key, String value, BiFunction<String, String, String> function){
        if(text.get(key) != null){
            return getOrDefault(key, value);
        }
        
        return function.apply(key, value);
    }
    
    public void queue(String key, String value, boolean parsed){
        text.put(key, new ParsedText(value, parsed));
    }
    
    public String getOrDefault(String key, String def){
        if(text.get(key) == null)
            return def;
        
        return text.get(key).parsed() ? text.remove(key).text() : text.get(key).text();
    }
    
    private record ParsedText(String text, boolean parsed){}
}

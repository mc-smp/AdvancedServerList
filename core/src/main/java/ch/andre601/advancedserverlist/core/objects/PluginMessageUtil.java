package ch.andre601.advancedserverlist.core.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginMessageUtil{
    
    private static PluginMessageUtil instance;
    
    private final List<String> knownServers = new ArrayList<>();
    private final Map<String, String> queuedText = new HashMap<>();
    private final Map<String, String> parsedText = new HashMap<>();
    
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
    
    public void putInQueue(String key, String value){
        queuedText.put(key, value);
    }
    
    public void removeFromQueue(String key){
        queuedText.remove(key);
    }
    
    public String getQueuedValue(String key){
        return queuedText.get(key);
    }
    
    public void putInParsed(String key, String value){
        parsedText.put(key, value);
    }
    
    public boolean hasParsed(String key){
        return parsedText.containsKey(key);
    }
    
    public String getAndRemoveParsed(String key){
        return parsedText.remove(key);
    }
}

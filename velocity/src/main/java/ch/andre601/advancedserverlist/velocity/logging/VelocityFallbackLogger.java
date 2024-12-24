package ch.andre601.advancedserverlist.velocity.logging;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Used in case the Velocity version used doesn't have ComponentLogger
public class VelocityFallbackLogger implements PluginLogger{
    
    private final VelocityCore plugin;
    private final Logger logger;
    
    public VelocityFallbackLogger(VelocityCore plugin){
        this.plugin = plugin;
        this.logger = LoggerFactory.getLogger("AdvancedServerList");
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
        logger.info(ComponentParser.textFormatted("<grey>" + msg, args).toStringStriped());
    }
    
    @Override
    public void warn(String msg, Object... args){
        logger.warn(ComponentParser.textFormatted(msg, args).toStringStriped());
    }
    
    @Override
    public void warn(String msg, Throwable throwable){
        logger.warn(ComponentParser.text(msg).toStringStriped(), throwable);
    }
    
    @Override
    public void warn(String msg, Throwable throwable, Object... args){
        logger.warn(ComponentParser.textFormatted(msg, args).toStringStriped(), throwable);
    }
}

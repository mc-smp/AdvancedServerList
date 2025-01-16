package ch.andre601.advancedserverlist.core.parsing;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class TextCenterUtil{
    private final ComponentFlattener COMPONENT_FLATTENER = ComponentFlattener.textOnly();
    
    private final double MAX_WIDTH = 270.0; // Max width of the MOTD
    private final int SPACE_WIDTH = 4;
    
    private final Map<String, FontInfo> charWidths;
    
    public TextCenterUtil(AdvancedServerList<?> plugin){
        charWidths = loadFontWidths(plugin);
    }
    
    public String getCenteredText(String text){
        if(!text.toLowerCase(Locale.ROOT).startsWith("<center>"))
            return text;
        
        if(getDefault() == null)
            return text;
        
        text = text.substring("<center>".length());
        TextFlattenerListener listener = new TextFlattenerListener(this);
        
        COMPONENT_FLATTENER.flatten(ComponentParser.text(text).toComponent(), listener);
        
        double width = listener.getWidth();
        if(width >= MAX_WIDTH)
            return text;
        
        return createSpaces(width) + text;
    }
    
    private double getCharWidth(int codePoint, boolean isBold, String fontName){
        FontInfo font = charWidths.getOrDefault(fontName, getDefault());
        if(font == null)
            return -1.0;
        
        int index = Arrays.binarySearch(font.codePoints, codePoint);
        if(index < 0){
            return isBold ? 7.0 : 6.0;
        }else{
            return isBold ? font.advanceBf[index] : font.advance[index];
        }
    }
    
    private FontInfo getDefault(){
        return charWidths.get("minecraft:default");
    }
    
    private String createSpaces(double length){
        double remainder = MAX_WIDTH - length;
        int padding = (int)Math.max(0, Math.floor(remainder / 2.0) - 1);
        
        return " ".repeat(Math.floorDiv(padding, SPACE_WIDTH));
    }
    
    private Map<String, FontInfo> loadFontWidths(AdvancedServerList<?> plugin){
        try(InputStream stream = plugin.getClass().getResourceAsStream("/char-widths.json.gz")){
            if(stream == null){
                plugin.getPlugin().getPluginLogger().warn("Internal char-widths.json.gz couldn't be retrieved.");
                plugin.getPlugin().getPluginLogger().warn("The <center> Placeholder won't work.");
                return new HashMap<>();
            }
            
            GZIPInputStream gzipStream = new GZIPInputStream(stream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(gzipStream));
            
            return new Gson().fromJson(reader, new TypeToken<Map<String, FontInfo>>(){}.getType());
        }catch(IOException ex){
            plugin.getPlugin().getPluginLogger().warn("Encountered IOException while loading font widths.");
            plugin.getPlugin().getPluginLogger().warn("The <center> Placeholder won't work.");
            plugin.getPlugin().getPluginLogger().warn("Cause: <white>%s</white>", ex.getMessage());
            return new HashMap<>();
        }
    }
    
    private static class TextFlattenerListener implements FlattenerListener{
        private double width = 0.0;
        private boolean isBold = false;
        private String font = "minecraft:default";
        
        private final TextCenterUtil instance;
        
        public TextFlattenerListener(TextCenterUtil instance){
            this.instance = instance;
        }
        
        @Override
        public void pushStyle(@NotNull Style style){
            if(style.hasDecoration(TextDecoration.BOLD))
                isBold = true;
            
            if(style.font() != null)
                font = style.font().asString();
        }
        
        @Override
        public void component(@NotNull String text){
            for(int i = 0; i < text.length(); i++){
                width += instance.getCharWidth(text.codePointAt(i), isBold, font);
            }
        }
        
        @Override
        public void popStyle(@NotNull Style style){
            if(style.hasDecoration(TextDecoration.BOLD))
                isBold = false;
        }
        
        public double getWidth(){
            return width;
        }
    }
    
    private record FontInfo(int[] codePoints, float[] advance, float[] advanceBf){}
}

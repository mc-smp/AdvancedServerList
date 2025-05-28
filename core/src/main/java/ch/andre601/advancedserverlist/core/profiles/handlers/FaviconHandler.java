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

package ch.andre601.advancedserverlist.core.profiles.handlers;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class FaviconHandler<F>{
    
    private final AdvancedServerList<F> core;
    private final PluginLogger logger;
    private final ThreadPoolExecutor faviconThreadPool;
    private final Cache<String, CompletableFuture<FaviconHolder<F>>> faviconCache;
    
    private final Map<String, FaviconHolder<F>> localFavicons = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();
    private final Random random = new Random();
    
    public FaviconHandler(AdvancedServerList<F> core){
        this.core = core;
        this.logger = core.getPlugin().getPluginLogger();
        this.faviconThreadPool = createFaviconThreadPool();
        this.faviconCache = CacheBuilder.newBuilder()
            .expireAfterWrite(core.getFileHandler().getLong(1, 1, "faviconCacheTime"), TimeUnit.MINUTES)
            .build();
        
        loadLocalFavicons();
    }
    
    public F getFavicon(String input){
        if(!localFavicons.isEmpty()){
            logger.debug(FaviconHandler.class, "Current Local Favicons:");
            for(String key : localFavicons.keySet()){
                logger.debug(FaviconHandler.class, "  - %s", key);
            }
        }
        
        String[] parts = input.split(";");
        if(parts.length > 1){
            try{
                FaviconHolder<F> holder = faviconCache.get(input, () -> CompletableFuture.supplyAsync(() -> {
                    List<CompletableFuture<FaviconHolder<F>>> futures = new ArrayList<>();
                    for(String part : parts){
                        futures.add(getFuture(part, true));
                    }
                    
                    CompletableFuture<?>[] futuresArray = futures.toArray(new CompletableFuture<?>[0]);
                    List<FaviconHolder<F>> list = CompletableFuture.allOf(futuresArray)
                        .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
                        .getNow(null);
                    
                    if(list == null || list.isEmpty())
                        return null;
                    
                    return composeImage(list);
                })).getNow(null);
                
                if(holder == null)
                    return null;
                
                return holder.favicon();
            }catch(ExecutionException ex){
                logger.warn("Encountered an ExecutionException while composing Favicon!", ex);
                return null;
            }
        }
        
        if(input.equalsIgnoreCase("random")){
            logger.debug(FaviconHandler.class, "Input matches 'random'. Returning random Favicon...");
            return faviconOrNull(getRandomized());
        }
        
        if(localFavicons.containsKey(input.toLowerCase(Locale.ROOT))){
            logger.debug(FaviconHandler.class, "Input matches local favicon Image. Returning it...");
            return faviconOrNull(localFavicons.get(input.toLowerCase(Locale.ROOT)));
        }
        
        try{
            logger.debug(FaviconHandler.class, "Getting Favicon image from cache...");
            FaviconHolder<F> holder = faviconCache.get(input, () -> getFuture(input, false)).getNow(null);
            if(holder == null)
                return null;
            
            return holder.favicon();
        }catch(ExecutionException ex){
            logger.warn("Received ExecutionException while retrieving Favicon for '<white>%s</white>'.", ex, input);
            return null;
        }
    }
    
    public void cleanCache(){
        faviconCache.invalidateAll();
        loadLocalFavicons();
    }
    
    private CompletableFuture<FaviconHolder<F>> getFuture(String input, boolean skipFaviconCreation){
        if(input.toLowerCase(Locale.ROOT).startsWith("https://")){
            logger.debug(FaviconHandler.class, "Resolving URL '%s'...", input);
            return CompletableFuture.supplyAsync(() -> fromURL(core, input, skipFaviconCreation), this.faviconThreadPool);
        }else
        if(input.equalsIgnoreCase("random")){
            return CompletableFuture.completedFuture(getRandomized());
        }else
        if(input.toLowerCase(Locale.ROOT).endsWith(".png")){
            logger.debug(FaviconHandler.class, "Resolving image file '%s'...", input);
            return CompletableFuture.completedFuture(localFavicons.get(input.toLowerCase(Locale.ROOT)));
        }else{
            logger.debug(FaviconHandler.class, "Resolving Name/UUID as https://mc-heads.net/avatar/%s/64...", input);
            return CompletableFuture.supplyAsync(() -> fromURL(core, "https://mc-heads.net/avatar/" + input + "/64", skipFaviconCreation), this.faviconThreadPool);
        }
    }
    
    private FaviconHolder<F> getRandomized(){
        logger.debug(FaviconHandler.class, "Obtaining random Favicon...");
        if(localFavicons.isEmpty())
            return null;
        
        List<FaviconHolder<F>> faviconList = List.copyOf(localFavicons.values());
        
        // Don't use Random for just one entry.
        if(faviconList.size() == 1)
            return faviconList.getFirst();
        
        synchronized(random){
            return faviconList.get(random.nextInt(faviconList.size()));
        }
    }
    
    private void loadLocalFavicons(){
        this.localFavicons.clear();
        
        logger.info("[-] Loading local image files as Favicons...");
        
        Path folder = core.getPlugin().getFolderPath().resolve("favicons");
        if(!Files.exists(folder)){
            try{
                Files.createDirectories(folder);
                logger.debugSuccess(FaviconHandler.class, "Created favicons folder.");
            }catch(IOException ex){
                logger.failure("Cannot create favicons folder. Encountered an IOException.", ex);
                return;
            }
        }
        
        try(Stream<Path> pathStream = Files.list(folder)){
            pathStream.filter(Files::isRegularFile)
                .filter(file -> file.getFileName().toString().endsWith(".png"))
                .forEach(this::loadFile);
            
            logger.success("Loaded <white>%d</white> local Favicons.", localFavicons.size());
        }catch(IOException ex){
            logger.failure("Cannot load files from favicons folder.", ex);
        }
    }
    
    private void loadFile(Path path){
        try(InputStream stream = Files.newInputStream(path)){
            FaviconHolder<F> favicon = createFavicon(stream, false);
            if(favicon == null){
                logger.failure("Cannot create Favicon from file '<white>%s</white>'. Received Favicon was null.", path.getFileName().toString());
                return;
            }
            
            localFavicons.put(path.getFileName().toString().toLowerCase(Locale.ROOT), favicon);
            logger.debugSuccess(FaviconHandler.class, "Loaded file '<white>%s</white>' as Favicon.", path.getFileName().toString());
        }catch(IOException ex){
            logger.failure("Cannot create Favicon from file '<white>%s</white>'. Encountered IOException.", ex);
        }
    }
    
    private FaviconHolder<F> fromURL(AdvancedServerList<F> core, String url, boolean skipFaviconCreation){
        try{
            logger.debug(FaviconHandler.class, "Creating Request for URL '%s'...", url);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("User-Agent", "AdvancedServerList-" + core.getPlugin().getLoader() + "/" + core.getVersion())
                .build();
            
            try(InputStream stream = client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body()){
                return createFavicon(stream, skipFaviconCreation);
            }catch(InterruptedException | IOException ex){
                logger.warn("Cannot create Favicon from URL '<white>%s</white>'. Encountered an Exception.", ex, url);
                return null;
            }
        }catch(URISyntaxException ex){
            logger.warn("Cannot create Favicon from URL '<white>%s</white>'. Encountered a URISyntaxException.", ex, url);
            return null;
        }
    }
    
    private FaviconHolder<F> createFavicon(InputStream stream, boolean skipFaviconCreation){
        try{
            logger.debug(FaviconHandler.class, "Creating BufferedImage from InputStream...");
            BufferedImage original = ImageIO.read(stream);
            if(original == null){
                logger.warn("Cannot create Favicon. Received null BufferedImage.");
                return null;
            }
            
            // Don't waste resources resizing images already having right size.
            if(original.getWidth() == 64 && original.getHeight() == 64){
                logger.debug(FaviconHandler.class, "BufferedImage is 64x64 pixels. No resizing needed.");
                return new FaviconHolder<>(original, skipFaviconCreation ? null : core.getPlugin().createFavicon(original));
            }
            
            logger.debug(FaviconHandler.class, "Resizing image to 64x64 pixels...");
            BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = image.createGraphics();
            
            graphics2D.drawImage(original, 0, 0, 64, 64, null);
            graphics2D.dispose();
            
            logger.debug(FaviconHandler.class, "Image resized! Returning Favicon...");
            return new FaviconHolder<>(image, skipFaviconCreation ? null : core.getPlugin().createFavicon(image));
        }catch(Exception ex){
            logger.warn("Unable to create Favicon. Received an Exception.", ex);
            return null;
        }
    }
    
    private FaviconHolder<F> composeImage(List<FaviconHolder<F>> holders){
        try{
            logger.debug(FaviconHandler.class, "Composing favicon from %d parts...", holders.size());
            BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            
            for(FaviconHolder<F> holder : holders)
                graphics.drawImage(holder.image(), 0, 0, 64, 64, null);
            
            graphics.dispose();
            
            return new FaviconHolder<>(image, core.getPlugin().createFavicon(image));
        }catch(Exception ex){
            logger.warn("Unable to compose Favicon. Received an Exception.", ex);
            return null;
        }
    }
    
    private ThreadPoolExecutor createFaviconThreadPool(){
        return new ThreadPoolExecutor(3, 3, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024), r -> {
            Thread t = new Thread(r, "AdvancedServerList-FaviconThread");
            t.setDaemon(true);
            return t;
        });
    }
    
    private F faviconOrNull(FaviconHolder<F> holder){
        if(holder == null)
            return null;
        
        return holder.favicon();
    }
    
    public record FaviconHolder<F>(BufferedImage image, F favicon){}
}

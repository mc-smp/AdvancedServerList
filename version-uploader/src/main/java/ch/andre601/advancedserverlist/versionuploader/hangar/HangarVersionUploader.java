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

package ch.andre601.advancedserverlist.versionuploader.hangar;

import ch.andre601.advancedserverlist.versionuploader.PlatformInfo;
import ch.andre601.advancedserverlist.versionuploader.ReleaseHolder;
import ch.andre601.advancedserverlist.versionuploader.VersionUploader;
import ch.andre601.advancedserverlist.versionuploader.data.CodebergRelease;
import ch.andre601.advancedserverlist.versionuploader.hangar.version.Dependency;
import ch.andre601.advancedserverlist.versionuploader.hangar.version.MultipartObject;
import ch.andre601.advancedserverlist.versionuploader.hangar.version.Platform;
import ch.andre601.advancedserverlist.versionuploader.hangar.version.Version;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpMessage;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class HangarVersionUploader{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HangarVersionUploader.class);
    private static final String API_URL = "https://hangar.papermc.io/api/v1/";
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String apiKey;
    private ActiveJWT activeJWT;
    
    public HangarVersionUploader(){
        this.apiKey = System.getenv("HANGAR_API_TOKEN");
    }
    
    public CompletableFuture<?> performUpload(CodebergRelease release, ReleaseHolder releaseInfo, boolean dryrun){
        LOGGER.info("Starting HangarVersionUploader...");
        
        if((apiKey == null || apiKey.isEmpty()) && !dryrun){
            LOGGER.warn("Received a null/empty API key!");
            return CompletableFuture.completedFuture(null);
        }
        
        final String releaseVersion = release.tagName().startsWith("v") ? release.tagName().substring(1) : release.tagName();
        final String pluginVersion = VersionUploader.retrieveVersion();
        
        if(pluginVersion == null || pluginVersion.isEmpty()){
            LOGGER.warn("Retrieved null/empty plugin version.");
            return CompletableFuture.completedFuture(false);
        }
        
        final String changelog = release.body();
        boolean preRelease = release.prerelease();
        
        final List<Path> filePaths = List.of(
            new File(PlatformInfo.PAPER.getFilePaths().get(0).replace("{{version}}", pluginVersion)).toPath(),
            new File(PlatformInfo.BUNGEECORD.getFilePaths().get(0).replace("{{version}}", pluginVersion)).toPath(),
            new File(PlatformInfo.VELOCITY.getFilePaths().get(0).replace("{{version}}", pluginVersion)).toPath()
        );
        
        final List<Dependency> paperDependencies = List.of(
            Dependency.fromNamespace("ViaVersion", false),
            Dependency.fromNamespace("Maintenance", false),
            Dependency.fromNamespace("PlaceholderAPI", false),
            Dependency.fromUrl("ServerListPlus", "https://github.com/Minecrell/ServerListPlus", false)
        );
        final List<Dependency> bungeeDependencies = List.of(
            Dependency.fromNamespace("PapiProxyBridge", false),
            Dependency.fromNamespace("Maintenance", false),
            Dependency.fromUrl("ServerListPlus", "https://github.com/Minecrell/ServerListPlus", false)
        );
        final List<Dependency> velocityDependencies = List.of(
            Dependency.fromNamespace("PapiProxyBridge", false),
            Dependency.fromNamespace("Maintenance", false),
            Dependency.fromUrl("ServerListPlus", "https://github.com/Minecrell/ServerListPlus", false)
        );
        
        final List<MultipartObject> fileInfo = List.of(
            new MultipartObject(Collections.singletonList(Platform.PAPER), null),
            new MultipartObject(Collections.singletonList(Platform.WATERFALL), null),
            new MultipartObject(Collections.singletonList(Platform.VELOCITY), null)
        );
        
        final Version versionUpload = new Version(
            releaseVersion,
            Map.of(
                Platform.PAPER, paperDependencies,
                Platform.WATERFALL, bungeeDependencies,
                Platform.VELOCITY, velocityDependencies
            ),
            Map.of(
                Platform.PAPER, List.of("1.20.6", "1.21.x"),
                Platform.WATERFALL, List.of("1.19.x", "1.20.x", "1.21"),
                Platform.VELOCITY, Collections.singletonList("3.2")
            ),
            changelog.replaceAll("\r\n", "\n"),
            fileInfo,
            preRelease ? "Beta" : "Release"
        );
        
        if(dryrun){
            String json = GSON.toJson(versionUpload);
            
            LOGGER.info("Namespace: AdvancedServerList");
            LOGGER.info("JSON: {}", json);
            
            return CompletableFuture.completedFuture(json);
        }
        
        try{
            HttpClient client = HttpClients.createDefault();
            return uploadVersion(client, versionUpload, filePaths, releaseInfo);
        }catch(ParseException | IOException ex){
            LOGGER.warn("Encountered an exception while uploading to Hangar!", ex);
            return CompletableFuture.completedFuture(ex);
        }
    }
    
    private CompletableFuture<?> uploadVersion(HttpClient client, Version versionUpload, List<Path> filePaths, ReleaseHolder releaseInfo) throws IOException, ParseException{
        return CompletableFuture.supplyAsync(() -> {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("versionUpload", new StringBody(GSON.toJson(versionUpload), ContentType.APPLICATION_JSON));
            
            for(Path path : filePaths){
                builder.addPart("files", new FileBody(path.toFile(), ContentType.DEFAULT_BINARY));
            }
            
            HttpPost post = new HttpPost(API_URL + "projects/AdvancedServerList/upload");
            post.setEntity(builder.build());
            try{
                this.addAuthHeader(client, post);
                
                LOGGER.info("Uploading release...");
                return client.execute(post, response -> {
                    if(response.getCode() != 200){
                        String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                        
                        throw new RuntimeException(String.format(
                            "Received non-successful response while uploading release.\nCode: %d\nMessage: %s\nBody: %s",
                            response.getCode(),
                            response.getReasonPhrase(),
                            body
                        ));
                    }
                    LOGGER.info("Successfully uploaded new release on Hangar!");
                    
                    String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    try{
                        JsonObject json = GSON.fromJson(body, JsonObject.class);
                        if(json == null)
                            return true;
                        
                        releaseInfo.addRelease(
                            "hangar",
                            "Paper, Waterfall, Velocity",
                            json.getAsJsonPrimitive("url").getAsString()
                        );
                        return true;
                    }catch(JsonSyntaxException ex){
                        return true;
                    }
                });
            }catch(IOException ex){
                LOGGER.warn("Encountered IOException while perfoming upload.", ex);
                return false;
            }
        });
    }
    
    private synchronized void addAuthHeader(HttpClient client, HttpMessage message) throws IOException{
        LOGGER.info("Applying Authorization Header...");
        if(this.activeJWT != null && !this.activeJWT.hasExpired()){
            message.addHeader("Authorization", this.activeJWT.jwt());
            LOGGER.info("Authorization Header applied. Expires at {}", getDate(this.activeJWT.expiresAt()));
            return;
        }
        
        LOGGER.info("Current JWT expired. Requesting new one...");
        ActiveJWT jwt = client.execute(new HttpPost(API_URL + "authenticate?apiKey=" + this.apiKey), response -> {
            if(response.getCode() == 400){
                LOGGER.error("Bad JWT request; is the API-token valid?");
                return null;
            }else
            if(response.getCode() != 200){
                LOGGER.error("Received non-successful response code {}: {}", response.getCode(), response.getReasonPhrase());
                return null;
            }
            
            final String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            final JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
            final String token = jsonObject.getAsJsonPrimitive("token").getAsString();
            final long expiresIn = jsonObject.getAsJsonPrimitive("expiresIn").getAsLong();
    
            LOGGER.info("Received new JWT!");
            return new ActiveJWT(token, System.currentTimeMillis() + expiresIn);
        });
        
        if(jwt == null){
            throw new RuntimeException("Error getting JWT.");
        }
        
        this.activeJWT = jwt;
        
        LOGGER.info("Authorization Header applied. Expires at {}", getDate(this.activeJWT.expiresAt()));
        message.addHeader("Authorization", jwt.jwt());
    }
    
    private synchronized String getDate(long timestamp){
        return timestamp + " (" + new Date(timestamp) + ")";
    }
    
    private record ActiveJWT(String jwt, long expiresAt){
        
        public boolean hasExpired(){
            return System.currentTimeMillis() < this.expiresAt + TimeUnit.SECONDS.toMillis(3);
        }
    }
}

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

package ch.andre601.advancedserverlist.versionuploader.modrinth;

import ch.andre601.advancedserverlist.versionuploader.PlatformInfo;
import ch.andre601.advancedserverlist.versionuploader.ReleaseHolder;
import ch.andre601.advancedserverlist.versionuploader.VersionUploader;
import ch.andre601.advancedserverlist.versionuploader.data.CodebergRelease;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import masecla.modrinth4j.client.agent.UserAgent;
import masecla.modrinth4j.endpoints.version.CreateVersion;
import masecla.modrinth4j.main.ModrinthAPI;
import masecla.modrinth4j.model.version.ProjectVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModrinthVersionUploader{
    
    private final Logger logger = LoggerFactory.getLogger(ModrinthVersionUploader.class);
    
    private final String versionUrl = "https://modrinth.com/plugin/advancedserverlist/version/";
    
    private final List<PlatformInfo> platforms = List.of(
        PlatformInfo.PAPER,
        PlatformInfo.BUNGEECORD,
        PlatformInfo.VELOCITY
    );
    private final List<String> paperVersions = List.of(
        "1.20.6",
        "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4"
    );
    private final List<String> versions = List.of(
        "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
        "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4"
    );
    
    private final ModrinthAPI api;
    
    public ModrinthVersionUploader(){
        this.api = createClient();
    }
    
    public CompletableFuture<?> performUpload(CodebergRelease release, ReleaseHolder releaseHolder, boolean dryrun){
        logger.info("Starting ModrinthVersionUploader...");
        
        if(api == null){
            logger.warn("Cannot create updates on Modrinth. API instance is null.");
            return CompletableFuture.completedFuture(false);
        }
        
        final String releaseVersion = release.tagName().startsWith("v") ? release.tagName().substring(1) : release.tagName();
        final String pluginVersion = VersionUploader.retrieveVersion();
        
        if(pluginVersion == null || pluginVersion.isEmpty()){
            logger.warn("Retrieved null/empty plugin version.");
            return CompletableFuture.completedFuture(false);
        }
        
        final String changelog = release.body();
        final boolean prerelease = release.prerelease();
        
        CompletableFuture<?>[] futures = new CompletableFuture[platforms.size()];
        
        List<ProjectVersion.ProjectDependency> paperDependencies = List.of(
            new ProjectVersion.ProjectDependency(null, "VCAqN1ln", null, ProjectVersion.ProjectDependencyType.OPTIONAL), // Maintenance
            new ProjectVersion.ProjectDependency(null, "P1OZGk5p", null, ProjectVersion.ProjectDependencyType.OPTIONAL)  // ViaVersion
        );
        List<ProjectVersion.ProjectDependency> bungeeDependencies = List.of(
            new ProjectVersion.ProjectDependency(null, "VCAqN1ln", null, ProjectVersion.ProjectDependencyType.OPTIONAL), // Maintenance
            new ProjectVersion.ProjectDependency(null, "bEIUEGTX", null, ProjectVersion.ProjectDependencyType.OPTIONAL)  // PAPIProxyBridge
        );
        List<ProjectVersion.ProjectDependency> velocityDependencies = List.of(
            new ProjectVersion.ProjectDependency(null, "VCAqN1ln", null, ProjectVersion.ProjectDependencyType.OPTIONAL), // Maintenance
            new ProjectVersion.ProjectDependency(null, "bEIUEGTX", null, ProjectVersion.ProjectDependencyType.OPTIONAL)  // PAPIProxyBridge
        );
        
        for(int i = 0; i < platforms.size(); i++){
            PlatformInfo platformInfo = platforms.get(i);
            logger.info("Creating release for platform {}...", platformInfo.getPlatform());
            
            List<File> files = platformInfo.getFilePaths().stream()
                .map(path -> new File(path.replace("{{version}}", pluginVersion)))
                .filter(File::exists)
                .toList();
            
            CreateVersion.CreateVersionRequest.CreateVersionRequestBuilder builder = CreateVersion.CreateVersionRequest.builder()
                .projectId("xss83sOY")
                .name(String.format("v%s (%s)", releaseVersion, String.join(", ", platformInfo.getLoaders())))
                .versionNumber(releaseVersion)
                .changelog(changelog.replaceAll("\r\n", "\n"))
                .featured(false)
                .files(files)
                .gameVersions(platformInfo == PlatformInfo.PAPER ? paperVersions : versions)
                .loaders(platformInfo.getLoaders())
                .versionType(prerelease ? ProjectVersion.VersionType.BETA : ProjectVersion.VersionType.RELEASE);
            
            switch(platformInfo){
                case PAPER -> builder.dependencies(paperDependencies);
                case BUNGEECORD -> builder.dependencies(bungeeDependencies);
                case VELOCITY -> builder.dependencies(velocityDependencies);
            }
            
            if(dryrun){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(builder.build());
                
                logger.info("JSON Data to send: {}", json);
                
                futures[i] = CompletableFuture.completedFuture(json);
                continue;
            }
            
            futures[i] = api.versions().createProjectVersion(builder.build()).whenComplete(((projectVersion, throwable) -> {
                if(throwable != null){
                    logger.warn("Encountered an exception while uploading a new release to Modrinth!", throwable);
                    return;
                }
                
                String url = versionUrl + projectVersion.getId();
                
                logger.info("Created new release!");
                logger.info("Link:     {}", url);
                logger.info("Platform: {}", platformInfo.getPlatform());
                
                releaseHolder.addRelease(
                    "modrinth",
                    String.join(", ", platformInfo.getPlatformNames()),
                    url
                );
            }));
        }
        
        return CompletableFuture.allOf(futures);
    }
    
    private ModrinthAPI createClient(){
        String token = System.getenv("MODRINTH_API_TOKEN");
        if(token == null || token.isEmpty())
            return null;
        
        UserAgent agent = UserAgent.builder()
            .authorUsername("Andre_601")
            .projectVersion("v1.0.0")
            .projectName("ModrinthVersionUploader")
            .contact("github@andre601.ch")
            .build();
        
        return ModrinthAPI.rateLimited(agent, token);
    }
}

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

package ch.andre601.advancedserverlist.core.migration.serverlistplus;

import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileSerializer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SLPConfigMigrator{
    
    public static boolean migrate(AdvancedServerList<?> core){
        Path path = core.getPlugin().getFolderPath().getParent().resolve("ServerListPlus");
        PluginLogger logger = core.getPlugin().getPluginLogger();
        if(!Files.exists(path) || !Files.isDirectory(path)){
            logger.warn("No ServerListPlus folder found or it wasn't a folder.");
            return false;
        }
        
        Path file = path.resolve("ServerListPlus.yml");
        if(!Files.exists(file) || !Files.isRegularFile(file)){
            logger.warn("No ServerListPlus.yml file found or it wasn't a regular file.");
            return false;
        }
        
        SLPConfig yaml = null;
        try(BufferedReader reader = Files.newBufferedReader(file)){
            Constructor constructor = new Constructor(SLPConfig.class);
            Representer representer = new Representer();
            TypeDescription slpDescription = new TypeDescription(SLPConfig.class);
            slpDescription.substituteProperty("Default", SLPConfig.Options.class, "getDef", "setDef");
            slpDescription.substituteProperty("Personalized", SLPConfig.Options.class, "getPersonalized", "setPersonalized");
            representer.addTypeDescription(slpDescription);
            yaml = new Yaml(constructor, representer).load(reader);
            
            
        }catch(IOException ex){
            logger.warn("Encountered IOException while migrating ServerListPlus configuration!", ex);
            return false;
        }
        
        if(yaml == null){
            logger.warn("Unable to migrate ServerListPlus configuration. Retrieved YAML was null.");
            return false;
        }
        
        boolean defSuccess = createFile(core, yaml.getDef(), logger, "slp_default.yml", 0);
        boolean personalSuccess = createFile(core, yaml.getPersonalized(), logger, "slp_personalized.yml", 1);
        
        if(!defSuccess && !personalSuccess){
            logger.warn("Migration of ServerListPlus configuration was not successfull! Check previous entries for warnings.");
            return false;
        }
        
        if(defSuccess != personalSuccess){
            logger.warn("Only one profile has been successfully migrated from ServerListPlus!");
            logger.warn("  - Default migrated?      %s", (defSuccess ? "Yes" : "No"));
            logger.warn("  - Personalized migrated? %s", (personalSuccess ? "Yes" : "No"));
        }
        
        logger.info("Migration completed!");
        
        return true;
    }
    
    private static boolean createFile(AdvancedServerList<?> core, SLPConfig.Options options, PluginLogger logger, String fileName, int priority){
        List<String> motds = options.getDescription();
        List<String> hovers = options.getPlayers().getHover();
        List<String> favicons = options.getFavicon().getFiles();
        
        ProfileEntry.Builder defProfileBuilder = ProfileEntry.empty().builder();
        List<ProfileEntry.Builder> profileBuilders = new ArrayList<>();
        
        int size = motds.size();
        if(hovers.size() > size)
            size = hovers.size();
        
        if(favicons.size() > size)
            size = favicons.size();
        
        for(int i = 0; i < size; i++){
            profileBuilders.add(ProfileEntry.empty().builder());
        }
        
        if(motds.size() == 1){
            List<String> lines = Arrays.stream(motds.get(0).split("\n")).toList();
            defProfileBuilder.motd(lines);
        }else
        if(motds.size() > 1){
            for(int i = 0; i < motds.size(); i++){
                List<String> lines = Arrays.stream(motds.get(i).split("\n")).toList();
                profileBuilders.get(i).motd(lines);
            }
        }
        
        if(hovers.size() == 1){
            List<String> lines = Arrays.stream(hovers.get(0).split("\n")).toList();
            defProfileBuilder.players(lines);
        }else
        if(hovers.size() > 1){
            for(int i = 0; i < hovers.size(); i++){
                List<String> lines = Arrays.stream(hovers.get(i).split("\n")).toList();
                profileBuilders.get(i).players(lines);
            }
        }
        
        List<ProfileEntry> profiles = profileBuilders.stream()
            .map(ProfileEntry.Builder::build)
            .toList();
        ProfileEntry defProfile = defProfileBuilder.build();
        
        Path newFile = core.getPlugin().getFolderPath().resolve("profiles").resolve(fileName);
        if(Files.exists(newFile)){
            logger.warn("Cannot migrate configuration from ServerListPlus. File named %s already exists!", fileName);
            return false;
        }
        
        try{
            Files.createFile(newFile);
        }catch(IOException ex){
            logger.warn("IOException while creating new file for migration.", ex);
            return false;
        }
        
        try{
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .defaultOptions(opt -> opt.serializers(builder -> builder.register(ProfileEntry.class, ProfileSerializer.INSTANCE)))
                .path(newFile)
                .build();
            
            ConfigurationNode node = loader.load();
            if(node == null)
                return false;
            
            node.node("priority").set(priority);
            node.node("condition").set(priority > 0 ? "${player name} != \"anonymous\"" : "");
            
            node.node("profiles").set(profiles);
            node.set(defProfile);
            
            loader.save(node);
            
            logger.info("Created new file '%s'!", fileName);
            return true;
        }catch(IOException ex){
            logger.warn("Encountered IOException while trying to save migrated configuration file.", ex);
            return false;
        }
    }
}

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

import java.util.List;

public class SLPConfig{
    
    private Options def;
    private Options personalized;
    
    public Options getDef(){
        return def;
    }
    
    public Options getPersonalized(){
        return personalized;
    }
    
    public void setDef(Options def){
        this.def = def;
    }
    
    public void setPersonalized(Options personalized){
        this.personalized = personalized;
    }
    
    public static class Options{
        private List<String> description;
        private Players players;
        private Favicon favicon;
        
        public List<String> getDescription(){
            return description;
        }
        
        public Players getPlayers(){
            return players;
        }
        
        public Favicon getFavicon(){
            return favicon;
        }
        
        public void setDescription(List<String> description){
            this.description = description;
        }
        
        public void setPlayers(Players players){
            this.players = players;
        }
        
        public void setFavicon(Favicon favicon){
            this.favicon = favicon;
        }
    }
    
    public static class Players{
        private List<String> hover;
        
        public List<String> getHover(){
            return hover;
        }
        
        public void setHover(List<String> hover){
            this.hover = hover;
        }
    }
    
    public static class Favicon{
        private List<String> files;
        
        public List<String> getFiles(){
            return files;
        }
        
        public void setFiles(List<String> files){
            this.files = files;
        }
    }
}

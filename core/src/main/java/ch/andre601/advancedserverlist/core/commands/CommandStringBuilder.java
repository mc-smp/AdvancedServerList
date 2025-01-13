/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Andre_601
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
 */

package ch.andre601.advancedserverlist.core.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandStringBuilder{
    
    private final List<CommandPart> commandParts = new ArrayList<>();
    
    public CommandStringBuilder(String subcommand, String description){
        appendText("<hover:show_text:\"<grey>" + description + "</grey>\"><white>" + subcommand + "</white></hover>");
    }
    
    public CommandStringBuilder appendArgument(Argument argument){
        this.commandParts.add(argument);
        return this;
    }
    
    public CommandStringBuilder appendText(String text){
        this.commandParts.add(new Text(text));
        return this;
    }
    
    public CommandStringBuilder appendLiteralArgument(String name, String description){
        return appendText("<hover:show_text:\"<grey>" + description + "</grey>\"><white>" + name + "</white></hover>");
    }
    
    public CommandStringBuilder appendSpace(){
        return appendText(" ");
    }
    
    public String build(){
        StringBuilder builder = new StringBuilder("<aqua>/asl ");
        for(CommandPart part : commandParts){
            builder.append(part.build());
        }
        builder.append("</aqua>"); // Closing the first color tag.
        
        return builder.toString();
    }
    
    public static class ArgumentBuilder{
        private final String name;
        
        private String description = "No description provided.";
        private boolean required = false;
        
        public ArgumentBuilder(String name){
            this.name = name;
        }
        
        public ArgumentBuilder description(String description){
            this.description = description;
            return this;
        }
        
        public ArgumentBuilder required(){
            this.required = true;
            return this;
        }
        
        public Argument build(){
            return new Argument(name, description, required);
        }
    }
    
    private record Text(String text) implements CommandPart{
        @Override
        public String build(){
            return text();
        }
    }
    
    private record Argument(String name, String description, boolean required) implements CommandPart{
        @Override
        public String build(){
            return "<hover:show_text:\"<grey>" + description() + "</grey>\">" +
                "<grey>" + (required() ? "\\<" : "[") + "<white>" + name() + "</white>" + (required() ? ">" : "]") +
                "</grey></hover>";
        }
    }
    
    private interface CommandPart{
        String build();
    }
}

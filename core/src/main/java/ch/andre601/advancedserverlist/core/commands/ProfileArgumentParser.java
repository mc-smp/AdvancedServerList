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

import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;

import java.util.List;

public class ProfileArgumentParser<C> implements ArgumentParser<C, String>{
    
    public static <C> ParserDescriptor<C, String> profileParser(List<ServerListProfile> profiles){
        return ParserDescriptor.of(new ProfileArgumentParser<C>(profiles), String.class);
    }
    
    private final List<String> profiles;
    
    private ProfileArgumentParser(List<ServerListProfile> profiles){
        this.profiles = profiles.stream()
            .map(profile -> profile.file().substring(0, profile.file().lastIndexOf('.')))
            .toList();
    }
    
    @Override
    public @NonNull ArgumentParseResult<@NonNull String> parse(@NonNull CommandContext<@NonNull C> commandContext, @NonNull CommandInput commandInput){
        if(profiles.contains(commandInput.readString())){
            return ArgumentParseResult.success(commandInput.readString());
        }else{
            return ArgumentParseResult.failure(new IllegalArgumentException("Unknown profile " + commandInput.readString()));
        }
    }
}

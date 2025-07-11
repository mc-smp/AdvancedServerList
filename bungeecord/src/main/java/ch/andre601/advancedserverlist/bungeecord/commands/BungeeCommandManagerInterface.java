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

package ch.andre601.advancedserverlist.bungeecord.commands;

import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.interfaces.commands.CommandManagerInterface;
import net.md_5.bungee.api.CommandSender;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.bungee.BungeeCommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;

public class BungeeCommandManagerInterface implements CommandManagerInterface<CommandSender>{
    
    private final BungeeCordCore plugin;
    
    public BungeeCommandManagerInterface(BungeeCordCore plugin){
        this.plugin = plugin;
    }
    
    @Override
    public CommandManager<CmdSender> commandHandler(){
        return new BungeeCommandManager<>(
            plugin,
            ExecutionCoordinator.asyncCoordinator(),
            senderMapper()
        );
    }
    
    @Override
    public SenderMapper<CommandSender, CmdSender> senderMapper(){
        return SenderMapper.create(
            cmdSender -> new BungeeCmdSender(cmdSender, plugin.getAudiences()),
            sender -> ((BungeeCmdSender)sender).sender()
        );
    }
}

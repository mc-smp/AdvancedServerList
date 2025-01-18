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

package ch.andre601.advancedserverlist.velocity.commands;

import ch.andre601.advancedserverlist.core.interfaces.commands.CommandHandler;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.VelocityCommandManager;

public class VelocityCommandHandler implements CommandHandler<CommandSource, VelocityCmdSender>{
    
    private final PluginContainer pluginContainer;
    private final ProxyServer proxy;
    
    public VelocityCommandHandler(PluginContainer pluginContainer, ProxyServer proxy){
        this.pluginContainer = pluginContainer;
        this.proxy = proxy;
    }
    
    @Override
    public CommandManager<VelocityCmdSender> commandHandler(){
        return new VelocityCommandManager<>(
            pluginContainer,
            proxy,
            ExecutionCoordinator.asyncCoordinator(),
            senderMapper()
        );
    }
    
    @Override
    public SenderMapper<CommandSource, VelocityCmdSender> senderMapper(){
        return SenderMapper.create(
            VelocityCmdSender::new,
            VelocityCmdSender::sender
        );
    }
}

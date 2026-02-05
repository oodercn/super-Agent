package net.ooder.sdk.command.api;

import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.network.packet.CommandPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommandInterceptorChain {
    private final List<CommandInterceptor> interceptors = new ArrayList<>();
    
    public void addInterceptor(CommandInterceptor interceptor) {
        interceptors.add(interceptor);
        // 按优先级排序
        Collections.sort(interceptors, Comparator.comparingInt(CommandInterceptor::getPriority));
    }
    
    public void removeInterceptor(CommandInterceptor interceptor) {
        interceptors.remove(interceptor);
    }
    
    public boolean executeBefore(CommandPacket packet) {
        for (CommandInterceptor interceptor : interceptors) {
            if (!interceptor.beforeExecute(packet)) {
                return false;
            }
        }
        return true;
    }
    
    public void executeAfter(CommandPacket packet, CommandResult result) {
        for (CommandInterceptor interceptor : interceptors) {
            interceptor.afterExecute(packet, result);
        }
    }
    
    public void executeOnError(CommandPacket packet, Exception e, CommandResult result) {
        for (CommandInterceptor interceptor : interceptors) {
            interceptor.onError(packet, e, result);
        }
    }
    
    public List<CommandInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }
    
    public boolean isEmpty() {
        return interceptors.isEmpty();
    }
}

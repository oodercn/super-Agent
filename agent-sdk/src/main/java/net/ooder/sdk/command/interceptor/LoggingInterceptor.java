package net.ooder.sdk.command.interceptor;

import net.ooder.sdk.command.api.CommandInterceptor;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.network.packet.CommandPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingInterceptor implements CommandInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final int priority;
    
    public LoggingInterceptor() {
        this.priority = 100;
    }
    
    public LoggingInterceptor(int priority) {
        this.priority = priority;
    }
    
    @Override
    public boolean beforeExecute(CommandPacket packet) {
        log.info("Starting to execute command: {} from {} to {}", 
                packet.getOperation(), packet.getSenderId(), packet.getReceiverId());
        log.debug("Command packet details: {}", packet);
        return true;
    }
    
    @Override
    public void afterExecute(CommandPacket packet, CommandResult result) {
        log.info("Command execution completed: {} with status: {}", 
                packet.getOperation(), result.getStatus());
        if (result.getData() != null) {
            log.debug("Command result data: {}", result.getData());
        }
    }
    
    @Override
    public void onError(CommandPacket packet, Exception e, CommandResult result) {
        log.error("Command execution failed: {} with error: {}", 
                packet.getOperation(), e.getMessage(), e);
        log.debug("Error details: {}", result);
    }
    
    @Override
    public int getPriority() {
        return priority;
    }
}

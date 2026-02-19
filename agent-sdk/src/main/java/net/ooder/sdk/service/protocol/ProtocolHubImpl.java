package net.ooder.sdk.service.protocol;

import net.ooder.sdk.api.protocol.CommandPacket;
import net.ooder.sdk.api.protocol.CommandResult;
import net.ooder.sdk.api.protocol.ProtocolHandler;
import net.ooder.sdk.api.protocol.ProtocolHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Protocol Hub Implementation
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class ProtocolHubImpl implements ProtocolHub {

    private static final Logger log = LoggerFactory.getLogger(ProtocolHubImpl.class);

    private final Map<String, ProtocolHandler> handlers;
    private final Map<String, ProtocolStats> stats;
    private final ExecutorService executor;
    private final AtomicBoolean running;

    public ProtocolHubImpl() {
        this.handlers = new ConcurrentHashMap<String, ProtocolHandler>();
        this.stats = new ConcurrentHashMap<String, ProtocolStats>();
        this.executor = Executors.newFixedThreadPool(4);
        this.running = new AtomicBoolean(false);
        log.info("ProtocolHubImpl created");
    }

    @Override
    public void registerProtocolHandler(String protocolType, ProtocolHandler handler) {
        log.info("Registering protocol handler: {}", protocolType);
        
        handlers.put(protocolType, handler);
        
        ProtocolStats protocolStats = new ProtocolStats();
        protocolStats.setProtocolType(protocolType);
        stats.put(protocolType, protocolStats);
        
        handler.initialize();
        log.info("Protocol handler registered: {}", protocolType);
    }

    @Override
    public void unregisterProtocolHandler(String protocolType) {
        log.info("Unregistering protocol handler: {}", protocolType);
        
        ProtocolHandler handler = handlers.remove(protocolType);
        if (handler != null) {
            handler.shutdown();
        }
        stats.remove(protocolType);
        
        log.info("Protocol handler unregistered: {}", protocolType);
    }

    @Override
    public boolean hasHandler(String protocolType) {
        return handlers.containsKey(protocolType);
    }

    @Override
    public List<String> getSupportedProtocols() {
        return new ArrayList<String>(handlers.keySet());
    }

    @Override
    public CommandResult handleCommand(CommandPacket packet) {
        String protocolType = packet.getHeader().getProtocolType();
        log.debug("Handling command: {} for protocol: {}", packet.getHeader().getCommandType(), protocolType);
        
        ProtocolHandler handler = handlers.get(protocolType);
        if (handler == null) {
            log.warn("No handler found for protocol: {}", protocolType);
            return CommandResult.failure("PROTOCOL_NOT_FOUND", "No handler for protocol: " + protocolType);
        }

        ProtocolStats protocolStats = stats.get(protocolType);
        long startTime = System.currentTimeMillis();
        
        try {
            if (!handler.validateCommand(packet)) {
                log.warn("Command validation failed for packet: {}", packet.getPacketId());
                if (protocolStats != null) {
                    protocolStats.incrementTotal();
                    protocolStats.incrementFailed();
                }
                return CommandResult.failure("VALIDATION_FAILED", "Command validation failed");
            }

            CommandResult result = handler.handleCommand(packet);
            long processingTime = System.currentTimeMillis() - startTime;
            result.setProcessingTime(processingTime);
            result.setResponderId(handler.getProtocolType());
            
            if (protocolStats != null) {
                protocolStats.incrementTotal();
                if (result.isSuccess()) {
                    protocolStats.incrementSuccess();
                } else {
                    protocolStats.incrementFailed();
                }
                protocolStats.setLastCommandTime(System.currentTimeMillis());
            }
            
            log.debug("Command handled: {} in {}ms", packet.getPacketId(), processingTime);
            return result;
            
        } catch (Exception e) {
            log.error("Error handling command: {}", packet.getPacketId(), e);
            if (protocolStats != null) {
                protocolStats.incrementTotal();
                protocolStats.incrementFailed();
            }
            return CommandResult.failure("HANDLER_ERROR", e.getMessage());
        }
    }

    @Override
    public CompletableFuture<CommandResult> handleCommandAsync(CommandPacket packet) {
        return CompletableFuture.supplyAsync(() -> handleCommand(packet), executor);
    }

    @Override
    public CommandResult handleCommand(CommandPacket packet, String target) {
        packet.getHeader().setProtocolType(target);
        return handleCommand(packet);
    }

    @Override
    public ProtocolStats getProtocolStats(String protocolType) {
        return stats.get(protocolType);
    }

    @Override
    public Map<String, ProtocolStats> getAllStats() {
        return new HashMap<String, ProtocolStats>(stats);
    }

    @Override
    public void resetStats(String protocolType) {
        ProtocolStats protocolStats = stats.get(protocolType);
        if (protocolStats != null) {
            protocolStats.setTotalCommands(0);
            protocolStats.setSuccessfulCommands(0);
            protocolStats.setFailedCommands(0);
            protocolStats.setAverageProcessingTime(0);
            log.info("Stats reset for protocol: {}", protocolType);
        }
    }

    @Override
    public void initialize() {
        log.info("Initializing ProtocolHub");
        running.set(true);
        
        for (ProtocolHandler handler : handlers.values()) {
            try {
                handler.initialize();
            } catch (Exception e) {
                log.error("Failed to initialize handler: {}", handler.getProtocolType(), e);
            }
        }
        
        log.info("ProtocolHub initialized with {} handlers", handlers.size());
    }

    @Override
    public void shutdown() {
        log.info("Shutting down ProtocolHub");
        running.set(false);
        
        for (ProtocolHandler handler : handlers.values()) {
            try {
                handler.shutdown();
            } catch (Exception e) {
                log.error("Failed to shutdown handler: {}", handler.getProtocolType(), e);
            }
        }
        
        handlers.clear();
        stats.clear();
        executor.shutdown();
        
        log.info("ProtocolHub shutdown complete");
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }
}

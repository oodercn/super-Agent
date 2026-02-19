package net.ooder.sdk.core.cmd.impl;

import net.ooder.sdk.api.cmd.*;
import net.ooder.sdk.api.protocol.*;
import net.ooder.sdk.core.transport.CoreTransport;
import net.ooder.sdk.core.transport.TransportMessage;
import net.ooder.sdk.core.transport.TransportResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CmdClientProxyImpl implements CmdClientProxy {
    
    private static final Logger log = LoggerFactory.getLogger(CmdClientProxyImpl.class);
    
    private final String clientId;
    private final String sceneId;
    private final String groupId;
    
    private CmdClientConfig config;
    private CoreTransport transport;
    
    private final Map<String, CommandPacket> pendingCommands = new ConcurrentHashMap<>();
    private final Map<String, CommandResult> commandResults = new ConcurrentHashMap<>();
    private final List<CommandPacket> commandHistory = new CopyOnWriteArrayList<>();
    private final List<CommandListener> listeners = new CopyOnWriteArrayList<>();
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    public CmdClientProxyImpl(String clientId, String sceneId) {
        this.clientId = clientId;
        this.sceneId = sceneId;
        this.groupId = sceneId + ":default";
    }
    
    public void setTransport(CoreTransport transport) {
        this.transport = transport;
    }
    
    @Override
    public String getClientId() { return clientId; }
    
    @Override
    public String getSceneId() { return sceneId; }
    
    @Override
    public String getGroupId() { return groupId; }
    
    @Override
    public void initialize(CmdClientConfig config) {
        this.config = config;
        log.info("CmdClientProxy initialized: {} for scene: {}", clientId, sceneId);
    }
    
    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            if (transport != null) {
                transport.start();
            }
            
            scheduler.scheduleAtFixedRate(
                this::checkTimeoutCommands,
                5000,
                5000,
                TimeUnit.MILLISECONDS
            );
            
            log.info("CmdClientProxy started: {}", clientId);
        }
    }
    
    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            scheduler.shutdown();
            
            if (transport != null) {
                transport.stop();
            }
            
            log.info("CmdClientProxy stopped: {}", clientId);
        }
    }
    
    @Override
    public boolean isRunning() { return running.get(); }
    
    @Override
    public CompletableFuture<CommandPacket> createCommand(String protocolType, String commandType) {
        return CompletableFuture.supplyAsync(() -> {
            CommandPacket command = CommandPacket.of(protocolType, commandType);
            command.setSource(clientId);
            command.setSceneId(sceneId);
            if (config != null && config.getDomainId() != null) {
                command.setDomainId(config.getDomainId());
            }
            return command;
        });
    }
    
    @Override
    public CompletableFuture<CommandPacket> createCommand(String protocolType, String commandType, Map<String, Object> payload) {
        return createCommand(protocolType, commandType).thenApply(command -> {
            command.setPayload(payload);
            return command;
        });
    }
    
    @Override
    public CompletableFuture<CommandResult> sendCommand(CommandPacket command) {
        return CompletableFuture.supplyAsync(() -> {
            if (transport == null) {
                throw new IllegalStateException("Transport not configured");
            }
            
            String commandId = command.getPacketId();
            pendingCommands.put(commandId, command);
            
            TransportMessage transportMsg = toTransportMessage(command);
            TransportResult transportResult = transport.transmit(transportMsg).join();
            
            CommandResult result;
            if (transportResult.isSuccess()) {
                result = CommandResult.success("Command sent successfully");
                pendingCommands.remove(commandId);
                commandHistory.add(command);
                commandResults.put(commandId, result);
                
                notifyCommandCompleted(command, result);
                log.info("Command sent successfully: {}", commandId);
            } else {
                result = CommandResult.failure(transportResult.getErrorCode(), transportResult.getErrorMessage());
                notifyCommandFailed(command, new RuntimeException(transportResult.getErrorMessage()));
                log.error("Command failed: {} - {}", commandId, transportResult.getErrorMessage());
            }
            
            notifyCommandSent(command);
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<CommandResult> sendCommandAndWait(CommandPacket command, long timeoutMs) {
        CompletableFuture<CommandResult> future = sendCommand(command);
        
        ScheduledFuture<?> timeoutFuture = scheduler.schedule(() -> {
            if (!future.isDone()) {
                future.complete(CommandResult.failure("TIMEOUT", "Timeout after " + timeoutMs + "ms"));
            }
        }, timeoutMs, TimeUnit.MILLISECONDS);
        
        return future.whenComplete((result, ex) -> timeoutFuture.cancel(false));
    }
    
    @Override
    public CompletableFuture<CommandResult> getCommandResult(String commandId) {
        return CompletableFuture.supplyAsync(() -> commandResults.get(commandId));
    }
    
    @Override
    public CompletableFuture<Void> cancelCommand(String commandId) {
        return CompletableFuture.runAsync(() -> {
            CommandPacket command = pendingCommands.remove(commandId);
            if (command != null) {
                notifyCommandCancelled(commandId);
                log.info("Command cancelled: {}", commandId);
            }
        });
    }
    
    @Override
    public CompletableFuture<List<CommandPacket>> getPendingCommands() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<>(pendingCommands.values()));
    }
    
    @Override
    public CompletableFuture<List<CommandPacket>> getCommandHistory() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<>(commandHistory));
    }
    
    @Override
    public CompletableFuture<CommandPacket> getCommandById(String commandId) {
        return CompletableFuture.supplyAsync(() -> {
            CommandPacket pending = pendingCommands.get(commandId);
            if (pending != null) {
                return pending;
            }
            
            for (CommandPacket cmd : commandHistory) {
                if (commandId.equals(cmd.getPacketId())) {
                    return cmd;
                }
            }
            
            return null;
        });
    }
    
    @Override
    public void addCommandListener(CommandListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeCommandListener(CommandListener listener) {
        listeners.remove(listener);
    }
    
    public void receiveCommandResult(String commandId, CommandResult result) {
        CommandPacket command = pendingCommands.remove(commandId);
        if (command != null) {
            commandResults.put(commandId, result);
            commandHistory.add(command);
            notifyCommandCompleted(command, result);
        }
    }
    
    private TransportMessage toTransportMessage(CommandPacket command) {
        TransportMessage msg = new TransportMessage();
        msg.setMessageId(command.getPacketId());
        msg.setSource(command.getSource());
        msg.setTarget(command.getTarget());
        msg.setTimestamp(command.getTimestamp());
        
        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"protocolType\":\"").append(command.getHeader().getProtocolType()).append("\",");
        payloadBuilder.append("\"commandType\":\"").append(command.getHeader().getCommandType()).append("\",");
        payloadBuilder.append("\"priority\":").append(command.getHeader().getPriority()).append(",");
        payloadBuilder.append("\"timeout\":").append(command.getHeader().getTimeout());
        
        if (command.getPayload() != null) {
            payloadBuilder.append(",\"payload\":").append(mapToJson(command.getPayload()));
        }
        payloadBuilder.append("}");
        
        msg.setPayload(payloadBuilder.toString().getBytes(StandardCharsets.UTF_8));
        
        return msg;
    }
    
    private String mapToJson(Map<String, Object> map) {
        if (map == null) return "{}";
        
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nested = (Map<String, Object>) value;
                sb.append(mapToJson(nested));
            } else {
                sb.append(value);
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    private void checkTimeoutCommands() {
        long now = System.currentTimeMillis();
        List<String> timeoutIds = new ArrayList<>();
        
        for (Map.Entry<String, CommandPacket> entry : pendingCommands.entrySet()) {
            CommandPacket cmd = entry.getValue();
            long timeout = cmd.getHeader().getTimeout();
            if (now - cmd.getCreatedTime() > timeout) {
                timeoutIds.add(entry.getKey());
            }
        }
        
        for (String commandId : timeoutIds) {
            CommandPacket command = pendingCommands.remove(commandId);
            if (command != null) {
                CommandResult result = CommandResult.failure("TIMEOUT", "Command timeout");
                
                commandResults.put(commandId, result);
                notifyCommandFailed(command, new TimeoutException("Command timeout"));
                
                log.warn("Command timeout: {}", commandId);
            }
        }
    }
    
    private void notifyCommandSent(CommandPacket command) {
        for (CommandListener listener : listeners) {
            try {
                listener.onCommandSent(command);
            } catch (Exception e) {
                log.warn("CommandListener error", e);
            }
        }
    }
    
    private void notifyCommandCompleted(CommandPacket command, CommandResult result) {
        for (CommandListener listener : listeners) {
            try {
                listener.onCommandCompleted(command, result);
            } catch (Exception e) {
                log.warn("CommandListener error", e);
            }
        }
    }
    
    private void notifyCommandFailed(CommandPacket command, Throwable error) {
        for (CommandListener listener : listeners) {
            try {
                listener.onCommandFailed(command, error);
            } catch (Exception e) {
                log.warn("CommandListener error", e);
            }
        }
    }
    
    private void notifyCommandCancelled(String commandId) {
        for (CommandListener listener : listeners) {
            try {
                listener.onCommandCancelled(commandId);
            } catch (Exception e) {
                log.warn("CommandListener error", e);
            }
        }
    }
    
    public int getPendingCount() { return pendingCommands.size(); }
    public int getHistoryCount() { return commandHistory.size(); }
}

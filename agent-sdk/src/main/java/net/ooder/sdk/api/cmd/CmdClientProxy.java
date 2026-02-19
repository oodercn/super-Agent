package net.ooder.sdk.api.cmd;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.api.protocol.CommandPacket;
import net.ooder.sdk.api.protocol.CommandResult;

public interface CmdClientProxy {
    
    String getClientId();
    
    String getSceneId();
    
    String getGroupId();
    
    void initialize(CmdClientConfig config);
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    CompletableFuture<CommandPacket> createCommand(String protocolType, String commandType);
    
    CompletableFuture<CommandPacket> createCommand(String protocolType, String commandType, Map<String, Object> payload);
    
    CompletableFuture<CommandResult> sendCommand(CommandPacket command);
    
    CompletableFuture<CommandResult> sendCommandAndWait(CommandPacket command, long timeoutMs);
    
    CompletableFuture<CommandResult> getCommandResult(String commandId);
    
    CompletableFuture<Void> cancelCommand(String commandId);
    
    CompletableFuture<List<CommandPacket>> getPendingCommands();
    
    CompletableFuture<List<CommandPacket>> getCommandHistory();
    
    CompletableFuture<CommandPacket> getCommandById(String commandId);
    
    void addCommandListener(CommandListener listener);
    
    void removeCommandListener(CommandListener listener);
    
    interface CommandListener {
        void onCommandSent(CommandPacket command);
        void onCommandCompleted(CommandPacket command, CommandResult result);
        void onCommandFailed(CommandPacket command, Throwable error);
        void onCommandCancelled(String commandId);
    }
}

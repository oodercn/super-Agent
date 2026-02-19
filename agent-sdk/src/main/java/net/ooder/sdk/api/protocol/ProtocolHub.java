package net.ooder.sdk.api.protocol;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ProtocolHub {

    void registerProtocolHandler(String protocolType, ProtocolHandler handler);

    void unregisterProtocolHandler(String protocolType);

    boolean hasHandler(String protocolType);

    List<String> getSupportedProtocols();

    CommandResult handleCommand(CommandPacket packet);

    CompletableFuture<CommandResult> handleCommandAsync(CommandPacket packet);

    CommandResult handleCommand(CommandPacket packet, String target);

    ProtocolStats getProtocolStats(String protocolType);

    Map<String, ProtocolStats> getAllStats();

    void resetStats(String protocolType);

    void initialize();

    void shutdown();

    boolean isRunning();

    default CompletableFuture<BatchCommandResult> handleBatch(List<CommandPacket> packets) {
        return CompletableFuture.supplyAsync(() -> {
            BatchCommandResult result = new BatchCommandResult();
            result.setTotalCount(packets.size());
            for (CommandPacket packet : packets) {
                try {
                    CommandResult r = handleCommand(packet);
                    if (r.isSuccess()) {
                        result.incrementSuccess();
                    } else {
                        result.incrementFailed();
                        result.addFailedPacketId(packet.getPacketId());
                    }
                } catch (Exception e) {
                    result.incrementFailed();
                    result.addFailedPacketId(packet.getPacketId());
                }
            }
            return result;
        });
    }

    default CompletableFuture<CommandResult> handleChain(CommandPacket... packets) {
        CompletableFuture<CommandResult> future = CompletableFuture.completedFuture(null);
        for (CommandPacket packet : packets) {
            future = future.thenCompose(prevResult -> {
                if (prevResult != null && !prevResult.isSuccess()) {
                    return CompletableFuture.completedFuture(prevResult);
                }
                return handleCommandAsync(packet);
            });
        }
        return future;
    }

    default CompletableFuture<CommandResult> rollback(String commandId) {
        CommandResult result = new CommandResult();
        result.setSuccess(false);
        result.setMessage("Rollback not supported for command: " + commandId);
        return CompletableFuture.completedFuture(result);
    }

    default CommandTrace traceCommand(String commandId) {
        return null;
    }

    default CompletableFuture<CommandResult> retryCommand(String commandId) {
        CommandResult result = new CommandResult();
        result.setSuccess(false);
        result.setMessage("Retry not supported for command: " + commandId);
        return CompletableFuture.completedFuture(result);
    }

    class ProtocolStats {
        private String protocolType;
        private long totalCommands;
        private long successfulCommands;
        private long failedCommands;
        private long averageProcessingTime;
        private long lastCommandTime;

        public String getProtocolType() { return protocolType; }
        public void setProtocolType(String protocolType) { this.protocolType = protocolType; }

        public long getTotalCommands() { return totalCommands; }
        public void setTotalCommands(long totalCommands) { this.totalCommands = totalCommands; }

        public long getSuccessfulCommands() { return successfulCommands; }
        public void setSuccessfulCommands(long successfulCommands) { this.successfulCommands = successfulCommands; }

        public long getFailedCommands() { return failedCommands; }
        public void setFailedCommands(long failedCommands) { this.failedCommands = failedCommands; }

        public long getAverageProcessingTime() { return averageProcessingTime; }
        public void setAverageProcessingTime(long averageProcessingTime) { this.averageProcessingTime = averageProcessingTime; }

        public long getLastCommandTime() { return lastCommandTime; }
        public void setLastCommandTime(long lastCommandTime) { this.lastCommandTime = lastCommandTime; }

        public void incrementTotal() { totalCommands++; }
        public void incrementSuccess() { successfulCommands++; }
        public void incrementFailed() { failedCommands++; }
    }
}

package net.ooder.nexus.protocol;

import java.util.concurrent.CompletableFuture;

/**
 * 增强协议中心接口
 *
 * <p>SDK 0.7.2 增强协议处理能力。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface EnhancedProtocolHub {

    /**
     * 处理增强命令
     *
     * @param packet 命令包
     * @return 处理结果
     */
    CompletableFuture<CommandResult> handleEnhancedCommand(EnhancedCommandPacket packet);

    /**
     * 追踪命令
     *
     * @param packetId 命令包 ID
     * @return 命令追踪信息
     */
    CommandTrace traceCommand(String packetId);

    /**
     * 重试命令
     *
     * @param packetId 命令包 ID
     * @return 处理结果
     */
    CompletableFuture<CommandResult> retryCommand(String packetId);

    /**
     * 注册协议处理器
     *
     * @param protocolType 协议类型
     * @param handler 处理器
     */
    void registerProtocolHandler(String protocolType, EnhancedProtocolHandler handler);

    /**
     * 注销协议处理器
     *
     * @param protocolType 协议类型
     */
    void unregisterProtocolHandler(String protocolType);

    /**
     * 增强命令包
     */
    class EnhancedCommandPacket {
        private String packetId;
        private String protocolType;
        private String commandType;
        private java.util.Map<String, Object> payload;
        private java.util.Map<String, String> headers;
        private long timestamp;
        private int retryCount;
        private int maxRetries;
        private long timeout;

        public String getPacketId() { return packetId; }
        public void setPacketId(String packetId) { this.packetId = packetId; }
        public String getProtocolType() { return protocolType; }
        public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
        public String getCommandType() { return commandType; }
        public void setCommandType(String commandType) { this.commandType = commandType; }
        public java.util.Map<String, Object> getPayload() { return payload; }
        public void setPayload(java.util.Map<String, Object> payload) { this.payload = payload; }
        public java.util.Map<String, String> getHeaders() { return headers; }
        public void setHeaders(java.util.Map<String, String> headers) { this.headers = headers; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        public long getTimeout() { return timeout; }
        public void setTimeout(long timeout) { this.timeout = timeout; }
    }

    /**
     * 命令结果
     */
    class CommandResult {
        private String packetId;
        private boolean success;
        private java.util.Map<String, Object> data;
        private String errorCode;
        private String errorMessage;
        private long processingTime;
        private int attemptCount;

        public String getPacketId() { return packetId; }
        public void setPacketId(String packetId) { this.packetId = packetId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public java.util.Map<String, Object> getData() { return data; }
        public void setData(java.util.Map<String, Object> data) { this.data = data; }
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public long getProcessingTime() { return processingTime; }
        public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }
        public int getAttemptCount() { return attemptCount; }
        public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }
    }

    /**
     * 命令追踪信息
     */
    class CommandTrace {
        private String packetId;
        private String status;
        private long submittedAt;
        private long startedAt;
        private long completedAt;
        private java.util.List<TraceStep> steps;

        public String getPacketId() { return packetId; }
        public void setPacketId(String packetId) { this.packetId = packetId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getSubmittedAt() { return submittedAt; }
        public void setSubmittedAt(long submittedAt) { this.submittedAt = submittedAt; }
        public long getStartedAt() { return startedAt; }
        public void setStartedAt(long startedAt) { this.startedAt = startedAt; }
        public long getCompletedAt() { return completedAt; }
        public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }
        public java.util.List<TraceStep> getSteps() { return steps; }
        public void setSteps(java.util.List<TraceStep> steps) { this.steps = steps; }
    }

    /**
     * 追踪步骤
     */
    class TraceStep {
        private String stepName;
        private long startTime;
        private long endTime;
        private boolean success;
        private String message;

        public String getStepName() { return stepName; }
        public void setStepName(String stepName) { this.stepName = stepName; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * 增强协议处理器
     */
    interface EnhancedProtocolHandler {
        CompletableFuture<CommandResult> handle(EnhancedCommandPacket packet);
        String getProtocolType();
        boolean supports(String commandType);
    }
}

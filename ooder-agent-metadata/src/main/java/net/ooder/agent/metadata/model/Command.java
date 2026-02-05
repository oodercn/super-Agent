package net.ooder.agent.metadata.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Command模型
 * 标准化的操作指令
 */
public class Command implements Serializable {
    private static final long serialVersionUID = 1L;

    @JSONField(name = "version")
    private String version = "0.6.0";

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "command_id") // 兼容旧格式
    private String commandId;

    @JSONField(name = "timestamp")
    private long timestamp;

    @JSONField(name = "type")
    private String type; // request, response, notification

    @JSONField(name = "command")
    private String command;

    @JSONField(name = "params")
    private Map<String, Object> params;

    @JSONField(name = "metadata")
    private CommandMetadata metadata;

    @JSONField(name = "response_to")
    private String responseTo;

    @JSONField(name = "status")
    private String status; // success, error, pending

    @JSONField(name = "result")
    private Object result;

    @JSONField(name = "error")
    private CommandError error;

    /**
     * MCP透传数据
     */
    @JSONField(name = "passthrough")
    private McpPassthrough passthrough;

    public Command() {
        this.params = new HashMap<>();
        this.metadata = new CommandMetadata();
        this.timestamp = System.currentTimeMillis();
    }

    public Command(String id, String command) {
        this();
        this.id = id;
        this.commandId = id;
        this.command = command;
        this.type = "request";
    }

    // Getters and Setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.commandId = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }

    public CommandMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CommandMetadata metadata) {
        this.metadata = metadata;
    }

    public String getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(String responseTo) {
        this.responseTo = responseTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public CommandError getError() {
        return error;
    }

    public void setError(CommandError error) {
        this.error = error;
    }

    public McpPassthrough getPassthrough() {
        return passthrough;
    }

    public void setPassthrough(McpPassthrough passthrough) {
        this.passthrough = passthrough;
    }

    // CommandMetadata inner class
    public static class CommandMetadata implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "sender_id")
        private String senderId;

        @JSONField(name = "trace_id")
        private String traceId;

        @JSONField(name = "priority")
        private String priority; // high, medium, low

        @JSONField(name = "timeout")
        private long timeout; // milliseconds

        @JSONField(name = "ttl")
        private int ttl; // time to live

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }
    }

    // CommandError inner class
    public static class CommandError implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "code")
        private int code;

        @JSONField(name = "message")
        private String message;

        @JSONField(name = "details")
        private String details;

        @JSONField(name = "type")
        private String type; // validation, business, system

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

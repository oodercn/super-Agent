package net.ooder.examples.endagent.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class AiBridgeMessage {
    @JSONField(name = "version")
    private String version = "0.6.0";

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "message_id") // 兼容旧格式
    private String messageId;

    @JSONField(name = "timestamp")
    private long timestamp;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "command")
    private String command;

    @JSONField(name = "params")
    private Map<String, Object> params;

    @JSONField(name = "metadata")
    private Metadata metadata;

    @JSONField(name = "source") // 兼容旧格式
    private String source;

    @JSONField(name = "target") // 兼容旧格式
    private String target;

    @JSONField(name = "response_to")
    private String responseTo;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "result")
    private Object result;

    @JSONField(name = "error")
    private ErrorInfo error;

    @JSONField(name = "extension")
    private Extension extension;

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
        this.messageId = id; // 兼容旧格式
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
        this.id = messageId; // 兼容旧格式
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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
        // 兼容旧格式
        if (metadata != null) {
            this.source = metadata.getSenderId();
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        // 兼容旧格式
        if (metadata == null) {
            metadata = new Metadata();
        }
        metadata.setSenderId(source);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    // Metadata inner class
    public static class Metadata {
        @JSONField(name = "sender_id")
        private String senderId;

        @JSONField(name = "trace_id")
        private String traceId;

        @JSONField(name = "priority")
        private String priority;

        @JSONField(name = "timeout")
        private long timeout;

        // Getters and Setters
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
    }

    // Error Info inner class
    public static class ErrorInfo {
        @JSONField(name = "code")
        private int code;

        @JSONField(name = "message")
        private String message;

        @JSONField(name = "details")
        private String details;

        // Getters and Setters
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
    }

    // Extension inner class
    public static class Extension {
        @JSONField(name = "vendor")
        private String vendor;

        @JSONField(name = "schema_version")
        private String schemaVersion;

        // Getters and Setters
        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getSchemaVersion() {
            return schemaVersion;
        }

        public void setSchemaVersion(String schemaVersion) {
            this.schemaVersion = schemaVersion;
        }
    }
}

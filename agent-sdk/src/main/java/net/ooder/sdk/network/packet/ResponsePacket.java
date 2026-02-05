package net.ooder.sdk.network.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.JSON;

@JSONType(orders = {"protocol_version", "command_id", "correlation_id", "timestamp", "status", "data", "error", "metadata"})
public class ResponsePacket<T> extends UDPPacket {
    @JSONField(name = "protocol_version")
    private String protocolVersion = "0.6.3";
    
    @JSONField(name = "command_id")
    private String commandId;
    
    @JSONField(name = "correlation_id")
    private String correlationId;
    
    @JSONField(name = "timestamp")
    private String timestamp;
    
    @JSONField(name = "status")
    private String status;
    
    @JSONField(name = "data")
    private T data;
    
    @JSONField(name = "error")
    private ErrorInfo error;
    
    @JSONField(name = "metadata")
    private CommandMetadata metadata;
    
    public ResponsePacket() {
        super();
        this.commandId = java.util.UUID.randomUUID().toString();
        this.timestamp = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).format(java.time.format.DateTimeFormatter.ISO_INSTANT);
    }
    
    public static ResponsePacket<?> fromJson(String json) {
        return JSON.parseObject(json, ResponsePacket.class);
    }
    
    @Override
    public String getType() {
        return "response";
    }
    
    // Getter and Setter methods
    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public CommandMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CommandMetadata metadata) {
        this.metadata = metadata;
    }

    // ErrorInfo class
    public static class ErrorInfo {
        @JSONField(name = "code")
        private String code;
        
        @JSONField(name = "message")
        private String message;
        
        public ErrorInfo() {
        }

        public ErrorInfo(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // Builder class
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private ResponsePacket<T> packet = new ResponsePacket<>();

        public Builder<T> commandId(String commandId) {
            packet.setCommandId(commandId);
            return this;
        }

        public Builder<T> correlationId(String correlationId) {
            packet.setCorrelationId(correlationId);
            return this;
        }

        public Builder<T> timestamp(String timestamp) {
            packet.setTimestamp(timestamp);
            return this;
        }

        public Builder<T> status(String status) {
            packet.setStatus(status);
            return this;
        }

        public Builder<T> data(T data) {
            packet.setData(data);
            return this;
        }

        public Builder<T> error(String code, String message) {
            packet.setError(new ErrorInfo(code, message));
            return this;
        }

        public Builder<T> metadata(CommandMetadata metadata) {
            packet.setMetadata(metadata);
            return this;
        }

        public ResponsePacket<T> build() {
            return packet;
        }
    }
}

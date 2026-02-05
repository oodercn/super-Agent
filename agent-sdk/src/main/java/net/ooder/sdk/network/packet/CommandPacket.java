package net.ooder.sdk.network.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.JSON;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

@JSONType(orders = {"protocol_version", "command_id", "timestamp", "source", "destination", "operation", "payload", "metadata"})
public class CommandPacket<T> extends UDPPacket {
    @JSONField(name = "protocol_version")
    private String protocolVersion = "0.6.3";
    
    @JSONField(name = "command_id")
    private String commandId;

    
    @JSONField(name = "source")
    private SourceInfo source;
    
    @JSONField(name = "destination")
    private DestinationInfo destination;
    
    @JSONField(name = "operation")
    private String operation;
    
    @JSONField(name = "payload")
    private T payload;
    
    @JSONField(name = "metadata")
    private CommandMetadata metadata;

    public CommandPacket() {
        super();
        this.commandId = java.util.UUID.randomUUID().toString();
        this.timestamp = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).format(java.time.format.DateTimeFormatter.ISO_INSTANT);
    }
    
    public static CommandPacket<?> fromJson(String json) {
        return JSON.parseObject(json, CommandPacket.class);
    }
    
    @Override
    public String getType() {
        return "command";
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


    public SourceInfo getSource() {
        return source;
    }

    public void setSource(SourceInfo source) {
        this.source = source;
    }

    public DestinationInfo getDestination() {
        return destination;
    }

    public void setDestination(DestinationInfo destination) {
        this.destination = destination;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public CommandMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CommandMetadata metadata) {
        this.metadata = metadata;
    }

    // Additional methods for compatibility
    public Map<String, Object> getParams() {
        if (payload instanceof Map) {
            return (Map<String, Object>) payload;
        }
        return null;
    }

    public void setParams(Map<String, Object> params) {
        this.payload = (T) params;
    }

    public String getCommand() {
        return operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CommandPacket<?> that = (CommandPacket<?>) o;

        if (protocolVersion != null ? !protocolVersion.equals(that.protocolVersion) : that.protocolVersion != null) return false;
        if (commandId != null ? !commandId.equals(that.commandId) : that.commandId != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (destination != null ? !destination.equals(that.destination) : that.destination != null) return false;
        if (operation != null ? !operation.equals(that.operation) : that.operation != null) return false;
        if (payload != null ? !payload.equals(that.payload) : that.payload != null) return false;
        return metadata != null ? metadata.equals(that.metadata) : that.metadata == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (protocolVersion != null ? protocolVersion.hashCode() : 0);
        result = 31 * result + (commandId != null ? commandId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    // SourceInfo class
    public static class SourceInfo {
        @JSONField(name = "component")
        private String component;
        
        @JSONField(name = "id")
        private String id;

        public SourceInfo() {
        }

        public SourceInfo(String component, String id) {
            this.component = component;
            this.id = id;
        }

        public String getComponent() {
            return component;
        }

        public void setComponent(String component) {
            this.component = component;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    // DestinationInfo class
    public static class DestinationInfo {
        @JSONField(name = "component")
        private String component;
        
        @JSONField(name = "id")
        private String id;

        public DestinationInfo() {
        }

        public DestinationInfo(String component, String id) {
            this.component = component;
            this.id = id;
        }

        public String getComponent() {
            return component;
        }

        public void setComponent(String component) {
            this.component = component;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    // Builder class
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private CommandPacket<T> packet = new CommandPacket<>();

        public Builder<T> commandId(String commandId) {
            packet.setCommandId(commandId);
            return this;
        }

        public Builder<T> timestamp(String timestamp) {
            packet.setTimestamp(timestamp);
            return this;
        }

        public Builder<T> source(String component, String id) {
            packet.setSource(new SourceInfo(component, id));
            return this;
        }

        public Builder<T> destination(String component, String id) {
            packet.setDestination(new DestinationInfo(component, id));
            return this;
        }

        public Builder<T> operation(String operation) {
            packet.setOperation(operation);
            return this;
        }

        public Builder<T> command(CommandType commandType) {
            packet.setOperation(commandType.toString());
            return this;
        }

        public Builder<T> payload(T payload) {
            packet.setPayload(payload);
            return this;
        }

        public Builder<T> params(Map<String, Object> params) {
            packet.setPayload((T) params);
            return this;
        }

        public Builder<T> metadata(CommandMetadata metadata) {
            packet.setMetadata(metadata);
            return this;
        }

        public CommandPacket<T> build() {
            return packet;
        }
    }
}

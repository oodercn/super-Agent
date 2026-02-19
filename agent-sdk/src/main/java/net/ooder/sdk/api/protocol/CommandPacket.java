package net.ooder.sdk.api.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandPacket {

    private String packetId;
    private CommandHeader header;
    private Map<String, Object> payload;
    private long timestamp;
    private String source;
    private String target;
    private CommandDirection direction;
    private String parentCommandId;
    private List<String> childCommandIds = new ArrayList<String>();
    private boolean rollbackable;
    private String sceneId;
    private String domainId;
    private int retryCount;
    private long createdTime;
    private long executedTime;

    public CommandPacket() {
        this.packetId = "pkt_" + System.currentTimeMillis();
        this.timestamp = System.currentTimeMillis();
        this.createdTime = System.currentTimeMillis();
        this.header = new CommandHeader();
        this.rollbackable = false;
        this.retryCount = 0;
    }

    public static CommandPacket of(String protocolType, String commandType) {
        CommandPacket packet = new CommandPacket();
        packet.getHeader().setProtocolType(protocolType);
        packet.getHeader().setCommandType(commandType);
        return packet;
    }

    public static CommandPacket of(String protocolType, String commandType, Map<String, Object> payload) {
        CommandPacket packet = of(protocolType, commandType);
        packet.setPayload(payload);
        return packet;
    }

    public static CommandBuilder builder() {
        return new CommandBuilder();
    }

    public String getPacketId() { return packetId; }
    public void setPacketId(String packetId) { this.packetId = packetId; }

    public CommandHeader getHeader() { return header; }
    public void setHeader(CommandHeader header) { this.header = header; }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public CommandDirection getDirection() { return direction; }
    public void setDirection(CommandDirection direction) { this.direction = direction; }

    public String getParentCommandId() { return parentCommandId; }
    public void setParentCommandId(String parentCommandId) { this.parentCommandId = parentCommandId; }

    public List<String> getChildCommandIds() { return childCommandIds; }
    public void setChildCommandIds(List<String> childCommandIds) { this.childCommandIds = childCommandIds; }
    
    public void addChildCommandId(String commandId) { this.childCommandIds.add(commandId); }

    public boolean isRollbackable() { return rollbackable; }
    public void setRollbackable(boolean rollbackable) { this.rollbackable = rollbackable; }

    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }

    public long getExecutedTime() { return executedTime; }
    public void setExecutedTime(long executedTime) { this.executedTime = executedTime; }

    public boolean isNorthbound() { return direction == CommandDirection.NORTHBOUND; }
    
    public boolean isSouthbound() { return direction == CommandDirection.SOUTHBOUND; }
    
    public boolean hasParent() { return parentCommandId != null && !parentCommandId.isEmpty(); }
    
    public boolean hasChildren() { return !childCommandIds.isEmpty(); }

    public static class CommandHeader {
        private String protocolType;
        private String commandType;
        private String version;
        private int priority;
        private boolean requiresAck;
        private long timeout;

        public CommandHeader() {
            this.version = "1.0";
            this.priority = 5;
            this.requiresAck = true;
            this.timeout = 30000;
        }

        public String getProtocolType() { return protocolType; }
        public void setProtocolType(String protocolType) { this.protocolType = protocolType; }

        public String getCommandType() { return commandType; }
        public void setCommandType(String commandType) { this.commandType = commandType; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }

        public boolean isRequiresAck() { return requiresAck; }
        public void setRequiresAck(boolean requiresAck) { this.requiresAck = requiresAck; }

        public long getTimeout() { return timeout; }
        public void setTimeout(long timeout) { this.timeout = timeout; }
    }

    @Override
    public String toString() {
        return "CommandPacket{" +
                "packetId='" + packetId + '\'' +
                ", protocolType='" + header.getProtocolType() + '\'' +
                ", commandType='" + header.getCommandType() + '\'' +
                ", direction=" + direction +
                ", timestamp=" + timestamp +
                '}';
    }
}

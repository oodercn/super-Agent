package net.ooder.sdk.api.protocol;

import java.util.HashMap;
import java.util.Map;

public class CommandBuilder {
    
    private String protocolType;
    private String commandType;
    private Map<String, Object> payload = new HashMap<String, Object>();
    private String source;
    private String target;
    private int priority = 5;
    private long timeout = 30000;
    private boolean requiresAck = true;
    private CommandDirection direction;
    private String parentCommandId;
    private boolean rollbackable;
    private String sceneId;
    private String domainId;
    
    public static CommandBuilder create() {
        return new CommandBuilder();
    }
    
    public CommandBuilder protocolType(String protocolType) {
        this.protocolType = protocolType;
        return this;
    }
    
    public CommandBuilder commandType(String commandType) {
        this.commandType = commandType;
        return this;
    }
    
    public CommandBuilder payload(String key, Object value) {
        this.payload.put(key, value);
        return this;
    }
    
    public CommandBuilder payload(Map<String, Object> payload) {
        this.payload.putAll(payload);
        return this;
    }
    
    public CommandBuilder source(String source) {
        this.source = source;
        return this;
    }
    
    public CommandBuilder target(String target) {
        this.target = target;
        return this;
    }
    
    public CommandBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }
    
    public CommandBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
    
    public CommandBuilder requiresAck(boolean requiresAck) {
        this.requiresAck = requiresAck;
        return this;
    }
    
    public CommandBuilder direction(CommandDirection direction) {
        this.direction = direction;
        return this;
    }
    
    public CommandBuilder northbound() {
        this.direction = CommandDirection.NORTHBOUND;
        return this;
    }
    
    public CommandBuilder southbound() {
        this.direction = CommandDirection.SOUTHBOUND;
        return this;
    }
    
    public CommandBuilder parentCommandId(String parentCommandId) {
        this.parentCommandId = parentCommandId;
        return this;
    }
    
    public CommandBuilder rollbackable(boolean rollbackable) {
        this.rollbackable = rollbackable;
        return this;
    }
    
    public CommandBuilder sceneId(String sceneId) {
        this.sceneId = sceneId;
        return this;
    }
    
    public CommandBuilder domainId(String domainId) {
        this.domainId = domainId;
        return this;
    }
    
    public CommandPacket build() {
        CommandPacket packet = CommandPacket.of(protocolType, commandType, payload);
        packet.setSource(source);
        packet.setTarget(target);
        packet.getHeader().setPriority(priority);
        packet.getHeader().setTimeout(timeout);
        packet.getHeader().setRequiresAck(requiresAck);
        packet.setDirection(direction);
        packet.setParentCommandId(parentCommandId);
        packet.setRollbackable(rollbackable);
        packet.setSceneId(sceneId);
        packet.setDomainId(domainId);
        return packet;
    }
}

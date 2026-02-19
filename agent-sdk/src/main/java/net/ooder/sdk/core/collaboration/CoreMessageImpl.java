package net.ooder.sdk.core.collaboration;

import java.util.Map;

class CoreMessageImpl implements CoreMessage {
    
    private final String messageId;
    private final String source;
    private final String target;
    private final MessageType type;
    private final Map<String, Object> payload;
    private final long timestamp;
    private final int priority;
    private final long ttl;
    private final String correlationId;
    private final Map<String, String> headers;
    private final String title;
    private final MsgStatus status;
    private final Integer retryCount;
    private final String groupId;
    private final Map<String, Object> metadata;
    
    CoreMessageImpl(MessageBuilder builder) {
        this.messageId = builder.getMessageId();
        this.source = builder.getSource();
        this.target = builder.getTarget();
        this.type = builder.getType();
        this.payload = builder.getPayload();
        this.timestamp = builder.getTimestamp();
        this.priority = builder.getPriority();
        this.ttl = builder.getTtl();
        this.correlationId = builder.getCorrelationId();
        this.headers = builder.getHeaders();
        this.title = builder.getTitle();
        this.status = builder.getStatus();
        this.retryCount = builder.getRetryCount();
        this.groupId = builder.getGroupId();
        this.metadata = builder.getMetadata();
    }
    
    @Override
    public String getMessageId() { return messageId; }
    
    @Override
    public String getSource() { return source; }
    
    @Override
    public String getTarget() { return target; }
    
    @Override
    public MessageType getType() { return type; }
    
    @Override
    public Map<String, Object> getPayload() { return payload; }
    
    @Override
    public long getTimestamp() { return timestamp; }
    
    @Override
    public int getPriority() { return priority; }
    
    @Override
    public long getTtl() { return ttl; }
    
    @Override
    public String getCorrelationId() { return correlationId; }
    
    @Override
    public Map<String, String> getHeaders() { return headers; }
    
    @Override
    public String getTitle() { return title; }
    
    @Override
    public MsgStatus getStatus() { return status; }
    
    @Override
    public Integer getRetryCount() { return retryCount; }
    
    @Override
    public String getGroupId() { return groupId; }
    
    @Override
    public Map<String, Object> getMetadata() { return metadata; }
}

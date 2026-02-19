package net.ooder.sdk.core.collaboration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageBuilder {
    
    private String messageId;
    private String source;
    private String target;
    private MessageType type;
    private Map<String, Object> payload = new HashMap<String, Object>();
    private long timestamp = System.currentTimeMillis();
    private int priority = 5;
    private long ttl = 30000;
    private String correlationId;
    private Map<String, String> headers = new HashMap<String, String>();
    private String title;
    private MsgStatus status = MsgStatus.NORMAL;
    private Integer retryCount = 0;
    private String groupId;
    private Map<String, Object> metadata = new HashMap<String, Object>();
    
    public static MessageBuilder create() {
        return new MessageBuilder();
    }
    
    public MessageBuilder messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }
    
    public MessageBuilder source(String source) {
        this.source = source;
        return this;
    }
    
    public MessageBuilder target(String target) {
        this.target = target;
        return this;
    }
    
    public MessageBuilder type(MessageType type) {
        this.type = type;
        return this;
    }
    
    public MessageBuilder payload(String key, Object value) {
        this.payload.put(key, value);
        return this;
    }
    
    public MessageBuilder payload(Map<String, Object> payload) {
        this.payload.putAll(payload);
        return this;
    }
    
    public MessageBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }
    
    public MessageBuilder ttl(long ttl) {
        this.ttl = ttl;
        return this;
    }
    
    public MessageBuilder correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }
    
    public MessageBuilder header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
    
    public MessageBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    public MessageBuilder status(MsgStatus status) {
        this.status = status;
        return this;
    }
    
    public MessageBuilder retryCount(Integer retryCount) {
        this.retryCount = retryCount;
        return this;
    }
    
    public MessageBuilder groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }
    
    public MessageBuilder metadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }
    
    public MessageBuilder metadata(Map<String, Object> metadata) {
        this.metadata.putAll(metadata);
        return this;
    }
    
    public CoreMessage build() {
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        return new CoreMessageImpl(this);
    }
    
    String getMessageId() { return messageId; }
    
    String getSource() { return source; }
    
    String getTarget() { return target; }
    
    MessageType getType() { return type; }
    
    Map<String, Object> getPayload() { return payload; }
    
    long getTimestamp() { return timestamp; }
    
    int getPriority() { return priority; }
    
    long getTtl() { return ttl; }
    
    String getCorrelationId() { return correlationId; }
    
    Map<String, String> getHeaders() { return headers; }
    
    String getTitle() { return title; }
    
    MsgStatus getStatus() { return status; }
    
    Integer getRetryCount() { return retryCount; }
    
    String getGroupId() { return groupId; }
    
    Map<String, Object> getMetadata() { return metadata; }
}

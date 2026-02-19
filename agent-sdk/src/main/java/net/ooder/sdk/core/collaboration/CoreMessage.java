package net.ooder.sdk.core.collaboration;

import java.util.Collections;
import java.util.Map;

public interface CoreMessage {
    
    String getMessageId();
    
    String getSource();
    
    String getTarget();
    
    MessageType getType();
    
    Map<String, Object> getPayload();
    
    long getTimestamp();
    
    int getPriority();
    
    long getTtl();
    
    String getCorrelationId();
    
    Map<String, String> getHeaders();
    
    default String getTitle() { return null; }
    
    default MsgStatus getStatus() { return MsgStatus.NORMAL; }
    
    default Integer getRetryCount() { return 0; }
    
    default String getGroupId() { return null; }
    
    default Map<String, Object> getMetadata() { return Collections.emptyMap(); }
    
    default long getExpiresAt() { return getTimestamp() + getTtl(); }
    
    default boolean isExpired() { 
        return getTtl() > 0 && System.currentTimeMillis() > getExpiresAt(); 
    }
    
    default boolean isUrgent() { return getPriority() >= 8; }
}

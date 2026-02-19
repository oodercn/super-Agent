package net.ooder.sdk.core.collaboration;

import java.util.Map;

public interface CoreEvent {
    
    String getEventId();
    
    String getEventType();
    
    Object getSource();
    
    Object getTarget();
    
    Map<String, Object> getData();
    
    long getTimestamp();
    
    boolean isCancelled();
    
    void cancel();
}

package net.ooder.sdk.core.collaboration;

public interface CoreEventHandler {
    
    void handle(CoreEvent event);
    
    boolean canHandle(String eventType);
    
    int getPriority();
}

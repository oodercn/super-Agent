package net.ooder.sdk.core.collaboration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventDispatcher {
    
    void registerHandler(String eventType, CoreEventHandler handler);
    
    void unregisterHandler(String eventType, CoreEventHandler handler);
    
    void dispatch(CoreEvent event);
    
    CompletableFuture<Void> dispatchAsync(CoreEvent event);
    
    List<CoreEventHandler> getHandlers(String eventType);
    
    void clearHandlers(String eventType);
}

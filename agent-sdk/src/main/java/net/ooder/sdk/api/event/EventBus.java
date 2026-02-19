package net.ooder.sdk.api.event;

import java.util.concurrent.CompletableFuture;

public interface EventBus {
    
    <T extends Event> void publish(T event);
    
    <T extends Event> void subscribe(Class<T> eventType, EventHandler<T> handler);
    
    <T extends Event> void unsubscribe(Class<T> eventType, EventHandler<T> handler);
    
    <T extends Event> void publishSync(T event);
    
    <T extends Event, R> CompletableFuture<R> publishAndWait(T event, Class<R> resultType);
    
    void shutdown();
}

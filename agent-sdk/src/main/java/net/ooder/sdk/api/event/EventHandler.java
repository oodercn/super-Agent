package net.ooder.sdk.api.event;

@FunctionalInterface
public interface EventHandler<T extends Event> {
    void handle(T event);
}

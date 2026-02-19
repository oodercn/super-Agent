package net.ooder.sdk.service.event;

import net.ooder.sdk.api.event.Event;
import net.ooder.sdk.api.event.EventBus;
import net.ooder.sdk.api.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class EventBusImpl implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(EventBusImpl.class);

    private final Map<Class<? extends Event>, List<EventHandler<?>>> handlers;
    private final ExecutorService executor;

    public EventBusImpl() {
        this.handlers = new ConcurrentHashMap<Class<? extends Event>, List<EventHandler<?>>>();
        this.executor = Executors.newCachedThreadPool();
        log.info("EventBusImpl initialized");
    }

    @Override
    public <T extends Event> void publish(T event) {
        if (event == null) return;

        Class<? extends Event> eventType = event.getClass();
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);

        if (eventHandlers == null || eventHandlers.isEmpty()) {
            log.debug("No handlers registered for event type: {}", eventType.getSimpleName());
            return;
        }

        log.debug("Publishing event: {} to {} handlers", eventType.getSimpleName(), eventHandlers.size());

        for (EventHandler<?> handler : eventHandlers) {
            executor.submit(new Runnable() {
                @Override
                @SuppressWarnings("unchecked")
                public void run() {
                    try {
                        ((EventHandler<T>) handler).handle(event);
                    } catch (Exception e) {
                        log.error("Error handling event: {}", eventType.getSimpleName(), e);
                    }
                }
            });
        }
    }

    @Override
    public <T extends Event> void subscribe(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<EventHandler<?>>()).add(handler);
        log.debug("Subscribed handler for event type: {}", eventType.getSimpleName());
    }

    @Override
    public <T extends Event> void unsubscribe(Class<T> eventType, EventHandler<T> handler) {
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);
        if (eventHandlers != null) {
            eventHandlers.remove(handler);
            log.debug("Unsubscribed handler for event type: {}", eventType.getSimpleName());
        }
    }

    @Override
    public <T extends Event> void publishSync(T event) {
        if (event == null) return;

        Class<? extends Event> eventType = event.getClass();
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);

        if (eventHandlers == null || eventHandlers.isEmpty()) {
            return;
        }

        log.debug("Publishing event synchronously: {} to {} handlers", eventType.getSimpleName(), eventHandlers.size());

        for (EventHandler<?> handler : eventHandlers) {
            try {
                @SuppressWarnings("unchecked")
                EventHandler<T> typedHandler = (EventHandler<T>) handler;
                typedHandler.handle(event);
            } catch (Exception e) {
                log.error("Error handling event: {}", eventType.getSimpleName(), e);
            }
        }
    }

    @Override
    public <T extends Event, R> CompletableFuture<R> publishAndWait(T event, Class<R> resultType) {
        return CompletableFuture.supplyAsync(new java.util.function.Supplier<R>() {
            @Override
            public R get() {
                publishSync(event);
                return null;
            }
        }, executor);
    }

    @Override
    public void shutdown() {
        log.info("Shutting down EventBus");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        handlers.clear();
        log.info("EventBus shutdown complete");
    }

    public int getHandlerCount(Class<? extends Event> eventType) {
        List<EventHandler<?>> eventHandlers = handlers.get(eventType);
        return eventHandlers != null ? eventHandlers.size() : 0;
    }

    public int getTotalHandlerCount() {
        int total = 0;
        for (List<EventHandler<?>> eventHandlers : handlers.values()) {
            total += eventHandlers.size();
        }
        return total;
    }
}

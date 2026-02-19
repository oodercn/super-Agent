
package net.ooder.sdk.infra.lifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class LifecycleManager {
    
    private static final LifecycleManager INSTANCE = new LifecycleManager();
    
    private final List<LifecycleComponent> components = new CopyOnWriteArrayList<>();
    private final AtomicInteger state = new AtomicInteger(State.CREATED.ordinal());
    private final List<ShutdownHook> shutdownHooks = new CopyOnWriteArrayList<>();
    
    private LifecycleManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "lifecycle-shutdown-hook"));
    }
    
    public static LifecycleManager getInstance() {
        return INSTANCE;
    }
    
    public void register(LifecycleComponent component) {
        components.add(component);
    }
    
    public void unregister(LifecycleComponent component) {
        components.remove(component);
    }
    
    public void addShutdownHook(ShutdownHook hook) {
        shutdownHooks.add(hook);
    }
    
    public void removeShutdownHook(ShutdownHook hook) {
        shutdownHooks.remove(hook);
    }
    
    public synchronized void initialize() throws Exception {
        if (!state.compareAndSet(State.CREATED.ordinal(), State.INITIALIZING.ordinal())) {
            throw new IllegalStateException("Lifecycle already initialized");
        }
        
        List<Exception> exceptions = new ArrayList<>();
        for (LifecycleComponent component : components) {
            try {
                component.initialize();
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        
        if (!exceptions.isEmpty()) {
            state.set(State.FAILED.ordinal());
            throw new LifecycleException("Failed to initialize components", exceptions);
        }
        
        state.set(State.INITIALIZED.ordinal());
    }
    
    public synchronized void start() throws Exception {
        int currentState = state.get();
        if (currentState != State.INITIALIZED.ordinal()) {
            throw new IllegalStateException("Lifecycle not initialized, current state: " + State.values()[currentState]);
        }
        
        state.set(State.STARTING.ordinal());
        
        List<Exception> exceptions = new ArrayList<>();
        for (LifecycleComponent component : components) {
            try {
                component.start();
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        
        if (!exceptions.isEmpty()) {
            state.set(State.FAILED.ordinal());
            throw new LifecycleException("Failed to start components", exceptions);
        }
        
        state.set(State.RUNNING.ordinal());
    }
    
    public synchronized void stop() {
        int currentState = state.get();
        if (currentState != State.RUNNING.ordinal()) {
            return;
        }
        
        state.set(State.STOPPING.ordinal());
        
        for (int i = components.size() - 1; i >= 0; i--) {
            try {
                components.get(i).stop();
            } catch (Exception e) {
            }
        }
        
        state.set(State.STOPPED.ordinal());
    }
    
    public synchronized void shutdown() {
        int currentState = state.get();
        
        if (currentState == State.RUNNING.ordinal()) {
            stop();
        }
        
        state.set(State.SHUTTING_DOWN.ordinal());
        
        for (ShutdownHook hook : shutdownHooks) {
            try {
                hook.execute();
            } catch (Exception e) {
            }
        }
        
        for (int i = components.size() - 1; i >= 0; i--) {
            try {
                components.get(i).destroy();
            } catch (Exception e) {
            }
        }
        
        state.set(State.SHUTDOWN.ordinal());
    }
    
    public synchronized void reset() {
        int currentState = state.get();
        
        if (currentState == State.RUNNING.ordinal()) {
            stop();
        }
        
        if (currentState == State.SHUTDOWN.ordinal() || currentState == State.STOPPED.ordinal()) {
            state.set(State.CREATED.ordinal());
        }
        
        components.clear();
        shutdownHooks.clear();
        state.set(State.CREATED.ordinal());
    }
    
    public State getState() {
        return State.values()[state.get()];
    }
    
    public boolean isRunning() {
        return state.get() == State.RUNNING.ordinal();
    }
    
    public boolean isShutdown() {
        int currentState = state.get();
        return currentState == State.SHUTTING_DOWN.ordinal() || currentState == State.SHUTDOWN.ordinal();
    }
    
    public enum State {
        CREATED,
        INITIALIZING,
        INITIALIZED,
        STARTING,
        RUNNING,
        STOPPING,
        STOPPED,
        SHUTTING_DOWN,
        SHUTDOWN,
        FAILED
    }
    
    public static class LifecycleException extends Exception {
        private final List<Exception> causes;
        
        public LifecycleException(String message, List<Exception> causes) {
            super(message);
            this.causes = causes;
        }
        
        public List<Exception> getCauses() {
            return causes;
        }
    }
}

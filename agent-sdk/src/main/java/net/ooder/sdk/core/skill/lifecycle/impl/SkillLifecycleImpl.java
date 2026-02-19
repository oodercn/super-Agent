
package net.ooder.sdk.core.skill.lifecycle.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.core.skill.lifecycle.SkillLifecycle;
import net.ooder.sdk.core.skill.lifecycle.SkillState;

public class SkillLifecycleImpl implements SkillLifecycle {
    
    private static final Logger log = LoggerFactory.getLogger(SkillLifecycleImpl.class);
    
    private final String skillId;
    private volatile SkillState currentState = SkillState.CREATED;
    private volatile long startTime = 0;
    private volatile long lastStateChangeTime = System.currentTimeMillis();
    private final AtomicInteger restartCount = new AtomicInteger(0);
    private final List<LifecycleListener> listeners = new CopyOnWriteArrayList<LifecycleListener>();
    private final Object stateLock = new Object();
    
    public SkillLifecycleImpl(String skillId) {
        this.skillId = skillId;
        log.debug("SkillLifecycle created for skill: {}", skillId);
    }
    
    @Override
    public void initialize() {
        synchronized (stateLock) {
            if (currentState != SkillState.CREATED && currentState != SkillState.DESTROYED) {
                throw new IllegalStateException("Cannot initialize from state: " + currentState);
            }
            transitionTo(SkillState.INITIALIZING);
            try {
                doInitialize();
                transitionTo(SkillState.INITIALIZED);
            } catch (Exception e) {
                transitionTo(SkillState.ERROR);
                notifyError(e);
                throw new RuntimeException("Initialization failed", e);
            }
        }
    }
    
    @Override
    public void start() {
        synchronized (stateLock) {
            if (!currentState.canStart()) {
                throw new IllegalStateException("Cannot start from state: " + currentState);
            }
            transitionTo(SkillState.STARTING);
            try {
                doStart();
                startTime = System.currentTimeMillis();
                transitionTo(SkillState.RUNNING);
            } catch (Exception e) {
                transitionTo(SkillState.ERROR);
                notifyError(e);
                throw new RuntimeException("Start failed", e);
            }
        }
    }
    
    @Override
    public void stop() {
        synchronized (stateLock) {
            if (!currentState.canStop()) {
                throw new IllegalStateException("Cannot stop from state: " + currentState);
            }
            transitionTo(SkillState.STOPPING);
            try {
                doStop();
                startTime = 0;
                transitionTo(SkillState.STOPPED);
            } catch (Exception e) {
                transitionTo(SkillState.ERROR);
                notifyError(e);
                throw new RuntimeException("Stop failed", e);
            }
        }
    }
    
    @Override
    public void pause() {
        synchronized (stateLock) {
            if (!currentState.canPause()) {
                throw new IllegalStateException("Cannot pause from state: " + currentState);
            }
            SkillState previousState = currentState;
            transitionTo(SkillState.PAUSED);
            try {
                doPause();
            } catch (Exception e) {
                transitionTo(previousState);
                notifyError(e);
                throw new RuntimeException("Pause failed", e);
            }
        }
    }
    
    @Override
    public void resume() {
        synchronized (stateLock) {
            if (!currentState.canResume()) {
                throw new IllegalStateException("Cannot resume from state: " + currentState);
            }
            transitionTo(SkillState.RUNNING);
            try {
                doResume();
            } catch (Exception e) {
                transitionTo(SkillState.ERROR);
                notifyError(e);
                throw new RuntimeException("Resume failed", e);
            }
        }
    }
    
    @Override
    public void destroy() {
        synchronized (stateLock) {
            if (currentState.isTerminal()) {
                return;
            }
            transitionTo(SkillState.DESTROYING);
            try {
                if (currentState == SkillState.RUNNING || currentState == SkillState.PAUSED) {
                    doStop();
                }
                doDestroy();
                startTime = 0;
                transitionTo(SkillState.DESTROYED);
            } catch (Exception e) {
                transitionTo(SkillState.ERROR);
                notifyError(e);
                throw new RuntimeException("Destroy failed", e);
            }
        }
    }
    
    @Override
    public SkillState getState() {
        return currentState;
    }
    
    @Override
    public long getUptime() {
        if (startTime > 0 && (currentState == SkillState.RUNNING || currentState == SkillState.PAUSED)) {
            return System.currentTimeMillis() - startTime;
        }
        return 0;
    }
    
    @Override
    public long getStartTime() {
        return startTime;
    }
    
    @Override
    public int getRestartCount() {
        return restartCount.get();
    }
    
    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        listeners.remove(listener);
    }
    
    public CompletableFuture<Void> initializeAsync() {
        return CompletableFuture.runAsync(this::initialize);
    }
    
    public CompletableFuture<Void> startAsync() {
        return CompletableFuture.runAsync(this::start);
    }
    
    public CompletableFuture<Void> stopAsync() {
        return CompletableFuture.runAsync(this::stop);
    }
    
    public CompletableFuture<Void> pauseAsync() {
        return CompletableFuture.runAsync(this::pause);
    }
    
    public CompletableFuture<Void> resumeAsync() {
        return CompletableFuture.runAsync(this::resume);
    }
    
    public CompletableFuture<Void> destroyAsync() {
        return CompletableFuture.runAsync(this::destroy);
    }
    
    public void restart() {
        stop();
        restartCount.incrementAndGet();
        start();
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public long getLastStateChangeTime() {
        return lastStateChangeTime;
    }
    
    private void transitionTo(SkillState newState) {
        SkillState oldState = currentState;
        currentState = newState;
        lastStateChangeTime = System.currentTimeMillis();
        log.info("Skill {} state changed: {} -> {}", skillId, oldState, newState);
        notifyStateChange(oldState, newState);
    }
    
    private void notifyStateChange(SkillState oldState, SkillState newState) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.onStateChange(oldState, newState);
            } catch (Exception e) {
                log.warn("Listener notification failed for state change: {} -> {}", oldState, newState, e);
            }
        }
    }
    
    private void notifyError(Throwable error) {
        log.error("Skill {} encountered error: {}", skillId, error.getMessage(), error);
        for (LifecycleListener listener : listeners) {
            try {
                listener.onError(error);
            } catch (Exception e) {
                log.warn("Listener notification failed for error", e);
            }
        }
    }
    
    protected void doInitialize() throws Exception {
        log.debug("Skill {} initializing", skillId);
    }
    
    protected void doStart() throws Exception {
        log.debug("Skill {} starting", skillId);
    }
    
    protected void doStop() throws Exception {
        log.debug("Skill {} stopping", skillId);
    }
    
    protected void doPause() throws Exception {
        log.debug("Skill {} pausing", skillId);
    }
    
    protected void doResume() throws Exception {
        log.debug("Skill {} resuming", skillId);
    }
    
    protected void doDestroy() throws Exception {
        log.debug("Skill {} destroying", skillId);
    }
    
    public void shutdown() {
        log.info("Shutting down skill lifecycle for: {}", skillId);
        if (currentState != SkillState.DESTROYED && currentState != SkillState.ERROR) {
            try {
                destroy();
            } catch (Exception e) {
                log.warn("Error during shutdown for skill: {}", skillId, e);
            }
        }
        listeners.clear();
    }
}

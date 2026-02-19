package net.ooder.sdk.core.skill.lifecycle.impl;

import net.ooder.sdk.core.skill.lifecycle.SkillLifecycle;
import net.ooder.sdk.core.skill.lifecycle.SkillState;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkillLifecycleImplTest {
    
    private SkillLifecycleImpl lifecycle;
    
    @Before
    public void setUp() {
        lifecycle = new SkillLifecycleImpl("skill-001");
    }
    
    @Test
    public void testInitialization() {
        assertEquals("skill-001", lifecycle.getSkillId());
        assertEquals(SkillState.CREATED, lifecycle.getState());
    }
    
    @Test
    public void testInitialize() {
        lifecycle.initialize();
        
        assertEquals(SkillState.INITIALIZED, lifecycle.getState());
    }
    
    @Test
    public void testStart() {
        lifecycle.initialize();
        lifecycle.start();
        
        assertEquals(SkillState.RUNNING, lifecycle.getState());
    }
    
    @Test
    public void testStop() {
        lifecycle.initialize();
        lifecycle.start();
        lifecycle.stop();
        
        assertEquals(SkillState.STOPPED, lifecycle.getState());
    }
    
    @Test
    public void testPause() {
        lifecycle.initialize();
        lifecycle.start();
        lifecycle.pause();
        
        assertEquals(SkillState.PAUSED, lifecycle.getState());
    }
    
    @Test
    public void testResume() {
        lifecycle.initialize();
        lifecycle.start();
        lifecycle.pause();
        lifecycle.resume();
        
        assertEquals(SkillState.RUNNING, lifecycle.getState());
    }
    
    @Test
    public void testDestroy() {
        lifecycle.initialize();
        lifecycle.start();
        lifecycle.stop();
        lifecycle.destroy();
        
        assertEquals(SkillState.DESTROYED, lifecycle.getState());
    }
    
    @Test
    public void testGetUptime() {
        assertEquals(0, lifecycle.getUptime());
        
        lifecycle.initialize();
        lifecycle.start();
        
        assertTrue(lifecycle.getUptime() >= 0);
    }
    
    @Test
    public void testGetStartTime() {
        assertEquals(0, lifecycle.getStartTime());
        
        lifecycle.initialize();
        lifecycle.start();
        
        assertTrue(lifecycle.getStartTime() > 0);
    }
    
    @Test
    public void testGetRestartCount() {
        assertEquals(0, lifecycle.getRestartCount());
    }
    
    @Test
    public void testAddLifecycleListener() {
        final AtomicBoolean stateChanged = new AtomicBoolean(false);
        
        lifecycle.addLifecycleListener(new SkillLifecycle.LifecycleListener() {
            @Override
            public void onStateChange(SkillState oldState, SkillState newState) {
                stateChanged.set(true);
            }
            @Override
            public void onError(Throwable error) {}
        });
        
        lifecycle.initialize();
        
        assertTrue(stateChanged.get());
    }
    
    @Test
    public void testRemoveLifecycleListener() {
        final AtomicBoolean stateChanged = new AtomicBoolean(false);
        
        SkillLifecycle.LifecycleListener listener = new SkillLifecycle.LifecycleListener() {
            @Override
            public void onStateChange(SkillState oldState, SkillState newState) {
                stateChanged.set(true);
            }
            @Override
            public void onError(Throwable error) {}
        };
        
        lifecycle.addLifecycleListener(listener);
        lifecycle.removeLifecycleListener(listener);
        
        lifecycle.initialize();
        
        assertFalse(stateChanged.get());
    }
    
    @Test
    public void testInitializeAsync() throws Exception {
        lifecycle.initializeAsync().get(10, TimeUnit.SECONDS);
        
        assertEquals(SkillState.INITIALIZED, lifecycle.getState());
    }
    
    @Test
    public void testStartAsync() throws Exception {
        lifecycle.initialize();
        lifecycle.startAsync().get(10, TimeUnit.SECONDS);
        
        assertEquals(SkillState.RUNNING, lifecycle.getState());
    }
    
    @Test
    public void testStopAsync() throws Exception {
        lifecycle.initialize();
        lifecycle.start();
        lifecycle.stopAsync().get(10, TimeUnit.SECONDS);
        
        assertEquals(SkillState.STOPPED, lifecycle.getState());
    }
    
    @Test
    public void testPauseAsync() throws Exception {
        lifecycle.initialize();
        lifecycle.start();
        lifecycle.pauseAsync().get(10, TimeUnit.SECONDS);
        
        assertEquals(SkillState.PAUSED, lifecycle.getState());
    }
    
    @Test
    public void testResumeAsync() throws Exception {
        lifecycle.initialize();
        lifecycle.start();
        lifecycle.pause();
        lifecycle.resumeAsync().get(10, TimeUnit.SECONDS);
        
        assertEquals(SkillState.RUNNING, lifecycle.getState());
    }
    
    @Test
    public void testStateTransition() {
        assertEquals(SkillState.CREATED, lifecycle.getState());
        
        lifecycle.initialize();
        assertEquals(SkillState.INITIALIZED, lifecycle.getState());
        
        lifecycle.start();
        assertEquals(SkillState.RUNNING, lifecycle.getState());
        
        lifecycle.pause();
        assertEquals(SkillState.PAUSED, lifecycle.getState());
        
        lifecycle.resume();
        assertEquals(SkillState.RUNNING, lifecycle.getState());
        
        lifecycle.stop();
        assertEquals(SkillState.STOPPED, lifecycle.getState());
        
        lifecycle.destroy();
        assertEquals(SkillState.DESTROYED, lifecycle.getState());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotStartFromCreated() {
        lifecycle.start();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotStopFromCreated() {
        lifecycle.stop();
    }
}

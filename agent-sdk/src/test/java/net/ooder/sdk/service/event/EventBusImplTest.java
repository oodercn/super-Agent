package net.ooder.sdk.service.event;

import net.ooder.sdk.api.event.EventBus;
import net.ooder.sdk.api.event.EventHandler;
import net.ooder.sdk.api.event.SkillInstalledEvent;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

public class EventBusImplTest {
    
    private EventBusImpl eventBus;
    
    @Before
    public void setUp() {
        eventBus = new EventBusImpl();
    }
    
    @After
    public void tearDown() {
        eventBus.shutdown();
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(eventBus);
    }
    
    @Test
    public void testPublishAndSubscribe() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        
        eventBus.subscribe(SkillInstalledEvent.class, new EventHandler<SkillInstalledEvent>() {
            @Override
            public void handle(SkillInstalledEvent event) {
                counter.incrementAndGet();
            }
        });
        
        SkillInstalledEvent event = new SkillInstalledEvent("skill-001", "Test Skill", "1.0.0");
        eventBus.publish(event);
        
        Thread.sleep(100);
        
        assertEquals(1, counter.get());
    }
    
    @Test
    public void testMultipleHandlers() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        
        eventBus.subscribe(SkillInstalledEvent.class, new EventHandler<SkillInstalledEvent>() {
            @Override
            public void handle(SkillInstalledEvent event) {
                counter.incrementAndGet();
            }
        });
        
        eventBus.subscribe(SkillInstalledEvent.class, new EventHandler<SkillInstalledEvent>() {
            @Override
            public void handle(SkillInstalledEvent event) {
                counter.incrementAndGet();
            }
        });
        
        SkillInstalledEvent event = new SkillInstalledEvent("skill-001", "Test Skill", "1.0.0");
        eventBus.publish(event);
        
        Thread.sleep(100);
        
        assertEquals(2, counter.get());
    }
    
    @Test
    public void testUnsubscribe() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        
        EventHandler<SkillInstalledEvent> handler = new EventHandler<SkillInstalledEvent>() {
            @Override
            public void handle(SkillInstalledEvent event) {
                counter.incrementAndGet();
            }
        };
        
        eventBus.subscribe(SkillInstalledEvent.class, handler);
        eventBus.unsubscribe(SkillInstalledEvent.class, handler);
        
        SkillInstalledEvent event = new SkillInstalledEvent("skill-001", "Test Skill", "1.0.0");
        eventBus.publish(event);
        
        Thread.sleep(100);
        
        assertEquals(0, counter.get());
    }
    
    @Test
    public void testPublishNull() {
        eventBus.publish(null);
    }
    
    @Test
    public void testNoHandlers() {
        SkillInstalledEvent event = new SkillInstalledEvent("skill-001", "Test Skill", "1.0.0");
        eventBus.publish(event);
    }
    
    @Test
    public void testHandlerException() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        
        eventBus.subscribe(SkillInstalledEvent.class, new EventHandler<SkillInstalledEvent>() {
            @Override
            public void handle(SkillInstalledEvent event) {
                throw new RuntimeException("Test exception");
            }
        });
        
        eventBus.subscribe(SkillInstalledEvent.class, new EventHandler<SkillInstalledEvent>() {
            @Override
            public void handle(SkillInstalledEvent event) {
                counter.incrementAndGet();
            }
        });
        
        SkillInstalledEvent event = new SkillInstalledEvent("skill-001", "Test Skill", "1.0.0");
        eventBus.publish(event);
        
        Thread.sleep(100);
        
        assertEquals(1, counter.get());
    }
    
    @Test
    public void testShutdown() {
        eventBus.subscribe(SkillInstalledEvent.class, new EventHandler<SkillInstalledEvent>() {
            @Override
            public void handle(SkillInstalledEvent event) {}
        });
        
        eventBus.shutdown();
    }
}

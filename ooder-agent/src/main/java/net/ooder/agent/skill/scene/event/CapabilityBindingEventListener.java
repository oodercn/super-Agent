package net.ooder.mvp.skill.scene.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CapabilityBindingEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityBindingEventListener.class);
    
    @EventListener
    public void onBindingCreated(Object event) {
        log.info("[onBindingCreated] Capability binding created: {}", event);
    }
    
    @EventListener
    public void onBindingRemoved(Object event) {
        log.info("[onBindingRemoved] Capability binding removed: {}", event);
    }
    
    @EventListener
    public void onBindingUpdated(Object event) {
        log.info("[onBindingUpdated] Capability binding updated: {}", event);
    }
}

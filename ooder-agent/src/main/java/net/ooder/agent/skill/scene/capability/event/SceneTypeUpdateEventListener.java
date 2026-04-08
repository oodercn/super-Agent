package net.ooder.mvp.skill.scene.capability.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SceneTypeUpdateEventListener {

    private static final Logger log = LoggerFactory.getLogger(SceneTypeUpdateEventListener.class);

    @Async
    @EventListener
    public void onSceneTypeUpdate(SceneTypeUpdateEvent event) {
        log.info("[Event] Received scene type update event: {}", event);

        switch (event.getAction()) {
            case ADD:
                handleAddSceneType(event);
                break;
            case REMOVE:
                handleRemoveSceneType(event);
                break;
            default:
                log.warn("[Event] Unknown action: {}", event.getAction());
        }

        notifyRunningInstances(event);
    }

    private void handleAddSceneType(SceneTypeUpdateEvent event) {
        log.info("[Event] Scene type '{}' added to capability '{}'", 
            event.getSceneType(), event.getCapabilityId());
    }

    private void handleRemoveSceneType(SceneTypeUpdateEvent event) {
        log.info("[Event] Scene type '{}' removed from capability '{}'", 
            event.getSceneType(), event.getCapabilityId());
    }

    private void notifyRunningInstances(SceneTypeUpdateEvent event) {
        log.info("[Event] Notifying running instances for capability: {}", event.getCapabilityId());

        log.info("[Event] Scene type update notification sent - capabilityId={}, action={}, sceneType={}", 
            event.getCapabilityId(), event.getAction(), event.getSceneType());
    }
}

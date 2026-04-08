package net.ooder.mvp.skill.scene.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SceneStateEventPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(SceneStateEventPublisher.class);
    
    private final List<SceneStateChangeListener> listeners = new CopyOnWriteArrayList<>();
    
    @Autowired(required = false)
    public void setListeners(List<SceneStateChangeListener> listenerList) {
        if (listenerList != null) {
            listeners.addAll(listenerList);
            listeners.sort((a, b) -> Integer.compare(a.getOrder(), b.getOrder()));
            log.info("[SceneStateEventPublisher] Registered {} listeners", listeners.size());
        }
    }
    
    public void addListener(SceneStateChangeListener listener) {
        listeners.add(listener);
        listeners.sort((a, b) -> Integer.compare(a.getOrder(), b.getOrder()));
    }
    
    public void removeListener(SceneStateChangeListener listener) {
        listeners.remove(listener);
    }
    
    public void publishSceneCreated(SceneStateEvent event) {
        log.debug("[publishSceneCreated] SceneGroup: {}", event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onSceneCreated(event);
            } catch (Exception e) {
                log.error("[publishSceneCreated] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishSceneActivated(SceneStateEvent event) {
        log.debug("[publishSceneActivated] SceneGroup: {}", event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onSceneActivated(event);
            } catch (Exception e) {
                log.error("[publishSceneActivated] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishSceneDeactivated(SceneStateEvent event) {
        log.debug("[publishSceneDeactivated] SceneGroup: {}", event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onSceneDeactivated(event);
            } catch (Exception e) {
                log.error("[publishSceneDeactivated] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishSceneDestroyed(SceneStateEvent event) {
        log.debug("[publishSceneDestroyed] SceneGroup: {}", event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onSceneDestroyed(event);
            } catch (Exception e) {
                log.error("[publishSceneDestroyed] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishParticipantJoined(SceneStateEvent event) {
        log.debug("[publishParticipantJoined] Participant: {} -> SceneGroup: {}", 
            event.getParticipantId(), event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onParticipantJoined(event);
            } catch (Exception e) {
                log.error("[publishParticipantJoined] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishParticipantLeft(SceneStateEvent event) {
        log.debug("[publishParticipantLeft] Participant: {} <- SceneGroup: {}", 
            event.getParticipantId(), event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onParticipantLeft(event);
            } catch (Exception e) {
                log.error("[publishParticipantLeft] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishParticipantRoleChanged(SceneStateEvent event) {
        log.debug("[publishParticipantRoleChanged] Participant: {}, {} -> {}", 
            event.getParticipantId(), event.getOldState(), event.getNewState());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onParticipantRoleChanged(event);
            } catch (Exception e) {
                log.error("[publishParticipantRoleChanged] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishCapabilityBound(SceneStateEvent event) {
        log.debug("[publishCapabilityBound] SceneGroup: {}", event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onCapabilityBound(event);
            } catch (Exception e) {
                log.error("[publishCapabilityBound] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
    
    public void publishCapabilityUnbound(SceneStateEvent event) {
        log.debug("[publishCapabilityUnbound] SceneGroup: {}", event.getSceneGroupId());
        for (SceneStateChangeListener listener : listeners) {
            try {
                listener.onCapabilityUnbound(event);
            } catch (Exception e) {
                log.error("[publishCapabilityUnbound] Listener {} failed: {}", 
                    listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}

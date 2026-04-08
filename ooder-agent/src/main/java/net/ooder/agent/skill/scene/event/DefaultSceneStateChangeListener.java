package net.ooder.mvp.skill.scene.event;

import net.ooder.mvp.skill.scene.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSceneStateChangeListener implements SceneStateChangeListener {
    
    private static final Logger log = LoggerFactory.getLogger(DefaultSceneStateChangeListener.class);
    
    @Autowired(required = false)
    private TodoService todoService;
    
    @Override
    public void onSceneCreated(SceneStateEvent event) {
        log.info("[onSceneCreated] SceneGroup: {}, Source: {}", 
            event.getSceneGroupId(), event.getSource());
    }
    
    @Override
    public void onSceneActivated(SceneStateEvent event) {
        log.info("[onSceneActivated] SceneGroup: {}", event.getSceneGroupId());
        
        if (todoService != null) {
            try {
                String title = "场景已激活";
                String description = "场景组 " + event.getSceneGroupId() + " 已成功激活";
                todoService.createSceneNotificationTodo(
                    event.getSceneGroupId(), 
                    "current-user", 
                    title, 
                    description
                );
            } catch (Exception e) {
                log.warn("[onSceneActivated] Failed to create todo: {}", e.getMessage());
            }
        }
    }
    
    @Override
    public void onSceneDeactivated(SceneStateEvent event) {
        log.info("[onSceneDeactivated] SceneGroup: {}", event.getSceneGroupId());
    }
    
    @Override
    public void onSceneDestroyed(SceneStateEvent event) {
        log.info("[onSceneDestroyed] SceneGroup: {}", event.getSceneGroupId());
    }
    
    @Override
    public void onParticipantJoined(SceneStateEvent event) {
        log.info("[onParticipantJoined] Participant: {} joined SceneGroup: {}", 
            event.getParticipantId(), event.getSceneGroupId());
        
        if (todoService != null) {
            try {
                String title = "新成员加入场景";
                String description = (event.getParticipantName() != null ? 
                    event.getParticipantName() : event.getParticipantId()) + 
                    " 加入了场景组";
                todoService.createSceneNotificationTodo(
                    event.getSceneGroupId(), 
                    "current-user", 
                    title, 
                    description
                );
            } catch (Exception e) {
                log.warn("[onParticipantJoined] Failed to create todo: {}", e.getMessage());
            }
        }
    }
    
    @Override
    public void onParticipantLeft(SceneStateEvent event) {
        log.info("[onParticipantLeft] Participant: {} left SceneGroup: {}", 
            event.getParticipantId(), event.getSceneGroupId());
    }
    
    @Override
    public void onParticipantRoleChanged(SceneStateEvent event) {
        log.info("[onParticipantRoleChanged] Participant: {}, Role: {} -> {}", 
            event.getParticipantId(), event.getOldState(), event.getNewState());
    }
    
    @Override
    public void onCapabilityBound(SceneStateEvent event) {
        log.info("[onCapabilityBound] SceneGroup: {}, Capability: {}", 
            event.getSceneGroupId(), event.getData() != null ? 
            event.getData().get("capabilityId") : "unknown");
    }
    
    @Override
    public void onCapabilityUnbound(SceneStateEvent event) {
        log.info("[onCapabilityUnbound] SceneGroup: {}, Capability: {}", 
            event.getSceneGroupId(), event.getData() != null ? 
            event.getData().get("capabilityId") : "unknown");
    }
    
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}

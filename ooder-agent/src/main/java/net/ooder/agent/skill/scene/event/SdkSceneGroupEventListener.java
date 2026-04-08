package net.ooder.mvp.skill.scene.event;

import net.ooder.mvp.skill.scene.service.TodoService;
import net.ooder.scene.group.SceneGroup;
import net.ooder.scene.participant.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class SdkSceneGroupEventListener implements Consumer<SceneGroup> {
    
    private static final Logger log = LoggerFactory.getLogger(SdkSceneGroupEventListener.class);
    
    @Autowired(required = false)
    private TodoService todoService;
    
    @Override
    public void accept(SceneGroup sceneGroup) {
        log.info("[accept] SceneGroup event received: {}", sceneGroup.getSceneGroupId());
    }
    
    public void onMemberJoined(String sceneGroupId, String sceneGroupName, Participant participant) {
        log.info("[onMemberJoined] Member {} joined scene group {}", 
            participant.getParticipantId(), sceneGroupId);
        
        if (todoService != null) {
            try {
                String title = "新成员加入场景";
                String description = participant.getParticipantId() + " 加入了场景组 " + sceneGroupName;
                todoService.createSceneNotificationTodo(sceneGroupId, "current-user", title, description);
                log.info("[onMemberJoined] Created scene notification todo for member join");
            } catch (Exception e) {
                log.error("[onMemberJoined] Failed to create todo: {}", e.getMessage());
            }
        }
    }
    
    public void onMemberLeft(String sceneGroupId, String participantId) {
        log.info("[onMemberLeft] Member {} left scene group {}", participantId, sceneGroupId);
    }
    
    public void onStatusChanged(String sceneGroupId, String sceneGroupName, String oldStatus, String newStatus) {
        log.info("[onStatusChanged] SceneGroup {} status changed: {} -> {}", 
            sceneGroupId, oldStatus, newStatus);
        
        if (todoService != null && "ACTIVATED".equals(newStatus)) {
            try {
                String title = "场景已激活";
                String description = "场景组 " + sceneGroupName + " 已成功激活";
                todoService.createSceneNotificationTodo(sceneGroupId, "current-user", title, description);
                log.info("[onStatusChanged] Created scene notification todo for activation");
            } catch (Exception e) {
                log.error("[onStatusChanged] Failed to create todo: {}", e.getMessage());
            }
        }
    }
    
    public void onCapabilityBound(String sceneGroupId, String capabilityId) {
        log.info("[onCapabilityBound] Capability {} bound to scene group {}", 
            capabilityId, sceneGroupId);
    }
    
    public void onCapabilityUnbound(String sceneGroupId, String capabilityId) {
        log.info("[onCapabilityUnbound] Capability {} unbound from scene group {}", 
            capabilityId, sceneGroupId);
    }
}

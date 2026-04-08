package net.ooder.mvp.skill.scene.event;

import net.ooder.mvp.skill.scene.dto.scene.SceneGroupEventLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SceneGroupEventLogService {

    private static final Logger log = LoggerFactory.getLogger(SceneGroupEventLogService.class);
    
    private final Map<String, List<SceneGroupEventLogDTO>> eventLogs = new ConcurrentHashMap<>();
    
    public void logEvent(String sceneGroupId, String eventType, String action, 
                        String participantId, String participantName, 
                        String status, String message) {
        SceneGroupEventLogDTO event = new SceneGroupEventLogDTO();
        event.setEventId("evt-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8));
        event.setSceneGroupId(sceneGroupId);
        event.setEventType(eventType);
        event.setAction(action);
        event.setParticipantId(participantId);
        event.setParticipantName(participantName);
        event.setStatus(status);
        event.setMessage(message);
        event.setTimestamp(System.currentTimeMillis());
        
        eventLogs.computeIfAbsent(sceneGroupId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(0, event);
        
        log.debug("[logEvent] Recorded event: {} - {} for sceneGroup: {}", eventType, action, sceneGroupId);
    }
    
    public void logCreateEvent(String sceneGroupId, String creatorId, String creatorName, String sceneGroupName) {
        logEvent(sceneGroupId, "SCENE_CREATE", "创建场景组", 
                 creatorId, creatorName, 
                 "SUCCESS", "创建场景组: " + sceneGroupName);
    }
    
    public void logParticipantJoin(String sceneGroupId, String participantId, String participantName, String role) {
        logEvent(sceneGroupId, "PARTICIPANT_JOIN", "加入参与者",
                 participantId, participantName,
                 "SUCCESS", "参与者 " + participantName + " 以 " + role + " 角色加入");
    }
    
    public void logParticipantLeave(String sceneGroupId, String participantId, String participantName) {
        logEvent(sceneGroupId, "PARTICIPANT_LEAVE", "离开参与者",
                 participantId, participantName,
                 "SUCCESS", "参与者 " + participantName + " 离开场景组");
    }
    
    public void logCapabilityBind(String sceneGroupId, String capId, String capName, String providerId) {
        logEvent(sceneGroupId, "CAPABILITY_BIND", "绑定能力",
                 providerId, providerId,
                 "SUCCESS", "绑定能力: " + capName + " (" + capId + ")");
    }
    
    public void logCapabilityUnbind(String sceneGroupId, String capId, String capName) {
        logEvent(sceneGroupId, "CAPABILITY_UNBIND", "解绑能力",
                 null, null,
                 "SUCCESS", "解绑能力: " + capName + " (" + capId + ")");
    }
    
    public void logStatusChange(String sceneGroupId, String oldStatus, String newStatus) {
        logEvent(sceneGroupId, "STATUS_CHANGE", "状态变更",
                 null, null,
                 "SUCCESS", "状态从 " + oldStatus + " 变更为 " + newStatus);
    }
    
    public List<SceneGroupEventLogDTO> getEventLogs(String sceneGroupId, int limit) {
        List<SceneGroupEventLogDTO> logs = eventLogs.get(sceneGroupId);
        if (logs == null || logs.isEmpty()) {
            return new ArrayList<>();
        }
        
        return logs.stream()
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    public void clearLogs(String sceneGroupId) {
        eventLogs.remove(sceneGroupId);
    }
}

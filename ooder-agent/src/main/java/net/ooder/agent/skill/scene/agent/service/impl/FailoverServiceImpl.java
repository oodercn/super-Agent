package net.ooder.mvp.skill.scene.agent.service.impl;

import net.ooder.mvp.skill.scene.agent.dto.AgentSessionDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentStatus;
import net.ooder.mvp.skill.scene.agent.dto.FailoverRecordDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentMessageDTO;
import net.ooder.mvp.skill.scene.agent.dto.MessageType;
import net.ooder.mvp.skill.scene.agent.service.AgentSessionService;
import net.ooder.mvp.skill.scene.agent.service.AgentMessageService;
import net.ooder.mvp.skill.scene.agent.service.FailoverService;
import net.ooder.mvp.skill.scene.event.SceneStateEvent;
import net.ooder.mvp.skill.scene.event.SceneStateEventPublisher;
import net.ooder.skill.common.storage.JsonStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FailoverServiceImpl implements FailoverService {
    
    private static final Logger log = LoggerFactory.getLogger(FailoverServiceImpl.class);
    
    private static final String STORAGE_KEY_FAILOVER = "failover-records";
    
    @Autowired
    private JsonStorageService storage;
    
    @Autowired
    private AgentSessionService sessionService;
    
    @Autowired(required = false)
    private AgentMessageService messageService;
    
    @Autowired(required = false)
    private SceneStateEventPublisher eventPublisher;
    
    private final Map<String, FailoverRecordDTO> records = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        loadFromStorage();
        log.info("[FailoverService] Initialized with {} records", records.size());
    }
    
    private void loadFromStorage() {
        try {
            Map<String, FailoverRecordDTO> storedRecords = storage.getAll(STORAGE_KEY_FAILOVER);
            if (storedRecords != null) {
                records.putAll(storedRecords);
            }
        } catch (Exception e) {
            log.warn("[FailoverService] Failed to load records: {}", e.getMessage());
        }
    }
    
    @Override
    public void detectFailure(String agentId) {
        log.info("[detectFailure] Detecting failure for agent: {}", agentId);
        
        AgentSessionDTO session = sessionService.getSession(agentId);
        if (session == null) {
            log.warn("[detectFailure] Agent {} not found", agentId);
            return;
        }
        
        FailoverRecordDTO record = new FailoverRecordDTO();
        record.setRecordId("fo-" + UUID.randomUUID().toString().substring(0, 12));
        record.setFailedAgentId(agentId);
        record.setFailedAgentName(session.getAgentName());
        record.setSceneGroupId(session.getSceneGroupId());
        record.setStatus("DETECTED");
        record.setCreateTime(System.currentTimeMillis());
        record.setFailureReason("Heartbeat timeout");
        
        records.put(record.getRecordId(), record);
        persistRecord(record);
        
        log.info("[detectFailure] Failure record created: {}", record.getRecordId());
        
        reassignTasksAuto(agentId);
    }
    
    @Override
    public void reassignTasks(String failedAgentId, String targetAgentId) {
        log.info("[reassignTasks] Reassigning tasks from {} to {}", failedAgentId, targetAgentId);
        
        AgentSessionDTO failedSession = sessionService.getSession(failedAgentId);
        AgentSessionDTO targetSession = sessionService.getSession(targetAgentId);
        
        if (failedSession == null || targetSession == null) {
            log.warn("[reassignTasks] Invalid agent IDs");
            return;
        }
        
        if (messageService != null) {
            AgentMessageDTO message = new AgentMessageDTO();
            message.setFromAgent("system-failover");
            message.setToAgent(targetAgentId);
            message.setSceneGroupId(failedSession.getSceneGroupId());
            message.setType(MessageType.TASK_DELEGATE.name());
            message.setTitle("故障转移任务");
            message.setContent("Agent " + failedSession.getAgentName() + " 故障，任务已转移");
            message.setPriority(10);
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("failedAgentId", failedAgentId);
            payload.put("failedAgentName", failedSession.getAgentName());
            payload.put("failoverTime", System.currentTimeMillis());
            message.setPayload(payload);
            
            messageService.sendMessage(message);
        }
        
        FailoverRecordDTO record = findPendingRecord(failedAgentId);
        if (record != null) {
            record.setTargetAgentId(targetAgentId);
            record.setTargetAgentName(targetSession.getAgentName());
            record.setStatus("REASSIGNED");
            persistRecord(record);
        }
        
        if (eventPublisher != null) {
            SceneStateEvent event = SceneStateEvent.create(
                failedSession.getSceneGroupId(),
                SceneStateEvent.EVENT_PARTICIPANT_LEFT
            );
            event.setParticipantId(failedAgentId);
            event.setParticipantName(failedSession.getAgentName());
            eventPublisher.publishParticipantLeft(event);
        }
        
        log.info("[reassignTasks] Tasks reassigned successfully");
    }
    
    @Override
    public void reassignTasksAuto(String failedAgentId) {
        log.info("[reassignTasksAuto] Auto-reassigning tasks for failed agent: {}", failedAgentId);
        
        AgentSessionDTO failedSession = sessionService.getSession(failedAgentId);
        if (failedSession == null) {
            log.warn("[reassignTasksAuto] Failed agent not found");
            return;
        }
        
        List<String> availableAgents = getAvailableAgentsForFailover(failedSession.getSceneGroupId());
        
        if (availableAgents.isEmpty()) {
            log.warn("[reassignTasksAuto] No available agents for failover");
            notifyNoAvailableAgent(failedAgentId, failedSession.getSceneGroupId());
            return;
        }
        
        String targetAgentId = selectBestTarget(availableAgents);
        reassignTasks(failedAgentId, targetAgentId);
    }
    
    private String selectBestTarget(List<String> availableAgents) {
        if (availableAgents.size() == 1) {
            return availableAgents.get(0);
        }
        
        String bestAgent = null;
        int minTasks = Integer.MAX_VALUE;
        
        for (String agentId : availableAgents) {
            AgentSessionDTO session = sessionService.getSession(agentId);
            if (session != null && AgentStatus.IDLE.name().equals(session.getStatus())) {
                return agentId;
            }
            
            int pendingCount = 0;
            if (messageService != null) {
                pendingCount = messageService.getPendingCount(agentId);
            }
            
            if (pendingCount < minTasks) {
                minTasks = pendingCount;
                bestAgent = agentId;
            }
        }
        
        return bestAgent != null ? bestAgent : availableAgents.get(0);
    }
    
    private void notifyNoAvailableAgent(String failedAgentId, String sceneGroupId) {
        if (messageService != null) {
            AgentMessageDTO message = new AgentMessageDTO();
            message.setFromAgent("system-failover");
            message.setToAgent("admin");
            message.setSceneGroupId(sceneGroupId);
            message.setType(MessageType.STATUS_UPDATE.name());
            message.setTitle("故障转移失败");
            message.setContent("Agent " + failedAgentId + " 故障，但无可用 Agent 接管任务");
            message.setPriority(10);
            messageService.sendMessage(message);
        }
    }
    
    @Override
    public void notifyRecovery(String agentId) {
        log.info("[notifyRecovery] Agent {} recovered", agentId);
        
        AgentSessionDTO session = sessionService.getSession(agentId);
        if (session == null) {
            return;
        }
        
        FailoverRecordDTO record = findPendingRecord(agentId);
        if (record != null) {
            record.setStatus("RECOVERED");
            record.setRecoveryTime(System.currentTimeMillis());
            persistRecord(record);
        }
        
        if (eventPublisher != null) {
            SceneStateEvent event = SceneStateEvent.create(
                session.getSceneGroupId(),
                SceneStateEvent.EVENT_PARTICIPANT_JOINED
            );
            event.setParticipantId(agentId);
            event.setParticipantName(session.getAgentName());
            eventPublisher.publishParticipantJoined(event);
        }
    }
    
    @Override
    public List<FailoverRecordDTO> getFailoverHistory(String sceneGroupId) {
        return records.values().stream()
            .filter(r -> sceneGroupId.equals(r.getSceneGroupId()))
            .sorted((a, b) -> Long.compare(b.getCreateTime(), a.getCreateTime()))
            .collect(Collectors.toList());
    }
    
    @Override
    public FailoverRecordDTO getFailoverRecord(String recordId) {
        return records.get(recordId);
    }
    
    @Override
    public Map<String, Object> getFailoverStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long total = records.size();
        long detected = records.values().stream()
            .filter(r -> "DETECTED".equals(r.getStatus()))
            .count();
        long reassigned = records.values().stream()
            .filter(r -> "REASSIGNED".equals(r.getStatus()))
            .count();
        long recovered = records.values().stream()
            .filter(r -> "RECOVERED".equals(r.getStatus()))
            .count();
        
        stats.put("total", total);
        stats.put("detected", detected);
        stats.put("reassigned", reassigned);
        stats.put("recovered", recovered);
        
        return stats;
    }
    
    @Override
    public List<String> getAvailableAgentsForFailover(String sceneGroupId) {
        List<AgentSessionDTO> sessions = sessionService.getSessionsByScene(sceneGroupId);
        
        return sessions.stream()
            .filter(s -> AgentStatus.ONLINE.name().equals(s.getStatus()) || 
                        AgentStatus.IDLE.name().equals(s.getStatus()))
            .map(AgentSessionDTO::getAgentId)
            .collect(Collectors.toList());
    }
    
    private FailoverRecordDTO findPendingRecord(String failedAgentId) {
        return records.values().stream()
            .filter(r -> failedAgentId.equals(r.getFailedAgentId()) && 
                        ("DETECTED".equals(r.getStatus()) || "REASSIGNED".equals(r.getStatus())))
            .findFirst()
            .orElse(null);
    }
    
    private void persistRecord(FailoverRecordDTO record) {
        try {
            storage.put(STORAGE_KEY_FAILOVER, record.getRecordId(), record);
        } catch (Exception e) {
            log.error("[persistRecord] Failed to persist record: {}", e.getMessage());
        }
    }
}

package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.*;

import java.util.List;
import java.util.Map;

public interface SceneGroupService {
    
    SceneGroupDTO create(String templateId, SceneGroupConfigDTO config);
    
    SceneGroupDTO update(String sceneGroupId, SceneGroupConfigDTO config);
    
    boolean destroy(String sceneGroupId);
    
    SceneGroupDTO get(String sceneGroupId);
    
    PageResult<SceneGroupDTO> listAll(int pageNum, int pageSize);
    
    PageResult<SceneGroupDTO> listByTemplate(String templateId, int pageNum, int pageSize);
    
    PageResult<SceneGroupDTO> listByCreator(String creatorId, int pageNum, int pageSize);
    
    PageResult<SceneGroupDTO> listByParticipant(String participantId, int pageNum, int pageSize);
    
    boolean activate(String sceneGroupId);
    
    boolean deactivate(String sceneGroupId);
    
    boolean join(String sceneGroupId, SceneParticipantDTO participant);
    
    boolean leave(String sceneGroupId, String participantId);
    
    boolean changeRole(String sceneGroupId, String participantId, String newRole);
    
    PageResult<SceneParticipantDTO> listParticipants(String sceneGroupId, int pageNum, int pageSize);
    
    SceneParticipantDTO getParticipant(String sceneGroupId, String participantId);
    
    boolean bindCapability(String sceneGroupId, CapabilityBindingDTO binding);
    
    boolean updateCapabilityBinding(String sceneGroupId, String bindingId, CapabilityBindingDTO binding);
    
    boolean unbindCapability(String sceneGroupId, String bindingId);
    
    PageResult<CapabilityBindingDTO> listCapabilityBindings(String sceneGroupId, int pageNum, int pageSize);
    
    SceneSnapshotDTO createSnapshot(String sceneGroupId);
    
    List<SceneSnapshotDTO> listSnapshots(String sceneGroupId);
    
    boolean restoreSnapshot(String sceneGroupId, SceneSnapshotDTO snapshot);
    
    boolean deleteSnapshot(String sceneGroupId, String snapshotId);
    
    FailoverStatusDTO getFailoverStatus(String sceneGroupId);
    
    boolean handleFailover(String sceneGroupId, String failedParticipantId);
    
    boolean bindKnowledgeBase(String sceneGroupId, KnowledgeBindingDTO binding);
    
    boolean unbindKnowledgeBase(String sceneGroupId, String kbId);
    
    List<KnowledgeBindingDTO> listKnowledgeBindings(String sceneGroupId);
    
    boolean updateKnowledgeConfig(String sceneGroupId, Map<String, Object> config);
    
    Map<String, Object> getLlmConfig(String sceneGroupId);
    
    boolean updateLlmConfig(String sceneGroupId, Map<String, Object> config);
    
    List<SceneGroupEventLogDTO> getEventLog(String sceneGroupId, int limit);
}

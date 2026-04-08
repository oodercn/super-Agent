package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.SceneDefinitionDTO;
import net.ooder.mvp.skill.scene.dto.SceneStateDTO;
import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDTO;
import net.ooder.mvp.skill.scene.dto.scene.SceneSnapshotDTO;
import net.ooder.mvp.skill.scene.controller.SceneController.LogDTO;

public interface SceneService {
    
    SceneDefinitionDTO create(SceneDefinitionDTO definition);
    
    boolean delete(String sceneId);
    
    SceneDefinitionDTO get(String sceneId);
    
    PageResult<SceneDefinitionDTO> listAll(int pageNum, int pageSize);
    
    boolean activate(String sceneId);
    
    boolean deactivate(String sceneId);
    
    SceneStateDTO getState(String sceneId);
    
    boolean addCapability(String sceneId, CapabilityDTO capability);
    
    boolean removeCapability(String sceneId, String capId);
    
    PageResult<CapabilityDTO> listCapabilities(String sceneId, int pageNum, int pageSize);
    
    CapabilityDTO getCapability(String sceneId, String capId);
    
    boolean addCollaborativeScene(String sceneId, String collaborativeSceneId);
    
    boolean removeCollaborativeScene(String sceneId, String collaborativeSceneId);
    
    PageResult<String> listCollaborativeScenes(String sceneId, int pageNum, int pageSize);
    
    SceneSnapshotDTO createSnapshot(String sceneId);
    
    boolean restoreSnapshot(String sceneId, SceneSnapshotDTO snapshot);
    
    PageResult<SceneSnapshotDTO> listSnapshots(String sceneId, int pageNum, int pageSize);
    
    PageResult<LogDTO> getLogs(String sceneId, String level, Long startTime, Long endTime, int pageNum, int pageSize);
}

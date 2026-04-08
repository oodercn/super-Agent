package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.SceneDefinitionDTO;
import net.ooder.mvp.skill.scene.dto.SceneStateDTO;
import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDTO;
import net.ooder.mvp.skill.scene.dto.scene.SceneSnapshotDTO;
import net.ooder.mvp.skill.scene.service.SceneService;
import net.ooder.mvp.skill.scene.controller.SceneController.LogDTO;
import net.ooder.scene.core.service.UnifiedSceneService;
import net.ooder.scene.core.service.UnifiedSceneService.SkillDetail;
import net.ooder.scene.core.service.UnifiedSceneService.SceneInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SceneServiceImpl implements SceneService {
    
    private static final Logger log = LoggerFactory.getLogger(SceneServiceImpl.class);
    
    private static final String DEFAULT_SESSION_ID = "default";
    
    @Autowired(required = false)
    private UnifiedSceneService unifiedSceneService;
    
    private final Map<String, SceneDefinitionDTO> localScenes = new ConcurrentHashMap<>();
    private final Map<String, SceneStateDTO> sceneStates = new ConcurrentHashMap<>();
    private final Map<String, List<CapabilityDTO>> sceneCapabilities = new ConcurrentHashMap<>();
    private final Map<String, List<String>> collaborativeScenes = new ConcurrentHashMap<>();
    private final Map<String, List<SceneSnapshotDTO>> sceneSnapshots = new ConcurrentHashMap<>();
    private final Map<String, List<LogDTO>> sceneLogs = new ConcurrentHashMap<>();

    @Override
    public SceneDefinitionDTO create(SceneDefinitionDTO definition) {
        log.info("[create] Creating scene: {}", definition.getName());
        String sceneId = definition.getSceneId() != null ? definition.getSceneId() : "scene-" + System.currentTimeMillis();
        definition.setSceneId(sceneId);
        
        if (unifiedSceneService != null) {
            try {
                log.info("[create] Using UnifiedSceneService to create scene");
                unifiedSceneService.installSkill(DEFAULT_SESSION_ID, sceneId, null);
            } catch (Exception e) {
                log.warn("[create] UnifiedSceneService create failed, fallback to local: {}", e.getMessage());
                localScenes.put(sceneId, definition);
            }
        } else {
            localScenes.put(sceneId, definition);
        }
        
        SceneStateDTO state = new SceneStateDTO();
        state.setSceneId(sceneId);
        state.setStatus("CREATED");
        sceneStates.put(sceneId, state);
        
        return definition;
    }

    @Override
    public boolean delete(String sceneId) {
        log.info("[delete] Deleting scene: {}", sceneId);
        
        if (unifiedSceneService != null) {
            try {
                log.info("[delete] Using UnifiedSceneService to delete scene");
                unifiedSceneService.uninstallSkill(DEFAULT_SESSION_ID, sceneId, true);
            } catch (Exception e) {
                log.warn("[delete] UnifiedSceneService delete failed: {}", e.getMessage());
            }
        }
        
        localScenes.remove(sceneId);
        sceneStates.remove(sceneId);
        sceneCapabilities.remove(sceneId);
        collaborativeScenes.remove(sceneId);
        sceneSnapshots.remove(sceneId);
        sceneLogs.remove(sceneId);
        return true;
    }

    @Override
    public SceneDefinitionDTO get(String sceneId) {
        if (unifiedSceneService != null) {
            try {
                SkillDetail detail = unifiedSceneService.getSkillDetail(DEFAULT_SESSION_ID, sceneId);
                if (detail != null) {
                    return convertToSceneDefinitionDTO(detail);
                }
            } catch (Exception e) {
                log.debug("[get] UnifiedSceneService get failed: {}", e.getMessage());
            }
        }
        return localScenes.get(sceneId);
    }

    @Override
    public PageResult<SceneDefinitionDTO> listAll(int pageNum, int pageSize) {
        List<SceneDefinitionDTO> allScenes = new ArrayList<>();
        
        if (unifiedSceneService != null) {
            try {
                log.info("[listAll] Using UnifiedSceneService to list scenes");
                List<SceneInfo> sceneInfos = unifiedSceneService.getScenes(DEFAULT_SESSION_ID);
                if (sceneInfos != null) {
                    for (SceneInfo info : sceneInfos) {
                        allScenes.add(convertSceneInfoToDTO(info));
                    }
                }
            } catch (Exception e) {
                log.warn("[listAll] UnifiedSceneService list failed: {}", e.getMessage());
            }
        }
        
        allScenes.addAll(localScenes.values());
        
        int total = allScenes.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<SceneDefinitionDTO> pagedList = fromIndex < total ? 
            allScenes.subList(fromIndex, toIndex) : new ArrayList<>();
        
        PageResult<SceneDefinitionDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }

    @Override
    public boolean activate(String sceneId) {
        log.info("[activate] Activating scene: {}", sceneId);
        
        if (unifiedSceneService != null) {
            try {
                log.info("[activate] Using UnifiedSceneService to activate scene");
                return unifiedSceneService.activateScene(DEFAULT_SESSION_ID, sceneId);
            } catch (Exception e) {
                log.warn("[activate] UnifiedSceneService activate failed: {}", e.getMessage());
            }
        }
        
        SceneStateDTO state = sceneStates.get(sceneId);
        if (state != null) {
            state.setStatus("ACTIVE");
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivate(String sceneId) {
        log.info("[deactivate] Deactivating scene: {}", sceneId);
        
        if (unifiedSceneService != null) {
            try {
                log.info("[deactivate] Using UnifiedSceneService to deactivate scene");
                return unifiedSceneService.deactivateScene(DEFAULT_SESSION_ID, sceneId);
            } catch (Exception e) {
                log.warn("[deactivate] UnifiedSceneService deactivate failed: {}", e.getMessage());
            }
        }
        
        SceneStateDTO state = sceneStates.get(sceneId);
        if (state != null) {
            state.setStatus("INACTIVE");
            return true;
        }
        return false;
    }

    @Override
    public SceneStateDTO getState(String sceneId) {
        return sceneStates.get(sceneId);
    }

    @Override
    public boolean addCapability(String sceneId, CapabilityDTO capability) {
        log.info("[addCapability] Adding capability to scene: {}", sceneId);
        List<CapabilityDTO> caps = sceneCapabilities.computeIfAbsent(sceneId, k -> new ArrayList<>());
        caps.add(capability);
        return true;
    }

    @Override
    public boolean removeCapability(String sceneId, String capId) {
        log.info("[removeCapability] Removing capability from scene: {}", capId);
        List<CapabilityDTO> caps = sceneCapabilities.get(sceneId);
        if (caps != null) {
            caps.removeIf(c -> capId.equals(c.getCapId()));
            return true;
        }
        return false;
    }

    @Override
    public PageResult<CapabilityDTO> listCapabilities(String sceneId, int pageNum, int pageSize) {
        List<CapabilityDTO> allCaps = sceneCapabilities.getOrDefault(sceneId, new ArrayList<>());
        int total = allCaps.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<CapabilityDTO> pagedList = fromIndex < total ? 
            allCaps.subList(fromIndex, toIndex) : new ArrayList<>();
        
        PageResult<CapabilityDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }

    @Override
    public CapabilityDTO getCapability(String sceneId, String capId) {
        List<CapabilityDTO> caps = sceneCapabilities.get(sceneId);
        if (caps != null) {
            for (CapabilityDTO cap : caps) {
                if (capId.equals(cap.getCapId())) {
                    return cap;
                }
            }
        }
        return null;
    }

    @Override
    public boolean addCollaborativeScene(String sceneId, String collaborativeSceneId) {
        log.info("[addCollaborativeScene] Adding collaborative scene: {} -> {}", sceneId, collaborativeSceneId);
        List<String> collabs = collaborativeScenes.computeIfAbsent(sceneId, k -> new ArrayList<>());
        if (!collabs.contains(collaborativeSceneId)) {
            collabs.add(collaborativeSceneId);
        }
        return true;
    }

    @Override
    public boolean removeCollaborativeScene(String sceneId, String collaborativeSceneId) {
        log.info("[removeCollaborativeScene] Removing collaborative scene: {} -> {}", sceneId, collaborativeSceneId);
        List<String> collabs = collaborativeScenes.get(sceneId);
        if (collabs != null) {
            return collabs.remove(collaborativeSceneId);
        }
        return false;
    }

    @Override
    public PageResult<String> listCollaborativeScenes(String sceneId, int pageNum, int pageSize) {
        List<String> allCollabs = collaborativeScenes.getOrDefault(sceneId, new ArrayList<>());
        int total = allCollabs.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<String> pagedList = fromIndex < total ? 
            allCollabs.subList(fromIndex, toIndex) : new ArrayList<>();
        
        PageResult<String> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }

    @Override
    public SceneSnapshotDTO createSnapshot(String sceneId) {
        log.info("[createSnapshot] Creating snapshot for scene: {}", sceneId);
        SceneSnapshotDTO snapshot = new SceneSnapshotDTO();
        snapshot.setSnapshotId("snap-" + System.currentTimeMillis());
        snapshot.setSceneGroupId(sceneId);
        snapshot.setCreateTime(System.currentTimeMillis());
        
        List<SceneSnapshotDTO> snapshots = sceneSnapshots.computeIfAbsent(sceneId, k -> new ArrayList<>());
        snapshots.add(snapshot);
        
        return snapshot;
    }

    @Override
    public boolean restoreSnapshot(String sceneId, SceneSnapshotDTO snapshot) {
        log.info("[restoreSnapshot] Restoring snapshot for scene: {}", sceneId);
        return true;
    }

    @Override
    public PageResult<SceneSnapshotDTO> listSnapshots(String sceneId, int pageNum, int pageSize) {
        List<SceneSnapshotDTO> allSnapshots = sceneSnapshots.getOrDefault(sceneId, new ArrayList<>());
        int total = allSnapshots.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<SceneSnapshotDTO> pagedList = fromIndex < total ? 
            allSnapshots.subList(fromIndex, toIndex) : new ArrayList<>();
        
        PageResult<SceneSnapshotDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }

    @Override
    public PageResult<LogDTO> getLogs(String sceneId, String level, Long startTime, Long endTime, int pageNum, int pageSize) {
        List<LogDTO> allLogs = sceneLogs.getOrDefault(sceneId, new ArrayList<>());
        int total = allLogs.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<LogDTO> pagedList = fromIndex < total ? 
            allLogs.subList(fromIndex, toIndex) : new ArrayList<>();
        
        PageResult<LogDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }
    
    private SceneDefinitionDTO convertToSceneDefinitionDTO(SkillDetail detail) {
        SceneDefinitionDTO dto = new SceneDefinitionDTO();
        dto.setSceneId(detail.getSkillId());
        dto.setName(detail.getName());
        dto.setDescription(detail.getDescription());
        dto.setVersion(detail.getVersion());
        return dto;
    }
    
    private SceneDefinitionDTO convertSceneInfoToDTO(SceneInfo info) {
        SceneDefinitionDTO dto = new SceneDefinitionDTO();
        dto.setSceneId(info.getSceneId());
        dto.setName(info.getName());
        dto.setDescription(info.getDescription());
        return dto;
    }
}

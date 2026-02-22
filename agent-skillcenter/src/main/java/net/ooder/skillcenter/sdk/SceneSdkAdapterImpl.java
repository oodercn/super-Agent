package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.config.SdkConfig;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Primary
public class SceneSdkAdapterImpl implements SceneSdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(SceneSdkAdapterImpl.class);

    @Autowired
    private SdkConfig sdkConfig;

    @Autowired
    private SceneEngineAdapter sceneEngineAdapter;

    @PostConstruct
    public void init() {
        log.info("[SceneSdkAdapter] Initialized with SceneEngineAdapter");
    }

    @Override
    public boolean isAvailable() {
        return sceneEngineAdapter.isAvailable();
    }

    @Override
    public SceneDefinitionDTO createScene(SceneDefinitionDTO definition) {
        SceneInfoDTO info = new SceneInfoDTO();
        info.setName(definition.getName());
        info.setDescription(definition.getDescription());
        SceneInfoDTO created = sceneEngineAdapter.createScene(info);
        return convertToDefinitionDTO(created);
    }

    @Override
    public boolean deleteScene(String sceneId) {
        log.debug("[SceneSdkAdapter] Deleting scene: {}", sceneId);
        return true;
    }

    @Override
    public SceneDefinitionDTO getScene(String sceneId) {
        SceneInfoDTO info = sceneEngineAdapter.getScene(sceneId);
        return info != null ? convertToDefinitionDTO(info) : null;
    }

    @Override
    public PageResult<SceneDefinitionDTO> listScenes(int pageNum, int pageSize) {
        PageResult<SceneInfoDTO> result = sceneEngineAdapter.listScenesPaged(pageNum, pageSize);
        List<SceneDefinitionDTO> list = new ArrayList<>();
        for (SceneInfoDTO s : result.getList()) {
            list.add(convertToDefinitionDTO(s));
        }
        return new PageResult<>(list, result.getTotal(), result.getPageNum(), result.getPageSize());
    }

    @Override
    public boolean activateScene(String sceneId) {
        return sceneEngineAdapter.activateScene(sceneId);
    }

    @Override
    public boolean deactivateScene(String sceneId) {
        return sceneEngineAdapter.deactivateScene(sceneId);
    }

    @Override
    public SceneStateDTO getSceneState(String sceneId) {
        SceneStateDTO dto = new SceneStateDTO();
        dto.setSceneId(sceneId);
        dto.setStatus("active");
        dto.setActiveCapabilities(new ArrayList<>());
        dto.setActiveMembers(new ArrayList<>());
        return dto;
    }

    @Override
    public boolean addCapability(String sceneId, CapabilityDTO capability) {
        CapabilityInfoDTO info = new CapabilityInfoDTO();
        info.setCapId(capability.getCapId());
        info.setName(capability.getName());
        info.setDescription(capability.getDescription());
        return sceneEngineAdapter.addCapability(sceneId, info);
    }

    @Override
    public boolean removeCapability(String sceneId, String capabilityId) {
        return sceneEngineAdapter.removeCapability(sceneId, capabilityId);
    }

    @Override
    public PageResult<CapabilityDTO> listCapabilities(String sceneId, int pageNum, int pageSize) {
        PageResult<CapabilityInfoDTO> result = sceneEngineAdapter.listCapabilitiesPaged(sceneId, pageNum, pageSize);
        List<CapabilityDTO> list = new ArrayList<>();
        for (CapabilityInfoDTO c : result.getList()) {
            list.add(convertCapToDTO(c));
        }
        return new PageResult<>(list, result.getTotal(), result.getPageNum(), result.getPageSize());
    }

    @Override
    public CapabilityDTO getCapability(String sceneId, String capabilityId) {
        CapabilityInfoDTO info = sceneEngineAdapter.getCapability(sceneId, capabilityId);
        return info != null ? convertCapToDTO(info) : null;
    }

    @Override
    public boolean addRole(String sceneId, SceneRoleDTO role) {
        log.debug("[SceneSdkAdapter] Adding role to scene: {}", sceneId);
        return true;
    }

    @Override
    public boolean removeRole(String sceneId, String roleId) {
        log.debug("[SceneSdkAdapter] Removing role from scene: {}", sceneId);
        return true;
    }

    @Override
    public PageResult<SceneRoleDTO> listRoles(String sceneId, int pageNum, int pageSize) {
        return new PageResult<>(new ArrayList<>(), 0, pageNum, pageSize);
    }

    @Override
    public SceneSnapshotDTO createSnapshot(String sceneId) {
        SceneSnapshotDTO dto = new SceneSnapshotDTO();
        dto.setSnapshotId("snapshot-" + UUID.randomUUID().toString().substring(0, 8));
        dto.setSceneId(sceneId);
        dto.setCreatedAt(System.currentTimeMillis());
        return dto;
    }

    @Override
    public boolean restoreSnapshot(String sceneId, String snapshotId) {
        log.debug("[SceneSdkAdapter] Restoring snapshot: {} for scene: {}", snapshotId, sceneId);
        return true;
    }

    @Override
    public PageResult<SceneSnapshotDTO> listSnapshots(String sceneId, int pageNum, int pageSize) {
        return new PageResult<>(new ArrayList<>(), 0, pageNum, pageSize);
    }

    @Override
    public boolean deleteSnapshot(String sceneId, String snapshotId) {
        log.debug("[SceneSdkAdapter] Deleting snapshot: {} for scene: {}", snapshotId, sceneId);
        return true;
    }

    private SceneDefinitionDTO convertToDefinitionDTO(SceneInfoDTO info) {
        SceneDefinitionDTO dto = new SceneDefinitionDTO();
        dto.setSceneId(info.getSceneId());
        dto.setName(info.getName());
        dto.setDescription(info.getDescription());
        if (info.getStatus() != null) {
            dto.setStatus(info.getStatus());
        }
        if (info.getCreatedAt() != null) {
            dto.setCreateTime(info.getCreatedAt());
        }
        return dto;
    }

    private CapabilityDTO convertCapToDTO(CapabilityInfoDTO info) {
        CapabilityDTO dto = new CapabilityDTO();
        dto.setCapId(info.getCapId());
        dto.setName(info.getName());
        dto.setDescription(info.getDescription());
        dto.setType(info.getType());
        return dto;
    }
}

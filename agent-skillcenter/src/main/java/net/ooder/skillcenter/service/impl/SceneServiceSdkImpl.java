package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;
import net.ooder.skillcenter.sdk.SceneSdkAdapter;
import net.ooder.skillcenter.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class SceneServiceSdkImpl implements SceneService {

    @Autowired
    private SceneSdkAdapter sceneSdkAdapter;

    @Override
    public SceneDefinitionDTO create(SceneDefinitionDTO definition) {
        return sceneSdkAdapter.createScene(definition);
    }

    @Override
    public boolean delete(String sceneId) {
        return sceneSdkAdapter.deleteScene(sceneId);
    }

    @Override
    public SceneDefinitionDTO get(String sceneId) {
        return sceneSdkAdapter.getScene(sceneId);
    }

    @Override
    public PageResult<SceneDefinitionDTO> listAll(int pageNum, int pageSize) {
        return sceneSdkAdapter.listScenes(pageNum, pageSize);
    }

    @Override
    public boolean activate(String sceneId) {
        return sceneSdkAdapter.activateScene(sceneId);
    }

    @Override
    public boolean deactivate(String sceneId) {
        return sceneSdkAdapter.deactivateScene(sceneId);
    }

    @Override
    public SceneStateDTO getState(String sceneId) {
        return sceneSdkAdapter.getSceneState(sceneId);
    }

    @Override
    public boolean addCapability(String sceneId, CapabilityDTO capability) {
        return sceneSdkAdapter.addCapability(sceneId, capability);
    }

    @Override
    public boolean removeCapability(String sceneId, String capId) {
        return sceneSdkAdapter.removeCapability(sceneId, capId);
    }

    @Override
    public PageResult<CapabilityDTO> listCapabilities(String sceneId, int pageNum, int pageSize) {
        return sceneSdkAdapter.listCapabilities(sceneId, pageNum, pageSize);
    }

    @Override
    public CapabilityDTO getCapability(String sceneId, String capId) {
        return sceneSdkAdapter.getCapability(sceneId, capId);
    }

    @Override
    public boolean addCollaborativeScene(String sceneId, String collaborativeSceneId) {
        return true;
    }

    @Override
    public boolean removeCollaborativeScene(String sceneId, String collaborativeSceneId) {
        return true;
    }

    @Override
    public PageResult<String> listCollaborativeScenes(String sceneId, int pageNum, int pageSize) {
        return new PageResult<>(new ArrayList<>(), 0, pageNum, pageSize);
    }

    @Override
    public SceneSnapshotDTO createSnapshot(String sceneId) {
        return sceneSdkAdapter.createSnapshot(sceneId);
    }

    @Override
    public boolean restoreSnapshot(String sceneId, SceneSnapshotDTO snapshot) {
        return sceneSdkAdapter.restoreSnapshot(sceneId, snapshot.getSnapshotId());
    }
}

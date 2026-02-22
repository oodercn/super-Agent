package net.ooder.nexus.service.scene;

import net.ooder.nexus.domain.scene.model.SceneDefinition;
import net.ooder.nexus.domain.scene.model.SceneInstance;

import java.util.List;
import java.util.Map;

public interface SceneDefinitionService {

    SceneDefinition createSceneDefinition(SceneDefinition definition);

    SceneDefinition getSceneDefinition(String sceneId);

    List<SceneDefinition> listSceneDefinitions(String type, String status, int page, int size);

    SceneDefinition updateSceneDefinition(SceneDefinition definition);

    boolean deleteSceneDefinition(String sceneId, boolean confirm);

    SceneInstance createSceneInstance(String sceneId, String instanceName, Map<String, Object> config);

    SceneInstance getSceneInstance(String instanceId);

    List<SceneInstance> listSceneInstances(String ownerId, int page, int size);

    boolean startSceneInstance(String instanceId);

    boolean stopSceneInstance(String instanceId);

    boolean pauseSceneInstance(String instanceId);

    boolean resumeSceneInstance(String instanceId);

    boolean archiveSceneInstance(String instanceId);

    Map<String, Object> getLocalSceneOverview();

    String generateInviteCode(String instanceId);

    boolean joinSceneInstance(String instanceId, String inviteCode, String memberId);
}

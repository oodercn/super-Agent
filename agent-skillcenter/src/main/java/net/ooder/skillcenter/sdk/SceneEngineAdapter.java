package net.ooder.skillcenter.sdk;

import net.ooder.scene.provider.SecurityProvider;
import net.ooder.scene.provider.NetworkProvider;
import net.ooder.scene.provider.HostingProvider;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;

import java.util.List;

public interface SceneEngineAdapter {

    boolean isAvailable();

    SecurityProvider getSecurityProvider();

    NetworkProvider getNetworkProvider();

    HostingProvider getHostingProvider();

    List<SkillInfoDTO> listInstalledSkills();

    SkillInfoDTO getSkill(String skillId);

    SkillInstallResultDTO installSkill(String skillId);

    SkillUninstallResultDTO uninstallSkill(String skillId);

    SceneInfoDTO createScene(SceneInfoDTO scene);

    SceneInfoDTO getScene(String sceneId);

    List<SceneInfoDTO> listScenes();

    boolean activateScene(String sceneId);

    boolean deactivateScene(String sceneId);

    boolean addCapability(String sceneId, CapabilityInfoDTO capability);

    boolean removeCapability(String sceneId, String capabilityId);

    List<CapabilityInfoDTO> listCapabilities(String sceneId);

    CapabilityInfoDTO getCapability(String sceneId, String capabilityId);

    SceneGroupInfoDTO createSceneGroup(String sceneId, SceneGroupConfigDTO config);

    boolean destroySceneGroup(String sceneGroupId);

    List<SceneGroupInfoDTO> listSceneGroups();

    boolean joinSceneGroup(String sceneGroupId, String agentId, String role);

    boolean leaveSceneGroup(String sceneGroupId, String agentId);

    List<SceneMemberInfoDTO> listMembers(String sceneGroupId);

    SceneMemberInfoDTO getPrimaryMember(String sceneGroupId);

    boolean handleFailover(String sceneGroupId, String failedMemberId);

    PageResult<SceneInfoDTO> listScenesPaged(int pageNum, int pageSize);

    PageResult<SceneGroupInfoDTO> listSceneGroupsPaged(int pageNum, int pageSize);

    PageResult<SceneMemberInfoDTO> listMembersPaged(String sceneGroupId, int pageNum, int pageSize);

    PageResult<CapabilityInfoDTO> listCapabilitiesPaged(String sceneId, int pageNum, int pageSize);
}

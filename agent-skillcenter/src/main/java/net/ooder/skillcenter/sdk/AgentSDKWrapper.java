package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.dto.scene.*;
import net.ooder.skillcenter.config.SdkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AgentSDKWrapper {

    private static final Logger log = LoggerFactory.getLogger(AgentSDKWrapper.class);

    @Autowired
    private SdkConfig sdkConfig;

    @Autowired
    private SceneEngineAdapter sceneEngineAdapter;

    private final Map<String, SkillInfoDTO> skillCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("[AgentSDKWrapper] Initializing with SceneEngineAdapter...");
        log.info("[AgentSDKWrapper] Mode: {}", sdkConfig.getMode());
        log.info("[AgentSDKWrapper] SceneEngineAdapter available: {}", sceneEngineAdapter.isAvailable());

        loadInstalledSkills();
    }

    private void loadInstalledSkills() {
        try {
            List<SkillInfoDTO> skills = sceneEngineAdapter.listInstalledSkills();
            if (skills != null) {
                log.info("[AgentSDKWrapper] Found {} installed skills", skills.size());
                for (SkillInfoDTO skill : skills) {
                    skillCache.put(skill.getSkillId(), skill);
                }
            }
        } catch (Exception e) {
            log.warn("[AgentSDKWrapper] Failed to load installed skills: {}", e.getMessage());
        }
    }

    public boolean isInitialized() {
        return sceneEngineAdapter.isAvailable();
    }

    public SceneEngineAdapter getSceneEngineAdapter() {
        return sceneEngineAdapter;
    }

    public SkillInfoDTO getSkill(String skillId) {
        if (skillCache.containsKey(skillId)) {
            return skillCache.get(skillId);
        }

        SkillInfoDTO skill = sceneEngineAdapter.getSkill(skillId);
        if (skill != null) {
            skillCache.put(skillId, skill);
        }
        return skill;
    }

    public Map<String, SkillInfoDTO> getAllSkills() {
        List<SkillInfoDTO> skills = sceneEngineAdapter.listInstalledSkills();
        Map<String, SkillInfoDTO> result = new HashMap<>();
        for (SkillInfoDTO skill : skills) {
            result.put(skill.getSkillId(), skill);
            skillCache.put(skill.getSkillId(), skill);
        }
        return result;
    }

    public List<SkillInfoDTO> listAllSkills() {
        List<SkillInfoDTO> skills = sceneEngineAdapter.listInstalledSkills();
        for (SkillInfoDTO skill : skills) {
            skillCache.put(skill.getSkillId(), skill);
        }
        return skills;
    }

    public SkillInstallResultDTO installSkill(String skillId) {
        SkillInstallResultDTO result = sceneEngineAdapter.installSkill(skillId);
        if (result.isSuccess()) {
            SkillInfoDTO skill = sceneEngineAdapter.getSkill(skillId);
            if (skill != null) {
                skillCache.put(skillId, skill);
            }
        }
        return result;
    }

    public SkillUninstallResultDTO uninstallSkill(String skillId) {
        SkillUninstallResultDTO result = sceneEngineAdapter.uninstallSkill(skillId);
        if (result.isSuccess()) {
            skillCache.remove(skillId);
        }
        return result;
    }

    public SceneInfoDTO createScene(SceneInfoDTO scene) {
        return sceneEngineAdapter.createScene(scene);
    }

    public SceneInfoDTO getScene(String sceneId) {
        return sceneEngineAdapter.getScene(sceneId);
    }

    public List<SceneInfoDTO> listScenes() {
        return sceneEngineAdapter.listScenes();
    }

    public boolean activateScene(String sceneId) {
        return sceneEngineAdapter.activateScene(sceneId);
    }

    public boolean deactivateScene(String sceneId) {
        return sceneEngineAdapter.deactivateScene(sceneId);
    }

    public SceneGroupInfoDTO createSceneGroup(String sceneId, SceneGroupConfigDTO config) {
        return sceneEngineAdapter.createSceneGroup(sceneId, config);
    }

    public boolean joinSceneGroup(String sceneGroupId, String agentId, String role) {
        return sceneEngineAdapter.joinSceneGroup(sceneGroupId, agentId, role);
    }

    public List<SceneGroupInfoDTO> listSceneGroups() {
        return sceneEngineAdapter.listSceneGroups();
    }

    public List<SceneMemberInfoDTO> listMembers(String sceneGroupId) {
        return sceneEngineAdapter.listMembers(sceneGroupId);
    }
}

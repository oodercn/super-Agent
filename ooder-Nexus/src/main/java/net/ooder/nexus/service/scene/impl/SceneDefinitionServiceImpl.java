package net.ooder.nexus.service.scene.impl;

import net.ooder.nexus.domain.scene.model.*;
import net.ooder.nexus.service.scene.SceneDefinitionService;
import net.ooder.nexus.service.scene.SceneEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SceneDefinitionServiceImpl implements SceneDefinitionService {

    private static final Logger log = LoggerFactory.getLogger(SceneDefinitionServiceImpl.class);

    @Autowired(required = false)
    private SceneEngineService sceneEngineService;

    private final Map<String, SceneDefinition> definitionStore = new HashMap<>();
    private final Map<String, SceneInstance> instanceStore = new HashMap<>();

    @Override
    public SceneDefinition createSceneDefinition(SceneDefinition definition) {
        String sceneId = definition.getSceneId();
        if (sceneId == null || sceneId.isEmpty()) {
            sceneId = "scene-" + UUID.randomUUID().toString().substring(0, 8);
            definition.setSceneId(sceneId);
        }
        
        definition.setStatus("CREATED");
        definition.setCreatedAt(new Date());
        definition.setUpdatedAt(new Date());
        
        definitionStore.put(sceneId, definition);
        log.info("Created scene definition: {}", sceneId);
        
        return definition;
    }

    @Override
    public SceneDefinition getSceneDefinition(String sceneId) {
        return definitionStore.get(sceneId);
    }

    @Override
    public List<SceneDefinition> listSceneDefinitions(String type, String status, int page, int size) {
        List<SceneDefinition> result = new ArrayList<>();
        
        for (SceneDefinition def : definitionStore.values()) {
            boolean match = true;
            if (type != null && !type.isEmpty() && !type.equals(def.getType())) {
                match = false;
            }
            if (status != null && !status.isEmpty() && !status.equals(def.getStatus())) {
                match = false;
            }
            if (match) {
                result.add(def);
            }
        }
        
        result.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        
        int start = page * size;
        int end = Math.min(start + size, result.size());
        
        if (start >= result.size()) {
            return new ArrayList<>();
        }
        
        return result.subList(start, end);
    }

    @Override
    public SceneDefinition updateSceneDefinition(SceneDefinition definition) {
        String sceneId = definition.getSceneId();
        SceneDefinition existing = definitionStore.get(sceneId);
        if (existing == null) {
            return null;
        }
        
        definition.setUpdatedAt(new Date());
        definition.setCreatedAt(existing.getCreatedAt());
        definitionStore.put(sceneId, definition);
        
        log.info("Updated scene definition: {}", sceneId);
        return definition;
    }

    @Override
    public boolean deleteSceneDefinition(String sceneId, boolean confirm) {
        if (!confirm) {
            return false;
        }
        
        SceneDefinition removed = definitionStore.remove(sceneId);
        if (removed != null) {
            log.info("Deleted scene definition: {}", sceneId);
            return true;
        }
        return false;
    }

    @Override
    public SceneInstance createSceneInstance(String sceneId, String instanceName, Map<String, Object> config) {
        SceneDefinition definition = definitionStore.get(sceneId);
        if (definition == null) {
            return null;
        }
        
        String instanceId = "inst-" + UUID.randomUUID().toString().substring(0, 8);
        
        SceneInstance instance = new SceneInstance();
        instance.setInstanceId(instanceId);
        instance.setSceneId(sceneId);
        instance.setSceneName(definition.getName());
        instance.setInstanceName(instanceName);
        instance.setDescription(definition.getDescription());
        instance.setStatus("CREATED");
        instance.setCreatedAt(new Date());
        instance.setUpdatedAt(new Date());
        
        SceneConfig sceneConfig = new SceneConfig();
        if (config != null) {
            if (config.containsKey("maxMembers")) {
                sceneConfig.setMaxMembers(((Number) config.get("maxMembers")).intValue());
            }
            if (config.containsKey("heartbeatInterval")) {
                sceneConfig.setHeartbeatInterval(((Number) config.get("heartbeatInterval")).longValue());
            }
            if (config.containsKey("approvalRequired")) {
                sceneConfig.setApprovalRequired((Boolean) config.get("approvalRequired"));
            }
        }
        instance.setConfig(sceneConfig);
        
        String inviteCode = "GRP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        instance.setInviteCode(inviteCode);
        
        instanceStore.put(instanceId, instance);
        log.info("Created scene instance: {} from scene: {}", instanceId, sceneId);
        
        return instance;
    }

    @Override
    public SceneInstance getSceneInstance(String instanceId) {
        return instanceStore.get(instanceId);
    }

    @Override
    public List<SceneInstance> listSceneInstances(String ownerId, int page, int size) {
        List<SceneInstance> result = new ArrayList<>();
        
        for (SceneInstance inst : instanceStore.values()) {
            if (ownerId == null || ownerId.isEmpty() || ownerId.equals(inst.getOwnerId())) {
                result.add(inst);
            }
        }
        
        result.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        
        int start = page * size;
        int end = Math.min(start + size, result.size());
        
        if (start >= result.size()) {
            return new ArrayList<>();
        }
        
        return result.subList(start, end);
    }

    @Override
    public boolean startSceneInstance(String instanceId) {
        SceneInstance instance = instanceStore.get(instanceId);
        if (instance == null) {
            return false;
        }
        
        instance.setStatus("RUNNING");
        instance.setUpdatedAt(new Date());
        log.info("Started scene instance: {}", instanceId);
        return true;
    }

    @Override
    public boolean stopSceneInstance(String instanceId) {
        SceneInstance instance = instanceStore.get(instanceId);
        if (instance == null) {
            return false;
        }
        
        instance.setStatus("STOPPED");
        instance.setUpdatedAt(new Date());
        log.info("Stopped scene instance: {}", instanceId);
        return true;
    }

    @Override
    public boolean pauseSceneInstance(String instanceId) {
        SceneInstance instance = instanceStore.get(instanceId);
        if (instance == null) {
            return false;
        }
        
        instance.setStatus("PAUSED");
        instance.setUpdatedAt(new Date());
        log.info("Paused scene instance: {}", instanceId);
        return true;
    }

    @Override
    public boolean resumeSceneInstance(String instanceId) {
        SceneInstance instance = instanceStore.get(instanceId);
        if (instance == null) {
            return false;
        }
        
        instance.setStatus("RUNNING");
        instance.setUpdatedAt(new Date());
        log.info("Resumed scene instance: {}", instanceId);
        return true;
    }

    @Override
    public boolean archiveSceneInstance(String instanceId) {
        SceneInstance instance = instanceStore.get(instanceId);
        if (instance == null) {
            return false;
        }
        
        instance.setStatus("ARCHIVED");
        instance.setUpdatedAt(new Date());
        log.info("Archived scene instance: {}", instanceId);
        return true;
    }

    @Override
    public Map<String, Object> getLocalSceneOverview() {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> primaryScenes = new ArrayList<>();
        int primaryTotal = 0;
        int primaryActive = 0;
        
        for (SceneDefinition def : definitionStore.values()) {
            if ("primary".equals(def.getType())) {
                primaryTotal++;
                if ("RUNNING".equals(def.getStatus()) || "CREATED".equals(def.getStatus())) {
                    primaryActive++;
                }
                
                Map<String, Object> sceneInfo = new HashMap<>();
                sceneInfo.put("sceneId", def.getSceneId());
                sceneInfo.put("name", def.getName());
                sceneInfo.put("status", def.getStatus());
                sceneInfo.put("capabilities", def.getCapabilities() != null ? def.getCapabilities().size() : 0);
                sceneInfo.put("icon", getCategoryIcon(def.getCategory()));
                primaryScenes.add(sceneInfo);
            }
        }
        
        Map<String, Object> primaryData = new HashMap<>();
        primaryData.put("total", primaryTotal);
        primaryData.put("active", primaryActive);
        primaryData.put("scenes", primaryScenes);
        result.put("primaryScenes", primaryData);
        
        List<Map<String, Object>> collabScenes = new ArrayList<>();
        int collabTotal = 0;
        int collabActive = 0;
        
        for (SceneInstance inst : instanceStore.values()) {
            SceneDefinition def = definitionStore.get(inst.getSceneId());
            if (def != null && "collaboration".equals(def.getType())) {
                collabTotal++;
                if ("RUNNING".equals(inst.getStatus())) {
                    collabActive++;
                }
                
                Map<String, Object> sceneInfo = new HashMap<>();
                sceneInfo.put("instanceId", inst.getInstanceId());
                sceneInfo.put("name", inst.getInstanceName());
                sceneInfo.put("members", inst.getMembers() != null ? inst.getMembers().size() : 0);
                sceneInfo.put("status", inst.getStatus());
                sceneInfo.put("role", "owner");
                collabScenes.add(sceneInfo);
            }
        }
        
        Map<String, Object> collabData = new HashMap<>();
        collabData.put("total", collabTotal);
        collabData.put("active", collabActive);
        collabData.put("scenes", collabScenes);
        result.put("collaborationScenes", collabData);
        
        List<Map<String, Object>> skillScenes = new ArrayList<>();
        int skillTotal = 0;
        int skillConfigured = 0;
        
        for (SceneDefinition def : definitionStore.values()) {
            if ("skill".equals(def.getType())) {
                skillTotal++;
                boolean configured = "CONFIGURED".equals(def.getStatus()) || "CONNECTED".equals(def.getStatus());
                if (configured) {
                    skillConfigured++;
                }
                
                Map<String, Object> skillInfo = new HashMap<>();
                skillInfo.put("skillId", def.getSceneId());
                skillInfo.put("name", def.getName());
                skillInfo.put("status", def.getStatus());
                skillInfo.put("connected", "CONNECTED".equals(def.getStatus()));
                skillScenes.add(skillInfo);
            }
        }
        
        Map<String, Object> skillData = new HashMap<>();
        skillData.put("total", skillTotal);
        skillData.put("configured", skillConfigured);
        skillData.put("skills", skillScenes);
        result.put("configurableSkills", skillData);
        
        return result;
    }

    @Override
    public String generateInviteCode(String instanceId) {
        SceneInstance instance = instanceStore.get(instanceId);
        if (instance == null) {
            return null;
        }
        
        String newCode = "GRP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        instance.setInviteCode(newCode);
        instance.setUpdatedAt(new Date());
        
        return newCode;
    }

    @Override
    public boolean joinSceneInstance(String instanceId, String inviteCode, String memberId) {
        SceneInstance instance = instanceStore.get(instanceId);
        if (instance == null) {
            return false;
        }
        
        if (!inviteCode.equals(instance.getInviteCode())) {
            return false;
        }
        
        List<SceneMemberInfo> members = instance.getMembers();
        if (members == null) {
            members = new ArrayList<>();
            instance.setMembers(members);
        }
        
        SceneMemberInfo member = new SceneMemberInfo();
        member.setMemberId(memberId);
        member.setRole("member");
        member.setJoinedAt(new Date());
        member.setOnline(true);
        members.add(member);
        
        instance.setUpdatedAt(new Date());
        log.info("Member {} joined scene instance: {}", memberId, instanceId);
        
        return true;
    }

    private String getCategoryIcon(String category) {
        if (category == null) {
            return "ri-apps-line";
        }
        
        Map<String, String> icons = new HashMap<>();
        icons.put("infrastructure", "ri-database-2-line");
        icons.put("security", "ri-shield-line");
        icons.put("communication", "ri-message-3-line");
        icons.put("business", "ri-flow-chart");
        icons.put("development", "ri-code-line");
        icons.put("identity", "ri-user-settings-line");
        
        return icons.getOrDefault(category, "ri-apps-line");
    }
}

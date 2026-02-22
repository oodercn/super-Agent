package net.ooder.nexus.service.skill.impl;

import net.ooder.nexus.domain.skill.model.InstallPreview;
import net.ooder.nexus.domain.skill.model.SkillDependency;
import net.ooder.nexus.service.skill.SkillDependencyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SkillDependencyServiceImpl implements SkillDependencyService {

    private static final Logger log = LoggerFactory.getLogger(SkillDependencyServiceImpl.class);

    private final ConcurrentHashMap<String, SkillDependency> dependencyCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, InstallPreview> previewCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> capInfoStore = new ConcurrentHashMap<>();

    @Override
    public SkillDependency analyzeDependencies(String skillId, String userId) {
        log.info("Analyzing dependencies for skill: {}, user: {}", skillId, userId);
        
        SkillDependency dep = dependencyCache.get(skillId);
        if (dep != null) {
            return dep;
        }
        
        dep = new SkillDependency();
        dep.setSkillId(skillId);
        dep.setSkillName(getSkillName(skillId));
        dep.setSkillType(determineSkillType(skillId));
        dep.setVersion("1.0.0");
        dep.setDescription(getSkillDescription(skillId));
        dep.setSource(getSkillSource(skillId));
        
        if ("enterprise".equals(dep.getSkillType())) {
            analyzeEnterpriseDependencies(skillId, dep);
        } else {
            analyzePersonalDependencies(skillId, dep);
        }
        
        dependencyCache.put(skillId, dep);
        return dep;
    }

    @Override
    public InstallPreview previewInstall(String skillId, String userId, String downloadUrl) {
        log.info("Previewing install for skill: {}, user: {}", skillId, userId);
        
        InstallPreview preview = new InstallPreview();
        preview.setPreviewId(UUID.randomUUID().toString());
        preview.setSkillId(skillId);
        preview.setSkillName(getSkillName(skillId));
        preview.setSkillType(determineSkillType(skillId));
        preview.setVersion("1.0.0");
        preview.setDescription(getSkillDescription(skillId));
        preview.setSource(getSkillSource(skillId));
        preview.setDownloadUrl(downloadUrl);
        
        SkillDependency dep = analyzeDependencies(skillId, userId);
        
        for (SkillDependency.SceneDependency scene : dep.getScenes()) {
            InstallPreview.SceneInfo info = new InstallPreview.SceneInfo();
            info.setSceneId(scene.getSceneId());
            info.setSceneName(scene.getSceneName());
            info.setSceneType(scene.getSceneType());
            info.setRequired(scene.isRequired());
            info.setWillJoin(scene.isRequired());
            preview.getScenes().add(info);
            
            if (scene.getGroupId() != null) {
                InstallPreview.GroupInfo groupInfo = new InstallPreview.GroupInfo();
                groupInfo.setGroupId(scene.getGroupId());
                groupInfo.setGroupName(scene.getGroupName());
                groupInfo.setSceneId(scene.getSceneId());
                groupInfo.setSceneName(scene.getSceneName());
                groupInfo.setGroupAddress(generateGroupAddress(scene.getGroupId()));
                preview.getGroups().add(groupInfo);
            }
        }
        
        for (SkillDependency.SkillRefDependency skill : dep.getSkills()) {
            InstallPreview.SkillDepInfo info = new InstallPreview.SkillDepInfo();
            info.setSkillId(skill.getSkillId());
            info.setSkillName(skill.getSkillName());
            info.setRequired(skill.isRequired());
            info.setInstalled(checkSkillInstalled(skill.getSkillId(), userId));
            preview.getSkillDeps().add(info);
            
            if (skill.isRequired() && !info.isInstalled()) {
                preview.addWarning("需要安装依赖技能: " + skill.getSkillName());
            }
        }
        
        for (SkillDependency.PermissionRequest perm : dep.getPermissions()) {
            InstallPreview.PermissionInfo info = new InstallPreview.PermissionInfo();
            info.setPermissionId(perm.getPermissionId());
            info.setPermissionType(perm.getPermissionType());
            info.setResourceName(perm.getResourceName());
            info.setPermission(perm.getPermission());
            info.setDescription(perm.getDescription());
            info.setRequired(perm.isRequired());
            info.setGranted(false);
            preview.getPermissions().add(info);
        }
        
        previewCache.put(preview.getPreviewId(), preview);
        return preview;
    }

    @Override
    public List<Map<String, Object>> analyzeRequiredPermissions(String skillId) {
        List<Map<String, Object>> permissions = new ArrayList<>();
        
        String skillType = determineSkillType(skillId);
        
        if ("enterprise".equals(skillType)) {
            permissions.add(createPermission("file-read", "file", "文件资源", "read", "读取文件内容", true));
            permissions.add(createPermission("file-write", "file", "文件资源", "write", "写入文件内容", false));
            permissions.add(createPermission("message-send", "message", "消息资源", "send", "发送消息", false));
            permissions.add(createPermission("group-access", "group", "群组资源", "access", "访问群组", false));
        } else {
            permissions.add(createPermission("local-file", "file", "本地文件", "read", "读取本地文件", true));
        }
        
        return permissions;
    }

    @Override
    public List<Map<String, Object>> analyzeSceneDependencies(String skillId) {
        List<Map<String, Object>> scenes = new ArrayList<>();
        
        String skillType = determineSkillType(skillId);
        
        if ("enterprise".equals(skillType)) {
            Map<String, Object> scene = new HashMap<>();
            scene.put("sceneId", "scene-" + skillId);
            scene.put("sceneName", getSkillName(skillId).replace("技能", "协作场景"));
            scene.put("sceneType", "enterprise");
            scene.put("required", true);
            scene.put("groupId", "group-" + skillId);
            scene.put("groupName", getSkillName(skillId).replace("技能", "") + "协作组");
            scenes.add(scene);
        }
        
        return scenes;
    }

    @Override
    public List<Map<String, Object>> analyzeSkillDependencies(String skillId) {
        List<Map<String, Object>> skills = new ArrayList<>();
        
        if (skillId.contains("finance")) {
            Map<String, Object> skill = new HashMap<>();
            skill.put("skillId", "skill-approve");
            skill.put("skillName", "审批助手");
            skill.put("required", false);
            skill.put("reason", "用于审批流程");
            skills.add(skill);
        }
        
        return skills;
    }

    @Override
    public boolean checkSkillInstalled(String skillId, String userId) {
        return false;
    }

    @Override
    public Map<String, Object> getGroupAddress(String sceneId, String userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("sceneId", sceneId);
        result.put("groupAddress", "nexus://group/" + sceneId);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @Override
    public Map<String, Object> writeCapInfo(String skillId, String userId, Map<String, Object> capConfig) {
        log.info("Writing Cap info for skill: {}, user: {}", skillId, userId);
        
        String capId = "cap-" + skillId + "-" + userId;
        
        Map<String, Object> capInfo = new HashMap<>();
        capInfo.put("capId", capId);
        capInfo.put("skillId", skillId);
        capInfo.put("userId", userId);
        capInfo.put("config", capConfig);
        capInfo.put("createdAt", System.currentTimeMillis());
        capInfo.put("status", "active");
        
        capInfoStore.put(capId, capInfo);
        
        return capInfo;
    }

    private void analyzeEnterpriseDependencies(String skillId, SkillDependency dep) {
        List<Map<String, Object>> sceneDeps = analyzeSceneDependencies(skillId);
        for (Map<String, Object> scene : sceneDeps) {
            SkillDependency.SceneDependency sd = new SkillDependency.SceneDependency();
            sd.setSceneId((String) scene.get("sceneId"));
            sd.setSceneName((String) scene.get("sceneName"));
            sd.setSceneType((String) scene.get("sceneType"));
            sd.setRequired(Boolean.TRUE.equals(scene.get("required")));
            sd.setGroupId((String) scene.get("groupId"));
            sd.setGroupName((String) scene.get("groupName"));
            dep.getScenes().add(sd);
        }
        
        List<Map<String, Object>> skillDeps = analyzeSkillDependencies(skillId);
        for (Map<String, Object> skill : skillDeps) {
            SkillDependency.SkillRefDependency sd = new SkillDependency.SkillRefDependency();
            sd.setSkillId((String) skill.get("skillId"));
            sd.setSkillName((String) skill.get("skillName"));
            sd.setRequired(Boolean.TRUE.equals(skill.get("required")));
            sd.setReason((String) skill.get("reason"));
            dep.getSkills().add(sd);
        }
        
        List<Map<String, Object>> perms = analyzeRequiredPermissions(skillId);
        for (Map<String, Object> perm : perms) {
            SkillDependency.PermissionRequest pr = new SkillDependency.PermissionRequest();
            pr.setPermissionId((String) perm.get("permissionId"));
            pr.setPermissionType((String) perm.get("permissionType"));
            pr.setResourceName((String) perm.get("resourceName"));
            pr.setPermission((String) perm.get("permission"));
            pr.setDescription((String) perm.get("description"));
            pr.setRequired(Boolean.TRUE.equals(perm.get("required")));
            dep.getPermissions().add(pr);
        }
        
        dep.setHasDependencies(true);
    }

    private void analyzePersonalDependencies(String skillId, SkillDependency dep) {
        SkillDependency.PermissionRequest pr = new SkillDependency.PermissionRequest();
        pr.setPermissionId("local-access");
        pr.setPermissionType("local");
        pr.setResourceName("本地资源");
        pr.setPermission("access");
        pr.setDescription("访问本地资源");
        pr.setRequired(true);
        dep.getPermissions().add(pr);
        
        dep.setHasDependencies(false);
    }

    private String getSkillName(String skillId) {
        if (skillId.contains("finance")) return "财务助手";
        if (skillId.contains("hr")) return "人事助手";
        if (skillId.contains("project")) return "项目助手";
        if (skillId.contains("approve")) return "审批助手";
        return "技能-" + skillId;
    }

    private String determineSkillType(String skillId) {
        if (skillId.contains("finance") || skillId.contains("hr") || 
            skillId.contains("project") || skillId.contains("approve")) {
            return "enterprise";
        }
        return "personal";
    }

    private String getSkillDescription(String skillId) {
        if (skillId.contains("finance")) return "企业财务数据分析和报表生成";
        if (skillId.contains("hr")) return "人事管理和员工档案处理";
        if (skillId.contains("project")) return "项目管理和进度跟踪";
        if (skillId.contains("approve")) return "审批流程处理";
        return "通用技能";
    }

    private String getSkillSource(String skillId) {
        return "github";
    }

    private String generateGroupAddress(String groupId) {
        return "nexus://group/" + groupId;
    }

    private Map<String, Object> createPermission(String id, String type, String resource, 
            String permission, String desc, boolean required) {
        Map<String, Object> perm = new HashMap<>();
        perm.put("permissionId", id);
        perm.put("permissionType", type);
        perm.put("resourceName", resource);
        perm.put("permission", permission);
        perm.put("description", desc);
        perm.put("required", required);
        return perm;
    }
}

package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityCategory;
import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.Visibility;
import net.ooder.mvp.skill.scene.capability.service.BusinessSemanticsScorer.BusinessSemanticsScore;
import net.ooder.mvp.skill.scene.capability.service.impl.CapabilityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillCapabilitySyncService {

    private static final Logger log = LoggerFactory.getLogger(SkillCapabilitySyncService.class);

    @Value("${ooder.skills.path:../skills}")
    private String skillsPath;

    @Value("${ooder.skills.auto-sync:true}")
    private boolean autoSync;

    @Autowired
    private CapabilityService capabilityService;

    private final Yaml yaml = new Yaml();

    private int syncedCount = 0;
    private int skippedCount = 0;
    private int errorCount = 0;

    @PostConstruct
    public void init() {
        if (autoSync) {
            log.info("[SkillCapabilitySyncService] Starting auto sync from: {}", skillsPath);
            syncAllSkills();
        }
    }

    public Map<String, Object> syncAllSkills() {
        syncedCount = 0;
        skippedCount = 0;
        errorCount = 0;
        List<String> syncedSkills = new ArrayList<>();
        List<String> errorSkills = new ArrayList<>();

        Path skillsDir = resolveSkillsPath();
        log.info("[syncAllSkills] Scanning directory: {}", skillsDir.toAbsolutePath());

        if (!Files.exists(skillsDir)) {
            log.warn("[syncAllSkills] Skills directory not found: {}", skillsDir);
            return createResult(syncedSkills, errorSkills);
        }

        // 启动批量模式，避免每次更新都保存文件
        if (capabilityService instanceof CapabilityServiceImpl) {
            ((CapabilityServiceImpl) capabilityService).startBatchMode();
        }

        try {
            Files.walk(skillsDir)
                .filter(this::isSkillYaml)
                .forEach(this::processSkillYaml);
        } catch (Exception e) {
            log.error("[syncAllSkills] Error walking skills directory: {}", e.getMessage());
        } finally {
            // 结束批量模式，一次性保存所有变更
            if (capabilityService instanceof CapabilityServiceImpl) {
                ((CapabilityServiceImpl) capabilityService).endBatchMode();
            }
        }

        log.info("[syncAllSkills] Sync completed: synced={}, skipped={}, errors={}", 
                syncedCount, skippedCount, errorCount);
        return createResult(syncedSkills, errorSkills);
    }

    private Path resolveSkillsPath() {
        Path dir = Paths.get(skillsPath);
        if (Files.exists(dir)) {
            return dir;
        }
        dir = Paths.get(System.getProperty("user.dir"), skillsPath);
        if (Files.exists(dir)) {
            return dir;
        }
        dir = Paths.get("..", "skills");
        if (Files.exists(dir)) {
            return dir;
        }
        return Paths.get("../../skills");
    }

    private boolean isSkillYaml(Path path) {
        String fileName = path.getFileName().toString();
        if (!"skill.yaml".equals(fileName)) {
            return false;
        }
        String pathStr = path.toString().replace('\\', '/');
        log.debug("[isSkillYaml] Checking path: {}", pathStr);
        if (pathStr.contains("/target/")) {
            log.debug("[isSkillYaml] Skipping target directory: {}", pathStr);
            return false;
        }
        if (pathStr.contains("/src/main/resources/")) {
            log.debug("[isSkillYaml] Skipping src/main/resources directory: {}", pathStr);
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void processSkillYaml(Path skillYamlPath) {
        try {
            Path skillDir = skillYamlPath.getParent();
            String skillId = skillDir.getFileName().toString();
            log.info("[processSkillYaml] Processing: {} -> skillId: {}", skillYamlPath, skillId);

            Map<String, Object> skillData = yaml.load(new FileInputStream(skillYamlPath.toFile()));
            if (skillData == null) {
                skippedCount++;
                return;
            }

            Map<String, Object> metadata = (Map<String, Object>) skillData.get("metadata");
            String skillName = metadata != null ? (String) metadata.get("name") : skillId;
            String version = metadata != null ? (String) metadata.get("version") : "1.0.0";
            String description = metadata != null ? (String) metadata.get("description") : "";

            Map<String, Object> spec = (Map<String, Object>) skillData.get("spec");
            String skillType = spec != null ? (String) spec.get("type") : "service-skill";
            
            // 【新增】从 spec.capability.category 读取技能级别的分类
            String skillCategory = null;
            if (spec != null) {
                Map<String, Object> capability = (Map<String, Object>) spec.get("capability");
                if (capability != null) {
                    skillCategory = (String) capability.get("category");
                    if (skillCategory != null) {
                        log.info("[processSkillYaml] Found skill-level category '{}' for skill: {}", skillCategory, skillId);
                    }
                }
            }
            
            Boolean specMainFirst = spec != null ? Boolean.TRUE.equals(spec.get("mainFirst")) : false;
            Boolean specHasSelfDrive = spec != null ? Boolean.TRUE.equals(spec.get("hasSelfDrive")) : null;
            Integer specBusinessScore = null;
            if (spec != null && spec.get("businessSemanticsScore") != null) {
                try {
                    specBusinessScore = Integer.parseInt(spec.get("businessSemanticsScore").toString());
                } catch (Exception e) {
                    specBusinessScore = null;
                }
            }
            Boolean specSceneSkill = spec != null ? Boolean.TRUE.equals(spec.get("sceneSkill")) : false;

            List<Map<String, Object>> capabilities = null;
            if (spec != null) {
                capabilities = (List<Map<String, Object>>) spec.get("capabilities");
            }

            if (capabilities == null || capabilities.isEmpty()) {
                log.debug("[processSkillYaml] No capabilities defined in skill: {}", skillId);
                skippedCount++;
                return;
            }

            syncSkillCapabilities(skillId, capabilities, version, skillType, specMainFirst, specHasSelfDrive, specBusinessScore, specSceneSkill, skillCategory, skillName, description, spec);
            syncedCount++;

        } catch (Exception e) {
            log.error("[processSkillYaml] Error processing {}: {}", skillYamlPath, e.getMessage());
            errorCount++;
        }
    }

    private void syncSkillCapabilities(String skillId, List<Map<String, Object>> capabilities, String version, String skillType, Boolean specMainFirst, Boolean specHasSelfDrive, Integer specBusinessScore, Boolean specSceneSkill, String skillCategory, String skillName, String description, Map<String, Object> spec) {
        List<Capability> caps = new ArrayList<>();
        
        String skillForm = null;
        String sceneType = null;
        String visibility = "public";
        if (spec != null) {
            Object skillFormObj = spec.get("skillForm");
            if (skillFormObj != null) {
                skillForm = String.valueOf(skillFormObj);
            }
            Map<String, Object> scene = (Map<String, Object>) spec.get("scene");
            if (scene != null) {
                sceneType = (String) scene.get("type");
                visibility = scene.get("visibility") != null ? (String) scene.get("visibility") : "public";
            }
        }
        
        boolean isSceneSkill = "SCENE".equals(skillForm) || "scene-skill".equals(skillType);
        
        log.info("[syncSkillCapabilities] skillId={}, skillForm={}, skillType={}, isSceneSkill={}", skillId, skillForm, skillType, isSceneSkill);
        
        boolean hasMainCapability = capabilities.stream()
            .anyMatch(cap -> skillId.equals(cap.get("id")));
        
        log.info("[syncSkillCapabilities] hasMainCapability={}, capabilities count={}", hasMainCapability, capabilities.size());
        
        if (isSceneSkill && !hasMainCapability) {
            log.info("[syncSkillCapabilities] Auto-creating main capability for SCENE skill: {}", skillId);
            Map<String, Object> mainCap = new HashMap<>();
            mainCap.put("id", skillId);
            mainCap.put("name", skillName != null ? skillName : skillId);
            mainCap.put("description", description != null ? description : "");
            mainCap.put("category", skillCategory != null ? skillCategory : "biz");
            mainCap.put("type", "SCENE");
            mainCap.put("skillForm", "SCENE");
            mainCap.put("sceneType", sceneType != null ? sceneType : "INTERACTIVE");
            mainCap.put("visibility", visibility);
            mainCap.put("mainFirst", true);
            mainCap.put("isSceneCapability", true);
            
            List<Map<String, Object>> modifiableCapabilities = new ArrayList<>(capabilities);
            modifiableCapabilities.add(0, mainCap);
            capabilities = modifiableCapabilities;
        }

        for (Map<String, Object> capData : capabilities) {
            Capability cap = createCapabilityFromYaml(skillId, capData, version, skillType, specMainFirst, specHasSelfDrive, specBusinessScore, specSceneSkill, skillCategory);
            caps.add(cap);
        }

        for (Capability newCap : caps) {
            Capability existing = capabilityService.findById(newCap.getCapabilityId());
            if (existing == null) {
                capabilityService.register(newCap);
                log.info("[syncSkillCapabilities] Registered new capability: {} from skill: {}", 
                        newCap.getCapabilityId(), skillId);
            } else if (needsUpdate(newCap, existing, version)) {
                updateCapabilityFromSkill(newCap, existing, version);
                log.debug("[syncSkillCapabilities] Updated capability: {} from skill: {}", 
                        newCap.getCapabilityId(), skillId);
            } else {
                log.debug("[syncSkillCapabilities] No changes, skipping: {} from skill: {}", 
                        newCap.getCapabilityId(), skillId);
            }
        }
    }

    private boolean needsUpdate(Capability newCap, Capability existing, String newVersion) {
        if (newVersion != null && !newVersion.equals(existing.getVersion())) {
            log.debug("[needsUpdate] {} version changed: {} -> {}", newCap.getCapabilityId(), existing.getVersion(), newVersion);
            return true;
        }
        if (!Objects.equals(newCap.getName(), existing.getName())) {
            log.debug("[needsUpdate] {} name changed: {} -> {}", newCap.getCapabilityId(), existing.getName(), newCap.getName());
            return true;
        }
        if (!Objects.equals(newCap.getDescription(), existing.getDescription())) {
            log.debug("[needsUpdate] {} description changed", newCap.getCapabilityId());
            return true;
        }
        if (newCap.getCapabilityType() != existing.getCapabilityType()) {
            log.debug("[needsUpdate] {} type changed: {} -> {}", newCap.getCapabilityId(), existing.getCapabilityType(), newCap.getCapabilityType());
            return true;
        }
        if (!Objects.equals(newCap.getParameters(), existing.getParameters())) {
            log.debug("[needsUpdate] {} parameters changed", newCap.getCapabilityId());
            return true;
        }
        return false;
    }

    private Capability createCapabilityFromYaml(String skillId, Map<String, Object> capData, String version, String skillType, Boolean specMainFirst, Boolean specHasSelfDrive, Integer specBusinessScore, Boolean specSceneSkill, String skillCategory) {
        Capability cap = new Capability();
        
        String capId = (String) capData.get("id");
        cap.setCapabilityId(capId);
        cap.setName((String) capData.get("name"));
        cap.setDescription((String) capData.get("description"));
        cap.setVersion(version);
        cap.setSkillId(skillId);
        
        CapabilityType capType = determineCapabilityType(capData, skillType);
        cap.setCapabilityType(capType);
        
        Object mainFirstObj = capData.get("mainFirst");
        boolean capMainFirst = mainFirstObj != null ? Boolean.TRUE.equals(mainFirstObj) : Boolean.TRUE.equals(specMainFirst);
        cap.setMainFirst(capMainFirst);
        
        Object skillFormObj = capData.get("skillForm");
        if (skillFormObj != null) {
            cap.setSkillForm(String.valueOf(skillFormObj));
        } else {
            cap.setSkillForm("STANDALONE");
        }
        
        Object sceneTypeObj = capData.get("sceneType");
        if (sceneTypeObj != null) {
            cap.setSceneType(String.valueOf(sceneTypeObj));
        }
        
        Object visibilityObj = capData.get("visibility");
        if (visibilityObj != null) {
            cap.setVisibility(String.valueOf(visibilityObj));
        } else {
            cap.setVisibility("public");
        }
        
        Object isSceneCapObj = capData.get("isSceneCapability");
        if (isSceneCapObj != null) {
            cap.setSceneCapability(Boolean.TRUE.equals(isSceneCapObj));
        } else if (Boolean.TRUE.equals(specSceneSkill)) {
            cap.setSceneCapability(true);
        } else {
            cap.setSceneCapability("scene-skill".equals(skillType));
        }
        
        // 【修复】优先使用能力级别的 category，如果没有则使用技能级别的 skillCategory
        Object categoryObj = capData.get("category");
        if (categoryObj != null) {
            String categoryStr = String.valueOf(categoryObj);
            CapabilityCategory category = CapabilityCategory.fromCode(categoryStr);
            cap.setCapabilityCategory(category);
            log.debug("[createCapabilityFromYaml] Set category '{}' from capability for {}", categoryStr, capId);
        } else if (skillCategory != null && !skillCategory.isEmpty()) {
            CapabilityCategory category = CapabilityCategory.fromCode(skillCategory);
            cap.setCapabilityCategory(category);
            log.info("[createCapabilityFromYaml] Set category '{}' from skill-level for {}", skillCategory, capId);
        }
        
        @SuppressWarnings("unchecked")
        List<String> capabilityIds = (List<String>) capData.get("capabilities");
        if (capabilityIds != null && !capabilityIds.isEmpty()) {
            cap.setCapabilities(capabilityIds);
        } else if (cap.getCapabilityType() == CapabilityType.SCENE) {
            cap.setCapabilities(Arrays.asList(capId));
        }
        
        List<Capability.ParameterDef> parameters = parseParameters(capData);
        if (parameters != null && !parameters.isEmpty()) {
            cap.setParameters(parameters);
        }
        
        Capability.ReturnDef returns = parseReturns(capData);
        if (returns != null) {
            cap.setReturns(returns);
        }
        
        List<Map<String, Object>> examples = parseExamples(capData);
        if (examples != null && !examples.isEmpty()) {
            Map<String, Object> metadata = cap.getMetadata();
            metadata.put("examples", examples);
            cap.setMetadata(metadata);
        }
        
        cap.setStatus(CapabilityStatus.REGISTERED);
        cap.setCreateTime(System.currentTimeMillis());
        cap.setUpdateTime(System.currentTimeMillis());
        
        return cap;
    }
    
    private void calculateAndSetClassification(Capability cap) {
        if (!cap.isSceneCapability()) {
            cap.setSkillForm(SkillForm.PROVIDER);
            cap.setSceneType((SceneType) null);
            return;
        }
        
        cap.setSkillForm(SkillForm.SCENE);
        
        boolean hasSelfDrive = cap.isHasSelfDrive();
        int score = cap.getBusinessSemanticsScore() != null ? cap.getBusinessSemanticsScore() : 5;
        
        if (hasSelfDrive) {
            cap.setSceneType(SceneType.AUTO);
        } else {
            cap.setSceneType(SceneType.TRIGGER);
        }
    }
    
    private boolean isInternal(Capability cap) {
        if (cap.getSceneType() == null) {
            return false;
        }
        if ("AUTO".equals(cap.getSceneType())) {
            int score = cap.getBusinessSemanticsScore() != null ? cap.getBusinessSemanticsScore() : 5;
            return score < 8;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private List<Capability.ParameterDef> parseParameters(Map<String, Object> capData) {
        List<Map<String, Object>> paramsData = (List<Map<String, Object>>) capData.get("parameters");
        if (paramsData == null || paramsData.isEmpty()) {
            return null;
        }
        
        List<Capability.ParameterDef> parameters = new ArrayList<>();
        for (Map<String, Object> paramData : paramsData) {
            Capability.ParameterDef param = new Capability.ParameterDef();
            param.setName((String) paramData.get("name"));
            param.setType((String) paramData.get("type"));
            param.setRequired(Boolean.TRUE.equals(paramData.get("required")));
            param.setDescription((String) paramData.get("description"));
            
            Object defaultVal = paramData.get("default");
            if (defaultVal != null) {
                param.setDefaultValue(defaultVal);
            }
            
            parameters.add(param);
        }
        return parameters;
    }

    @SuppressWarnings("unchecked")
    private Capability.ReturnDef parseReturns(Map<String, Object> capData) {
        Map<String, Object> returnsData = (Map<String, Object>) capData.get("returns");
        if (returnsData == null) {
            return null;
        }
        
        Capability.ReturnDef returns = new Capability.ReturnDef();
        returns.setType((String) returnsData.get("type"));
        
        Map<String, String> properties = (Map<String, String>) returnsData.get("properties");
        if (properties != null) {
            returns.setProperties(properties);
        }
        
        return returns;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseExamples(Map<String, Object> capData) {
        List<Map<String, Object>> rawExamples = (List<Map<String, Object>>) capData.get("examples");
        if (rawExamples == null || rawExamples.isEmpty()) {
            return null;
        }
        
        List<Map<String, Object>> examples = new ArrayList<>();
        for (Map<String, Object> element : rawExamples) {
            Map<String, Object> ex = new HashMap<>();
            ex.put("name", (String) element.get("name"));
            ex.put("description", (String) element.get("description"));
            ex.put("type", (String) element.get("type"));
            examples.add(ex);
        }
        return examples;
    }

    private CapabilityType determineCapabilityType(Map<String, Object> capData, String skillType) {
        Object typeObj = capData.get("type");
        if (typeObj != null) {
            String typeStr = typeObj.toString();
            switch (typeStr.toUpperCase()) {
                case "ATOMIC": return CapabilityType.ATOMIC;
                case "COMPOSITE": return CapabilityType.COMPOSITE;
                case "SCENE": return CapabilityType.SCENE;
                case "DRIVER": return CapabilityType.DRIVER;
                case "SERVICE": return CapabilityType.SERVICE;
                case "AI": return CapabilityType.AI;
                case "DATA": return CapabilityType.DATA;
                case "COMMUNICATION": return CapabilityType.COMMUNICATION;
                case "SECURITY": return CapabilityType.SECURITY;
                case "TOOL": return CapabilityType.TOOL;
            }
        }

        String categoryStr = (String) capData.get("category");
        if (categoryStr != null) {
            switch (categoryStr.toLowerCase()) {
                case "ai": return CapabilityType.AI;
                case "service": return CapabilityType.SERVICE;
                case "data": return CapabilityType.DATA;
                case "driver": return CapabilityType.DRIVER;
                case "security": return CapabilityType.SECURITY;
                case "communication": return CapabilityType.COMMUNICATION;
            }
        }

        if ("scene-skill".equals(skillType)) {
            return CapabilityType.SCENE;
        }

        return CapabilityType.SERVICE;
    }

    private void updateCapabilityFromSkill(Capability newCap, Capability existing, String newVersion) {
        existing.setName(newCap.getName());
        existing.setDescription(newCap.getDescription());
        existing.setVersion(newVersion);
        existing.setSkillId(newCap.getSkillId());
        if (newCap.getCapabilityType() != null) {
            existing.setCapabilityType(newCap.getCapabilityType());
        }
        if (newCap.getSceneType() != null) {
            existing.setSceneType(newCap.getSceneType());
        }
        if (newCap.getSkillForm() != null) {
            existing.setSkillForm(newCap.getSkillForm());
        }
        if (newCap.getVisibility() != null) {
            existing.setVisibility(newCap.getVisibility());
        }
        if (newCap.getCapabilityCategory() != null) {
            existing.setCapabilityCategory(newCap.getCapabilityCategory());
        }
        existing.setMainFirst(newCap.isMainFirst());
        existing.setSceneCapability(newCap.isSceneCapability());
        if (newCap.getParameters() != null && !newCap.getParameters().isEmpty()) {
            existing.setParameters(newCap.getParameters());
        }
        if (newCap.getReturns() != null) {
            existing.setReturns(newCap.getReturns());
        }
        if (newCap.getMetadata() != null) {
            Map<String, Object> existingMeta = existing.getMetadata();
            existingMeta.putAll(newCap.getMetadata());
            existing.setMetadata(existingMeta);
        }
        existing.setUpdateTime(System.currentTimeMillis());
        capabilityService.update(existing);
    }

    private Map<String, Object> createResult(List<String> syncedSkills, List<String> errorSkills) {
        Map<String, Object> result = new HashMap<>();
        result.put("synced", syncedCount);
        result.put("skipped", skippedCount);
        result.put("errors", errorCount);
        result.put("syncedSkills", syncedSkills);
        result.put("errorSkills", errorSkills);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    public int getSyncedCount() {
        return syncedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public int getErrorCount() {
        return errorCount;
    }
}

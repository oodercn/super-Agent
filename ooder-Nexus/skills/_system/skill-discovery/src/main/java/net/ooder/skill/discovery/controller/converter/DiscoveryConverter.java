package net.ooder.skill.discovery.controller.converter;

import net.ooder.skill.discovery.dto.discovery.CapabilityDTO;
import net.ooder.skill.discovery.model.CapabilityCategory;
import net.ooder.skills.api.InstalledSkill;
import net.ooder.skills.api.SkillPackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 发现服务转换器
 * 
 * 注意：SDK 3.0.1+ 已经提供自动推断功能：
 * - CategoryResolver: 自动推断业务分类
 * - SkillFormResolver: 自动推断技能形态
 * - DefaultConverters: 标准 DTO 转换器
 * 
 * OS 层只需简单调用 SDK 方法即可，无需再维护推断逻辑
 */
public final class DiscoveryConverter {

    private static final Logger log = LoggerFactory.getLogger(DiscoveryConverter.class);

    private static final Map<String, String> SKILL_FORM_ALIAS_MAP = new LinkedHashMap<>();
    
    static {
        SKILL_FORM_ALIAS_MAP.put("scene-skill", "SCENE");
        SKILL_FORM_ALIAS_MAP.put("scene", "SCENE");
        SKILL_FORM_ALIAS_MAP.put("provider-skill", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("provider", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("service-skill", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("service", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("enterprise-skill", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("infrastructure-skill", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("system-core", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("nexus-ui", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("ui", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("driver", "DRIVER");
        SKILL_FORM_ALIAS_MAP.put("driver-skill", "DRIVER");
        SKILL_FORM_ALIAS_MAP.put("integration", "INTEGRATION");
        SKILL_FORM_ALIAS_MAP.put("integration-skill", "INTEGRATION");
        SKILL_FORM_ALIAS_MAP.put("standalone", "PROVIDER");
        SKILL_FORM_ALIAS_MAP.put("skill", "PROVIDER");
    }

    private DiscoveryConverter() {
    }
    
    private static String normalizeSkillForm(String rawForm) {
        if (rawForm == null || rawForm.isEmpty()) {
            return null;
        }
        String upperForm = rawForm.toUpperCase();
        if (upperForm.equals("SCENE") || upperForm.equals("PROVIDER") || 
            upperForm.equals("DRIVER") || upperForm.equals("INTEGRATION")) {
            return upperForm;
        }
        if (upperForm.equals("STANDALONE")) {
            return "PROVIDER";
        }
        String normalized = SKILL_FORM_ALIAS_MAP.get(rawForm.toLowerCase());
        if (normalized != null) {
            log.debug("[normalizeSkillForm] Mapped '{}' to '{}'", rawForm, normalized);
            return normalized;
        }
        log.debug("[normalizeSkillForm] Unknown skillForm '{}', defaulting to PROVIDER", rawForm);
        return "PROVIDER";
    }

    /**
     * 将 SkillPackage 转换为 CapabilityDTO
     * 
     * SDK 3.0.1+ 自动处理：
     * - category 字段推断（通过 CategoryResolver）
     * - skillForm 字段推断（通过 SkillFormResolver）
     * - 标准字段映射
     */
    public static CapabilityDTO toCapabilityDTO(SkillPackage pkg) {
        if (pkg == null) {
            return null;
        }

        String skillId = pkg.getSkillId();

        CapabilityDTO dto = new CapabilityDTO();
        dto.setId(skillId);
        dto.setName(pkg.getName());
        dto.setVersion(pkg.getVersion());
        dto.setSkillId(skillId);
        dto.setSource(pkg.getSource());
        
        String category = pkg.getCategory();
        if (category == null || category.isEmpty()) {
            category = inferCategoryFromSkillId(skillId);
        }
        dto.setCategory(category);
        
        if (category != null) {
            CapabilityCategory mappedCategory = CapabilityCategory.fromCode(category);
            dto.setBusinessCategory(mappedCategory.getCode());
            dto.setCapabilityCategory(mappedCategory.getCode());
        }

        String rawSkillForm = pkg.getForm() != null ? pkg.getForm().name() : null;
        if (rawSkillForm == null && pkg.getMetadata() != null) {
            Object formObj = pkg.getMetadata().get("form");
            if (formObj != null) {
                rawSkillForm = formObj.toString();
            }
        }
        String skillForm = normalizeSkillForm(rawSkillForm);
        if (skillForm == null) {
            skillForm = inferSkillFormFromSkillId(skillId);
        }
        dto.setSkillForm(skillForm);
        
        boolean isScene = "SCENE".equals(skillForm);
        dto.setSceneCapability(isScene);
        dto.setType(isScene ? "SCENE" : "SKILL");

        convertTags(pkg.getTags(), dto);
        convertDependencies(pkg.getDependencies(), dto);
        convertMetadata(pkg.getMetadata(), dto);
        convertCapabilities(pkg.getCapabilities(), dto);

        return dto;
    }
    
    private static String inferCategoryFromSkillId(String skillId) {
        if (skillId == null) return null;
        String lower = skillId.toLowerCase();
        if (lower.contains("llm") || lower.contains("chat") || lower.contains("qianwen") || 
            lower.contains("deepseek") || lower.contains("baidu") || lower.contains("zhipu")) {
            return "llm";
        }
        if (lower.contains("knowledge") || lower.contains("qa")) {
            return "knowledge";
        }
        if (lower.contains("org") || lower.contains("dingding") || lower.contains("feishu") || 
            lower.contains("user") || lower.contains("tenant") || lower.contains("auth")) {
            return "org";
        }
        if (lower.contains("msg") || lower.contains("message") || lower.contains("notify")) {
            return "msg";
        }
        if (lower.contains("vfs") || lower.contains("storage") || lower.contains("file")) {
            return "vfs";
        }
        if (lower.contains("ui") || lower.contains("dashboard") || lower.contains("console")) {
            return "ui";
        }
        if (lower.contains("sys") || lower.contains("config") || lower.contains("management")) {
            return "sys";
        }
        if (lower.contains("biz") || lower.contains("approval") || lower.contains("recruitment") ||
            lower.contains("calendar") || lower.contains("procedure")) {
            return "biz";
        }
        return "util";
    }
    
    private static String inferSkillFormFromSkillId(String skillId) {
        if (skillId == null) return "PROVIDER";
        String lower = skillId.toLowerCase();
        if (lower.contains("-scene") || lower.startsWith("scene-") || lower.contains("-form")) {
            return "SCENE";
        }
        if (lower.contains("-driver") || lower.startsWith("driver-")) {
            return "DRIVER";
        }
        return "PROVIDER";
    }

    /**
     * 将 InstalledSkill 转换为 CapabilityDTO
     * 
     * SDK 3.0.1+ 自动处理字段推断
     */
    public static CapabilityDTO toCapabilityDTO(InstalledSkill skill) {
        if (skill == null) {
            return null;
        }

        CapabilityDTO dto = new CapabilityDTO();
        dto.setId(skill.getSkillId());
        dto.setName(skill.getName());
        dto.setVersion(skill.getVersion());
        dto.setSkillId(skill.getSkillId());
        dto.setSceneType(skill.getSceneId());
        dto.setStatus(skill.getStatus());
        dto.setDependencies(skill.getDependencies());
        dto.setInstalled(true);

        // SDK 3.0.1+ 自动推断
        String category = skill.getCategory();
        String skillForm = skill.getForm() != null ? skill.getForm().name() : null;
        
        dto.setCategory(category);
        dto.setCapabilityCategory(category);
        dto.setBusinessCategory(category);
        dto.setSkillForm(skillForm);
        
        boolean isScene = "SCENE".equals(skillForm);
        dto.setSceneCapability(isScene);
        dto.setType(isScene ? "SCENE" : "SKILL");

        return dto;
    }

    public static Map<String, Object> toMap(SkillPackage pkg) {
        if (pkg == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", pkg.getSkillId());
        map.put("name", pkg.getName());
        map.put("version", pkg.getVersion());
        map.put("skillId", pkg.getSkillId());
        map.put("source", pkg.getSource());
        map.put("downloadUrl", pkg.getDownloadUrl());
        map.put("category", pkg.getCategory());
        map.put("skillForm", pkg.getForm() != null ? pkg.getForm().name() : null);
        map.put("tags", pkg.getTags());
        map.put("dependencies", pkg.getDependencies());

        Map<String, Object> metadata = pkg.getMetadata();
        if (metadata != null) {
            map.put("description", metadata.get("description"));
        }

        List<?> caps = pkg.getCapabilities();
        if (caps != null) {
            List<String> capNames = new ArrayList<>();
            for (Object cap : caps) {
                if (cap != null) {
                    capNames.add(cap.toString());
                }
            }
            map.put("capabilities", capNames);
        }

        return map;
    }

    public static Map<String, Object> toMap(InstalledSkill skill) {
        if (skill == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", skill.getSkillId());
        map.put("name", skill.getName());
        map.put("version", skill.getVersion());
        map.put("skillId", skill.getSkillId());
        map.put("sceneId", skill.getSceneId());
        map.put("installPath", skill.getInstallPath());
        map.put("status", skill.getStatus());
        map.put("installTime", skill.getInstallTime());
        map.put("dependencies", skill.getDependencies());
        map.put("category", skill.getCategory());
        map.put("skillForm", skill.getSkillForm());
        map.put("installed", true);

        return map;
    }

    public static Map<String, Object> toMap(CapabilityDTO cap) {
        if (cap == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", cap.getId());
        map.put("name", cap.getName());
        map.put("description", cap.getDescription());
        map.put("version", cap.getVersion());
        map.put("source", cap.getSource());
        map.put("status", cap.getStatus());
        map.put("type", cap.getType());
        map.put("sceneCapability", cap.isSceneCapability());
        map.put("skillForm", cap.getSkillForm());
        map.put("sceneType", cap.getSceneType());
        map.put("category", cap.getCategory());
        map.put("capabilityCategory", cap.getCapabilityCategory());
        map.put("businessCategory", cap.getBusinessCategory());
        map.put("visibility", cap.getVisibility());
        map.put("installed", cap.isInstalled());
        map.put("capabilities", cap.getCapabilities());
        map.put("dependencies", cap.getDependencies());
        map.put("tags", cap.getTags());
        map.put("driverConditions", cap.getDriverConditions());
        map.put("participants", cap.getParticipants());

        return map;
    }

    public static List<Map<String, Object>> toMapList(List<CapabilityDTO> caps) {
        if (caps == null) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (CapabilityDTO cap : caps) {
            result.add(toMap(cap));
        }
        return result;
    }

    private static void convertTags(List<?> tags, CapabilityDTO dto) {
        if (tags == null) {
            return;
        }
        List<String> tagStrings = new ArrayList<>();
        for (Object tag : tags) {
            if (tag != null) {
                tagStrings.add(tag.toString());
            }
        }
        dto.setTags(tagStrings);
    }

    private static void convertDependencies(List<?> deps, CapabilityDTO dto) {
        if (deps == null) {
            return;
        }
        List<String> depStrings = new ArrayList<>();
        for (Object dep : deps) {
            if (dep != null) {
                depStrings.add(dep.toString());
            }
        }
        dto.setDependencies(depStrings);
    }

    private static void convertMetadata(Map<String, Object> metadata, CapabilityDTO dto) {
        if (metadata != null) {
            dto.setDescription((String) metadata.get("description"));

            Object sceneTypeObj = metadata.get("sceneType");
            if (sceneTypeObj != null) {
                dto.setSceneType(String.valueOf(sceneTypeObj));
            }

            Object visibilityObj = metadata.get("visibility");
            if (visibilityObj != null) {
                dto.setVisibility(String.valueOf(visibilityObj));
            }

            Object rolesObj = metadata.get("roles");
            if (rolesObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> roles = (List<Map<String, Object>>) rolesObj;
                dto.setParticipants(roles);
            }

            Object driverConditionsObj = metadata.get("driverConditions");
            if (driverConditionsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> driverConditions = (List<Map<String, Object>>) driverConditionsObj;
                dto.setDriverConditions(driverConditions);
            }
        }
    }

    private static void convertCapabilities(List<?> caps, CapabilityDTO dto) {
        if (caps == null) {
            return;
        }
        List<String> capNames = new ArrayList<>();
        for (Object cap : caps) {
            if (cap != null) {
                if (cap instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> capMap = (Map<String, Object>) cap;
                    Object name = capMap.get("name");
                    if (name != null) {
                        capNames.add(name.toString());
                    } else {
                        Object id = capMap.get("id");
                        if (id != null) {
                            capNames.add(id.toString());
                        }
                    }
                } else {
                    capNames.add(cap.toString());
                }
            }
        }
        dto.setCapabilities(capNames);
    }
}

package net.ooder.mvp.skill.scene.dto.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class YamlDtoConverter {
    
    private YamlDtoConverter() {}
    
    @SuppressWarnings("unchecked")
    public static SkillYamlDTO convertToSkillYamlDTO(Map<String, Object> yamlData) {
        if (yamlData == null) {
            return null;
        }
        
        SkillYamlDTO dto = new SkillYamlDTO();
        dto.setApiVersion((String) yamlData.get("apiVersion"));
        dto.setKind((String) yamlData.get("kind"));
        dto.setMetadata(convertToMetadataDTO((Map<String, Object>) yamlData.get("metadata")));
        dto.setSpec(convertToSpecDTO((Map<String, Object>) yamlData.get("spec")));
        
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    public static SkillMetadataDTO convertToMetadataDTO(Map<String, Object> metadata) {
        if (metadata == null) {
            return null;
        }
        
        SkillMetadataDTO dto = new SkillMetadataDTO();
        dto.setId((String) metadata.get("id"));
        dto.setName((String) metadata.get("name"));
        dto.setVersion((String) metadata.get("version"));
        dto.setCategory((String) metadata.get("category"));
        dto.setSubCategory((String) metadata.get("subCategory"));
        dto.setDescription((String) metadata.get("description"));
        dto.setAuthor((String) metadata.get("author"));
        dto.setIcon((String) metadata.get("icon"));
        dto.setTags((List<String>) metadata.get("tags"));
        
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    public static SkillSpecDTO convertToSpecDTO(Map<String, Object> spec) {
        if (spec == null) {
            return null;
        }
        
        SkillSpecDTO dto = new SkillSpecDTO();
        dto.setSkillForm((String) spec.get("skillForm"));
        dto.setScene(convertToSceneConfigDTO((Map<String, Object>) spec.get("scene")));
        dto.setCapabilities(convertToCapabilityYamlDTOList((List<Map<String, Object>>) spec.get("capabilities")));
        dto.setDependencies(convertToDependencyYamlDTOList((List<Map<String, Object>>) spec.get("dependencies")));
        dto.setRoles(convertToRoleYamlDTOList((List<Map<String, Object>>) spec.get("roles")));
        dto.setActivationSteps(convertToActivationStepMap((Map<String, List<Map<String, Object>>>) spec.get("activationSteps")));
        dto.setMenus(convertToMenuMap((Map<String, List<Map<String, Object>>>) spec.get("menus")));
        
        return dto;
    }
    
    public static SceneConfigDTO convertToSceneConfigDTO(Map<String, Object> scene) {
        if (scene == null) {
            return null;
        }
        
        SceneConfigDTO dto = new SceneConfigDTO();
        dto.setType((String) scene.get("type"));
        dto.setVisibility((String) scene.get("visibility"));
        dto.setParticipantMode((String) scene.get("participantMode"));
        
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    public static List<CapabilityYamlDTO> convertToCapabilityYamlDTOList(List<Map<String, Object>> capabilities) {
        if (capabilities == null) {
            return null;
        }
        
        List<CapabilityYamlDTO> result = new ArrayList<>();
        for (Map<String, Object> cap : capabilities) {
            result.add(convertToCapabilityYamlDTO(cap));
        }
        return result;
    }
    
    public static CapabilityYamlDTO convertToCapabilityYamlDTO(Map<String, Object> cap) {
        if (cap == null) {
            return null;
        }
        
        CapabilityYamlDTO dto = new CapabilityYamlDTO();
        dto.setId((String) cap.get("id"));
        dto.setName((String) cap.get("name"));
        dto.setDescription((String) cap.get("description"));
        dto.setType((String) cap.get("type"));
        dto.setCategory((String) cap.get("category"));
        dto.setAutoBind((Boolean) cap.get("autoBind"));
        
        @SuppressWarnings("unchecked")
        List<String> deps = (List<String>) cap.get("dependencies");
        dto.setDependencies(deps);
        
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    public static List<DependencyYamlDTO> convertToDependencyYamlDTOList(List<Map<String, Object>> dependencies) {
        if (dependencies == null) {
            return null;
        }
        
        List<DependencyYamlDTO> result = new ArrayList<>();
        for (Map<String, Object> dep : dependencies) {
            result.add(convertToDependencyYamlDTO(dep));
        }
        return result;
    }
    
    public static DependencyYamlDTO convertToDependencyYamlDTO(Map<String, Object> dep) {
        if (dep == null) {
            return null;
        }
        
        DependencyYamlDTO dto = new DependencyYamlDTO();
        dto.setId((String) dep.get("id"));
        dto.setVersion((String) dep.get("version"));
        dto.setRequired((Boolean) dep.get("required"));
        dto.setAutoInstall((Boolean) dep.get("autoInstall"));
        dto.setDescription((String) dep.get("description"));
        
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    public static List<RoleYamlDTO> convertToRoleYamlDTOList(List<Map<String, Object>> roles) {
        if (roles == null) {
            return null;
        }
        
        List<RoleYamlDTO> result = new ArrayList<>();
        for (Map<String, Object> role : roles) {
            result.add(convertToRoleYamlDTO(role));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static RoleYamlDTO convertToRoleYamlDTO(Map<String, Object> role) {
        if (role == null) {
            return null;
        }
        
        RoleYamlDTO dto = new RoleYamlDTO();
        dto.setName((String) role.get("name"));
        dto.setDescription((String) role.get("description"));
        dto.setRequired((Boolean) role.get("required"));
        dto.setMinCount((Integer) role.get("minCount"));
        dto.setMaxCount((Integer) role.get("maxCount"));
        dto.setPermissions((List<String>) role.get("permissions"));
        
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, List<ActivationStepYamlDTO>> convertToActivationStepMap(
            Map<String, List<Map<String, Object>>> activationSteps) {
        if (activationSteps == null) {
            return null;
        }
        
        Map<String, List<ActivationStepYamlDTO>> result = new HashMap<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : activationSteps.entrySet()) {
            result.put(entry.getKey(), convertToActivationStepYamlDTOList(entry.getValue()));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static List<ActivationStepYamlDTO> convertToActivationStepYamlDTOList(List<Map<String, Object>> steps) {
        if (steps == null) {
            return null;
        }
        
        List<ActivationStepYamlDTO> result = new ArrayList<>();
        for (Map<String, Object> step : steps) {
            result.add(convertToActivationStepYamlDTO(step));
        }
        return result;
    }
    
    public static ActivationStepYamlDTO convertToActivationStepYamlDTO(Map<String, Object> step) {
        if (step == null) {
            return null;
        }
        
        ActivationStepYamlDTO dto = new ActivationStepYamlDTO();
        dto.setStepId((String) step.get("stepId"));
        dto.setName((String) step.get("name"));
        dto.setDescription((String) step.get("description"));
        dto.setRequired((Boolean) step.get("required"));
        dto.setSkippable((Boolean) step.get("skippable"));
        dto.setAutoExecute((Boolean) step.get("autoExecute"));
        
        @SuppressWarnings("unchecked")
        List<String> privateCaps = (List<String>) step.get("privateCapabilities");
        dto.setPrivateCapabilities(privateCaps);
        
        return dto;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, List<MenuYamlDTO>> convertToMenuMap(
            Map<String, List<Map<String, Object>>> menus) {
        if (menus == null) {
            return null;
        }
        
        Map<String, List<MenuYamlDTO>> result = new HashMap<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : menus.entrySet()) {
            result.put(entry.getKey(), convertToMenuYamlDTOList(entry.getValue()));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static List<MenuYamlDTO> convertToMenuYamlDTOList(List<Map<String, Object>> menus) {
        if (menus == null) {
            return null;
        }
        
        List<MenuYamlDTO> result = new ArrayList<>();
        for (Map<String, Object> menu : menus) {
            result.add(convertToMenuYamlDTO(menu));
        }
        return result;
    }
    
    public static MenuYamlDTO convertToMenuYamlDTO(Map<String, Object> menu) {
        if (menu == null) {
            return null;
        }
        
        MenuYamlDTO dto = new MenuYamlDTO();
        dto.setId((String) menu.get("id"));
        dto.setName((String) menu.get("name"));
        dto.setIcon((String) menu.get("icon"));
        dto.setUrl((String) menu.get("url"));
        dto.setOrder((Integer) menu.get("order"));
        dto.setVisible((Boolean) menu.get("visible"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> children = (List<Map<String, Object>>) menu.get("children");
        dto.setChildren(convertToMenuYamlDTOList(children));
        
        return dto;
    }
}

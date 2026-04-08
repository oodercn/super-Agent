package net.ooder.mvp.skill.scene.dto.yaml;

import java.util.List;
import java.util.Map;

public class SkillSpecDTO {
    
    private String skillForm;
    private SceneConfigDTO scene;
    private List<CapabilityYamlDTO> capabilities;
    private List<DependencyYamlDTO> dependencies;
    private Map<String, List<ActivationStepYamlDTO>> activationSteps;
    private Map<String, List<MenuYamlDTO>> menus;
    private List<RoleYamlDTO> roles;
    
    public String getSkillForm() { return skillForm; }
    public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
    
    public SceneConfigDTO getScene() { return scene; }
    public void setScene(SceneConfigDTO scene) { this.scene = scene; }
    
    public List<CapabilityYamlDTO> getCapabilities() { return capabilities; }
    public void setCapabilities(List<CapabilityYamlDTO> capabilities) { this.capabilities = capabilities; }
    
    public List<DependencyYamlDTO> getDependencies() { return dependencies; }
    public void setDependencies(List<DependencyYamlDTO> dependencies) { this.dependencies = dependencies; }
    
    public Map<String, List<ActivationStepYamlDTO>> getActivationSteps() { return activationSteps; }
    public void setActivationSteps(Map<String, List<ActivationStepYamlDTO>> activationSteps) { this.activationSteps = activationSteps; }
    
    public Map<String, List<MenuYamlDTO>> getMenus() { return menus; }
    public void setMenus(Map<String, List<MenuYamlDTO>> menus) { this.menus = menus; }
    
    public List<RoleYamlDTO> getRoles() { return roles; }
    public void setRoles(List<RoleYamlDTO> roles) { this.roles = roles; }
}

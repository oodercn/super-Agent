package net.ooder.mvp.skill.scene.dto.discovery;

import java.util.ArrayList;
import java.util.List;

public class InstallResultDTO {
    
    private String skillId;
    private String capabilityId;
    private String status;
    private String message;
    private Long installTime;
    private List<CapabilityDTO> capabilities;
    private List<String> installedDependencies;
    private List<String> existingDependencies;
    private List<String> failedDependencies;
    private String selectedRole;
    private List<String> driverConditions;
    private String llmProvider;

    public InstallResultDTO() {
        this.installedDependencies = new ArrayList<>();
        this.existingDependencies = new ArrayList<>();
        this.failedDependencies = new ArrayList<>();
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
        this.capabilityId = skillId;
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Long installTime) {
        this.installTime = installTime;
    }

    public List<CapabilityDTO> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityDTO> capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getInstalledDependencies() {
        return installedDependencies;
    }

    public void setInstalledDependencies(List<String> installedDependencies) {
        this.installedDependencies = installedDependencies != null ? installedDependencies : new ArrayList<>();
    }

    public List<String> getExistingDependencies() {
        return existingDependencies;
    }

    public void setExistingDependencies(List<String> existingDependencies) {
        this.existingDependencies = existingDependencies != null ? existingDependencies : new ArrayList<>();
    }

    public List<String> getFailedDependencies() {
        return failedDependencies;
    }

    public void setFailedDependencies(List<String> failedDependencies) {
        this.failedDependencies = failedDependencies != null ? failedDependencies : new ArrayList<>();
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public List<String> getDriverConditions() {
        return driverConditions;
    }

    public void setDriverConditions(List<String> driverConditions) {
        this.driverConditions = driverConditions;
    }

    public String getLlmProvider() {
        return llmProvider;
    }

    public void setLlmProvider(String llmProvider) {
        this.llmProvider = llmProvider;
    }
}

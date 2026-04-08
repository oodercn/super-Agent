package net.ooder.mvp.skill.scene.dto.key;

import java.util.List;

public class KeyRuleDTO {
    
    private String ruleId;
    private String ruleName;
    private String description;
    private Integer validityDays;
    private Boolean autoExpire;
    private Integer maxUseCount;
    private Integer dailyUseLimit;
    private List<String> allowedScenes;
    private List<String> allowedOperations;
    private List<String> allowedRoles;
    private Boolean requireApproval;
    private Boolean enableAudit;
    private Boolean enableAlert;
    private Long createdAt;
    private Long updatedAt;
    
    public KeyRuleDTO() {
    }
    
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }
    
    public Boolean getAutoExpire() { return autoExpire; }
    public void setAutoExpire(Boolean autoExpire) { this.autoExpire = autoExpire; }
    
    public Integer getMaxUseCount() { return maxUseCount; }
    public void setMaxUseCount(Integer maxUseCount) { this.maxUseCount = maxUseCount; }
    
    public Integer getDailyUseLimit() { return dailyUseLimit; }
    public void setDailyUseLimit(Integer dailyUseLimit) { this.dailyUseLimit = dailyUseLimit; }
    
    public List<String> getAllowedScenes() { return allowedScenes; }
    public void setAllowedScenes(List<String> allowedScenes) { this.allowedScenes = allowedScenes; }
    
    public List<String> getAllowedOperations() { return allowedOperations; }
    public void setAllowedOperations(List<String> allowedOperations) { this.allowedOperations = allowedOperations; }
    
    public List<String> getAllowedRoles() { return allowedRoles; }
    public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }
    
    public Boolean getRequireApproval() { return requireApproval; }
    public void setRequireApproval(Boolean requireApproval) { this.requireApproval = requireApproval; }
    
    public Boolean getEnableAudit() { return enableAudit; }
    public void setEnableAudit(Boolean enableAudit) { this.enableAudit = enableAudit; }
    
    public Boolean getEnableAlert() { return enableAlert; }
    public void setEnableAlert(Boolean enableAlert) { this.enableAlert = enableAlert; }
    
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}

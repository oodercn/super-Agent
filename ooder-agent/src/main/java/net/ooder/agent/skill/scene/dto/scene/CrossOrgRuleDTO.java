package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class CrossOrgRuleDTO {
    private String sourceOrg;
    private String targetOrg;
    private List<String> allowedCapabilities;

    public String getSourceOrg() { return sourceOrg; }
    public void setSourceOrg(String sourceOrg) { this.sourceOrg = sourceOrg; }
    public String getTargetOrg() { return targetOrg; }
    public void setTargetOrg(String targetOrg) { this.targetOrg = targetOrg; }
    public List<String> getAllowedCapabilities() { return allowedCapabilities; }
    public void setAllowedCapabilities(List<String> allowedCapabilities) { this.allowedCapabilities = allowedCapabilities; }
}

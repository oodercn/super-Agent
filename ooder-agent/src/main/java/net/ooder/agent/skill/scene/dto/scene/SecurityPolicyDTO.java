package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class SecurityPolicyDTO {
    private List<DataIsolationRuleDTO> dataIsolation;
    private List<CrossOrgRuleDTO> crossOrgRules;
    private AuditLoggingConfigDTO auditLogging;

    public List<DataIsolationRuleDTO> getDataIsolation() { return dataIsolation; }
    public void setDataIsolation(List<DataIsolationRuleDTO> dataIsolation) { this.dataIsolation = dataIsolation; }
    public List<CrossOrgRuleDTO> getCrossOrgRules() { return crossOrgRules; }
    public void setCrossOrgRules(List<CrossOrgRuleDTO> crossOrgRules) { this.crossOrgRules = crossOrgRules; }
    public AuditLoggingConfigDTO getAuditLogging() { return auditLogging; }
    public void setAuditLogging(AuditLoggingConfigDTO auditLogging) { this.auditLogging = auditLogging; }
}

package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class CoordinationConfigDTO {
    private CoordinationType type;
    private List<CoordinationRuleDTO> rules;
    private ConflictResolution conflictResolution;
    private long timeout;

    public CoordinationType getType() { return type; }
    public void setType(CoordinationType type) { this.type = type; }
    public List<CoordinationRuleDTO> getRules() { return rules; }
    public void setRules(List<CoordinationRuleDTO> rules) { this.rules = rules; }
    public ConflictResolution getConflictResolution() { return conflictResolution; }
    public void setConflictResolution(ConflictResolution conflictResolution) { this.conflictResolution = conflictResolution; }
    public long getTimeout() { return timeout; }
    public void setTimeout(long timeout) { this.timeout = timeout; }
}

package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class OrchestrationDef {
    
    private String orchestrationId;
    private String name;
    private String description;
    private OrchestrationType type;
    private List<OrchestrationStep> steps;
    private Map<String, Object> variables;
    private ErrorHandling errorHandling;
    private long createdTime;
    private long updatedTime;
    private OrchestrationStatus status;
    
    public String getOrchestrationId() { return orchestrationId; }
    public void setOrchestrationId(String orchestrationId) { this.orchestrationId = orchestrationId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public OrchestrationType getType() { return type; }
    public void setType(OrchestrationType type) { this.type = type; }
    
    public List<OrchestrationStep> getSteps() { return steps; }
    public void setSteps(List<OrchestrationStep> steps) { this.steps = steps; }
    
    public Map<String, Object> getVariables() { return variables; }
    public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    
    public ErrorHandling getErrorHandling() { return errorHandling; }
    public void setErrorHandling(ErrorHandling errorHandling) { this.errorHandling = errorHandling; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public long getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(long updatedTime) { this.updatedTime = updatedTime; }
    
    public OrchestrationStatus getStatus() { return status; }
    public void setStatus(OrchestrationStatus status) { this.status = status; }
}

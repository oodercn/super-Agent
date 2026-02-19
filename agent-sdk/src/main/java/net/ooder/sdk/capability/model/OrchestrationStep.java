package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class OrchestrationStep {
    
    private String stepId;
    private String name;
    private String capabilityId;
    private Map<String, Object> parameters;
    private List<String> dependencies;
    private StepCondition condition;
    private RetryPolicy retryPolicy;
    private OutputMapping outputMapping;
    
    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    
    public StepCondition getCondition() { return condition; }
    public void setCondition(StepCondition condition) { this.condition = condition; }
    
    public RetryPolicy getRetryPolicy() { return retryPolicy; }
    public void setRetryPolicy(RetryPolicy retryPolicy) { this.retryPolicy = retryPolicy; }
    
    public OutputMapping getOutputMapping() { return outputMapping; }
    public void setOutputMapping(OutputMapping outputMapping) { this.outputMapping = outputMapping; }
    
    private String action;
    private String onError;
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getOnError() { return onError; }
    public void setOnError(String onError) { this.onError = onError; }
}

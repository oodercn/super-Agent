package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class DistRequest {
    
    private String specId;
    private List<String> targetNodes;
    private DistStrategy strategy;
    private int priority;
    private Map<String, Object> parameters;
    
    public String getSpecId() { return specId; }
    public void setSpecId(String specId) { this.specId = specId; }
    
    public List<String> getTargetNodes() { return targetNodes; }
    public void setTargetNodes(List<String> targetNodes) { this.targetNodes = targetNodes; }
    
    public DistStrategy getStrategy() { return strategy; }
    public void setStrategy(DistStrategy strategy) { this.strategy = strategy; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
}

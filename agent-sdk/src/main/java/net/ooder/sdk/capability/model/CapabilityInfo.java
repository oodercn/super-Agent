package net.ooder.sdk.capability.model;

import java.util.Map;

public class CapabilityInfo {
    
    private String capabilityId;
    private String specId;
    private String specName;
    private String version;
    private String nodeId;
    private CapabilityState state;
    private long registeredTime;
    private long lastActiveTime;
    private int executionCount;
    private Map<String, Object> config;
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getSpecId() { return specId; }
    public void setSpecId(String specId) { this.specId = specId; }
    
    public String getSpecName() { return specName; }
    public void setSpecName(String specName) { this.specName = specName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public CapabilityState getState() { return state; }
    public void setState(CapabilityState state) { this.state = state; }
    
    public long getRegisteredTime() { return registeredTime; }
    public void setRegisteredTime(long registeredTime) { this.registeredTime = registeredTime; }
    
    public long getLastActiveTime() { return lastActiveTime; }
    public void setLastActiveTime(long lastActiveTime) { this.lastActiveTime = lastActiveTime; }
    
    public int getExecutionCount() { return executionCount; }
    public void setExecutionCount(int executionCount) { this.executionCount = executionCount; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
}

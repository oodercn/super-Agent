package net.ooder.sdk.capability.model;

import java.util.Map;

public class CapabilityRegistration {
    
    private String specId;
    private String nodeId;
    private String installPath;
    private Map<String, Object> config;
    
    public String getSpecId() { return specId; }
    public void setSpecId(String specId) { this.specId = specId; }
    
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public String getInstallPath() { return installPath; }
    public void setInstallPath(String installPath) { this.installPath = installPath; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
}

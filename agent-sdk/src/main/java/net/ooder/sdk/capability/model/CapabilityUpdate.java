package net.ooder.sdk.capability.model;

import java.util.Map;

public class CapabilityUpdate {
    
    private String version;
    private Map<String, Object> config;
    private String reason;
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

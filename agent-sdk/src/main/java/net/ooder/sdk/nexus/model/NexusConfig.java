package net.ooder.sdk.nexus.model;

import java.util.Map;

public class NexusConfig {
    
    private String nodeId;
    private String nodeName;
    private boolean autoStart;
    private boolean autoLogin;
    private boolean offlineEnabled;
    private int discoveryTimeout;
    private Map<String, Object> properties;
    
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    
    public boolean isAutoStart() { return autoStart; }
    public void setAutoStart(boolean autoStart) { this.autoStart = autoStart; }
    
    public boolean isAutoLogin() { return autoLogin; }
    public void setAutoLogin(boolean autoLogin) { this.autoLogin = autoLogin; }
    
    public boolean isOfflineEnabled() { return offlineEnabled; }
    public void setOfflineEnabled(boolean offlineEnabled) { this.offlineEnabled = offlineEnabled; }
    
    public int getDiscoveryTimeout() { return discoveryTimeout; }
    public void setDiscoveryTimeout(int discoveryTimeout) { this.discoveryTimeout = discoveryTimeout; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}

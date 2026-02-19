package net.ooder.sdk.southbound.protocol.model;

import java.util.Map;

public class RoleContext {
    
    private String agentId;
    private boolean hasMcp;
    private boolean hasNetwork;
    private boolean hasDomain;
    private int peerCount;
    private Map<String, Object> properties;
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public boolean isHasMcp() { return hasMcp; }
    public void setHasMcp(boolean hasMcp) { this.hasMcp = hasMcp; }
    
    public boolean isHasNetwork() { return hasNetwork; }
    public void setHasNetwork(boolean hasNetwork) { this.hasNetwork = hasNetwork; }
    
    public boolean isHasDomain() { return hasDomain; }
    public void setHasDomain(boolean hasDomain) { this.hasDomain = hasDomain; }
    
    public int getPeerCount() { return peerCount; }
    public void setPeerCount(int peerCount) { this.peerCount = peerCount; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}

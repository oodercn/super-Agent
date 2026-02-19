package net.ooder.sdk.southbound.protocol.model;

import java.util.Map;

public class JoinRequest {
    
    private String agentId;
    private String inviteCode;
    private Map<String, Object> capabilities;
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public String getInviteCode() { return inviteCode; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
    
    public Map<String, Object> getCapabilities() { return capabilities; }
    public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
}

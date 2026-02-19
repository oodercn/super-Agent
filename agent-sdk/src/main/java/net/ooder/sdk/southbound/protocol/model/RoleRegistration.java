package net.ooder.sdk.southbound.protocol.model;

import java.util.Map;

public class RoleRegistration {
    
    private String agentId;
    private RoleType roleType;
    private String mcpId;
    private String domainId;
    private Map<String, Object> capabilities;
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public RoleType getRoleType() { return roleType; }
    public void setRoleType(RoleType roleType) { this.roleType = roleType; }
    
    public String getMcpId() { return mcpId; }
    public void setMcpId(String mcpId) { this.mcpId = mcpId; }
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    
    public Map<String, Object> getCapabilities() { return capabilities; }
    public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
}

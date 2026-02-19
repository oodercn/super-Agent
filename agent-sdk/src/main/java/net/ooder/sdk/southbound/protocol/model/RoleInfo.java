package net.ooder.sdk.southbound.protocol.model;

public class RoleInfo {
    
    private String agentId;
    private RoleType roleType;
    private RoleStatus status;
    private String mcpId;
    private String domainId;
    private long registeredTime;
    private long lastHeartbeat;
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public RoleType getRoleType() { return roleType; }
    public void setRoleType(RoleType roleType) { this.roleType = roleType; }
    
    public RoleStatus getStatus() { return status; }
    public void setStatus(RoleStatus status) { this.status = status; }
    
    public String getMcpId() { return mcpId; }
    public void setMcpId(String mcpId) { this.mcpId = mcpId; }
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    
    public long getRegisteredTime() { return registeredTime; }
    public void setRegisteredTime(long registeredTime) { this.registeredTime = registeredTime; }
    
    public long getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
}

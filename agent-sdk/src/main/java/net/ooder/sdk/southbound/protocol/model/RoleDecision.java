package net.ooder.sdk.southbound.protocol.model;

import java.util.Map;

public class RoleDecision {
    
    private String agentId;
    private RoleType decidedRole;
    private String reason;
    private RoleContext context;
    private long decidedTime;
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public RoleType getDecidedRole() { return decidedRole; }
    public void setDecidedRole(RoleType decidedRole) { this.decidedRole = decidedRole; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public RoleContext getContext() { return context; }
    public void setContext(RoleContext context) { this.context = context; }
    
    public long getDecidedTime() { return decidedTime; }
    public void setDecidedTime(long decidedTime) { this.decidedTime = decidedTime; }
}

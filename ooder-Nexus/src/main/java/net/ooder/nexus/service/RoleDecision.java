package net.ooder.nexus.service;

/**
 * 角色决策
 */
public class RoleDecision {
    
    public enum Role {
        NEXUS,
        AGENT,
        OBSERVER,
        COORDINATOR
    }
    
    private Role role;
    private String reason;
    private long decisionTime;
    private boolean canChange;

    public RoleDecision() {
        this.decisionTime = System.currentTimeMillis();
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public long getDecisionTime() { return decisionTime; }
    public void setDecisionTime(long decisionTime) { this.decisionTime = decisionTime; }
    
    public boolean isCanChange() { return canChange; }
    public void setCanChange(boolean canChange) { this.canChange = canChange; }
}

package net.ooder.skill.config.dto;

public class SecurityStatsDTO {
    
    private int threatCount;
    private int policyCount;
    private long lastAuditTime;
    private int blockedAttempts;
    private int activeSessions;

    public int getThreatCount() { return threatCount; }
    public void setThreatCount(int threatCount) { this.threatCount = threatCount; }
    public int getPolicyCount() { return policyCount; }
    public void setPolicyCount(int policyCount) { this.policyCount = policyCount; }
    public long getLastAuditTime() { return lastAuditTime; }
    public void setLastAuditTime(long lastAuditTime) { this.lastAuditTime = lastAuditTime; }
    public int getBlockedAttempts() { return blockedAttempts; }
    public void setBlockedAttempts(int blockedAttempts) { this.blockedAttempts = blockedAttempts; }
    public int getActiveSessions() { return activeSessions; }
    public void setActiveSessions(int activeSessions) { this.activeSessions = activeSessions; }
}

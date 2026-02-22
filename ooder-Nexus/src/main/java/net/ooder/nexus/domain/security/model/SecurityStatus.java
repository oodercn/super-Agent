package net.ooder.nexus.domain.security.model;

/**
 * 安全状态实体Bean
 * 
 * 用于表示系统的安全状态信息
 * 
 * @version 1.0.0
 */
public class SecurityStatus {
    
    private String status;
    private String description;
    private int userCount;
    private int activeSessions;
    private boolean authenticationEnabled;
    private boolean authorizationEnabled;
    private boolean encryptionEnabled;
    private boolean auditLoggingEnabled;
    private long lastUpdated;
    
    public SecurityStatus() {
    }
    
    public SecurityStatus(String status, String description, int userCount, int activeSessions, 
                         boolean authenticationEnabled, boolean authorizationEnabled, 
                         boolean encryptionEnabled, boolean auditLoggingEnabled, long lastUpdated) {
        this.status = status;
        this.description = description;
        this.userCount = userCount;
        this.activeSessions = activeSessions;
        this.authenticationEnabled = authenticationEnabled;
        this.authorizationEnabled = authorizationEnabled;
        this.encryptionEnabled = encryptionEnabled;
        this.auditLoggingEnabled = auditLoggingEnabled;
        this.lastUpdated = lastUpdated;
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getUserCount() { return userCount; }
    public void setUserCount(int userCount) { this.userCount = userCount; }
    public int getActiveSessions() { return activeSessions; }
    public void setActiveSessions(int activeSessions) { this.activeSessions = activeSessions; }
    public boolean isAuthenticationEnabled() { return authenticationEnabled; }
    public void setAuthenticationEnabled(boolean authenticationEnabled) { this.authenticationEnabled = authenticationEnabled; }
    public boolean isAuthorizationEnabled() { return authorizationEnabled; }
    public void setAuthorizationEnabled(boolean authorizationEnabled) { this.authorizationEnabled = authorizationEnabled; }
    public boolean isEncryptionEnabled() { return encryptionEnabled; }
    public void setEncryptionEnabled(boolean encryptionEnabled) { this.encryptionEnabled = encryptionEnabled; }
    public boolean isAuditLoggingEnabled() { return auditLoggingEnabled; }
    public void setAuditLoggingEnabled(boolean auditLoggingEnabled) { this.auditLoggingEnabled = auditLoggingEnabled; }
    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
}

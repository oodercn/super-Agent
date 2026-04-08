package net.ooder.skill.config.dto;

public class SecurityConfigDTO {
    
    private boolean enableAuth;
    private boolean enableEncryption;
    private boolean enableAudit;
    private int sessionTimeout;
    private int maxLoginAttempts;
    private int keyRotationDays;
    private boolean enableFirewall;
    private String firewallMode;
    private boolean enableAgentAuth;
    private boolean enableAgentEncryption;
    private boolean enableAgentIsolation;
    private int llmRateLimit;
    private int costAlertThreshold;

    public boolean isEnableAuth() { return enableAuth; }
    public void setEnableAuth(boolean enableAuth) { this.enableAuth = enableAuth; }
    public boolean isEnableEncryption() { return enableEncryption; }
    public void setEnableEncryption(boolean enableEncryption) { this.enableEncryption = enableEncryption; }
    public boolean isEnableAudit() { return enableAudit; }
    public void setEnableAudit(boolean enableAudit) { this.enableAudit = enableAudit; }
    public int getSessionTimeout() { return sessionTimeout; }
    public void setSessionTimeout(int sessionTimeout) { this.sessionTimeout = sessionTimeout; }
    public int getMaxLoginAttempts() { return maxLoginAttempts; }
    public void setMaxLoginAttempts(int maxLoginAttempts) { this.maxLoginAttempts = maxLoginAttempts; }
    public int getKeyRotationDays() { return keyRotationDays; }
    public void setKeyRotationDays(int keyRotationDays) { this.keyRotationDays = keyRotationDays; }
    public boolean isEnableFirewall() { return enableFirewall; }
    public void setEnableFirewall(boolean enableFirewall) { this.enableFirewall = enableFirewall; }
    public String getFirewallMode() { return firewallMode; }
    public void setFirewallMode(String firewallMode) { this.firewallMode = firewallMode; }
    public boolean isEnableAgentAuth() { return enableAgentAuth; }
    public void setEnableAgentAuth(boolean enableAgentAuth) { this.enableAgentAuth = enableAgentAuth; }
    public boolean isEnableAgentEncryption() { return enableAgentEncryption; }
    public void setEnableAgentEncryption(boolean enableAgentEncryption) { this.enableAgentEncryption = enableAgentEncryption; }
    public boolean isEnableAgentIsolation() { return enableAgentIsolation; }
    public void setEnableAgentIsolation(boolean enableAgentIsolation) { this.enableAgentIsolation = enableAgentIsolation; }
    public int getLlmRateLimit() { return llmRateLimit; }
    public void setLlmRateLimit(int llmRateLimit) { this.llmRateLimit = llmRateLimit; }
    public int getCostAlertThreshold() { return costAlertThreshold; }
    public void setCostAlertThreshold(int costAlertThreshold) { this.costAlertThreshold = costAlertThreshold; }
}

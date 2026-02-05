package net.ooder.nexus.model.config;

import java.io.Serializable;

public class SecurityConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean enableAuthentication;
    private boolean enableAuthorization;
    private boolean enableEncryption;
    private boolean enableAuditLogging;
    private int sessionTimeout;
    private int maxLoginAttempts;
    private int lockoutDuration;
    private String encryptionAlgorithm;
    private long lastUpdated;

    public SecurityConfig() {
    }

    public SecurityConfig(boolean enableAuthentication, boolean enableAuthorization, boolean enableEncryption, boolean enableAuditLogging, int sessionTimeout, int maxLoginAttempts, int lockoutDuration, String encryptionAlgorithm) {
        this.enableAuthentication = enableAuthentication;
        this.enableAuthorization = enableAuthorization;
        this.enableEncryption = enableEncryption;
        this.enableAuditLogging = enableAuditLogging;
        this.sessionTimeout = sessionTimeout;
        this.maxLoginAttempts = maxLoginAttempts;
        this.lockoutDuration = lockoutDuration;
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.lastUpdated = System.currentTimeMillis();
    }

    public SecurityConfig(boolean enableAuthentication, boolean enableAuthorization, boolean enableEncryption, boolean enableAuditLogging, int sessionTimeout, int maxLoginAttempts, int lockoutDuration, String encryptionAlgorithm, long lastUpdated) {
        this.enableAuthentication = enableAuthentication;
        this.enableAuthorization = enableAuthorization;
        this.enableEncryption = enableEncryption;
        this.enableAuditLogging = enableAuditLogging;
        this.sessionTimeout = sessionTimeout;
        this.maxLoginAttempts = maxLoginAttempts;
        this.lockoutDuration = lockoutDuration;
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.lastUpdated = lastUpdated;
    }

    public boolean isEnableAuthentication() {
        return enableAuthentication;
    }

    public void setEnableAuthentication(boolean enableAuthentication) {
        this.enableAuthentication = enableAuthentication;
    }

    public boolean isEnableAuthorization() {
        return enableAuthorization;
    }

    public void setEnableAuthorization(boolean enableAuthorization) {
        this.enableAuthorization = enableAuthorization;
    }

    public boolean isEnableEncryption() {
        return enableEncryption;
    }

    public void setEnableEncryption(boolean enableEncryption) {
        this.enableEncryption = enableEncryption;
    }

    public boolean isEnableAuditLogging() {
        return enableAuditLogging;
    }

    public void setEnableAuditLogging(boolean enableAuditLogging) {
        this.enableAuditLogging = enableAuditLogging;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public int getLockoutDuration() {
        return lockoutDuration;
    }

    public void setLockoutDuration(int lockoutDuration) {
        this.lockoutDuration = lockoutDuration;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

package net.ooder.sdk.southbound.protocol.model;

public class Credential {
    
    private String userId;
    private String credential;
    private long savedAt;
    private long expiresAt;
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getCredential() { return credential; }
    public void setCredential(String credential) { this.credential = credential; }
    
    public long getSavedAt() { return savedAt; }
    public void setSavedAt(long savedAt) { this.savedAt = savedAt; }
    
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
}

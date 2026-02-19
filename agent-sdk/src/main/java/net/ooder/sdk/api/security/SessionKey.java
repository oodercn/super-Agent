package net.ooder.sdk.api.security;

public class SessionKey {
    
    private String sessionId;
    private String peerId;
    private String key;
    private long createdAt;
    private long expiresAt;
    private SessionKeyStatus status;
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public String getPeerId() { return peerId; }
    public void setPeerId(String peerId) { this.peerId = peerId; }
    
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    
    public SessionKeyStatus getStatus() { return status; }
    public void setStatus(SessionKeyStatus status) { this.status = status; }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }
    
    public boolean isValid() {
        return status == SessionKeyStatus.ACTIVE && !isExpired();
    }
}

package net.ooder.sdk.southbound.protocol.model;

import java.util.List;
import java.util.Map;

public class LoginResult {
    
    private boolean success;
    private String sessionId;
    private String userId;
    private String userName;
    private String token;
    private long expiresAt;
    private List<DomainInfo> domains;
    private DomainPolicy policy;
    private String errorMessage;
    private String errorCode;
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    
    public List<DomainInfo> getDomains() { return domains; }
    public void setDomains(List<DomainInfo> domains) { this.domains = domains; }
    
    public DomainPolicy getPolicy() { return policy; }
    public void setPolicy(DomainPolicy policy) { this.policy = policy; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
}

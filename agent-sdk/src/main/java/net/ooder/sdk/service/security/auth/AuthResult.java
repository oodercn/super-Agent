
package net.ooder.sdk.service.security.auth;

public class AuthResult {
    
    private boolean success;
    private String agentId;
    private String token;
    private String error;
    private long expireTime;
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public long getExpireTime() { return expireTime; }
    public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
}

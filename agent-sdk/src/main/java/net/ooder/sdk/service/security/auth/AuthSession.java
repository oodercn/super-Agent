
package net.ooder.sdk.service.security.auth;

public class AuthSession {
    
    private String agentId;
    private String token;
    private long createTime;
    private long expireTime;
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getExpireTime() { return expireTime; }
    public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }
}

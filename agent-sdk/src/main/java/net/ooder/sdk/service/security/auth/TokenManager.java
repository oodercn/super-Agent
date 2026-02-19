
package net.ooder.sdk.service.security.auth;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenManager {
    
    private static final Logger log = LoggerFactory.getLogger(TokenManager.class);
    
    private final SecureRandom random;
    private final Map<String, TokenInfo> tokens;
    private long tokenTtl = 3600000;
    
    public TokenManager() {
        this.random = new SecureRandom();
        this.tokens = new ConcurrentHashMap<>();
    }
    
    public String generateToken(String agentId) {
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        
        TokenInfo info = new TokenInfo();
        info.setAgentId(agentId);
        info.setCreateTime(System.currentTimeMillis());
        info.setExpireTime(System.currentTimeMillis() + tokenTtl);
        
        tokens.put(token, info);
        
        log.debug("Generated token for agent: {}", agentId);
        
        return token;
    }
    
    public boolean validateToken(String token) {
        TokenInfo info = tokens.get(token);
        if (info == null) {
            return false;
        }
        
        if (System.currentTimeMillis() > info.getExpireTime()) {
            tokens.remove(token);
            return false;
        }
        
        return true;
    }
    
    public void invalidateToken(String token) {
        tokens.remove(token);
        log.debug("Token invalidated");
    }
    
    public String getAgentId(String token) {
        TokenInfo info = tokens.get(token);
        return info != null ? info.getAgentId() : null;
    }
    
    public void setTokenTtl(long ttlMs) {
        this.tokenTtl = ttlMs;
    }
    
    public void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        tokens.entrySet().removeIf(entry -> entry.getValue().getExpireTime() < now);
    }
    
    private static class TokenInfo {
        private String agentId;
        private long createTime;
        private long expireTime;
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public long getExpireTime() { return expireTime; }
        public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
    }
}

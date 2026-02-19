
package net.ooder.sdk.service.security.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthManager {
    
    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);
    
    private final TokenManager tokenManager;
    private final Map<String, AuthProvider> providers;
    private final Map<String, AuthSession> sessions;
    
    public AuthManager() {
        this.tokenManager = new TokenManager();
        this.providers = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
    }
    
    public void registerProvider(String type, AuthProvider provider) {
        providers.put(type, provider);
        log.info("Registered auth provider: {}", type);
    }
    
    public AuthResult authenticate(String providerType, String agentId, String credentials) {
        AuthProvider provider = providers.get(providerType);
        if (provider == null) {
            AuthResult result = new AuthResult();
            result.setSuccess(false);
            result.setError("Provider not found: " + providerType);
            return result;
        }
        
        AuthResult result = provider.authenticate(agentId, credentials);
        if (result.isSuccess()) {
            String token = tokenManager.generateToken(agentId);
            result.setToken(token);
            
            AuthSession session = new AuthSession();
            session.setAgentId(agentId);
            session.setToken(token);
            session.setCreateTime(System.currentTimeMillis());
            session.setExpireTime(result.getExpireTime());
            sessions.put(token, session);
            
            log.info("Agent authenticated: {}", agentId);
        }
        
        return result;
    }
    
    public boolean validateToken(String token) {
        return tokenManager.validateToken(token);
    }
    
    public String getAgentId(String token) {
        return tokenManager.getAgentId(token);
    }
    
    public void logout(String token) {
        tokenManager.invalidateToken(token);
        sessions.remove(token);
        log.info("Agent logged out");
    }
    
    public AuthSession getSession(String token) {
        return sessions.get(token);
    }
    
    public void cleanup() {
        tokenManager.cleanupExpiredTokens();
        sessions.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}

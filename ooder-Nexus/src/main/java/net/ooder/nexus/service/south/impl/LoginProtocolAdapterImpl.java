package net.ooder.nexus.service.south.impl;

import net.ooder.nexus.service.south.LoginProtocolAdapter;
import net.ooder.sdk.api.security.SecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LoginProtocolAdapterImpl implements LoginProtocolAdapter {

    private static final Logger log = LoggerFactory.getLogger(LoginProtocolAdapterImpl.class);

    private static final long SESSION_TIMEOUT = 86400000;

    private final SecurityService securityService;
    private final List<LoginEventListener> listeners = new CopyOnWriteArrayList<LoginEventListener>();
    private final Map<String, SessionDTO> sessions = new ConcurrentHashMap<String, SessionDTO>();
    private final Map<String, LoginResultDTO> loginResults = new ConcurrentHashMap<String, LoginResultDTO>();
    
    private SessionDTO currentSession;

    @Autowired
    public LoginProtocolAdapterImpl(@Autowired(required = false) SecurityService securityService) {
        this.securityService = securityService;
        log.info("LoginProtocolAdapter initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<LoginResultDTO> login(LoginRequestDTO request) {
        log.info("Login request for user: {}", request.getUsername());
        
        return CompletableFuture.supplyAsync(() -> {
            LoginResultDTO result = new LoginResultDTO();
            
            try {
                String userId = UUID.randomUUID().toString();
                String sessionId = UUID.randomUUID().toString();
                String token = UUID.randomUUID().toString().replace("-", "");
                
                SessionDTO session = new SessionDTO();
                session.setSessionId(sessionId);
                session.setUserId(userId);
                session.setUserName(request.getUsername());
                session.setDomainId(request.getDomain());
                session.setCreatedAt(System.currentTimeMillis());
                session.setExpiresAt(System.currentTimeMillis() + SESSION_TIMEOUT);
                session.setLastActiveAt(System.currentTimeMillis());
                session.setStatus("ACTIVE");
                
                sessions.put(sessionId, session);
                currentSession = session;
                
                result.setSuccess(true);
                result.setSessionId(sessionId);
                result.setUserId(userId);
                result.setUserName(request.getUsername());
                result.setToken(token);
                result.setExpiresAt(session.getExpiresAt());
                
                DomainDTO domain = new DomainDTO();
                domain.setDomainId("default");
                domain.setDomainName("Default Domain");
                domain.setDomainType("LOCAL");
                domain.setRole("ADMIN");
                result.setDomains(Arrays.asList(domain));
                
                DomainPolicyDTO policy = new DomainPolicyDTO();
                policy.setDomainId("default");
                policy.setAllowedSkills(Arrays.asList("*"));
                policy.setRequiredSkills(new ArrayList<String>());
                result.setPolicy(policy);
                
                loginResults.put(sessionId, result);
                
                notifyLoginSuccess(result);
                notifyPolicyApplied("default", policy);
                
                log.info("Login successful for user: {}", request.getUsername());
                
            } catch (Exception e) {
                log.error("Login failed", e);
                result.setSuccess(false);
                result.setErrorCode("LOGIN_FAILED");
                result.setErrorMessage(e.getMessage());
                
                notifyLoginFailure("LOGIN_FAILED", e.getMessage());
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<Void> logout(String sessionId) {
        log.info("Logout request for session: {}", sessionId);
        
        return CompletableFuture.runAsync(() -> {
            SessionDTO session = sessions.remove(sessionId);
            loginResults.remove(sessionId);
            
            if (session != null && session.equals(currentSession)) {
                currentSession = null;
            }
            
            notifyLogout(sessionId);
            log.info("Logout successful for session: {}", sessionId);
        });
    }

    @Override
    public CompletableFuture<LoginResultDTO> autoLogin(AutoLoginConfigDTO config) {
        log.info("Auto login request for user: {}", config.getUserId());
        
        return CompletableFuture.supplyAsync(() -> {
            LoginResultDTO result = new LoginResultDTO();
            
            for (Map.Entry<String, LoginResultDTO> entry : loginResults.entrySet()) {
                if (entry.getValue().getUserId().equals(config.getUserId())) {
                    SessionDTO session = sessions.get(entry.getKey());
                    if (session != null && !session.isExpired()) {
                        result.setSuccess(true);
                        result.setSessionId(entry.getKey());
                        result.setUserId(entry.getValue().getUserId());
                        result.setUserName(entry.getValue().getUserName());
                        result.setToken(entry.getValue().getToken());
                        result.setExpiresAt(session.getExpiresAt());
                        return result;
                    }
                }
            }
            
            result.setSuccess(false);
            result.setErrorCode("AUTO_LOGIN_FAILED");
            result.setErrorMessage("No valid session found");
            
            return result;
        });
    }

    @Override
    public CompletableFuture<SessionDTO> getSession(String sessionId) {
        log.info("Get session request: {}", sessionId);
        
        return CompletableFuture.supplyAsync(() -> {
            SessionDTO session = sessions.get(sessionId);
            if (session != null) {
                session.setLastActiveAt(System.currentTimeMillis());
            }
            return session;
        });
    }

    @Override
    public CompletableFuture<SessionDTO> validateSession(String sessionId) {
        log.info("Validate session request: {}", sessionId);
        
        return CompletableFuture.supplyAsync(() -> {
            SessionDTO session = sessions.get(sessionId);
            if (session == null) {
                return null;
            }
            
            if (session.isExpired()) {
                sessions.remove(sessionId);
                loginResults.remove(sessionId);
                notifySessionExpired(sessionId);
                return null;
            }
            
            session.setLastActiveAt(System.currentTimeMillis());
            return session;
        });
    }

    @Override
    public CompletableFuture<Void> refreshSession(String sessionId) {
        log.info("Refresh session request: {}", sessionId);
        
        return CompletableFuture.runAsync(() -> {
            SessionDTO session = sessions.get(sessionId);
            if (session != null) {
                session.setExpiresAt(System.currentTimeMillis() + SESSION_TIMEOUT);
                session.setLastActiveAt(System.currentTimeMillis());
                
                LoginResultDTO result = loginResults.get(sessionId);
                if (result != null) {
                    result.setExpiresAt(session.getExpiresAt());
                }
            }
        });
    }

    @Override
    public CompletableFuture<DomainPolicyDTO> getDomainPolicy(String userId) {
        log.info("Get domain policy for user: {}", userId);
        
        return CompletableFuture.supplyAsync(() -> {
            DomainPolicyDTO policy = new DomainPolicyDTO();
            policy.setDomainId("default");
            policy.setAllowedSkills(Arrays.asList("*"));
            policy.setRequiredSkills(new ArrayList<String>());
            
            Map<String, Object> storageConfig = new HashMap<String, Object>();
            storageConfig.put("maxSize", 1024 * 1024 * 1024);
            storageConfig.put("encryptionEnabled", true);
            policy.setStorageConfig(storageConfig);
            
            Map<String, Object> securityConfig = new HashMap<String, Object>();
            securityConfig.put("tokenExpiration", SESSION_TIMEOUT);
            securityConfig.put("encryptionAlgorithm", "AES-256");
            policy.setSecurityConfig(securityConfig);
            
            return policy;
        });
    }

    @Override
    public void addLoginListener(LoginEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLoginListener(LoginEventListener listener) {
        listeners.remove(listener);
    }

    public SessionDTO getCurrentSession() {
        return currentSession;
    }

    private void notifyLoginSuccess(LoginResultDTO result) {
        for (LoginEventListener listener : listeners) {
            try {
                listener.onLoginSuccess(result);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyLoginFailure(String errorCode, String errorMessage) {
        for (LoginEventListener listener : listeners) {
            try {
                listener.onLoginFailure(errorCode, errorMessage);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyLogout(String sessionId) {
        for (LoginEventListener listener : listeners) {
            try {
                listener.onLogout(sessionId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifySessionExpired(String sessionId) {
        for (LoginEventListener listener : listeners) {
            try {
                listener.onSessionExpired(sessionId);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyPolicyApplied(String domainId, DomainPolicyDTO policy) {
        for (LoginEventListener listener : listeners) {
            try {
                listener.onPolicyApplied(domainId, policy);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}

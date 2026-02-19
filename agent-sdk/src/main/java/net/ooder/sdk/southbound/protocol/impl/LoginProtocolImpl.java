package net.ooder.sdk.southbound.protocol.impl;

import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class LoginProtocolImpl implements LoginProtocol {
    
    private static final Logger log = LoggerFactory.getLogger(LoginProtocolImpl.class);
    
    private final Map<String, SessionInfo> sessions;
    private final Map<String, Credential> savedCredentials;
    private final Map<String, DomainPolicy> domainPolicies;
    private final List<LoginListener> listeners;
    private final ExecutorService executor;
    private final AtomicReference<SessionInfo> currentSession;
    
    public LoginProtocolImpl() {
        this.sessions = new ConcurrentHashMap<String, SessionInfo>();
        this.savedCredentials = new ConcurrentHashMap<String, Credential>();
        this.domainPolicies = new ConcurrentHashMap<String, DomainPolicy>();
        this.listeners = new CopyOnWriteArrayList<LoginListener>();
        this.executor = Executors.newCachedThreadPool();
        this.currentSession = new AtomicReference<SessionInfo>();
        log.info("LoginProtocolImpl initialized");
    }
    
    @Override
    public CompletableFuture<LoginResult> login(LoginRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Login attempt: username={}", request.getUsername());
            
            LoginResult result = new LoginResult();
            
            try {
                SessionInfo session = doLogin(request);
                
                result.setSuccess(true);
                result.setSessionId(session.getSessionId());
                result.setUserId(session.getUserId());
                result.setUserName(session.getUserName());
                result.setExpiresAt(session.getExpiresAt());
                
                sessions.put(session.getSessionId(), session);
                currentSession.set(session);
                
                if (session.getDomainId() != null) {
                    DomainPolicy policy = getDomainPolicy(session.getUserId()).join();
                    result.setPolicy(policy);
                    notifyPolicyApplied(session.getDomainId(), policy);
                }
                
                notifyLoginSuccess(result);
                log.info("Login successful: userId={}", session.getUserId());
            } catch (Exception e) {
                log.error("Login failed", e);
                result.setSuccess(false);
                result.setErrorCode("LOGIN_FAILED");
                result.setErrorMessage(e.getMessage());
                notifyLoginFailure("LOGIN_FAILED", e.getMessage());
            }
            
            return result;
        }, executor);
    }
    
    private SessionInfo doLogin(LoginRequest request) {
        SessionInfo session = new SessionInfo();
        session.setSessionId(UUID.randomUUID().toString());
        
        String userId = request.getUsername();
        if (userId == null || userId.isEmpty()) {
            userId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        }
        session.setUserId(userId);
        session.setUserName(request.getUsername());
        session.setCreatedAt(System.currentTimeMillis());
        session.setExpiresAt(System.currentTimeMillis() + 86400000);
        session.setLastActiveAt(System.currentTimeMillis());
        session.setStatus(SessionStatus.ACTIVE);
        
        if (request.getDomain() != null) {
            session.setDomainId(request.getDomain());
        }
        
        return session;
    }
    
    @Override
    public CompletableFuture<Void> logout(String sessionId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Logout: sessionId={}", sessionId);
            
            SessionInfo session = sessions.remove(sessionId);
            if (session != null) {
                session.setStatus(SessionStatus.LOGGED_OUT);
                if (currentSession.get() != null && currentSession.get().getSessionId().equals(sessionId)) {
                    currentSession.set(null);
                }
                notifyLogout(sessionId);
                log.info("Logout successful: sessionId={}", sessionId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<LoginResult> autoLogin(AutoLoginConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Auto login attempt: userId={}", config.getUserId());
            
            Credential credential = savedCredentials.get(config.getUserId());
            if (credential == null || credential.getExpiresAt() < System.currentTimeMillis()) {
                LoginResult result = new LoginResult();
                result.setSuccess(false);
                result.setErrorCode("NO_CREDENTIAL");
                result.setErrorMessage("No valid saved credential found");
                return result;
            }
            
            LoginRequest request = new LoginRequest();
            request.setUsername(config.getUserId());
            request.setDomain(config.getDomain());
            
            return login(request).join();
        }, executor);
    }
    
    @Override
    public CompletableFuture<SessionInfo> getSession(String sessionId) {
        return CompletableFuture.supplyAsync(() -> sessions.get(sessionId), executor);
    }
    
    @Override
    public CompletableFuture<SessionInfo> validateSession(String sessionId) {
        return CompletableFuture.supplyAsync(() -> {
            SessionInfo session = sessions.get(sessionId);
            if (session == null) {
                return null;
            }
            
            if (session.getExpiresAt() < System.currentTimeMillis()) {
                session.setStatus(SessionStatus.EXPIRED);
                notifySessionExpired(sessionId);
                return null;
            }
            
            session.setLastActiveAt(System.currentTimeMillis());
            return session;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> refreshSession(String sessionId) {
        return CompletableFuture.runAsync(() -> {
            SessionInfo session = sessions.get(sessionId);
            if (session != null) {
                session.setExpiresAt(System.currentTimeMillis() + 86400000);
                log.debug("Session refreshed: sessionId={}", sessionId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<DomainPolicy> getDomainPolicy(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            DomainPolicy policy = domainPolicies.get(userId);
            if (policy != null) {
                return policy;
            }
            
            policy = new DomainPolicy();
            policy.setDomainId("default-domain");
            
            List<String> allowedSkills = new ArrayList<String>();
            allowedSkills.add("skill-basic");
            allowedSkills.add("skill-common");
            policy.setAllowedSkills(allowedSkills);
            
            List<String> requiredSkills = new ArrayList<String>();
            policy.setRequiredSkills(requiredSkills);
            
            Map<String, Object> storageConfig = new HashMap<String, Object>();
            storageConfig.put("maxStorageMB", 1024);
            storageConfig.put("allowSync", true);
            storageConfig.put("userId", userId);
            storageConfig.put("createdAt", System.currentTimeMillis());
            storageConfig.put("updatedAt", System.currentTimeMillis());
            policy.setStorageConfig(storageConfig);
            
            Map<String, Object> securityConfig = new HashMap<String, Object>();
            securityConfig.put("sessionTimeoutMs", 86400000);
            securityConfig.put("maxLoginAttempts", 5);
            securityConfig.put("requireMfa", false);
            policy.setSecurityConfig(securityConfig);
            
            Map<String, Object> networkConfig = new HashMap<String, Object>();
            networkConfig.put("allowedNetworks", new ArrayList<String>());
            networkConfig.put("enableOfflineMode", true);
            policy.setNetworkConfig(networkConfig);
            
            domainPolicies.put(userId, policy);
            
            return policy;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveCredential(Credential credential) {
        return CompletableFuture.runAsync(() -> {
            savedCredentials.put(credential.getUserId(), credential);
            log.info("Credential saved for user: {}", credential.getUserId());
        }, executor);
    }
    
    @Override
    public CompletableFuture<Credential> loadCredential(String userId) {
        return CompletableFuture.supplyAsync(() -> savedCredentials.get(userId), executor);
    }
    
    @Override
    public CompletableFuture<Void> clearCredential(String userId) {
        return CompletableFuture.runAsync(() -> {
            savedCredentials.remove(userId);
            log.info("Credential cleared for user: {}", userId);
        }, executor);
    }
    
    @Override
    public void addLoginListener(LoginListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeLoginListener(LoginListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyLoginSuccess(LoginResult result) {
        for (LoginListener listener : listeners) {
            try {
                listener.onLoginSuccess(result);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyLoginFailure(String errorCode, String errorMessage) {
        for (LoginListener listener : listeners) {
            try {
                listener.onLoginFailure(errorCode, errorMessage);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyLogout(String sessionId) {
        for (LoginListener listener : listeners) {
            try {
                listener.onLogout(sessionId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySessionExpired(String sessionId) {
        for (LoginListener listener : listeners) {
            try {
                listener.onSessionExpired(sessionId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyPolicyApplied(String domainId, DomainPolicy policy) {
        for (LoginListener listener : listeners) {
            try {
                listener.onPolicyApplied(domainId, policy);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down LoginProtocol");
        executor.shutdown();
        sessions.clear();
        savedCredentials.clear();
        domainPolicies.clear();
        currentSession.set(null);
        log.info("LoginProtocol shutdown complete");
    }
    
    public SessionInfo getCurrentSession() {
        return currentSession.get();
    }
}

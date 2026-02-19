
package net.ooder.sdk.southbound.adapter.impl;

import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.southbound.adapter.LoginProtocolAdapter;
import net.ooder.sdk.southbound.adapter.model.LoginRequest;
import net.ooder.sdk.southbound.adapter.model.LoginResult;
import net.ooder.sdk.southbound.adapter.model.SessionInfo;
import net.ooder.sdk.southbound.protocol.LoginProtocol;
import net.ooder.sdk.southbound.protocol.model.AutoLoginConfig;
import net.ooder.sdk.southbound.protocol.model.DomainPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginProtocolAdapterImpl implements LoginProtocolAdapter {
    
    private static final Logger log = LoggerFactory.getLogger(LoginProtocolAdapterImpl.class);
    
    private final LoginProtocol loginProtocol;
    private volatile String currentSessionId;
    private volatile SessionInfo currentSession;
    
    public LoginProtocolAdapterImpl(LoginProtocol loginProtocol) {
        this.loginProtocol = loginProtocol;
        setupInternalListener();
    }
    
    private void setupInternalListener() {
        loginProtocol.addLoginListener(new net.ooder.sdk.southbound.protocol.model.LoginListener() {
            @Override
            public void onLoginSuccess(net.ooder.sdk.southbound.protocol.model.LoginResult result) {
                currentSessionId = result.getSessionId();
            }
            
            @Override
            public void onLoginFailure(String errorCode, String errorMessage) {
                currentSessionId = null;
                currentSession = null;
            }
            
            @Override
            public void onLogout(String sessionId) {
                if (sessionId != null && sessionId.equals(currentSessionId)) {
                    currentSessionId = null;
                    currentSession = null;
                }
            }
            
            @Override
            public void onSessionExpired(String sessionId) {
                if (sessionId != null && sessionId.equals(currentSessionId)) {
                    currentSessionId = null;
                    currentSession = null;
                }
            }
            
            @Override
            public void onPolicyApplied(String domainId, DomainPolicy policy) {
                log.debug("Policy applied for domain: {}", domainId);
            }
        });
    }
    
    @Override
    public CompletableFuture<LoginResult> login(LoginRequest request) {
        net.ooder.sdk.southbound.protocol.model.LoginRequest protocolRequest = convertToProtocolRequest(request);
        
        return loginProtocol.login(protocolRequest)
            .thenApply(this::convertToAdapterResult);
    }
    
    @Override
    public CompletableFuture<Void> logout(String sessionId) {
        return loginProtocol.logout(sessionId)
            .thenRun(() -> {
                if (sessionId != null && sessionId.equals(currentSessionId)) {
                    currentSessionId = null;
                    currentSession = null;
                }
            });
    }
    
    @Override
    public CompletableFuture<SessionInfo> getSession(String sessionId) {
        return loginProtocol.getSession(sessionId)
            .thenApply(this::convertToAdapterSession);
    }
    
    @Override
    public CompletableFuture<Boolean> validateSession(String sessionId) {
        return loginProtocol.validateSession(sessionId)
            .thenApply(session -> session != null && session.getStatus() != null);
    }
    
    @Override
    public CompletableFuture<Void> refreshSession(String sessionId) {
        return loginProtocol.refreshSession(sessionId);
    }
    
    @Override
    public CompletableFuture<LoginResult> autoLogin(String userId) {
        AutoLoginConfig config = new AutoLoginConfig();
        config.setUserId(userId);
        
        return loginProtocol.autoLogin(config)
            .thenApply(result -> {
                if (result.isSuccess()) {
                    currentSessionId = result.getSessionId();
                }
                return convertToAdapterResult(result);
            });
    }
    
    @Override
    public boolean isLoggedIn() {
        return currentSessionId != null;
    }
    
    @Override
    public String getCurrentSessionId() {
        return currentSessionId;
    }
    
    private net.ooder.sdk.southbound.protocol.model.LoginRequest convertToProtocolRequest(LoginRequest request) {
        net.ooder.sdk.southbound.protocol.model.LoginRequest protocolRequest = 
            new net.ooder.sdk.southbound.protocol.model.LoginRequest();
        protocolRequest.setUsername(request.getUserId());
        protocolRequest.setPassword(request.getPassword());
        protocolRequest.setDomain(request.getDomain());
        return protocolRequest;
    }
    
    private LoginResult convertToAdapterResult(net.ooder.sdk.southbound.protocol.model.LoginResult protocolResult) {
        LoginResult result = new LoginResult();
        result.setSuccess(protocolResult.isSuccess());
        result.setSessionId(protocolResult.getSessionId());
        result.setUserId(protocolResult.getUserId());
        result.setUserName(protocolResult.getUserName());
        result.setToken(protocolResult.getToken());
        result.setExpiresAt(protocolResult.getExpiresAt());
        result.setErrorCode(protocolResult.getErrorCode());
        result.setErrorMessage(protocolResult.getErrorMessage());
        
        if (protocolResult.isSuccess() && protocolResult.getSessionId() != null) {
            currentSessionId = protocolResult.getSessionId();
        }
        
        return result;
    }
    
    private SessionInfo convertToAdapterSession(net.ooder.sdk.southbound.protocol.model.SessionInfo protocolSession) {
        SessionInfo session = new SessionInfo();
        session.setSessionId(protocolSession.getSessionId());
        session.setUserId(protocolSession.getUserId());
        session.setUserName(protocolSession.getUserName());
        session.setDomain(protocolSession.getDomainId());
        session.setStatus(protocolSession.getStatus() != null ? protocolSession.getStatus().name() : "UNKNOWN");
        session.setCreatedAt(protocolSession.getCreatedAt());
        session.setLastActiveAt(protocolSession.getLastActiveAt());
        session.setExpiresAt(protocolSession.getExpiresAt());
        return session;
    }
}

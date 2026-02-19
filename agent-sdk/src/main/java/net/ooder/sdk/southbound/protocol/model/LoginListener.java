package net.ooder.sdk.southbound.protocol.model;

import net.ooder.sdk.southbound.protocol.model.DomainPolicy;

public interface LoginListener {
    
    void onLoginSuccess(LoginResult result);
    
    void onLoginFailure(String errorCode, String errorMessage);
    
    void onLogout(String sessionId);
    
    void onSessionExpired(String sessionId);
    
    void onPolicyApplied(String domainId, DomainPolicy policy);
}

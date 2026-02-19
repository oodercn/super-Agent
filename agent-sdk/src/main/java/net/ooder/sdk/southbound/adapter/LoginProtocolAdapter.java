
package net.ooder.sdk.southbound.adapter;

import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.southbound.adapter.model.LoginRequest;
import net.ooder.sdk.southbound.adapter.model.LoginResult;
import net.ooder.sdk.southbound.adapter.model.SessionInfo;

public interface LoginProtocolAdapter {
    
    CompletableFuture<LoginResult> login(LoginRequest request);
    
    CompletableFuture<Void> logout(String sessionId);
    
    CompletableFuture<SessionInfo> getSession(String sessionId);
    
    CompletableFuture<Boolean> validateSession(String sessionId);
    
    CompletableFuture<Void> refreshSession(String sessionId);
    
    CompletableFuture<LoginResult> autoLogin(String userId);
    
    boolean isLoggedIn();
    
    String getCurrentSessionId();
}

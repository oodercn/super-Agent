package net.ooder.sdk.southbound.protocol;

import net.ooder.sdk.southbound.protocol.model.*;
import java.util.concurrent.CompletableFuture;

public interface LoginProtocol {
    
    CompletableFuture<LoginResult> login(LoginRequest request);
    
    CompletableFuture<Void> logout(String sessionId);
    
    CompletableFuture<LoginResult> autoLogin(AutoLoginConfig config);
    
    CompletableFuture<SessionInfo> getSession(String sessionId);
    
    CompletableFuture<SessionInfo> validateSession(String sessionId);
    
    CompletableFuture<Void> refreshSession(String sessionId);
    
    CompletableFuture<DomainPolicy> getDomainPolicy(String userId);
    
    CompletableFuture<Void> saveCredential(Credential credential);
    
    CompletableFuture<Credential> loadCredential(String userId);
    
    CompletableFuture<Void> clearCredential(String userId);
    
    void addLoginListener(LoginListener listener);
    
    void removeLoginListener(LoginListener listener);
}

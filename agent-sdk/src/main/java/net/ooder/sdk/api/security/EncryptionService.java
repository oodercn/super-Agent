package net.ooder.sdk.api.security;

import java.util.concurrent.CompletableFuture;

public interface EncryptionService {
    
    CompletableFuture<SessionKey> generateSessionKey(String targetPeerId);
    
    CompletableFuture<byte[]> encryptWithSessionKey(String sessionId, byte[] data);
    
    CompletableFuture<byte[]> decryptWithSessionKey(String sessionId, byte[] data);
    
    CompletableFuture<Void> destroySessionKey(String sessionId);
    
    CompletableFuture<SessionKeyStatus> getSessionKeyStatus(String sessionId);
    
    void shutdown();
}

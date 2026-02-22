package net.ooder.nexus.service;

import net.ooder.sdk.api.security.KeyPair;
import net.ooder.sdk.api.security.TokenInfo;

import java.util.concurrent.CompletableFuture;

public interface NexusSecurityService {

    KeyPair generateKeyPair();
    
    String encrypt(String data, String publicKey);
    
    String decrypt(String encryptedData, String privateKey);
    
    CompletableFuture<String> encryptAsync(String data, String publicKey);
    
    CompletableFuture<String> decryptAsync(String encryptedData, String privateKey);
    
    String sign(String data, String privateKey);
    
    boolean verify(String data, String signature, String publicKey);
    
    String generateToken(String subject, long expireMs);
    
    TokenInfo validateToken(String token);
    
    void revokeToken(String token);
    
    KeyPair generateSceneKey(String sceneId);
    
    void rotateSceneKey(String sceneId);
    
    String encryptForPeer(String sceneId, String peerId, String data);
    
    String decryptFromPeer(String sceneId, String peerId, String encryptedData);
    
    boolean isHealthy();
}

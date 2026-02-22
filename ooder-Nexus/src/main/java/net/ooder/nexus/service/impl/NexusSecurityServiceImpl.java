package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.NexusSecurityService;
import net.ooder.sdk.api.security.SecurityService;
import net.ooder.sdk.api.security.KeyPair;
import net.ooder.sdk.api.security.TokenInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service("sdkSecurityService")
public class NexusSecurityServiceImpl implements NexusSecurityService {

    private static final Logger log = LoggerFactory.getLogger(NexusSecurityServiceImpl.class);

    private final SecurityService securityService;

    @Autowired
    public NexusSecurityServiceImpl(SecurityService securityService) {
        this.securityService = securityService;
        log.info("NexusSecurityServiceImpl initialized with SecurityService (SDK 0.7.1)");
    }

    @Override
    public KeyPair generateKeyPair() {
        log.info("Generating new key pair");
        return securityService.generateKeyPair();
    }

    @Override
    public String encrypt(String data, String publicKey) {
        log.info("Encrypting data with public key");
        return securityService.encrypt(data, publicKey);
    }

    @Override
    public String decrypt(String encryptedData, String privateKey) {
        log.info("Decrypting data with private key");
        return securityService.decrypt(encryptedData, privateKey);
    }

    @Override
    public CompletableFuture<String> encryptAsync(String data, String publicKey) {
        log.info("Encrypting data async");
        return securityService.encryptAsync(data, publicKey);
    }

    @Override
    public CompletableFuture<String> decryptAsync(String encryptedData, String privateKey) {
        log.info("Decrypting data async");
        return securityService.decryptAsync(encryptedData, privateKey);
    }

    @Override
    public String sign(String data, String privateKey) {
        log.info("Signing data");
        return securityService.sign(data, privateKey);
    }

    @Override
    public boolean verify(String data, String signature, String publicKey) {
        log.info("Verifying signature");
        return securityService.verify(data, signature, publicKey);
    }

    @Override
    public String generateToken(String subject, long expireMs) {
        log.info("Generating token for subject: {}", subject);
        return securityService.generateToken(subject, expireMs);
    }

    @Override
    public TokenInfo validateToken(String token) {
        return securityService.validateToken(token);
    }

    @Override
    public void revokeToken(String token) {
        log.info("Revoking token");
        securityService.revokeToken(token);
    }

    @Override
    public KeyPair generateSceneKey(String sceneId) {
        log.info("Generating scene key for: {}", sceneId);
        return securityService.generateSceneKey(sceneId);
    }

    @Override
    public void rotateSceneKey(String sceneId) {
        log.info("Rotating scene key for: {}", sceneId);
        securityService.rotateSceneKey(sceneId);
    }

    @Override
    public String encryptForPeer(String sceneId, String peerId, String data) {
        log.info("Encrypting for peer: {} in scene: {}", peerId, sceneId);
        return securityService.encryptForPeer(sceneId, peerId, data);
    }

    @Override
    public String decryptFromPeer(String sceneId, String peerId, String encryptedData) {
        log.info("Decrypting from peer: {} in scene: {}", peerId, sceneId);
        return securityService.decryptFromPeer(sceneId, peerId, encryptedData);
    }

    @Override
    public boolean isHealthy() {
        return securityService.isHealthy();
    }
}

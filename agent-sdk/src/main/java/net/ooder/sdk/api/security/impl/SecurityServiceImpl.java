package net.ooder.sdk.api.security.impl;

import net.ooder.sdk.api.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class SecurityServiceImpl implements SecurityService {
    
    private static final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);
    
    private final Map<String, KeyPair> keyPairs;
    private final Map<String, KeyPair> sceneKeys;
    private final Map<String, Map<String, String>> peerPublicKeys;
    private final Set<String> revokedTokens;
    private final ExecutorService executor;
    private final String secretKey;
    
    public SecurityServiceImpl() {
        this.keyPairs = new ConcurrentHashMap<String, KeyPair>();
        this.sceneKeys = new ConcurrentHashMap<String, KeyPair>();
        this.peerPublicKeys = new ConcurrentHashMap<String, Map<String, String>>();
        this.revokedTokens = ConcurrentHashMap.newKeySet();
        this.executor = Executors.newCachedThreadPool();
        this.secretKey = UUID.randomUUID().toString();
        log.info("SecurityServiceImpl initialized");
    }
    
    public SecurityServiceImpl(String secretKey) {
        this.keyPairs = new ConcurrentHashMap<String, KeyPair>();
        this.sceneKeys = new ConcurrentHashMap<String, KeyPair>();
        this.peerPublicKeys = new ConcurrentHashMap<String, Map<String, String>>();
        this.revokedTokens = ConcurrentHashMap.newKeySet();
        this.executor = Executors.newCachedThreadPool();
        this.secretKey = secretKey;
        log.info("SecurityServiceImpl initialized with custom secret key");
    }
    
    @Override
    public KeyPair generateKeyPair() {
        return generateKeyPair(365L * 24 * 60 * 60);
    }
    
    @Override
    public KeyPair generateKeyPair(long expiresInSeconds) {
        KeyPair keyPair = new KeyPair();
        keyPair.setPublicKey(generatePublicKey());
        keyPair.setPrivateKey(generatePrivateKey());
        keyPair.setAlgorithm("RSA");
        keyPair.setCreatedAt(System.currentTimeMillis());
        keyPair.setExpiresAt(System.currentTimeMillis() + expiresInSeconds * 1000);
        
        log.debug("Generated key pair");
        return keyPair;
    }
    
    @Override
    public String encrypt(String data, String publicKey) {
        return base64Encode(xorEncrypt(data, publicKey));
    }
    
    @Override
    public String decrypt(String encryptedData, String privateKey) {
        String decoded = base64Decode(encryptedData);
        return xorEncrypt(decoded, privateKey);
    }
    
    @Override
    public CompletableFuture<String> encryptAsync(String data, String publicKey) {
        return CompletableFuture.supplyAsync(() -> encrypt(data, publicKey), executor);
    }
    
    @Override
    public CompletableFuture<String> decryptAsync(String encryptedData, String privateKey) {
        return CompletableFuture.supplyAsync(() -> decrypt(encryptedData, privateKey), executor);
    }
    
    @Override
    public String sign(String data, String privateKey) {
        String combined = data + ":" + privateKey + ":" + secretKey;
        return base64Encode(hash(combined));
    }
    
    @Override
    public boolean verify(String data, String signature, String publicKey) {
        KeyPair keyPair = findKeyPairByPublicKey(publicKey);
        if (keyPair == null) return false;
        
        String expected = sign(data, keyPair.getPrivateKey());
        return expected.equals(signature);
    }
    
    @Override
    public String generateToken(String subject, long expireInMillis) {
        return generateToken(subject, new HashMap<String, Object>(), expireInMillis);
    }
    
    @Override
    public String generateToken(String subject, Map<String, Object> claims, long expireInMillis) {
        long now = System.currentTimeMillis();
        long expires = now + expireInMillis;
        
        StringBuilder token = new StringBuilder();
        token.append(base64Encode(subject)).append(".");
        token.append(base64Encode(String.valueOf(now))).append(".");
        token.append(base64Encode(String.valueOf(expires))).append(".");
        token.append(base64Encode(hash(subject + secretKey + expires)));
        
        return token.toString();
    }
    
    @Override
    public TokenInfo validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        if (isTokenRevoked(token)) {
            return null;
        }
        
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 4) {
                return null;
            }
            
            String subject = base64Decode(parts[0]);
            long issuedAt = Long.parseLong(base64Decode(parts[1]));
            long expiresAt = Long.parseLong(base64Decode(parts[2]));
            String signature = parts[3];
            
            String expectedSignature = base64Encode(hash(subject + secretKey + expiresAt));
            if (!expectedSignature.equals(signature)) {
                return null;
            }
            
            if (System.currentTimeMillis() > expiresAt) {
                return null;
            }
            
            TokenInfo info = new TokenInfo();
            info.setSubject(subject);
            info.setIssuedAt(issuedAt);
            info.setExpiresAt(expiresAt);
            info.setValid(true);
            
            return info;
        } catch (Exception e) {
            log.warn("Token validation failed", e);
            return null;
        }
    }
    
    @Override
    public void revokeToken(String token) {
        revokedTokens.add(token);
        log.debug("Token revoked");
    }
    
    @Override
    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }
    
    @Override
    public KeyPair generateSceneKey(String sceneId) {
        KeyPair keyPair = generateKeyPair();
        sceneKeys.put(sceneId, keyPair);
        peerPublicKeys.put(sceneId, new ConcurrentHashMap<String, String>());
        log.info("Generated scene key for: {}", sceneId);
        return keyPair;
    }
    
    @Override
    public KeyPair getSceneKey(String sceneId) {
        return sceneKeys.get(sceneId);
    }
    
    @Override
    public KeyPair rotateSceneKey(String sceneId) {
        return generateSceneKey(sceneId);
    }
    
    @Override
    public void removeSceneKey(String sceneId) {
        sceneKeys.remove(sceneId);
        peerPublicKeys.remove(sceneId);
        log.info("Removed scene key for: {}", sceneId);
    }
    
    @Override
    public boolean hasSceneKey(String sceneId) {
        return sceneKeys.containsKey(sceneId);
    }
    
    @Override
    public String encryptForPeer(String sceneId, String peerId, String data) {
        Map<String, String> peers = peerPublicKeys.get(sceneId);
        if (peers == null || !peers.containsKey(peerId)) {
            throw new RuntimeException("No public key registered for peer: " + peerId);
        }
        
        String peerPublicKey = peers.get(peerId);
        return encrypt(data, peerPublicKey);
    }
    
    @Override
    public String decryptFromPeer(String sceneId, String peerId, String encryptedData) {
        KeyPair sceneKey = sceneKeys.get(sceneId);
        if (sceneKey == null) {
            throw new RuntimeException("No scene key for: " + sceneId);
        }
        
        return decrypt(encryptedData, sceneKey.getPrivateKey());
    }
    
    @Override
    public void registerPeerPublicKey(String sceneId, String peerId, String publicKey) {
        Map<String, String> peers = peerPublicKeys.get(sceneId);
        if (peers == null) {
            peers = new ConcurrentHashMap<String, String>();
            peerPublicKeys.put(sceneId, peers);
        }
        peers.put(peerId, publicKey);
        log.debug("Registered public key for peer: {} in scene: {}", peerId, sceneId);
    }
    
    @Override
    public void removePeerPublicKey(String sceneId, String peerId) {
        Map<String, String> peers = peerPublicKeys.get(sceneId);
        if (peers != null) {
            peers.remove(peerId);
        }
    }
    
    @Override
    public void shutdown() {
        log.info("Shutting down SecurityService");
        executor.shutdown();
        keyPairs.clear();
        sceneKeys.clear();
        peerPublicKeys.clear();
        revokedTokens.clear();
        log.info("SecurityService shutdown complete");
    }
    
    @Override
    public boolean isHealthy() {
        return !executor.isShutdown();
    }
    
    private KeyPair findKeyPairByPublicKey(String publicKey) {
        for (KeyPair kp : keyPairs.values()) {
            if (kp.getPublicKey().equals(publicKey)) {
                return kp;
            }
        }
        return null;
    }
    
    private String generatePublicKey() {
        return "pub-" + UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
    
    private String generatePrivateKey() {
        return "priv-" + UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
    
    private String xorEncrypt(String data, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            result.append((char) (data.charAt(i) ^ key.charAt(i % key.length())));
        }
        return result.toString();
    }
    
    private String hash(String data) {
        int hash = 0;
        for (char c : data.toCharArray()) {
            hash = 31 * hash + c;
        }
        return Integer.toHexString(hash);
    }
    
    private String base64Encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    
    private String base64Decode(String data) {
        return new String(Base64.getDecoder().decode(data));
    }
}

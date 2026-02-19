package net.ooder.sdk.service.security;

import net.ooder.sdk.api.security.KeyPair;
import net.ooder.sdk.api.security.TokenInfo;
import net.ooder.sdk.api.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Security Service Implementation
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class SecurityServiceImpl implements SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private final Map<String, KeyPair> sceneKeys;
    private final Map<String, Map<String, String>> peerPublicKeys;
    private final Set<String> revokedTokens;
    private final ExecutorService executor;
    private final AtomicBoolean healthy;

    private KeyPair defaultKeyPair;

    public SecurityServiceImpl() {
        this.sceneKeys = new ConcurrentHashMap<String, KeyPair>();
        this.peerPublicKeys = new ConcurrentHashMap<String, Map<String, String>>();
        this.revokedTokens = ConcurrentHashMap.newKeySet();
        this.executor = Executors.newFixedThreadPool(4);
        this.healthy = new AtomicBoolean(true);
        
        try {
            this.defaultKeyPair = generateKeyPairInternal();
        } catch (Exception e) {
            log.error("Failed to generate default key pair", e);
        }
        
        log.info("SecurityServiceImpl initialized");
    }

    @Override
    public KeyPair generateKeyPair() {
        return generateKeyPair(0);
    }

    @Override
    public KeyPair generateKeyPair(long expiresInSeconds) {
        try {
            KeyPair keyPair = generateKeyPairInternal();
            if (expiresInSeconds > 0) {
                keyPair.setExpiresAt(System.currentTimeMillis() + expiresInSeconds * 1000);
            }
            return keyPair;
        } catch (Exception e) {
            log.error("Failed to generate key pair", e);
            throw new RuntimeException("Failed to generate key pair: " + e.getMessage(), e);
        }
    }

    private KeyPair generateKeyPairInternal() throws NoSuchAlgorithmException {
        java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(KEY_SIZE);
        java.security.KeyPair javaKeyPair = keyGen.generateKeyPair();

        KeyPair keyPair = new KeyPair();
        keyPair.setPublicKey(Base64.getEncoder().encodeToString(javaKeyPair.getPublic().getEncoded()));
        keyPair.setPrivateKey(Base64.getEncoder().encodeToString(javaKeyPair.getPrivate().getEncoded()));
        keyPair.setAlgorithm(ALGORITHM);
        keyPair.setCreatedAt(System.currentTimeMillis());

        return keyPair;
    }

    @Override
    public String encrypt(String data, String publicKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(spec);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String encryptedData, String privateKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privKey = keyFactory.generatePrivate(spec);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
        }
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
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privKey = keyFactory.generatePrivate(spec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            log.error("Signing failed", e);
            throw new RuntimeException("Signing failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verify(String data, String signatureStr, String publicKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(spec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(pubKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            return signature.verify(Base64.getDecoder().decode(signatureStr));
        } catch (Exception e) {
            log.error("Verification failed", e);
            return false;
        }
    }

    @Override
    public String generateToken(String subject, long expireInMillis) {
        return generateToken(subject, new HashMap<String, Object>(), expireInMillis);
    }

    @Override
    public String generateToken(String subject, Map<String, Object> claims, long expireInMillis) {
        long now = System.currentTimeMillis();
        long expiresAt = now + expireInMillis;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(Base64.getEncoder().encodeToString(subject.getBytes(StandardCharsets.UTF_8)));
        tokenBuilder.append(".");
        tokenBuilder.append(Base64.getEncoder().encodeToString(String.valueOf(now).getBytes(StandardCharsets.UTF_8)));
        tokenBuilder.append(".");
        tokenBuilder.append(Base64.getEncoder().encodeToString(String.valueOf(expiresAt).getBytes(StandardCharsets.UTF_8)));

        if (claims != null && !claims.isEmpty()) {
            tokenBuilder.append(".");
            String claimsStr = claims.toString();
            tokenBuilder.append(Base64.getEncoder().encodeToString(claimsStr.getBytes(StandardCharsets.UTF_8)));
        }

        String tokenData = tokenBuilder.toString();
        if (defaultKeyPair != null) {
            String signature = sign(tokenData, defaultKeyPair.getPrivateKey());
            return tokenData + "." + signature;
        }

        return tokenData;
    }

    @Override
    public TokenInfo validateToken(String token) {
        TokenInfo info = new TokenInfo();

        if (token == null || token.isEmpty()) {
            info.setValid(false);
            info.setError("Token is null or empty");
            return info;
        }

        if (isTokenRevoked(token)) {
            info.setValid(false);
            info.setError("Token has been revoked");
            return info;
        }

        try {
            String[] parts = token.split("\\.");
            if (parts.length < 3) {
                info.setValid(false);
                info.setError("Invalid token format");
                return info;
            }

            String subject = new String(Base64.getDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            long issuedAt = Long.parseLong(new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8));
            long expiresAt = Long.parseLong(new String(Base64.getDecoder().decode(parts[2]), StandardCharsets.UTF_8));

            info.setSubject(subject);
            info.setIssuedAt(issuedAt);
            info.setExpiresAt(expiresAt);

            if (System.currentTimeMillis() > expiresAt) {
                info.setValid(false);
                info.setError("Token has expired");
                return info;
            }

            info.setValid(true);
            return info;
        } catch (Exception e) {
            info.setValid(false);
            info.setError("Token validation failed: " + e.getMessage());
            return info;
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
        log.info("Generating scene key for: {}", sceneId);
        KeyPair keyPair = generateKeyPair(86400);
        sceneKeys.put(sceneId, keyPair);
        peerPublicKeys.put(sceneId, new ConcurrentHashMap<String, String>());
        log.info("Scene key generated for: {}", sceneId);
        return keyPair;
    }

    @Override
    public KeyPair getSceneKey(String sceneId) {
        return sceneKeys.get(sceneId);
    }

    @Override
    public KeyPair rotateSceneKey(String sceneId) {
        log.info("Rotating scene key for: {}", sceneId);
        return generateSceneKey(sceneId);
    }

    @Override
    public void removeSceneKey(String sceneId) {
        log.info("Removing scene key for: {}", sceneId);
        sceneKeys.remove(sceneId);
        peerPublicKeys.remove(sceneId);
    }

    @Override
    public boolean hasSceneKey(String sceneId) {
        return sceneKeys.containsKey(sceneId);
    }

    @Override
    public String encryptForPeer(String sceneId, String peerId, String data) {
        Map<String, String> scenePeerKeys = peerPublicKeys.get(sceneId);
        if (scenePeerKeys == null) {
            throw new IllegalArgumentException("Scene not found: " + sceneId);
        }

        String peerPublicKey = scenePeerKeys.get(peerId);
        if (peerPublicKey == null) {
            KeyPair sceneKey = sceneKeys.get(sceneId);
            if (sceneKey != null) {
                peerPublicKey = sceneKey.getPublicKey();
            } else {
                throw new IllegalArgumentException("No key available for peer: " + peerId);
            }
        }

        return encrypt(data, peerPublicKey);
    }

    @Override
    public String decryptFromPeer(String sceneId, String peerId, String encryptedData) {
        KeyPair sceneKey = sceneKeys.get(sceneId);
        if (sceneKey == null) {
            throw new IllegalArgumentException("Scene not found: " + sceneId);
        }

        return decrypt(encryptedData, sceneKey.getPrivateKey());
    }

    @Override
    public void registerPeerPublicKey(String sceneId, String peerId, String publicKey) {
        Map<String, String> scenePeerKeys = peerPublicKeys.computeIfAbsent(sceneId, k -> new ConcurrentHashMap<String, String>());
        scenePeerKeys.put(peerId, publicKey);
        log.debug("Registered public key for peer: {} in scene: {}", peerId, sceneId);
    }

    @Override
    public void removePeerPublicKey(String sceneId, String peerId) {
        Map<String, String> scenePeerKeys = peerPublicKeys.get(sceneId);
        if (scenePeerKeys != null) {
            scenePeerKeys.remove(peerId);
            log.debug("Removed public key for peer: {} in scene: {}", peerId, sceneId);
        }
    }

    @Override
    public void shutdown() {
        log.info("Shutting down SecurityService");
        executor.shutdown();
        sceneKeys.clear();
        peerPublicKeys.clear();
        revokedTokens.clear();
        healthy.set(false);
        log.info("SecurityService shutdown complete");
    }

    @Override
    public boolean isHealthy() {
        return healthy.get();
    }
}

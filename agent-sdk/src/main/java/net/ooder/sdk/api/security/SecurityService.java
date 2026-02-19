package net.ooder.sdk.api.security;

import java.util.concurrent.CompletableFuture;

/**
 * Security Service Interface
 *
 * <p>Provides security capabilities including:</p>
 * <ul>
 *   <li>Asymmetric encryption (RSA)</li>
 *   <li>Digital signatures</li>
 *   <li>Token management</li>
 *   <li>Scene group key management</li>
 *   <li>End-to-end encryption</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * SecurityService security = new SecurityServiceImpl();
 *
 * // Generate key pair
 * KeyPair keyPair = security.generateKeyPair();
 *
 * // Encrypt and decrypt
 * String encrypted = security.encrypt("secret data", keyPair.getPublicKey());
 * String decrypted = security.decrypt(encrypted, keyPair.getPrivateKey());
 *
 * // Sign and verify
 * String signature = security.sign("data to sign", keyPair.getPrivateKey());
 * boolean valid = security.verify("data to sign", signature, keyPair.getPublicKey());
 *
 * // Token management
 * String token = security.generateToken("user123", 3600000);
 * TokenInfo info = security.validateToken(token);
 *
 * // Scene group keys
 * KeyPair sceneKey = security.generateSceneKey("scene-001");
 * String encryptedForPeer = security.encryptForPeer("scene-001", "peer-002", "secret");
 * String decrypted = security.decryptFromPeer("scene-001", "peer-002", encryptedForPeer);
 * </pre>
 *
 * @author ooder Team
 * @since 0.7.1
 */
public interface SecurityService {

    // ==================== Key Management ====================

    /**
     * Generate a new key pair
     *
     * @return generated key pair
     */
    KeyPair generateKeyPair();

    /**
     * Generate a key pair with expiration
     *
     * @param expiresInSeconds expiration time in seconds
     * @return generated key pair
     */
    KeyPair generateKeyPair(long expiresInSeconds);

    // ==================== Encryption ====================

    /**
     * Encrypt data with public key
     *
     * @param data      data to encrypt
     * @param publicKey public key
     * @return encrypted data (Base64 encoded)
     */
    String encrypt(String data, String publicKey);

    /**
     * Decrypt data with private key
     *
     * @param encryptedData encrypted data (Base64 encoded)
     * @param privateKey   private key
     * @return decrypted data
     */
    String decrypt(String encryptedData, String privateKey);

    /**
     * Encrypt data asynchronously
     *
     * @param data      data to encrypt
     * @param publicKey public key
     * @return CompletableFuture with encrypted data
     */
    CompletableFuture<String> encryptAsync(String data, String publicKey);

    /**
     * Decrypt data asynchronously
     *
     * @param encryptedData encrypted data
     * @param privateKey   private key
     * @return CompletableFuture with decrypted data
     */
    CompletableFuture<String> decryptAsync(String encryptedData, String privateKey);

    // ==================== Digital Signature ====================

    /**
     * Sign data with private key
     *
     * @param data       data to sign
     * @param privateKey private key
     * @return signature (Base64 encoded)
     */
    String sign(String data, String privateKey);

    /**
     * Verify signature with public key
     *
     * @param data      original data
     * @param signature signature to verify
     * @param publicKey public key
     * @return true if signature is valid
     */
    boolean verify(String data, String signature, String publicKey);

    // ==================== Token Management ====================

    /**
     * Generate a token
     *
     * @param subject        token subject (e.g., user ID)
     * @param expireInMillis expiration time in milliseconds
     * @return generated token
     */
    String generateToken(String subject, long expireInMillis);

    /**
     * Generate a token with claims
     *
     * @param subject        token subject
     * @param claims         additional claims
     * @param expireInMillis expiration time
     * @return generated token
     */
    String generateToken(String subject, java.util.Map<String, Object> claims, long expireInMillis);

    /**
     * Validate a token
     *
     * @param token token to validate
     * @return token information
     */
    TokenInfo validateToken(String token);

    /**
     * Revoke a token
     *
     * @param token token to revoke
     */
    void revokeToken(String token);

    /**
     * Check if token is revoked
     *
     * @param token token to check
     * @return true if revoked
     */
    boolean isTokenRevoked(String token);

    // ==================== Scene Group Key Management ====================

    /**
     * Generate a key pair for a scene group
     *
     * @param sceneId scene group ID
     * @return generated key pair
     */
    KeyPair generateSceneKey(String sceneId);

    /**
     * Get key pair for a scene group
     *
     * @param sceneId scene group ID
     * @return key pair, or null if not exists
     */
    KeyPair getSceneKey(String sceneId);

    /**
     * Rotate the key pair for a scene group
     *
     * @param sceneId scene group ID
     * @return new key pair
     */
    KeyPair rotateSceneKey(String sceneId);

    /**
     * Remove key pair for a scene group
     *
     * @param sceneId scene group ID
     */
    void removeSceneKey(String sceneId);

    /**
     * Check if scene group has a key pair
     *
     * @param sceneId scene group ID
     * @return true if has key pair
     */
    boolean hasSceneKey(String sceneId);

    // ==================== End-to-End Encryption ====================

    /**
     * Encrypt data for a peer in a scene group
     *
     * @param sceneId scene group ID
     * @param peerId  target peer ID
     * @param data    data to encrypt
     * @return encrypted data
     */
    String encryptForPeer(String sceneId, String peerId, String data);

    /**
     * Decrypt data from a peer in a scene group
     *
     * @param sceneId      scene group ID
     * @param peerId       source peer ID
     * @param encryptedData encrypted data
     * @return decrypted data
     */
    String decryptFromPeer(String sceneId, String peerId, String encryptedData);

    /**
     * Register a peer's public key for a scene group
     *
     * @param sceneId  scene group ID
     * @param peerId   peer ID
     * @param publicKey peer's public key
     */
    void registerPeerPublicKey(String sceneId, String peerId, String publicKey);

    /**
     * Remove a peer's public key
     *
     * @param sceneId scene group ID
     * @param peerId  peer ID
     */
    void removePeerPublicKey(String sceneId, String peerId);

    // ==================== Lifecycle ====================

    /**
     * Shutdown the service
     */
    void shutdown();

    /**
     * Check if service is healthy
     *
     * @return true if healthy
     */
    boolean isHealthy();
}

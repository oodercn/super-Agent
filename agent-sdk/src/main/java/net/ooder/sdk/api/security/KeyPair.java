package net.ooder.sdk.api.security;

/**
 * Key Pair for encryption
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class KeyPair {

    private String publicKey;
    private String privateKey;
    private String algorithm;
    private long createdAt;
    private long expiresAt;

    public KeyPair() {
        this.algorithm = "RSA";
        this.createdAt = System.currentTimeMillis();
    }

    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }

    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }

    public boolean isExpired() {
        return expiresAt > 0 && System.currentTimeMillis() > expiresAt;
    }
}

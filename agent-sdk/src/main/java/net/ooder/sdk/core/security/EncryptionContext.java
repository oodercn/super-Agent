package net.ooder.sdk.core.security;

import java.util.Map;

public class EncryptionContext {
    
    private String algorithm;
    private String keyId;
    private String key;
    private Map<String, Object> parameters;
    
    public static EncryptionContext of(String algorithm, String key) {
        EncryptionContext context = new EncryptionContext();
        context.setAlgorithm(algorithm);
        context.setKey(key);
        return context;
    }
    
    public static EncryptionContext aes256(String key) {
        return of("AES-256", key);
    }
    
    public static EncryptionContext rsa(String keyId) {
        EncryptionContext context = new EncryptionContext();
        context.setAlgorithm("RSA");
        context.setKeyId(keyId);
        return context;
    }
    
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
}


package net.ooder.sdk.service.security.crypto;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyManager {
    
    private static final Logger log = LoggerFactory.getLogger(KeyManager.class);
    
    private final Map<String, SecretKey> keys;
    private final SecureRandom random;
    private final String algorithm = "AES/GCM/NoPadding";
    
    public KeyManager() {
        this.keys = new ConcurrentHashMap<>();
        this.random = new SecureRandom();
    }
    
    public String generateKey(String keyId) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256, random);
            SecretKey key = keyGen.generateKey();
            
            keys.put(keyId, key);
            
            log.info("Generated key: {}", keyId);
            
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            log.error("Failed to generate key", e);
            throw new RuntimeException("Key generation failed", e);
        }
    }
    
    public void importKey(String keyId, String keyData) {
        byte[] keyBytes = Base64.getDecoder().decode(keyData);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        keys.put(keyId, key);
        log.info("Imported key: {}", keyId);
    }
    
    public String exportKey(String keyId) {
        SecretKey key = keys.get(keyId);
        if (key == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
    
    public void deleteKey(String keyId) {
        keys.remove(keyId);
        log.info("Deleted key: {}", keyId);
    }
    
    public boolean hasKey(String keyId) {
        return keys.containsKey(keyId);
    }
    
    public byte[] encrypt(String keyId, byte[] data) {
        try {
            SecretKey key = keys.get(keyId);
            if (key == null) {
                throw new IllegalArgumentException("Key not found: " + keyId);
            }
            
            byte[] iv = new byte[12];
            random.nextBytes(iv);
            
            Cipher cipher = Cipher.getInstance(algorithm);
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            
            byte[] encrypted = cipher.doFinal(data);
            
            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
            
            return result;
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    public byte[] decrypt(String keyId, byte[] data) {
        try {
            SecretKey key = keys.get(keyId);
            if (key == null) {
                throw new IllegalArgumentException("Key not found: " + keyId);
            }
            
            byte[] iv = new byte[12];
            System.arraycopy(data, 0, iv, 0, 12);
            
            byte[] encrypted = new byte[data.length - 12];
            System.arraycopy(data, 12, encrypted, 0, encrypted.length);
            
            Cipher cipher = Cipher.getInstance(algorithm);
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            
            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Decryption failed", e);
        }
    }
}

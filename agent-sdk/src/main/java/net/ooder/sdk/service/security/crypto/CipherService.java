
package net.ooder.sdk.service.security.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherService {
    
    private static final Logger log = LoggerFactory.getLogger(CipherService.class);
    
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final int AES_KEY_SIZE = 256;
    
    private final SecureRandom secureRandom;
    
    public CipherService() {
        this.secureRandom = new SecureRandom();
    }
    
    public byte[] generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(AES_KEY_SIZE, secureRandom);
            return keyGen.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to generate AES key", e);
            throw new RuntimeException("AES key generation failed", e);
        }
    }
    
    public byte[] encrypt(byte[] plaintext, byte[] key) {
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            
            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
            
            byte[] ciphertext = cipher.doFinal(plaintext);
            
            byte[] result = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
            
            return result;
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    public byte[] decrypt(byte[] ciphertext, byte[] key) {
        try {
            if (ciphertext.length < GCM_IV_LENGTH) {
                throw new IllegalArgumentException("Ciphertext too short");
            }
            
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(ciphertext, 0, iv, 0, GCM_IV_LENGTH);
            
            byte[] encryptedData = new byte[ciphertext.length - GCM_IV_LENGTH];
            System.arraycopy(ciphertext, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length);
            
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            
            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
            
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    public String encryptString(String plaintext, byte[] key) {
        byte[] encrypted = encrypt(plaintext.getBytes(StandardCharsets.UTF_8), key);
        return encodeBase64(encrypted);
    }
    
    public String decryptString(String ciphertext, byte[] key) {
        byte[] decrypted = decrypt(decodeBase64(ciphertext), key);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
    
    public byte[] hash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    
    public String hashString(String data) {
        byte[] hash = hash(data.getBytes(StandardCharsets.UTF_8));
        return encodeHex(hash);
    }
    
    public byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
    
    private String encodeBase64(byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }
    
    private byte[] decodeBase64(String data) {
        return java.util.Base64.getDecoder().decode(data);
    }
    
    private String encodeHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public List<KeyShare> splitKey(byte[] key, int totalShares, int threshold) {
        List<KeyShare> shares = new ArrayList<>();
        
        for (int i = 0; i < totalShares; i++) {
            byte[] shareData = generateRandomBytes(key.length);
            for (int j = 0; j < key.length; j++) {
                shareData[j] = (byte) (key[j] ^ shareData[j]);
            }
            shares.add(new KeyShare(i + 1, shareData, totalShares, threshold));
        }
        
        log.debug("Split key into {} shares with threshold {}", totalShares, threshold);
        return shares;
    }
    
    public byte[] combineShares(List<KeyShare> shares) {
        if (shares == null || shares.isEmpty()) {
            throw new IllegalArgumentException("No shares provided");
        }
        
        KeyShare first = shares.get(0);
        if (shares.size() < first.getThreshold()) {
            throw new IllegalArgumentException("Not enough shares");
        }
        
        byte[] result = new byte[first.getShareData().length];
        for (KeyShare share : shares) {
            byte[] shareData = share.getShareData();
            for (int i = 0; i < result.length; i++) {
                result[i] ^= shareData[i];
            }
        }
        
        return result;
    }
}

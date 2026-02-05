package net.ooder.sdk.system.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class SecurityUtils {
    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);
    private static final String AES_ALGORITHM = "AES";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int AES_KEY_SIZE = 256;

    /**
     * 生成随机的AES密钥
     */
    public static String generateSessionKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(AES_KEY_SIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating session key: {}", e.getMessage());
            // 生成一个随机字符串作为备选
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    /**
     * 使用AES加密数据
     */
    public static String encrypt(String data, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Error encrypting data: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 使用AES解密数据
     */
    public static String decrypt(String encryptedData, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error decrypting data: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成数据的哈希值
     */
    public static String hash(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error hashing data: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证签名
     */
    public static boolean verifySignature(String data, String signature, String publicKey) {
        // 这里可以实现基于公钥的签名验证逻辑
        // 为了简化，目前只验证哈希值
        String calculatedHash = hash(data);
        return calculatedHash != null && calculatedHash.equals(signature);
    }

    /**
     * 生成签名
     */
    public static String generateSignature(String data, String privateKey) {
        // 这里可以实现基于私钥的签名生成逻辑
        // 为了简化，目前只返回哈希值
        return hash(data);
    }

    /**
     * 生成认证令牌
     */
    public static String generateAuthToken(String agentId, String secret) {
        String data = agentId + ":" + System.currentTimeMillis() + ":" + secret;
        return hash(data);
    }
}
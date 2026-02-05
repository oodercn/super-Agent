package net.ooder.skillcenter.identity;

import net.ooder.skillcenter.personalai.IdentityManager;

import java.security.*;
import java.util.Base64;
import java.util.UUID;

/**
 * 去中心化身份管理器，扩展现有身份管理功能
 * 支持DID（去中心化标识符）、密码学签名等
 */
public class DecentralizedIdentityManager extends IdentityManager {
    // 单例实例
    private static DecentralizedIdentityManager instance;
    
    // 密钥对
    private KeyPair keyPair;
    
    // 去中心化标识符 (DID)
    private String did;
    
    // 身份存储
    private IdentityStorage identityStorage;
    
    /**
     * 私有构造方法
     */
    private DecentralizedIdentityManager() {
        super();
        this.identityStorage = new IdentityStorage();
        this.keyPair = generateKeyPair();
        this.did = generateDID();
        
        // 初始化身份存储
        initializeIdentityStorage();
    }
    
    /**
     * 获取实例
     * @return 去中心化身份管理器实例
     */
    public static synchronized DecentralizedIdentityManager getInstance() {
        if (instance == null) {
            instance = new DecentralizedIdentityManager();
        }
        return instance;
    }
    
    /**
     * 生成密钥对
     * @return 密钥对
     */
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to generate key pair: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 生成DID（去中心化标识符）
     * @return DID
     */
    private String generateDID() {
        String userId = getCurrentUserId();
        return "did:ooder:" + Base64.getUrlEncoder().withoutPadding()
            .encodeToString(userId.getBytes());
    }
    
    /**
     * 初始化身份存储
     */
    private void initializeIdentityStorage() {
        // 存储DID和密钥信息
        identityStorage.storeDID(did);
        if (keyPair != null) {
            try {
                String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                identityStorage.storePublicKey(publicKey);
                identityStorage.storePrivateKey(privateKey);
            } catch (Exception e) {
                System.err.println("Failed to store keys: " + e.getMessage());
            }
        }
        
        // 存储用户身份信息
        identityStorage.storeUserIdentity(getIdentity());
    }
    
    /**
     * 获取DID
     * @return DID
     */
    public String getDID() {
        return did;
    }
    
    /**
     * 签名数据
     * @param data 待签名数据
     * @return 签名结果
     * @throws Exception 签名异常
     */
    public String signData(String data) throws Exception {
        if (keyPair == null) {
            throw new Exception("Key pair not initialized");
        }
        
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(data.getBytes());
        byte[] signedData = signature.sign();
        
        return Base64.getEncoder().encodeToString(signedData);
    }
    
    /**
     * 验证签名
     * @param data 原始数据
     * @param signature 签名
     * @param publicKeyBase64 Base64编码的公钥
     * @return 是否验证通过
     * @throws Exception 验证异常
     */
    public boolean verifySignature(String data, String signature, String publicKeyBase64) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        java.security.spec.X509EncodedKeySpec keySpec = new java.security.spec.X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        
        return sig.verify(Base64.getDecoder().decode(signature));
    }
    
    /**
     * 获取公钥（Base64编码）
     * @return 公钥
     */
    public String getPublicKeyBase64() {
        if (keyPair != null) {
            return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        }
        return null;
    }
    
    /**
     * 从存储中恢复身份
     * @param recoveryKey 恢复密钥
     * @return 是否恢复成功
     */
    public boolean recoverIdentity(String recoveryKey) {
        // 实现身份恢复逻辑
        // 实际项目中，这里应该从备份中恢复身份信息
        System.out.println("Attempting to recover identity with key: " + recoveryKey);
        return identityStorage.verifyRecoveryKey(recoveryKey);
    }
    
    /**
     * 导出身份信息（用于备份）
     * @return 导出的身份信息
     */
    public String exportIdentity() {
        // 实现身份导出逻辑
        return identityStorage.exportIdentity();
    }
    
    /**
     * 导入身份信息
     * @param exportedIdentity 导出的身份信息
     * @return 是否导入成功
     */
    public boolean importIdentity(String exportedIdentity) {
        // 实现身份导入逻辑
        return identityStorage.importIdentity(exportedIdentity);
    }
    
    /**
     * 生成身份恢复密钥
     * @return 恢复密钥
     */
    public String generateRecoveryKey() {
        // 生成恢复密钥
        String recoveryKey = "recovery-" + UUID.randomUUID().toString();
        identityStorage.setRecoveryKey(recoveryKey);
        return recoveryKey;
    }
    
    /**
     * 验证DID
     * @param did DID
     * @return 是否验证通过
     */
    public boolean verifyDID(String did) {
        // 验证DID格式
        return did.startsWith("did:ooder:");
    }
    
    /**
     * 从DID中提取用户ID
     * @param did DID
     * @return 用户ID
     */
    public String extractUserIdFromDID(String did) {
        if (verifyDID(did)) {
            String encodedPart = did.substring(8); // 移除 "did:ooder:" 前缀
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPart);
            return new String(decodedBytes);
        }
        return null;
    }
    
    /**
     * 启动身份管理器
     */
    @Override
    public void start() {
        super.start();
        System.out.println("Decentralized Identity Manager started with DID: " + did);
        System.out.println("Public key: " + getPublicKeyBase64());
    }
    
    /**
     * 停止身份管理器
     */
    @Override
    public void stop() {
        super.stop();
        System.out.println("Decentralized Identity Manager stopped");
    }
    
    /**
     * 获取身份存储
     * @return 身份存储
     */
    public IdentityStorage getIdentityStorage() {
        return identityStorage;
    }
}

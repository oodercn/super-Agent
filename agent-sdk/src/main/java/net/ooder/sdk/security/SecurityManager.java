package net.ooder.sdk.security;

import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SecurityManager {
    // 初始化安全管理器
    CompletableFuture<Boolean> initialize();
    
    // 关闭安全管理器
    CompletableFuture<Boolean> shutdown();
    
    // 认证相关方法
    CompletableFuture<AuthenticationResult> authenticate(String principal, String credentials);
    CompletableFuture<AuthenticationResult> authenticate(String principal, String credentials, Map<String, Object> context);
    CompletableFuture<Boolean> validateToken(String token);
    CompletableFuture<Map<String, Object>> parseToken(String token);
    CompletableFuture<String> generateToken(String principal, Map<String, Object> claims);
    CompletableFuture<String> generateToken(String principal, Map<String, Object> claims, long expiration);
    
    // 授权相关方法
    CompletableFuture<AuthorizationResult> authorize(String principal, String resource, String action);
    CompletableFuture<AuthorizationResult> authorize(String principal, String resource, String action, Map<String, Object> context);
    CompletableFuture<Boolean> hasPermission(String principal, String resource, String action);
    CompletableFuture<List<Permission>> getPermissions(String principal);
    CompletableFuture<Boolean> addPermission(String principal, Permission permission);
    CompletableFuture<Boolean> removePermission(String principal, Permission permission);
    CompletableFuture<Boolean> setPermissions(String principal, List<Permission> permissions);
    
    // 加密相关方法
    CompletableFuture<String> encrypt(String plaintext);
    CompletableFuture<String> decrypt(String ciphertext);
    CompletableFuture<byte[]> encrypt(byte[] plaintext);
    CompletableFuture<byte[]> decrypt(byte[] ciphertext);
    CompletableFuture<String> hash(String input);
    CompletableFuture<Boolean> verifyHash(String input, String hash);
    
    // 安全存储相关方法
    CompletableFuture<Boolean> saveSecureConfig(String key, Object value);
    CompletableFuture<Object> loadSecureConfig(String key);
    CompletableFuture<Boolean> deleteSecureConfig(String key);
    CompletableFuture<Boolean> existsSecureConfig(String key);
    
    // 安全策略相关方法
    CompletableFuture<Boolean> addSecurityPolicy(SecurityPolicy policy);
    CompletableFuture<Boolean> removeSecurityPolicy(String policyId);
    CompletableFuture<List<SecurityPolicy>> getSecurityPolicies();
    CompletableFuture<SecurityPolicy> getSecurityPolicy(String policyId);
    CompletableFuture<Boolean> updateSecurityPolicy(String policyId, SecurityPolicy policy);
    
    // 安全审计相关方法
    CompletableFuture<Boolean> logSecurityEvent(SecurityEvent event);
    CompletableFuture<List<SecurityEvent>> getSecurityEvents(int limit);
    CompletableFuture<List<SecurityEvent>> getSecurityEventsByType(String eventType, int limit);
    CompletableFuture<List<SecurityEvent>> getSecurityEventsByPrincipal(String principal, int limit);
    CompletableFuture<Map<String, Object>> getSecurityStatistics();
    
    // 密钥管理相关方法
    CompletableFuture<Boolean> generateKeyPair(String keyId);
    CompletableFuture<Boolean> importKeyPair(String keyId, String publicKey, String privateKey);
    CompletableFuture<Map<String, String>> getKeyPair(String keyId);
    CompletableFuture<Boolean> deleteKeyPair(String keyId);
    CompletableFuture<List<String>> listKeyPairs();
    
    // TLS/SSL 相关方法
    CompletableFuture<Boolean> configureTLS(Map<String, Object> tlsConfig);
    CompletableFuture<Map<String, Object>> getTLSConfig();
    CompletableFuture<Boolean> validateCertificate(String certificate);
    CompletableFuture<Boolean> renewCertificate(String certificateId);
}

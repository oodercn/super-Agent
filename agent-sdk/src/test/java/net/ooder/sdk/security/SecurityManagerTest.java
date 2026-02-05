package net.ooder.sdk.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SecurityManagerTest {

    @Mock
    private SecurityManager securityManager;

    private String testPrincipal;
    private String testCredentials;
    private String testResource;
    private String testAction;
    private Map<String, Object> testContext;
    private Permission testPermission;
    private SecurityPolicy testPolicy;
    private SecurityEvent testEvent;

    @Before
    public void setUp() {
        // 初始化测试数据
        testPrincipal = "test-user";
        testCredentials = "test-password";
        testResource = "resource-1";
        testAction = "read";

        // 初始化测试上下文
        testContext = new HashMap<>();
        testContext.put("ipAddress", "192.168.1.100");
        testContext.put("userAgent", "Mozilla/5.0");
        testContext.put("timestamp", System.currentTimeMillis());

        // 初始化测试权限
        testPermission = new Permission();
        testPermission.setResource(testResource);
        testPermission.setAction(testAction);
        testPermission.setEffect(Permission.Effect.ALLOW);
        testPermission.setCondition(Collections.emptyMap());

        // 初始化测试安全策略
        testPolicy = new SecurityPolicy();
        testPolicy.setPolicyId("policy-1");
        testPolicy.setName("Test Policy");
        testPolicy.setDescription("Test security policy");
        testPolicy.setType(SecurityPolicy.PolicyType.AUTHORIZATION);
        testPolicy.setPriority(1);
        testPolicy.setEnabled(true);
        
        // 创建PolicyRule
        SecurityPolicy.PolicyRule rule = new SecurityPolicy.PolicyRule(
            Permission.Effect.ALLOW, testResource, testAction
        );
        testPolicy.setRules(Collections.singletonList(rule));

        // 初始化测试安全事件
        testEvent = new SecurityEvent();
        testEvent.setEventId("event-1");
        testEvent.setEventType(SecurityEvent.EventType.AUTHENTICATION_SUCCESS);
        testEvent.setLevel(SecurityEvent.EventLevel.INFO);
        testEvent.setMessage("User authentication successful");
        testEvent.setPrincipal(testPrincipal);
        testEvent.setAction("login");
        testEvent.setResource("/api/auth");
        testEvent.setTimestamp(System.currentTimeMillis());
        testEvent.setDetails(testContext);
        testEvent.setSource("test-source");
    }

    // 测试初始化和关闭方法
    @Test
    public void testInitializeAndShutdown() throws ExecutionException, InterruptedException {
        // 测试初始化
        when(securityManager.initialize()).thenReturn(CompletableFuture.completedFuture(true));
        boolean initialized = securityManager.initialize().get();
        assertTrue(initialized);

        // 测试关闭
        when(securityManager.shutdown()).thenReturn(CompletableFuture.completedFuture(true));
        boolean shutdown = securityManager.shutdown().get();
        assertTrue(shutdown);
    }

    // 测试认证相关方法
    @Test
    public void testAuthenticationMethods() throws ExecutionException, InterruptedException {
        // 测试简单认证
        AuthenticationResult successResult = new AuthenticationResult(true, testPrincipal, "test-token", Collections.singletonMap("role", "admin"));
        when(securityManager.authenticate(testPrincipal, testCredentials)).thenReturn(CompletableFuture.completedFuture(successResult));
        AuthenticationResult result1 = securityManager.authenticate(testPrincipal, testCredentials).get();
        assertNotNull(result1);
        assertTrue(result1.isSuccess());
        assertEquals(testPrincipal, result1.getPrincipal());

        // 测试带上下文的认证
        when(securityManager.authenticate(testPrincipal, testCredentials, testContext)).thenReturn(CompletableFuture.completedFuture(successResult));
        AuthenticationResult result2 = securityManager.authenticate(testPrincipal, testCredentials, testContext).get();
        assertNotNull(result2);
        assertTrue(result2.isSuccess());

        // 测试令牌验证
        String testToken = "test-jwt-token";
        when(securityManager.validateToken(testToken)).thenReturn(CompletableFuture.completedFuture(true));
        boolean tokenValid = securityManager.validateToken(testToken).get();
        assertTrue(tokenValid);

        // 测试令牌解析
        Map<String, Object> tokenClaims = new HashMap<>();
        tokenClaims.put("sub", testPrincipal);
        tokenClaims.put("role", "admin");
        tokenClaims.put("iat", System.currentTimeMillis() / 1000);
        when(securityManager.parseToken(testToken)).thenReturn(CompletableFuture.completedFuture(tokenClaims));
        Map<String, Object> parsedClaims = securityManager.parseToken(testToken).get();
        assertNotNull(parsedClaims);
        assertEquals(testPrincipal, parsedClaims.get("sub"));
        assertEquals("admin", parsedClaims.get("role"));

        // 测试令牌生成
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "user");
        when(securityManager.generateToken(testPrincipal, claims)).thenReturn(CompletableFuture.completedFuture(testToken));
        String generatedToken = securityManager.generateToken(testPrincipal, claims).get();
        assertNotNull(generatedToken);
        assertEquals(testToken, generatedToken);

        // 测试带过期时间的令牌生成
        long expiration = 3600000; // 1小时
        when(securityManager.generateToken(testPrincipal, claims, expiration)).thenReturn(CompletableFuture.completedFuture(testToken));
        String generatedTokenWithExpiration = securityManager.generateToken(testPrincipal, claims, expiration).get();
        assertNotNull(generatedTokenWithExpiration);
        assertEquals(testToken, generatedTokenWithExpiration);
    }

    // 测试授权相关方法
    @Test
    public void testAuthorizationMethods() throws ExecutionException, InterruptedException {
        // 测试简单授权
        AuthorizationResult allowResult = new AuthorizationResult(true, testPrincipal, testResource, testAction);
        when(securityManager.authorize(testPrincipal, testResource, testAction)).thenReturn(CompletableFuture.completedFuture(allowResult));
        AuthorizationResult result1 = securityManager.authorize(testPrincipal, testResource, testAction).get();
        assertNotNull(result1);
        assertTrue(result1.isGranted());

        // 测试带上下文的授权
        when(securityManager.authorize(testPrincipal, testResource, testAction, testContext)).thenReturn(CompletableFuture.completedFuture(allowResult));
        AuthorizationResult result2 = securityManager.authorize(testPrincipal, testResource, testAction, testContext).get();
        assertNotNull(result2);
        assertTrue(result2.isGranted());

        // 测试权限检查
        when(securityManager.hasPermission(testPrincipal, testResource, testAction)).thenReturn(CompletableFuture.completedFuture(true));
        boolean hasPermission = securityManager.hasPermission(testPrincipal, testResource, testAction).get();
        assertTrue(hasPermission);

        // 测试获取权限列表
        List<Permission> permissions = Collections.singletonList(testPermission);
        when(securityManager.getPermissions(testPrincipal)).thenReturn(CompletableFuture.completedFuture(permissions));
        List<Permission> retrievedPermissions = securityManager.getPermissions(testPrincipal).get();
        assertNotNull(retrievedPermissions);
        assertEquals(1, retrievedPermissions.size());
        assertEquals(testResource, retrievedPermissions.get(0).getResource());
        assertEquals(testAction, retrievedPermissions.get(0).getAction());

        // 测试添加权限
        when(securityManager.addPermission(testPrincipal, testPermission)).thenReturn(CompletableFuture.completedFuture(true));
        boolean added = securityManager.addPermission(testPrincipal, testPermission).get();
        assertTrue(added);

        // 测试移除权限
        when(securityManager.removePermission(testPrincipal, testPermission)).thenReturn(CompletableFuture.completedFuture(true));
        boolean removed = securityManager.removePermission(testPrincipal, testPermission).get();
        assertTrue(removed);

        // 测试设置权限
        when(securityManager.setPermissions(testPrincipal, permissions)).thenReturn(CompletableFuture.completedFuture(true));
        boolean set = securityManager.setPermissions(testPrincipal, permissions).get();
        assertTrue(set);
    }

    // 测试加密相关方法
    @Test
    public void testEncryptionMethods() throws ExecutionException, InterruptedException {
        String plaintext = "secret message";
        String ciphertext = "encrypted-message";
        String hash = "hashed-value";

        // 测试字符串加密
        when(securityManager.encrypt(plaintext)).thenReturn(CompletableFuture.completedFuture(ciphertext));
        String encrypted = securityManager.encrypt(plaintext).get();
        assertNotNull(encrypted);
        assertEquals(ciphertext, encrypted);

        // 测试字符串解密
        when(securityManager.decrypt(ciphertext)).thenReturn(CompletableFuture.completedFuture(plaintext));
        String decrypted = securityManager.decrypt(ciphertext).get();
        assertNotNull(decrypted);
        assertEquals(plaintext, decrypted);

        // 测试字节数组加密
        byte[] plaintextBytes = plaintext.getBytes();
        byte[] ciphertextBytes = ciphertext.getBytes();
        when(securityManager.encrypt(plaintextBytes)).thenReturn(CompletableFuture.completedFuture(ciphertextBytes));
        byte[] encryptedBytes = securityManager.encrypt(plaintextBytes).get();
        assertNotNull(encryptedBytes);
        assertArrayEquals(ciphertextBytes, encryptedBytes);

        // 测试字节数组解密
        when(securityManager.decrypt(ciphertextBytes)).thenReturn(CompletableFuture.completedFuture(plaintextBytes));
        byte[] decryptedBytes = securityManager.decrypt(ciphertextBytes).get();
        assertNotNull(decryptedBytes);
        assertArrayEquals(plaintextBytes, decryptedBytes);

        // 测试哈希
        when(securityManager.hash(plaintext)).thenReturn(CompletableFuture.completedFuture(hash));
        String hashed = securityManager.hash(plaintext).get();
        assertNotNull(hashed);
        assertEquals(hash, hashed);

        // 测试哈希验证
        when(securityManager.verifyHash(plaintext, hash)).thenReturn(CompletableFuture.completedFuture(true));
        boolean verified = securityManager.verifyHash(plaintext, hash).get();
        assertTrue(verified);
    }

    // 测试安全存储相关方法
    @Test
    public void testSecureStorageMethods() throws ExecutionException, InterruptedException {
        String testKey = "test-key";
        String testValue = "test-value";

        // 测试保存安全配置
        when(securityManager.saveSecureConfig(testKey, testValue)).thenReturn(CompletableFuture.completedFuture(true));
        boolean saved = securityManager.saveSecureConfig(testKey, testValue).get();
        assertTrue(saved);

        // 测试加载安全配置
        when(securityManager.loadSecureConfig(testKey)).thenReturn(CompletableFuture.completedFuture(testValue));
        Object loaded = securityManager.loadSecureConfig(testKey).get();
        assertNotNull(loaded);
        assertEquals(testValue, loaded);

        // 测试删除安全配置
        when(securityManager.deleteSecureConfig(testKey)).thenReturn(CompletableFuture.completedFuture(true));
        boolean deleted = securityManager.deleteSecureConfig(testKey).get();
        assertTrue(deleted);

        // 测试检查安全配置是否存在
        when(securityManager.existsSecureConfig(testKey)).thenReturn(CompletableFuture.completedFuture(false));
        boolean exists = securityManager.existsSecureConfig(testKey).get();
        assertFalse(exists);
    }

    // 测试安全策略相关方法
    @Test
    public void testSecurityPolicyMethods() throws ExecutionException, InterruptedException {
        String policyId = "policy-1";
        List<SecurityPolicy> policies = Collections.singletonList(testPolicy);

        // 测试添加安全策略
        when(securityManager.addSecurityPolicy(testPolicy)).thenReturn(CompletableFuture.completedFuture(true));
        boolean added = securityManager.addSecurityPolicy(testPolicy).get();
        assertTrue(added);

        // 测试移除安全策略
        when(securityManager.removeSecurityPolicy(policyId)).thenReturn(CompletableFuture.completedFuture(true));
        boolean removed = securityManager.removeSecurityPolicy(policyId).get();
        assertTrue(removed);

        // 测试获取所有安全策略
        when(securityManager.getSecurityPolicies()).thenReturn(CompletableFuture.completedFuture(policies));
        List<SecurityPolicy> retrievedPolicies = securityManager.getSecurityPolicies().get();
        assertNotNull(retrievedPolicies);
        assertEquals(1, retrievedPolicies.size());
        assertEquals(policyId, retrievedPolicies.get(0).getPolicyId());

        // 测试获取单个安全策略
        when(securityManager.getSecurityPolicy(policyId)).thenReturn(CompletableFuture.completedFuture(testPolicy));
        SecurityPolicy retrievedPolicy = securityManager.getSecurityPolicy(policyId).get();
        assertNotNull(retrievedPolicy);
        assertEquals(policyId, retrievedPolicy.getPolicyId());

        // 测试更新安全策略
        SecurityPolicy updatedPolicy = testPolicy;
        updatedPolicy.setDescription("Updated test policy");
        when(securityManager.updateSecurityPolicy(policyId, updatedPolicy)).thenReturn(CompletableFuture.completedFuture(true));
        boolean updated = securityManager.updateSecurityPolicy(policyId, updatedPolicy).get();
        assertTrue(updated);
    }

    // 测试安全审计相关方法
    @Test
    public void testSecurityAuditMethods() throws ExecutionException, InterruptedException {
        // 测试记录安全事件
        when(securityManager.logSecurityEvent(testEvent)).thenReturn(CompletableFuture.completedFuture(true));
        boolean logged = securityManager.logSecurityEvent(testEvent).get();
        assertTrue(logged);

        // 测试获取安全事件
        List<SecurityEvent> events = Collections.singletonList(testEvent);
        when(securityManager.getSecurityEvents(10)).thenReturn(CompletableFuture.completedFuture(events));
        List<SecurityEvent> retrievedEvents = securityManager.getSecurityEvents(10).get();
        assertNotNull(retrievedEvents);
        assertEquals(1, retrievedEvents.size());
        assertEquals("event-1", retrievedEvents.get(0).getEventId());

        // 测试按类型获取安全事件
        when(securityManager.getSecurityEventsByType("authentication", 10)).thenReturn(CompletableFuture.completedFuture(events));
        List<SecurityEvent> authEvents = securityManager.getSecurityEventsByType("authentication", 10).get();
        assertNotNull(authEvents);
        assertEquals(1, authEvents.size());

        // 测试按主体获取安全事件
        when(securityManager.getSecurityEventsByPrincipal(testPrincipal, 10)).thenReturn(CompletableFuture.completedFuture(events));
        List<SecurityEvent> principalEvents = securityManager.getSecurityEventsByPrincipal(testPrincipal, 10).get();
        assertNotNull(principalEvents);
        assertEquals(1, principalEvents.size());

        // 测试获取安全统计信息
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEvents", 100);
        stats.put("successfulLogins", 80);
        stats.put("failedLogins", 5);
        stats.put("authorizedActions", 500);
        stats.put("unauthorizedActions", 10);
        when(securityManager.getSecurityStatistics()).thenReturn(CompletableFuture.completedFuture(stats));
        Map<String, Object> securityStats = securityManager.getSecurityStatistics().get();
        assertNotNull(securityStats);
        assertEquals(100, securityStats.get("totalEvents"));
        assertEquals(80, securityStats.get("successfulLogins"));
    }

    // 测试密钥管理相关方法
    @Test
    public void testKeyManagementMethods() throws ExecutionException, InterruptedException {
        String keyId = "key-1";
        String publicKey = "test-public-key";
        String privateKey = "test-private-key";
        Map<String, String> keyPair = new HashMap<>();
        keyPair.put("publicKey", publicKey);
        keyPair.put("privateKey", privateKey);

        // 测试生成密钥对
        when(securityManager.generateKeyPair(keyId)).thenReturn(CompletableFuture.completedFuture(true));
        boolean generated = securityManager.generateKeyPair(keyId).get();
        assertTrue(generated);

        // 测试导入密钥对
        when(securityManager.importKeyPair(keyId, publicKey, privateKey)).thenReturn(CompletableFuture.completedFuture(true));
        boolean imported = securityManager.importKeyPair(keyId, publicKey, privateKey).get();
        assertTrue(imported);

        // 测试获取密钥对
        when(securityManager.getKeyPair(keyId)).thenReturn(CompletableFuture.completedFuture(keyPair));
        Map<String, String> retrievedKeyPair = securityManager.getKeyPair(keyId).get();
        assertNotNull(retrievedKeyPair);
        assertEquals(publicKey, retrievedKeyPair.get("publicKey"));
        assertEquals(privateKey, retrievedKeyPair.get("privateKey"));

        // 测试删除密钥对
        when(securityManager.deleteKeyPair(keyId)).thenReturn(CompletableFuture.completedFuture(true));
        boolean deleted = securityManager.deleteKeyPair(keyId).get();
        assertTrue(deleted);

        // 测试列出密钥对
        List<String> keyIds = Collections.singletonList(keyId);
        when(securityManager.listKeyPairs()).thenReturn(CompletableFuture.completedFuture(keyIds));
        List<String> retrievedKeyIds = securityManager.listKeyPairs().get();
        assertNotNull(retrievedKeyIds);
        assertEquals(1, retrievedKeyIds.size());
        assertEquals(keyId, retrievedKeyIds.get(0));
    }

    // 测试TLS/SSL相关方法
    @Test
    public void testTlsMethods() throws ExecutionException, InterruptedException {
        Map<String, Object> tlsConfig = new HashMap<>();
        tlsConfig.put("enabled", true);
        tlsConfig.put("protocol", "TLSv1.2");
        tlsConfig.put("certificate", "test-certificate");
        tlsConfig.put("privateKey", "test-private-key");

        // 测试配置TLS
        when(securityManager.configureTLS(tlsConfig)).thenReturn(CompletableFuture.completedFuture(true));
        boolean configured = securityManager.configureTLS(tlsConfig).get();
        assertTrue(configured);

        // 测试获取TLS配置
        when(securityManager.getTLSConfig()).thenReturn(CompletableFuture.completedFuture(tlsConfig));
        Map<String, Object> retrievedTlsConfig = securityManager.getTLSConfig().get();
        assertNotNull(retrievedTlsConfig);
        assertTrue((boolean) retrievedTlsConfig.get("enabled"));
        assertEquals("TLSv1.2", retrievedTlsConfig.get("protocol"));

        // 测试验证证书
        String testCertificate = "test-certificate-content";
        when(securityManager.validateCertificate(testCertificate)).thenReturn(CompletableFuture.completedFuture(true));
        boolean valid = securityManager.validateCertificate(testCertificate).get();
        assertTrue(valid);

        // 测试更新证书
        String certificateId = "cert-1";
        when(securityManager.renewCertificate(certificateId)).thenReturn(CompletableFuture.completedFuture(true));
        boolean renewed = securityManager.renewCertificate(certificateId).get();
        assertTrue(renewed);
    }

    // 测试认证失败情况
    @Test
    public void testAuthenticationFailure() throws ExecutionException, InterruptedException {
        AuthenticationResult errorResult = new AuthenticationResult(false, "Invalid credentials", "AUTH_ERROR", "Wrong password");
        when(securityManager.authenticate(testPrincipal, "wrong-password")).thenReturn(CompletableFuture.completedFuture(errorResult));
        AuthenticationResult result = securityManager.authenticate(testPrincipal, "wrong-password").get();
        assertNotNull(result);
        assertFalse(result.isSuccess());
    }

    // 测试授权失败情况
    @Test
    public void testAuthorizationFailure() throws ExecutionException, InterruptedException {
        AuthorizationResult denyResult = new AuthorizationResult(false, testPrincipal, "restricted-resource", "admin-action", "Access denied", "AUTH_ERROR");
        when(securityManager.authorize(testPrincipal, "restricted-resource", "admin-action")).thenReturn(CompletableFuture.completedFuture(denyResult));
        AuthorizationResult result = securityManager.authorize(testPrincipal, "restricted-resource", "admin-action").get();
        assertNotNull(result);
        assertFalse(result.isGranted());
    }

    // 测试安全策略启用/禁用
    @Test
    public void testSecurityPolicyEnableDisable() {
        // 测试启用策略
        testPolicy.setEnabled(true);
        assertTrue(testPolicy.isEnabled());

        // 测试禁用策略
        testPolicy.setEnabled(false);
        assertFalse(testPolicy.isEnabled());
    }


}

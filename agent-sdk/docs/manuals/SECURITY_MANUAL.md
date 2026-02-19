# Ooder Agent SDK 安全手册

## 1. 安全体系概述

### 1.1 安全目标

Ooder Agent SDK 0.7.2 版本的安全体系旨在实现以下目标：

| 目标 | 描述 |
|------|------|
| **身份认证** | 确保用户和Agent的身份真实可靠 |
| **访问控制** | 基于角色和域的细粒度权限控制 |
| **数据加密** | 传输和存储数据的加密保护 |
| **审计追踪** | 完整的操作审计和日志记录 |
| **安全隔离** | 基于域的资源隔离 |

### 1.2 安全层次

```
┌─────────────────────────────────────────────────────────────────┐
│                      应用层安全                                  │
│  - 业务权限控制  - 数据脱敏  - 操作审计                         │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      北向安全服务                                │
│  - 域级安全  - P2P加密  - 细粒度权限  - 安全策略                │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      南向安全服务                                │
│  - 基础认证  - JWT令牌  - 基础权限  - HTTPS加密                 │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      核心安全抽象                                │
│  - 身份模型  - 权限接口  - 加密接口  - 安全上下文               │
└─────────────────────────────────────────────────────────────────┘
```

### 1.3 安全原则

| 原则 | 描述 |
|------|------|
| **最小权限** | 只授予完成任务所需的最小权限 |
| **深度防御** | 多层安全防护，避免单点失效 |
| **默认拒绝** | 默认拒绝所有访问，显式授权 |
| **审计完整** | 记录所有安全相关操作 |

## 2. 核心安全抽象

### 2.1 身份模型

#### 2.1.1 身份接口

```java
package net.ooder.sdk.core.security;

import java.util.Map;

public interface CoreIdentity {
    
    String getIdentityId();
    
    String getName();
    
    IdentityType getType();
    
    Map<String, Object> getAttributes();
    
    void setAttribute(String key, Object value);
    
    Object getAttribute(String key);
    
    boolean hasAttribute(String key);
}
```

#### 2.1.2 身份类型

```java
public enum IdentityType {
    USER,           // 用户
    AGENT,          // Agent
    ORGANIZATION,   // 组织
    DOMAIN,         // 域
    SERVICE,        // 服务
    DEVICE          // 设备
}
```

#### 2.1.3 身份上下文

```java
package net.ooder.sdk.core.security;

public class IdentityContext {
    
    private static final ThreadLocal<CoreIdentity> currentIdentity = new ThreadLocal<>();
    
    public static void setCurrentIdentity(CoreIdentity identity) {
        currentIdentity.set(identity);
    }
    
    public static CoreIdentity getCurrentIdentity() {
        return currentIdentity.get();
    }
    
    public static void clear() {
        currentIdentity.remove();
    }
    
    public static boolean hasIdentity() {
        return currentIdentity.get() != null;
    }
}
```

### 2.2 权限模型

#### 2.2.1 权限接口

```java
package net.ooder.sdk.core.security;

import java.util.List;

public interface CorePermission {
    
    String getPermissionId();
    
    String getResource();
    
    String getAction();
    
    boolean check(CoreIdentity identity);
    
    List<String> getConditions();
    
    void addCondition(String condition);
    
    void removeCondition(String condition);
}
```

#### 2.2.2 权限类型

| 权限类型 | 描述 | 示例 |
|----------|------|------|
| **资源权限** | 对特定资源的操作权限 | `skill:read`, `skill:write` |
| **域权限** | 域内资源的操作权限 | `domain:member`, `domain:admin` |
| **组织权限** | 组织内资源的操作权限 | `org:member`, `org:admin` |
| **系统权限** | 系统级操作权限 | `system:config`, `system:deploy` |

#### 2.2.3 权限检查器

```java
package net.ooder.sdk.core.security;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PermissionChecker {
    
    CompletableFuture<Boolean> checkPermission(CoreIdentity identity, String resource, String action);
    
    CompletableFuture<List<CorePermission>> getPermissions(CoreIdentity identity);
    
    CompletableFuture<Void> grantPermission(CoreIdentity identity, CorePermission permission);
    
    CompletableFuture<Void> revokePermission(CoreIdentity identity, String permissionId);
}
```

### 2.3 加密模型

#### 2.3.1 加密接口

```java
package net.ooder.sdk.core.security;

import java.util.concurrent.CompletableFuture;

public interface CoreEncryption {
    
    String getAlgorithm();
    
    byte[] encrypt(byte[] data, EncryptionContext context) throws EncryptionException;
    
    byte[] decrypt(byte[] encryptedData, EncryptionContext context) throws EncryptionException;
    
    String generateKey();
    
    boolean verifySignature(byte[] data, byte[] signature, EncryptionContext context);
    
    byte[] sign(byte[] data, EncryptionContext context) throws EncryptionException;
    
    CompletableFuture<byte[]> encryptAsync(byte[] data, EncryptionContext context);
    
    CompletableFuture<byte[]> decryptAsync(byte[] encryptedData, EncryptionContext context);
}
```

#### 2.3.2 加密上下文

```java
package net.ooder.sdk.core.security;

import java.util.Map;

public class EncryptionContext {
    
    private String algorithm;
    private String keyId;
    private String key;
    private Map<String, Object> parameters;
    
    public static EncryptionContext aes256(String key) {
        EncryptionContext context = new EncryptionContext();
        context.setAlgorithm("AES-256");
        context.setKey(key);
        return context;
    }
    
    public static EncryptionContext rsa(String keyId) {
        EncryptionContext context = new EncryptionContext();
        context.setAlgorithm("RSA");
        context.setKeyId(keyId);
        return context;
    }
}
```

## 3. 南向安全服务

### 3.1 基础认证

#### 3.1.1 认证接口

```java
package net.ooder.sdk.service.security.south;

import java.util.concurrent.CompletableFuture;

public interface SouthSecurityService {
    
    CompletableFuture<AuthenticationResult> authenticate(String username, String password);
    
    CompletableFuture<TokenResult> generateToken(String userId);
    
    CompletableFuture<Boolean> validateToken(String token);
    
    CompletableFuture<Boolean> checkPermission(String userId, String resource, String action);
    
    CompletableFuture<byte[]> encrypt(String data);
    
    CompletableFuture<String> decrypt(byte[] encryptedData);
}
```

#### 3.1.2 认证结果

```java
package net.ooder.sdk.service.security.south;

public class AuthenticationResult {
    
    private boolean success;
    private String userId;
    private String token;
    private String errorCode;
    private String errorMessage;
    
    public static AuthenticationResult success(String userId, String token) {
        AuthenticationResult result = new AuthenticationResult();
        result.setSuccess(true);
        result.setUserId(userId);
        result.setToken(token);
        return result;
    }
    
    public static AuthenticationResult failure(String errorCode, String errorMessage) {
        AuthenticationResult result = new AuthenticationResult();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
```

#### 3.1.3 与ooder-common集成

基于 ooder-common 的 OrgManager 实现认证：

```java
package net.ooder.sdk.service.security.south.impl;

import net.ooder.org.OrgManager;
import net.ooder.org.Person;
import net.ooder.org.PersonNotFoundException;

public class SouthSecurityServiceImpl implements SouthSecurityService {
    
    private OrgManager orgManager;
    private JwtTokenManager tokenManager;
    
    @Override
    public CompletableFuture<AuthenticationResult> authenticate(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean verified = orgManager.verifyPerson(username, password);
                if (verified) {
                    Person person = orgManager.getPersonByAccount(username);
                    String token = tokenManager.generateToken(person.getId());
                    return AuthenticationResult.success(person.getId(), token);
                }
                return AuthenticationResult.failure("AUTH_FAILED", "Invalid credentials");
            } catch (PersonNotFoundException e) {
                return AuthenticationResult.failure("USER_NOT_FOUND", "User not found");
            }
        });
    }
}
```

### 3.2 JWT令牌管理

#### 3.2.1 令牌生成

```java
package net.ooder.sdk.service.security.south;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtTokenManager {
    
    private String secretKey;
    private long expirationTime;
    
    public CompletableFuture<TokenResult> generateToken(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            String token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim("type", "access")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
            return TokenResult.success(token);
        });
    }
    
    public CompletableFuture<Boolean> validateToken(String token) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
    
    public CompletableFuture<String> getUserIdFromToken(String token) {
        return CompletableFuture.supplyAsync(() -> {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        });
    }
}
```

#### 3.2.2 令牌配置

```properties
ooder.sdk.south.security.jwt-secret=your-256-bit-secret-key-here
ooder.sdk.south.security.jwt-expiration=86400000
ooder.sdk.south.security.jwt-issuer=ooder-sdk
```

### 3.3 基础权限控制

#### 3.3.1 权限检查

```java
package net.ooder.sdk.service.security.south;

import java.util.concurrent.CompletableFuture;

public class BasicPermissionChecker implements PermissionChecker {
    
    private PermissionStore permissionStore;
    
    @Override
    public CompletableFuture<Boolean> checkPermission(CoreIdentity identity, String resource, String action) {
        return CompletableFuture.supplyAsync(() -> {
            List<CorePermission> permissions = permissionStore.getPermissions(identity.getIdentityId());
            return permissions.stream()
                .anyMatch(p -> p.getResource().equals(resource) && p.getAction().equals(action));
        });
    }
    
    @Override
    public CompletableFuture<Void> grantPermission(CoreIdentity identity, CorePermission permission) {
        return CompletableFuture.runAsync(() -> {
            permissionStore.addPermission(identity.getIdentityId(), permission);
        });
    }
    
    @Override
    public CompletableFuture<Void> revokePermission(CoreIdentity identity, String permissionId) {
        return CompletableFuture.runAsync(() -> {
            permissionStore.removePermission(identity.getIdentityId(), permissionId);
        });
    }
}
```

## 4. 北向安全服务

### 4.1 域级安全

#### 4.1.1 域安全接口

```java
package net.ooder.sdk.service.security.north;

import java.util.concurrent.CompletableFuture;

public interface NorthSecurityService {
    
    CompletableFuture<Boolean> checkDomainPermission(String userId, String domainId, Permission permission);
    
    CompletableFuture<DomainKey> generateDomainKey(String domainId);
    
    CompletableFuture<byte[]> encryptP2P(byte[] data, String peerId);
    
    CompletableFuture<byte[]> decryptP2P(byte[] encryptedData, String peerId);
    
    CompletableFuture<AccessControlResult> checkAccess(String userId, String resource, String action);
}
```

#### 4.1.2 域密钥管理

```java
package net.ooder.sdk.service.security.north;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DomainKeyManager {
    
    private Map<String, DomainKey> domainKeys = new ConcurrentHashMap<>();
    
    public CompletableFuture<DomainKey> generateDomainKey(String domainId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                KeyPair keyPair = keyGen.generateKeyPair();
                
                DomainKey domainKey = new DomainKey();
                domainKey.setDomainId(domainId);
                domainKey.setPublicKey(keyPair.getPublic().getEncoded());
                domainKey.setPrivateKey(keyPair.getPrivate().getEncoded());
                domainKey.setCreatedAt(System.currentTimeMillis());
                
                domainKeys.put(domainId, domainKey);
                return domainKey;
            } catch (Exception e) {
                throw new SecurityException("Failed to generate domain key", e);
            }
        });
    }
    
    public DomainKey getDomainKey(String domainId) {
        return domainKeys.get(domainId);
    }
}
```

#### 4.1.3 域权限检查

```java
package net.ooder.sdk.service.security.north.impl;

import java.util.concurrent.CompletableFuture;

public class DomainPermissionChecker {
    
    private DomainPolicyStore policyStore;
    
    public CompletableFuture<Boolean> checkDomainPermission(String userId, String domainId, Permission permission) {
        return CompletableFuture.supplyAsync(() -> {
            DomainPolicy policy = policyStore.getPolicy(domainId);
            if (policy == null) {
                return false;
            }
            
            if (!policy.getMembers().contains(userId)) {
                return false;
            }
            
            return policy.hasPermission(userId, permission);
        });
    }
}
```

### 4.2 P2P加密

#### 4.2.1 P2P加密服务

```java
package net.ooder.sdk.service.security.north;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class P2PEncryptionService {
    
    private Map<String, byte[]> peerKeys = new ConcurrentHashMap<>();
    private String algorithm = "AES";
    
    public CompletableFuture<byte[]> encryptP2P(byte[] data, String peerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] peerKey = peerKeys.get(peerId);
                if (peerKey == null) {
                    peerKey = generatePeerKey(peerId);
                }
                
                SecretKey secretKey = new SecretKeySpec(peerKey, algorithm);
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                throw new SecurityException("P2P encryption failed", e);
            }
        });
    }
    
    public CompletableFuture<byte[]> decryptP2P(byte[] encryptedData, String peerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] peerKey = peerKeys.get(peerId);
                if (peerKey == null) {
                    throw new SecurityException("Peer key not found");
                }
                
                SecretKey secretKey = new SecretKeySpec(peerKey, algorithm);
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return cipher.doFinal(encryptedData);
            } catch (Exception e) {
                throw new SecurityException("P2P decryption failed", e);
            }
        });
    }
    
    private byte[] generatePeerKey(String peerId) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        byte[] key = secretKey.getEncoded();
        peerKeys.put(peerId, key);
        return key;
    }
}
```

### 4.3 细粒度权限控制

#### 4.3.1 基于属性的访问控制（ABAC）

```java
package net.ooder.sdk.service.security.north;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AbacPermissionChecker {
    
    private PolicyDecisionPoint pdp;
    
    public CompletableFuture<AccessControlResult> checkAccess(
        String userId, 
        String resource, 
        String action,
        Map<String, Object> environment
    ) {
        return CompletableFuture.supplyAsync(() -> {
            AccessRequest request = new AccessRequest();
            request.setSubject(new Subject(userId));
            request.setResource(new Resource(resource));
            request.setAction(new Action(action));
            request.setEnvironment(environment);
            
            AccessDecision decision = pdp.evaluate(request);
            
            return AccessControlResult.fromDecision(decision);
        });
    }
}
```

#### 4.3.2 访问控制结果

```java
package net.ooder.sdk.service.security.north;

public class AccessControlResult {
    
    private boolean permitted;
    private String decision;
    private String reason;
    private Map<String, Object> obligations;
    
    public static AccessControlResult permit(String reason) {
        AccessControlResult result = new AccessControlResult();
        result.setPermitted(true);
        result.setDecision("PERMIT");
        result.setReason(reason);
        return result;
    }
    
    public static AccessControlResult deny(String reason) {
        AccessControlResult result = new AccessControlResult();
        result.setPermitted(false);
        result.setDecision("DENY");
        result.setReason(reason);
        return result;
    }
}
```

## 5. 用户-组织-域安全

### 5.1 用户安全

#### 5.1.1 用户身份

```java
package net.ooder.sdk.security.user;

import net.ooder.sdk.core.security.CoreIdentity;
import net.ooder.sdk.core.security.IdentityType;
import java.util.Set;

public class UserIdentity implements CoreIdentity {
    
    private String userId;
    private String username;
    private String email;
    private Set<UserRole> roles;
    private Set<String> domainIds;
    private Set<String> organizationIds;
    
    @Override
    public String getIdentityId() {
        return userId;
    }
    
    @Override
    public String getName() {
        return username;
    }
    
    @Override
    public IdentityType getType() {
        return IdentityType.USER;
    }
}
```

#### 5.1.2 用户角色

```java
package net.ooder.sdk.security.user;

public enum UserRole {
    USER,           // 普通用户
    ADMIN,          // 管理员
    DEVELOPER,      // 开发者
    ORG_ADMIN,      // 组织管理员
    DOMAIN_ADMIN    // 域管理员
}
```

### 5.2 组织安全

#### 5.2.1 组织权限模型

```java
package net.ooder.sdk.security.organization;

import java.util.List;

public class OrganizationPolicy {
    
    private String organizationId;
    private List<String> adminUserIds;
    private List<String> memberUserIds;
    private Map<String, List<String>> rolePermissions;
    
    public boolean isAdmin(String userId) {
        return adminUserIds.contains(userId);
    }
    
    public boolean isMember(String userId) {
        return memberUserIds.contains(userId);
    }
    
    public boolean hasPermission(String userId, String permission) {
        for (Map.Entry<String, List<String>> entry : rolePermissions.entrySet()) {
            if (entry.getValue().contains(permission)) {
                return true;
            }
        }
        return false;
    }
}
```

### 5.3 域安全

#### 5.3.1 域策略

```java
package net.ooder.sdk.security.domain;

import java.util.List;
import java.util.Map;

public class DomainPolicy {
    
    private String domainId;
    private DomainType domainType;
    private String ownerId;
    private List<String> memberIds;
    private Map<String, List<Permission>> memberPermissions;
    private List<SharingRule> sharingRules;
    
    public boolean isOwner(String userId) {
        return ownerId.equals(userId);
    }
    
    public boolean isMember(String userId) {
        return memberIds.contains(userId);
    }
    
    public boolean hasPermission(String userId, Permission permission) {
        List<Permission> permissions = memberPermissions.get(userId);
        return permissions != null && permissions.contains(permission);
    }
}
```

#### 5.3.2 域类型

```java
package net.ooder.sdk.security.domain;

public enum DomainType {
    USER,           // 用户域
    ORGANIZATION,   // 组织域
    GLOBAL          // 全局域
}
```

## 6. 安全配置

### 6.1 南向安全配置

```properties
ooder.sdk.south.security.enabled=true
ooder.sdk.south.security.jwt-secret=your-256-bit-secret-key
ooder.sdk.south.security.jwt-expiration=86400000
ooder.sdk.south.security.jwt-issuer=ooder-sdk
ooder.sdk.south.security.https-enabled=true
ooder.sdk.south.security.cors-allowed-origins=*
ooder.sdk.south.security.cors-allowed-methods=GET,POST,PUT,DELETE
```

### 6.2 北向安全配置

```properties
ooder.sdk.north.security.enabled=true
ooder.sdk.north.security.domain-key-algorithm=RSA
ooder.sdk.north.security.domain-key-size=2048
ooder.sdk.north.security.p2p-encryption-enabled=true
ooder.sdk.north.security.p2p-encryption-algorithm=AES-256
ooder.sdk.north.security.abac-enabled=true
```

### 6.3 审计配置

```properties
ooder.sdk.security.audit.enabled=true
ooder.sdk.security.audit.log-level=INFO
ooder.sdk.security.audit.log-file=logs/security-audit.log
ooder.sdk.security.audit.log-rotation=daily
ooder.sdk.security.audit.log-retention=30
```

## 7. 安全最佳实践

### 7.1 认证最佳实践

1. **使用强密码策略**：密码长度至少8位，包含大小写字母、数字和特殊字符
2. **启用多因素认证**：对敏感操作启用多因素认证
3. **令牌安全**：JWT令牌使用HTTPS传输，设置合理的过期时间
4. **会话管理**：设置合理的会话超时时间，及时清理过期会话

### 7.2 授权最佳实践

1. **最小权限原则**：只授予完成任务所需的最小权限
2. **角色分离**：不同角色使用不同的权限集合
3. **定期审计**：定期审计用户权限，清理不必要的权限
4. **权限继承**：合理使用权限继承，避免权限膨胀

### 7.3 加密最佳实践

1. **传输加密**：所有网络传输使用TLS加密
2. **存储加密**：敏感数据存储时加密
3. **密钥管理**：使用安全的密钥管理系统
4. **密钥轮换**：定期轮换加密密钥

### 7.4 审计最佳实践

1. **完整记录**：记录所有安全相关操作
2. **日志保护**：保护审计日志不被篡改
3. **定期分析**：定期分析审计日志，发现安全风险
4. **告警机制**：建立安全事件告警机制

## 8. 安全检查清单

### 8.1 认证检查

- [ ] 密码策略是否配置正确
- [ ] JWT密钥是否足够复杂
- [ ] 令牌过期时间是否合理
- [ ] 会话超时是否配置

### 8.2 授权检查

- [ ] 权限模型是否正确配置
- [ ] 角色权限是否合理分配
- [ ] 域权限是否正确设置
- [ ] 组织权限是否正确配置

### 8.3 加密检查

- [ ] HTTPS是否启用
- [ ] 加密算法是否安全
- [ ] 密钥是否安全存储
- [ ] 密钥轮换是否配置

### 8.4 审计检查

- [ ] 审计日志是否启用
- [ ] 日志级别是否正确
- [ ] 日志存储是否安全
- [ ] 日志分析是否配置

## 9. 总结

Ooder Agent SDK 0.7.2 版本的安全体系提供了完整的安全保障：

1. **分层安全**：南向基础安全，北向高级安全
2. **域级隔离**：基于域的资源隔离和权限控制
3. **细粒度权限**：基于角色和属性的访问控制
4. **端到端加密**：传输和存储数据的加密保护
5. **完整审计**：所有安全操作的审计追踪

---

**Ooder Agent SDK 0.7.2** - 构建智能、协作、安全的Agent生态系统！

# Ooder Agent SDK 南向协议闭环推导设计

## 1. 场景用例概述

### 1.1 核心场景流程

```
用户安装Nexus
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 阶段1：网络发现与角色决策                                        │
├─────────────────────────────────────────────────────────────────┤
│ 1. 启动混合发现机制                                              │
│    ├── UDP广播：宣告自身存在                                     │
│    ├── HTTP查询：查询局域网/组织域已知MCP                        │
│    └── 配置文件：读取预配置的MCP地址                             │
│                                                                 │
│ 2. MCP存在性检测                                                │
│    ├── 有MCP → 降级为RouteAgent                                 │
│    └── 无MCP → 升级为McpAgent                                   │
│                                                                 │
│ 3. 角色初始化                                                    │
│    ├── RouteAgent：向MCP注册，等待任务分配                       │
│    └── McpAgent：初始化网络，开始场景和组自动化操作              │
└─────────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 阶段2：登录与域策略响应                                          │
├─────────────────────────────────────────────────────────────────┤
│ 1. 登录触发                                                      │
│    ├── 用户主动登录                                              │
│    └── 配置文件自动登录                                          │
│                                                                 │
│ 2. 域策略检测                                                    │
│    ├── 有域策略 → 自动响应域配置                                 │
│    │   ├── 自动关联域存储                                        │
│    │   ├── 自动关联技能中心                                      │
│    │   └── 应用域安全策略                                        │
│    └── 无域策略 → 开启雷达模式                                   │
│        └── 公共资源检索                                          │
└─────────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────────┐
│ 阶段3：正常运行与协作                                            │
├─────────────────────────────────────────────────────────────────┤
│ 1. 场景组自动化                                                  │
│ 2. 技能执行与协作                                                │
│ 3. 数据同步与存储                                                │
│ 4. 网络维护与故障恢复                                            │
└─────────────────────────────────────────────────────────────────┘
```

## 2. 协议设计原则

### 2.1 南向协议核心原则

| 原则 | 描述 | 实现方式 |
|------|------|----------|
| **确定性优先** | 优先使用HTTP等确定性协议 | HTTP作为主协议，UDP作为辅助 |
| **渐进式发现** | 从局域网到组织域再到公共域 | 分层发现机制 |
| **角色自适应** | 根据网络环境自动调整角色 | 动态角色决策 |
| **域策略驱动** | 以域策略为核心驱动配置 | 策略响应机制 |
| **闭环验证** | 每个场景都需要闭环验证 | 场景验证框架 |

### 2.2 协议分层设计

```
┌─────────────────────────────────────────────────────────────────┐
│                    应用层协议                                    │
│  - 场景组协议  - 技能执行协议  - 数据同步协议                    │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                    角色协议层                                    │
│  - McpAgent协议  - RouteAgent协议  - EndAgent协议               │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                    发现协议层                                    │
│  - UDP广播协议  - HTTP查询协议  - 配置发现协议                   │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                    传输协议层                                    │
│  - HTTP/HTTPS  - WebSocket  - TCP                               │
└─────────────────────────────────────────────────────────────────┘
```

## 3. 阶段1：网络发现与角色决策

### 3.1 混合发现协议

#### 3.1.1 发现协议接口

```java
package net.ooder.sdk.protocol.south.discovery;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiscoveryProtocol {
    
    String getProtocolType();
    
    CompletableFuture<List<PeerInfo>> discover(DiscoveryContext context);
    
    void announce(Announcement announcement);
    
    void startListening();
    
    void stopListening();
}

public class DiscoveryContext {
    
    private DiscoveryScope scope;
    private String domainId;
    private String organizationId;
    private int timeout;
    private Map<String, Object> parameters;
    
    public enum DiscoveryScope {
        LOCAL_NETWORK,    // 局域网
        ORGANIZATION,     // 组织域
        GLOBAL            // 公共域
    }
}

public class PeerInfo {
    
    private String peerId;
    private String peerType;        // "MCP", "ROUTE", "END"
    private String endpoint;
    private String domainId;
    private String organizationId;
    private Map<String, Object> capabilities;
    private long discoveredTime;
}
```

#### 3.1.2 UDP广播发现协议

```java
package net.ooder.sdk.protocol.south.discovery.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UdpDiscoveryProtocol implements DiscoveryProtocol {
    
    private int broadcastPort = 9001;
    private DatagramSocket socket;
    private boolean listening = false;
    
    @Override
    public String getProtocolType() {
        return "UDP_BROADCAST";
    }
    
    @Override
    public CompletableFuture<List<PeerInfo>> discover(DiscoveryContext context) {
        return CompletableFuture.supplyAsync(() -> {
            List<PeerInfo> peers = new ArrayList<>();
            
            try {
                byte[] query = createDiscoveryQuery(context);
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet = new DatagramPacket(query, query.length, broadcastAddress, broadcastPort);
                socket.send(packet);
                
                long startTime = System.currentTimeMillis();
                byte[] buffer = new byte[4096];
                
                while (System.currentTimeMillis() - startTime < context.getTimeout()) {
                    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                    socket.receive(response);
                    
                    PeerInfo peer = parsePeerInfo(response.getData());
                    if (peer != null && matchesScope(peer, context.getScope())) {
                        peers.add(peer);
                    }
                }
            } catch (Exception e) {
                log.error("UDP discovery failed", e);
            }
            
            return peers;
        });
    }
    
    @Override
    public void announce(Announcement announcement) {
        try {
            byte[] data = serializeAnnouncement(announcement);
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(data, data.length, broadcastAddress, broadcastPort);
            socket.send(packet);
        } catch (Exception e) {
            log.error("UDP announcement failed", e);
        }
    }
    
    private byte[] createDiscoveryQuery(DiscoveryContext context) {
        Map<String, Object> query = new HashMap<>();
        query.put("type", "DISCOVERY_QUERY");
        query.put("scope", context.getScope().name());
        query.put("domainId", context.getDomainId());
        query.put("organizationId", context.getOrganizationId());
        query.put("timestamp", System.currentTimeMillis());
        return JsonUtil.toJsonBytes(query);
    }
}
```

#### 3.1.3 HTTP查询发现协议

```java
package net.ooder.sdk.protocol.south.discovery.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpDiscoveryProtocol implements DiscoveryProtocol {
    
    private HttpClient httpClient;
    private List<String> knownEndpoints;
    
    @Override
    public String getProtocolType() {
        return "HTTP_QUERY";
    }
    
    @Override
    public CompletableFuture<List<PeerInfo>> discover(DiscoveryContext context) {
        return CompletableFuture.supplyAsync(() -> {
            List<PeerInfo> allPeers = new ArrayList<>();
            
            for (String endpoint : knownEndpoints) {
                try {
                    String url = endpoint + "/api/discovery/peers";
                    if (context.getDomainId() != null) {
                        url += "?domainId=" + context.getDomainId();
                    }
                    
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .GET()
                        .timeout(Duration.ofSeconds(5))
                        .build();
                    
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    
                    if (response.statusCode() == 200) {
                        List<PeerInfo> peers = parsePeers(response.body());
                        allPeers.addAll(peers);
                    }
                } catch (Exception e) {
                    log.warn("HTTP discovery failed for endpoint: " + endpoint);
                }
            }
            
            return allPeers;
        });
    }
    
    @Override
    public void announce(Announcement announcement) {
        for (String endpoint : knownEndpoints) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint + "/api/discovery/announce"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(announcement)))
                    .timeout(Duration.ofSeconds(5))
                    .build();
                
                httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            } catch (Exception e) {
                log.warn("HTTP announcement failed for endpoint: " + endpoint);
            }
        }
    }
}
```

#### 3.1.4 配置文件发现协议

```java
package net.ooder.sdk.protocol.south.discovery.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConfigDiscoveryProtocol implements DiscoveryProtocol {
    
    private ConfigFile configFile;
    
    @Override
    public String getProtocolType() {
        return "CONFIG_FILE";
    }
    
    @Override
    public CompletableFuture<List<PeerInfo>> discover(DiscoveryContext context) {
        return CompletableFuture.supplyAsync(() -> {
            List<PeerInfo> peers = new ArrayList<>();
            
            List<PeerConfig> peerConfigs = configFile.getPeerConfigs();
            for (PeerConfig config : peerConfigs) {
                if (matchesScope(config, context.getScope())) {
                    PeerInfo peer = new PeerInfo();
                    peer.setPeerId(config.getPeerId());
                    peer.setPeerType(config.getPeerType());
                    peer.setEndpoint(config.getEndpoint());
                    peer.setDomainId(config.getDomainId());
                    peer.setOrganizationId(config.getOrganizationId());
                    peers.add(peer);
                }
            }
            
            return peers;
        });
    }
    
    @Override
    public void announce(Announcement announcement) {
        // 配置文件协议不支持主动宣告
    }
}
```

### 3.2 角色决策协议

#### 3.2.1 角色决策服务

```java
package net.ooder.sdk.protocol.south.role;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RoleDecisionService {
    
    CompletableFuture<RoleDecision> decideRole(DiscoveryResult discoveryResult);
    
    CompletableFuture<Void> downgradeToRouteAgent(String mcpEndpoint);
    
    CompletableFuture<Void> upgradeToMcpAgent();
}

public class RoleDecision {
    
    private AgentRole role;
    private String targetEndpoint;
    private String reason;
    private Map<String, Object> parameters;
    
    public enum AgentRole {
        MCP_AGENT,
        ROUTE_AGENT,
        END_AGENT
    }
}
```

#### 3.2.2 角色决策实现

```java
package net.ooder.sdk.protocol.south.role.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoleDecisionServiceImpl implements RoleDecisionService {
    
    private AgentFactory agentFactory;
    private ConfigService configService;
    
    @Override
    public CompletableFuture<RoleDecision> decideRole(DiscoveryResult discoveryResult) {
        return CompletableFuture.supplyAsync(() -> {
            List<PeerInfo> mcpPeers = discoveryResult.getMcpPeers();
            
            if (!mcpPeers.isEmpty()) {
                PeerInfo selectedMcp = selectBestMcp(mcpPeers);
                
                return new RoleDecision(
                    AgentRole.ROUTE_AGENT,
                    selectedMcp.getEndpoint(),
                    "Found existing MCP in network"
                );
            }
            
            return new RoleDecision(
                AgentRole.MCP_AGENT,
                null,
                "No MCP found in network, upgrading to MCP"
            );
        });
    }
    
    @Override
    public CompletableFuture<Void> downgradeToRouteAgent(String mcpEndpoint) {
        return CompletableFuture.runAsync(() -> {
            RouteAgent routeAgent = agentFactory.createRouteAgent();
            
            routeAgent.register(mcpEndpoint).join();
            
            routeAgent.start();
            
            log.info("Downgraded to RouteAgent, registered to MCP: " + mcpEndpoint);
        });
    }
    
    @Override
    public CompletableFuture<Void> upgradeToMcpAgent() {
        return CompletableFuture.runAsync(() -> {
            McpAgent mcpAgent = agentFactory.createMcpAgent();
            
            mcpAgent.register().join();
            
            mcpAgent.start();
            
            initializeSceneGroups(mcpAgent);
            
            log.info("Upgraded to McpAgent, network initialized");
        });
    }
    
    private PeerInfo selectBestMcp(List<PeerInfo> mcpPeers) {
        return mcpPeers.stream()
            .filter(p -> p.getCapabilities().containsKey("sceneGroupSupport"))
            .max((p1, p2) -> {
                int score1 = calculateMcpScore(p1);
                int score2 = calculateMcpScore(p2);
                return Integer.compare(score1, score2);
            })
            .orElse(mcpPeers.get(0));
    }
    
    private int calculateMcpScore(PeerInfo peer) {
        int score = 0;
        
        if (peer.getDomainId() != null) score += 10;
        if (peer.getOrganizationId() != null) score += 5;
        if (peer.getCapabilities().containsKey("highAvailability")) score += 3;
        
        return score;
    }
}
```

### 3.3 阶段1闭环验证

```java
package net.ooder.sdk.protocol.south.validation;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Phase1Validation {
    
    private DiscoveryProtocol udpDiscovery;
    private DiscoveryProtocol httpDiscovery;
    private DiscoveryProtocol configDiscovery;
    private RoleDecisionService roleDecision;
    
    public CompletableFuture<ValidationResult> validate() {
        return CompletableFuture.supplyAsync(() -> {
            ValidationResult result = new ValidationResult("Phase1: Network Discovery & Role Decision");
            
            // 验证场景1：局域网有MCP
            ValidationResult scenario1 = validateScenario1();
            result.addScenarioResult(scenario1);
            
            // 验证场景2：局域网无MCP
            ValidationResult scenario2 = validateScenario2();
            result.addScenarioResult(scenario2);
            
            // 验证场景3：组织域有MCP
            ValidationResult scenario3 = validateScenario3();
            result.addScenarioResult(scenario3);
            
            // 验证场景4：配置文件指定MCP
            ValidationResult scenario4 = validateScenario4();
            result.addScenarioResult(scenario4);
            
            return result;
        });
    }
    
    private ValidationResult validateScenario1() {
        // 场景：局域网有MCP
        // 预期：降级为RouteAgent
        // 验证：成功注册到MCP
        return ValidationResult.scenario("局域网有MCP")
            .given("局域网中存在MCP节点")
            .when("启动发现流程")
            .then("应降级为RouteAgent并注册到MCP")
            .validate(() -> {
                // 模拟发现MCP
                DiscoveryContext context = new DiscoveryContext(DiscoveryScope.LOCAL_NETWORK);
                List<PeerInfo> peers = udpDiscovery.discover(context).join();
                
                // 验证发现结果
                if (peers.stream().noneMatch(p -> "MCP".equals(p.getPeerType()))) {
                    return ValidationFailure.of("未发现MCP节点");
                }
                
                // 验证角色决策
                RoleDecision decision = roleDecision.decideRole(new DiscoveryResult(peers)).join();
                if (decision.getRole() != AgentRole.ROUTE_AGENT) {
                    return ValidationFailure.of("角色决策错误，应为RouteAgent");
                }
                
                return ValidationSuccess.of("成功降级为RouteAgent");
            });
    }
    
    private ValidationResult validateScenario2() {
        // 场景：局域网无MCP
        // 预期：升级为McpAgent
        // 验证：成功初始化网络
        return ValidationResult.scenario("局域网无MCP")
            .given("局域网中不存在MCP节点")
            .when("启动发现流程")
            .then("应升级为McpAgent并初始化网络")
            .validate(() -> {
                DiscoveryContext context = new DiscoveryContext(DiscoveryScope.LOCAL_NETWORK);
                List<PeerInfo> peers = udpDiscovery.discover(context).join();
                
                if (peers.stream().anyMatch(p -> "MCP".equals(p.getPeerType()))) {
                    return ValidationFailure.of("发现意外的MCP节点");
                }
                
                RoleDecision decision = roleDecision.decideRole(new DiscoveryResult(peers)).join();
                if (decision.getRole() != AgentRole.MCP_AGENT) {
                    return ValidationFailure.of("角色决策错误，应为McpAgent");
                }
                
                return ValidationSuccess.of("成功升级为McpAgent");
            });
    }
}
```

## 4. 阶段2：登录与域策略响应

### 4.1 登录协议

#### 4.1.1 登录服务接口

```java
package net.ooder.sdk.protocol.south.login;

import java.util.concurrent.CompletableFuture;

public interface LoginProtocol {
    
    CompletableFuture<LoginResult> login(LoginRequest request);
    
    CompletableFuture<LoginResult> autoLogin(ConfigFile config);
    
    CompletableFuture<Void> logout();
    
    CompletableFuture<Boolean> isLoggedIn();
}

public class LoginRequest {
    
    private String username;
    private String password;
    private String domainId;
    private String organizationId;
    private LoginType type;
    
    public enum LoginType {
        USER_LOGIN,         // 用户主动登录
        AUTO_LOGIN,         // 配置自动登录
        DOMAIN_LOGIN        // 域登录
    }
}

public class LoginResult {
    
    private boolean success;
    private String userId;
    private String token;
    private DomainPolicy domainPolicy;
    private List<String> availableDomains;
    private String errorMessage;
}
```

#### 4.1.2 登录协议实现

```java
package net.ooder.sdk.protocol.south.login.impl;

import java.util.concurrent.CompletableFuture;

public class LoginProtocolImpl implements LoginProtocol {
    
    private AuthService authService;
    private DomainPolicyService domainPolicyService;
    private ConfigService configService;
    
    @Override
    public CompletableFuture<LoginResult> login(LoginRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // 1. 认证
            AuthenticationResult authResult = authService.authenticate(
                request.getUsername(),
                request.getPassword()
            ).join();
            
            if (!authResult.isSuccess()) {
                return LoginResult.failure(authResult.getErrorMessage());
            }
            
            // 2. 获取域策略
            DomainPolicy policy = null;
            if (request.getDomainId() != null) {
                policy = domainPolicyService.getPolicy(request.getDomainId()).join();
            }
            
            // 3. 获取可用域列表
            List<String> domains = domainPolicyService.getAvailableDomains(authResult.getUserId()).join();
            
            return LoginResult.success(
                authResult.getUserId(),
                authResult.getToken(),
                policy,
                domains
            );
        });
    }
    
    @Override
    public CompletableFuture<LoginResult> autoLogin(ConfigFile config) {
        return CompletableFuture.supplyAsync(() -> {
            LoginConfig loginConfig = config.getLoginConfig();
            
            if (loginConfig == null || !loginConfig.isAutoLoginEnabled()) {
                return LoginResult.failure("Auto login not configured");
            }
            
            LoginRequest request = new LoginRequest();
            request.setUsername(loginConfig.getUsername());
            request.setPassword(loginConfig.getPassword());
            request.setDomainId(loginConfig.getDomainId());
            request.setOrganizationId(loginConfig.getOrganizationId());
            request.setType(LoginType.AUTO_LOGIN);
            
            return login(request).join();
        });
    }
}
```

### 4.2 域策略响应协议

#### 4.2.1 域策略服务接口

```java
package net.ooder.sdk.protocol.south.domain;

import java.util.concurrent.CompletableFuture;

public interface DomainPolicyProtocol {
    
    CompletableFuture<DomainPolicy> getPolicy(String domainId);
    
    CompletableFuture<Void> applyPolicy(DomainPolicy policy);
    
    CompletableFuture<Void> configureStorage(StorageConfig config);
    
    CompletableFuture<Void> configureSkillCenter(SkillCenterConfig config);
    
    CompletableFuture<Void> configureSecurity(SecurityConfig config);
}

public class DomainPolicy {
    
    private String domainId;
    private String domainName;
    private DomainType domainType;
    private StoragePolicy storagePolicy;
    private SkillCenterPolicy skillCenterPolicy;
    private SecurityPolicy securityPolicy;
    private NetworkPolicy networkPolicy;
    private List<String> requiredSkills;
    private Map<String, Object> customConfig;
}
```

#### 4.2.2 域策略响应实现

```java
package net.ooder.sdk.protocol.south.domain.impl;

import java.util.concurrent.CompletableFuture;

public class DomainPolicyProtocolImpl implements DomainPolicyProtocol {
    
    private StorageService storageService;
    private SkillCenterService skillCenterService;
    private SecurityService securityService;
    
    @Override
    public CompletableFuture<Void> applyPolicy(DomainPolicy policy) {
        return CompletableFuture.runAsync(() -> {
            // 1. 配置存储
            if (policy.getStoragePolicy() != null) {
                configureStorage(policy.getStoragePolicy().toConfig()).join();
            }
            
            // 2. 配置技能中心
            if (policy.getSkillCenterPolicy() != null) {
                configureSkillCenter(policy.getSkillCenterPolicy().toConfig()).join();
            }
            
            // 3. 配置安全
            if (policy.getSecurityPolicy() != null) {
                configureSecurity(policy.getSecurityPolicy().toConfig()).join();
            }
            
            // 4. 安装必需技能
            for (String skillId : policy.getRequiredSkills()) {
                skillCenterService.installSkill(skillId).join();
            }
            
            log.info("Domain policy applied: " + policy.getDomainId());
        });
    }
    
    @Override
    public CompletableFuture<Void> configureStorage(StorageConfig config) {
        return CompletableFuture.runAsync(() -> {
            storageService.configure(config);
            log.info("Storage configured: " + config.getType());
        });
    }
    
    @Override
    public CompletableFuture<Void> configureSkillCenter(SkillCenterConfig config) {
        return CompletableFuture.runAsync(() -> {
            skillCenterService.configure(config);
            log.info("Skill center configured: " + config.getEndpoint());
        });
    }
}
```

### 4.3 雷达模式协议

#### 4.3.1 雷达服务接口

```java
package net.ooder.sdk.protocol.south.radar;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RadarProtocol {
    
    CompletableFuture<List<PublicResource>> scan(RadarScanRequest request);
    
    CompletableFuture<List<SkillInfo>> searchSkills(String query);
    
    CompletableFuture<List<StorageInfo>> searchStorage(String query);
    
    CompletableFuture<List<NetworkInfo>> searchNetworks(String query);
}

public class RadarScanRequest {
    
    private RadarScope scope;
    private List<String> resourceTypes;
    private int timeout;
    private Map<String, Object> filters;
    
    public enum RadarScope {
        PUBLIC_DOMAIN,      // 公共域
        ORGANIZATION,       // 组织域
        LOCAL_NETWORK       // 局域网
    }
}

public class PublicResource {
    
    private String resourceId;
    private String resourceType;
    private String name;
    private String description;
    private String source;
    private Map<String, Object> metadata;
}
```

#### 4.3.2 雷达模式实现

```java
package net.ooder.sdk.protocol.south.radar.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RadarProtocolImpl implements RadarProtocol {
    
    private PublicResourceRegistry registry;
    private DiscoveryProtocol discoveryProtocol;
    
    @Override
    public CompletableFuture<List<PublicResource>> scan(RadarScanRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            List<PublicResource> resources = new ArrayList<>();
            
            // 1. 扫描公共域
            if (request.getScope() == RadarScope.PUBLIC_DOMAIN) {
                List<PublicResource> publicResources = registry.listPublicResources();
                resources.addAll(publicResources);
            }
            
            // 2. 扫描组织域
            if (request.getScope() == RadarScope.ORGANIZATION) {
                DiscoveryContext context = new DiscoveryContext(DiscoveryScope.ORGANIZATION);
                List<PeerInfo> peers = discoveryProtocol.discover(context).join();
                
                for (PeerInfo peer : peers) {
                    resources.addAll(fetchResourcesFromPeer(peer));
                }
            }
            
            // 3. 扫描局域网
            if (request.getScope() == RadarScope.LOCAL_NETWORK) {
                DiscoveryContext context = new DiscoveryContext(DiscoveryScope.LOCAL_NETWORK);
                List<PeerInfo> peers = discoveryProtocol.discover(context).join();
                
                for (PeerInfo peer : peers) {
                    resources.addAll(fetchResourcesFromPeer(peer));
                }
            }
            
            return filterResources(resources, request.getFilters());
        });
    }
}
```

### 4.4 阶段2闭环验证

```java
package net.ooder.sdk.protocol.south.validation;

public class Phase2Validation {
    
    private LoginProtocol loginProtocol;
    private DomainPolicyProtocol domainPolicyProtocol;
    private RadarProtocol radarProtocol;
    
    public CompletableFuture<ValidationResult> validate() {
        return CompletableFuture.supplyAsync(() -> {
            ValidationResult result = new ValidationResult("Phase2: Login & Domain Policy Response");
            
            // 验证场景1：用户主动登录，有域策略
            result.addScenarioResult(validateUserLoginWithDomainPolicy());
            
            // 验证场景2：配置自动登录，有域策略
            result.addScenarioResult(validateAutoLoginWithDomainPolicy());
            
            // 验证场景3：登录后无域策略，开启雷达
            result.addScenarioResult(validateLoginWithoutDomainPolicy());
            
            // 验证场景4：域策略自动配置
            result.addScenarioResult(validateDomainPolicyAutoConfig());
            
            return result;
        });
    }
    
    private ValidationResult validateUserLoginWithDomainPolicy() {
        return ValidationResult.scenario("用户主动登录，有域策略")
            .given("用户有域成员资格")
            .when("用户主动登录")
            .then("应自动响应域策略并完成配置")
            .validate(() -> {
                LoginRequest request = new LoginRequest();
                request.setUsername("user001");
                request.setPassword("password");
                request.setDomainId("domain001");
                request.setType(LoginType.USER_LOGIN);
                
                LoginResult loginResult = loginProtocol.login(request).join();
                
                if (!loginResult.isSuccess()) {
                    return ValidationFailure.of("登录失败");
                }
                
                if (loginResult.getDomainPolicy() == null) {
                    return ValidationFailure.of("未获取到域策略");
                }
                
                // 验证域策略应用
                domainPolicyProtocol.applyPolicy(loginResult.getDomainPolicy()).join();
                
                // 验证存储配置
                if (!storageService.isConfigured()) {
                    return ValidationFailure.of("存储未配置");
                }
                
                // 验证技能中心配置
                if (!skillCenterService.isConfigured()) {
                    return ValidationFailure.of("技能中心未配置");
                }
                
                return ValidationSuccess.of("成功响应域策略");
            });
    }
    
    private ValidationResult validateLoginWithoutDomainPolicy() {
        return ValidationResult.scenario("登录后无域策略，开启雷达")
            .given("用户无域成员资格")
            .when("用户登录")
            .then("应开启雷达模式进行公共资源检索")
            .validate(() -> {
                LoginRequest request = new LoginRequest();
                request.setUsername("user002");
                request.setPassword("password");
                request.setType(LoginType.USER_LOGIN);
                
                LoginResult loginResult = loginProtocol.login(request).join();
                
                if (!loginResult.isSuccess()) {
                    return ValidationFailure.of("登录失败");
                }
                
                if (loginResult.getDomainPolicy() != null) {
                    return ValidationFailure.of("不应有域策略");
                }
                
                // 验证雷达模式
                RadarScanRequest scanRequest = new RadarScanRequest();
                scanRequest.setScope(RadarScope.PUBLIC_DOMAIN);
                
                List<PublicResource> resources = radarProtocol.scan(scanRequest).join();
                
                if (resources.isEmpty()) {
                    return ValidationFailure.of("雷达扫描无结果");
                }
                
                return ValidationSuccess.of("成功开启雷达模式");
            });
    }
}
```

## 5. 阶段3：正常运行与协作

### 5.1 场景组自动化协议

```java
package net.ooder.sdk.protocol.south.scene;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SceneGroupProtocol {
    
    CompletableFuture<SceneGroup> createSceneGroup(String sceneId, SceneGroupConfig config);
    
    CompletableFuture<Void> autoJoinSceneGroups();
    
    CompletableFuture<Void> leaveSceneGroup(String sceneGroupId);
    
    CompletableFuture<List<SceneGroup>> listSceneGroups();
    
    CompletableFuture<SceneGroupStatus> getStatus(String sceneGroupId);
}
```

### 5.2 技能执行协议

```java
package net.ooder.sdk.protocol.south.skill;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface SkillExecutionProtocol {
    
    CompletableFuture<SkillResult> execute(String skillId, Map<String, Object> params);
    
    CompletableFuture<SkillResult> executeWithRetry(String skillId, Map<String, Object> params, int maxRetries);
    
    CompletableFuture<Void> install(String skillId);
    
    CompletableFuture<Void> uninstall(String skillId);
    
    CompletableFuture<List<SkillInfo>> listInstalledSkills();
}
```

### 5.3 数据同步协议

```java
package net.ooder.sdk.protocol.south.sync;

import java.util.concurrent.CompletableFuture;

public interface DataSyncProtocol {
    
    CompletableFuture<SyncResult> sync(String dataType);
    
    CompletableFuture<SyncResult> syncAll();
    
    CompletableFuture<Void> configureSync(SyncConfig config);
    
    CompletableFuture<SyncStatus> getSyncStatus();
}
```

## 6. 协议约定总结（SDK 7.1兼容）

### 6.1 协议类型映射

| SDK 7.1协议 | 0.7.2南向协议 | 说明 |
|-------------|---------------|------|
| ProtocolHub | SouthProtocolHub | 南向协议中心 |
| McpAgent协议 | McpAgentProtocol | MCP角色协议 |
| RouteAgent协议 | RouteAgentProtocol | Route角色协议 |
| CommandPacket | SouthCommandPacket | 南向命令包 |

### 6.2 协议兼容性

```java
public class ProtocolCompatibility {
    
    public static SouthCommandPacket fromSdk71(CommandPacket packet) {
        SouthCommandPacket southPacket = new SouthCommandPacket();
        southPacket.setPacketId(packet.getPacketId());
        southPacket.setHeader(packet.getHeader());
        southPacket.setPayload(packet.getPayload());
        return southPacket;
    }
    
    public static CommandPacket toSdk71(SouthCommandPacket southPacket) {
        CommandPacket packet = new CommandPacket();
        packet.setPacketId(southPacket.getPacketId());
        packet.setHeader(southPacket.getHeader());
        packet.setPayload(southPacket.getPayload());
        return packet;
    }
}
```

## 7. 完整闭环验证流程

```java
public class SouthboundProtocolValidation {
    
    public static void main(String[] args) {
        SouthboundProtocolValidation validation = new SouthboundProtocolValidation();
        
        // 阶段1验证
        ValidationResult phase1 = validation.validatePhase1().join();
        System.out.println("Phase1: " + phase1);
        
        // 阶段2验证
        ValidationResult phase2 = validation.validatePhase2().join();
        System.out.println("Phase2: " + phase2);
        
        // 阶段3验证
        ValidationResult phase3 = validation.validatePhase3().join();
        System.out.println("Phase3: " + phase3);
        
        // 总体验证报告
        ValidationReport report = ValidationReport.aggregate(phase1, phase2, phase3);
        System.out.println("Overall: " + report);
    }
}
```

---

**Ooder Agent SDK 0.7.2** - 南向协议闭环推导设计完成！

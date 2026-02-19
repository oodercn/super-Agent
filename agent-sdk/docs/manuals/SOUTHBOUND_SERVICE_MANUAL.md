# Ooder Agent SDK 南向服务手册

## 术语表

| 术语 | 英文 | 定义 | 示例 |
|------|------|------|------|
| **南向服务** | Southbound Service | 面向内部网络的服务层，提供简单确定性的服务 | HTTP通信、MCP端点通讯 |
| **MCP端点** | MCP Endpoint | MCP Agent的网络通讯端点，通讯只到达此端点结束 | `http://mcp-001:7070/api` |
| **Nexus** | Nexus | 用户安装的Agent节点实例 | 用户部署的Agent服务 |
| **混合发现** | Hybrid Discovery | 结合UDP、HTTP、配置文件的多渠道发现机制 | UDP广播+HTTP查询+配置文件 |
| **角色决策** | Role Decision | 根据网络环境自动决定Agent角色的过程 | 发现MCP→降级为RouteAgent |
| **域策略** | Domain Policy | 域级别的配置和安全策略 | 存储配置、技能中心关联 |
| **雷达模式** | Radar Mode | 无域策略时扫描公共资源的模式 | 公共技能检索、资源发现 |
| **闭环验证** | Closed-Loop Validation | 场景从开始到结束的完整验证过程 | 登录→域策略响应→配置完成 |
| **确定性网络** | Deterministic Network | 基于HTTP的稳定可靠网络连接 | HTTP长连接、连接池 |
| **场景组** | Scene Group | 多Agent协作的场景管理单元 | 任务协作组、技能执行组 |

## 1. 南向服务概述

### 1.1 设计目标

南向服务层面向内部网络，提供简单确定性的服务，核心目标包括：

| 目标 | 描述 |
|------|------|
| **简单确定性** | HTTP协议、确定性网络、MCP端点通讯 |
| **智能化** | LLM介入、智能安装、自动配置 |
| **自主性** | 自组网、离线运行、多场景闭环验证 |
| **可靠性** | 增强场景组、故障恢复、数据持久化 |

### 1.2 角色规格引用

南向服务涉及的角色详细需求请参考以下规格手册：

| 角色 | 规格手册 | 主要职责 |
|------|----------|----------|
| **Nexus（个人）** | [NEXUS_ROLE_SPECIFICATION.md](NEXUS_ROLE_SPECIFICATION.md) | 网络发现、登录认证、私有资源管理、离线运行、协作参与 |
| **管理（企业）** | [MANAGEMENT_ROLE_SPECIFICATION.md](MANAGEMENT_ROLE_SPECIFICATION.md) | 立体观测、协作协调、异常处理、策略管理、资源调度 |

> **注意**：角色相关的功能需求、场景闭环、数据流向、观测角度和参与者已在角色规格手册中详细定义，本手册聚焦于协议和技术实现。

### 1.3 能力中心引用

南向服务涉及的能力管理功能请参考能力中心规格手册：

| 能力功能 | 规格手册 | 主要职责 |
|----------|----------|----------|
| **能力规范** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 技能/场景的标准化定义和元数据管理 |
| **能力分发** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 能力从中心分发到目标节点 |
| **能力管理** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 能力的生命周期管理 |
| **能力监测** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 能力运行状态的实时监测 |
| **能力协同** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 多能力之间的协作编排 |

> **注意**：南向服务的技能执行、场景组自动化等功能通过能力中心进行能力管理和协同。

### 1.4 核心场景流程

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

### 1.3 服务组成

```
南向服务层（Southbound Service Layer）
├── 发现协议层（Discovery Protocol Layer）
│   ├── UDP广播发现协议
│   ├── HTTP查询发现协议
│   └── 配置文件发现协议
├── 角色协议层（Role Protocol Layer）
│   ├── 角色决策协议
│   ├── McpAgent协议
│   └── RouteAgent协议
├── 登录协议层（Login Protocol Layer）
│   ├── 用户登录协议
│   ├── 自动登录协议
│   └── 域策略响应协议
├── 协作协议层（Collaboration Protocol Layer）
│   ├── 场景组协议
│   ├── 技能执行协议
│   └── 数据同步协议
└── 网络协议层（Network Protocol Layer）
    ├── HTTP协议支持
    ├── 确定性网络
    └── MCP端点通讯
```

## 2. 阶段1：网络发现与角色决策

### 2.1 流程详细描述

#### 2.1.1 启动流程

```
Nexus启动
    │
    ├─→ 读取配置文件
    │   ├── 读取网络配置（端口、超时等）
    │   ├── 读取发现配置（发现范围、已知节点）
    │   └── 读取角色配置（默认角色、优先级）
    │
    ├─→ 初始化发现服务
    │   ├── 创建UDP广播监听器
    │   ├── 创建HTTP查询客户端
    │   └── 加载配置文件节点列表
    │
    ├─→ 执行混合发现
    │   ├── 并行执行三种发现方式
    │   ├── 合并发现结果
    │   └── 去重和验证
    │
    └─→ 角色决策
        ├── 分析发现的MCP节点
        ├── 根据策略选择角色
        └── 执行角色初始化
```

#### 2.1.2 混合发现详细流程

```
混合发现机制
    │
    ├─→ UDP广播发现
    │   │
    │   ├─→ 发送广播宣告
    │   │   ├── 构建宣告消息（节点ID、类型、端点）
    │   │   ├── 发送到广播地址 255.255.255.255:9001
    │   │   └── 设置超时等待响应
    │   │
    │   ├─→ 监听广播响应
    │   │   ├── 接收其他节点的宣告
    │   │   ├── 解析节点信息
    │   │   └── 添加到发现列表
    │   │
    │   └─→ 超时处理
    │       ├── 等待5秒收集响应
    │       └── 返回发现的节点列表
    │
    ├─→ HTTP查询发现
    │   │
    │   ├─→ 查询已知端点
    │   │   ├── 遍历配置的已知端点
    │   │   ├── 发送 GET /api/discovery/peers
    │   │   └── 解析返回的节点列表
    │   │
    │   ├─→ 查询组织域
    │   │   ├── 获取组织域服务器地址
    │   │   ├── 发送带域ID的查询请求
    │   │   └── 解析域内节点列表
    │   │
    │   └─→ 错误处理
    │       ├── 单个端点失败不影响其他
    │       └── 记录失败日志
    │
    └─→ 配置文件发现
        │
        ├─→ 读取配置
        │   ├── 解析 peer-config.json
        │   ├── 提取节点地址和类型
        │   └── 验证配置有效性
        │
        └─→ 健康检查
            ├── 对配置节点发送心跳
            ├── 过滤不可达节点
            └── 返回有效节点列表
```

### 2.2 发现协议接口

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
    private PeerType peerType;
    private String endpoint;
    private String domainId;
    private String organizationId;
    private Map<String, Object> capabilities;
    private long discoveredTime;
    
    public enum PeerType {
        MCP,        // MCP Agent
        ROUTE,      // Route Agent
        END         // End Agent
    }
}
```

### 2.3 UDP广播发现协议实现

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
    private List<DiscoveryListener> listeners = new CopyOnWriteArrayList<>();
    
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
                DatagramPacket packet = new DatagramPacket(
                    query, query.length, broadcastAddress, broadcastPort
                );
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
            DatagramPacket packet = new DatagramPacket(
                data, data.length, broadcastAddress, broadcastPort
            );
            socket.send(packet);
            log.info("Announced presence via UDP broadcast");
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

### 2.4 HTTP查询发现协议实现

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
                    List<PeerInfo> peers = discoverFromEndpoint(endpoint, context);
                    allPeers.addAll(peers);
                } catch (Exception e) {
                    log.warn("HTTP discovery failed for endpoint: " + endpoint);
                }
            }
            
            return allPeers;
        });
    }
    
    private List<PeerInfo> discoverFromEndpoint(String endpoint, DiscoveryContext context) {
        try {
            String url = buildDiscoveryUrl(endpoint, context);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-Discovery-Scope", context.getScope().name())
                .GET()
                .timeout(Duration.ofSeconds(5))
                .build();
            
            HttpResponse<String> response = httpClient.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
            );
            
            if (response.statusCode() == 200) {
                return parsePeers(response.body());
            }
        } catch (Exception e) {
            log.warn("Discovery from endpoint {} failed: {}", endpoint, e.getMessage());
        }
        
        return Collections.emptyList();
    }
    
    private String buildDiscoveryUrl(String endpoint, DiscoveryContext context) {
        StringBuilder url = new StringBuilder(endpoint);
        url.append("/api/discovery/peers");
        
        List<String> params = new ArrayList<>();
        if (context.getDomainId() != null) {
            params.add("domainId=" + context.getDomainId());
        }
        if (context.getOrganizationId() != null) {
            params.add("organizationId=" + context.getOrganizationId());
        }
        
        if (!params.isEmpty()) {
            url.append("?").append(String.join("&", params));
        }
        
        return url.toString();
    }
}
```

### 2.5 角色决策协议

#### 2.5.1 角色决策流程

```
角色决策流程
    │
    ├─→ 收集发现结果
    │   ├── 合并三种发现方式的节点列表
    │   ├── 去重（按peerId）
    │   └── 验证节点可用性
    │
    ├─→ 分析MCP节点
    │   ├── 筛选类型为MCP的节点
    │   ├── 计算每个MCP的优先级分数
    │   │   ├── 域匹配 +10分
    │   │   ├── 组织匹配 +5分
    │   │   ├── 高可用支持 +3分
    │   │   └── 延迟最低 +2分
    │   └── 选择最优MCP
    │
    ├─→ 决策判断
    │   │
    │   ├─→ 存在可用MCP
    │   │   ├── 决策：降级为RouteAgent
    │   │   ├── 目标：选中MCP的端点
    │   │   └── 原因：网络中已有MCP
    │   │
    │   └─→ 不存在可用MCP
    │       ├── 决策：升级为McpAgent
    │       ├── 目标：自身端点
    │       └── 原因：网络中无MCP
    │
    └─→ 执行角色初始化
        ├── RouteAgent：向MCP注册
        └── McpAgent：初始化网络
```

#### 2.5.2 角色决策接口

```java
package net.ooder.sdk.protocol.south.role;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RoleDecisionProtocol {
    
    CompletableFuture<RoleDecision> decideRole(DiscoveryResult discoveryResult);
    
    CompletableFuture<Void> downgradeToRouteAgent(String mcpEndpoint);
    
    CompletableFuture<Void> upgradeToMcpAgent();
    
    CompletableFuture<AgentRole> getCurrentRole();
}

public class RoleDecision {
    
    private AgentRole role;
    private String targetEndpoint;
    private String reason;
    private PeerInfo selectedMcp;
    private Map<String, Object> parameters;
    
    public enum AgentRole {
        MCP_AGENT,      // MCP Agent角色
        ROUTE_AGENT,    // Route Agent角色
        END_AGENT       // End Agent角色
    }
}

public class DiscoveryResult {
    
    private List<PeerInfo> allPeers;
    private List<PeerInfo> mcpPeers;
    private List<PeerInfo> routePeers;
    private List<PeerInfo> endPeers;
    private long discoveryTime;
    private DiscoveryContext context;
}
```

#### 2.5.3 角色决策实现

```java
package net.ooder.sdk.protocol.south.role.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoleDecisionProtocolImpl implements RoleDecisionProtocol {
    
    private AgentFactory agentFactory;
    private ConfigService configService;
    private AgentRole currentRole;
    
    @Override
    public CompletableFuture<RoleDecision> decideRole(DiscoveryResult discoveryResult) {
        return CompletableFuture.supplyAsync(() -> {
            List<PeerInfo> mcpPeers = discoveryResult.getMcpPeers();
            
            if (!mcpPeers.isEmpty()) {
                PeerInfo selectedMcp = selectBestMcp(mcpPeers, discoveryResult.getContext());
                
                return RoleDecision.builder()
                    .role(AgentRole.ROUTE_AGENT)
                    .targetEndpoint(selectedMcp.getEndpoint())
                    .selectedMcp(selectedMcp)
                    .reason("Found existing MCP in network: " + selectedMcp.getPeerId())
                    .build();
            }
            
            return RoleDecision.builder()
                .role(AgentRole.MCP_AGENT)
                .targetEndpoint(configService.getSelfEndpoint())
                .reason("No MCP found in network, upgrading to MCP")
                .build();
        });
    }
    
    @Override
    public CompletableFuture<Void> downgradeToRouteAgent(String mcpEndpoint) {
        return CompletableFuture.runAsync(() -> {
            log.info("Downgrading to RouteAgent, target MCP: {}", mcpEndpoint);
            
            RouteAgent routeAgent = agentFactory.createRouteAgent();
            
            routeAgent.register(mcpEndpoint).join();
            
            routeAgent.start();
            
            currentRole = AgentRole.ROUTE_AGENT;
            
            log.info("Successfully downgraded to RouteAgent");
        });
    }
    
    @Override
    public CompletableFuture<Void> upgradeToMcpAgent() {
        return CompletableFuture.runAsync(() -> {
            log.info("Upgrading to McpAgent");
            
            McpAgent mcpAgent = agentFactory.createMcpAgent();
            
            mcpAgent.register().join();
            
            mcpAgent.start();
            
            initializeSceneGroups(mcpAgent);
            
            currentRole = AgentRole.MCP_AGENT;
            
            log.info("Successfully upgraded to McpAgent");
        });
    }
    
    private PeerInfo selectBestMcp(List<PeerInfo> mcpPeers, DiscoveryContext context) {
        return mcpPeers.stream()
            .max((p1, p2) -> {
                int score1 = calculateMcpScore(p1, context);
                int score2 = calculateMcpScore(p2, context);
                return Integer.compare(score1, score2);
            })
            .orElse(mcpPeers.get(0));
    }
    
    private int calculateMcpScore(PeerInfo peer, DiscoveryContext context) {
        int score = 0;
        
        if (context.getDomainId() != null && 
            context.getDomainId().equals(peer.getDomainId())) {
            score += 10;
        }
        
        if (context.getOrganizationId() != null && 
            context.getOrganizationId().equals(peer.getOrganizationId())) {
            score += 5;
        }
        
        if (peer.getCapabilities().containsKey("highAvailability")) {
            score += 3;
        }
        
        if (peer.getCapabilities().containsKey("sceneGroupSupport")) {
            score += 2;
        }
        
        return score;
    }
}
```

### 2.6 阶段1闭环验证

#### 2.6.1 验证场景定义

| 场景ID | 场景名称 | 前置条件 | 触发动作 | 预期结果 |
|--------|----------|----------|----------|----------|
| P1-S1 | 局域网有MCP | 局域网存在MCP节点 | 启动发现流程 | 降级为RouteAgent |
| P1-S2 | 局域网无MCP | 局域网不存在MCP节点 | 启动发现流程 | 升级为McpAgent |
| P1-S3 | 组织域有MCP | 组织域存在MCP节点 | 启动发现流程 | 注册到组织MCP |
| P1-S4 | 配置指定MCP | 配置文件指定MCP地址 | 启动发现流程 | 使用配置的MCP |
| P1-S5 | 多MCP选择 | 存在多个MCP节点 | 启动发现流程 | 选择最优MCP |

#### 2.6.2 闭环验证实现

```java
package net.ooder.sdk.protocol.south.validation;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Phase1ClosedLoopValidation {
    
    private DiscoveryProtocol udpDiscovery;
    private DiscoveryProtocol httpDiscovery;
    private DiscoveryProtocol configDiscovery;
    private RoleDecisionProtocol roleDecision;
    
    public CompletableFuture<ValidationReport> validate() {
        return CompletableFuture.supplyAsync(() -> {
            ValidationReport report = new ValidationReport("Phase1: Network Discovery & Role Decision");
            
            report.addResult(validateScenario_P1_S1());
            report.addResult(validateScenario_P1_S2());
            report.addResult(validateScenario_P1_S3());
            report.addResult(validateScenario_P1_S4());
            report.addResult(validateScenario_P1_S5());
            
            return report;
        });
    }
    
    private ValidationResult validateScenario_P1_S1() {
        String scenarioId = "P1-S1";
        String scenarioName = "局域网有MCP";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
            .given("局域网中存在MCP节点")
            .when("启动发现流程")
            .then("应降级为RouteAgent并注册到MCP")
            .validate(() -> {
                DiscoveryContext context = new DiscoveryContext(DiscoveryScope.LOCAL_NETWORK);
                context.setTimeout(5000);
                
                List<PeerInfo> peers = udpDiscovery.discover(context).join();
                
                if (peers.stream().noneMatch(p -> p.getPeerType() == PeerType.MCP)) {
                    return ValidationFailure.of(scenarioId, "未发现MCP节点，前置条件不满足");
                }
                
                DiscoveryResult discoveryResult = new DiscoveryResult(peers);
                RoleDecision decision = roleDecision.decideRole(discoveryResult).join();
                
                if (decision.getRole() != AgentRole.ROUTE_AGENT) {
                    return ValidationFailure.of(scenarioId, 
                        "角色决策错误，应为RouteAgent，实际为" + decision.getRole());
                }
                
                if (decision.getTargetEndpoint() == null) {
                    return ValidationFailure.of(scenarioId, "未指定目标MCP端点");
                }
                
                return ValidationSuccess.of(scenarioId, 
                    "成功降级为RouteAgent，目标MCP: " + decision.getSelectedMcp().getPeerId());
            });
    }
    
    private ValidationResult validateScenario_P1_S2() {
        String scenarioId = "P1-S2";
        String scenarioName = "局域网无MCP";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
            .given("局域网中不存在MCP节点")
            .when("启动发现流程")
            .then("应升级为McpAgent并初始化网络")
            .validate(() -> {
                DiscoveryContext context = new DiscoveryContext(DiscoveryScope.LOCAL_NETWORK);
                context.setTimeout(5000);
                
                List<PeerInfo> peers = udpDiscovery.discover(context).join();
                
                if (peers.stream().anyMatch(p -> p.getPeerType() == PeerType.MCP)) {
                    return ValidationFailure.of(scenarioId, "发现意外的MCP节点，前置条件不满足");
                }
                
                DiscoveryResult discoveryResult = new DiscoveryResult(peers);
                RoleDecision decision = roleDecision.decideRole(discoveryResult).join();
                
                if (decision.getRole() != AgentRole.MCP_AGENT) {
                    return ValidationFailure.of(scenarioId, 
                        "角色决策错误，应为McpAgent，实际为" + decision.getRole());
                }
                
                return ValidationSuccess.of(scenarioId, "成功升级为McpAgent");
            });
    }
    
    private ValidationResult validateScenario_P1_S5() {
        String scenarioId = "P1-S5";
        String scenarioName = "多MCP选择";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
            .given("存在多个MCP节点，不同域和组织")
            .when("启动发现流程")
            .then("应选择最优MCP（域匹配优先）")
            .validate(() -> {
                DiscoveryContext context = new DiscoveryContext(DiscoveryScope.ORGANIZATION);
                context.setDomainId("domain-001");
                context.setOrganizationId("org-001");
                context.setTimeout(5000);
                
                List<PeerInfo> peers = new ArrayList<>();
                peers.addAll(udpDiscovery.discover(context).join());
                peers.addAll(httpDiscovery.discover(context).join());
                
                List<PeerInfo> mcpPeers = peers.stream()
                    .filter(p -> p.getPeerType() == PeerType.MCP)
                    .collect(Collectors.toList());
                
                if (mcpPeers.size() < 2) {
                    return ValidationFailure.of(scenarioId, "MCP节点数量不足，需要至少2个");
                }
                
                DiscoveryResult discoveryResult = new DiscoveryResult(peers);
                RoleDecision decision = roleDecision.decideRole(discoveryResult).join();
                
                PeerInfo selectedMcp = decision.getSelectedMcp();
                
                if (!context.getDomainId().equals(selectedMcp.getDomainId())) {
                    boolean hasDomainMatch = mcpPeers.stream()
                        .anyMatch(p -> context.getDomainId().equals(p.getDomainId()));
                    
                    if (hasDomainMatch) {
                        return ValidationFailure.of(scenarioId, 
                            "未选择域匹配的MCP，选择了: " + selectedMcp.getPeerId());
                    }
                }
                
                return ValidationSuccess.of(scenarioId, 
                    "成功选择最优MCP: " + selectedMcp.getPeerId() + 
                    " (domain=" + selectedMcp.getDomainId() + ")");
            });
    }
}
```

## 3. 阶段2：登录与域策略响应

### 3.1 流程详细描述

#### 3.1.1 登录流程

```
登录触发
    │
    ├─→ 用户主动登录
    │   ├── 用户输入用户名密码
    │   ├── 选择登录域（可选）
    │   └── 点击登录按钮
    │
    └─→ 配置自动登录
        ├── 读取配置文件中的登录信息
        ├── 验证配置有效性
        └── 自动执行登录

登录执行
    │
    ├─→ 认证请求
    │   ├── 构建认证请求
    │   ├── 发送到认证服务
    │   └── 等待认证响应
    │
    ├─→ 认证成功
    │   ├── 获取用户ID和令牌
    │   ├── 获取用户所属域列表
    │   └── 获取域策略（如有）
    │
    └─→ 认证失败
        ├── 返回错误信息
        └── 提示用户重试
```

#### 3.1.2 域策略响应流程

```
域策略检测
    │
    ├─→ 有域策略
    │   │
    │   ├─→ 获取域策略详情
    │   │   ├── 存储策略
    │   │   ├── 技能中心策略
    │   │   ├── 安全策略
    │   │   └── 网络策略
    │   │
    │   ├─→ 应用存储策略
    │   │   ├── 配置存储端点
    │   │   ├── 配置存储认证
    │   │   └── 验证存储可用性
    │   │
    │   ├─→ 应用技能中心策略
    │   │   ├── 配置技能中心端点
    │   │   ├── 同步技能列表
    │   │   └── 安装必需技能
    │   │
    │   ├─→ 应用安全策略
    │   │   ├── 配置加密参数
    │   │   ├── 配置访问控制
    │   │   └── 应用审计策略
    │   │
    │   └─→ 完成配置
    │       ├── 保存配置状态
    │       ├── 通知配置完成
    │       └── 进入正常运行
    │
    └─→ 无域策略
        │
        ├─→ 开启雷达模式
        │   ├── 初始化雷达扫描器
        │   ├── 配置扫描范围
        │   └── 配置过滤条件
        │
        ├─→ 扫描公共资源
        │   ├── 扫描公共技能
        │   ├── 扫描公共存储
        │   └── 扫描公共网络
        │
        └─→ 展示资源列表
            ├── 按类型分类
            ├── 按热度排序
            └── 提供搜索功能
```

### 3.2 登录协议接口

```java
package net.ooder.sdk.protocol.south.login;

import java.util.concurrent.CompletableFuture;

public interface LoginProtocol {
    
    CompletableFuture<LoginResult> login(LoginRequest request);
    
    CompletableFuture<LoginResult> autoLogin(ConfigFile config);
    
    CompletableFuture<Void> logout();
    
    CompletableFuture<Boolean> isLoggedIn();
    
    CompletableFuture<UserSession> getCurrentSession();
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
    private String errorCode;
    private String errorMessage;
    
    public static LoginResult success(String userId, String token, 
                                       DomainPolicy policy, List<String> domains) {
        LoginResult result = new LoginResult();
        result.setSuccess(true);
        result.setUserId(userId);
        result.setToken(token);
        result.setDomainPolicy(policy);
        result.setAvailableDomains(domains);
        return result;
    }
    
    public static LoginResult failure(String errorCode, String errorMessage) {
        LoginResult result = new LoginResult();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
```

### 3.3 域策略响应协议

```java
package net.ooder.sdk.protocol.south.domain;

import java.util.concurrent.CompletableFuture;

public interface DomainPolicyProtocol {
    
    CompletableFuture<DomainPolicy> getPolicy(String domainId);
    
    CompletableFuture<Void> applyPolicy(DomainPolicy policy);
    
    CompletableFuture<Void> configureStorage(StorageConfig config);
    
    CompletableFuture<Void> configureSkillCenter(SkillCenterConfig config);
    
    CompletableFuture<Void> configureSecurity(SecurityConfig config);
    
    CompletableFuture<PolicyStatus> getPolicyStatus();
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
    
    public enum DomainType {
        USER,           // 用户域
        ORGANIZATION,   // 组织域
        GLOBAL          // 全局域
    }
}

public class PolicyStatus {
    
    private String domainId;
    private boolean storageConfigured;
    private boolean skillCenterConfigured;
    private boolean securityConfigured;
    private List<String> installedSkills;
    private List<String> pendingSkills;
    private long appliedTime;
}
```

### 3.4 雷达模式协议

```java
package net.ooder.sdk.protocol.south.radar;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RadarProtocol {
    
    CompletableFuture<List<PublicResource>> scan(RadarScanRequest request);
    
    CompletableFuture<List<SkillInfo>> searchSkills(String query);
    
    CompletableFuture<List<StorageInfo>> searchStorage(String query);
    
    CompletableFuture<List<NetworkInfo>> searchNetworks(String query);
    
    void addRadarListener(RadarListener listener);
    
    void removeRadarListener(RadarListener listener);
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
    private ResourceType resourceType;
    private String name;
    private String description;
    private String source;
    private int popularity;
    private Map<String, Object> metadata;
    
    public enum ResourceType {
        SKILL,          // 技能
        STORAGE,        // 存储
        NETWORK,        // 网络
        DATA            // 数据
    }
}
```

### 3.5 阶段2闭环验证

#### 3.5.1 验证场景定义

| 场景ID | 场景名称 | 前置条件 | 触发动作 | 预期结果 |
|--------|----------|----------|----------|----------|
| P2-S1 | 用户登录有域策略 | 用户有域成员资格 | 用户主动登录 | 成功响应域策略 |
| P2-S2 | 自动登录有域策略 | 配置自动登录 | 启动时自动登录 | 成功自动配置 |
| P2-S3 | 登录无域策略 | 用户无域成员资格 | 用户登录 | 开启雷达模式 |
| P2-S4 | 域存储配置 | 有存储策略 | 应用域策略 | 存储配置成功 |
| P2-S5 | 技能中心配置 | 有技能中心策略 | 应用域策略 | 技能中心配置成功 |

#### 3.5.2 闭环验证实现

```java
package net.ooder.sdk.protocol.south.validation;

public class Phase2ClosedLoopValidation {
    
    private LoginProtocol loginProtocol;
    private DomainPolicyProtocol domainPolicyProtocol;
    private RadarProtocol radarProtocol;
    private StorageService storageService;
    private SkillCenterService skillCenterService;
    
    public CompletableFuture<ValidationReport> validate() {
        return CompletableFuture.supplyAsync(() -> {
            ValidationReport report = new ValidationReport("Phase2: Login & Domain Policy Response");
            
            report.addResult(validateScenario_P2_S1());
            report.addResult(validateScenario_P2_S2());
            report.addResult(validateScenario_P2_S3());
            report.addResult(validateScenario_P2_S4());
            report.addResult(validateScenario_P2_S5());
            
            return report;
        });
    }
    
    private ValidationResult validateScenario_P2_S1() {
        String scenarioId = "P2-S1";
        String scenarioName = "用户登录有域策略";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
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
                    return ValidationFailure.of(scenarioId, 
                        "登录失败: " + loginResult.getErrorMessage());
                }
                
                if (loginResult.getDomainPolicy() == null) {
                    return ValidationFailure.of(scenarioId, "未获取到域策略");
                }
                
                domainPolicyProtocol.applyPolicy(loginResult.getDomainPolicy()).join();
                
                PolicyStatus status = domainPolicyProtocol.getPolicyStatus().join();
                
                if (!status.isStorageConfigured()) {
                    return ValidationFailure.of(scenarioId, "存储未配置");
                }
                
                if (!status.isSkillCenterConfigured()) {
                    return ValidationFailure.of(scenarioId, "技能中心未配置");
                }
                
                if (!status.isSecurityConfigured()) {
                    return ValidationFailure.of(scenarioId, "安全策略未配置");
                }
                
                return ValidationSuccess.of(scenarioId, 
                    "成功响应域策略，存储、技能中心、安全策略均已配置");
            });
    }
    
    private ValidationResult validateScenario_P2_S3() {
        String scenarioId = "P2-S3";
        String scenarioName = "登录无域策略";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
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
                    return ValidationFailure.of(scenarioId, 
                        "登录失败: " + loginResult.getErrorMessage());
                }
                
                if (loginResult.getDomainPolicy() != null) {
                    return ValidationFailure.of(scenarioId, "不应有域策略");
                }
                
                RadarScanRequest scanRequest = new RadarScanRequest();
                scanRequest.setScope(RadarScope.PUBLIC_DOMAIN);
                scanRequest.setResourceTypes(Arrays.asList("SKILL", "STORAGE"));
                scanRequest.setTimeout(5000);
                
                List<PublicResource> resources = radarProtocol.scan(scanRequest).join();
                
                if (resources.isEmpty()) {
                    return ValidationFailure.of(scenarioId, "雷达扫描无结果");
                }
                
                long skillCount = resources.stream()
                    .filter(r -> r.getResourceType() == ResourceType.SKILL)
                    .count();
                
                long storageCount = resources.stream()
                    .filter(r -> r.getResourceType() == ResourceType.STORAGE)
                    .count();
                
                return ValidationSuccess.of(scenarioId, 
                    "成功开启雷达模式，发现" + skillCount + "个技能，" + storageCount + "个存储");
            });
    }
}
```

## 4. 阶段3：正常运行与协作

### 4.1 场景组自动化协议

```java
package net.ooder.sdk.protocol.south.scene;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SceneGroupProtocol {
    
    CompletableFuture<SceneGroup> createSceneGroup(String sceneId, SceneGroupConfig config);
    
    CompletableFuture<Void> autoJoinSceneGroups();
    
    CompletableFuture<Void> joinSceneGroup(String sceneGroupId);
    
    CompletableFuture<Void> leaveSceneGroup(String sceneGroupId);
    
    CompletableFuture<List<SceneGroup>> listSceneGroups();
    
    CompletableFuture<SceneGroupStatus> getStatus(String sceneGroupId);
    
    CompletableFuture<Void> executeScenario(String scenarioId, Map<String, Object> params);
}
```

### 4.2 技能执行协议

```java
package net.ooder.sdk.protocol.south.skill;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface SkillExecutionProtocol {
    
    CompletableFuture<SkillResult> execute(String skillId, Map<String, Object> params);
    
    CompletableFuture<SkillResult> executeWithRetry(String skillId, 
        Map<String, Object> params, int maxRetries);
    
    CompletableFuture<Void> install(String skillId);
    
    CompletableFuture<Void> uninstall(String skillId);
    
    CompletableFuture<List<SkillInfo>> listInstalledSkills();
    
    CompletableFuture<SkillStatus> getStatus(String skillId);
}
```

### 4.3 数据同步协议

```java
package net.ooder.sdk.protocol.south.sync;

import java.util.concurrent.CompletableFuture;

public interface DataSyncProtocol {
    
    CompletableFuture<SyncResult> sync(String dataType);
    
    CompletableFuture<SyncResult> syncAll();
    
    CompletableFuture<Void> configureSync(SyncConfig config);
    
    CompletableFuture<SyncStatus> getSyncStatus();
    
    CompletableFuture<Void> enableOfflineMode();
    
    CompletableFuture<Void> syncWhenOnline();
}
```

### 4.4 阶段3闭环验证

```java
package net.ooder.sdk.protocol.south.validation;

public class Phase3ClosedLoopValidation {
    
    private SceneGroupProtocol sceneGroupProtocol;
    private SkillExecutionProtocol skillExecutionProtocol;
    private DataSyncProtocol dataSyncProtocol;
    
    public CompletableFuture<ValidationReport> validate() {
        return CompletableFuture.supplyAsync(() -> {
            ValidationReport report = new ValidationReport("Phase3: Normal Operation & Collaboration");
            
            report.addResult(validateScenario_P3_S1());
            report.addResult(validateScenario_P3_S2());
            report.addResult(validateScenario_P3_S3());
            
            return report;
        });
    }
    
    private ValidationResult validateScenario_P3_S1() {
        String scenarioId = "P3-S1";
        String scenarioName = "场景组自动化";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
            .given("McpAgent已初始化")
            .when("创建和加入场景组")
            .then("场景组应正常工作")
            .validate(() -> {
                SceneGroupConfig config = new SceneGroupConfig();
                config.setSceneId("scene-001");
                config.setMinMembers(1);
                config.setMaxMembers(10);
                
                SceneGroup group = sceneGroupProtocol.createSceneGroup("scene-001", config).join();
                
                if (group == null) {
                    return ValidationFailure.of(scenarioId, "场景组创建失败");
                }
                
                sceneGroupProtocol.autoJoinSceneGroups().join();
                
                List<SceneGroup> groups = sceneGroupProtocol.listSceneGroups().join();
                
                if (groups.isEmpty()) {
                    return ValidationFailure.of(scenarioId, "场景组列表为空");
                }
                
                return ValidationSuccess.of(scenarioId, 
                    "场景组自动化成功，当前" + groups.size() + "个场景组");
            });
    }
}
```

## 5. 协议约定总结

### 5.1 与SDK 7.1兼容性

| SDK 7.1协议 | 0.7.2南向协议 | 兼容方式 |
|-------------|---------------|----------|
| ProtocolHub | SouthProtocolHub | 协议适配器 |
| CommandPacket | SouthCommandPacket | 双向转换 |
| McpAgent接口 | McpAgentProtocol | 接口继承 |
| RouteAgent接口 | RouteAgentProtocol | 接口继承 |

### 5.2 协议转换器

```java
package net.ooder.sdk.protocol.south.compat;

public class ProtocolCompatibility {
    
    public static SouthCommandPacket fromSdk71(CommandPacket packet) {
        SouthCommandPacket southPacket = new SouthCommandPacket();
        southPacket.setPacketId(packet.getPacketId());
        southPacket.setHeader(convertHeader(packet.getHeader()));
        southPacket.setPayload(packet.getPayload());
        southPacket.setTimestamp(packet.getTimestamp());
        return southPacket;
    }
    
    public static CommandPacket toSdk71(SouthCommandPacket southPacket) {
        CommandPacket packet = new CommandPacket();
        packet.setPacketId(southPacket.getPacketId());
        packet.setHeader(convertHeader(southPacket.getHeader()));
        packet.setPayload(southPacket.getPayload());
        packet.setTimestamp(southPacket.getTimestamp());
        return packet;
    }
}
```

## 6. 配置参考

### 6.1 发现配置

```properties
ooder.sdk.south.discovery.enabled=true
ooder.sdk.south.discovery.udp.port=9001
ooder.sdk.south.discovery.udp.timeout=5000
ooder.sdk.south.discovery.http.endpoints=http://localhost:8080,http://peer:8080
ooder.sdk.south.discovery.config.file=config/peers.json
```

### 6.2 登录配置

```properties
ooder.sdk.south.login.auto-enabled=true
ooder.sdk.south.login.default-domain=domain-001
ooder.sdk.south.login.token-expiration=86400000
```

### 6.3 域策略配置

```properties
ooder.sdk.south.domain.policy.enabled=true
ooder.sdk.south.domain.storage.auto-configure=true
ooder.sdk.south.domain.skill-center.auto-configure=true
ooder.sdk.south.domain.security.auto-configure=true
```

## 7. 总结

南向服务层为 Ooder Agent SDK 提供了完整的内部网络服务：

1. **三阶段流程**：网络发现与角色决策 → 登录与域策略响应 → 正常运行与协作
2. **混合发现机制**：UDP广播 + HTTP查询 + 配置文件
3. **角色自适应**：根据网络环境自动决定Agent角色
4. **域策略驱动**：以域策略为核心驱动配置
5. **闭环验证**：每个场景都经过完整的闭环验证

---

**Ooder Agent SDK 0.7.2** - 构建智能、协作、安全的Agent生态系统！

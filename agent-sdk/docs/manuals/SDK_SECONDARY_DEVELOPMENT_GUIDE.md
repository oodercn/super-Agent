# Ooder Agent SDK 二次开发指南

## 1. 概述

本文档面向需要进行SDK二次开发的开发者，介绍如何扩展和定制Ooder Agent SDK。

## 2. 开发环境配置

### 2.1 环境要求

| 工具 | 版本要求 |
|------|----------|
| JDK | 8+ |
| Maven | 3.6+ |
| IDE | IntelliJ IDEA / Eclipse |

### 2.2 项目结构

```
agent-sdk/
├── src/main/java/net/ooder/sdk/
│   ├── api/                    # 公开API层
│   │   ├── llm/               # LLM服务API
│   │   ├── metadata/          # 元数据服务API
│   │   ├── network/           # 网络服务API
│   │   ├── security/          # 安全服务API
│   │   └── storage/           # 存储服务API
│   ├── capability/            # 能力中心
│   │   ├── impl/              # 能力中心实现
│   │   └── model/             # 能力中心数据模型
│   ├── core/                  # 核心抽象层
│   │   ├── collaboration/     # 协作抽象
│   │   ├── network/           # 网络抽象
│   │   ├── protocol/          # 协议抽象
│   │   └── security/          # 安全抽象
│   ├── northbound/            # 北向服务层
│   │   └── protocol/          # 北向协议
│   │       ├── impl/          # 协议实现
│   │       └── model/         # 协议数据模型
│   ├── southbound/            # 南向服务层
│   │   └── protocol/          # 南向协议
│   │       ├── impl/          # 协议实现
│   │       └── model/         # 协议数据模型
│   ├── nexus/                 # Nexus连接服务
│   │   ├── impl/              # 实现类
│   │   ├── model/             # 数据模型
│   │   ├── offline/           # 离线服务
│   │   └── resource/          # 资源服务
│   └── service/               # 内部服务层
│       ├── agent/             # Agent服务
│       ├── discovery/         # 发现服务
│       ├── heartbeat/         # 心跳服务
│       ├── llm/               # LLM服务
│       ├── metadata/          # 元数据服务
│       ├── network/           # 网络服务
│       ├── scene/             # 场景服务
│       ├── security/          # 安全服务
│       ├── skill/             # 技能服务
│       ├── skillcenter/       # 技能中心
│       └── storage/           # 存储服务
└── src/test/java/             # 测试代码
```

## 3. 扩展能力中心

### 3.1 添加新的能力类型

```java
package net.ooder.sdk.capability.model;

public enum CapabilityType {
    SKILL,
    TOOL,
    AGENT,
    WORKFLOW,
    CUSTOM_TYPE    // 新增类型
}
```

### 3.2 实现自定义能力服务

```java
package com.example.sdk.extension;

import net.ooder.sdk.capability.CapabilitySpecService;
import net.ooder.sdk.capability.model.*;

public class CustomCapabilitySpecService implements CapabilitySpecService {
    
    private final CapabilitySpecService delegate;
    
    public CustomCapabilitySpecService(CapabilitySpecService delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public CompletableFuture<CapabilitySpec> registerSpec(SpecDefinition definition) {
        if (definition.getType() == CapabilityType.CUSTOM_TYPE) {
            validateCustomSpec(definition);
        }
        return delegate.registerSpec(definition);
    }
    
    private void validateCustomSpec(SpecDefinition definition) {
        // 自定义验证逻辑
    }
    
    // 其他方法委托给delegate...
}
```

### 3.3 扩展能力监测

```java
package com.example.sdk.extension;

import net.ooder.sdk.capability.CapabilityMonService;
import net.ooder.sdk.capability.model.*;

public class CustomMonitorListener implements MonitorListener {
    
    @Override
    public void onLogRecorded(ExecutionLog log) {
        // 自定义日志处理
        if (log.getLevel() >= 3) {  // ERROR级别
            sendAlert(log);
        }
    }
    
    @Override
    public void onMetricRecorded(MetricRecord metric) {
        // 自定义指标处理
        if (metric.getValue() > threshold) {
            triggerScaling(metric);
        }
    }
    
    @Override
    public void onTraceRecorded(ExecutionTrace trace) {
        // 自定义追踪处理
    }
    
    @Override
    public void onAlertTriggered(AlertInfo alert) {
        // 自定义告警处理
        notifyAdmin(alert);
    }
}
```

## 4. 扩展南向协议

### 4.1 实现自定义发现协议

```java
package com.example.sdk.extension;

import net.ooder.sdk.southbound.protocol.DiscoveryProtocol;
import net.ooder.sdk.southbound.protocol.model.*;

public class CustomDiscoveryProtocol implements DiscoveryProtocol {
    
    @Override
    public CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            DiscoveryResult result = new DiscoveryResult();
            List<PeerInfo> peers = customDiscoveryLogic();
            result.setPeers(peers);
            return result;
        });
    }
    
    private List<PeerInfo> customDiscoveryLogic() {
        // 实现自定义发现逻辑
        return new ArrayList<>();
    }
    
    // 其他方法实现...
}
```

### 4.2 扩展角色决策

```java
package com.example.sdk.extension;

import net.ooder.sdk.southbound.protocol.RoleProtocol;
import net.ooder.sdk.southbound.protocol.model.*;

public class CustomRoleProtocol implements RoleProtocol {
    
    @Override
    public CompletableFuture<RoleDecision> decideRole(RoleContext context) {
        return CompletableFuture.supplyAsync(() -> {
            RoleDecision decision = new RoleDecision();
            
            // 自定义角色决策逻辑
            if (hasHighCapability(context)) {
                decision.setDecidedRole(RoleType.MCP_AGENT);
            } else if (hasNetworkResource(context)) {
                decision.setDecidedRole(RoleType.ROUTE_AGENT);
            } else {
                decision.setDecidedRole(RoleType.END_AGENT);
            }
            
            return decision;
        });
    }
    
    private boolean hasHighCapability(RoleContext context) {
        // 检查能力评分
        return true;
    }
    
    private boolean hasNetworkResource(RoleContext context) {
        // 检查网络资源
        return false;
    }
}
```

## 5. 扩展北向协议

### 5.1 实现自定义域管理

```java
package com.example.sdk.extension;

import net.ooder.sdk.northbound.protocol.DomainManagementProtocol;
import net.ooder.sdk.northbound.protocol.model.*;

public class CustomDomainManagement implements DomainManagementProtocol {
    
    @Override
    public CompletableFuture<DomainInfo> createDomain(CreateDomainRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // 前置检查
            validateDomainRequest(request);
            
            // 创建域
            DomainInfo domain = doCreateDomain(request);
            
            // 后置处理
            initializeDomainResources(domain);
            
            return domain;
        });
    }
    
    private void validateDomainRequest(CreateDomainRequest request) {
        // 验证逻辑
    }
    
    private DomainInfo doCreateDomain(CreateDomainRequest request) {
        // 创建逻辑
        return new DomainInfo();
    }
    
    private void initializeDomainResources(DomainInfo domain) {
        // 资源初始化
    }
}
```

### 5.2 扩展观测协议

```java
package com.example.sdk.extension;

import net.ooder.sdk.northbound.protocol.ObservationProtocol;
import net.ooder.sdk.northbound.protocol.model.*;

public class CustomObservationProtocol implements ObservationProtocol {
    
    private final ObservationProtocol delegate;
    
    public CustomObservationProtocol(ObservationProtocol delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public CompletableFuture<List<ObservationMetric>> getMetrics(String targetId, MetricQuery query) {
        return delegate.getMetrics(targetId, query)
            .thenApply(metrics -> {
                // 添加自定义指标
                metrics.addAll(collectCustomMetrics(targetId));
                return metrics;
            });
    }
    
    private List<ObservationMetric> collectCustomMetrics(String targetId) {
        // 收集自定义指标
        return new ArrayList<>();
    }
}
```

## 6. 扩展Nexus服务

### 6.1 自定义离线策略

```java
package com.example.sdk.extension;

import net.ooder.sdk.nexus.offline.OfflineService;
import net.ooder.sdk.nexus.offline.model.*;

public class CustomOfflineService implements OfflineService {
    
    @Override
    public CompletableFuture<SyncResult> syncNow() {
        return CompletableFuture.supplyAsync(() -> {
            SyncResult result = new SyncResult();
            
            // 自定义同步策略
            List<PendingSync> items = getPendingSyncItems().join();
            
            for (PendingSync item : items) {
                if (shouldPrioritize(item)) {
                    syncWithPriority(item);
                } else {
                    syncNormal(item);
                }
            }
            
            return result;
        });
    }
    
    private boolean shouldPrioritize(PendingSync item) {
        return item.getPriority() == PendingSync.SyncPriority.HIGH;
    }
}
```

### 6.2 扩展资源服务

```java
package com.example.sdk.extension;

import net.ooder.sdk.nexus.resource.PrivateResourceService;
import net.ooder.sdk.nexus.resource.model.*;

public class CustomResourceService implements PrivateResourceService {
    
    @Override
    public CompletableFuture<SkillResult> executeSkill(String skillId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> {
            // 前置检查
            checkSkillPermission(skillId);
            
            // 执行技能
            SkillResult result = doExecuteSkill(skillId, params);
            
            // 后置审计
            auditSkillExecution(skillId, result);
            
            return result;
        });
    }
    
    private void checkSkillPermission(String skillId) {
        // 权限检查
    }
    
    private SkillResult doExecuteSkill(String skillId, Map<String, Object> params) {
        // 执行逻辑
        return new SkillResult();
    }
    
    private void auditSkillExecution(String skillId, SkillResult result) {
        // 审计记录
    }
}
```

## 7. 数据模型扩展

### 7.1 添加自定义模型字段

```java
package com.example.sdk.extension.model;

import net.ooder.sdk.capability.model.CapabilitySpec;

public class ExtendedCapabilitySpec extends CapabilitySpec {
    
    private String customField1;
    private int customField2;
    private Map<String, Object> customMetadata;
    
    public String getCustomField1() { return customField1; }
    public void setCustomField1(String customField1) { this.customField1 = customField1; }
    
    public int getCustomField2() { return customField2; }
    public void setCustomField2(int customField2) { this.customField2 = customField2; }
    
    public Map<String, Object> getCustomMetadata() { return customMetadata; }
    public void setCustomMetadata(Map<String, Object> customMetadata) { this.customMetadata = customMetadata; }
}
```

### 7.2 创建新的数据模型

```java
package com.example.sdk.extension.model;

public class CustomExecutionReport {
    
    private String reportId;
    private String capabilityId;
    private long executionTime;
    private double successRate;
    private Map<String, Object> customMetrics;
    
    // getters and setters...
}
```

## 8. 测试指南

### 8.1 单元测试

```java
package com.example.sdk.extension;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomCapabilitySpecServiceTest {
    
    @Test
    public void testRegisterCustomSpec() throws Exception {
        CustomCapabilitySpecService service = new CustomCapabilitySpecService(delegate);
        
        SpecDefinition definition = new SpecDefinition();
        definition.setName("CustomCapability");
        definition.setType(CapabilityType.CUSTOM_TYPE);
        definition.setVersion("1.0.0");
        
        CapabilitySpec spec = service.registerSpec(definition).get(10, TimeUnit.SECONDS);
        
        assertNotNull(spec);
        assertEquals("CustomCapability", spec.getSpecName());
    }
}
```

### 8.2 集成测试

```java
package com.example.sdk.extension;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class CustomIntegrationTest {
    
    private CapabilityCenter capabilityCenter;
    
    @Before
    public void setUp() {
        capabilityCenter = new CapabilityCenterImpl();
        capabilityCenter.initialize();
    }
    
    @After
    public void tearDown() {
        capabilityCenter.shutdown();
    }
    
    @Test
    public void testEndToEndFlow() throws Exception {
        // 测试完整流程
    }
}
```

## 9. 最佳实践

### 9.1 代码规范

- 遵循Java命名规范
- 使用接口进行抽象
- 保持方法职责单一
- 添加必要的日志记录

### 9.2 性能优化

- 使用异步编程模型
- 合理使用缓存
- 避免阻塞操作
- 控制并发度

### 9.3 安全考虑

- 输入验证
- 权限检查
- 敏感数据加密
- 审计日志

## 10. 发布流程

### 10.1 版本管理

```xml
<version>0.7.2-SNAPSHOT</version>  <!-- 开发版本 -->
<version>0.7.2</version>           <!-- 发布版本 -->
```

### 10.2 构建命令

```bash
# 编译
mvn compile

# 测试
mvn test

# 打包
mvn package

# 安装到本地仓库
mvn install

# 部署到远程仓库
mvn deploy
```

## 11. 常见问题

### Q1: 如何添加新的能力类型？

在`CapabilityType`枚举中添加新类型，然后实现相应的验证和处理逻辑。

### Q2: 如何自定义角色决策算法？

实现`RoleProtocol`接口，在`decideRole`方法中实现自定义逻辑。

### Q3: 如何扩展监控指标？

实现`MonitorListener`接口，在`onMetricRecorded`方法中处理自定义指标。

### Q4: 如何处理离线数据同步？

扩展`OfflineService`，实现自定义的同步策略和冲突解决机制。

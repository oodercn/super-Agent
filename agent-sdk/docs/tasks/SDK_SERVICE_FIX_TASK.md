# SDK服务不可用问题修复任务

## 问题概述

SkillCenter项目集成AgentSDK v0.7.2时，`SkillPackageManager`和`SceneManager`返回null，而`SceneGroupManager`正常工作。

## 问题分析

### 根本原因

1. **Builder配置不完整**: 当前Builder只支持手动注入服务实例，没有提供便捷的配置方法
2. **缺少便捷配置API**: 用户需要通过`configuration()`方法设置完整配置，不够直观
3. **文档缺失**: 没有明确说明各服务的初始化条件和依赖关系

### 对比分析

| 服务 | 状态 | 原因 |
|------|------|------|
| SceneGroupManager | ✅ 可用 | 无外部依赖，默认构造即可工作 |
| SkillPackageManager | ❌ null | 需要skillRootPath等配置 |
| SceneManager | ❌ null | 可能缺少必要依赖 |

---

## 开发任务

### 任务1: 增强SDK Builder配置API

**优先级**: 高

**目标**: 提供更便捷的Builder配置方法

**实现内容**:

```java
public static class Builder {
    // 新增便捷配置方法
    public Builder agentId(String agentId) {
        if (configuration == null) {
            configuration = new SDKConfiguration();
        }
        configuration.setAgentId(agentId);
        return this;
    }
    
    public Builder agentName(String agentName) {
        if (configuration == null) {
            configuration = new SDKConfiguration();
        }
        configuration.setAgentName(agentName);
        return this;
    }
    
    public Builder agentType(String agentType) {
        if (configuration == null) {
            configuration = new SDKConfiguration();
        }
        configuration.setAgentType(agentType);
        return this;
    }
    
    public Builder endpoint(String endpoint) {
        if (configuration == null) {
            configuration = new SDKConfiguration();
        }
        configuration.setEndpoint(endpoint);
        return this;
    }
    
    public Builder skillRootPath(String skillRootPath) {
        if (configuration == null) {
            configuration = new SDKConfiguration();
        }
        configuration.setSkillRootPath(skillRootPath);
        return this;
    }
    
    public Builder skillCenterUrl(String skillCenterUrl) {
        if (configuration == null) {
            configuration = new SDKConfiguration();
        }
        configuration.setSkillCenterUrl(skillCenterUrl);
        return this;
    }
    
    public Builder vfsUrl(String vfsUrl) {
        if (configuration == null) {
            configuration = new SDKConfiguration();
        }
        configuration.setVfsUrl(vfsUrl);
        return this;
    }
}
```

**预期效果**:
```java
// 之前的方式
ooderSDK = OoderSDK.builder().build();

// 之后的方式
ooderSDK = OoderSDK.builder()
    .agentId("skillcenter-agent")
    .agentName("SkillCenter Agent")
    .agentType("END")
    .skillRootPath("./skillcenter")
    .skillCenterUrl("https://skillcenter.ooder.net")
    .build();
```

---

### 任务2: 确保服务实例正确创建

**优先级**: 高

**目标**: 确保所有服务在build()时正确实例化

**检查项**:

1. 验证SkillPackageManagerImpl构造函数无异常
2. 验证SceneManagerImpl构造函数无异常
3. 确保所有依赖类可正确加载

**修改内容**:

在Builder.build()中添加异常处理和日志：

```java
public OoderSDK build() {
    if (configuration == null) {
        configuration = new SDKConfiguration();
    }
    
    try {
        if (agentFactory == null) {
            agentFactory = new net.ooder.sdk.core.agent.factory.AgentFactoryImpl();
        }
        if (skillPackageManager == null) {
            skillPackageManager = new net.ooder.sdk.core.skill.impl.SkillPackageManagerImpl();
        }
        if (sceneManager == null) {
            sceneManager = new net.ooder.sdk.core.scene.impl.SceneManagerImpl();
        }
        if (sceneGroupManager == null) {
            sceneGroupManager = new net.ooder.sdk.core.scene.impl.SceneGroupManagerImpl();
        }
        if (capabilityInvoker == null) {
            capabilityInvoker = new net.ooder.sdk.core.scene.impl.CapabilityInvokerImpl();
        }
        if (metadataQueryService == null) {
            metadataQueryService = new net.ooder.sdk.core.metadata.impl.MetadataQueryServiceImpl();
        }
        if (changeLogService == null) {
            changeLogService = new net.ooder.sdk.core.metadata.impl.ChangeLogServiceImpl();
        }
        if (skillCenterClient == null) {
            skillCenterClient = new net.ooder.sdk.service.skillcenter.SkillCenterClientImpl(configuration);
        }
    } catch (Exception e) {
        throw new RuntimeException("Failed to initialize SDK services", e);
    }
    
    return new OoderSDK(this);
}
```

---

### 任务3: 添加服务初始化诊断

**优先级**: 中

**目标**: 提供诊断方法帮助用户排查问题

**实现内容**:

```java
public class OoderSDK {
    
    /**
     * 获取服务状态诊断信息
     */
    public Map<String, ServiceStatus> diagnoseServices() {
        Map<String, ServiceStatus> status = new LinkedHashMap<>();
        
        status.put("AgentFactory", checkService(agentFactory));
        status.put("SkillPackageManager", checkService(skillPackageManager));
        status.put("SceneManager", checkService(sceneManager));
        status.put("SceneGroupManager", checkService(sceneGroupManager));
        status.put("CapabilityInvoker", checkService(capabilityInvoker));
        status.put("MetadataQueryService", checkService(metadataQueryService));
        status.put("ChangeLogService", checkService(changeLogService));
        status.put("SkillCenterClient", checkService(skillCenterClient));
        
        return status;
    }
    
    private ServiceStatus checkService(Object service) {
        if (service == null) {
            return ServiceStatus.NULL;
        }
        return ServiceStatus.AVAILABLE;
    }
    
    public enum ServiceStatus {
        NULL("服务未初始化"),
        AVAILABLE("服务可用"),
        ERROR("服务异常");
        
        private final String description;
        
        ServiceStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
```

---

### 任务4: 更新使用文档

**优先级**: 中

**目标**: 提供清晰的使用指南

**文档内容**:

```markdown
# OoderSDK 快速开始

## 最简配置

OoderSDK支持零配置启动，所有服务使用默认值：

```java
OoderSDK sdk = OoderSDK.builder().build();
sdk.start();
```

## 完整配置

```java
OoderSDK sdk = OoderSDK.builder()
    .agentId("my-agent")                    // Agent唯一标识
    .agentName("My Agent")                  // Agent名称
    .agentType("END")                       // Agent类型: END/MCP/ROUTE
    .endpoint("http://localhost:8080")      // 服务端点
    .skillRootPath("./skills")              // 技能存储路径
    .skillCenterUrl("https://skillcenter.ooder.net")  // SkillCenter服务地址
    .vfsUrl("https://vfs.ooder.net")        // VFS服务地址
    .build();

sdk.start();
```

## 服务可用性检查

```java
// 诊断所有服务状态
Map<String, ServiceStatus> status = sdk.diagnoseServices();
for (Map.Entry<String, ServiceStatus> entry : status.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue().getDescription());
}
```

## 初始化流程

1. `OoderSDK.builder().build()` - 创建SDK实例
2. `sdk.initialize()` - 初始化服务（可选，start()会自动调用）
3. `sdk.start()` - 启动SDK
4. `sdk.stop()` - 暂停SDK
5. `sdk.shutdown()` - 关闭SDK
```

---

## 验收标准

1. 使用`OoderSDK.builder().build()`创建实例，所有服务不为null
2. 使用便捷配置方法可以正确设置各项参数
3. `diagnoseServices()`方法能正确报告服务状态
4. 文档清晰说明使用方法

---

## 时间计划

| 任务 | 预计时间 |
|------|----------|
| 任务1: 增强Builder配置API | 2小时 |
| 任务2: 确保服务正确创建 | 1小时 |
| 任务3: 添加诊断功能 | 1小时 |
| 任务4: 更新文档 | 1小时 |
| **总计** | **5小时** |

---

## 相关文件

- `src/main/java/net/ooder/sdk/api/OoderSDK.java`
- `src/main/java/net/ooder/sdk/infra/config/SDKConfiguration.java`
- `src/main/java/net/ooder/sdk/core/skill/impl/SkillPackageManagerImpl.java`
- `src/main/java/net/ooder/sdk/core/scene/impl/SceneManagerImpl.java`

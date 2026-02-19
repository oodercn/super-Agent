# OoderSDK 快速开始指南

## 版本信息

- SDK版本: 0.7.2+
- Java版本: 1.8+

---

## 一、快速开始

### 1.1 最简配置

OoderSDK支持零配置启动，所有服务使用默认值：

```java
import net.ooder.sdk.api.OoderSDK;

public class QuickStart {
    public static void main(String[] args) throws Exception {
        OoderSDK sdk = OoderSDK.builder().build();
        sdk.start();
        
        // 使用SDK服务...
        
        sdk.shutdown();
    }
}
```

### 1.2 完整配置

```java
OoderSDK sdk = OoderSDK.builder()
    .agentId("my-agent")                        // Agent唯一标识
    .agentName("My Agent")                      // Agent名称
    .agentType("END")                           // Agent类型: END/MCP/ROUTE
    .endpoint("http://localhost:8080")          // 服务端点
    .udpPort(8888)                              // UDP端口
    .skillRootPath("./skills")                  // 技能存储路径
    .skillCenterUrl("https://skillcenter.ooder.net")  // SkillCenter服务地址
    .vfsUrl("https://vfs.ooder.net")            // VFS服务地址
    .strictMode(false)                          // 严格模式
    .discoveryEnabled(true)                     // 启用发现
    .build();

sdk.start();
```

---

## 二、服务可用性检查

### 2.1 诊断服务状态

```java
OoderSDK sdk = OoderSDK.builder().build();

// 诊断所有服务状态
Map<String, OoderSDK.ServiceStatus> status = sdk.diagnoseServices();
for (Map.Entry<String, OoderSDK.ServiceStatus> entry : status.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue().getDescription());
}
```

输出示例：
```
AgentFactory: 服务可用
SkillPackageManager: 服务可用
SceneManager: 服务可用
SceneGroupManager: 服务可用
CapabilityInvoker: 服务可用
MetadataQueryService: 服务可用
ChangeLogService: 服务可用
SkillCenterClient: 服务可用
```

### 2.2 检查单个服务

```java
SkillPackageManager spm = sdk.getSkillPackageManager();
if (spm != null) {
    // 服务可用
    List<SkillPackage> packages = spm.discoverAll(DiscoveryMethod.LOCAL_FS).join();
} else {
    // 服务不可用
    System.err.println("SkillPackageManager不可用");
}
```

---

## 三、核心服务说明

### 3.1 服务列表

| 服务 | 说明 | 默认可用 |
|------|------|----------|
| AgentFactory | Agent工厂，创建各类Agent | ✅ |
| SkillPackageManager | 技能包管理器 | ✅ |
| SceneManager | 场景管理器 | ✅ |
| SceneGroupManager | 场景组管理器 | ✅ |
| CapabilityInvoker | 能力调用器 | ✅ |
| MetadataQueryService | 元数据查询服务 | ✅ |
| ChangeLogService | 变更日志服务 | ✅ |
| SkillCenterClient | SkillCenter客户端 | ✅ |

### 3.2 服务依赖关系

```
OoderSDK
├── AgentFactory (创建Agent实例)
│   ├── McpAgent
│   ├── RouteAgent
│   └── EndAgent
├── SkillPackageManager (技能包管理)
│   ├── discover() - 发现技能
│   ├── install() - 安装技能
│   └── uninstall() - 卸载技能
├── SceneManager (场景管理)
│   ├── create() - 创建场景
│   ├── delete() - 删除场景
│   └── activate() - 激活场景
└── SceneGroupManager (场景组管理)
    ├── create() - 创建场景组
    ├── join() - 加入场景组
    └── leave() - 离开场景组
```

---

## 四、生命周期管理

### 4.1 初始化流程

```java
// 1. 创建SDK实例
OoderSDK sdk = OoderSDK.builder().build();

// 2. 初始化（可选，start()会自动调用）
sdk.initialize();

// 3. 启动SDK
sdk.start();

// 4. 检查状态
boolean running = sdk.isRunning();
boolean started = sdk.isStarted();

// 5. 停止SDK（可恢复）
sdk.stop();

// 6. 关闭SDK（完全释放资源）
sdk.shutdown();
```

### 4.2 状态检查

```java
if (sdk.isInitialized()) {
    System.out.println("SDK已初始化");
}

if (sdk.isStarted()) {
    System.out.println("SDK已启动");
}

if (sdk.isRunning()) {
    System.out.println("SDK正在运行");
}
```

---

## 五、常见问题

### 5.1 服务返回null

**问题**: 调用`getSkillPackageManager()`等方法返回null

**解决方案**: 
1. 确保使用`OoderSDK.builder().build()`创建实例
2. 调用`diagnoseServices()`检查所有服务状态
3. 如果仍有问题，检查日志中是否有初始化异常

### 5.2 配置不生效

**问题**: 设置了配置但服务行为未改变

**解决方案**:
```java
// 正确方式：在build()之前设置配置
OoderSDK sdk = OoderSDK.builder()
    .skillRootPath("./my-skills")  // 先设置
    .build();                       // 再构建

// 错误方式：build()之后设置无效
OoderSDK sdk = OoderSDK.builder().build();
sdk.getConfiguration().setSkillRootPath("./my-skills");  // 无效
```

### 5.3 Spring Boot集成

```java
@Configuration
public class SdkConfig {
    
    @Bean
    public OoderSDK ooderSDK() {
        OoderSDK sdk = OoderSDK.builder()
            .agentId("skillcenter-agent")
            .agentName("SkillCenter Agent")
            .agentType("END")
            .skillRootPath("./skillcenter")
            .skillCenterUrl("https://skillcenter.ooder.net")
            .build();
        
        sdk.start();
        return sdk;
    }
    
    @PreDestroy
    public void shutdown() {
        ooderSDK().shutdown();
    }
}
```

---

## 六、API参考

### 6.1 Builder配置方法

| 方法 | 参数类型 | 说明 |
|------|----------|------|
| agentId(String) | String | Agent唯一标识 |
| agentName(String) | String | Agent名称 |
| agentType(String) | String | Agent类型(END/MCP/ROUTE) |
| endpoint(String) | String | 服务端点 |
| udpPort(int) | int | UDP端口 |
| skillRootPath(String) | String | 技能存储路径 |
| skillCenterUrl(String) | String | SkillCenter服务地址 |
| vfsUrl(String) | String | VFS服务地址 |
| strictMode(boolean) | boolean | 严格模式 |
| discoveryEnabled(boolean) | boolean | 启用发现 |

### 6.2 SDK实例方法

| 方法 | 返回类型 | 说明 |
|------|----------|------|
| initialize() | void | 初始化SDK |
| start() | void | 启动SDK |
| stop() | void | 停止SDK |
| shutdown() | void | 关闭SDK |
| isInitialized() | boolean | 是否已初始化 |
| isStarted() | boolean | 是否已启动 |
| isRunning() | boolean | 是否运行中 |
| diagnoseServices() | Map | 诊断服务状态 |
| getSkillPackageManager() | SkillPackageManager | 获取技能包管理器 |
| getSceneManager() | SceneManager | 获取场景管理器 |
| getSceneGroupManager() | SceneGroupManager | 获取场景组管理器 |

---

## 七、版本历史

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| 0.7.2 | 2026-02-18 | 增强Builder配置API，添加服务诊断功能 |

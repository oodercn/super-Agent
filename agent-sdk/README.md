# Ooder SDK 文档

## 1. 概述

Ooder SDK 是一个功能强大的网络管理开发工具包，提供了终端管理、路由管理、网络拓扑、命令处理、网络链路管理、数据持久化、监控和告警、安全管理等核心功能。本SDK设计为模块化架构，支持Java 8及以上版本。

## 2. 核心模块

### 2.1 终端管理模块

#### 功能概述
- 终端设备的自动发现机制
- 终端设备的注册和管理
- 终端设备状态的实时同步
- 终端设备事件的处理

#### 主要类和接口
- `TerminalManager`: 终端管理接口
- `TerminalManagerImpl`: 终端管理实现
- `TerminalDevice`: 终端设备模型
- `TerminalEvent`: 终端事件模型
- `TerminalEventType`: 终端事件类型

#### 使用示例

```java
// 初始化终端管理器
TerminalManager terminalManager = new TerminalManagerImpl();

// 启动终端发现
terminalManager.startTerminalDiscovery();

// 获取所有终端设备
List<TerminalDevice> devices = terminalManager.getAllTerminals().join();

// 订阅终端事件
terminalManager.subscribeToTerminalEvents(event -> {
    System.out.println("Terminal event: " + event);
});
```

### 2.2 路由管理模块

#### 功能概述
- 网络路由的自动发现
- 最优路由的计算算法
- 网络拓扑变化时的路由更新
- 路由状态和性能的监控

#### 主要类和接口
- `RouteManager`: 路由管理接口
- `RouteManagerImpl`: 路由管理实现
- `Route`: 路由模型
- `RouteEvent`: 路由事件模型
- `RouteEventType`: 路由事件类型

#### 使用示例

```java
// 初始化路由管理器
RouteManager routeManager = new RouteManagerImpl();

// 启动路由发现
routeManager.startRouteDiscovery();

// 计算最优路由
Route optimalRoute = routeManager.calculateOptimalRoute("sourceNode", "targetNode").join();

// 获取所有路由
List<Route> routes = routeManager.getAllRoutes().join();
```

### 2.3 网络拓扑模块

#### 功能概述
- 网络设备和链路的拓扑结构构建
- 网络变化时的拓扑更新
- 网络拓扑信息的查询
- 网络拓扑性能和可靠性的分析

#### 主要类和接口
- `TopologyManager`: 拓扑管理接口
- `TopologyManagerImpl`: 拓扑管理实现
- `NetworkNode`: 网络节点模型
- `NetworkLink`: 网络链路模型
- `TopologyEvent`: 拓扑事件模型
- `TopologyEventType`: 拓扑事件类型

#### 使用示例

```java
// 初始化拓扑管理器
TopologyManager topologyManager = new TopologyManagerImpl();

// 构建网络拓扑
topologyManager.buildTopology().join();

// 获取网络拓扑信息
NetworkTopology topology = topologyManager.getTopology().join();

// 分析拓扑性能
Map<String, Object> analysis = topologyManager.analyzeTopology().join();
```

### 2.4 命令处理模块

#### 功能概述
- 解析各种MCP命令
- 执行命令的具体逻辑
- 生成命令的响应
- 处理命令执行过程中的错误

#### 主要类和接口
- `CommandProcessor`: 命令处理器接口
- `CommandProcessorImpl`: 命令处理器实现
- `CommandInterceptor`: 命令拦截器接口
- `CommandInterceptorChain`: 命令拦截器链
- `CommandExecutor`: 命令执行器

#### 使用示例

```java
// 初始化命令处理器
CommandProcessor commandProcessor = new CommandProcessorImpl();

// 添加命令拦截器
commandProcessor.addInterceptor(new LoggingInterceptor());

// 处理命令
CommandPacket packet = new CommandPacket();
packet.setCommandId("cmd-001");
packet.setCommandType("GET_TERMINALS");

CommandResult result = commandProcessor.processCommand(packet).join();
System.out.println("Command result: " + result);
```

### 2.5 网络链路管理模块

#### 功能概述
- 网络链路的创建、移除、更新和查询
- 网络链路状态的监控
- 网络链路性能的分析

#### 主要类和接口
- `EnhancedLinkManager`: 增强的链路管理接口
- `EnhancedLinkManagerImpl`: 增强的链路管理实现
- `LinkInfo`: 链路信息模型
- `LinkEvent`: 链路事件模型
- `LinkEventType`: 链路事件类型

#### 使用示例

```java
// 初始化链路管理器
LinkTableManager linkTableManager = new LinkTableManagerImpl();
EnhancedLinkManager linkManager = new EnhancedLinkManagerImpl(linkTableManager);

// 启动链路监控
linkManager.startLinkMonitoring();

// 分析链路性能
Map<String, Object> performance = linkManager.analyzeLinkPerformance("link-001").join();

// 订阅链路事件
linkManager.subscribeToLinkEvents(event -> {
    System.out.println("Link event: " + event);
});
```

### 2.6 数据持久化模块

#### 功能概述
- 终端设备信息的持久化存储
- 路由信息的持久化存储
- 网络拓扑信息的持久化存储
- 配置信息的持久化存储

#### 主要类和接口
- `StorageManager`: 存储管理接口
- `FileSystemStorageManager`: 文件系统存储管理实现
- `TerminalStorage`: 终端存储接口
- `TerminalStorageImpl`: 终端存储实现
- `RouteStorage`: 路由存储接口
- `RouteStorageImpl`: 路由存储实现
- `TopologyStorage`: 拓扑存储接口
- `TopologyStorageImpl`: 拓扑存储实现
- `ConfigStorage`: 配置存储接口
- `ConfigStorageImpl`: 配置存储实现

#### 使用示例

```java
// 初始化存储管理器
StorageManager storageManager = new FileSystemStorageManager("/path/to/storage");
storageManager.initialize().join();

// 初始化终端存储
TerminalStorage terminalStorage = new TerminalStorageImpl(storageManager);

// 保存终端设备
TerminalDevice device = new TerminalDevice("terminal-001", "Device 1", "type1", "192.168.1.1");
terminalStorage.saveTerminal(device).join();

// 加载终端设备
TerminalDevice loadedDevice = terminalStorage.loadTerminal("terminal-001").join();
```

### 2.7 监控和告警模块

#### 功能概述
- 系统指标的采集
- 网络状态的监控
- 异常情况的告警
- 性能数据的分析

#### 主要类和接口
- `MonitoringManager`: 监控管理接口
- `MonitoringManagerImpl`: 监控管理实现
- `Alert`: 告警模型
- `AlertLevel`: 告警级别
- `AlertRule`: 告警规则
- `HealthStatus`: 健康状态模型

#### 使用示例

```java
// 初始化监控管理器
MonitoringManager monitoringManager = new MonitoringManagerImpl(linkManager, terminalManager);

// 启动监控
monitoringManager.startMonitoring().join();

// 收集系统指标
Map<String, Object> systemMetrics = monitoringManager.collectAllSystemMetrics().join();

// 订阅告警
monitoringManager.subscribeToAlerts(alert -> {
    System.out.println("Alert: " + alert);
});

// 添加告警规则
AlertRule cpuRule = new AlertRule(
    "High CPU Load",
    AlertLevel.WARNING,
    "system",
    "system.cpuLoad",
    "cpuLoad > threshold",
    0.85
);
monitoringManager.addAlertRule(cpuRule).join();
```

### 2.8 安全管理模块

#### 功能概述
- 终端设备的认证和授权
- 网络链路的安全加密
- 命令的安全传输
- 配置信息的安全存储

#### 主要类和接口
- `SecurityManager`: 安全管理接口
- `SecurityManagerImpl`: 安全管理实现
- `AuthenticationResult`: 认证结果模型
- `AuthorizationResult`: 授权结果模型
- `Permission`: 权限模型
- `SecurityPolicy`: 安全策略模型
- `SecurityEvent`: 安全事件模型

#### 使用示例

```java
// 初始化安全管理器
SecurityManager securityManager = new SecurityManagerImpl(storageManager);
securityManager.initialize().join();

// 认证用户
AuthenticationResult authResult = securityManager.authenticate("admin", "admin123").join();
if (authResult.isSuccess()) {
    System.out.println("Authentication successful, token: " + authResult.getToken());
}

// 授权检查
AuthorizationResult authzResult = securityManager.authorize("admin", "terminal", "read").join();
if (authzResult.isGranted()) {
    System.out.println("Authorization granted");
}

// 加密数据
String encrypted = securityManager.encrypt("sensitive data").join();
System.out.println("Encrypted: " + encrypted);

// 解密数据
String decrypted = securityManager.decrypt(encrypted).join();
System.out.println("Decrypted: " + decrypted);
```

## 3. 配置和部署

### 3.1 配置项

| 配置项 | 描述 | 默认值 |
|-------|------|-------|
| `storage.path` | 存储路径 | `./storage` |
| `monitoring.interval` | 监控间隔（秒） | `30` |
| `monitoring.retention` | 监控数据保留天数 | `7` |
| `security.jwt.secret` | JWT密钥 | `ooder-sdk-jwt-secret-key` |
| `security.token.expiration` | 令牌过期时间（毫秒） | `86400000` |

### 3.2 部署步骤

1. **添加依赖**
   ```xml
   <dependency>
       <groupId>com.github.ooderCN</groupId>
       <artifactId>agent-sdk</artifactId>
       <version>0.6.6</version>
   </dependency>
   ```

2. **初始化SDK**
   ```java
   // 初始化存储管理器
   StorageManager storageManager = new FileSystemStorageManager("/path/to/storage");
   storageManager.initialize().join();
   
   // 初始化链路管理器
   LinkTableManager linkTableManager = new LinkTableManagerImpl();
   EnhancedLinkManager linkManager = new EnhancedLinkManagerImpl(linkTableManager);
   
   // 初始化终端管理器
   TerminalManager terminalManager = new TerminalManagerImpl();
   
   // 初始化路由管理器
   RouteManager routeManager = new RouteManagerImpl();
   
   // 初始化拓扑管理器
   TopologyManager topologyManager = new TopologyManagerImpl();
   
   // 初始化命令处理器
   CommandProcessor commandProcessor = new CommandProcessorImpl();
   
   // 初始化监控管理器
   MonitoringManager monitoringManager = new MonitoringManagerImpl(linkManager, terminalManager);
   
   // 初始化安全管理器
   SecurityManager securityManager = new SecurityManagerImpl(storageManager);
   securityManager.initialize().join();
   ```

3. **启动服务**
   ```java
   // 启动终端发现
   terminalManager.startTerminalDiscovery();
   
   // 启动路由发现
   routeManager.startRouteDiscovery();
   
   // 构建拓扑
   topologyManager.buildTopology().join();
   
   // 启动链路监控
   linkManager.startLinkMonitoring();
   
   // 启动监控
   monitoringManager.startMonitoring().join();
   ```

## 4. 故障排除

### 4.1 常见问题

| 问题 | 可能原因 | 解决方案 |
|-----|---------|--------|
| 终端发现失败 | 网络连接问题 | 检查网络连接，确保设备在同一网络 |
| 路由计算错误 | 拓扑信息不准确 | 重新构建网络拓扑 |
| 链路监控无数据 | 链路未激活 | 确保链路状态为ACTIVE |
| 认证失败 | 用户名或密码错误 | 检查用户凭证 |
| 授权失败 | 权限不足 | 检查用户权限配置 |

### 4.2 日志和调试

SDK使用标准Java日志系统，可通过配置日志级别来获取详细的调试信息：

```java
// 配置日志级别
System.setProperty("java.util.logging.config.file", "logging.properties");
```

## 5. 版本历史

| 版本 | 日期 | 变更内容 |
|-----|------|--------|
| 1.0.0 | 2026-01-31 | 初始版本，包含所有核心模块 |

## 6. 贡献指南

欢迎贡献代码和提出问题。请遵循以下步骤：

1. Fork 仓库
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建拉取请求

## 7. 许可证

本SDK使用Apache 2.0许可证。

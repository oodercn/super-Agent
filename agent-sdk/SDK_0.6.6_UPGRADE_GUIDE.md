# SDK 0.6.6 版本升级指南

## 1. 版本概述

SDK 0.6.6 版本是一个重大升级版本，引入了多项重要的技术改进，包括：

- **完整的 Spring Boot 配置体系**：基于 `@ConfigurationProperties` 的统一配置管理
- **增强的 UDP 监控管理机制**：实时指标收集、EWMA 延迟估算、吞吐量和错误率计算
- **重构的网络结构**：从单例模式重构为 Spring 组件，智能端口分配策略
- **新增异步处理能力**：基于 `CompletableFuture` 的异步执行，支持带返回值和无返回值的任务
- **消除硬编码值**：所有网络参数可配置，Agent 端口可通过配置文件管理

## 2. 主要变更

### 2.1 PortManager 变更

**核心变更**：从单例模式改为 Spring 组件

**旧代码**：
```java
// 使用单例模式获取实例
PortManager portManager = PortManager.getInstance();
int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
```

**新代码**：
```java
// 使用 Spring 依赖注入
@Autowired
private PortManager portManager;

// 或使用构造函数注入
private final PortManager portManager;

@Autowired
public MyService(PortManager portManager) {
    this.portManager = portManager;
}

// 分配端口
int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
```

### 2.2 UDPSDK 变更

**核心变更**：构造函数变更，现在需要多个配置参数

**旧代码**：
```java
// 使用 UDPConfig 创建 UDPSDK
UDPConfig config = UDPConfig.builder()
        .port(5000)
        .bufferSize(8192)
        .maxPacketSize(65536)
        .build();

UDPSDK udpSDK = new UDPSDK(config);
```

**新代码**：
```java
// 使用 Spring 依赖注入
@Autowired
private UDPSDK udpSDK;

// 或使用构造函数注入
private final UDPSDK udpSDK;

@Autowired
public MyService(UDPSDK udpSDK) {
    this.udpSDK = udpSDK;
}

// 发送命令（使用新的方法名）
udpSDK.sendCommand(commandPacket).thenAccept(result -> {
    if (result.isSuccess()) {
        System.out.println("Command sent successfully");
    }
});
```

### 2.3 方法名变更

**旧方法** | **新方法** | **说明**
---|---|---
`sendBroadcastCommand()` | `sendCommand()` | 发送广播命令
`sendBroadcastHeartbeat()` | `sendHeartbeat()` | 发送广播心跳

### 2.4 新增配置类

SDK 0.6.6 引入了以下配置类：

- `NetworkProperties`：网络配置
- `PortProperties`：端口配置
- `RetryProperties`：重试配置
- `PerformanceProperties`：性能配置
- `MonitoringProperties`：监控配置
- `AgentProperties`：Agent 端口配置
- `SkillDiscoveryProperties`：技能发现配置
- `TerminalDiscoveryProperties`：终端发现配置

### 2.5 新增异步执行服务

SDK 0.6.6 新增了 `AsyncExecutorService` 接口，提供基于 `CompletableFuture` 的异步执行能力：

```java
@Autowired
private AsyncExecutorService asyncExecutorService;

// 执行带返回值的异步任务
CompletableFuture<String> future = asyncExecutorService.executeAsync(() -> {
    // 执行耗时操作
    return "Task result";
});

// 执行无返回值的异步任务
asyncExecutorService.executeAsync(() -> {
    // 执行后台操作
});

// 执行带超时控制的任务
CompletableFuture<String> timeoutFuture = asyncExecutorService.executeAsyncWithTimeout(
    () -> {
        // 可能超时的操作
        return "Timeout test";
    }, 
    2000 // 2秒超时
);
```

## 3. 升级步骤

### 步骤 1: 更新依赖版本

在您的项目 `pom.xml` 文件中更新 Agent SDK 版本：

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.6.6</version>
</dependency>
```

### 步骤 2: 添加测试配置类

创建测试配置类，用于在测试环境中提供必要的 Bean：

```java
@SpringBootApplication
@Profile("test")
public class TestConfiguration {
    
    @Bean
    public NetworkProperties networkProperties() {
        NetworkProperties properties = new NetworkProperties();
        properties.setPort(5000);
        properties.setBufferSize(8192);
        // 设置其他属性
        return properties;
    }
    
    // 添加其他配置 Bean
}
```

### 步骤 3: 更新测试用例

将测试用例从使用单例模式改为使用 Spring 依赖注入：

```java
@SpringBootTest
@ActiveProfiles("test")
public class MyTest {
    
    @Autowired
    private PortManager portManager;
    
    @Autowired
    private UDPSDK udpSDK;
    
    // 测试方法
}
```

### 步骤 4: 更新生产代码

将生产代码从使用单例模式改为使用 Spring 依赖注入：

```java
@Service
public class MyService {
    
    private final PortManager portManager;
    private final UDPSDK udpSDK;
    
    @Autowired
    public MyService(PortManager portManager, UDPSDK udpSDK) {
        this.portManager = portManager;
        this.udpSDK = udpSDK;
    }
    
    // 业务方法
}
```

### 步骤 5: 创建配置文件

在 `src/main/resources` 目录下创建 `application.properties` 文件，配置必要的参数：

```properties
# 网络配置
ooder.sdk.network.port=5000
ooder.sdk.network.buffer-size=8192
ooder.sdk.network.max-packet-size=65536
ooder.sdk.network.timeout=30000
ooder.sdk.network.ack-timeout=5000
ooder.sdk.network.socket-reuse=true
ooder.sdk.network.socket-broadcast=true
ooder.sdk.network.broadcast-address=255.255.255.255
ooder.sdk.network.default-port=8080

# 端口配置
ooder.sdk.port.local-start=8000
ooder.sdk.port.local-end=8100
ooder.sdk.port.lan-start=9000
ooder.sdk.port.lan-end=9100
ooder.sdk.port.intranet-start=10000
ooder.sdk.port.intranet-end=10100
ooder.sdk.port.global-start=1024
ooder.sdk.port.global-end=65535
ooder.sdk.port.smart-allocation-enabled=true
ooder.sdk.port.history-size=1000
ooder.sdk.port.cleanup-interval-ms=3600000
ooder.sdk.port.allocation-strategy=DYNAMIC

# 重试配置
ooder.sdk.retry.max-retries=3
ooder.sdk.retry.delay-base=1000
ooder.sdk.retry.strategy=EXPONENTIAL
ooder.sdk.retry.jitter-enabled=true

# 性能配置
ooder.sdk.performance.optimizer-enabled=true
ooder.sdk.performance.compression-enabled=true
ooder.sdk.performance.compression-threshold=1024
ooder.sdk.performance.adaptive-buffer=true
ooder.sdk.performance.adaptive-timeout=true
ooder.sdk.performance.connection-pool-enabled=true
ooder.sdk.performance.connection-pool-size=10
ooder.sdk.performance.thread-pool-size=8
ooder.sdk.performance.use-nio=true

# 监控配置
ooder.sdk.monitoring.enabled=true
ooder.sdk.monitoring.metrics-collection-enabled=true
ooder.sdk.monitoring.metrics-collection-interval-ms=5000
ooder.sdk.monitoring.alert-enabled=true
ooder.sdk.monitoring.error-threshold=10
ooder.sdk.monitoring.latency-threshold-ms=5000
ooder.sdk.performance.throughput-threshold-bytes=1000000
ooder.sdk.monitoring.reporting-enabled=true
ooder.sdk.monitoring.reporting-interval-ms=60000
ooder.sdk.monitoring.intelligent-monitoring-enabled=false
ooder.sdk.monitoring.anomaly-threshold=3.0
ooder.sdk.monitoring.prediction-horizon=10

# Agent配置
ooder.sdk.agent.endagent.default-port=9000
ooder.sdk.agent.routeagent.default-port=8080
ooder.sdk.agent.mcpagent.default-port=7070

# 技能发现配置
skill.discovery.buffer-size=1024
skill.discovery.broadcast-interval-ms=5000
skill.discovery.max-retries=10
skill.discovery.port=5000
skill.discovery.broadcast-address=255.255.255.255
skill.discovery.skill-port=9000

# 终端发现配置
terminal.discovery.scan-interval-ms=30000
```

## 4. 兼容性说明

### 4.1 向后兼容的 API

- `AgentFactory.createEndAgent()`
- `AgentFactory.createRouteAgent()`
- `AgentFactory.createMcpAgent()`
- `UDPSDK` 的核心方法（除构造函数外）
- 所有 Agent 接口方法

### 4.2 不兼容的变更

1. **PortManager 不再是单例**：现在是 Spring 组件，通过依赖注入获取
2. **UDPSDK 构造函数变更**：现在需要多个配置参数，建议通过 Spring 依赖注入获取
3. **方法名变更**：`sendBroadcastCommand()` 改为 `sendCommand()`，`sendBroadcastHeartbeat()` 改为 `sendHeartbeat()`
4. **配置管理方式变更**：从硬编码改为配置文件管理

## 5. 故障排除

### 5.1 常见问题

**问题**：`No qualifying bean of type 'net.ooder.sdk.network.udp.PortManager' available`
**解决方案**：确保使用了 `@SpringBootApplication` 注解，并且配置类被正确扫描

**问题**：`java.lang.NullPointerException` 在测试中
**解决方案**：确保测试类使用了 `@SpringBootTest` 和 `@ActiveProfiles("test")` 注解

**问题**：端口冲突
**解决方案**：在配置文件中调整端口范围，或启用智能端口分配

### 5.2 日志和调试

SDK 0.6.6 引入了增强的监控功能，可以通过以下方式获取详细的调试信息：

```java
// 获取指标收集器
UDPMetricsCollector metricsCollector = udpSDK.getMetricsCollector();

// 打印端口统计信息
portManager.printPortStatistics();
```

## 6. 最佳实践

### 6.1 使用 Spring Boot 启动器

```java
@SpringBootApplication
@Import(SDKConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 6.2 合理配置线程池

根据系统资源调整异步执行服务的线程池大小：

```properties
# 在 application.properties 中配置
spring.task.execution.pool.core-size=4
spring.task.execution.pool.max-size=8
spring.task.execution.pool.queue-capacity=1000
```

### 6.3 启用监控和指标收集

```properties
# 启用监控
ooder.sdk.monitoring.enabled=true
ooder.sdk.monitoring.metrics-collection-enabled=true

# 配置监控参数
ooder.sdk.monitoring.metrics-collection-interval-ms=5000
ooder.sdk.monitoring.alert-enabled=true
```

### 6.4 优化网络配置

根据网络环境调整配置：

```properties
# 局域网环境
ooder.sdk.network.broadcast-address=192.168.1.255
ooder.sdk.network.default-port=9000

# 互联网环境
# ooder.sdk.network.broadcast-address=255.255.255.255
# ooder.sdk.network.default-port=10000
```

## 7. 总结

SDK 0.6.6 版本是一个重大升级，引入了多项重要的技术改进，包括完整的 Spring Boot 配置体系、增强的 UDP 监控管理机制、重构的网络结构、新增异步处理能力和消除硬编码值。

虽然升级过程中需要对代码进行一些修改，但这些变更带来了更好的可维护性、可测试性和性能。通过遵循本指南，您可以顺利完成从 SDK 0.6.5 到 SDK 0.6.6 的升级。

## 8. 联系方式

如果您在升级过程中遇到任何问题，请通过以下方式获取支持：

- **GitHub Issues**：https://github.com/oodercn/super-Agent/issues
- **文档中心**：https://docs.ooder.net/agent-sdk
- **技术支持**：support@ooder.net

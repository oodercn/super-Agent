# Ooder Agent SDK 升级指南

## 从 0.6.5 升级到 0.6.6

### 📋 升级概览

Ooder Agent SDK 0.6.6 版本带来了全新的配置体系、增强的监控能力、重构的网络结构和异步处理能力，为开发者提供了更灵活、更可靠的Agent开发框架。

### 🚀 主要新特性

#### 1. 完整的 Spring Boot 配置体系
- ✅ 基于 `@ConfigurationProperties` 的统一配置管理
- ✅ 支持 `application.properties` 热配置
- ✅ 8个专用配置类，覆盖所有配置项
- ✅ 智能配置默认值和类型安全

#### 2. 增强的 UDP 监控管理机制
- ✅ 实时指标收集和监控
- ✅ EWMA 延迟估算
- ✅ 吞吐量和错误率计算
- ✅ 端口级别的指标监控
- ✅ 自动清理过期指标

#### 3. 重构的网络结构
- ✅ 从单例模式重构为 Spring 组件
- ✅ 智能端口分配策略
- ✅ 基于网络环境的端口选择
- ✅ 端口冲突自动检测和处理

#### 4. 新增异步处理能力
- ✅ 基于 `CompletableFuture` 的异步执行
- ✅ 支持带返回值和无返回值的任务
- ✅ 内置超时控制机制
- ✅ 优雅的错误处理
- ✅ 线程池优化配置

#### 5. 消除硬编码值
- ✅ 所有网络参数可配置
- ✅ Agent 端口可通过配置文件管理
- ✅ 服务发现和终端发现参数可配置
- ✅ 心跳和重试参数可配置

### 🛠️ 升级步骤

#### 步骤 1: 更新依赖版本

在您的项目 `pom.xml` 文件中更新 Agent SDK 版本：

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.6.6</version>
</dependency>
```

#### 步骤 2: 创建/更新配置文件

在 `src/main/resources` 目录下创建或更新 `application.properties` 文件：

```properties
# Ooder Agent SDK 配置文件

# 网络配置
ooder.sdk.network.broadcast-address=255.255.255.255
ooder.sdk.network.default-port=8080
ooder.sdk.network.buffer-size=8192
ooder.sdk.network.max-packet-size=65536
ooder.sdk.network.timeout=30000
ooder.sdk.network.ack-timeout=5000
ooder.sdk.network.socket-reuse=true
ooder.sdk.network.socket-broadcast=true

# 重试配置
ooder.sdk.retry.max-retries=3
ooder.sdk.retry.delay-base=1000
ooder.sdk.retry.strategy=EXPONENTIAL
ooder.sdk.retry.jitter-enabled=true

# 端口配置
ooder.sdk.port.allocation-strategy=DYNAMIC
ooder.sdk.port.local-start=8080
ooder.sdk.port.local-end=8192
ooder.sdk.port.lan-start=9000
ooder.sdk.port.lan-end=9100
ooder.sdk.port.intranet-start=10000
ooder.sdk.port.intranet-end=10100
ooder.sdk.port.global-start=1024
ooder.sdk.port.global-end=65535
ooder.sdk.port.smart-allocation-enabled=true
ooder.sdk.port.history-size=1000
ooder.sdk.port.cleanup-interval-ms=3600000

# Agent端口配置
ooder.sdk.agent.endagent.default-port=9000
ooder.sdk.agent.routeagent.default-port=8080
ooder.sdk.agent.mcpagent.default-port=7070

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
ooder.sdk.monitoring.throughput-threshold-bytes=1000000
ooder.sdk.monitoring.reporting-enabled=true
ooder.sdk.monitoring.reporting-interval-ms=60000
ooder.sdk.monitoring.intelligent-monitoring-enabled=false
ooder.sdk.monitoring.anomaly-threshold=3.0
ooder.sdk.monitoring.prediction-horizon=10

# 服务发现配置
skill.discovery.buffer-size=1024
skill.discovery.broadcast-interval-ms=5000
skill.discovery.max-retries=10
skill.discovery.port=5000
skill.discovery.broadcast-address=255.255.255.255
skill.discovery.skill-port=9000

# 终端发现配置
terminal.discovery.scan-interval-ms=30000

# Agent配置
agent.config.udp-port=9001
agent.config.udp-buffer-size=65535
agent.config.udp-timeout=5000
agent.config.udp-max-packet-size=65507
agent.config.heartbeat-interval=30000
agent.config.heartbeat-timeout=90000
agent.config.heartbeat-loss-threshold=3
agent.config.retry-max-retries=5
agent.config.retry-initial-interval=1000
agent.config.retry-max-interval=30000
agent.config.retry-backoff-factor=2.0

# 日志配置
logging.level.net.ooder.sdk=INFO
logging.file.name=logs/ooder-sdk.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
# Spring Boot 配置
spring.main.banner-mode=off
spring.application.name=ooder-agent-sdk
```

#### 步骤 3: 更新 AgentFactory 初始化

AgentFactory 现在需要设置 ApplicationContext 以获取配置：

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import net.ooder.sdk.agent.factory.AgentFactory;
import net.ooder.sdk.config.SDKConfiguration;

// 初始化 Spring 上下文
ApplicationContext context = new AnnotationConfigApplicationContext(SDKConfiguration.class);

// 设置 ApplicationContext 到 AgentFactory
AgentFactory.setApplicationContext(context);

// 现在可以创建 Agent 了
EndAgent endAgent = AgentFactory.createEndAgent("agent-1", "Test Agent", capabilities);
```

#### 步骤 4: 使用新的异步执行服务

```java
import net.ooder.sdk.async.AsyncExecutorService;
import org.springframework.beans.factory.annotation.Autowired;

@Autowired
private AsyncExecutorService asyncExecutorService;

// 执行带返回值的异步任务
CompletableFuture<String> future = asyncExecutorService.executeAsync(() -> {
    // 执行耗时操作
    return "Task result";
});

// 获取结果
String result = future.get(5000, TimeUnit.MILLISECONDS);

// 执行无返回值的异步任务
asyncExecutorService.executeAsync(() -> {
    // 执行后台操作
});

// 带超时控制的任务
CompletableFuture<String> timeoutFuture = asyncExecutorService.executeAsyncWithTimeout(
    () -> {
        // 可能超时的操作
        return "Timeout test";
    }, 
    2000 // 2秒超时
);
```

#### 步骤 5: 使用新的配置管理

```java
import net.ooder.sdk.config.NetworkProperties;
import net.ooder.sdk.config.AgentProperties;
import org.springframework.beans.factory.annotation.Autowired;

@Autowired
private NetworkProperties networkProperties;

@Autowired
private AgentProperties agentProperties;

// 使用配置
String broadcastAddress = networkProperties.getBroadcastAddress();
int endAgentPort = agentProperties.getEndagentDefaultPort();
```

### 🔄 向后兼容性

#### 兼容的 API
- ✅ `AgentFactory.createEndAgent()`
- ✅ `AgentFactory.createRouteAgent()`
- ✅ `AgentFactory.createMcpAgent()`
- ✅ `UDPSDK` 的核心方法
- ✅ 所有 Agent 接口方法

#### 不兼容的变更
1. **UDPSDK 构造函数变更**：现在需要多个配置参数，建议通过 Spring 依赖注入获取
2. **PortManager 不再是单例**：现在是 Spring 组件，通过依赖注入获取
3. **配置管理方式变更**：从硬编码改为配置文件管理

### 📁 新增文件结构

```
src/main/java/net/ooder/sdk/
├── async/
│   ├── AsyncConfiguration.java       # 异步执行配置
│   └── AsyncExecutorService.java      # 异步执行服务
├── config/
│   ├── AgentConfigProperties.java     # Agent详细配置
│   ├── AgentProperties.java           # Agent端口配置
│   ├── MonitoringProperties.java      # 监控配置
│   ├── NetworkProperties.java         # 网络配置
│   ├── PerformanceProperties.java     # 性能配置
│   ├── PortProperties.java            # 端口配置
│   ├── RetryProperties.java           # 重试配置
│   ├── SDKConfiguration.java          # 统一配置管理
│   └── TerminalDiscoveryProperties.java # 终端发现配置
├── network/
│   └── udp/
│       ├── PortManager.java           # 智能端口管理器
│       └── monitoring/
│           ├── MetricEntry.java       # 指标条目
│           ├── PortMetrics.java       # 端口指标
│           ├── UDPMetricsCollector.java # UDP指标收集器
│           └── UDPMetricsSnapshot.java # 指标快照
└── agent/
    └── model/
        └── AgentConfig.java           # Agent配置模型（支持从配置文件加载）
```

### 🧪 新增测试用例

0.6.6 版本新增了完整的测试套件，覆盖所有新功能：

- ✅ `AsyncExecutorServiceTest.java` - 异步执行服务测试
- ✅ `UDPMetricsCollectorTest.java` - UDP指标收集器测试
- ✅ `PortManagerEnhancedTest.java` - 端口管理器增强测试
- ✅ `ConfigurationTest.java` - 配置类测试

### 🎯 最佳实践

#### 1. 使用 Spring Boot 启动器

```java
@SpringBootApplication
@Import(SDKConfiguration.class)
public class AgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }
}
```

#### 2. 合理配置线程池

根据您的系统资源调整异步执行服务的线程池大小：

```properties
# 在 application.properties 中配置
spring.task.execution.pool.core-size=4
spring.task.execution.pool.max-size=8
spring.task.execution.pool.queue-capacity=1000
```

#### 3. 启用监控和指标收集

```properties
# 启用监控
ooder.sdk.monitoring.enabled=true
ooder.sdk.monitoring.metrics-collection-enabled=true

# 配置监控参数
ooder.sdk.monitoring.metrics-collection-interval-ms=5000
ooder.sdk.monitoring.alert-enabled=true
```

#### 4. 优化网络配置

根据您的网络环境调整配置：

```properties
# 局域网环境
ooder.sdk.network.broadcast-address=192.168.1.255
ooder.sdk.network.default-port=9000

# 互联网环境
# ooder.sdk.network.broadcast-address=255.255.255.255
# ooder.sdk.network.default-port=10000
```

### 📞 支持和反馈

如果您在升级过程中遇到任何问题，请通过以下方式获取支持：

- **GitHub Issues**: https://github.com/oodercn/super-Agent/issues
- **文档中心**: https://docs.ooder.net/agent-sdk
- **技术支持**: support@ooder.net

### 📖 相关文档

- [Agent SDK 开发指南](https://docs.ooder.net/agent-sdk/developer-guide)
- [配置参考手册](https://docs.ooder.net/agent-sdk/configuration)
- [API 文档](https://docs.ooder.net/agent-sdk/api)
- [示例代码](https://github.com/oodercn/super-Agent/tree/main/agent-sdk/src/examples)

---

**Ooder Agent SDK 0.6.6** - 为您的 Agent 开发提供更强大、更灵活的框架！🚀

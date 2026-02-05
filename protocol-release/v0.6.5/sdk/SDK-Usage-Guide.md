# Agent SDK 上层应用调用指南

## 1. SDK 概述

Agent SDK 是一个用于实现南向协议的开发工具包，版本 v0.6.3。它提供了以下核心功能：

- Agent 生命周期管理（初始化、启动、停止）
- 命令发送与接收
- 网络链路管理
- 心跳机制
- 事件处理
- 网络状态监控

## 2. 安装与配置

### 2.1 Maven 依赖配置

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.6.3</version>
    <scope>compile</scope>
</dependency>
```

### 2.2 基本配置参数

| 参数 | 描述 | 默认值 |
|------|------|--------|
| agentId | Agent 唯一标识符 | 无，必须设置 |
| agentName | Agent 名称 | 无，必须设置 |
| agentType | Agent 类型 | 无，必须设置 |
| endpoint | 端点地址 | 无，必须设置 |
| udpPort | UDP 端口 | 无，必须设置 |
| heartbeatInterval | 心跳间隔（毫秒） | 30000 |

## 3. 快速开始

### 3.1 初始化与启动

```java
import net.ooder.sdk.AgentConfig;
import net.ooder.sdk.AgentSDK;

// 1. 创建配置
AgentConfig config = AgentConfig.builder()
        .agentId("my-agent-001")
        .agentName("My Agent")
        .agentType("mcp")
        .endpoint("localhost:9876")
        .udpPort(9876)
        .heartbeatInterval(30000)
        .build();

// 2. 初始化 SDK
AgentSDK agentSDK = new AgentSDK(config);

// 3. 启动 SDK
agentSDK.start();

// 4. 停止 SDK（在应用关闭时）
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    agentSDK.stop();
}));
```

### 3.2 集成到 Spring Boot 应用

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import net.ooder.sdk.AgentConfig;
import net.ooder.sdk.AgentSDK;

@Configuration
public class AppConfig {
    
    @Bean
    public AgentSDK agentSDK() throws Exception {
        AgentConfig config = AgentConfig.builder()
                .agentId("spring-agent-001")
                .agentName("Spring Boot Agent")
                .agentType("mcp")
                .endpoint("localhost:9876")
                .udpPort(9876)
                .heartbeatInterval(30000)
                .build();
        
        AgentSDK sdk = new AgentSDK(config);
        return sdk;
    }
}
```

## 4. 核心功能使用

### 4.1 命令发送

```java
import net.ooder.sdk.packet.CommandPacket;
import net.ooder.sdk.enums.CommandType;

// 创建命令包
CommandPacket packet = new CommandPacket();
packet.setCommandType(CommandType.MCP_REGISTER);
packet.setAgentId("target-agent-001");
packet.setData("{\"key\": \"value\"}");

// 发送命令
boolean success = agentSDK.sendCommand(packet);
if (success) {
    System.out.println("命令发送成功");
} else {
    System.out.println("命令发送失败");
}
```

### 4.2 命令处理

创建命令处理类并实现相应的接口：

```java
public class MyCommandHandler implements McpAgentSkill {
    
    private AgentSDK agentSDK;
    
    @Override
    public void initialize(AgentSDK sdk) {
        this.agentSDK = sdk;
        // 初始化逻辑
    }
    
    @Override
    public void handleMcpRegisterCommand(CommandPacket packet) {
        // 处理注册命令
        System.out.println("处理注册命令: " + packet);
    }
    
    // 实现其他命令处理方法...
    
    @Override
    public void start() {
        // 启动逻辑
    }
    
    @Override
    public void stop() {
        // 停止逻辑
    }
}
```

### 4.3 网络链路管理

```java
// 添加网络链路
public void addNetworkLink(String linkId, String sourceAgentId, String targetAgentId, String linkType) {
    // 创建链路信息
    NetworkLink link = new NetworkLink();
    link.setLinkId(linkId);
    link.setSourceAgentId(sourceAgentId);
    link.setTargetAgentId(targetAgentId);
    link.setLinkType(linkType);
    link.setStatus("active");
    link.setCreateTime(System.currentTimeMillis());
    
    // 存储链路
    networkLinks.put(linkId, link);
}

// 更新链路状态
public void updateNetworkLinkStatus(String linkId, String status) {
    NetworkLink link = networkLinks.get(linkId);
    if (link != null) {
        link.setStatus(status);
        link.setLastUpdateTime(System.currentTimeMillis());
    }
}
```

### 4.4 网络状态监控

```java
// 监控网络状态
private void startNetworkMonitoring() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    executor.scheduleAtFixedRate(() -> {
        try {
            checkNetworkStatus();
            checkLinkHealth();
            logNetworkStats();
        } catch (Exception e) {
            System.err.println("网络监控错误: " + e.getMessage());
        }
    }, 10, 30, TimeUnit.SECONDS);
}

// 检查网络状态
private void checkNetworkStatus() {
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastPacketReceivedTime > networkTimeoutThreshold) {
        setNetworkStatus(NetworkStatus.TIMEOUT);
        System.out.println("网络超时: 长时间未收到数据包");
    } else {
        setNetworkStatus(NetworkStatus.OK);
    }
}
```

## 5. 高级功能

### 5.1 命令队列与超时处理

```java
// 命令任务队列
private final ConcurrentLinkedQueue<CommandTask> commandQueue = new ConcurrentLinkedQueue<>();
private final Map<String, CommandTask> activeCommands = new ConcurrentHashMap<>();

// 处理命令
private void processCommandQueue() {
    while (!commandQueue.isEmpty() && activeCommands.size() < maxConcurrentCommands) {
        CommandTask task = commandQueue.poll();
        if (task != null) {
            activeCommands.put(task.getCommandId(), task);
            // 异步处理命令
            executorService.submit(() -> {
                try {
                    executeCommand(task);
                } finally {
                    activeCommands.remove(task.getCommandId());
                }
            });
        }
    }
}
```

### 5.2 网络拓扑管理

```java
// 获取网络拓扑
public Map<String, Object> getNetworkTopology() {
    Map<String, Object> topology = new HashMap<>();
    topology.put("agents", routeAgents);
    topology.put("endAgents", endAgents);
    topology.put("links", networkLinks);
    topology.put("networkStatus", networkStatus);
    topology.put("networkStats", getNetworkStats());
    topology.put("timestamp", System.currentTimeMillis());
    return topology;
}
```

### 5.3 LLM 交互管理

```java
// 注册LLM提供者
public void registerLlmProvider(String providerId, Map<String, Object> providerInfo) {
    llmProviders.put(providerId, providerInfo);
}

// 发送LLM请求
public Map<String, Object> sendLlmRequest(String providerId, Map<String, Object> requestData) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("providerId", providerId);
    response.put("requestData", requestData);
    response.put("responseData", "LLM response data");
    response.put("timestamp", System.currentTimeMillis());
    return response;
}
```

## 6. 最佳实践

### 6.1 代码结构建议

```
├── config/           # 配置类
├── handler/          # 命令处理器
├── manager/          # 管理器类
├── model/            # 数据模型
├── service/          # 业务服务
├── util/             # 工具类
└── Application.java  # 应用入口
```

### 6.2 异常处理

```java
try {
    // SDK 操作
    agentSDK.sendCommand(packet);
} catch (Exception e) {
    // 记录错误
    logger.error("SDK 操作失败: {}", e.getMessage(), e);
    // 恢复策略
    recoverFromError();
}
```

### 6.3 日志管理

使用 SLF4J 进行日志记录：

```java
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

// 不同级别的日志
logger.info("信息日志");
logger.warn("警告日志");
logger.error("错误日志");
logger.debug("调试日志");
```

### 6.4 资源管理

使用 try-with-resources 或显式关闭资源：

```java
// 正确关闭 SDK
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    try {
        mcpAgentSkill.stop();
        agentSDK.stop();
        logger.info("应用已安全关闭");
    } catch (Exception e) {
        logger.error("关闭过程中发生错误: {}", e.getMessage());
    }
}));
```

## 7. 常见问题与解决方案

### 7.1 网络连接问题

- **症状**：命令发送失败，网络状态显示为 ERROR
- **解决方案**：
  1. 检查网络连接
  2. 验证目标 Agent 是否在线
  3. 检查防火墙设置
  4. 查看 UDP 端口是否被占用

### 7.2 命令超时

- **症状**：命令执行时间过长或无响应
- **解决方案**：
  1. 检查命令处理逻辑
  2. 调整超时阈值
  3. 优化网络性能
  4. 增加命令队列处理能力

### 7.3 内存泄漏

- **症状**：应用内存使用持续增长
- **解决方案**：
  1. 使用内存分析工具检测泄漏点
  2. 确保所有线程正确关闭
  3. 清理不再使用的资源
  4. 优化对象生命周期管理

## 8. 性能优化建议

1. **使用线程池**：处理并发命令时使用线程池，避免创建过多线程
2. **批量操作**：对于多个相似操作，考虑批量处理
3. **缓存策略**：合理使用缓存减少重复计算
4. **连接复用**：复用网络连接，减少连接建立开销
5. **异步处理**：对于耗时操作，使用异步处理方式
6. **资源限制**：设置合理的资源使用限制，避免系统过载

## 9. 版本兼容性

| SDK 版本 | Java 版本 | Spring Boot 版本 |
|---------|----------|-----------------|
| v0.6.3  | 1.8+     | 2.7.0+          |
| v0.6.2  | 1.8+     | 2.7.0+          |
| v0.6.1  | 1.8+     | 2.7.0+          |

## 10. 示例代码

### 10.1 完整应用示例

```java
import net.ooder.sdk.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.packet.CommandPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(AgentApplication.class);
    
    public static void main(String[] args) {
        logger.info("启动 Agent 应用...");
        
        try {
            // 1. 创建配置
            AgentConfig config = AgentConfig.builder()
                    .agentId("app-agent-001")
                    .agentName("示例应用 Agent")
                    .agentType("app")
                    .endpoint("localhost:9876")
                    .udpPort(9876)
                    .heartbeatInterval(30000)
                    .build();
            
            // 2. 初始化 SDK
            AgentSDK agentSDK = new AgentSDK(config);
            
            // 3. 启动 SDK
            agentSDK.start();
            logger.info("Agent SDK 启动成功");
            
            // 4. 发送测试命令
            CommandPacket testPacket = new CommandPacket();
            testPacket.setCommandType("TEST_COMMAND");
            testPacket.setAgentId("target-agent");
            testPacket.setData("{\"message\": \"Hello from SDK\"}");
            
            boolean success = agentSDK.sendCommand(testPacket);
            logger.info("测试命令发送结果: {}", success);
            
            // 5. 注册关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("关闭 Agent 应用...");
                agentSDK.stop();
                logger.info("Agent 应用已关闭");
            }));
            
            // 保持应用运行
            synchronized (AgentApplication.class) {
                AgentApplication.class.wait();
            }
            
        } catch (Exception e) {
            logger.error("应用启动失败: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
```

### 10.2 命令处理器示例

```java
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.packet.CommandPacket;

public class CommandHandler {
    
    private AgentSDK agentSDK;
    
    public void initialize(AgentSDK sdk) {
        this.agentSDK = sdk;
    }
    
    public void handleCommand(CommandPacket packet) {
        String commandType = packet.getCommandType();
        switch (commandType) {
            case "MCP_REGISTER":
                handleRegisterCommand(packet);
                break;
            case "MCP_HEARTBEAT":
                handleHeartbeatCommand(packet);
                break;
            // 处理其他命令类型...
            default:
                System.out.println("未知命令类型: " + commandType);
        }
    }
    
    private void handleRegisterCommand(CommandPacket packet) {
        System.out.println("处理注册命令: " + packet);
        // 处理逻辑...
    }
    
    private void handleHeartbeatCommand(CommandPacket packet) {
        System.out.println("处理心跳命令: " + packet);
        // 处理逻辑...
    }
}
```

## 11. 总结

Agent SDK v0.6.3 提供了一套完整的南向协议实现框架，上层应用可以通过以下步骤快速集成：

1. **配置依赖**：添加 SDK 到项目依赖
2. **初始化 SDK**：创建配置并初始化 SDK 实例
3. **实现业务逻辑**：根据需要实现命令处理器和业务逻辑
4. **启动 SDK**：启动 SDK 并开始处理命令
5. **监控与维护**：实现网络状态监控和错误处理
6. **优雅关闭**：在应用退出时正确关闭 SDK

通过合理使用 SDK 提供的功能，上层应用可以快速构建稳定、高效的南向协议实现，实现与其他 Agent 的无缝通信。

## 12. 联系与支持

- **文档**：参考项目中的 `SDK-集成说明.md` 和 `protocol-sdk-comparison.md`
- **问题反馈**：提交 Issue 到项目仓库
- **版本历史**：查看 `update-summary.md` 获取版本更新信息
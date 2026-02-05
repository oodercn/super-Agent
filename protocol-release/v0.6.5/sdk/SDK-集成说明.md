# Ooder Agent SDK 集成说明

## 1. 概述

Ooder Agent SDK 是一个用于开发和集成技能模块的Java SDK，提供了命令处理、参数验证、服务发现、心跳管理等核心功能，简化技能模块的开发流程。

## 2. 版本信息

- 当前版本：0.6.0
- 支持的Java版本：1.8及以上
- 支持的Spring Boot版本：2.7.0及以上

## 3. 安装与配置

### 3.1 Maven依赖配置

在项目的`pom.xml`文件中添加以下依赖：

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.6.0</version>
</dependency>
```

### 3.2 基本配置

在项目的`application.yml`文件中添加以下基本配置：

```yaml
# Agent Configuration
agent:
  agentId: skill-example-001
  agentName: ExampleSkill
  agentType: skill
  endpoint: http://localhost:9000
  udpPort: 9001
  heartbeatInterval: 30000
```

### 3.3 服务发现配置

```yaml
# Service Discovery Configuration
skill:
  discovery:
    port: 5000
    broadcast-address: 255.255.255.255
    skill-name: ExampleSkill
    skill-type: skill-example
    skill-port: 9000
```

## 4. 快速开始

### 4.1 创建技能应用类

```java
import lombok.extern.slf4j.Slf4j;
import net.ooder.sdk.AgentConfig;
import net.ooder.sdk.AgentSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication(
        scanBasePackages = "net.ooder.examples.skills",
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class ExampleSkillApplication implements CommandLineRunner {

    private AgentSDK agentSDK;
    private AgentConfig agentConfig;

    public static void main(String[] args) {
        SpringApplication.run(ExampleSkillApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "agent")
    public AgentConfig agentConfig() {
        return AgentConfig.builder().build();
    }

    @Autowired
    public void setAgentConfig(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Example Skill Application Starting");

        agentSDK = AgentSDK.builder().config(agentConfig).build();
        agentSDK.start();

        log.info("Example Skill started successfully");
        log.info("Agent ID: {}", agentConfig.getAgentId());
        log.info("Agent Type: {}", agentConfig.getAgentType());
        log.info("Endpoint: {}", agentConfig.getEndpoint());
        log.info("UDP Port: {}", agentConfig.getUdpPort());
    }
}
```

### 4.2 创建命令处理器

```java
import lombok.extern.slf4j.Slf4j;
import net.ooder.sdk.enums.CommandType;
import net.ooder.sdk.handler.AbstractSkillCommandHandler;
import net.ooder.sdk.handler.CommandHandler;
import net.ooder.sdk.packet.CommandPacket;
import net.ooder.sdk.validation.ValidParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleCommandHandler extends AbstractSkillCommandHandler {

    @CommandHandler(CommandType.SKILL_INVOKE)
    @ValidParam(name = "query", required = true, type = String.class, minLength = 1, maxLength = 1000)
    public void handleSkillInvoke(CommandPacket packet) {
        log.info("Handling skill.invoke command");
        log.info("Params: {}", packet.getParams());

        try {
            String query = (String) packet.getParams().get("query");
            String result = processQuery(query);

            log.info("Query result: {}", result);
        } catch (Exception e) {
            log.error("Error handling skill.invoke command", e);
        }
    }

    @CommandHandler(CommandType.SKILL_STATUS)
    public void handleSkillStatus(CommandPacket packet) {
        log.info("Handling skill.status command");
    }

    private String processQuery(String query) {
        return "Processed: " + query;
    }
}
```

## 5. 核心功能

### 5.1 命令处理

命令处理是SDK的核心功能之一，通过`@CommandHandler`注解标记方法来处理特定类型的命令：

```java
@CommandHandler(CommandType.SKILL_INVOKE)
public void handleSkillInvoke(CommandPacket packet) {
    // 处理skill.invoke命令
}
```

### 5.2 参数验证

使用`@ValidParam`注解对命令参数进行严格验证：

```java
@CommandHandler(CommandType.SKILL_INVOKE)
@ValidParam(name = "query", required = true, type = String.class, minLength = 1, maxLength = 1000)
@ValidParam(name = "count", required = false, type = Integer.class, min = 1, max = 100)
@ValidParam(name = "status", required = true, type = String.class, enumValues = {"ACTIVE", "INACTIVE"})
public void handleSkillInvoke(CommandPacket packet) {
    // 处理命令
}
```

参数验证支持以下特性：
- 必填检查
- 类型检查
- 长度限制（字符串类型）
- 范围限制（数字类型）
- 枚举值验证
- 正则表达式验证
- 集合元素类型验证
- Map键值类型验证

### 5.3 服务发现

SDK内置了服务发现功能，可以自动发现和注册到场景管理器：

```java
import net.ooder.sdk.discovery.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;

@Autowired
private DiscoveryService discoveryService;

public void checkDiscoveryStatus() {
    if (discoveryService.isJoined()) {
        log.info("Joined scene: {}", discoveryService.getSceneId());
    } else {
        log.warn("Not yet joined to any scene");
    }
}
```

### 5.4 心跳管理

SDK自动管理心跳，保持与场景管理器的连接：

```java
// 心跳配置已在AgentConfig中设置
AgentConfig config = AgentConfig.builder()
        .heartbeatInterval(30000) // 30秒心跳间隔
        .build();
```

## 6. 高级特性

### 6.1 重试机制

SDK支持命令重试功能：

```java
import net.ooder.sdk.retry.RetryStrategy;

RetryStrategy retryStrategy = RetryStrategy.builder()
        .type(RetryStrategy.RetryType.EXPONENTIAL)
        .maxRetries(5)
        .baseInterval(1000)
        .maxInterval(30000)
        .backoffMultiplier(2)
        .build();

AgentConfig config = AgentConfig.builder()
        .retryStrategy(retryStrategy)
        .build();
```

### 6.2 空闲睡眠策略

SDK支持空闲时的睡眠功能，节省资源：

```java
import net.ooder.sdk.sleep.SleepStrategy;

SleepStrategy.IdleSleepConfig idleSleep = new SleepStrategy.IdleSleepConfig();
idleSleep.setEnabled(true);
idleSleep.setIdleTimeout(300000); // 5分钟空闲超时
idleSleep.setCheckInterval(60000); // 1分钟检查间隔

SleepStrategy sleepStrategy = SleepStrategy.builder()
        .idleSleep(idleSleep)
        .build();

AgentConfig config = AgentConfig.builder()
        .sleepStrategy(sleepStrategy)
        .build();
```

## 7. 错误处理

### 7.1 命令处理错误

```java
@CommandHandler(CommandType.SKILL_INVOKE)
public void handleSkillInvoke(CommandPacket packet) {
    try {
        // 处理命令
    } catch (Exception e) {
        log.error("Error handling command", e);
        // 可以发送错误响应
    }
}
```

### 7.2 连接错误处理

```java
@Override
public void onError(UDPPacket packet, Exception e) {
    log.error("UDP connection error", e);
    // 处理连接错误
}
```

## 8. 测试

### 8.1 单元测试

```java
import net.ooder.sdk.AgentConfig;
import net.ooder.sdk.AgentSDK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExampleSkillTest {

    private AgentSDK agentSDK;

    @BeforeEach
    void setUp() {
        AgentConfig config = AgentConfig.builder()
                .agentId("test-skill-001")
                .agentName("TestSkill")
                .agentType("skill")
                .endpoint("http://localhost:9000")
                .udpPort(9001)
                .build();

        agentSDK = AgentSDK.builder().config(config).build();
    }

    @Test
    void testAgentStart() throws Exception {
        agentSDK.start();
        // 测试代码
        agentSDK.stop();
    }
}
```

## 9. 最佳实践

### 9.1 代码结构

- 使用Spring Boot的自动配置功能
- 将命令处理逻辑与业务逻辑分离
- 使用依赖注入管理组件

### 9.2 性能优化

- 避免在命令处理方法中执行长时间运行的操作
- 使用异步处理复杂任务
- 合理配置心跳间隔和超时时间

### 9.3 安全性

- 验证所有输入参数
- 限制命令执行权限
- 加密敏感数据

## 10. 故障排查

### 10.1 常见问题

1. **无法连接到场景管理器**
   - 检查网络连接
   - 确认UDP端口配置正确
   - 验证防火墙设置

2. **命令处理失败**
   - 检查参数验证配置
   - 查看日志文件中的错误信息
   - 验证命令类型和参数格式

3. **服务发现失败**
   - 检查广播地址配置
   - 确认发现端口是否被占用
   - 查看发现服务日志

### 10.2 日志配置

```yaml
logging:
  level:
    root: INFO
    net.ooder.sdk: DEBUG
    net.ooder.examples.skills: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

## 11. 更新日志

### 版本 0.6.0

- 新增统一命令处理框架
- 增强参数验证功能
- 实现统一服务发现机制
- 优化配置管理
- 改进错误处理

## 12. 联系与支持

- 文档地址：https://docs.ooder.net/sdk
- 问题反馈：support@ooder.net
- 技术支持：tech@ooder.net

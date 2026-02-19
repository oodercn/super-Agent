# 命令场景三级分解方案

## 一、方案概述

### 1.1 命令独立设计原则

命令作为特殊的消息类型，具有以下特点：
- **请求-响应模式**：命令需要返回执行结果
- **超时控制**：命令执行有明确的超时时间
- **优先级**：命令可按优先级排序执行
- **确认机制**：命令执行需要确认
- **重试策略**：命令失败可配置重试

### 1.2 三级架构定义

```
┌─────────────────────────────────────────────────────────────┐
│  Level 1: 客户端层 (Client Side)                             │
│  - 命令客户端代理                                            │
│  - 命令构建与发送                                            │
│  - 结果接收与处理                                            │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│  Level 2: 服务端层 (Server Side)                             │
│  - ProtocolHub 命令路由                                      │
│  - ProtocolHandler 命令处理                                  │
│  - 命令执行引擎                                              │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│  Level 3: 能力挂接层 (Capability Registration)               │
│  - 能力组件接口                                              │
│  - 南北向命令统一                                            │
│  - SDK 7.2 集成                                              │
└─────────────────────────────────────────────────────────────┘
```

### 1.3 SDK 7.2 命令能力

SDK 7.2 提供了完整的命令处理框架：

| SDK 接口 | 说明 | 命令场景对应 |
|---------|------|-------------|
| `ProtocolHub` | 协议中心 | 命令路由中心 |
| `CommandPacket` | 命令包 | 命令载体 |
| `CommandResult` | 命令结果 | 执行结果 |
| `ProtocolHandler` | 协议处理器 | 命令处理器 |

## 二、Level 1: 客户端层

### 2.1 客户端架构

```
┌─────────────────────────────────────────────────────────────┐
│  Client Side                                                │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  CommandClient (命令客户端接口)                       │    │
│  │  ├── sendCommand(command)                           │    │
│  │  ├── sendCommandAsync(command)                      │    │
│  │  ├── getCommandResult(commandId)                    │    │
│  │  ├── cancelCommand(commandId)                       │    │
│  │  └── addCommandListener(listener)                   │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  CommandBuilder (命令构建器)                        │      │
│  │  ├── protocolType(protocolType)                    │      │
│  │  ├── commandType(commandType)                      │      │
│  │  ├── payload(payload)                              │      │
│  │  ├── timeout(timeout)                              │      │
│  │  ├── priority(priority)                            │      │
│  │  └── build()                                       │      │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 客户端接口设计

```java
public interface CommandClient {
    
    CommandResult sendCommand(CommandPacket packet) throws CommandException;
    
    CompletableFuture<CommandResult> sendCommandAsync(CommandPacket packet);
    
    CommandResult sendCommand(CommandPacket packet, String target);
    
    CommandResult getCommandResult(String commandId);
    
    boolean cancelCommand(String commandId);
    
    void addCommandListener(CommandListener listener);
    
    void removeCommandListener(CommandListener listener);
}

public interface CommandListener {
    
    void onCommandSent(String commandId);
    
    void onCommandSuccess(String commandId, CommandResult result);
    
    void onCommandFailure(String commandId, CommandResult result);
    
    void onCommandTimeout(String commandId);
}
```

### 2.3 命令构建器

```java
public class CommandBuilder {
    
    private String protocolType;
    private String commandType;
    private Map<String, Object> payload = new HashMap<>();
    private String source;
    private String target;
    private int priority = 5;
    private long timeout = 30000;
    private boolean requiresAck = true;
    
    public static CommandBuilder create() {
        return new CommandBuilder();
    }
    
    public CommandBuilder protocolType(String protocolType) {
        this.protocolType = protocolType;
        return this;
    }
    
    public CommandBuilder commandType(String commandType) {
        this.commandType = commandType;
        return this;
    }
    
    public CommandBuilder payload(String key, Object value) {
        this.payload.put(key, value);
        return this;
    }
    
    public CommandBuilder target(String target) {
        this.target = target;
        return this;
    }
    
    public CommandBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }
    
    public CommandBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
    
    public CommandPacket build() {
        CommandPacket packet = CommandPacket.of(protocolType, commandType, payload);
        packet.setSource(source);
        packet.setTarget(target);
        packet.getHeader().setPriority(priority);
        packet.getHeader().setTimeout(timeout);
        packet.getHeader().setRequiresAck(requiresAck);
        return packet;
    }
}
```

### 2.4 客户端能力清单

| 能力ID | 能力名称 | 接口方法 | 说明 |
|--------|---------|---------|------|
| **命令发送** |||
| cmd-send | 同步发送命令 | `sendCommand(packet)` | 阻塞等待结果 |
| cmd-send-async | 异步发送命令 | `sendCommandAsync(packet)` | 返回 Future |
| cmd-send-target | 定向发送命令 | `sendCommand(packet, target)` | 指定目标节点 |
| **命令管理** |||
| cmd-get-result | 获取命令结果 | `getCommandResult(commandId)` | 查询结果 |
| cmd-cancel | 取消命令 | `cancelCommand(commandId)` | 取消执行 |
| **命令监听** |||
| cmd-listen | 命令监听 | `addCommandListener(listener)` | 事件回调 |

## 三、Level 2: 服务端层

### 3.1 服务端架构

```
┌─────────────────────────────────────────────────────────────┐
│  Server Side                                                │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  ProtocolHub (协议中心 - SDK 7.2)                    │    │
│  │  ├── registerProtocolHandler(type, handler)         │    │
│  │  ├── handleCommand(packet)                          │    │
│  │  ├── handleCommandAsync(packet)                     │    │
│  │  └── getProtocolStats(type)                         │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  ProtocolHandler (协议处理器 - SDK 7.2)             │      │
│  │  ├── getProtocolType()                             │      │
│  │  ├── handleCommand(packet)                         │      │
│  │  ├── validateCommand(packet)                       │      │
│  │  └── getStatus()                                   │      │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  CommandExecutor (命令执行引擎)                      │      │
│  │  ├── execute(packet)                               │      │
│  │  ├── validate(packet)                              │      │
│  │  ├── timeout(packet)                               │      │
│  │  └── retry(packet)                                 │      │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 服务端接口设计

```java
public interface CommandHandler extends ProtocolHandler {
    
    List<String> getSupportedCommandTypes();
    
    CommandPacket preProcess(CommandPacket packet);
    
    CommandResult postProcess(CommandPacket packet, CommandResult result);
    
    CommandResult onTimeout(CommandPacket packet);
    
    CommandResult onError(CommandPacket packet, Exception error);
}

public interface CommandExecutor {
    
    CommandResult execute(CommandPacket packet);
    
    CompletableFuture<CommandResult> executeAsync(CommandPacket packet);
    
    boolean validate(CommandPacket packet);
    
    ExecutorStatus getStatus();
}

public enum ExecutorStatus {
    IDLE,
    RUNNING,
    BUSY,
    ERROR
}
```

### 3.3 服务端能力清单

| 能力ID | 能力名称 | 服务端实现 | 说明 |
|--------|---------|-----------|------|
| **命令路由** |||
| route-register | 处理器注册 | `ProtocolHub.registerProtocolHandler()` | 注册处理器 |
| route-dispatch | 命令分发 | `ProtocolHub.handleCommand()` | 路由分发 |
| route-stats | 统计信息 | `ProtocolHub.getProtocolStats()` | 执行统计 |
| **命令处理** |||
| handle-validate | 命令验证 | `ProtocolHandler.validateCommand()` | 参数校验 |
| handle-execute | 命令执行 | `ProtocolHandler.handleCommand()` | 业务执行 |
| handle-timeout | 超时处理 | `CommandHandler.onTimeout()` | 超时回调 |
| handle-error | 异常处理 | `CommandHandler.onError()` | 异常处理 |

## 四、Level 3: 能力挂接层

### 4.1 能力挂接架构

```
┌─────────────────────────────────────────────────────────────┐
│  Capability Registration Layer                              │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  SDK 7.2 集成                                       │    │
│  │  ├── ProtocolHub ↔ CommandHub 适配                  │    │
│  │  ├── CommandPacket ↔ Command 适配                   │    │
│  │  └── ProtocolHandler ↔ CapabilityHandler 适配       │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  CapabilityHandlerRegistry (能力处理器注册表)       │      │
│  │  ├── registerCapability(capability, handler)       │      │
│  │  ├── getHandler(capabilityId)                      │      │
│  │  └── queryCapabilities(sceneId)                    │      │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  南北向命令统一映射                                  │      │
│  │  ├── NorthboundCommand ↔ SouthboundCommand         │      │
│  │  ├── CommandCapability ↔ ProtocolHandler           │      │
│  │  └── SceneCapability ↔ CommandHandler              │      │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 南北向命令统一设计

```java
public interface UnifiedCommand {
    
    String getCommandId();
    
    String getCommandType();
    
    String getProtocolType();
    
    CommandDirection getDirection();
    
    String getSourceId();
    
    String getTargetId();
    
    Map<String, Object> getParameters();
    
    int getPriority();
    
    long getTimeout();
    
    CommandStatus getStatus();
    
    long getCreateTime();
    
    Long getExecuteTime();
}

public enum CommandDirection {
    NORTHBOUND,
    SOUTHBOUND
}

public enum CommandStatus {
    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    TIMEOUT,
    CANCELLED
}
```

### 4.3 能力组件接口

```java
public interface CapabilityComponent {
    
    String getCapabilityId();
    
    String getCapabilityName();
    
    List<String> getSupportedCommands();
    
    CommandResult handleCommand(UnifiedCommand command);
    
    ValidationResult validateCommand(UnifiedCommand command);
    
    CapabilityStatus getStatus();
}

public class CapabilityHandlerAdapter implements ProtocolHandler {
    
    private final CapabilityComponent component;
    
    public CapabilityHandlerAdapter(CapabilityComponent component) {
        this.component = component;
    }
    
    @Override
    public String getProtocolType() {
        return component.getCapabilityId();
    }
    
    @Override
    public CommandResult handleCommand(CommandPacket packet) {
        UnifiedCommand command = adaptToUnifiedCommand(packet);
        return component.handleCommand(command);
    }
    
    @Override
    public boolean validateCommand(CommandPacket packet) {
        UnifiedCommand command = adaptToUnifiedCommand(packet);
        ValidationResult result = component.validateCommand(command);
        return result.isValid();
    }
    
    @Override
    public ProtocolStatus getStatus() {
        CapabilityStatus status = component.getStatus();
        switch (status) {
            case READY: return ProtocolStatus.READY;
            case BUSY: return ProtocolStatus.BUSY;
            case ERROR: return ProtocolStatus.ERROR;
            default: return ProtocolStatus.INITIALIZING;
        }
    }
}
```

## 五、南北向命令统一映射

### 5.1 命令流向

```
┌─────────────────────────────────────────────────────────────┐
│  北向命令流 (Northbound Command Flow)                        │
│                                                              │
│  外部系统 ──▶ RouteAgent ──▶ ProtocolHub ──▶ CapabilityHandler│
│                                                              │
│  命令类型:                                                   │
│  - MCP命令 (模型调用)                                        │
│  - ROUTE命令 (路由请求)                                      │
│  - END命令 (终端请求)                                        │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  南向命令流 (Southbound Command Flow)                        │
│                                                              │
│  CapabilityHandler ──▶ ProtocolHub ──▶ EndAgent ──▶ 外部设备 │
│                                                              │
│  命令类型:                                                   │
│  - IoT命令 (设备控制)                                        │
│  - 传感器命令 (数据采集)                                     │
│  - 执行器命令 (动作执行)                                     │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 命令类型映射

| 方向 | 协议类型 | 命令类型 | 说明 |
|------|---------|---------|------|
| **北向** ||||
| NORTH | MCP | INVOKE | 模型调用命令 |
| NORTH | MCP | REGISTER | 注册命令 |
| NORTH | ROUTE | DISCOVER | 发现命令 |
| NORTH | ROUTE | CONNECT | 连接命令 |
| NORTH | END | EXECUTE | 执行命令 |
| **南向** ||||
| SOUTH | IOT | CONTROL | 设备控制命令 |
| SOUTH | IOT | CONFIG | 设备配置命令 |
| SOUTH | SENSOR | READ | 传感器读取命令 |
| SOUTH | ACTUATOR | ACTION | 执行器动作命令 |

### 5.3 能力-处理器映射

```java
public class CapabilityHandlerMapping {
    
    private Map<String, String> capabilityToHandler = new HashMap<>();
    private Map<String, List<String>> handlerToCapabilities = new HashMap<>();
    
    public void register(String capabilityId, String handlerType) {
        capabilityToHandler.put(capabilityId, handlerType);
        handlerToCapabilities.computeIfAbsent(handlerType, k -> new ArrayList<>())
                             .add(capabilityId);
    }
    
    public String getHandlerType(String capabilityId) {
        return capabilityToHandler.get(capabilityId);
    }
    
    public List<String> getCapabilities(String handlerType) {
        return handlerToCapabilities.getOrDefault(handlerType, Collections.emptyList());
    }
}

// 默认映射配置
CapabilityHandlerMapping mapping = new CapabilityHandlerMapping();

// 北向命令映射
mapping.register("cmd-mcp-invoke", "MCP");
mapping.register("cmd-route-discover", "ROUTE");
mapping.register("cmd-end-execute", "END");

// 南向命令映射
mapping.register("cmd-iot-control", "IOT");
mapping.register("cmd-sensor-read", "SENSOR");
mapping.register("cmd-actuator-action", "ACTUATOR");
```

## 六、SDK 协作支持请求

### 6.1 需要SDK支持的功能

| 需求ID | 需求描述 | 优先级 | 说明 |
|--------|---------|--------|------|
| SDK-CMD-001 | CommandPacket 扩展方向字段 | P1 | 支持南北向标识 |
| SDK-CMD-002 | ProtocolHandler 批量命令支持 | P2 | 批量执行优化 |
| SDK-CMD-003 | 命令链式执行 | P2 | 命令编排支持 |
| SDK-CMD-004 | 命令回滚机制 | P3 | 事务支持 |

### 6.2 SDK 接口扩展建议

```java
public class CommandPacket {
    
    // 现有字段...
    
    // 建议新增
    private CommandDirection direction;
    private String parentCommandId;
    private List<String> childCommandIds;
    private boolean rollbackable;
}

public interface ProtocolHub {
    
    // 现有方法...
    
    // 建议新增
    CompletableFuture<BatchResult> handleBatch(List<CommandPacket> packets);
    CompletableFuture<CommandResult> handleChain(CommandPacket... packets);
    CompletableFuture<CommandResult> rollback(String commandId);
}
```

## 七、实施计划

### 7.1 阶段一：客户端层实现

| 任务 | 工作量 | 依赖 |
|------|--------|------|
| CommandClient 接口实现 | 1天 | SDK 7.2 |
| CommandBuilder 构建 | 0.5天 | 无 |
| 命令结果处理器 | 0.5天 | 无 |

### 7.2 阶段二：服务端层实现

| 任务 | 工作量 | 依赖 |
|------|--------|------|
| ProtocolHandler 实现 | 2天 | 阶段一 |
| CommandExecutor 实现 | 1天 | 阶段一 |
| 命令路由配置 | 1天 | 阶段一 |

### 7.3 阶段三：能力挂接实现

| 任务 | 工作量 | 依赖 |
|------|--------|------|
| CapabilityHandlerAdapter | 1天 | 阶段二 |
| 南北向映射配置 | 1天 | 阶段二 |
| 能力注册表 | 1天 | 阶段二 |

---

**文档版本**: v1.0  
**创建日期**: 2026-02-19  
**状态**: 设计完成，待SDK协作确认

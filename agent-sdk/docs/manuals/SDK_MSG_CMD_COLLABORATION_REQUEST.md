# SDK 7.2 消息与命令场景协作支持请求

## 一、协作背景

### 1.1 项目信息

| 项目 | 说明 |
|------|------|
| 发起方 | ooder-common / northbound-services |
| SDK版本 | agent-sdk 0.7.2 |
| 协作目标 | 消息与命令场景集成 |
| 优先级 | P1 |

### 1.2 协作范围

本次协作请求涉及消息和命令场景的三级架构集成：

```
┌─────────────────────────────────────────────────────────────┐
│  Level 1: 客户端层                                          │
│  - MsgClient / CommandClient 接口适配                       │
│  - JMQClient MQTT集成                                       │
│  - 本地缓存管理                                              │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│  Level 2: 服务端层                                          │
│  - 消息存储服务 / ProtocolHub 命令路由                       │
│  - MQTT Broker 集成                                         │
│  - 场景配置管理                                              │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│  Level 3: 能力挂接层                                        │
│  - CoreMessage / CommandPacket 适配                         │
│  - CoreTransport / ProtocolHandler 适配                     │
│  - 能力注册与映射                                            │
└─────────────────────────────────────────────────────────────┘
```

## 二、SDK 现有能力分析

### 2.1 消息相关已有能力

| SDK 接口 | 说明 | 消息场景对应 |
|---------|------|-------------|
| `CoreMessage` | 核心消息接口 | 统一消息抽象 ✅ |
| `CoreTransport` | 传输层接口 | 传输协议适配 ✅ |
| `MessageType` | 消息类型枚举 | 6种类型支持 ✅ |
| `TransportMessage` | 传输消息实体 | 底层传输载体 ✅ |
| `TransportResult` | 传输结果 | 发送结果 ✅ |

### 2.2 命令相关已有能力

| SDK 接口 | 说明 | 命令场景对应 |
|---------|------|-------------|
| `ProtocolHub` | 协议中心 | 命令路由中心 ✅ |
| `CommandPacket` | 命令包 | 命令载体 ✅ |
| `CommandResult` | 命令结果 | 执行结果 ✅ |
| `ProtocolHandler` | 协议处理器 | 命令处理器 ✅ |

### 2.3 能力缺口

| 缺口ID | 描述 | 影响范围 | 优先级 |
|--------|------|---------|--------|
| GAP-001 | CoreMessage 缺少消息标题字段 | 点对点消息 | P1 |
| GAP-002 | CoreMessage 缺少消息状态 | 消息管理 | P1 |
| GAP-003 | 缺少消息确认机制 | 可靠传输 | P1 |
| GAP-004 | CommandPacket 缺少方向字段 | 南北向命令 | P1 |
| GAP-005 | MessageType 缺少告警类型 | IoT场景 | P2 |
| GAP-006 | 缺少批量发送支持 | 群发消息 | P2 |

## 三、协作需求清单

### 3.1 P1 优先级需求

#### 需求 SDK-MSG-001: CoreMessage 扩展字段

**描述**: 扩展 CoreMessage 接口，支持消息标题、状态等字段

**建议扩展**:
```java
public interface CoreMessage {
    // 现有方法保持不变...
    
    // 新增方法
    default String getTitle() { return null; }
    default MsgStatus getStatus() { return MsgStatus.NORMAL; }
    default Integer getRetryCount() { return 0; }
    default String getGroupId() { return null; }
    default Map<String, Object> getMetadata() { return Collections.emptyMap(); }
}
```

---

#### 需求 SDK-MSG-002: 消息确认机制

**描述**: 增加消息送达确认机制

**建议接口**:
```java
public interface CoreTransport {
    // 现有方法...
    
    // 新增方法
    void acknowledge(String messageId);
    void setAckListener(AckListener listener);
}

public interface AckListener {
    void onAckReceived(String messageId, AckStatus status);
}

public enum AckStatus {
    DELIVERED,
    READ,
    FAILED
}
```

---

#### 需求 SDK-CMD-001: CommandPacket 扩展方向字段

**描述**: 扩展 CommandPacket，支持南北向标识

**建议扩展**:
```java
public class CommandPacket {
    // 现有字段...
    
    // 建议新增
    private CommandDirection direction;
    private String parentCommandId;
    private List<String> childCommandIds;
    private boolean rollbackable;
}

public enum CommandDirection {
    NORTHBOUND,
    SOUTHBOUND
}
```

---

### 3.2 P2 优先级需求

#### 需求 SDK-MSG-003: MessageType 扩展

**建议扩展**:
```java
public enum MessageType {
    // 现有类型...
    
    // 新增类型
    ALARM,          // 告警消息
    SENSOR_DATA,    // 传感器数据
    CONTROL,        // 控制命令
    HEARTBEAT,      // 心跳消息
    SYNC            // 同步消息
}
```

---

#### 需求 SDK-MSG-004: 批量消息发送

**建议接口**:
```java
public interface CoreTransport {
    // 现有方法...
    
    // 新增方法
    CompletableFuture<BatchResult> transmitBatch(List<TransportMessage> messages);
}

public class BatchResult {
    private int totalCount;
    private int successCount;
    private int failedCount;
    private List<String> failedMessageIds;
}
```

---

#### 需求 SDK-CMD-002: 批量命令支持

**建议接口**:
```java
public interface ProtocolHub {
    // 现有方法...
    
    // 建议新增
    CompletableFuture<BatchResult> handleBatch(List<CommandPacket> packets);
    CompletableFuture<CommandResult> handleChain(CommandPacket... packets);
    CompletableFuture<CommandResult> rollback(String commandId);
}
```

---

## 四、适配器实现

### 4.1 CoreMessage 适配器

```java
public class MsgCoreMessageAdapter implements CoreMessage {
    
    private final Msg msg;
    
    public MsgCoreMessageAdapter(Msg msg) {
        this.msg = msg;
    }
    
    @Override
    public String getMessageId() {
        return msg.getId();
    }
    
    @Override
    public String getSource() {
        return msg.getFrom();
    }
    
    @Override
    public String getTarget() {
        return msg.getReceiver();
    }
    
    @Override
    public MessageType getType() {
        if (msg instanceof TopicMsg) {
            return MessageType.NOTIFICATION;
        } else if (msg instanceof CommandMsg) {
            return MessageType.COMMAND;
        } else if (msg instanceof SensorMsg) {
            return MessageType.EVENT;
        }
        return MessageType.QUERY;
    }
    
    @Override
    public Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", msg.getTitle());
        payload.put("body", msg.getBody());
        return payload;
    }
    
    @Override
    public long getTimestamp() {
        return msg.getArrivedTime() != null ? msg.getArrivedTime() : System.currentTimeMillis();
    }
    
    @Override
    public int getPriority() {
        return msg.getStatus() == MsgStatus.URGENT ? 10 : 5;
    }
    
    @Override
    public long getTtl() {
        return 86400000L;
    }
}
```

### 4.2 CapabilityHandlerAdapter

```java
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
}
```

## 五、协作时间表

| 阶段 | 内容 | 时间 | 负责方 |
|------|------|------|--------|
| 需求确认 | 确认协作需求清单 | Day 1 | 双方 |
| SDK评估 | SDK团队评估可行性 | Day 2-3 | SDK团队 |
| 接口设计 | 确定最终接口设计 | Day 4 | 双方 |
| SDK开发 | SDK扩展开发 | Day 5-10 | SDK团队 |
| 适配器开发 | 适配器实现 | Day 8-12 | ooder-common |
| 集成测试 | 集成测试 | Day 13-14 | 双方 |
| 发布 | 联合发布 | Day 15 | 双方 |

## 六、验收标准

### 6.1 功能验收

- [ ] CoreMessage 扩展字段正常工作
- [ ] 消息确认机制正常工作
- [ ] CommandPacket 方向字段正确识别
- [ ] MessageType 扩展类型正确识别
- [ ] 批量发送功能正常
- [ ] 能力组件挂接正常

### 6.2 兼容性验收

- [ ] 现有 Msg 接口兼容
- [ ] 现有 JMQClient 接口兼容
- [ ] 向后兼容 SDK 7.2

### 6.3 性能验收

- [ ] 单条消息发送延迟 < 100ms
- [ ] 批量消息发送吞吐量 > 1000条/秒
- [ ] 命令执行延迟 < 200ms

---

**文档版本**: v1.0  
**创建日期**: 2026-02-19  
**状态**: 待SDK团队确认

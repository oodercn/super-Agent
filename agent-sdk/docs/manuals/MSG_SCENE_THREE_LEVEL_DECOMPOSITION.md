# 消息场景三级分解方案

## 一、方案概述

### 1.1 三级架构定义

```
┌─────────────────────────────────────────────────────────────┐
│  Level 1: 客户端层 (Client Side)                             │
│  - 消息客户端代理                                            │
│  - 本地缓存管理                                              │
│  - 传输协议适配                                              │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│  Level 2: 服务端层 (Server Side)                             │
│  - 消息存储服务                                              │
│  - MQTT Broker 集成                                          │
│  - 场景配置管理                                              │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│  Level 3: 能力挂接层 (Capability Registration)               │
│  - 能力声明与注册                                            │
│  - 场景能力映射                                              │
│  - SDK 7.2 集成                                              │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 SDK 7.2 消息支持

SDK 7.2 提供了核心消息传输能力：

| SDK 接口 | 说明 | 对应消息场景 |
|---------|------|-------------|
| `CoreMessage` | 核心消息接口 | 统一消息抽象 |
| `CoreTransport` | 传输层接口 | 传输协议适配 |
| `MessageType` | 消息类型枚举 | 消息类型定义 |
| `TransportMessage` | 传输消息实体 | 底层传输载体 |

## 二、Level 1: 客户端层

### 2.1 客户端架构

```
┌─────────────────────────────────────────────────────────────┐
│  Client Side (ooder-msg-web)                                │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  MsgFactory (消息工厂)                               │    │
│  │  - getClient(personId, clazz)                       │    │
│  │  - 按场景配置创建客户端                              │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  MsgClient<V extends Msg> (消息客户端接口)          │      │
│  │  ├── getMsgById()                                   │      │
│  │  ├── getAllSendMsg()                                │      │
│  │  ├── getAllReceiveMsg()                             │      │
│  │  ├── creatMsg()                                     │      │
│  │  ├── updateMsg()                                    │      │
│  │  ├── sendMassMsg()                                  │      │
│  │  └── deleteMsg()                                    │      │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  JMQClient (MQTT客户端接口)                         │      │
│  │  ├── subscriptTopic()                               │      │
│  │  ├── unSubscriptTopic()                             │      │
│  │  ├── createTopic()                                  │      │
│  │  ├── sendMsg()                                      │      │
│  │  ├── broadcast()                                    │      │
│  │  └── sendCommand()                                  │      │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  CtMsgCacheManager (本地缓存管理)                   │      │
│  │  ├── 消息缓存                                       │      │
│  │  ├── 索引管理                                       │      │
│  │  └── 离线消息存储                                   │      │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 客户端场景配置

```yaml
# msg-client-scene.yaml
client:
  sceneId: msg-client-default
  configName: 默认消息客户端
  
  msgClass: net.ooder.msg.ct.CtMsg
  
  cache:
    enabled: true
    maxSize: 10485760
    expireTime: 86400000
    
  connection:
    timeout: 5000
    retryCount: 3
    retryInterval: 1000
    
  capabilities:
    p2p-message:
      enabled: true
    topic-subscribe:
      enabled: true
    offline-message:
      enabled: true
```

### 2.3 客户端能力清单

| 能力ID | 能力名称 | 接口方法 | 说明 |
|--------|---------|---------|------|
| **消息查询** |||
| msg-get-by-id | 根据ID获取消息 | `getMsgById(msgId)` | 获取单条消息 |
| msg-get-send | 获取已发送消息 | `getAllSendMsg()` | 获取发送列表 |
| msg-get-receive | 获取已接收消息 | `getAllReceiveMsg()` | 获取接收列表 |
| msg-query | 条件查询消息 | `getMsgList(condition)` | 条件查询 |
| **消息创建** |||
| msg-create | 创建消息 | `creatMsg()` | 创建新消息 |
| msg-create-person | 创建点对点消息 | `creatMsg2Person(toPersonId)` | 指定接收人 |
| msg-clone | 克隆消息 | `cloneMsg(msg)` | 复制消息 |
| **消息发送** |||
| msg-update | 更新/发送消息 | `updateMsg(msg)` | 发送消息 |
| msg-mass | 群发消息 | `sendMassMsg(msg, personIds)` | 批量发送 |
| **消息删除** |||
| msg-delete | 删除消息 | `deleteMsg(msgId)` | 删除消息 |
| **Topic操作** |||
| topic-create | 创建Topic | `createTopic(topic, retained, qos)` | 创建主题 |
| topic-subscribe | 订阅Topic | `subscriptTopic(topic)` | 订阅主题 |
| topic-unsubscribe | 取消订阅 | `unSubscriptTopic(topic)` | 取消订阅 |
| topic-broadcast | 广播消息 | `broadcast(topic, msg)` | 广播 |

## 三、Level 2: 服务端层

### 3.1 服务端架构

```
┌─────────────────────────────────────────────────────────────┐
│  Server Side (ooder-server / skills)                        │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  MsgServiceFactory (消息服务工厂)                    │    │
│  │  - getMsgService(configCode)                        │    │
│  │  - registerProvider(type, provider)                 │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  MsgStorageProvider (存储Provider接口)              │      │
│  │  ├── MemoryStorageProvider    (内存存储)            │      │
│  │  ├── DatabaseStorageProvider  (数据库存储)          │      │
│  │  └── LuceneStorageProvider    (索引存储)            │      │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  MQTT Broker 集成                                   │      │
│  │  ├── Topic 管理                                     │      │
│  │  ├── 订阅管理                                       │      │
│  │  ├── 消息路由                                       │      │
│  │  └── 持久化                                         │      │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 服务端场景配置

```yaml
# msg-server-scene.yaml
server:
  sceneId: msg-server-default
  configName: 默认消息服务端
  
  storage:
    type: lucene
    
    lucene:
      indexPath: ./data/msg-index
      analyzer: standard
      
    database:
      driver: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/msg_db
      username: root
      password: ${DB_PASSWORD}
  
  mqtt:
    broker: ${MQTT_BROKER:tcp://localhost:1883}
    port: ${MQTT_PORT:1883}
    username: ${MQTT_USERNAME:}
    password: ${MQTT_PASSWORD:}
    
  offline:
    enabled: true
    expireTime: 604800000
    maxCount: 1000
```

### 3.3 服务端能力清单

| 能力ID | 能力名称 | 服务端实现 | 说明 |
|--------|---------|-----------|------|
| **消息存储** |||
| store-memory | 内存存储 | `MemoryStorageProvider` | 临时存储 |
| store-database | 数据库存储 | `DatabaseStorageProvider` | 持久化存储 |
| store-lucene | 索引存储 | `LuceneStorageProvider` | 搜索存储 |
| **消息路由** |||
| route-p2p | 点对点路由 | `P2PService` | 私信路由 |
| route-topic | Topic路由 | `TopicService` | 主题路由 |
| route-broadcast | 广播路由 | `BroadcastService` | 广播路由 |

## 四、Level 3: 能力挂接层

### 4.1 能力挂接架构

```
┌─────────────────────────────────────────────────────────────┐
│  Capability Registration Layer                              │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  SDK 7.2 集成                                       │    │
│  │  ├── CoreMessage ↔ Msg 适配                         │    │
│  │  ├── CoreTransport ↔ MQTT/WebSocket 适配            │    │
│  │  └── MessageType ↔ 消息类型映射                      │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                               │
│  ┌───────────────────────────┴───────────────────────┐      │
│  │  CapabilityRegistry (能力注册表)                    │      │
│  │  ├── registerCapability(capability)                │      │
│  │  ├── getCapability(capabilityId)                   │      │
│  │  └── queryCapabilities(sceneId)                    │      │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 SDK 7.2 适配

```java
public class CoreMessageAdapter implements CoreMessage {
    
    private final Msg msg;
    
    public CoreMessageAdapter(Msg msg) {
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
        payload.put("eventTime", msg.getEventTime());
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
    
    @Override
    public String getCorrelationId() {
        return msg.getId();
    }
    
    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("msg-type", msg.getType());
        headers.put("system-code", msg.getSystemCode());
        return headers;
    }
}
```

### 4.3 消息类型映射

| Msg 类型 | SDK MessageType | 说明 |
|---------|-----------------|------|
| CtMsg | QUERY | 普通消息 |
| TopicMsg | NOTIFICATION | Topic通知 |
| CommandMsg | COMMAND | 命令消息 |
| SensorMsg | EVENT | 传感器事件 |
| AlarmMsg | NOTIFICATION | 告警通知 |
| ResponseMsg | RESPONSE | 响应消息 |

## 五、场景能力映射

### 5.1 场景-能力-实现映射表

| 场景 | 能力 | 客户端实现 | 服务端实现 | SDK映射 |
|------|------|-----------|-----------|---------|
| **点对点消息** |||||
| msg-p2p | p2p-message | `CtMsgClient.creatMsg2Person()` | `MsgWebServiceAPI.send()` | `MessageType.QUERY` |
| msg-p2p | offline-message | `CtMsgClient.getAllReceiveMsg()` | `OfflineMsgService` | `MessageType.NOTIFICATION` |
| msg-p2p | message-query | `CtMsgClient.getMsgList()` | `MsgIndexService` | `MessageType.QUERY` |
| **Topic消息** |||||
| msg-topic | topic-subscribe | `JMQClient.subscriptTopic()` | `TopicService` | `MessageType.NOTIFICATION` |
| msg-topic | topic-create | `JMQClient.createTopic()` | `TopicService` | `MessageType.COMMAND` |
| msg-topic | broadcast | `JMQClient.broadcast()` | `TopicService` | `MessageType.NOTIFICATION` |

### 5.2 能力降级配置

```yaml
capabilityFallback:
  p2p-message:
    primary: mqtt
    fallback: esb
    condition: mqtt_unavailable
    
  topic-subscribe:
    primary: mqtt
    fallback: websocket
    condition: mqtt_unavailable
    
  offline-message:
    primary: database
    fallback: memory
    condition: database_error
```

## 六、SDK 协作支持请求

### 6.1 需要SDK支持的功能

| 需求ID | 需求描述 | 优先级 | 说明 |
|--------|---------|--------|------|
| SDK-MSG-001 | CoreMessage 扩展字段 | P1 | 支持消息标题、状态等扩展字段 |
| SDK-MSG-002 | TransportMessage 消息体序列化 | P1 | 支持 JSON 序列化 |
| SDK-MSG-003 | 消息优先级队列 | P2 | 支持消息优先级排序 |
| SDK-MSG-004 | 消息 TTL 过期处理 | P2 | 支持消息自动过期 |
| SDK-MSG-005 | 消息确认机制 | P1 | 支持消息送达确认 |
| SDK-MSG-006 | 批量消息发送 | P2 | 支持批量发送优化 |

### 6.2 SDK 接口扩展建议

```java
public interface CoreMessage {
    
    // 现有方法...
    
    // 建议新增
    String getTitle();
    MsgStatus getStatus();
    Integer getRetryCount();
    String getGroupId();
    Map<String, Object> getMetadata();
}

public interface CoreTransport {
    
    // 现有方法...
    
    // 建议新增
    CompletableFuture<TransportResult> transmitBatch(List<TransportMessage> messages);
    void acknowledge(String messageId);
    void setQos(int qos);
    int getQos();
}
```

### 6.3 SDK 消息类型扩展建议

```java
public enum MessageType {
    // 现有类型
    COMMAND,
    RESPONSE,
    EVENT,
    NOTIFICATION,
    QUERY,
    ACKNOWLEDGMENT,
    
    // 建议新增
    ALARM,
    SENSOR_DATA,
    CONTROL,
    HEARTBEAT,
    SYNC
}
```

## 七、实施计划

### 7.1 阶段一：客户端层实现

| 任务 | 工作量 | 依赖 |
|------|--------|------|
| MsgClient 接口完善 | 1天 | 无 |
| JMQClient MQTT集成 | 2天 | 无 |
| 本地缓存管理 | 1天 | 无 |
| 场景配置加载 | 1天 | SDK 7.2 |

### 7.2 阶段二：服务端层实现

| 任务 | 工作量 | 依赖 |
|------|--------|------|
| MsgServiceFactory | 1天 | 阶段一 |
| 存储Provider实现 | 2天 | 阶段一 |
| MQTT Broker集成 | 2天 | 阶段一 |
| 离线消息服务 | 1天 | 阶段一 |

### 7.3 阶段三：能力挂接实现

| 任务 | 工作量 | 依赖 |
|------|--------|------|
| SDK 7.2 适配器 | 2天 | 阶段二 |
| 能力注册表 | 1天 | 阶段二 |
| 场景能力映射 | 1天 | 阶段二 |
| 降级机制 | 1天 | 阶段二 |

---

**文档版本**: v1.0  
**创建日期**: 2026-02-19  
**状态**: 设计完成，待SDK协作确认

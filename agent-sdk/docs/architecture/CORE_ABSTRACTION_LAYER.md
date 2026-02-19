# Ooder Agent SDK 核心抽象层手册

## 1. 概述

### 1.1 设计目标

核心抽象层是 Ooder Agent SDK 0.7.2 版本的基础架构层，提供统一的基础接口和数据模型，确保南北向服务的一致性和可扩展性。

### 1.2 核心原则

| 原则 | 描述 |
|------|------|
| **接口统一** | 提供统一的接口定义，屏蔽底层实现差异 |
| **扩展性强** | 支持自定义实现，易于扩展新功能 |
| **类型安全** | 使用强类型定义，确保编译时类型检查 |
| **异步优先** | 所有接口默认支持异步操作 |

### 1.3 模块组成

```
核心抽象层（Core Abstraction Layer）
├── 核心网络抽象（Core Network Abstraction）
│   ├── 连接管理（Connection）
│   ├── 协议接口（Protocol）
│   └── 传输抽象（Transport）
├── 核心安全抽象（Core Security Abstraction）
│   ├── 身份模型（Identity）
│   ├── 权限接口（Permission）
│   └── 加密接口（Encryption）
└── 核心协作抽象（Core Collaboration Abstraction）
    ├── 消息模型（Message）
    ├── 状态模型（State）
    └── 事件模型（Event）
```

## 2. 核心网络抽象

### 2.1 连接接口

#### 2.1.1 接口定义

```java
package net.ooder.sdk.core.network;

import java.util.concurrent.CompletableFuture;

public interface CoreConnection {
    
    String getConnectionId();
    
    ConnectionType getType();
    
    ConnectionState getState();
    
    CompletableFuture<Void> connect();
    
    CompletableFuture<Void> disconnect();
    
    CompletableFuture<byte[]> send(byte[] data);
    
    CompletableFuture<byte[]> receive();
    
    void setConnectionListener(ConnectionListener listener);
    
    void setTimeout(long timeoutMs);
    
    long getTimeout();
}
```

#### 2.1.2 连接类型

```java
package net.ooder.sdk.core.network;

public enum ConnectionType {
    HTTP,
    WEBSOCKET,
    TCP,
    UDP,
    P2P,
    SSE
}
```

#### 2.1.3 连接状态

```java
package net.ooder.sdk.core.network;

public enum ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
    ERROR,
    TIMEOUT
}
```

#### 2.1.4 连接监听器

```java
package net.ooder.sdk.core.network;

public interface ConnectionListener {
    
    void onConnected(CoreConnection connection);
    
    void onDisconnected(CoreConnection connection);
    
    void onError(CoreConnection connection, Throwable error);
    
    void onStateChanged(CoreConnection connection, ConnectionState oldState, ConnectionState newState);
}
```

#### 2.1.5 使用示例

```java
CoreConnection connection = new HttpConnection("http://localhost:8080/api");
connection.setTimeout(30000);

connection.setConnectionListener(new ConnectionListener() {
    @Override
    public void onConnected(CoreConnection conn) {
        System.out.println("Connected: " + conn.getConnectionId());
    }
    
    @Override
    public void onDisconnected(CoreConnection conn) {
        System.out.println("Disconnected: " + conn.getConnectionId());
    }
    
    @Override
    public void onError(CoreConnection conn, Throwable error) {
        System.err.println("Error: " + error.getMessage());
    }
    
    @Override
    public void onStateChanged(CoreConnection conn, ConnectionState oldState, ConnectionState newState) {
        System.out.println("State changed: " + oldState + " -> " + newState);
    }
});

CompletableFuture<Void> connectFuture = connection.connect();
connectFuture.thenRun(() -> {
    System.out.println("Connection established");
});
```

### 2.2 协议接口

#### 2.2.1 接口定义

```java
package net.ooder.sdk.core.protocol;

public interface CoreProtocol {
    
    String getProtocolType();
    
    String getProtocolVersion();
    
    byte[] encode(Object message) throws ProtocolException;
    
    <T> T decode(byte[] data, Class<T> type) throws ProtocolException;
    
    boolean validate(byte[] data);
    
    ProtocolMetadata getMetadata();
}
```

#### 2.2.2 协议元数据

```java
package net.ooder.sdk.core.protocol;

import java.util.Map;

public class ProtocolMetadata {
    
    private String protocolType;
    private String version;
    private String encoding;
    private Map<String, String> properties;
    
    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getEncoding() { return encoding; }
    public void setEncoding(String encoding) { this.encoding = encoding; }
    
    public Map<String, String> getProperties() { return properties; }
    public void setProperties(Map<String, String> properties) { this.properties = properties; }
}
```

#### 2.2.3 协议异常

```java
package net.ooder.sdk.core.protocol;

public class ProtocolException extends Exception {
    
    private String protocolType;
    private String operation;
    
    public ProtocolException(String protocolType, String operation, String message) {
        super(message);
        this.protocolType = protocolType;
        this.operation = operation;
    }
    
    public ProtocolException(String protocolType, String operation, Throwable cause) {
        super(cause);
        this.protocolType = protocolType;
        this.operation = operation;
    }
    
    public String getProtocolType() { return protocolType; }
    public String getOperation() { return operation; }
}
```

#### 2.2.4 使用示例

```java
CoreProtocol jsonProtocol = new JsonProtocol();

Map<String, Object> message = new HashMap<>();
message.put("type", "command");
message.put("action", "execute");
message.put("params", Arrays.asList("param1", "param2"));

byte[] encoded = jsonProtocol.encode(message);

boolean valid = jsonProtocol.validate(encoded);
if (valid) {
    Map<String, Object> decoded = jsonProtocol.decode(encoded, Map.class);
    System.out.println("Decoded: " + decoded);
}
```

### 2.3 传输接口

#### 2.3.1 接口定义

```java
package net.ooder.sdk.core.transport;

import java.util.concurrent.CompletableFuture;

public interface CoreTransport {
    
    String getTransportId();
    
    TransportType getTransportType();
    
    CompletableFuture<TransportResult> transmit(TransportMessage message);
    
    void registerHandler(TransportHandler handler);
    
    void unregisterHandler(TransportHandler handler);
    
    void start();
    
    void stop();
    
    boolean isRunning();
}
```

#### 2.3.2 传输消息

```java
package net.ooder.sdk.core.transport;

import java.util.Map;

public class TransportMessage {
    
    private String messageId;
    private String source;
    private String target;
    private byte[] payload;
    private Map<String, String> headers;
    private long timestamp;
    private int priority;
    private long ttl;
    
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public byte[] getPayload() { return payload; }
    public void setPayload(byte[] payload) { this.payload = payload; }
    
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public long getTtl() { return ttl; }
    public void setTtl(long ttl) { this.ttl = ttl; }
}
```

#### 2.3.3 传输结果

```java
package net.ooder.sdk.core.transport;

public class TransportResult {
    
    private String messageId;
    private boolean success;
    private String errorCode;
    private String errorMessage;
    private long processingTime;
    private byte[] response;
    
    public static TransportResult success(String messageId) {
        TransportResult result = new TransportResult();
        result.setMessageId(messageId);
        result.setSuccess(true);
        return result;
    }
    
    public static TransportResult failure(String messageId, String errorCode, String errorMessage) {
        TransportResult result = new TransportResult();
        result.setMessageId(messageId);
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        return result;
    }
    
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public long getProcessingTime() { return processingTime; }
    public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }
    
    public byte[] getResponse() { return response; }
    public void setResponse(byte[] response) { this.response = response; }
}
```

#### 2.3.4 传输处理器

```java
package net.ooder.sdk.core.transport;

public interface TransportHandler {
    
    void onMessage(TransportMessage message);
    
    void onResult(TransportResult result);
    
    void onError(TransportError error);
}
```

## 3. 核心安全抽象

### 3.1 身份接口

#### 3.1.1 接口定义

```java
package net.ooder.sdk.core.security;

import java.util.Map;

public interface CoreIdentity {
    
    String getIdentityId();
    
    String getName();
    
    IdentityType getType();
    
    Map<String, Object> getAttributes();
    
    void setAttribute(String key, Object value);
    
    Object getAttribute(String key);
    
    boolean hasAttribute(String key);
}
```

#### 3.1.2 身份类型

```java
package net.ooder.sdk.core.security;

public enum IdentityType {
    USER,
    AGENT,
    ORGANIZATION,
    DOMAIN,
    SERVICE,
    DEVICE
}
```

#### 3.1.3 身份上下文

```java
package net.ooder.sdk.core.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdentityContext {
    
    private static final ThreadLocal<CoreIdentity> currentIdentity = new ThreadLocal<>();
    
    public static void setCurrentIdentity(CoreIdentity identity) {
        currentIdentity.set(identity);
    }
    
    public static CoreIdentity getCurrentIdentity() {
        return currentIdentity.get();
    }
    
    public static void clear() {
        currentIdentity.remove();
    }
    
    public static boolean hasIdentity() {
        return currentIdentity.get() != null;
    }
}
```

#### 3.1.4 使用示例

```java
CoreIdentity user = new UserIdentity();
user.setIdentityId("user-001");
user.setName("张三");
user.setType(IdentityType.USER);
user.setAttribute("email", "zhangsan@example.com");
user.setAttribute("department", "研发部");

IdentityContext.setCurrentIdentity(user);

CoreIdentity current = IdentityContext.getCurrentIdentity();
System.out.println("Current user: " + current.getName());
```

### 3.2 权限接口

#### 3.2.1 接口定义

```java
package net.ooder.sdk.core.security;

import java.util.List;

public interface CorePermission {
    
    String getPermissionId();
    
    String getResource();
    
    String getAction();
    
    boolean check(CoreIdentity identity);
    
    List<String> getConditions();
    
    void addCondition(String condition);
    
    void removeCondition(String condition);
}
```

#### 3.2.2 权限检查器

```java
package net.ooder.sdk.core.security;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PermissionChecker {
    
    CompletableFuture<Boolean> checkPermission(CoreIdentity identity, String resource, String action);
    
    CompletableFuture<List<CorePermission>> getPermissions(CoreIdentity identity);
    
    CompletableFuture<Void> grantPermission(CoreIdentity identity, CorePermission permission);
    
    CompletableFuture<Void> revokePermission(CoreIdentity identity, String permissionId);
}
```

#### 3.2.3 权限结果

```java
package net.ooder.sdk.core.security;

public class PermissionResult {
    
    private boolean granted;
    private String permissionId;
    private String resource;
    private String action;
    private String reason;
    
    public static PermissionResult granted(String permissionId, String resource, String action) {
        PermissionResult result = new PermissionResult();
        result.setGranted(true);
        result.setPermissionId(permissionId);
        result.setResource(resource);
        result.setAction(action);
        return result;
    }
    
    public static PermissionResult denied(String permissionId, String resource, String action, String reason) {
        PermissionResult result = new PermissionResult();
        result.setGranted(false);
        result.setPermissionId(permissionId);
        result.setResource(resource);
        result.setAction(action);
        result.setReason(reason);
        return result;
    }
    
    public boolean isGranted() { return granted; }
    public void setGranted(boolean granted) { this.granted = granted; }
    
    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }
    
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
```

### 3.3 加密接口

#### 3.3.1 接口定义

```java
package net.ooder.sdk.core.security;

import java.util.concurrent.CompletableFuture;

public interface CoreEncryption {
    
    String getAlgorithm();
    
    byte[] encrypt(byte[] data, EncryptionContext context) throws EncryptionException;
    
    byte[] decrypt(byte[] encryptedData, EncryptionContext context) throws EncryptionException;
    
    String generateKey();
    
    boolean verifySignature(byte[] data, byte[] signature, EncryptionContext context);
    
    byte[] sign(byte[] data, EncryptionContext context) throws EncryptionException;
    
    CompletableFuture<byte[]> encryptAsync(byte[] data, EncryptionContext context);
    
    CompletableFuture<byte[]> decryptAsync(byte[] encryptedData, EncryptionContext context);
}
```

#### 3.3.2 加密上下文

```java
package net.ooder.sdk.core.security;

import java.util.Map;

public class EncryptionContext {
    
    private String algorithm;
    private String keyId;
    private String key;
    private Map<String, Object> parameters;
    
    public static EncryptionContext of(String algorithm, String key) {
        EncryptionContext context = new EncryptionContext();
        context.setAlgorithm(algorithm);
        context.setKey(key);
        return context;
    }
    
    public static EncryptionContext aes256(String key) {
        return of("AES-256", key);
    }
    
    public static EncryptionContext rsa(String keyId) {
        EncryptionContext context = new EncryptionContext();
        context.setAlgorithm("RSA");
        context.setKeyId(keyId);
        return context;
    }
    
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
}
```

#### 3.3.3 加密异常

```java
package net.ooder.sdk.core.security;

public class EncryptionException extends Exception {
    
    private String algorithm;
    private String operation;
    
    public EncryptionException(String algorithm, String operation, String message) {
        super(message);
        this.algorithm = algorithm;
        this.operation = operation;
    }
    
    public EncryptionException(String algorithm, String operation, Throwable cause) {
        super(cause);
        this.algorithm = algorithm;
        this.operation = operation;
    }
    
    public String getAlgorithm() { return algorithm; }
    public String getOperation() { return operation; }
}
```

## 4. 核心协作抽象

### 4.1 消息接口

#### 4.1.1 接口定义

```java
package net.ooder.sdk.core.collaboration;

import java.util.Map;

public interface CoreMessage {
    
    String getMessageId();
    
    String getSource();
    
    String getTarget();
    
    MessageType getType();
    
    Map<String, Object> getPayload();
    
    long getTimestamp();
    
    int getPriority();
    
    long getTtl();
    
    String getCorrelationId();
    
    Map<String, String> getHeaders();
}
```

#### 4.1.2 消息类型

```java
package net.ooder.sdk.core.collaboration;

public enum MessageType {
    COMMAND,
    RESPONSE,
    EVENT,
    NOTIFICATION,
    QUERY,
    ACKNOWLEDGMENT
}
```

#### 4.1.3 消息构建器

```java
package net.ooder.sdk.core.collaboration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageBuilder {
    
    private String messageId;
    private String source;
    private String target;
    private MessageType type;
    private Map<String, Object> payload = new HashMap<>();
    private long timestamp = System.currentTimeMillis();
    private int priority = 5;
    private long ttl = 30000;
    private String correlationId;
    private Map<String, String> headers = new HashMap<>();
    
    public static MessageBuilder create() {
        return new MessageBuilder();
    }
    
    public MessageBuilder messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }
    
    public MessageBuilder source(String source) {
        this.source = source;
        return this;
    }
    
    public MessageBuilder target(String target) {
        this.target = target;
        return this;
    }
    
    public MessageBuilder type(MessageType type) {
        this.type = type;
        return this;
    }
    
    public MessageBuilder payload(String key, Object value) {
        this.payload.put(key, value);
        return this;
    }
    
    public MessageBuilder payload(Map<String, Object> payload) {
        this.payload.putAll(payload);
        return this;
    }
    
    public MessageBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }
    
    public MessageBuilder ttl(long ttl) {
        this.ttl = ttl;
        return this;
    }
    
    public MessageBuilder correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }
    
    public MessageBuilder header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
    
    public CoreMessage build() {
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        return new CoreMessageImpl(this);
    }
}
```

### 4.2 状态接口

#### 4.2.1 接口定义

```java
package net.ooder.sdk.core.collaboration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CoreState {
    
    String getStateId();
    
    StateType getType();
    
    Map<String, Object> getData();
    
    CompletableFuture<Void> update(Map<String, Object> data);
    
    CompletableFuture<Void> sync();
    
    CompletableFuture<Void> reset();
    
    long getVersion();
    
    long getLastModified();
    
    void setStateListener(StateListener listener);
}
```

#### 4.2.2 状态类型

```java
package net.ooder.sdk.core.collaboration;

public enum StateType {
    AGENT_STATE,
    NETWORK_STATE,
    SCENE_STATE,
    USER_STATE,
    TASK_STATE,
    SESSION_STATE
}
```

#### 4.2.3 状态监听器

```java
package net.ooder.sdk.core.collaboration;

public interface StateListener {
    
    void onStateChanged(String stateId, Map<String, Object> oldData, Map<String, Object> newData);
    
    void onSynced(String stateId);
    
    void onError(String stateId, Throwable error);
}
```

### 4.3 事件接口

#### 4.3.1 接口定义

```java
package net.ooder.sdk.core.collaboration;

import java.util.Map;

public interface CoreEvent {
    
    String getEventId();
    
    String getEventType();
    
    Object getSource();
    
    Object getTarget();
    
    Map<String, Object> getData();
    
    long getTimestamp();
    
    boolean isCancelled();
    
    void cancel();
}
```

#### 4.3.2 事件处理器

```java
package net.ooder.sdk.core.collaboration;

public interface CoreEventHandler {
    
    void handle(CoreEvent event);
    
    boolean canHandle(String eventType);
    
    int getPriority();
}
```

#### 4.3.3 事件分发器

```java
package net.ooder.sdk.core.collaboration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventDispatcher {
    
    void registerHandler(String eventType, CoreEventHandler handler);
    
    void unregisterHandler(String eventType, CoreEventHandler handler);
    
    void dispatch(CoreEvent event);
    
    CompletableFuture<Void> dispatchAsync(CoreEvent event);
    
    List<CoreEventHandler> getHandlers(String eventType);
    
    void clearHandlers(String eventType);
}
```

## 5. 与ooder-common集成

### 5.1 身份集成

基于 ooder-common 的 Person 和 Org 接口：

```java
package net.ooder.sdk.core.security.integration;

import net.ooder.org.Person;
import net.ooder.org.Org;

public class OoderIdentityAdapter implements CoreIdentity {
    
    private Person person;
    private Org org;
    
    public OoderIdentityAdapter(Person person) {
        this.person = person;
    }
    
    public OoderIdentityAdapter(Org org) {
        this.org = org;
    }
    
    @Override
    public String getIdentityId() {
        if (person != null) {
            return person.getId();
        }
        return org != null ? org.getId() : null;
    }
    
    @Override
    public String getName() {
        if (person != null) {
            return person.getName();
        }
        return org != null ? org.getName() : null;
    }
    
    @Override
    public IdentityType getType() {
        if (person != null) {
            return IdentityType.USER;
        }
        return org != null ? IdentityType.ORGANIZATION : null;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attrs = new HashMap<>();
        if (person != null) {
            attrs.put("email", person.getEmail());
            attrs.put("mobile", person.getMobile());
            attrs.put("account", person.getAccount());
        }
        if (org != null) {
            attrs.put("orgCode", org.getCode());
            attrs.put("orgType", org.getType());
        }
        return attrs;
    }
}
```

### 5.2 消息集成

基于 ooder-common 的 Msg 接口：

```java
package net.ooder.sdk.core.collaboration.integration;

import net.ooder.msg.Msg;

public class OoderMessageAdapter implements CoreMessage {
    
    private Msg msg;
    
    public OoderMessageAdapter(Msg msg) {
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
        return MessageType.NOTIFICATION;
    }
    
    @Override
    public Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", msg.getTitle());
        payload.put("body", msg.getBody());
        payload.put("type", msg.getType());
        return payload;
    }
    
    @Override
    public long getTimestamp() {
        return msg.getEventTime();
    }
}
```

## 6. 最佳实践

### 6.1 接口实现规范

1. **异步优先**：所有耗时操作应返回 CompletableFuture
2. **异常处理**：使用特定异常类型，包含上下文信息
3. **资源管理**：实现 AutoCloseable 接口，确保资源释放
4. **线程安全**：实现类应保证线程安全

### 6.2 扩展开发指南

1. **继承接口**：可以继承核心接口，添加特定功能
2. **组合模式**：使用组合而非继承，提高灵活性
3. **工厂模式**：使用工厂创建实例，隐藏实现细节
4. **建造者模式**：复杂对象使用建造者模式创建

### 6.3 性能优化建议

1. **连接池**：使用连接池管理连接
2. **缓存**：缓存频繁访问的数据
3. **批量操作**：支持批量操作减少网络开销
4. **异步处理**：使用异步处理提高吞吐量

## 7. 总结

核心抽象层为 Ooder Agent SDK 提供了统一的基础架构：

1. **网络抽象**：统一的连接、协议、传输接口
2. **安全抽象**：统一的身份、权限、加密接口
3. **协作抽象**：统一的消息、状态、事件接口
4. **ooder-common集成**：与现有系统无缝集成

通过核心抽象层，南北向服务可以共享统一的数据模型和接口，确保系统的一致性和可扩展性。

---

**Ooder Agent SDK 0.7.2** - 构建智能、协作、安全的Agent生态系统！

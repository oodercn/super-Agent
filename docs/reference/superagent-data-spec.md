# SuperAgent数据规范

## 1. 核心数据模型

### 1.1 Skill数据模型

```java
public class Skill {
    // 基本信息
    private String skillId;          // 唯一标识
    private String name;             // 名称
    private String description;      // 描述
    private String version;          // 版本
    
    // 所有者信息
    private OwnerType ownerType;     // 所有者类型（PERSONAL, ORGANIZATION, EXTERNAL）
    private String ownerId;          // 所有者ID
    private String organizationId;   // 组织ID（如果是组织Skill）
    
    // 访问控制信息
    private AccessControlPolicy accessPolicy; // 访问控制策略
    private List<String> allowedExternalSystems; // 允许访问的外部系统列表
    private boolean isA2ASupported;  // 是否支持A2A访问
    
    // 静态定义信息
    private String vfsFilePath;      // VFS文件地址（静态定义时指定）
    private boolean isStatic;        // 是否为静态定义状态
    
    // 分类信息
    private List<String> categories; // 功能分类列表（如：network, file, http等）
    private SkillType skillType;     // Skill类型（对应deviceType）
    private String techStack;        // 技术栈（如：java, python, go等）
    private String deploymentType;   // 部署方式（如：local, cloud, edge等）
    
    // 部署信息
    private DeploymentLocation location;  // 部署位置信息
    private List<DeploymentExtProperty> extProperties; // 部署扩展属性
    private List<AppModule> associatedAppModules; // 关联的App/Module
    
    // 状态信息
    private SkillStatus status;      // 状态
    private Date registerTime;       // 注册时间
    private Date lastUpdateTime;     // 最后更新时间
    
    // 通信信息
    private List<SkillEndpoint> endpoints;  // 通信端点列表（对应deviceendpoint）
    
    // 功能接口列表
    private List<SkillInterface> interfaces;  // 对外提供的接口列表
    
    // 配置信息
    private Map<String, Object> config;       // 配置参数
    
    // VFS存储信息
    private VfsInfo vfsInfo;         // VFS存储空间分配信息
    
    // 授权信息
    private AuthorizationInfo authInfo;  // 授权访问信息
    private boolean ssoEnabled;      // 是否启用SSO
    private String ssoProvider;      // SSO提供商
    
    // AIServer认证信息
    private boolean isAiServerAuthenticated; // 是否通过AIServer认证
    private String aiServerAuthId;   // AIServer认证ID
    private Date aiServerAuthTime;   // AIServer认证时间
    private String aiServerCertUrl;  // AIServer认证证书URL
    private List<String> confirmedCategories; // 确认的Cat功能列表
    private boolean isSecuritySkill; // 是否为安全Skill
    
    // 安全通信信息
    private String publicKey;        // 公钥
    private String privateKeyId;     // 私钥ID（关联到密钥管理系统）
    private List<SkillCommunicationInfo> communicationPartners; // 通信伙伴列表
    
    // 健康信息
    private SkillHealth health;      // 健康状态
    
    // 发现信息
    private boolean isDiscovered;    // 是否被发现
    private Date discoveredTime;     // 发现时间
    private String discoveredBy;     // 发现者（MCP Agent ID）
}
```

### 1.2 Agent数据模型

```java
public abstract class AbstractAgent {
    // 基本信息
    private String agentId;          // 唯一标识
    private String name;             // 名称
    private String description;      // 描述
    private AgentType type;          // 类型（MCP, ROUTE, END）
    
    // 状态信息
    private AgentStatus status;      // 状态（如：INIT, RUNNING, STOPPED, ERROR等）
    private Date startTime;          // 启动时间
    private Date lastHeartbeatTime;  // 最后心跳时间
    
    // 通信信息
    private List<String> endpoints;         // 通信端点列表
    private String protocol;         // 通信协议
    
    // 配置信息
    private Map<String, Object> config;       // 配置参数
    
    // 健康信息
    private AgentHealth health;      // 健康状态
    
    // 核心功能
    public abstract void start();    // 启动agent
    public abstract void stop();     // 停止agent
    public abstract void processRequest(AgentRequest request);  // 处理请求
    public abstract void sendResponse(AgentResponse response);  // 发送响应
    public abstract void handleEvent(AgentEvent event);         // 处理事件
}
```

### 1.3 Skillflow数据模型

```json
{
  "skillflowId": "flow-001",
  "name": "文件上传并分析",
  "description": "上传文件到服务器并进行内容分析",
  "version": "1.0.0",
  "createTime": "2023-01-01T00:00:00Z",
  "updateTime": "2023-01-01T00:00:00Z",
  "status": "ACTIVE",
  "steps": [
    {
      "stepId": "step-001",
      "name": "接收文件",
      "description": "接收客户端上传的文件",
      "agentType": "mcpagent",
      "action": "receiveFile",
      "params": {
        "maxSize": "10MB"
      },
      "nextStep": "step-002",
      "onError": "step-error"
    }
  ],
  "variables": {
    "fileId": "",
    "fileName": "",
    "fileSize": 0,
    "analysisResult": {}
  },
  "metadata": {
    "author": "system",
    "tags": ["file", "analysis"]
  }
}
```

## 2. 数据分类

### 2.1 配置数据

- Skill配置
- Agent配置
- Skillflow配置
- 路由规则配置
- 安全策略配置

### 2.2 运行时数据

- Skill实例数据
- Agent实例数据
- Skillflow实例数据
- 上下文数据
- 执行结果数据

### 2.3 状态数据

- Skill状态
- Agent状态
- Skillflow状态
- 步骤状态

### 2.4 日志数据

- 操作日志
- 访问日志
- 执行日志
- 异常日志
- 事件日志

## 3. 数据存储

### 3.1 VFS存储

- Skill静态定义存储
- Skill实例数据存储
- 配置文件存储
- 日志文件存储

### 3.2 数据库存储

- Skill元数据
- Agent元数据
- Skillflow定义
- 实例状态数据
- 审计日志

## 4. 数据传输

### 4.1 传输协议

- HTTP/HTTPS
- WebSocket
- MQTT
- gRPC

### 4.2 数据格式

- JSON
- Protobuf
- XML

### 4.3 传输安全

- TLS/SSL加密
- 端到端加密
- 数据完整性验证
- 身份验证

## 5. 数据安全

### 5.1 数据加密

- 传输加密
- 存储加密
- 端到端加密

### 5.2 数据脱敏

- 敏感数据脱敏规则
- 脱敏策略配置
- 脱敏效果验证

### 5.3 数据完整性

- 哈希算法验证
- 数字签名验证
- 防止数据篡改

## 6. 数据生命周期

### 6.1 数据创建

- Skill创建
- Agent创建
- Skillflow创建
- 实例创建

### 6.2 数据更新

- Skill更新
- Agent更新
- Skillflow更新
- 状态更新

### 6.3 数据删除

- Skill删除
- Agent删除
- Skillflow删除
- 实例清理
- 日志归档

## 7. 数据质量

### 7.1 数据一致性

- 分布式数据一致性
- 状态同步机制
- 数据冲突解决

### 7.2 数据准确性

- 数据验证规则
- 数据校验机制
- 错误数据处理

### 7.3 数据可用性

- 数据备份策略
- 数据恢复机制
- 高可用性设计

## 8. 数据治理

### 8.1 数据标准

- 数据命名规范
- 数据格式规范
- 数据编码规范

### 8.2 数据管理

- 数据所有权
- 数据访问权限
- 数据审计机制

### 8.3 数据监控

- 数据流量监控
- 数据质量监控
- 异常数据检测
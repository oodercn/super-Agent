# Skill Runtime Specification - v0.7.0

## 概述

本文档定义技能在运行时的行为规范，包括技能注册、能力声明、接口规范、配置管理和可观测性。

---

## 一、技能生命周期

### 1.1 生命周期状态

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           技能生命周期                                        │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐
    │ CREATED │────▶│ STARTING│────▶│ RUNNING │────▶│ STOPPING│
    └─────────┘     └─────────┘     └─────────┘     └─────────┘
         │                               │               │
         │                               │               │
         │          ┌─────────┐          │          ┌─────────┐
         └─────────▶│  ERROR  │◀─────────┘─────────▶│ STOPPED │
                    └─────────┘                     └─────────┘
                         │
                         ▼
                    ┌─────────┐
                    │  FAILED │
                    └─────────┘
```

### 1.2 状态说明

| 状态 | 说明 | 可转换状态 |
|------|------|-----------|
| CREATED | 技能已创建，未启动 | STARTING, ERROR |
| STARTING | 技能正在启动 | RUNNING, ERROR, FAILED |
| RUNNING | 技能正常运行 | STOPPING, ERROR |
| STOPPING | 技能正在停止 | STOPPED, ERROR |
| STOPPED | 技能已停止 | STARTING |
| ERROR | 技能遇到错误 | STARTING, STOPPED |
| FAILED | 技能启动失败 | STOPPED |

### 1.3 生命周期事件

```java
public enum SkillLifecycleEvent {
    SKILL_CREATED,          // 技能创建
    SKILL_STARTING,         // 技能启动中
    SKILL_STARTED,          // 技能启动完成
    SKILL_RUNNING,          // 技能运行中
    SKILL_STOPPING,         // 技能停止中
    SKILL_STOPPED,          // 技能已停止
    SKILL_ERROR,            // 技能错误
    SKILL_FAILED,           // 技能失败
    SKILL_RESTARTING,       // 技能重启中
    SKILL_HEALTH_CHECK,     // 健康检查
    SKILL_CAPABILITY_UPDATE // 能力更新
}
```

---

## 二、技能注册

### 2.1 注册流程

```
┌─────────────────┐                              ┌─────────────────┐
│    Skill        │                              │   MCPAgent      │
│  (RouteAgent)   │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  1. 加载 skill.yaml                            │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  2. 验证技能签名                                │
         │◀───────────────────────────────────────────────│
         │                                                │
         │  3. SKILL_REGISTER                             │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  4. 验证注册信息                                │
         │                                                │
         │  5. SKILL_REGISTER_ACK                         │
         │◀───────────────────────────────────────────────│
         │                                                │
         │  6. 加入场景组                                  │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  7. 开始心跳                                    │
         │───────────────────────────────────────────────▶│
         │                                                │
```

### 2.2 注册信息

```json
{
  "agentId": "agent-001",
  "skillId": "skill-org-feishu",
  "version": "0.7.0",
  "skillType": "enterprise-skill",
  "endpoint": "192.168.1.100:8080",
  "capabilities": [
    {
      "id": "org-data-read",
      "name": "Organization Data Read",
      "category": "data-access"
    },
    {
      "id": "user-auth",
      "name": "User Authentication",
      "category": "authentication"
    }
  ],
  "scenes": ["auth"],
  "config": {
    "requiresAuth": true,
    "authType": "api-key"
  },
  "timestamp": 1707868800000,
  "signature": "MEUCIQDxxx..."
}
```

### 2.3 注册验证

```java
public class SkillRegistrationValidator {
    
    public ValidationResult validate(SkillRegistration registration) {
        // 1. 验证必填字段
        if (registration.getSkillId() == null) {
            return ValidationResult.error("skillId is required");
        }
        
        // 2. 验证版本格式
        if (!isValidVersion(registration.getVersion())) {
            return ValidationResult.error("invalid version format");
        }
        
        // 3. 验证能力列表
        if (registration.getCapabilities().isEmpty()) {
            return ValidationResult.error("at least one capability required");
        }
        
        // 4. 验证签名
        if (!verifySignature(registration)) {
            return ValidationResult.error("signature verification failed");
        }
        
        return ValidationResult.success();
    }
}
```

---

## 三、能力声明

### 3.1 能力声明格式

```json
{
  "skillId": "skill-org-feishu",
  "capabilities": [
    {
      "id": "org-data-read",
      "name": "Organization Data Read",
      "description": "读取飞书组织机构数据",
      "category": "data-access",
      "version": "1.0",
      "parameters": [
        {
          "name": "orgId",
          "type": "string",
          "required": false,
          "description": "组织ID"
        }
      ],
      "returns": {
        "type": "object",
        "description": "组织树结构"
      },
      "endpoints": [
        {
          "path": "/api/org/tree",
          "method": "GET"
        }
      ]
    }
  ]
}
```

### 3.2 能力发现接口

技能应提供能力发现接口：

```
GET /api/capabilities

Response:
{
  "success": true,
  "data": {
    "skillId": "skill-org-feishu",
    "version": "0.7.0",
    "capabilities": [...]
  }
}
```

### 3.3 能力调用示例

**通过 SDK 调用：**

```java
// 获取技能代理
SkillProxy proxy = sdk.getSkillProxy("skill-org-feishu");

// 调用能力
CapabilityResult result = proxy.invoke("org-data-read")
    .parameter("orgId", "root")
    .parameter("includeInactive", false)
    .execute();

Org org = result.getData(Org.class);
```

**通过 HTTP 调用：**

```bash
curl -X GET "http://192.168.1.100:8080/api/org/tree?orgId=root" \
  -H "X-API-Key: sk-xxxxx"
```

---

## 四、接口规范

### 4.1 RESTful 接口规范

**请求格式：**

```http
GET /api/org/tree HTTP/1.1
Host: 192.168.1.100:8080
X-API-Key: sk-xxxxx
Content-Type: application/json
```

**成功响应：**

```json
{
  "success": true,
  "data": {
    "orgId": "root",
    "name": "公司",
    "children": [...]
  },
  "timestamp": 1707868800000
}
```

**错误响应：**

```json
{
  "success": false,
  "error": {
    "code": "SKILL_001",
    "message": "Organization not found",
    "details": "Organization with ID 'org-xxx' does not exist",
    "traceId": "abc123"
  },
  "timestamp": 1707868800000
}
```

### 4.2 WebSocket 接口规范

**连接：**

```
ws://192.168.1.100:8080/ws/events?apiKey=sk-xxxxx
```

**消息格式：**

```json
{
  "type": "event",
  "event": "person.created",
  "data": {
    "personId": "user-001",
    "name": "张三"
  },
  "timestamp": 1707868800000
}
```

### 4.3 错误码规范

| 错误码 | HTTP状态码 | 说明 |
|--------|-----------|------|
| SKILL_001 | 404 | 资源未找到 |
| SKILL_002 | 400 | 参数错误 |
| SKILL_003 | 401 | 认证失败 |
| SKILL_004 | 403 | 权限不足 |
| SKILL_005 | 429 | 请求频率超限 |
| SKILL_006 | 500 | 内部错误 |
| SKILL_007 | 503 | 服务不可用 |
| SKILL_008 | 504 | 请求超时 |

---

## 五、配置管理

### 5.1 配置来源优先级

```
1. 环境变量 (最高优先级)
   ↓
2. 系统属性 (-D 参数)
   ↓
3. 配置文件 (application.yml)
   ↓
4. 默认值 (最低优先级)
```

### 5.2 配置文件位置

```
/vfs/
├── config/
│   └── skills/
│       └── skill-org-feishu/
│           ├── application.yml       # 主配置
│           ├── application-prod.yml  # 生产环境配置
│           └── application-dev.yml   # 开发环境配置
```

### 5.3 配置热更新

技能应支持配置热更新：

```java
@RestController
@RequestMapping("/api/config")
public class ConfigController {
    
    @Autowired
    private SkillConfigManager configManager;
    
    @PostMapping("/reload")
    public Result<Void> reloadConfig() {
        configManager.reload();
        return Result.success();
    }
}
```

### 5.4 敏感配置处理

```yaml
# 使用环境变量
feishu:
  app-secret: ${FEISHU_APP_SECRET}

# 使用加密存储
feishu:
  app-secret: ENC(encrypted_value)
```

---

## 六、可观测性

### 6.1 健康检查

**健康检查端点：**

```
GET /actuator/health

Response:
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "feishu": {
      "status": "UP",
      "details": {
        "connected": true,
        "latency": 45
      }
    }
  }
}
```

### 6.2 指标暴露

**Prometheus 格式：**

```
GET /actuator/prometheus

# HELP skill_requests_total Total requests to skill
# TYPE skill_requests_total counter
skill_requests_total{skill="skill-org-feishu",capability="org-data-read"} 1234

# HELP skill_request_duration_seconds Request duration
# TYPE skill_request_duration_seconds histogram
skill_request_duration_seconds{skill="skill-org-feishu",le="0.1"} 1000
skill_request_duration_seconds{skill="skill-org-feishu",le="0.5"} 1150
```

### 6.3 日志规范

**日志格式：**

```
{timestamp} [{level}] [{traceId}] {logger} - {message}
```

**示例：**

```
2026-02-14 10:00:00.000 [INFO] [abc123] n.o.s.o.f.api.OrgApiController - Get org tree request received
2026-02-14 10:00:00.050 [INFO] [abc123] n.o.s.o.f.service.OrgService - Org tree fetched successfully
```

### 6.4 链路追踪

```java
@RestController
public class OrgApiController {
    
    @GetMapping("/api/org/tree")
    @Traced("org-tree")
    public Result<Org> getOrgTree(@TraceParam String orgId) {
        // 自动记录链路信息
        return Result.success(orgService.getOrgTree(orgId));
    }
}
```

---

## 七、心跳与健康

### 7.1 心跳机制

```
┌─────────────────┐                              ┌─────────────────┐
│    Skill        │                              │   MCPAgent      │
│                 │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  HEARTBEAT (every 5s)                          │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  HEARTBEAT_ACK                                 │
         │◀───────────────────────────────────────────────│
         │                                                │
         │  ... (repeat)                                  │
         │                                                │
```

### 7.2 心跳消息

```json
{
  "type": "SKILL_HEARTBEAT",
  "agentId": "agent-001",
  "skillId": "skill-org-feishu",
  "status": "HEALTHY",
  "metrics": {
    "cpu": 45.5,
    "memory": 512,
    "requests": 1234,
    "errors": 5
  },
  "timestamp": 1707868800000,
  "signature": "xxx"
}
```

### 7.3 健康状态

| 状态 | 说明 | 处理方式 |
|------|------|----------|
| HEALTHY | 健康，正常服务 | 无需处理 |
| DEGRADED | 降级，部分功能不可用 | 记录日志，通知管理员 |
| UNHEALTHY | 不健康，无法服务 | 从服务列表移除，触发告警 |
| MAINTENANCE | 维护中 | 暂停服务，显示维护提示 |

---

## 八、优雅关闭

### 8.1 关闭流程

```
1. 收到关闭信号 (SIGTERM)
      │
      ▼
2. 停止接收新请求
      │
      ▼
3. 完成进行中的请求
      │
      ▼
4. 发送 SKILL_UNREGISTER
      │
      ▼
5. 退出场景组
      │
      ▼
6. 保存状态（如需要）
      │
      ▼
7. 关闭服务
```

### 8.2 关闭钩子

```java
@SpringBootApplication
public class FeishuSkillApplication {
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FeishuSkillApplication.class);
        app.registerShutdownHook(true);
        app.run(args);
    }
    
    @PreDestroy
    public void onShutdown() {
        // 发送注销消息
        skillRegistry.unregister();
        
        // 退出场景组
        sceneManager.leaveAllScenes();
        
        // 保存状态
        stateManager.save();
    }
}
```

---

## 九、安全

### 9.1 认证方式

| 方式 | 适用场景 | 说明 |
|------|----------|------|
| API Key | 服务间调用 | 简单高效 |
| OAuth2 | 用户授权 | 标准授权流程 |
| JWT | 无状态认证 | 携带用户信息 |
| mTLS | 服务间认证 | 双向证书验证 |

### 9.2 API Key 认证

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new ApiKeyFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

public class ApiKeyFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) {
        String apiKey = request.getHeader("X-API-Key");
        if (apiKeyValidator.validate(apiKey)) {
            // 认证成功
            filterChain.doFilter(request, response);
        } else {
            response.sendError(401, "Invalid API Key");
        }
    }
}
```

### 9.3 请求签名

```java
public class RequestSigner {
    
    public String sign(String method, String path, String body, long timestamp) {
        String data = method + path + body + timestamp;
        return HmacUtils.hmacSha256Hex(secretKey, data);
    }
    
    public boolean verify(String method, String path, String body, 
                          long timestamp, String signature) {
        String expected = sign(method, path, body, timestamp);
        return expected.equals(signature);
    }
}
```

---

## 十、性能规范

### 10.1 响应时间要求

| 操作类型 | 目标响应时间 | 最大响应时间 |
|----------|-------------|-------------|
| 简单查询 | < 100ms | 500ms |
| 复杂查询 | < 500ms | 2s |
| 批量操作 | < 2s | 10s |
| 文件上传 | - | 30s |

### 10.2 并发要求

```yaml
performance:
  maxConcurrentRequests: 1000
  requestQueueSize: 500
  threadPoolSize: 200
```

### 10.3 资源限制

```yaml
resources:
  cpu:
    limit: "1"
    request: "500m"
  memory:
    limit: "1Gi"
    request: "512Mi"
```

---

## 十一、容错处理

### 11.1 重试策略

```java
@Retryable(
    value = {ApiException.class},
    maxAttempts = 3,
    backoff = @Backoff(delay = 1000, multiplier = 2)
)
public Org getOrgTree(String orgId) {
    return feishuClient.getOrgTree(orgId);
}
```

### 11.2 熔断策略

```java
@CircuitBreaker(
    name = "feishu",
    fallbackMethod = "getOrgTreeFallback"
)
public Org getOrgTree(String orgId) {
    return feishuClient.getOrgTree(orgId);
}

public Org getOrgTreeFallback(String orgId, Exception e) {
    // 返回缓存数据或默认值
    return cacheManager.get("org-tree-" + orgId, Org.class);
}
```

### 11.3 限流策略

```yaml
resilience4j:
  ratelimiter:
    instances:
      feishu:
        limitForPeriod: 100
        limitRefreshPeriod: 1s
        timeoutDuration: 5s
```

---

## 十二、附录

### A. 配置模板

```yaml
# application.yml
server:
  port: 8080

skill:
  id: skill-org-feishu
  version: 0.7.0
  
feishu:
  app-id: ${FEISHU_APP_ID}
  app-secret: ${FEISHU_APP_SECRET}
  api-base-url: https://open.feishu.cn/open-apis
  timeout: 30000

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics
  metrics:
    tags:
      skill: skill-org-feishu

logging:
  level:
    net.ooder: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%X{traceId}] %-5level %logger{36} - %msg%n"
```

### B. 健康检查配置

```yaml
management:
  health:
    liveness:
      enabled: true
    readiness:
      enabled: true
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
```

### C. 监控指标

| 指标名称 | 类型 | 说明 |
|----------|------|------|
| skill_requests_total | Counter | 请求总数 |
| skill_request_duration | Histogram | 请求耗时 |
| skill_errors_total | Counter | 错误总数 |
| skill_active_requests | Gauge | 活跃请求数 |
| skill_cache_hits | Counter | 缓存命中数 |
| skill_cache_misses | Counter | 缓存未命中数 |

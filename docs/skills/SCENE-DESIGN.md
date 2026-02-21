# 场景设计规范 (Scene Design Specification)

## 概述

本文档面向技能开发者，介绍如何设计场景、定义能力、以及开发符合 Ooder 规范的技能。

---

## 一、核心概念

### 1.1 场景（Scene）

场景是技能协作的静态定义，描述了一组角色如何协同工作来提供服务。

**场景特点：**
- **静态性**：场景定义在技能发布时就已确定
- **平等性**：所有场景在 MCP 网络中是平等的，没有父子关系
- **可组合性**：多个技能可以参与同一个场景

### 1.2 组（Group）

组是场景在运行时的实例化，负责：
- 实时连接情况反馈
- 场景实例关系处理
- 成员管理和协调

### 1.3 角色（Role）

角色定义了参与场景的成员类型：

| 角色 | 说明 | 职责 |
|------|------|------|
| **RouteAgent** | 服务提供者 | 提供能力服务，创建场景组 |
| **EndAgent** | 服务消费者 | 消费能力服务，加入场景组 |

---

## 二、场景设计流程

### 2.1 设计步骤

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           场景设计流程                                        │
└─────────────────────────────────────────────────────────────────────────────┘

1. 确定场景目标
   │
   │  问：这个场景要解决什么问题？
   │  问：有哪些参与者？
   │
   ▼
2. 定义能力需求
   │
   │  问：需要哪些能力？
   │  问：这些能力如何分类？
   │
   ▼
3. 设计角色
   │
   │  问：有哪些角色？
   │  问：哪些角色是必须的？
   │  问：每个角色需要什么能力？
   │
   ▼
4. 定义接口
   │
   │  问：提供哪些 API？
   │  问：参数和返回值是什么？
   │
   ▼
5. 编写场景定义
   │
   │  输出：scene-{name}.yaml
   │
   ▼
6. 实现技能代码
   │
   │  输出：skill-{id}/
   │
```

### 2.2 设计检查清单

- [ ] 场景名称是否清晰表达目的？
- [ ] 能力定义是否完整？
- [ ] 角色分工是否明确？
- [ ] 必填角色是否标注？
- [ ] 接口设计是否合理？
- [ ] 是否考虑了安全策略？

---

## 三、能力定义

### 3.1 能力命名规范

```
{动作}-{对象}-{修饰}

示例：
- org-data-read     (读取组织数据)
- user-auth         (用户认证)
- send-message      (发送消息)
- file-read         (文件读取)
- file-write        (文件写入)
```

### 3.2 能力分类

| 分类 | 前缀 | 说明 | 示例 |
|------|------|------|------|
| 数据访问 | data- | 数据读写操作 | data-read, data-write |
| 认证授权 | auth- | 认证和授权 | auth-user, auth-sso |
| 通信 | msg-, email- | 消息和通知 | msg-send, email-send |
| 存储 | file-, storage- | 文件和存储 | file-read, storage-sync |
| 集成 | sync-, import- | 外部系统集成 | sync-data, import-user |

### 3.3 能力定义模板

```yaml
capabilities:
  - id: org-data-read
    name: Organization Data Read
    description: |
      读取组织机构数据，包括部门树、成员列表、成员详情等。
      支持增量同步和全量同步。
    category: data-access
    version: "1.0"
    parameters:
      - name: orgId
        type: string
        required: false
        description: 组织ID，不传则使用默认组织
      - name: includeInactive
        type: boolean
        required: false
        default: false
        description: 是否包含已停用成员
    returns:
      type: object
      description: 组织树结构
      schema:
        type: object
        properties:
          orgId:
            type: string
          name:
            type: string
          children:
            type: array
    examples:
      - description: 获取完整组织树
        input: {}
        output:
          orgId: "root"
          name: "公司"
          children: []
```

---

## 四、场景定义

### 4.1 场景定义模板

```yaml
# scenes/auth.yaml
name: auth
description: |
  认证场景，提供用户认证和组织数据访问能力。
  适用于需要用户登录和组织架构的应用。

capabilities:
  - org-data-read
  - user-auth

roles:
  - roleId: org-provider
    name: Organization Provider
    description: 提供组织数据和认证服务
    required: true
    capabilities:
      - org-data-read
      - user-auth
  
  - roleId: org-consumer
    name: Organization Consumer
    description: 消费组织数据
    required: false
    capabilities:
      - org-data-read

communicationProtocol: http
securityPolicy: api-key
allowParallel: true

constraints:
  maxProviders: 1           # 最多1个提供者（企业级技能）
  minConsumers: 0           # 最少0个消费者
  requireAuth: true         # 需要认证
```

### 4.2 场景字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 场景名称，全局唯一 |
| description | string | 是 | 场景描述 |
| capabilities | []string | 是 | 场景提供的能力列表 |
| roles | []Role | 是 | 角色定义列表 |
| communicationProtocol | string | 是 | 通信协议：http, websocket, grpc |
| securityPolicy | string | 是 | 安全策略：api-key, oauth2, jwt |
| allowParallel | boolean | 否 | 是否允许并行执行，默认 true |
| constraints | object | 否 | 约束条件 |

### 4.3 角色字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| roleId | string | 是 | 角色ID |
| name | string | 是 | 角色名称 |
| description | string | 否 | 角色描述 |
| required | boolean | 是 | 是否必须角色 |
| capabilities | []string | 是 | 角色需要的能力 |

---

## 五、技能类型与场景

### 5.1 企业级技能场景

企业级技能通常是单例的，一个组织只部署一个：

```yaml
# 企业级技能场景示例
name: auth
constraints:
  maxProviders: 1           # 只允许一个提供者
  singleton: true           # 单例模式
```

**运行策略：**
- 如果场景允许多中心并行运行：RouteAgent 直接提供服务
- 如果场景不允许并行运行：RouteAgent 作为代理转发到主节点

### 5.2 工具类技能场景

工具类技能可以部署多个：

```yaml
# 工具类技能场景示例
name: pdf-processing
constraints:
  maxProviders: -1          # 不限制提供者数量
  singleton: false          # 非单例模式
```

### 5.3 集成类技能场景

集成类技能连接外部系统：

```yaml
# 集成类技能场景示例
name: document
constraints:
  maxProviders: -1          # 可以有多个文档库
  singleton: false
```

---

## 六、接口设计

### 6.1 RESTful 接口规范

```yaml
endpoints:
  - path: /api/org/tree
    method: GET
    description: 获取组织树
    capability: org-data-read
    authentication: true
    rateLimit: 60
    parameters:
      - name: orgId
        in: query
        type: string
        required: false
        description: 组织ID
    response:
      type: object
      schema:
        $ref: '#/components/schemas/OrgTree'
    errors:
      - code: 401
        message: Unauthorized
      - code: 404
        message: Organization not found
```

### 6.2 接口命名规范

```
/api/{资源}/{动作或ID}

示例：
GET  /api/org/tree          # 获取组织树
GET  /api/org/person/{id}   # 获取人员信息
POST /api/msg/send          # 发送消息
GET  /api/doc/search        # 搜索文档
```

### 6.3 统一响应格式

```json
{
  "success": true,
  "data": {
    // 响应数据
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
    "details": "Organization with ID 'org-001' does not exist"
  },
  "timestamp": 1707868800000
}
```

---

## 七、技能项目结构

### 7.1 标准目录结构

```
skill-{id}/
├── skill.yaml                 # 技能清单（必需）
├── SKILLS.md                  # 技能说明文档（必需）
├── README.md                  # 简介（必需）
├── pom.xml                    # Maven 配置（Java技能）
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── net/ooder/skill/{category}/{name}/
│   │   │       ├── {Name}SkillApplication.java    # 启动类
│   │   │       ├── api/                           # API 接口
│   │   │       │   └── OrgApiController.java
│   │   │       ├── service/                       # 业务服务
│   │   │       │   └── OrgService.java
│   │   │       ├── client/                        # 外部客户端
│   │   │       │   └── FeishuClient.java
│   │   │       └── config/                        # 配置类
│   │   │           └── SkillConfig.java
│   │   └── resources/
│   │       ├── application.yml                    # 应用配置
│   │       └── application-example.yml            # 配置示例
│   └── test/
│       └── java/
├── scenes/                    # 场景定义
│   └── auth.yaml
├── config/                    # 配置模板
│   └── config-template.yaml
└── lib/                       # 依赖库（可选）
```

### 7.2 必需文件

| 文件 | 用途 | 读者 |
|------|------|------|
| skill.yaml | 技能元数据 | SDK、SkillCenter |
| SKILLS.md | 详细说明 | AI、开发者 |
| README.md | 简介 | 用户、开发者 |

---

## 八、配置设计

### 8.1 配置模板

```yaml
# config/config-template.yaml
# 技能配置模板 - 复制此文件并重命名为 application.yml

# 必需配置
feishu:
  app-id: ${FEISHU_APP_ID}           # 飞书应用ID
  app-secret: ${FEISHU_APP_SECRET}   # 飞书应用密钥

# 可选配置
feishu:
  api-base-url: https://open.feishu.cn/open-apis
  timeout: 30000
  cache:
    enabled: true
    ttl: 300000

# 日志配置
logging:
  level:
    net.ooder.skill: DEBUG
```

### 8.2 配置项定义

在 skill.yaml 中定义配置要求：

```yaml
spec:
  config:
    required:
      - name: FEISHU_APP_ID
        type: string
        description: 飞书应用ID，从飞书开放平台获取
        secret: false
        validation: "^cli_[a-zA-Z0-9]{16,32}$"
      - name: FEISHU_APP_SECRET
        type: string
        description: 飞书应用密钥
        secret: true
    optional:
      - name: FEISHU_API_BASE_URL
        type: string
        description: 飞书API基础URL
        default: https://open.feishu.cn/open-apis
```

---

## 九、开发示例

### 9.1 创建企业级技能

**步骤1：创建项目**

```bash
mkdir skill-org-feishu
cd skill-org-feishu

# 创建目录结构
mkdir -p src/main/java/net/ooder/skill/org/feishu/{api,service,client,config}
mkdir -p src/main/resources
mkdir -p scenes
mkdir -p config
```

**步骤2：编写 skill.yaml**

```yaml
apiVersion: skill.ooder.net/v1
kind: Skill

metadata:
  id: skill-org-feishu
  name: Feishu Organization Service
  version: 0.7.0
  description: 提供飞书组织机构数据集成能力
  author: Ooder Team
  license: Apache-2.0

spec:
  type: enterprise-skill
  runtime:
    language: java
    javaVersion: "11"
    framework: spring-boot
    mainClass: net.ooder.skill.org.feishu.FeishuSkillApplication
  
  capabilities:
    - id: org-data-read
      name: Organization Data Read
      description: 读取飞书组织机构数据
      category: data-access
    - id: user-auth
      name: User Authentication
      description: 飞书用户认证
      category: authentication
  
  scenes:
    - name: auth
      description: 认证场景
      capabilities: [org-data-read, user-auth]
      roles:
        - roleId: org-provider
          required: true
          capabilities: [org-data-read, user-auth]
  
  config:
    required:
      - name: FEISHU_APP_ID
        type: string
        description: 飞书应用ID
      - name: FEISHU_APP_SECRET
        type: string
        description: 飞书应用密钥
        secret: true
```

**步骤3：编写场景定义**

```yaml
# scenes/auth.yaml
name: auth
description: 认证场景，提供用户认证和组织数据访问

capabilities:
  - org-data-read
  - user-auth

roles:
  - roleId: org-provider
    name: Organization Provider
    required: true
    capabilities: [org-data-read, user-auth]

communicationProtocol: http
securityPolicy: api-key
allowParallel: true
```

**步骤4：实现 API**

```java
// api/OrgApiController.java
@RestController
@RequestMapping("/api/org")
public class OrgApiController {
    
    @Autowired
    private OrgService orgService;
    
    @GetMapping("/tree")
    public Result<Org> getOrgTree(
            @RequestParam(required = false) String orgId,
            @RequestParam(required = false, defaultValue = "false") boolean includeInactive) {
        Org org = orgService.getOrgTree(orgId, includeInactive);
        return Result.success(org);
    }
    
    @GetMapping("/person/{id}")
    public Result<Person> getPerson(@PathVariable String id) {
        Person person = orgService.getPerson(id);
        return Result.success(person);
    }
}
```

**步骤5：编写 SKILLS.md**

```markdown
# skill-org-feishu

## 基本信息
- 技能ID: skill-org-feishu
- 版本: 0.7.0
- 类型: 企业级技能

## 提供的能力

### org-data-read
读取飞书组织机构数据，包括部门树、成员列表等。

**参数：**
- `orgId` (可选): 组织ID
- `includeInactive` (可选): 是否包含停用成员

**返回：**
组织树结构

### user-auth
飞书用户认证。

## 提供的场景

### auth 场景
认证场景，提供用户认证和组织数据访问能力。

## 配置说明

### 必需配置
- `FEISHU_APP_ID`: 飞书应用ID
- `FEISHU_APP_SECRET`: 飞书应用密钥

### 配置示例
\`\`\`yaml
feishu:
  app-id: cli_xxxxxxxxxxxx
  app-secret: xxxxxxxxxxxxxxxx
\`\`\`

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/org/tree | GET | 获取组织树 |
| /api/org/person/{id} | GET | 获取人员信息 |
| /api/auth/verify | POST | 验证用户认证 |
```

---

## 十、测试与发布

### 10.1 本地测试

```bash
# 编译
mvn clean package

# 运行
java -jar target/skill-org-feishu-0.7.0.jar

# 测试 API
curl http://localhost:8080/api/org/tree
```

### 10.2 发布到 SkillCenter

```bash
# 打包
zip -r skill-org-feishu-0.7.0.skill .

# 上传到 SkillCenter
skill-cli publish skill-org-feishu-0.7.0.skill
```

---

## 十一、最佳实践

### 11.1 场景设计原则

1. **单一职责**：一个场景解决一类问题
2. **最小能力集**：只定义必要的能力
3. **明确角色**：清晰定义角色和职责
4. **合理约束**：设置合理的约束条件

### 11.2 接口设计原则

1. **RESTful 风格**：遵循 REST 设计规范
2. **版本控制**：接口支持版本控制
3. **错误处理**：提供清晰的错误信息
4. **文档完善**：每个接口都有文档

### 11.3 配置设计原则

1. **最小配置**：尽量减少必需配置项
2. **合理默认值**：为可选配置提供默认值
3. **敏感信息**：敏感配置使用环境变量
4. **配置验证**：提供配置验证机制

---

## 附录

### A. 场景模板

```yaml
name: {scene-name}
description: |
  {场景描述}

capabilities:
  - {capability-1}
  - {capability-2}

roles:
  - roleId: {role-id}
    name: {role-name}
    description: {role-description}
    required: true/false
    capabilities:
      - {capability}

communicationProtocol: http | websocket | grpc
securityPolicy: api-key | oauth2 | jwt
allowParallel: true | false

constraints:
  maxProviders: {number}
  minConsumers: {number}
  requireAuth: true | false
```

### B. 能力模板

```yaml
- id: {capability-id}
  name: {capability-name}
  description: |
    {能力描述}
  category: {category}
  parameters:
    - name: {param-name}
      type: string | number | boolean | object | array
      required: true | false
      default: {default-value}
      description: {param-description}
  returns:
    type: {return-type}
    description: {return-description}
```

### C. 参考资源

- [Skill Package Protocol](../../protocol-release/v0.7.0/skill/skill-package-protocol.md)
- [Skill Manifest Specification](../../protocol-release/v0.7.0/skill/skill-manifest-spec.md)
- [Skill Discovery Protocol](../../protocol-release/v0.7.0/skill/skill-discovery-protocol.md)

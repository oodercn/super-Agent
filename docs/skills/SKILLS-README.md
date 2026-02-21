# Ooder Skills 用户说明书

## 概述

Ooder Skills 是一个模块化的能力扩展系统，允许应用程序按需获取各种能力，如组织机构数据、消息通知、文档管理等。本说明书帮助您理解和使用 Ooder Skills。

---

## 一、什么是技能（Skill）？

技能是 Ooder 平台的能力单元，提供特定的功能服务。每个技能都是独立的、可配置的、可组合的。

### 技能分类

| 类型 | 说明 | 示例 |
|------|------|------|
| **企业级技能** | 组织级能力，通常一个组织只部署一个 | 飞书组织、钉钉组织、企业微信 |
| **工具类技能** | 通用工具能力，可部署多个 | 邮件发送、PDF转换、图片处理 |
| **集成类技能** | 外部系统集成 | Confluence、Notion、Jira |
| **基础设施技能** | 核心基础服务 | VFS存储、消息队列、缓存 |

### 技能提供的能力

每个技能提供一组能力（Capabilities），例如：

- `org-data-read` - 读取组织机构数据
- `user-auth` - 用户认证
- `send-message` - 发送消息
- `file-read` - 文件读取

---

## 二、场景（Scene）概念

场景是技能的组织方式。一个场景定义了一组协作的技能如何共同提供服务。

### 场景示例

```
┌─────────────────────────────────────────────────────────────────┐
│                        auth 场景                                │
│                                                                 │
│  ┌─────────────────┐                                           │
│  │ skill-org-feishu│  ← 提供: org-data-read, user-auth         │
│  │  (RouteAgent)   │                                           │
│  └─────────────────┘                                           │
│           │                                                     │
│           │  场景组                                              │
│           ▼                                                     │
│  ┌─────────────────┐     ┌─────────────────┐                   │
│  │   Nexus App 1   │     │   Nexus App 2   │                   │
│  │   (EndAgent)    │     │   (EndAgent)    │                   │
│  │   消费组织数据   │     │   消费组织数据   │                   │
│  └─────────────────┘     └─────────────────┘                   │
└─────────────────────────────────────────────────────────────────┘
```

### 常见场景

| 场景 | 说明 | 提供的能力 |
|------|------|-----------|
| `auth` | 认证场景 | 用户认证、组织数据读取 |
| `messaging` | 消息场景 | 消息发送、消息接收 |
| `document` | 文档场景 | 文档读写、文档搜索 |
| `storage` | 存储场景 | 文件读写、文件同步 |

---

## 三、如何发现技能

### 3.1 通过 SkillCenter 搜索

SkillCenter 是技能的中央仓库，您可以通过以下方式搜索技能：

**按能力搜索：**
```
搜索: org-data-read
结果: skill-org-feishu, skill-org-dingding, skill-org-qiwei
```

**按场景搜索：**
```
搜索: auth
结果: skill-org-feishu, skill-org-dingding, skill-auth-ldap
```

**按关键词搜索：**
```
搜索: 飞书
结果: skill-org-feishu, skill-msg-feishu
```

### 3.2 通过 SDK 发现

```java
// 搜索提供特定能力的技能
List<SkillInfo> skills = sdk.discoverSkills()
    .byCapability("org-data-read")
    .byScene("auth")
    .execute();

for (SkillInfo skill : skills) {
    System.out.println(skill.getId() + " - " + skill.getName());
}
```

---

## 四、如何安装技能

### 4.1 两种部署模式

企业级技能支持两种部署模式：

| 模式 | 说明 | 适用场景 |
|------|------|----------|
| **远程托管** | 技能运行在 SkillCenter，您只需配置连接信息 | 快速部署、无需维护 |
| **本地部署** | 技能下载到本地运行 | 数据安全、内网环境 |

### 4.2 安装流程

```
1. 选择技能
      │
      ▼
2. 选择部署模式
      │
      ├──────────────────┬──────────────────┐
      ▼                  ▼                  │
   远程托管            本地部署            │
      │                  │                  │
      ▼                  ▼                  │
   配置连接信息      下载技能包            │
      │                  │                  │
      │                  ▼                  │
      │              安装到本地            │
      │                  │                  │
      │                  ▼                  │
      │              配置技能参数          │
      │                  │                  │
      │                  ▼                  │
      │              启动技能服务          │
      │                  │                  │
      └──────────────────┴──────────────────┘
                         │
                         ▼
                    技能可用
```

### 4.3 使用 SDK 安装

```java
// 安装技能（远程托管模式）
InstallResult result = sdk.installSkill("skill-org-feishu")
    .mode(InstallMode.REMOTE_HOSTED)
    .execute();

// 安装技能（本地部署模式）
InstallResult result = sdk.installSkill("skill-org-feishu")
    .mode(InstallMode.LOCAL_DEPLOYED)
    .config("FEISHU_APP_ID", "your-app-id")
    .config("FEISHU_APP_SECRET", "your-app-secret")
    .execute();
```

---

## 五、如何配置技能

### 5.1 配置方式

技能配置支持以下方式：

| 方式 | 说明 | 示例 |
|------|------|------|
| 环境变量 | 通过环境变量传递配置 | `FEISHU_APP_ID=xxx` |
| 配置文件 | 通过 YAML/JSON 文件配置 | `application.yml` |
| SDK API | 通过代码动态配置 | `sdk.config(key, value)` |

### 5.2 配置示例

**飞书组织技能配置：**

```yaml
# application.yml
feishu:
  app-id: ${FEISHU_APP_ID}
  app-secret: ${FEISHU_APP_SECRET}
  api-base-url: https://open.feishu.cn/open-apis
  timeout: 30000
```

**钉钉组织技能配置：**

```yaml
# application.yml
dingding:
  app-key: ${DINGDING_APP_KEY}
  app-secret: ${DINGDING_APP_SECRET}
  api-base-url: https://oapi.dingtalk.com
```

### 5.3 敏感配置

敏感配置（如密钥、密码）应使用环境变量或密钥管理服务：

```bash
# 设置环境变量
export FEISHU_APP_SECRET="your-secret"
export DINGDING_APP_SECRET="your-secret"
```

---

## 六、如何使用技能

### 6.1 请求场景服务

推荐通过场景请求技能服务，SDK 会自动处理发现和连接：

```java
// 请求场景服务
SceneJoinResult result = sdk.requestScene("auth")
    .capabilities("org-data-read", "user-auth")
    .join();

if (result.isJoined()) {
    // 获取连接信息
    String endpoint = result.getConnectionInfo().get("endpoint");
    String apiKey = result.getConnectionInfo().get("apiKey");
    
    // 使用技能服务
    OrgClient orgClient = new OrgClient(endpoint, apiKey);
    Org org = orgClient.getOrgTree();
}
```

### 6.2 直接调用技能 API

如果知道技能的具体端点，可以直接调用：

```java
// 获取技能连接信息
SkillConnection conn = sdk.getSkillConnection("skill-org-feishu");

// 调用 API
HttpResponse response = HttpClient.get(conn.getEndpoint() + "/api/org/tree")
    .header("X-API-Key", conn.getApiKey())
    .execute();
```

### 6.3 通过 SDK 代理调用

SDK 提供代理接口，简化调用：

```java
// 获取技能代理
OrgSkillProxy orgProxy = sdk.getSkillProxy("skill-org-feishu", OrgSkillProxy.class);

// 调用方法
Org org = orgProxy.getOrgTree();
Person person = orgProxy.getPerson("user-001");
```

---

## 七、技能组合

### 7.1 多技能协作

一个应用可以使用多个技能：

```java
// 同时使用组织技能和消息技能
SceneJoinResult authResult = sdk.requestScene("auth").join();
SceneJoinResult msgResult = sdk.requestScene("messaging").join();

// 组织数据 + 消息发送
OrgSkillProxy orgProxy = sdk.getSkillProxy("skill-org-feishu", OrgSkillProxy.class);
MsgSkillProxy msgProxy = sdk.getSkillProxy("skill-msg", MsgSkillProxy.class);

// 获取组织成员并发送通知
List<Person> members = orgProxy.getOrgMembers("dept-001");
for (Person member : members) {
    msgProxy.sendMessage(member.getId(), "系统通知：...");
}
```

### 7.2 技能依赖

某些技能可能依赖其他技能，SDK 会自动处理：

```
skill-msg (消息技能)
    │
    └── 依赖 skill-vfs (存储技能) - 可选
            │
            └── 用于存储消息附件
```

---

## 八、常见场景示例

### 8.1 场景：企业组织集成

**需求：** 将飞书组织数据集成到应用中

**步骤：**

1. 安装技能
```java
sdk.installSkill("skill-org-feishu")
    .mode(InstallMode.LOCAL_DEPLOYED)
    .config("FEISHU_APP_ID", "cli_xxx")
    .config("FEISHU_APP_SECRET", "xxx")
    .execute();
```

2. 请求场景
```java
SceneJoinResult result = sdk.requestScene("auth").join();
```

3. 使用服务
```java
OrgSkillProxy proxy = sdk.getSkillProxy("skill-org-feishu", OrgSkillProxy.class);
Org org = proxy.getOrgTree();
```

### 8.2 场景：消息通知

**需求：** 通过钉钉发送消息通知

**步骤：**

1. 安装消息技能
```java
sdk.installSkill("skill-msg")
    .mode(InstallMode.LOCAL_DEPLOYED)
    .config("MSG_DINGDING_ENABLED", "true")
    .config("DINGDING_WEBHOOK", "https://oapi.dingtalk.com/robot/send?access_token=xxx")
    .execute();
```

2. 发送消息
```java
MsgSkillProxy proxy = sdk.getSkillProxy("skill-msg", MsgSkillProxy.class);
proxy.sendMessage("user-001", "您有新的待办任务");
```

### 8.3 场景：文档库集成

**需求：** 集成 Confluence 文档库

**步骤：**

1. 安装文档技能
```java
sdk.installSkill("skill-doc-confluence")
    .mode(InstallMode.LOCAL_DEPLOYED)
    .config("CONFLUENCE_URL", "https://wiki.company.com")
    .config("CONFLUENCE_USERNAME", "api-user")
    .config("CONFLUENCE_API_TOKEN", "xxx")
    .execute();
```

2. 搜索文档
```java
DocSkillProxy proxy = sdk.getSkillProxy("skill-doc-confluence", DocSkillProxy.class);
List<Document> docs = proxy.searchDocuments("项目文档");
```

---

## 九、故障排除

### 9.1 技能无法发现

**可能原因：**
- 网络不通
- 技能未启动
- 场景不存在

**解决方案：**
```java
// 检查技能状态
SkillStatus status = sdk.getSkillStatus("skill-org-feishu");
if (status == SkillStatus.NOT_FOUND) {
    // 从 SkillCenter 安装
    sdk.installSkill("skill-org-feishu").execute();
}
```

### 9.2 技能连接失败

**可能原因：**
- 配置错误
- 认证失败
- 服务不可用

**解决方案：**
```java
// 检查连接
ConnectionTestResult result = sdk.testConnection("skill-org-feishu");
if (!result.isSuccess()) {
    System.out.println("连接失败: " + result.getError());
}
```

### 9.3 技能响应慢

**可能原因：**
- 网络延迟
- 技能负载高
- 配置不当

**解决方案：**
```java
// 检查技能健康状态
HealthStatus health = sdk.getSkillHealth("skill-org-feishu");
System.out.println("响应时间: " + health.getResponseTime() + "ms");
```

---

## 十、最佳实践

### 10.1 使用场景而非直接连接

推荐：
```java
SceneJoinResult result = sdk.requestScene("auth").join();
```

不推荐：
```java
String endpoint = "192.168.1.100:8080";  // 硬编码
```

### 10.2 敏感配置使用环境变量

推荐：
```yaml
feishu:
  app-secret: ${FEISHU_APP_SECRET}
```

不推荐：
```yaml
feishu:
  app-secret: "hardcoded-secret"  # 不要硬编码
```

### 10.3 处理技能不可用情况

```java
try {
    SceneJoinResult result = sdk.requestScene("auth").join();
} catch (SceneNotFoundException e) {
    // 场景不存在，提示用户安装
    System.out.println("请先安装组织技能");
}
```

---

## 十一、附录

### A. 常用技能列表

| 技能ID | 名称 | 类型 | 提供的能力 |
|--------|------|------|-----------|
| skill-org-feishu | 飞书组织服务 | 企业级 | org-data-read, user-auth |
| skill-org-dingding | 钉钉组织服务 | 企业级 | org-data-read, user-auth |
| skill-org-qiwei | 企业微信组织服务 | 企业级 | org-data-read, user-auth |
| skill-msg | 统一消息服务 | 企业级 | send-message, receive-message |
| skill-vfs | VFS存储服务 | 基础设施 | file-read, file-write |
| skill-email | 邮件服务 | 工具类 | send-email |

### B. 常用场景列表

| 场景 | 描述 | 常用技能 |
|------|------|----------|
| auth | 认证场景 | skill-org-feishu, skill-org-dingding |
| messaging | 消息场景 | skill-msg |
| document | 文档场景 | skill-doc-confluence, skill-doc-notion |
| storage | 存储场景 | skill-vfs |

### C. 错误码参考

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| SKILL_001 | 技能未找到 | 安装对应技能 |
| SKILL_002 | 版本不兼容 | 更新技能版本 |
| SKILL_003 | 配置缺失 | 补充必要配置 |
| SKILL_004 | 认证失败 | 检查密钥配置 |
| SKILL_005 | 连接超时 | 检查网络状态 |

---

## 联系我们

- 官网：https://ooder.net
- 文档：https://docs.ooder.net
- GitHub：https://github.com/ooder
- 问题反馈：https://github.com/ooder/agent-sdk/issues

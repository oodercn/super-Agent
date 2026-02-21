# Ooder Agent SDK 0.7.0 协同工作文档

## 版本信息

| 项目 | 版本 |
|------|------|
| SDK版本 | 0.7.0 |
| 协议版本 | v0.7.0 |
| 发布日期 | 2026-02-14 |

---

## 一、版本升级指南

### 1.1 Maven 依赖

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.7.0</version>
</dependency>
```

### 1.2 新增模块

SDK 0.7.0 新增技能包管理模块：

```
net.ooder.sdk.skill.packageManager/
├── SkillPackageManager          # 技能包管理器（主入口）
├── SkillPackageObserver         # 观察者接口
├── model/                       # 数据模型
│   ├── InstallRequest
│   ├── InstallResult
│   ├── SkillPackage
│   ├── SceneJoinResult
│   └── ...
├── registry/                    # 本地注册表
├── client/                      # SkillCenter客户端
└── installer/                   # 安装器
```

### 1.3 API 变更

**新增接口：**

```java
// 技能包管理器
SkillPackageManager manager = new SkillPackageManagerImpl(basePath, skillCenterUrl);

// 安装技能
InstallResult result = manager.installSkill(
    InstallRequest.builder()
        .skillId("skill-org-feishu")
        .mode(InstallMode.LOCAL_DEPLOYED)
        .config("FEISHU_APP_ID", "xxx")
        .config("FEISHU_APP_SECRET", "xxx")
        .build()
).get();

// 请求场景
SceneJoinResult sceneResult = manager.requestScene(
    SceneRequest.builder()
        .sceneName("auth")
        .capability("org-data-read")
        .build()
).get();

// 获取连接信息
SkillConnectionInfo conn = manager.getSkillConnection("skill-org-feishu");
```

### 1.4 兼容性

- 完全向后兼容 0.6.x 版本
- 新增模块不影响现有功能
- 建议所有工程升级到 0.7.0

---

## 二、关键设计

### 2.1 技能包管理

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         技能包管理架构                                        │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Nexus应用     │────▶│ SkillPackageManager │────▶│  SkillCenter   │
│                 │     │                 │     │                 │
│ requestScene()  │     │ install()       │     │ 技能仓库        │
│ installSkill()  │     │ discover()      │     │ 元数据管理      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                               │
                               ▼
                        ┌─────────────────┐
                        │   VFS 存储      │
                        │                 │
                        │ skills/         │
                        │ ├── installed/  │
                        │ ├── cache/      │
                        │ └── registry.json│
                        └─────────────────┘
```

### 2.2 场景与组

```
场景（静态）                    组（运行时）
┌─────────────────┐            ┌─────────────────┐
│ auth 场景       │            │ auth-group-001  │
│                 │            │                 │
│ 能力:           │            │ 成员:           │
│ - org-data-read │◀──────────▶│ - RouteAgent    │
│ - user-auth     │            │ - EndAgent 1    │
│                 │            │ - EndAgent 2    │
│ 角色:           │            │                 │
│ - org-provider  │            │ 连接信息:       │
│ - org-consumer  │            │ - endpoint      │
└─────────────────┘            │ - apiKey        │
                               └─────────────────┘
```

### 2.3 两种部署模式

| 模式 | 说明 | 适用场景 |
|------|------|----------|
| **远程托管** | 技能运行在SkillCenter，只需配置连接信息 | 快速部署、无需维护 |
| **本地部署** | 技能下载到本地运行 | 数据安全、内网环境 |

---

## 三、文档位置

### 3.1 协议文档

| 文档 | 路径 | 说明 |
|------|------|------|
| 技能包协议 | [protocol-release/v0.7.0/skill/skill-package-protocol.md](protocol-release/v0.7.0/skill/skill-package-protocol.md) | 打包格式、分发机制 |
| 技能清单规范 | [protocol-release/v0.7.0/skill/skill-manifest-spec.md](protocol-release/v0.7.0/skill/skill-manifest-spec.md) | skill.yaml 结构 |
| 技能发现协议 | [protocol-release/v0.7.0/skill/skill-discovery-protocol.md](protocol-release/v0.7.0/skill/skill-discovery-protocol.md) | 注册、发现、心跳 |

### 3.2 用户文档

| 文档 | 路径 | 说明 |
|------|------|------|
| 用户说明书 | [docs/skills/SKILLS-README.md](docs/skills/SKILLS-README.md) | 面向终端用户和AI |
| 场景设计规范 | [docs/skills/SCENE-DESIGN.md](docs/skills/SCENE-DESIGN.md) | 面向开发者 |
| 运行时规范 | [docs/skills/SKILL-RUNTIME.md](docs/skills/SKILL-RUNTIME.md) | 面向运维 |

### 3.3 技能文档

| 技能 | SKILLS.md 路径 |
|------|----------------|
| 飞书组织 | [skill-org-feishu/SKILLS.md](skill-org-feishu/SKILLS.md) |
| 钉钉组织 | [skill-org-dingding/SKILLS.md](skill-org-dingding/SKILLS.md) |

---

## 四、工程任务分配

### 4.1 主工程（已完成）

| 任务 | 状态 | 说明 |
|------|------|------|
| 协议文档编写 | ✅ 完成 | v0.7.0 协议规范 |
| 用户文档编写 | ✅ 完成 | 三层文档体系 |
| SDK核心开发 | ✅ 完成 | SkillPackageManager |
| 技能SKILLS.md | ✅ 完成 | 飞书、钉钉模板 |
| 版本升级 | ✅ 完成 | 0.6.6 → 0.7.0 |
| 本地发布 | ✅ 完成 | mvn install |

### 4.2 SkillCenter 工程（待同步）

| 任务 | 说明 |
|------|------|
| 技能目录API | 实现 `/api/skills` 接口 |
| 技能搜索API | 实现 `/api/skills/search` 接口 |
| 技能下载API | 实现 `/api/skills/{id}/download` 接口 |
| 技能元数据管理 | 存储技能清单和版本信息 |

**参考文档：** [skill-discovery-protocol.md](protocol-release/v0.7.0/skill/skill-discovery-protocol.md)

### 4.3 Nexus 工程（待同步）

| 任务 | 说明 |
|------|------|
| 升级SDK依赖 | 更新到 0.7.0 |
| 集成SkillPackageManager | 添加技能管理功能 |
| 场景请求示例 | 实现 auth 场景集成 |
| 配置管理 | 支持技能配置注入 |

**参考文档：** [SKILLS-README.md](docs/skills/SKILLS-README.md)

### 4.4 技能工程（待同步）

| 任务 | 说明 |
|------|------|
| 添加skill.yaml | 按规范创建技能清单 |
| 完善SKILLS.md | 补充配置示例和API文档 |
| 场景定义 | 创建 scenes/*.yaml |

**参考文档：** [SCENE-DESIGN.md](docs/skills/SCENE-DESIGN.md)

---

## 五、集成测试清单

### 5.1 SkillCenter 测试

- [ ] 技能列表接口正常
- [ ] 技能搜索接口正常
- [ ] 技能下载接口正常
- [ ] 版本管理接口正常

### 5.2 SDK 测试

- [ ] 技能发现功能正常
- [ ] 技能安装功能正常（远程托管）
- [ ] 技能安装功能正常（本地部署）
- [ ] 场景请求功能正常
- [ ] 连接信息获取正常

### 5.3 Nexus 集成测试

- [ ] 飞书组织技能集成
- [ ] 钉钉组织技能集成
- [ ] 场景组自动加入
- [ ] 配置自动注入

---

## 六、联系方式

- **问题反馈：** https://github.com/ooder/agent-sdk/issues
- **技术文档：** https://docs.ooder.net
- **技能市场：** https://skillcenter.ooder.net

---

## 七、更新日志

### 0.7.0 (2026-02-14)

**新增：**
- 技能包管理模块 (SkillPackageManager)
- 技能发现协议支持
- 场景请求机制
- 本地技能注册表
- SkillCenter 客户端

**文档：**
- 技能包协议文档
- 技能清单规范
- 技能发现协议
- 用户说明书
- 场景设计规范
- 运行时规范

**升级：**
- 版本从 0.6.6 升级到 0.7.0
- 添加 jackson-dataformat-yaml 依赖

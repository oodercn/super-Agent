# ApexOS MVP 源码与依赖优化报告

## 1. 源码文件分析

### 1.1 Controller 层分析

| 文件 | 状态 | 说明 | 建议 |
|------|------|------|------|
| `HomeController.java` | ✅ 必须 | 首页控制器 | 保留 |
| `FaviconController.java` | ✅ 必须 | 图标控制器 | 保留 |
| `AuthMenuController.java` | ✅ 必须 | 菜单认证 | 保留 |
| `MvpAuthController.java` | ✅ 必须 | MVP认证 | 保留 |
| `LlmController.java` | ✅ 必须 | LLM服务 | 保留 |
| `SkillController.java` | ✅ 必须 | 技能管理 | 保留 |
| `InstallApiController.java` | ✅ 必须 | 安装API | 保留 |
| `SelectorsController.java` | ✅ 必须 | 选择器API | 保留 |
| `ConfigController.java` | ⚠️ 可选 | 配置元数据(mock) | 保留(前端调用) |
| `CapabilityStatsController.java` | ⚠️ 可选 | 能力统计 | 保留(前端调用) |
| `RoleManagementController.java` | ⚠️ 可选 | 角色管理 | 保留(前端调用) |
| `SceneCapabilitiesController.java` | ❌ 非必须 | 返回mock数据 | **可删除** |
| `SceneCapabilityController.java` | ❌ 非必须 | 返回mock数据 | **可删除** |

### 1.2 Config 层分析

| 文件 | 状态 | 说明 | 建议 |
|------|------|------|------|
| `WebConfig.java` | ✅ 必须 | Web配置 | 保留 |
| `AuthConfig.java` | ✅ 必须 | 认证配置 | 保留 |
| `OsJpaConfiguration.java` | ✅ 必须 | JPA配置 | 保留 |
| `PluginInitializer.java` | ✅ 必须 | 插件初始化 | 保留 |
| `OsGlobalExceptionHandler.java` | ✅ 必须 | 异常处理 | 保留 |
| `SkillsFrameworkConfig.java` | ✅ 必须 | 技能框架配置 | 保留 |
| `SeSdkConfig.java` | ✅ 必须 | SE SDK配置 | 保留 |
| `KnowledgeConfig.java` | ⚠️ 可选 | 知识库配置 | 保留 |
| `UiConfigAutoConfiguration.java` | ⚠️ 可选 | UI配置 | 保留 |
| `WebSocketAuthConfig.java` | ⚠️ 可选 | WebSocket认证 | 保留 |
| `InMemoryKnowledgeBindingManager.java` | ⚠️ 可选 | 内存知识绑定 | 保留 |
| `InMemorySceneGroupPersistence.java` | ⚠️ 可选 | 内存场景持久化 | 保留 |
| `InMemorySceneLlmConfigManager.java` | ⚠️ 可选 | 内存LLM配置 | 保留 |

### 1.3 DTO 层分析

| 目录 | 文件数 | 状态 | 说明 |
|------|--------|------|------|
| `dto/capability/` | 7个 | ⚠️ 部分使用 | 能力统计DTO |
| `dto/config/` | 5个 | ⚠️ 部分使用 | 配置DTO |
| `dto/llm/` | 10个 | ✅ 使用中 | LLM相关DTO |
| `dto/menu/` | 2个 | ✅ 使用中 | 菜单DTO |
| `dto/role/` | 1个 | ⚠️ 部分使用 | 角色DTO |
| `dto/selector/` | 1个 | ✅ 使用中 | 选择器DTO |
| `dto/skill/` | 2个 | ✅ 使用中 | 技能DTO |

### 1.4 LLM 层分析

| 文件 | 状态 | 说明 |
|------|------|------|
| `LLMService.java` | ✅ 必须 | LLM服务接口 |
| `LLMRequest.java` | ✅ 必须 | 请求模型 |
| `LLMResponse.java` | ✅ 必须 | 响应模型 |
| `FunctionCall.java` | ✅ 必须 | 函数调用 |
| `FunctionResult.java` | ✅ 必须 | 函数结果 |
| `AliyunBailianLlmProvider.java` | ✅ 必须 | 阿里云百炼 |
| `BaiduLlmProvider.java` | ✅ 必须 | 百度千帆 |
| `DeepSeekLlmProvider.java` | ✅ 必须 | DeepSeek |
| `LlmProviderManager.java` | ✅ 必须 | 提供者管理 |
| `LlmProviderManagerImpl.java` | ✅ 必须 | 提供者实现 |

---

## 2. POM 依赖优化

### 2.1 当前依赖状态

**必须保留的依赖：**
- Spring Boot: web, actuator, validation, aop, freemarker
- ooder核心: agent-sdk-core, skill-common, scene-engine, skill-hotplug-starter, skills-framework, ooder-bpm-web
- 工具库: lombok, fastjson2, jackson, mvel2, sqlite-jdbc, jgit, github-api

### 2.2 可移除的依赖

以下依赖在源码中没有直接使用，可以移除：

| 依赖 | 当前状态 | 使用情况 | 建议 |
|------|---------|---------|------|
| `skill-rag` | provided | 无import | **移除** |
| `skill-im-gateway` | provided | 无import | **移除** |
| `skill-tenant` | provided | 无import | **移除** |
| `skill-org` | provided | 无import | **移除** |
| `skill-dict` | provided | 无import | **移除** |
| `skill-scenes` | provided | 无import | **移除** |
| `skill-workflow` | provided | 无import | **移除** |
| `skill-notification` | provided | 无import | **移除** |
| `skill-spi-messaging` | provided | 无import | **移除** |

---

## 3. 数据配置分析

### 3.1 application.yml 配置项

| 配置项 | 状态 | 说明 |
|--------|------|------|
| `server.port` | ✅ 必须 | 服务端口 |
| `spring.jpa` | ✅ 必须 | JPA配置 |
| `os.datasource` | ✅ 必须 | 数据源配置 |
| `ooder.discovery` | ✅ 必须 | 技能发现配置 |
| `ooder.skill.hotplug` | ✅ 必须 | 插件热加载配置 |
| `ooder.llm` | ✅ 必须 | LLM配置 |
| `scene.engine` | ✅ 必须 | 场景引擎配置 |
| `auth.qrcode` | ⚠️ 可选 | 二维码登录(默认关闭) |
| `capability.publish` | ⚠️ 可选 | 能力发布配置 |

### 3.2 config 目录文件

| 文件 | 状态 | 说明 |
|------|------|------|
| `application.yml` | ✅ 必须 | 主配置文件 |
| `profiles/enterprise.json` | ⚠️ 可选 | 企业版配置 |
| `profiles/large.json` | ⚠️ 可选 | 大规模配置 |
| `profiles/micro.json` | ⚠️ 可选 | 微服务配置 |
| `profiles/small.json` | ⚠️ 可选 | 小规模配置 |
| `system-config.json` | ⚠️ 可选 | 系统配置 |

---

## 4. 优化建议

### 4.1 可删除的源码文件

```
src/main/java/net/ooder/os/controller/SceneCapabilitiesController.java
src/main/java/net/ooder/os/controller/SceneCapabilityController.java
```

### 4.2 可移除的POM依赖

以下9个provided scope依赖可从pom.xml中移除：

```xml
<!-- 可移除的依赖 -->
skill-rag, skill-im-gateway, skill-tenant, skill-org, 
skill-dict, skill-scenes, skill-workflow, skill-notification, skill-spi-messaging
```

### 4.3 优化后的效果预估

| 指标 | 优化前 | 优化后 | 减少 |
|------|--------|--------|------|
| POM依赖数 | 25个 | 16个 | 9个 |
| 源码文件数 | ~90个 | ~88个 | 2个 |
| 配置复杂度 | 高 | 中 | - |

---

**报告生成时间**: 2026-04-08  
**项目路径**: `e:\apex\apexos`  
**版本**: v1.0.0

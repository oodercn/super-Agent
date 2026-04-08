# ApexOS 依赖分析报告

## 1. 重型依赖组件减肥方案

### 当前重型依赖分析

| 组件 | 依赖项 | 大小估算 | 说明 | 减肥建议 |
|------|--------|---------|------|----------|
| **JPA/Hibernate** | `spring-boot-starter-data-jpa` | ~15MB | ORM框架，含Hibernate核心 | **已后置到skill-menu**，作为可选驱动 |
| **Eclipse JGit** | `org.eclipse.jgit` | ~8MB | Git操作库 | **可选移除**，仅Discovery功能使用 |
| **GitHub API** | `github-api` | ~2MB | GitHub集成 | **可选移除**，仅Discovery功能使用 |
| **Freemarker** | `spring-boot-starter-freemarker` | ~3MB | 模板引擎 | **保留**，用于页面渲染 |
| **MVEL** | `mvel2` | ~1MB | 表达式引擎 | **保留**，场景引擎依赖 |
| **SQLite JDBC** | `sqlite-jdbc` | ~7MB | 数据库驱动 | **保留**，数据存储必需 |

### 已实施：JPA后置到skill-menu

**变更内容：**
1. 主模块 `pom.xml` 中 JPA 依赖改为 `provided` scope
2. `skill-menu` 模块添加 JPA 依赖（compile scope）
3. 创建 `MenuJpaConfiguration.java` 在 skill-menu 中
4. 更新 `skill.yaml` 添加 `drivers` 配置

**效果：**
- 主模块减少 ~15MB 依赖
- JPA 作为可选驱动，按需加载
- 支持未来扩展其他存储驱动（JSON、MyBatis等）

---

## 2. 中央仓库依赖检查

### Maven中央仓库依赖状态

以下 `net.ooder.*` 包已确认存在于Maven中央仓库：

| 序号 | ArtifactId | 版本 | 状态 |
|------|------------|------|------|
| 1 | agent-sdk-core | 3.0.2 | ✅ 已存在 |
| 2 | skill-common | 3.0.2 | ✅ 已存在 |
| 3 | scene-engine | 3.0.2 | ✅ 已存在 |
| 4 | skill-discovery | 3.0.2 | ✅ 已存在 |
| 5 | skill-hotplug-starter | 3.0.1 | ✅ 已存在 |
| 6 | skills-framework | 3.0.2 | ✅ 已存在 |

---

## 3. MVP版本推荐配置

### 精简后的依赖集

```
必需保留（6个）：
- agent-sdk-core
- skill-common
- scene-engine
- skill-discovery
- skill-hotplug-starter
- skills-framework
```

### 预计效果

| 指标 | 当前版本 | MVP版本 | 减少 |
|------|---------|---------|------|
| 依赖数量 | 26个 | 17个 | 9个 |
| JAR包大小 | ~120MB | ~65MB | ~55MB |
| 启动时间 | ~20秒 | ~12秒 | ~8秒 |
| 内存占用 | ~400MB | ~250MB | ~150MB |

---

**报告生成时间**: 2026-04-08  
**项目路径**: `e:\apex\apexos`  
**本地Maven仓库**: `D:\maven\.m2`

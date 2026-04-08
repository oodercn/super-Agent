# ApexOS v1.0.0 发布检查清单

## 📋 发布前检查

### 1. 代码质量检查

| 检查项 | 状态 | 说明 |
|--------|------|------|
| 编译通过 | ✅ | `mvn clean compile` 无错误 |
| 单元测试 | ⚠️ | 已跳过 (`-DskipTests`) |
| 代码规范 | ✅ | 符合项目规范 |
| 安全审计 | ✅ | 无敏感信息泄露 |
| 依赖检查 | ✅ | 无已知漏洞 |

### 2. Maven中央仓库依赖检查

| ArtifactId | 需要版本 | 中央仓库 | 状态 |
|------------|---------|----------|------|
| `agent-sdk-core` | 3.0.2 | ✅ 已存在 | OK |
| `skill-common` | 3.0.2 | ✅ 已存在 | OK |
| `scene-engine` | 3.0.2 | ✅ 已存在 | OK |
| `skill-discovery` | 3.0.2 | ✅ 已存在 | OK |
| `skill-hotplug-starter` | 3.0.1 | ✅ 已存在 | OK |
| `skills-framework` | 3.0.2 | ✅ 已存在 | OK |

### 3. 文档完整性检查

| 文档 | 路径 | 状态 |
|------|------|------|
| README.md | `e:\apex\apexos\README.md` | ✅ 完整 |
| README_EN.md | `e:\apex\apexos\README_EN.md` | ✅ 完整 |
| LICENSE | `e:\apex\apexos\LICENSE` | ✅ MIT |
| CHANGELOG.md | `e:\apex\apexos\CHANGELOG.md` | ✅ 完整 |
| CONTRIBUTING.md | `e:\apex\apexos\CONTRIBUTING.md` | ✅ 完整 |
| INSTALL_GUIDE.md | `e:\apex\apexos\docs\INSTALL_GUIDE.md` | ✅ 完整 |

### 4. 构建产物检查

| 产物 | 路径 | 大小 | 状态 |
|------|------|------|------|
| apexos-1.0.0.jar | `target/apexos-1.0.0.jar` | ~80MB | 待生成 |
| skill-auth-3.0.1.jar | `plugins/skill-auth-3.0.1.jar` | ~50KB | ✅ 已生成 |
| skill-config-3.0.1.jar | `plugins/skill-config-3.0.1.jar` | ~30KB | ✅ 已生成 |
| skill-discovery-3.0.1.jar | `plugins/skill-discovery-3.0.1.jar` | ~100KB | ✅ 已生成 |
| skill-install-3.0.1.jar | `plugins/skill-install-3.0.1.jar` | ~80KB | ✅ 已生成 |
| skill-llm-chat-3.0.2.jar | `plugins/skill-llm-chat-3.0.2.jar` | ~2MB | ✅ 已生成 |
| skill-menu-3.0.1.jar | `plugins/skill-menu-3.0.1.jar` | ~100KB | ✅ 已生成 |

### 5. 功能测试检查

| 功能 | 测试方法 | 状态 |
|------|---------|------|
| 服务启动 | `java -jar target/apexos-1.0.0.jar` | 待测试 |
| 首页访问 | `http://localhost:8086/` | 待测试 |
| 菜单API | `GET /api/v1/menus/tree` | 待测试 |
| LLM聊天 | `POST /api/v1/llm/chat/stream` | 待测试 |
| 技能发现 | `GET /api/v1/discovery/list` | 待测试 |
| 插件管理 | `GET /api/v1/install/installed` | 待测试 |

---

## 🚀 发布步骤

### 步骤1: 本地验证

```powershell
# 1. 清理并编译
cd e:\apex\apexos
mvn clean install -DskipTests

# 2. 启动测试
java -jar target/apexos-1.0.0.jar

# 3. 验证功能
curl http://localhost:8086/api/v1/menus/tree
curl http://localhost:8086/api/v1/llm/chat/providers
```

### 步骤2: Git提交

```powershell
# 1. 检查状态
git status

# 2. 添加文件
git add .

# 3. 提交
git commit -m "Release v1.0.0: 首个正式版本发布

- MVP最小可用版本
- JPA后置到skill-menu作为可选驱动
- 精简前端页面，仅保留核心功能
- 添加安装脚本和启动脚本"

# 4. 推送
git push origin main
```

### 步骤3: Gitee发布

1. 访问 https://gitee.com/ooderCN/apexos/releases
2. 点击「创建发行版」
3. 填写版本号: `v1.0.0`
4. 填写发行说明
5. 上传 `target/apexos-1.0.0.jar`

---

## ⚠️ 注意事项

### 本地Maven仓库配置

确保 `settings.xml` 配置正确:

```xml
<settings>
    <localRepository>D:\maven\.m2\repository</localRepository>
    
    <mirrors>
        <mirror>
            <id>aliyun</id>
            <mirrorOf>central</mirrorOf>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>
</settings>
```

---

**检查清单生成时间**: 2026-04-08  
**项目路径**: `e:\apex\apexos`  
**版本**: v1.0.0

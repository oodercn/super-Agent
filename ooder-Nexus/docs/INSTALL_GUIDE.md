# ApexOS v1.0.0 安装发布文档

## 📦 发布信息

| 项目 | 信息 |
|------|------|
| 版本号 | v1.0.0 |
| 发布日期 | 2026-04-08 |
| 许可证 | MIT |
| 仓库地址 | https://gitee.com/ooderCN/apexos |
| JAR包大小 | ~65MB |
| 最低内存要求 | 512MB |

---

## 📋 环境要求

### 系统要求

| 项目 | 要求 |
|------|------|
| 操作系统 | Windows 10+ / Linux / macOS 10.15+ |
| Java | JDK 21 或更高版本 |
| Maven | 3.6+ (仅源码编译需要) |
| 内存 | 至少 512MB 可用内存 |
| 磁盘 | 至少 500MB 可用空间 |

### 验证环境

```powershell
# Windows
java -version
mvn -version

# Linux/macOS
java -version
mvn -version
```

---

## 🚀 快速开始

### 方式一: 下载预编译版本（推荐）

#### 1. 下载发布版本

从 Gitee 下载最新版本:

```powershell
# Windows PowerShell
Invoke-WebRequest -Uri "https://gitee.com/ooderCN/apexos/releases/download/v1.0.0/apex-os-1.0.0.jar" -OutFile "apex-os-1.0.0.jar"

# Linux/macOS
wget https://gitee.com/ooderCN/apexos/releases/download/v1.0.0/apex-os-1.0.0.jar
```

#### 2. 启动应用

```powershell
java -jar apex-os-1.0.0.jar
```

#### 3. 访问系统

打开浏览器访问: http://localhost:8086

- 默认用户名: `admin`
- 默认密码: `admin123`

---

### 方式二: 从源码构建

#### 1. 克隆项目

```powershell
git clone https://gitee.com/ooderCN/apexos.git
cd apexos
```

#### 2. 编译项目

```powershell
mvn clean install -DskipTests
```

#### 3. 启动应用

```powershell
java -jar target/apex-os-1.0.0.jar
```

---

## ⚙️ 配置说明

### 环境变量配置

ApexOS 使用环境变量来配置敏感信息:

#### LLM 服务配置

```powershell
# Windows PowerShell
$env:QIANWEN_API_KEY="your_qianwen_api_key"
$env:DEEPSEEK_API_KEY="your_deepseek_api_key"
$env:BAIDU_API_KEY="your_baidu_api_key"
$env:BAIDU_SECRET_KEY="your_baidu_secret_key"

# Linux/macOS
export QIANWEN_API_KEY=your_qianwen_api_key
export DEEPSEEK_API_KEY=your_deepseek_api_key
export BAIDU_API_KEY=your_baidu_api_key
export BAIDU_SECRET_KEY=your_baidu_secret_key
```

#### Gitee 配置

```powershell
# Windows PowerShell
$env:GITEE_TOKEN="your_gitee_token"

# Linux/macOS
export GITEE_TOKEN=your_gitee_token
```

### 配置文件

配置文件位于: `src/main/resources/application.yml`

```yaml
server:
  port: 8086

ooder:
  llm:
    provider: qianwen
    model: qwen-plus

  skills:
    gitee:
      owner: ooderCN
      repo: skills
      branch: master
```

---

## 📁 目录结构

```
apexos/
├── target/
│   └── apex-os-1.0.0.jar    # 主程序JAR包
├── plugins/                  # 插件目录
│   ├── skill-auth-3.0.1.jar
│   ├── skill-config-3.0.1.jar
│   ├── skill-discovery-3.0.1.jar
│   ├── skill-install-3.0.1.jar
│   ├── skill-llm-chat-3.0.2.jar
│   └── skill-menu-3.0.1.jar
├── data/                     # 数据目录
│   ├── os.db                 # SQLite数据库
│   ├── menu.db               # 菜单数据库
│   └── template.db           # 模板数据库
├── logs/                     # 日志目录
│   └── ooder-sdk.log
├── skills/                   # 技能源码目录
│   └── _system/              # 系统技能
├── docs/                     # 文档目录
│   └── INSTALL_GUIDE.md
├── README.md                 # 项目说明
├── LICENSE                   # MIT许可证
├── CHANGELOG.md              # 变更日志
├── DEPENDENCY_ANALYSIS.md    # 依赖分析
└── RELEASE_CHECKLIST.md      # 发布检查清单
```

---

## 🔧 MVP功能模块

### 核心模块（6个）

| 模块 | 说明 | API端点 |
|------|------|---------|
| skill-auth | 认证服务 | `/api/v1/auth/*` |
| skill-config | 配置管理 | `/api/v1/config/*` |
| skill-discovery | 技能发现 | `/api/v1/discovery/*` |
| skill-install | 技能安装 | `/api/v1/install/*` |
| skill-menu | 菜单管理 | `/api/v1/menus/*` |
| skill-llm-chat | LLM聊天 | `/api/v1/llm/chat/*` |

### 前端页面（8个）

| 页面 | 路径 | 说明 |
|------|------|------|
| 首页 | `/console/pages/dashboard.html` | 系统首页 |
| 能力发现 | `/console/pages/capability-discovery.html` | 技能发现安装 |
| 插件管理 | `/console/pages/plugins-management.html` | 已安装技能 |
| LLM对话 | `/console/pages/workbench.html` | 智能助手 |
| LLM配置 | `/console/pages/llm-config.html` | 大模型配置 |
| 系统配置 | `/console/pages/config-system.html` | 系统设置 |
| 数据库配置 | `/console/pages/db-config.html` | 数据库管理 |
| 登录页面 | `/console/pages/login.html` | 用户登录 |

---

## 🧪 功能测试

### API测试

```powershell
# 1. 测试服务状态
Invoke-WebRequest -Uri "http://localhost:8086/actuator/health"

# 2. 测试菜单API
Invoke-WebRequest -Uri "http://localhost:8086/api/v1/menus/tree"

# 3. 测试LLM提供商
Invoke-WebRequest -Uri "http://localhost:8086/api/v1/llm/chat/providers"

# 4. 测试技能发现
Invoke-WebRequest -Uri "http://localhost:8086/api/v1/discovery/list"

# 5. 测试已安装技能
Invoke-WebRequest -Uri "http://localhost:8086/api/v1/install/installed"
```

### LLM聊天测试

```powershell
$body = '{"message":"你是谁","provider":"qianwen"}'
Invoke-WebRequest -Uri "http://localhost:8086/api/v1/llm/chat/stream" -Method POST -ContentType "application/json" -Body $body
```

---

## 🐛 常见问题

### Q1: 启动失败，提示端口被占用

**解决方案**:

```powershell
# 修改端口
java -jar apex-os-1.0.0.jar --server.port=8087
```

### Q2: 无法连接 Gitee

**解决方案**:

1. 检查网络连接
2. 配置 Gitee Token:

```powershell
$env:GITEE_TOKEN="your_gitee_token"
```

### Q3: LLM 服务不可用

**解决方案**:

```powershell
$env:QIANWEN_API_KEY="your_qianwen_api_key"
```

### Q4: 内存不足

**解决方案**:

```powershell
java -Xms512m -Xmx1024m -jar apex-os-1.0.0.jar
```

### Q5: 数据库错误

**解决方案**:

```powershell
# 删除数据库重新创建
Remove-Item -Path "data/os.db" -Force
Remove-Item -Path "data/menu.db" -Force
```

---

## 📊 性能优化

### JVM 参数优化

```powershell
java -Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar apex-os-1.0.0.jar
```

### 启动时间优化

```yaml
spring:
  main:
    lazy-initialization: true
```

---

## 🔐 安全建议

### 1. 修改默认密码

首次登录后立即修改默认密码。

### 2. 配置 HTTPS

```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: your_password
```

### 3. 配置防火墙

```powershell
# Windows
netsh advfirewall firewall add rule name="ApexOS" dir=in action=allow protocol=tcp localport=8086

# Linux
firewall-cmd --add-port=8086/tcp --permanent
firewall-cmd --reload
```

---

## 📦 发布包内容

| 文件 | 大小 | 说明 |
|------|------|------|
| apex-os-1.0.0.jar | ~65MB | 主程序JAR包 |
| README.md | - | 项目说明文档 |
| LICENSE | - | MIT许可证 |
| CHANGELOG.md | - | 变更日志 |
| DEPENDENCY_ANALYSIS.md | - | 依赖分析报告 |
| RELEASE_CHECKLIST.md | - | 发布检查清单 |

---

## 📝 版本历史

### v1.0.0 (2026-04-08)

#### 新增
- ✨ MVP最小功能集发布
- ✨ JPA后置到skill-menu作为可选驱动
- ✨ 精简前端页面，仅保留MVP功能
- ✨ 添加发布检查清单

#### 优化
- ⚡ 减少约40MB依赖包大小
- ⚡ 启动时间优化约5秒
- ⚡ 内存占用优化约150MB

#### 文档
- 📝 完善白皮书README
- 📝 更新依赖分析报告
- 📝 添加安装发布文档

---

## 🆘 技术支持

### 获取帮助

- 📖 查阅文档: [docs/](docs/)
- 💬 提交 Issue: https://gitee.com/ooderCN/apexos/issues
- 📧 发送邮件: onecode@ooder.cn

### 反馈问题

提交 Issue 时，请提供以下信息:

1. 操作系统和版本
2. Java 版本
3. ApexOS 版本
4. 问题描述
5. 错误日志
6. 重现步骤

---

<div align="center">

**ApexOS v1.0.0 - 开源AI Agent操作系统 MVP版本**

Made with ❤️ by ooder Team

</div>

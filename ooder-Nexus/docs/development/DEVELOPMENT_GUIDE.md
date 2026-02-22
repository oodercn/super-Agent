# ooderNexus 开发指南

## 1. 概述

### 1.1 什么是 ooderNexus？

ooderNexus 是一个基于 **Ooder Agent 架构**的 **P2P AI 能力分发枢纽**，采用 MIT 开源协议。它将去中心化的 P2P 网络技术与 AI 能力管理相结合，让用户能够在本地网络中构建私有的 AI 能力共享平台。

### 1.2 核心特性

- 🤝 **去中心化组网** - 无需中心服务器，节点间直接通信
- 🧠 **AI 技能管理** - 发布、分享、执行 AI 技能
- 🔧 **网络管理中枢** - 可视化网络拓扑和设备管理
- 📡 **OpenWrt 集成** - 深度集成路由器系统
- 🧪 **协议仿真调试** - 离线开发和测试

### 1.3 技术栈

| 层级 | 技术 |
|------|------|
| **后端框架** | Spring Boot 2.7.0 |
| **编程语言** | Java 8+ |
| **P2P网络** | Ooder Agent SDK 0.7.3 |
| **场景引擎** | Scene Engine 0.7.3 |
| **前端** | HTML5 + CSS3 + JavaScript |
| **构建工具** | Maven 3.6+ |
| **测试框架** | JUnit 5 + Mockito |

---

## 2. 开发环境搭建

### 2.1 系统要求

- **操作系统**: Windows 10/11 / macOS / Linux
- **JDK**: 8 或更高版本
- **Maven**: 3.6 或更高版本
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **内存**: 至少 4GB RAM
- **磁盘**: 至少 2GB 可用空间

### 2.2 安装 JDK

**Windows:**
```powershell
# 下载 JDK 8 或 11
# 推荐: Amazon Corretto 或 Eclipse Temurin
choco install corretto8jdk
# 或
choco install temurin8
```

**macOS:**
```bash
brew install --cask corretto
# 或
brew install --cask temurin
```

**Linux:**
```bash
# Ubuntu/Debian
sudo apt-get install openjdk-8-jdk

# CentOS/RHEL
sudo yum install java-1.8.0-openjdk-devel
```

### 2.3 安装 Maven

**Windows:**
```powershell
choco install maven
```

**macOS:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt-get install maven
```

### 2.4 验证安装

```bash
java -version
mvn -version
```

### 2.5 克隆仓库

```bash
# GitHub
git clone https://github.com/oodercn/super-Agent.git

# Gitee 镜像（国内）
git clone https://gitee.com/ooderCN/ooder-agent.git

cd super-Agent/nexus
```

---

## 3. 项目结构

```
ooder-Nexus/
├── src/
│   ├── main/
│   │   ├── java/net/ooder/nexus/           # Java 源代码
│   │   │   ├── NexusSpringApplication.java # 主入口
│   │   │   ├── config/                     # 配置类
│   │   │   ├── controller/                 # REST API 控制器
│   │   │   ├── service/                    # 业务逻辑层
│   │   │   ├── repository/                 # 数据访问层
│   │   │   ├── model/                      # 实体类
│   │   │   ├── core/                       # 核心功能
│   │   │   │   ├── p2p/                    # P2P 网络
│   │   │   │   ├── skill/                  # 技能管理
│   │   │   │   └── storage/                # 存储管理
│   │   │   └── infrastructure/             # 基础设施
│   │   └── resources/
│   │       ├── application.yml             # 主配置文件
│   │       ├── application-dev.yml         # 开发环境配置
│   │       ├── application-prod.yml        # 生产环境配置
│   │       ├── static/                     # 静态资源
│   │       └── console/                    # Web 控制台
│   └── test/                               # 测试代码
├── docs/                                   # 文档
├── release/                                # 发布文件
├── pom.xml                                 # Maven 配置
├── settings.xml                            # Maven 设置
└── README.md                               # 项目说明
```

---

## 4. 构建项目

### 4.1 开发模式构建

```bash
# 编译并运行测试
mvn clean test -s settings.xml

# 打包（跳过测试）
mvn clean package -DskipTests -s settings.xml

# 运行
java -jar target/independent-nexus-2.2.jar
```

### 4.2 生产模式构建

```bash
# 使用生产环境配置
mvn clean package -DskipTests -s settings.xml -P prod

# 生成的 JAR 文件
ls target/*.jar
```

### 4.3 IDE 导入

**IntelliJ IDEA:**
1. File → Open → 选择 `pom.xml`
2. 等待 Maven 依赖下载完成
3. 右键 `NexusSpringApplication.java` → Run

**Eclipse:**
1. File → Import → Existing Maven Projects
2. 选择项目根目录
3. 等待构建完成

**VS Code:**
1. 安装 "Extension Pack for Java"
2. 打开项目文件夹
3. 等待 Java 项目加载完成

---

## 5. 配置说明

### 5.1 主配置文件

**application.yml:**
```yaml
server:
  port: 8081  # HTTP 端口

spring:
  application:
    name: nexus
  profiles:
    active: dev  # 激活的配置文件

# ooderNexus 核心配置
ooder:
  agent:
    id: ${OODER_AGENT_ID:nexus-001}        # Agent ID
    name: ${OODER_AGENT_NAME:nexus}        # Agent 名称
    type: ${OODER_AGENT_TYPE:nexusAgent}   # Agent 类型
  udp:
    port: ${OODER_UDP_PORT:8091}           # UDP 端口
  heartbeat:
    interval: ${OODER_HEARTBEAT_INTERVAL:30000}  # 心跳间隔（毫秒）
    timeout: ${OODER_HEARTBEAT_TIMEOUT:90000}    # 心跳超时（毫秒）
```

### 5.2 环境变量配置

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `OODER_AGENT_ID` | Agent 唯一标识 | nexus-001 |
| `OODER_AGENT_NAME` | Agent 显示名称 | nexus |
| `OODER_AGENT_TYPE` | Agent 类型 | nexusAgent |
| `OODER_UDP_PORT` | UDP 通信端口 | 8091 |
| `SERVER_PORT` | HTTP 服务端口 | 8081 |

### 5.3 开发环境配置

**application-dev.yml:**
```yaml
# 开发环境配置
logging:
  level:
    net.ooder.nexus: DEBUG
    net.ooder.sdk: DEBUG

# 启用调试功能
ooder:
  debug:
    enabled: true
    mock:
      enabled: true  # 启用 Mock 模式
```

---

## 6. 核心模块开发

### 6.1 添加 REST API

**示例：创建新的控制器**

```java
package net.ooder.nexus.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 示例控制器
 */
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    /**
     * GET 请求示例
     */
    @GetMapping("/hello")
    public Map<String, String> hello(@RequestParam String name) {
        return Map.of(
            "message", "Hello, " + name + "!",
            "timestamp", String.valueOf(System.currentTimeMillis())
        );
    }

    /**
     * POST 请求示例
     */
    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> data) {
        data.put("echo", true);
        data.put("timestamp", System.currentTimeMillis());
        return data;
    }
}
```

### 6.2 添加服务层

**示例：创建服务类**

```java
package net.ooder.nexus.service;

import org.springframework.stereotype.Service;

/**
 * 示例服务
 */
@Service
public class ExampleService {

    /**
     * 业务逻辑方法
     */
    public String process(String input) {
        // 业务逻辑
        return "Processed: " + input;
    }
}
```

### 6.3 添加 P2P 技能

**示例：创建技能处理器**

```java
package net.ooder.nexus.core.skill.handler;

import net.ooder.nexus.core.skill.annotation.SkillHandler;
import net.ooder.nexus.core.skill.model.SkillRequest;
import net.ooder.nexus.core.skill.model.SkillResponse;

/**
 * 示例技能处理器
 */
@SkillHandler("example.hello")
public class HelloSkillHandler {

    /**
     * 执行技能
     */
    public SkillResponse execute(SkillRequest request) {
        String name = request.getParam("name", "World");
        
        return SkillResponse.success()
            .withData("message", "Hello, " + name + "!")
            .withData("timestamp", System.currentTimeMillis());
    }
}
```

---

## 7. 测试开发

### 7.1 单元测试

**示例：控制器测试**

```java
package net.ooder.nexus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExampleController.class)
public class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHello() throws Exception {
        mockMvc.perform(get("/api/example/hello")
                .param("name", "Developer"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Hello, Developer!"));
    }
}
```

### 7.2 集成测试

```java
package net.ooder.nexus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NexusIntegrationTest {

    @Test
    public void contextLoads() {
        // 验证 Spring 上下文能正常加载
    }
}
```

### 7.3 运行测试

```bash
# 运行所有测试
mvn test -s settings.xml

# 运行特定测试类
mvn test -Dtest=ExampleControllerTest -s settings.xml

# 生成测试报告
mvn test jacoco:report -s settings.xml
# 报告位置: target/site/jacoco/index.html
```

---

## 8. 调试技巧

### 8.1 IDE 调试

**IntelliJ IDEA:**
1. 在代码行左侧点击设置断点
2. 右键 `NexusSpringApplication.java` → Debug
3. 使用 F8（步过）、F7（步入）、F9（继续）调试

**远程调试:**
```bash
# 启动时添加调试参数
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar target/independent-nexus-2.2.jar
```

### 8.2 日志调试

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleService {
    private static final Logger log = LoggerFactory.getLogger(ExampleService.class);

    public void process() {
        log.debug("调试信息: {}", someVariable);
        log.info("普通信息");
        log.warn("警告信息");
        log.error("错误信息", exception);
    }
}
```

### 8.3 API 测试

**使用 Swagger UI:**
1. 启动应用
2. 访问: http://localhost:8081/swagger-ui.html
3. 直接测试 REST API

**使用 curl:**
```bash
# GET 请求
curl http://localhost:8081/api/example/hello?name=Developer

# POST 请求
curl -X POST http://localhost:8081/api/example/echo \
  -H "Content-Type: application/json" \
  -d '{"key": "value"}'
```

---

## 9. 代码规范

### 9.1 Java 代码规范

遵循 [阿里巴巴 Java 开发手册](https://github.com/alibaba/p3c)：

- 类名使用大驼峰（UpperCamelCase）
- 方法名和变量名使用小驼峰（lowerCamelCase）
- 常量使用全大写+下划线（UPPER_SNAKE_CASE）
- 缩进使用 4 个空格
- 每行不超过 120 个字符

### 9.2 注释规范

```java
/**
 * 类功能说明
 *
 * @author 作者
 * @version 版本号
 * @since 起始版本
 */
public class ExampleClass {

    /**
     * 方法功能说明
     *
     * @param param1 参数1说明
     * @param param2 参数2说明
     * @return 返回值说明
     * @throws ExceptionType 异常说明
     */
    public ReturnType methodName(Type1 param1, Type2 param2) 
            throws ExceptionType {
        // 实现代码
    }
}
```

### 9.3 Git 提交规范

```
<type>(<scope>): <subject>

<body>

<footer>
```

**type 类型:**
- `feat`: 新功能
- `fix`: 修复
- `docs`: 文档
- `style`: 格式
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建/工具

**示例:**
```
feat(skill): 添加技能执行超时控制

- 添加 30 秒默认超时
- 支持自定义超时时间
- 添加超时异常处理

Refs: #123
```

---

## 10. 常见问题

### Q1: Maven 依赖下载失败

**解决:**
```bash
# 清理并重新下载
mvn clean -U

# 或更换镜像源
# 在 settings.xml 中添加阿里云镜像
```

### Q2: 端口被占用

**解决:**
```bash
# 查找占用端口的进程
netstat -ano | findstr :8081

# 结束进程
taskkill /PID <PID> /F

# 或修改配置文件使用其他端口
```

### Q3: 启动时报 AgentSDK 初始化失败

**解决:**
- 检查 UDP 端口是否被占用
- 检查配置文件是否正确
- 查看日志获取详细错误信息

### Q4: 如何贡献代码？

**步骤:**
1. Fork 本仓库
2. 创建特性分支: `git checkout -b feature/YourFeature`
3. 提交更改: `git commit -m 'feat: 添加新功能'`
4. 推送分支: `git push origin feature/YourFeature`
5. 创建 Pull Request

---

## 11. 参考资源

- [项目 Wiki](https://github.com/oodercn/super-Agent/wiki)
- [API 文档](http://localhost:8081/swagger-ui.html)（本地启动后）
- [CHANGELOG](../CHANGELOG.md)
- [优化路线图](../optimization/ROADMAP.md)

---

## 12. 联系方式

- **GitHub Issues**: https://github.com/oodercn/super-Agent/issues
- **Gitee Issues**: https://gitee.com/ooderCN/ooder-agent/issues
- **邮箱**: ooder@ooder.cn

---

**Happy Coding! 🚀**

# 快速入门

## 1. 概述

本指南将帮助您快速上手 ooder-agent-rad 开发，包括环境搭建、项目创建、开发流程和部署等方面。

## 2. 环境要求

| 软件 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8+ | Java 开发环境 |
| Maven | 3.6+ | 项目构建工具 |
| Git | 2.0+ | 版本控制工具 |

## 3. 环境搭建

### 3.1 安装 JDK

1. 下载 JDK 1.8+ 安装包
2. 按照安装向导安装 JDK
3. 配置 JAVA_HOME 环境变量
4. 验证安装：`java -version`

### 3.2 安装 Maven

1. 下载 Maven 3.6+ 安装包
2. 解压到指定目录
3. 配置 MAVEN_HOME 环境变量
4. 验证安装：`mvn -version`

### 3.3 安装 Git

1. 下载 Git 2.0+ 安装包
2. 按照安装向导安装 Git
3. 配置 Git 用户信息：
   ```bash
   git config --global user.name "Your Name"
   git config --global user.email "your.email@example.com"
   ```
4. 验证安装：`git --version`

## 4. 项目创建与导入

### 4.1 克隆项目

```bash
git clone https://github.com/ooder/ooder-agent-rad.git
cd ooder-agent-rad
```



## 5. 项目结构

ooder-agent-rad 采用标准的 Maven 项目结构：

```
ooder-agent-rad/
├── src/
│   ├── main/
│   │   ├── java/                # Java 源代码
│   │   │   └── net/ooder/editor/   # 主包
│   │   └── resources/           # 资源文件
│   │       ├── static/          # 静态资源
│   │       └── application.properties  # 配置文件
│   └── test/                    # 测试代码
├── docs/                        # 文档
├── doc/                         # 原始文档
├── lib/                         # 依赖库
├── form/                        # 表单模板
├── pom.xml                      # Maven 配置文件
└── README.md                    # 项目说明
```

## 6. 开发流程

### 6.1 开发前准备

1. 了解项目架构和核心组件
2. 熟悉代码规范和命名约定
3. 配置开发环境

### 6.2 开发步骤

1. **需求分析**：理解需求，确定实现方案
2. **设计**：设计类结构、接口和数据库模型
3. **编码**：实现功能代码
4. **测试**：编写单元测试和集成测试
5. **代码审查**：提交代码审查
6. **合并**：将代码合并到主分支

### 6.3 示例：创建一个简单的服务

#### 6.3.1 创建服务类

```java
package net.ooder.editor.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    
    /**
     * 简单的问候服务
     */
    public String sayHello(String name) {
        return "Hello, " + name + "! Welcome to ooder-agent-rad.";
    }
}
```

#### 6.3.2 创建控制器

```java
package net.ooder.editor.controller;

import net.ooder.editor.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @Autowired
    private HelloService helloService;
    
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return helloService.sayHello(name);
    }
}
```

#### 6.3.3 测试服务

```java
package net.ooder.editor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HelloServiceTest {
    
    private HelloService helloService = new HelloService();
    
    @Test
    public void testSayHello() {
        String result = helloService.sayHello("World");
        assertEquals("Hello, World! Welcome to ooder-agent-rad.", result);
    }
}
```

## 7. 构建与运行

### 7.1 构建项目

```bash
mvn clean package -DskipTests
```

### 7.2 运行项目

```bash
java -jar target/ooder-agent-rad-1.0.jar
```

或者使用 Maven 运行：

```bash
mvn spring-boot:run
```

### 7.3 访问应用

应用启动后，可通过以下 URL 访问：
- 应用首页：http://localhost:8080
- API 文档：http://localhost:8080/swagger-ui.html

## 8. 部署

### 8.1 本地部署

1. 构建项目：`mvn clean package -DskipTests`
2. 运行 JAR 文件：`java -jar target/ooder-agent-rad-1.0.jar`





## 10. 常见问题与解决方案

### 10.1 端口被占用

**问题**：启动应用时提示端口被占用

**解决方案**：
1. 查看占用端口的进程：`netstat -ano | findstr :8080`
2. 结束占用端口的进程：`taskkill /PID <PID> /F`
3. 或者修改应用端口：在 `application.properties` 中配置 `server.port=8081`

### 10.2 依赖冲突

**问题**：构建项目时提示依赖冲突

**解决方案**：
1. 查看依赖树：`mvn dependency:tree`
2. 找出冲突的依赖
3. 在 pom.xml 中使用 `<exclusions>` 排除冲突依赖

### 10.3 编译错误

**问题**：编译时提示错误

**解决方案**：
1. 检查代码语法错误
2. 检查依赖是否完整
3. 检查 JDK 版本是否符合要求

## 11. 学习资源

### 11.1 官方文档

- [项目文档](../README.md)
- [架构文档](../architecture/ARCHITECTURE_OVERVIEW.md)
- [注解文档](../annotation/ANNOTATION_OVERVIEW.md)
- [开发指南](DEVELOPMENT_ENV.md)
- [代码规范](CODE_SPEC.md)

### 11.2 示例代码

- [服务示例](../src/main/java/net/ooder/editor/service/)
- [控制器示例](../src/main/java/net/ooder/editor/service/)
- [视图示例](../src/main/java/net/ooder/editor/bean/)

### 11.3 社区资源

- GitHub Issues：https://github.com/ooder/ooder-agent-rad/issues
- Gitee：https://gitee.com/ooder/ooder-agent-rad
- 技术博客：https://blog.ooder.cn

## 12. 后续学习

1. 深入学习项目架构和核心组件
2. 熟悉注解驱动开发模式
3. 学习插件开发和扩展
4. 了解视图设计和服务开发
5. 掌握部署和运维知识

通过本快速入门指南，您已经了解了 ooder-agent-rad 的基本开发流程和环境搭建。接下来，您可以根据项目需求和个人兴趣深入学习相关技术和组件。
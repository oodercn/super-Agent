# 部署指南

## 1. 概述

本文档提供了 ooder-agent-rad 项目的部署指南，包括环境要求、部署方式、部署流程和注意事项等方面。通过本指南，您可以了解如何将 ooder-agent-rad 部署到开发环境、测试环境或生产环境。

## 2. 环境要求

| 软件 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8+ | Java 运行环境 |
| Maven | 3.6+ | 项目构建工具 |

## 3. 部署方式

### 3.1 本地部署

适合开发环境和测试环境，简单快捷，便于调试。

#### 3.1.1 构建项目

```bash
mvn clean package -DskipTests
```

#### 3.1.2 运行项目

```bash
java -jar target/ooder-agent-rad-1.0.jar
```

或者使用 Maven 运行：

```bash
mvn spring-boot:run
```

## 4. 部署流程

### 4.1 开发环境部署

1. 克隆代码仓库
2. 配置开发环境
3. 构建项目
4. 运行项目
5. 验证部署

### 4.2 测试环境部署

1. 从代码仓库拉取最新代码
2. 构建项目
3. 运行自动化测试
4. 部署到测试环境
5. 执行系统测试和集成测试
6. 验证部署

### 4.3 生产环境部署

1. 从代码仓库拉取稳定版本
2. 构建项目
3. 运行完整测试套件
4. 部署到预发布环境
5. 进行预发布验证
6. 部署到生产环境
7. 监控系统运行状态

## 5. 配置管理

### 5.1 配置文件

ooder-agent-rad 使用 Spring Boot 的配置机制，支持多种配置方式：

- `application.properties`：默认配置文件
- 环境变量
- 命令行参数

### 5.2 核心配置项

```properties
# 应用基本配置
server.port=8080
spring.application.name=ooder-agent-rad

# 日志配置
logging.level.root=INFO
logging.level.net.ooder=DEBUG
logging.file.name=logs/ooder-agent-rad.log

# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=100MB

# 插件配置
dsm.plugin.enabled=true
right.plugin.enabled=true
vfs.plugin.enabled=true
formula.plugin.enabled=true
```

### 5.3 环境变量配置

可以通过环境变量覆盖配置文件中的配置：

```bash
export SERVER_PORT=8081
java -jar ooder-agent-rad-1.0.jar
```

## 6. 监控与日志

### 6.1 日志管理

- 日志文件默认保存在 `logs/` 目录下
- 日志级别可以通过配置文件调整
- 支持日志滚动和压缩

### 6.2 监控指标

- 可以通过日志查看应用运行状态
- 可以通过进程监控工具查看应用性能

## 7. 安全性配置

### 7.1 权限控制

- 集成 Spring Security
- 配置用户认证和授权
- 实现基于角色的访问控制

### 7.2 防火墙配置

- 配置服务器防火墙，只开放必要的端口

## 8. 备份与恢复

### 8.1 文件备份

- 备份日志文件
- 备份配置文件
- 备份数据文件

## 9. 升级与回滚

### 9.1 升级流程

1. 备份当前版本和数据
2. 拉取新版本代码
3. 构建新版本
4. 部署新版本
5. 验证升级结果

### 9.2 回滚流程

1. 停止当前版本
2. 恢复备份的数据
3. 部署旧版本
4. 验证回滚结果

## 10. 常见问题与解决方案

### 10.1 端口被占用

**问题**：启动应用时提示端口被占用

**解决方案**：
1. 查看占用端口的进程：
   ```bash
   netstat -ano | findstr :8080
   ```
2. 结束占用端口的进程：
   ```bash
   taskkill /PID <PID> /F
   ```
3. 或者修改应用端口：
   ```properties
   server.port=8081
   ```

### 10.2 应用启动失败

**问题**：应用启动时抛出异常

**解决方案**：
1. 查看应用日志，分析错误原因
2. 检查配置文件是否正确
3. 检查依赖是否完整
4. 检查环境变量是否配置正确

## 11. 性能优化

### 11.1 JVM 优化

- 配置合适的 JVM 堆大小：
  ```bash
  java -Xms2g -Xmx4g -jar ooder-agent-rad-1.0.jar
  ```

- 配置 GC 算法：
  ```bash
  java -XX:+UseG1GC -jar ooder-agent-rad-1.0.jar
  ```

## 12. 监控与运维

### 12.1 监控指标

- 应用健康状态
- 系统资源使用情况
- 请求响应时间
- 错误率和异常情况

### 12.2 告警配置

- 配置邮件告警
- 配置短信告警
- 配置即时通讯工具告警（如 Slack、钉钉、企业微信）

### 12.3 自动化运维

- 使用 CI/CD 工具实现自动化部署
- 使用配置管理工具（如 Ansible、Chef、Puppet）管理服务器配置

## 13. 总结

本文档提供了 ooder-agent-rad 项目的部署指南，包括环境要求、部署方式、部署流程和注意事项等方面。通过本指南，您可以了解如何将 ooder-agent-rad 部署到不同环境，并进行配置管理、监控和运维。

部署是软件开发过程中的重要环节，合理的部署策略和流程可以提高系统的可靠性、稳定性和安全性。团队成员应遵循部署规范，确保系统能够顺利部署和运行。
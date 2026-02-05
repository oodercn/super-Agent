# SuperAgent Nexus 测试环境部署指南

## 1. 概述

本文档旨在详细说明如何搭建和配置 SuperAgent 的 Nexus 测试环境，确保开发和测试过程中依赖管理的一致性和可靠性。Nexus 作为 Maven 仓库管理器，为 SuperAgent 项目提供了本地依赖缓存和私有仓库功能。

## 2. Nexus 环境需求

### 2.1 硬件需求

| 配置项 | 最低配置 | 推荐配置 |
|--------|----------|----------|
| CPU | 2 核 | 4 核 |
| 内存 | 4 GB | 8 GB |
| 磁盘空间 | 50 GB | 100 GB |
| 网络 | 100 Mbps | 1 Gbps |

### 2.2 软件需求

| 软件 | 版本 | 用途 |
|------|------|------|
| Java | JDK 1.8+ | 运行 Nexus |
| Nexus Repository Manager | 3.30+ | Maven 仓库管理 |
| Maven | 3.6+ | 构建工具 |
| Git | 2.0+ | 版本控制 |

## 3. Nexus 安装与配置

### 3.1 下载与安装 Nexus

1. **下载 Nexus**
   - 访问 [Nexus 官网](https://www.sonatype.com/products/nexus-repository)
   - 下载 Nexus Repository Manager OSS 版本

2. **安装 Nexus**
   ```bash
   # 解压安装包
   tar -xvf nexus-3.x.x-unix.tar.gz
   
   # 移动到安装目录
   mv nexus-3.x.x /opt/nexus
   mv sonatype-work /opt/sonatype-work
   ```

3. **配置 Nexus 服务**
   ```bash
   # 创建系统服务
   vi /etc/systemd/system/nexus.service
   ```

   服务配置内容：
   ```
   [Unit]
   Description=Nexus Repository Manager
   After=network.target

   [Service]
   Type=forking
   LimitNOFILE=65536
   ExecStart=/opt/nexus/bin/nexus start
   ExecStop=/opt/nexus/bin/nexus stop
   User=nexus
   Restart=on-abort

   [Install]
   WantedBy=multi-user.target
   ```

4. **启动 Nexus 服务**
   ```bash
   # 启动服务
   systemctl start nexus
   
   # 设置开机自启
   systemctl enable nexus
   
   # 检查服务状态
   systemctl status nexus
   ```

### 3.2 初始化配置

1. **访问 Nexus 控制台**
   - 打开浏览器访问：`http://localhost:8081`
   - 点击 "Sign in" 登录
   - 默认用户名：`admin`
   - 默认密码：在 `/opt/sonatype-work/nexus3/admin.password` 文件中

2. **配置 Nexus 仓库**
   - **创建 Maven 代理仓库**：代理阿里云 Maven 仓库
   - **创建 Maven 宿主仓库**：存储本地构建的依赖
   - **创建 Maven 组仓库**：组合多个仓库为一个访问点

3. **配置仓库权限**
   - 创建专用的部署用户
   - 配置适当的仓库访问权限

## 4. SuperAgent 项目配置

### 4.1 Maven 配置

1. **更新 settings.xml 文件**
   ```xml
   <settings>
     <localRepository>${user.home}/.m2/repository</localRepository>
     
     <servers>
       <server>
         <id>nexus-releases</id>
         <username>deployment</username>
         <password>deployment123</password>
       </server>
       <server>
         <id>nexus-snapshots</id>
         <username>deployment</username>
         <password>deployment123</password>
       </server>
     </servers>
     
     <mirrors>
       <mirror>
         <id>nexus</id>
         <mirrorOf>*</mirrorOf>
         <url>http://localhost:8081/repository/maven-group/</url>
       </mirror>
     </mirrors>
     
     <profiles>
       <profile>
         <id>nexus</id>
         <activation>
           <activeByDefault>true</activeByDefault>
         </activation>
         <repositories>
           <repository>
             <id>nexus</id>
             <url>http://localhost:8081/repository/maven-group/</url>
             <releases>
               <enabled>true</enabled>
             </releases>
             <snapshots>
               <enabled>true</enabled>
             </snapshots>
           </repository>
         </repositories>
         <pluginRepositories>
           <pluginRepository>
             <id>nexus</id>
             <url>http://localhost:8081/repository/maven-group/</url>
             <releases>
               <enabled>true</enabled>
             </releases>
             <snapshots>
               <enabled>true</enabled>
             </snapshots>
           </pluginRepository>
         </pluginRepositories>
       </profile>
     </profiles>
   </settings>
   ```

2. **更新项目 pom.xml 文件**
   ```xml
   <project>
     <!-- 其他配置 -->
     
     <distributionManagement>
       <repository>
         <id>nexus-releases</id>
         <name>Nexus Releases Repository</name>
         <url>http://localhost:8081/repository/maven-releases/</url>
       </repository>
       <snapshotRepository>
         <id>nexus-snapshots</id>
         <name>Nexus Snapshots Repository</name>
         <url>http://localhost:8081/repository/maven-snapshots/</url>
       </snapshotRepository>
     </distributionManagement>
     
     <!-- 其他配置 -->
   </project>
   ```

### 4.2 部署依赖到 Nexus

1. **部署 agent-sdk**
   ```bash
   cd agent-sdk
   mvn clean deploy
   ```

2. **部署 skill-vfs**
   ```bash
   cd skill-vfs
   mvn clean deploy
   ```

3. **部署其他模块**
   ```bash
   cd ooder-agent-metadata
   mvn clean deploy
   ```

## 5. 测试环境使用指南

### 5.1 依赖解析测试

1. **清理本地仓库**
   ```bash
   rm -rf ~/.m2/repository/net/ooder
   ```

2. **构建测试**
   ```bash
   cd agent-skillcenter
   mvn clean compile
   ```

3. **验证依赖来源**
   - 检查 Nexus 控制台中的依赖下载记录
   - 确认依赖从 Nexus 仓库正确解析

### 5.2 版本一致性测试

1. **检查版本配置**
   ```bash
   # 检查所有 pom.xml 文件的版本配置
   find . -name "pom.xml" -exec grep -l "version" {} \;
   ```

2. **验证构建产物**
   ```bash
   # 检查构建产物的版本号
   ls -la agent-sdk/target/
   ```

## 6. 常见问题与解决方案

### 6.1 依赖解析失败

**症状**：Maven 构建时依赖下载失败

**解决方案**：
- 检查 Nexus 服务状态
- 验证网络连接
- 检查 Maven settings.xml 配置
- 查看 Nexus 日志确认错误原因

### 6.2 部署权限问题

**症状**：部署依赖到 Nexus 时权限不足

**解决方案**：
- 检查部署用户的权限配置
- 验证 settings.xml 中的服务器凭证
- 确认 Nexus 仓库的部署权限设置

### 6.3 版本冲突

**症状**：不同模块依赖版本不一致

**解决方案**：
- 使用 Maven 依赖管理统一版本
- 在父 POM 中集中管理版本号
- 运行 `mvn dependency:tree` 分析依赖树

## 7. 最佳实践

### 7.1 仓库管理

- **定期备份**：定期备份 Nexus 数据目录
- **清理策略**：配置合理的仓库清理策略
- **监控**：设置 Nexus 服务监控

### 7.2 依赖管理

- **版本规范**：遵循语义化版本规范
- **依赖锁定**：使用 dependencyManagement 锁定依赖版本
- **定期更新**：定期更新第三方依赖版本

### 7.3 构建流程

- **CI/CD 集成**：将 Nexus 集成到 CI/CD 流程
- **自动化测试**：每次部署后运行自动化测试
- **构建缓存**：合理配置 Maven 构建缓存

## 8. 与 V0.6.5 Nexus 版本的兼容性

本指南适用于 SuperAgent V0.6.5 Nexus 版本，确保以下兼容性：

- **依赖版本**：所有依赖版本统一为 0.6.5
- **仓库配置**：使用正确的 Nexus 仓库路径
- **部署流程**：遵循 V0.6.5 版本的部署规范
- **测试验证**：使用 V0.6.5 版本的测试用例

## 9. P2P 网络配置

### 9.1 网络环境准备

在测试环境中搭建 P2P 网络，需要确保：

- **网络连通性**：所有 Agent 节点之间能够相互通信
- **UDP 端口开放**：确保 UDP 广播端口（默认 9009、9007、9011）可访问
- **防火墙配置**：允许 Agent 之间的 TCP 和 UDP 通信

### 9.2 Agent 配置示例

#### Skill A（End Agent）配置：

```java
AgentConfig config = AgentConfig.builder()
        .agentId("skill-a-001")
        .agentName("SkillA")
        .agentType("end")
        .endpoint("http://localhost:9008")
        .udpPort(9009)
        .heartbeatInterval(30000)
        .build();
```

#### Skill B（End Agent）配置：

```java
AgentConfig config = AgentConfig.builder()
        .agentId("skill-b-001")
        .agentName("SkillB")
        .agentType("end")
        .endpoint("http://localhost:9006")
        .udpPort(9007)
        .heartbeatInterval(30000)
        .build();
```

#### Skill C（Route Agent）配置：

```java
AgentConfig config = AgentConfig.builder()
        .agentId("skill-c-001")
        .agentName("SkillC")
        .agentType("route")
        .endpoint("http://localhost:9010")
        .udpPort(9011)
        .heartbeatInterval(30000)
        .build();
```

### 9.3 P2P 功能测试

1. **节点发现测试**：启动所有 Agent 后，验证它们能够通过 UDP 广播相互发现
2. **命令路由测试**：通过 Skill C 发送命令，验证命令能够正确路由到目标 Agent
3. **数据流转测试**：测试完整的数据获取、处理和提交流程
4. **网络弹性测试**：模拟节点下线场景，验证网络的自组织能力

### 9.4 常见 P2P 网络问题

| 问题 | 症状 | 解决方案 |
|------|------|----------|
| 节点发现失败 | Agent 无法找到其他节点 | 检查 UDP 端口是否开放，网络是否连通 |
| 命令路由失败 | 命令无法到达目标节点 | 检查 Route Agent 配置，验证网络拓扑 |
| 数据流转中断 | 数据无法完整流转 | 检查 End Agent 状态，验证网络连接稳定性 |
| 网络性能下降 | 命令响应延迟增加 | 优化网络配置，考虑增加 Route Agent 节点 |

## 10. 后续维护

### 10.1 定期维护任务

- **仓库清理**：定期清理过期依赖
- **日志轮换**：配置日志轮换策略
- **性能优化**：根据使用情况调整 Nexus 配置

### 10.2 升级策略

- **Nexus 升级**：按照官方指南升级 Nexus 版本
- **依赖升级**：定期升级第三方依赖
- **配置备份**：升级前备份所有配置

## 11. 附录

### 11.1 常用 Nexus 命令

| 命令 | 描述 |
|------|------|
| `nexus start` | 启动 Nexus 服务 |
| `nexus stop` | 停止 Nexus 服务 |
| `nexus status` | 查看 Nexus 服务状态 |
| `nexus restart` | 重启 Nexus 服务 |

### 11.2 Maven 依赖命令

| 命令 | 描述 |
|------|------|
| `mvn dependency:tree` | 查看依赖树 |
| `mvn dependency:analyze` | 分析依赖使用情况 |
| `mvn dependency:copy-dependencies` | 复制依赖到指定目录 |

### 11.3 故障排查工具

| 工具 | 用途 |
|------|------|
| Nexus 日志 | 查看 Nexus 服务日志 |
| Maven 调试模式 | `mvn -X` 查看详细构建过程 |
| JVM 监控 | 监控 Nexus JVM 状态 |

## 12. 总结

通过本文档的指导，您应该能够成功搭建和配置 SuperAgent 的 Nexus 测试环境，确保依赖管理的一致性和可靠性。Nexus 作为 Maven 仓库管理器，不仅提高了构建速度，还为项目提供了可靠的依赖管理方案。

在使用过程中，请注意遵循版本管理规范，确保所有依赖版本与 SuperAgent V0.6.5 Nexus 版本保持一致，以获得最佳的开发和测试体验。

同时，通过本文档中的 P2P 网络配置指南，您可以搭建完整的 P2P 分布式测试环境，验证 SuperAgent 在分布式场景下的功能和性能。
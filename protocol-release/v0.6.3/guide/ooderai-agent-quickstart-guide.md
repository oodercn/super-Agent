# ooderAI Agent V0.6 快速上手指南

## 1. 项目概述

ooderAI Agent是一个基于Spring Boot的分布式智能代理系统，通过模块化技能（Skill）架构实现了高效的数据获取、协调和提交流程。系统包含三个核心技能：

- **Skill A**: 信息检索技能，负责从数据源获取信息
- **Skill B**: 数据提交技能，负责将处理后的数据提交到目标系统
- **Skill C**: 协调技能，负责协调整个数据流转过程

## 2. 环境准备

### 2.1 开发环境要求

- JDK 1.8+
- Maven 3.6+
- Git

### 2.2 依赖组件

- Spring Boot 2.7.0
- FastJSON 1.2.83
- Spring WebFlux（用于异步通信）
- SLF4J（日志框架）

## 3. 安装步骤

### 3.1 克隆代码库

```bash
git clone <repository-url>
cd ooder-public/super-agent
```

### 3.2 构建项目

```bash
# 构建Skill A
cd skill-a
mvn clean package -DskipTests

# 构建Skill B
cd ../skill-b
mvn clean package -DskipTests

# 构建Skill C
cd ../skill-c
mvn clean package -DskipTests
```

## 4. 配置说明

### 4.1 Skill A 配置

文件：`skill-a/src/main/resources/application.yml`

```yaml
server:
  port: 9000

spring:
  application:
    name: skill-a

skill-a:
  agent:
    id: skill-a-agent-001
    type: skill-a
  network:
    address: 127.0.0.1
    port: 8080
  security:
    enabled: true
    token: secret-token-001
```

### 4.2 Skill B 配置

文件：`skill-b/src/main/resources/application.yml`

```yaml
server:
  port: 9001

spring:
  application:
    name: skill-b

skill-b:
  agent:
    id: skill-b-agent-001
    type: skill-b
  network:
    address: 127.0.0.1
    port: 8081
  security:
    enabled: true
    token: secret-token-001
```

### 4.3 Skill C 配置

文件：`skill-c/src/main/resources/application.yml`

```yaml
server:
  port: 9010

spring:
  application:
    name: skill-c

skill-c:
  scene:
    id: RBC_SCENE_001
    name: Data Reporting Scene
    type: data-reporting
  agent:
    id: skill-c-agent-001
  network:
    broadcast:
      port: 9876
```

## 5. 运行指南

### 5.1 启动Skill C（协调服务）

```bash
cd skill-c
mvn spring-boot:run
```

### 5.2 启动Skill A（信息检索服务）

```bash
cd skill-a
mvn spring-boot:run
```

### 5.3 启动Skill B（数据提交服务）

```bash
cd skill-b
mvn spring-boot:run
```

## 6. 交互示例

### 6.1 健康检查

```bash
# 检查Skill A健康状态
curl http://localhost:9000/api/v1/skill-a/health

# 检查Skill B健康状态
curl http://localhost:9001/api/v1/skill-b/health

# 检查Skill C健康状态
curl http://localhost:9010/api/v1/skill-c/health
```

### 6.2 数据检索（Skill A）

```bash
curl http://localhost:9000/api/v1/skill-a/retrieve
```

### 6.3 数据提交（Skill B）

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9001/api/v1/skill-b/submit \
  -d '{"data":{"key":"value"},"agentId":"test-agent"}'
```

### 6.4 数据流转协调（Skill C）

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/data/coordinate \
  -d '{"sceneId":"RBC_SCENE_001"}'
```

## 7. 场景管理

### 7.1 创建场景组

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001/groups \
  -d '{"name":"Data Reporting Group","description":"A group for data reporting","groupId":"GROUP_001"}'
```

### 7.2 加入场景

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001/join \
  -d '{"agentId":"skill-a-agent-001","agentName":"Skill A Agent","type":"skill-a","status":"ACTIVE","capabilities":[{"capabilityId":"info-retrieval","capabilityName":"Information Retrieval","capabilityType":"DATA"}],"metadata":{"endpoint":"http://localhost:9000"}}'
```

### 7.3 获取场景详情

```bash
curl -X GET http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001
```

### 7.4 获取场景参与者

```bash
curl -X GET http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001/participants
```

### 7.5 离开场景

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001/leave \
  -d '{"agentId":"skill-a-agent-001"}'
```

## 8. 常见问题和解决方案

### 8.1 服务启动失败

**问题**：端口被占用
**解决方案**：修改各技能的配置文件，使用不同的端口号

### 8.2 数据流转失败

**问题**：Skill A或Skill B未正确加入场景
**解决方案**：检查场景ID是否正确，确保各技能已成功加入场景

### 8.3 安全认证错误

**问题**：Token验证失败
**解决方案**：确保各技能使用相同的安全Token，或禁用安全验证（仅用于开发环境）

## 9. 组管理

### 9.1 创建组

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001/groups \
  -d '{"name":"Data Reporting Group","description":"A group for data reporting","groupId":"GROUP_001"}'
```

### 9.2 获取组详情

```bash
curl -X GET http://localhost:9010/api/v1/skill-c/groups/GROUP_001
```

### 9.3 加入组

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001/join \
  -d '{"agentId":"skill-a-agent-001","agentName":"Skill A Agent","type":"skill-a","status":"ACTIVE","capabilities":[{"capabilityId":"info-retrieval","capabilityName":"Information Retrieval","capabilityType":"DATA"}],"metadata":{"endpoint":"http://localhost:9000"}}'
```

### 9.4 离开组

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/scenes/RBC_SCENE_001/leave \
  -d '{"agentId":"skill-a-agent-001"}'
```

### 9.5 获取组内成员

```bash
curl -X GET http://localhost:9010/api/v1/skill-c/groups/GROUP_001/members
```

### 9.6 移除组内成员

```bash
curl -X DELETE http://localhost:9010/api/v1/skill-c/groups/GROUP_001/members/skill-a-agent-001
```

### 9.7 删除组

```bash
curl -X DELETE http://localhost:9010/api/v1/skill-c/groups/GROUP_001
```

## 10. 数据存储和管理

### 10.1 存储代理数据

```bash
curl -X POST -H "Content-Type: application/json" \
  http://localhost:9010/api/v1/skill-c/data/store/skill-a-agent-001 \
  -d '{"key1":"value1","key2":"value2"}'
```

### 10.2 获取代理数据

```bash
curl -X GET http://localhost:9010/api/v1/skill-c/data/skill-a-agent-001
```

## 11. 代码结构说明

### 11.1 Skill A 核心组件

- `InformationRetrievalSkill`: 技能实现类
- `RpcController`: REST API控制器，提供信息检索和健康检查API
- `InformationRetrievalService`: 信息检索服务，实现数据获取核心逻辑
- `NetworkService`: 网络通信服务，处理UDP广播和网络通信
- `DiscoveryService`: 服务发现服务，处理场景加入和服务注册

### 11.2 Skill B 核心组件

- `DataSubmissionService`: 数据提交服务，实现数据提交核心逻辑
- `RpcController`: REST API控制器，提供数据提交和健康检查API
- `NetworkService`: 网络通信服务，处理UDP广播和网络通信

### 11.3 Skill C 核心组件

- `CoordinationService`: 数据流转协调服务，协调整个数据流转过程
- `SceneService`: 场景管理服务，管理场景和组
- `RpcController`: REST API控制器，提供场景管理和数据协调API
- `DiscoveryService`: 服务发现服务，处理服务发现和场景管理

## 12. 扩展开发

### 12.1 开发新技能

1. 创建新的Spring Boot项目
2. 实现技能接口
3. 配置网络通信
4. 注册到协调服务

### 10.2 自定义数据处理

修改`CoordinationService`类，实现自定义的数据处理逻辑：

```java
public Mono<Map<String, Object>> coordinateDataFlow(String sceneId) {
    return retrieveDataFromSkillA(sceneId)
            .flatMap(data -> {
                // 自定义数据处理逻辑
                processData(data);
                return submitDataToSkillB(sceneId, data);
            })
            .doOnSuccess(result -> logger.info("Successfully coordinated data flow for scene {}", sceneId));
}
```

## 13. 监控和日志

各技能使用SLF4J记录日志，日志文件默认输出到控制台。可以通过配置`logback-spring.xml`文件自定义日志配置。

---

**编写日期**：2026-01-18
**版本**：V0.6
# MCP Agent Console 系统文档

## 系统概述

MCP Agent Console 是一个基于 Ooder SDK 开发的主控节点管理控制台，用于监控和管理 MCP (Master Control Point) Agent 及其关联的网络和服务。系统提供了直观的可视化界面，帮助用户实时了解系统状态、网络拓扑、服务运行情况等关键信息。

## 技术架构

### 前端架构
- **技术栈**：HTML5, CSS3, JavaScript (ES6+)
- **UI 框架**：自定义 Ooder Rad Style 风格
- **图标库**：Remixicon (ri- 开源图标)
- **响应式设计**：支持多种屏幕尺寸
- **页面结构**：独立 HTML 页面 + JSON 菜单配置

### 后端架构
- **技术栈**：Java 8, Spring Boot 2.7.0
- **API 风格**：RESTful API
- **服务组件**：
  - MCP Agent Controller：主控节点API
  - Monitor Controller：系统监控API
  - Network Controller：网络管理API
  - System Controller：系统管理API

### SDK 集成
- **Agent SDK**：v0.6.5
- **功能**：网络通信、命令处理、服务发现

## 功能模块

### 1. 仪表盘
- 系统状态概览
- 网络状态监控
- 服务运行状态
- 健康评分

### 2. 健康检查
- 系统状态监控
- 网络状态监控
- 服务状态监控
- 健康检查历史

### 3. 系统监控
- CPU 使用率
- 内存使用率
- 磁盘使用率
- 系统负载
- 告警信息管理

### 4. 网络管理
- 路由管理
- 链路管理
- 网络拓扑
- 场景管理

### 5. 配置管理
- MCP 配置
- Route 配置
- End 配置
- 服务配置

### 6. 系统管理
- 请求管理
- 服务流程
- 结果处理
- 代理协调

### 7. 终端管理
- 终端详情
- 终端操作
- 终端状态

## API 接口文档

### 主控节点 API (/api/mcp)

#### 健康检查
- **GET /health**
- **响应**：系统健康状态、服务信息、系统状态详情

#### 网络状态
- **GET /network/status**
- **响应**：网络状态、统计信息、拓扑信息

#### 网络拓扑
- **GET /network/topology**
- **响应**：网络拓扑结构

#### 系统状态
- **GET /system/status**
- **响应**：系统状态详情

#### 测试命令
- **POST /command/test**
- **请求体**：{"commandType": "MCP_STATUS", "agentId": "test-agent", "data": {}}
- **响应**：命令执行结果

#### 网络链路
- **POST /network/link**
- **请求体**：{"linkId": "link-001", "sourceAgentId": "source-001", "targetAgentId": "target-001", "linkType": "direct"}
- **响应**：链路添加结果

#### 重置网络统计
- **POST /network/reset**
- **响应**：重置结果

#### RouteAgent 详情
- **GET /network/routeagent/{routeAgentId}**
- **响应**：RouteAgent 详细信息

#### RouteAgent VFS
- **GET /network/routeagent/{routeAgentId}/vfs?path=/**
- **响应**：VFS 文件系统结构

#### RouteAgent 能力
- **GET /network/routeagent/{routeAgentId}/capabilities**
- **响应**：RouteAgent 能力列表

#### RouteAgent 链路
- **GET /network/routeagent/{routeAgentId}/links**
- **响应**：RouteAgent 链路列表

### 监控 API (/api/monitor)

#### 系统指标
- **GET /metrics**
- **响应**：CPU、内存、磁盘、负载等系统指标

#### 系统日志
- **GET /logs?limit=50**
- **响应**：系统日志列表

#### 告警信息
- **GET /alerts**
- **响应**：系统告警列表

#### 清除告警
- **POST /alerts/clear**
- **响应**：告警清除结果

## 前端页面使用指南

### 1. 访问控制台
- **URL**：http://localhost:8091/console/index.html
- **默认端口**：8091

### 2. 导航菜单
- **一级菜单**：仪表盘、监控、网络、配置、系统、终端
- **二级菜单**：各功能模块的详细功能
- **三级菜单**：具体操作项

### 3. 仪表盘
- **系统状态**：显示系统运行状态
- **网络状态**：显示网络连接状态
- **服务状态**：显示服务运行情况
- **健康评分**：系统健康度评估

### 4. 健康检查
- **系统状态**：CPU、内存、磁盘、负载等指标
- **网络状态**：网络连接数、延迟、带宽使用率等
- **服务状态**：各服务运行状态检查
- **健康历史**：历史健康检查记录

### 5. 系统监控
- **监控指标**：实时系统指标数据
- **告警信息**：系统告警管理
- **告警阈值**：基于阈值的自动告警

### 6. 网络管理
- **路由管理**：网络路由配置和测试
- **链路管理**：网络链路创建和编辑
- **场景管理**：网络场景创建和管理
- **拓扑视图**：网络拓扑可视化

### 7. 配置管理
- **MCP 配置**：主控节点配置
- **Route 配置**：路由节点配置
- **End 配置**：执行节点配置
- **服务配置**：服务参数配置

### 8. 系统管理
- **请求管理**：请求过滤和排序
- **服务流程**：服务流程配置和执行
- **结果处理**：执行结果管理
- **代理协调**：多代理协调管理

### 9. 终端管理
- **终端详情**：终端信息查看
- **终端操作**：终端命令执行
- **终端状态**：终端运行状态

## 后端服务配置

### 配置文件
- **application.yml**：Spring Boot 应用配置

### 主要配置项
```yaml
server:
  port: 8091

spring:
  application:
    name: ooder-mcp-agent

ooder:
  agent:
    id: "mcp-agent-001"
    name: "Independent MCP Agent"
    type: "mcp"
    description: "Independent MCP Agent built with Ooder SDK"
  udp:
    port: 9876
    host: 0.0.0.0
    timeout: 30000
    retry: 3
  heartbeat:
    interval: 30000
    timeout: 60000
  skill:
    base-package: "net.ooder.mcpagent.skill"
```

### 环境要求
- **Java**：8+
- **Maven**：3.6+
- **操作系统**：Windows / Linux / macOS

## 部署指南

### 1. 编译打包
```bash
mvn clean package
```

### 2. 运行服务
```bash
java -jar target/independent-mcp-agent-0.6.5.jar
```

### 3. 访问控制台
打开浏览器，访问：http://localhost:8091/console/index.html

## 开发指南

### 前端开发
- **页面结构**：pages/ 目录下按功能模块组织 HTML 页面
- **菜单配置**：menu-config.json 定义导航菜单结构
- **样式规范**：使用 Ooder Rad Style 风格
- **响应式设计**：支持多种屏幕尺寸

### 后端开发
- **API 规范**：RESTful API 设计
- **控制器**：按功能模块组织控制器
- **服务层**：业务逻辑封装
- **异常处理**：统一的错误处理机制

## 常见问题解答

### 1. 服务启动失败
- **检查端口**：确保 8091 端口未被占用
- **检查依赖**：确保所有依赖已正确加载
- **查看日志**：检查应用日志获取详细错误信息

### 2. 前端页面无法加载
- **检查网络**：确保网络连接正常
- **检查服务**：确保后端服务正在运行
- **检查 URL**：确保访问地址正确

### 3. API 调用失败
- **检查权限**：确保请求有正确的权限
- **检查参数**：确保请求参数格式正确
- **查看日志**：检查后端日志获取详细错误信息

### 4. 监控指标异常
- **检查系统**：确保系统资源充足
- **检查配置**：确保监控配置正确
- **查看告警**：检查系统告警信息

### 5. 网络连接问题
- **检查网络**：确保网络连接正常
- **检查防火墙**：确保防火墙未阻止连接
- **检查路由**：确保网络路由配置正确

## 版本历史

### v0.6.5
- 初始版本
- 实现基本的系统监控和管理功能
- 支持 MCP Agent 健康检查
- 支持网络拓扑管理
- 支持服务状态监控

## 许可证

Apache License 2.0

## 联系信息

- **项目地址**：https://github.com/oodercn/super-Agent
- **技术支持**：ooder-support@example.com

## 附录

### 前端页面列表

| 页面名称 | 路径 | 功能描述 |
|---------|------|----------|
| 仪表盘 | console/index.html | 系统状态概览 |
| 健康检查 | console/pages/monitoring/health-check.html | 系统健康监控 |
| 服务配置 | console/pages/config/service-config.html | 服务配置管理 |
| 路由管理 | console/pages/network/route-management.html | 网络路由管理 |
| 请求管理 | console/pages/system/request-management.html | 系统请求管理 |
| 终端管理 | console/pages/terminal/terminal-management.html | 终端设备管理 |
| 服务流程 | console/pages/system/service-flow.html | 服务流程管理 |
| 代理协调 | console/pages/system/agent-coordination.html | 多代理协调管理 |
| 结果处理 | console/pages/system/result-processing.html | 执行结果管理 |
| 场景管理 | console/pages/network/scenario-management.html | 网络场景管理 |
| 链路管理 | console/pages/network/link-management.html | 网络链路管理 |

### 后端 API 列表

| API 路径 | 方法 | 功能描述 |
|---------|------|----------|
| /api/mcp/health | GET | 健康检查 |
| /api/mcp/network/status | GET | 网络状态查询 |
| /api/mcp/network/topology | GET | 网络拓扑查询 |
| /api/mcp/system/status | GET | 系统状态查询 |
| /api/mcp/command/test | POST | 测试命令发送 |
| /api/mcp/network/link | POST | 添加网络链路 |
| /api/mcp/network/reset | POST | 重置网络统计 |
| /api/mcp/network/routeagent/{id} | GET | 获取 RouteAgent 详情 |
| /api/mcp/network/routeagent/{id}/vfs | GET | 获取 RouteAgent VFS |
| /api/mcp/network/routeagent/{id}/capabilities | GET | 获取 RouteAgent 能力 |
| /api/mcp/network/routeagent/{id}/links | GET | 获取 RouteAgent 链路 |
| /api/monitor/metrics | GET | 获取系统指标 |
| /api/monitor/logs | GET | 获取系统日志 |
| /api/monitor/alerts | GET | 获取告警信息 |
| /api/monitor/alerts/clear | POST | 清除告警信息 |

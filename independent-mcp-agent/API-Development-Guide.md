# API 开发文档

## 1. 项目概述

本项目是基于 Ooder Agent SDK 开发的独立 MCP Agent 应用，用于管理网络设备、处理命令、监控系统状态等功能。

## 2. 现有API结构

### 2.1 已实现的API模块

| 模块 | 基础路径 | 主要功能 | 类型 |
|------|----------|----------|------|
| McpAgentController | `/api/mcp` | 核心MCP Agent功能 | 自身开发 |
| NetworkController | `/api/network` | 网络管理功能 | 自身开发 |
| SystemController | `/api/system` | 系统管理功能 | 自身开发 |
| MonitorController | `/api/monitor` | 监控管理功能 | 自身开发 |
| ConfigController | `/api/config` | 配置管理功能 | 自身开发 |
| HomeController | `/api/home` | 智能家居管理功能 | 自身开发 |
| LanController | `/api/lan` | 局域网管理功能 | 自身开发 |
| CommandController | `/api/command` | 命令管理功能 | 自身开发 |
| BatchApiController | `/api/batch` | 批量操作功能 | 自身开发 |
| ConsoleController | `/api/console` | 控制台功能 | 自身开发 |

### 2.2 未实现的API分类

根据前端需求和API差距分析，以下API需要实现，分为SDK开发和自身开发两部分：

#### 2.2.1 SDK开发API

| 功能 | 前端需求 | 后端实现 | 状态 | 类型 |
|------|---------|---------|------|------|
| 网络链路管理 | 需要调用API管理网络链路 | McpAgentController中已实现部分功能 | ⚠️ 部分实现 | SDK开发 |
| 终端管理 | 需要获取终端列表和详情 | 未发现专门的终端管理API | ❌ 缺失 | SDK开发 |
| 路由管理 | 需要获取路由列表和详情 | 未发现专门的路由管理API | ❌ 缺失 | SDK开发 |
| 命令处理 | 需要处理各种MCP命令 | McpAgentController中已实现基本功能 | ⚠️ 部分实现 | SDK开发 |
| 网络拓扑 | 需要获取网络拓扑结构 | McpAgentController中已实现基本功能 | ⚠️ 部分实现 | SDK开发 |

#### 2.2.2 自身开发API

| 功能 | 前端需求 | 后端实现 | 状态 | 类型 |
|------|---------|---------|------|------|
| 移除链路 | 需要调用API移除指定链路 | McpAgentController中未实现，NetworkController中有 `/link/{linkId}` (DELETE) | ⚠️ 路径不匹配 | 自身开发 |
| 日志管理 | 需要获取系统日志 | 未发现专门的日志管理API | ❌ 缺失 | 自身开发 |
| 配置管理 | 需要保存和重置配置 | 未发现专门的配置管理API | ❌ 缺失 | 自身开发 |
| 系统监控 | 需要监控系统状态 | MonitorController中已实现基本功能 | ⚠️ 部分实现 | 自身开发 |
| 批量操作 | 需要批量处理数据 | BatchApiController中已实现基本功能 | ⚠️ 部分实现 | 自身开发 |

## 3. API开发规范

### 3.1 命名规范

- **URL命名**：使用小写字母和连字符(`-`)，避免使用下划线(`_`)
- **参数命名**：使用驼峰命名法
- **响应格式**：统一使用JSON格式，包含status、data/error、message等字段

### 3.2 响应格式规范

```json
// 成功响应
{
  "status": "success",
  "data": { ... },
  "message": "操作成功",
  "timestamp": 1769767056
}

// 错误响应
{
  "status": "error",
  "error": "错误代码",
  "message": "错误信息",
  "timestamp": 1769767056
}
```

### 3.3 错误处理规范

- 使用HTTP状态码表示错误类型
- 提供详细的错误信息和错误代码
- 统一的错误处理机制

## 4. 未实现API开发计划

### 4.1 1. 移除链路API

**功能描述**：移除指定的网络链路

**API路径**：`/api/mcp/network/link/{linkId}` (DELETE)

**请求参数**：
- `linkId` (路径参数)：链路ID

**响应格式**：
```json
{
  "status": "success",
  "message": "网络链路移除成功",
  "linkId": "{linkId}",
  "timestamp": 1769767056
}
```

### 4.2 2. 终端管理API

**功能描述**：管理终端设备，包括获取终端列表、终端详情、添加终端、移除终端等

**API路径**：
- `GET /api/mcp/endagent/list` - 获取终端列表
- `GET /api/mcp/endagent/{id}` - 获取终端详情
- `POST /api/mcp/endagent` - 添加终端
- `DELETE /api/mcp/endagent/{id}` - 移除终端

**请求参数**：
- `id` (路径参数)：终端ID
- `POST`请求体：
  ```json
  {
    "name": "终端名称",
    "type": "终端类型",
    "ip": "终端IP",
    "mac": "终端MAC地址"
  }
  ```

**响应格式**：
```json
{
  "status": "success",
  "data": {
    "id": "终端ID",
    "name": "终端名称",
    "type": "终端类型",
    "ip": "终端IP",
    "mac": "终端MAC地址",
    "status": "active",
    "lastUpdate": 1769767056
  },
  "message": "操作成功",
  "timestamp": 1769767056
}
```

### 4.3 3. 路由管理API

**功能描述**：管理网络路由，包括获取路由列表、路由详情、添加路由、移除路由等

**API路径**：
- `GET /api/mcp/route/list` - 获取路由列表
- `GET /api/mcp/route/{id}` - 获取路由详情
- `POST /api/mcp/route` - 添加路由
- `DELETE /api/mcp/route/{id}` - 移除路由

**请求参数**：
- `id` (路径参数)：路由ID
- `POST`请求体：
  ```json
  {
    "name": "路由名称",
    "source": "源节点ID",
    "target": "目标节点ID",
    "type": "路由类型",
    "status": "active"
  }
  ```

**响应格式**：
```json
{
  "status": "success",
  "data": {
    "id": "路由ID",
    "name": "路由名称",
    "source": "源节点ID",
    "target": "目标节点ID",
    "type": "路由类型",
    "status": "active",
    "latency": 10,
    "lastUpdate": 1769767056
  },
  "message": "操作成功",
  "timestamp": 1769767056
}
```

### 4.4 4. 日志管理API

**功能描述**：管理系统日志，包括获取日志列表、日志详情、清空日志等

**API路径**：
- `GET /api/mcp/log/list` - 获取日志列表
- `GET /api/mcp/log/{id}` - 获取日志详情
- `POST /api/mcp/log/clear` - 清空日志

**请求参数**：
- `id` (路径参数)：日志ID
- 查询参数：
  - `limit`：日志条数限制
  - `level`：日志级别
  - `startTime`：开始时间
  - `endTime`：结束时间

**响应格式**：
```json
{
  "status": "success",
  "data": [
    {
      "id": "日志ID",
      "level": "INFO",
      "message": "日志信息",
      "timestamp": 1769767056,
      "source": "日志来源"
    }
  ],
  "count": 1,
  "timestamp": 1769767056
}
```

### 4.5 5. 配置管理API

**功能描述**：管理系统配置，包括获取配置、保存配置、重置配置等

**API路径**：
- `GET /api/mcp/config/basic` - 获取基础配置
- `GET /api/mcp/config/advanced` - 获取高级配置
- `GET /api/mcp/config/security` - 获取安全配置
- `POST /api/mcp/config/save` - 保存配置
- `POST /api/mcp/config/reset` - 重置配置

**请求参数**：
- `POST`请求体：
  ```json
  {
    "agentId": "mcp-agent-001",
    "agentName": "Independent MCP Agent",
    "agentType": "mcp",
    "endpoint": "localhost:9876",
    "heartbeatInterval": "30000"
  }
  ```

**响应格式**：
```json
{
  "status": "success",
  "message": "配置保存成功",
  "timestamp": 1769767056
}
```

## 5. 实现计划

### 5.1 阶段一：SDK开发API实现

1. **网络链路管理API**：完善网络链路的创建、移除、更新和查询功能
2. **终端管理API**：实现终端设备的发现、注册、状态查询和管理
3. **路由管理API**：实现路由的发现、更新、查询和管理
4. **命令处理API**：完善MCP命令的处理机制，支持更多命令类型
5. **网络拓扑API**：实现网络拓扑的构建、更新和查询

### 5.2 阶段二：自身开发API实现

1. **移除链路API**：实现 `/api/mcp/network/link/{linkId}` (DELETE) 接口
2. **日志管理API**：实现系统日志的收集、查询和管理
3. **配置管理API**：实现系统配置的保存、加载和重置
4. **系统监控API**：完善系统监控功能，包括指标采集和告警
5. **批量操作API**：优化批量操作功能，提高处理效率

### 5.3 阶段三：集成和测试

1. **SDK集成测试**：测试SDK开发的API与Agent SDK的集成
2. **功能测试**：测试所有API的功能是否正常
3. **性能测试**：测试API的响应时间和并发处理能力
4. **集成测试**：测试API之间的集成和前端调用
5. **文档完善**：更新API文档，添加使用示例和最佳实践

### 5.4 阶段四：优化和部署

1. **性能优化**：优化API的性能和资源使用
2. **错误处理**：完善错误处理机制，提供详细的错误信息
3. **安全加固**：加强API的安全性，防止未授权访问
4. **部署上线**：部署到生产环境，监控运行状态
5. **持续维护**：定期更新和维护API，确保系统稳定运行

## 6. 技术实现要点

### 6.1 SDK开发技术要点

#### 6.1.1 1. Agent SDK集成

- **SDK初始化**：正确初始化Agent SDK，设置必要的配置参数
- **McpAgentSkill实现**：实现McpAgentSkill接口，处理各种MCP命令
- **命令处理**：实现命令的解析、执行和响应机制
- **网络管理**：利用SDK的网络管理能力，处理网络链路和拓扑

#### 6.1.2 2. 终端管理

- **终端发现**：实现终端设备的自动发现机制
- **终端注册**：实现终端设备的注册和管理
- **状态同步**：保持终端设备状态的实时同步
- **事件处理**：处理终端设备的各种事件

#### 6.1.3 3. 路由管理

- **路由发现**：实现网络路由的自动发现
- **路由计算**：实现最优路由的计算算法
- **路由更新**：处理网络拓扑变化时的路由更新
- **路由监控**：监控路由的状态和性能

### 6.2 自身开发技术要点

#### 6.2.1 1. Spring Boot框架

- **RESTful API**：利用Spring Boot的RESTful API支持
- **控制器设计**：合理设计控制器结构，分离不同功能模块
- **参数验证**：实现请求参数的验证和错误处理
- **响应格式**：统一API响应格式，提供一致的用户体验

#### 6.2.2 2. 数据存储

- **内存存储**：使用内存存储模拟数据，提高开发效率
- **持久化存储**：支持配置和日志等数据的持久化存储
- **缓存机制**：合理使用缓存，提高API响应速度
- **数据一致性**：确保数据的一致性和可靠性

#### 6.2.3 3. 错误处理

- **统一异常处理**：实现统一的异常处理机制
- **错误代码**：定义详细的错误代码和错误信息
- **日志记录**：适当的日志记录，便于问题排查
- **容错机制**：实现必要的容错机制，提高系统稳定性

#### 6.2.4 4. 性能优化

- **代码优化**：减少不必要的计算和资源使用
- **数据结构**：选择合适的数据结构，提高操作效率
- **并发处理**：优化并发处理，提高系统吞吐量
- **资源管理**：合理管理系统资源，避免资源泄漏

#### 6.2.5 5. 安全管理

- **访问控制**：实现必要的访问控制机制
- **数据安全**：保护敏感数据，防止未授权访问
- **输入验证**：验证所有用户输入，防止注入攻击
- **安全日志**：记录安全相关的操作和事件

## 7. 代码示例

### 7.1 SDK开发代码示例

#### 7.1.1 1. 终端发现实现示例

```java
// 在McpAgentSkillImpl中实现终端发现
public List<Map<String, Object>> discoverEndAgents() {
    log.info("Discovering end agents...");
    List<Map<String, Object>> discoveredAgents = new ArrayList<>();
    
    try {
        // 使用SDK的网络扫描功能发现终端
        // 这里模拟终端发现过程
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> agent = new HashMap<>();
            agent.put("id", "endagent-" + System.currentTimeMillis() + "-" + i);
            agent.put("name", "智能设备 " + i);
            agent.put("type", i % 3 == 0 ? "light" : i % 3 == 1 ? "socket" : "camera");
            agent.put("ip", "192.168.1." + (100 + i));
            agent.put("mac", "AA:BB:CC:DD:EE:" + String.format("%02X", i));
            agent.put("status", "active");
            agent.put("discoveredTime", System.currentTimeMillis());
            discoveredAgents.add(agent);
        }
        
        log.info("Discovered {} end agents", discoveredAgents.size());
    } catch (Exception e) {
        log.error("Error discovering end agents: {}", e.getMessage());
    }
    
    return discoveredAgents;
}
```

#### 7.1.2 2. 路由计算实现示例

```java
// 在McpAgentSkillImpl中实现路由计算
public Map<String, Object> calculateRoute(String sourceAgentId, String targetAgentId) {
    log.info("Calculating route from {} to {}", sourceAgentId, targetAgentId);
    Map<String, Object> route = new HashMap<>();
    
    try {
        // 使用SDK的路由计算功能
        // 这里模拟路由计算过程
        route.put("id", "route-" + System.currentTimeMillis());
        route.put("source", sourceAgentId);
        route.put("target", targetAgentId);
        route.put("path", Arrays.asList(sourceAgentId, "route-agent-001", targetAgentId));
        route.put("latency", 15);
        route.put("bandwidth", "100Mbps");
        route.put("status", "active");
        route.put("calculatedTime", System.currentTimeMillis());
        
        log.info("Calculated route with latency: {}ms", route.get("latency"));
    } catch (Exception e) {
        log.error("Error calculating route: {}", e.getMessage());
    }
    
    return route;
}
```

### 7.2 自身开发代码示例

#### 7.2.1 1. 移除链路API实现示例

```java
// 自身开发的移除链路API
@DeleteMapping("/network/link/{linkId}")
public Map<String, Object> removeNetworkLink(@PathVariable String linkId) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        if (mcpAgentSkill instanceof McpAgentSkillImpl) {
            McpAgentSkillImpl skillImpl = (McpAgentSkillImpl) mcpAgentSkill;
            skillImpl.removeNetworkLink(linkId);
            
            response.put("status", "success");
            response.put("message", "网络链路移除成功");
            response.put("linkId", linkId);
            response.put("timestamp", System.currentTimeMillis());
        } else {
            response.put("status", "error");
            response.put("message", "McpAgentSkill is not an instance of McpAgentSkillImpl");
        }
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", e.getMessage());
    }
    
    return response;
}
```

#### 7.2.2 2. 日志管理API实现示例

```java
// 自身开发的日志管理API
@GetMapping("/log/list")
public Map<String, Object> getLogs(@RequestParam(defaultValue = "50") int limit, 
                                 @RequestParam(required = false) String level, 
                                 @RequestParam(required = false) Long startTime, 
                                 @RequestParam(required = false) Long endTime) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        List<Map<String, Object>> logs = new ArrayList<>();
        // 模拟日志数据
        for (int i = 0; i < Math.min(limit, 10); i++) {
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", "log-" + (System.currentTimeMillis() + i));
            logEntry.put("level", i % 3 == 0 ? "INFO" : i % 3 == 1 ? "WARN" : "ERROR");
            logEntry.put("message", "系统日志信息 " + i);
            logEntry.put("timestamp", System.currentTimeMillis() - (i * 60000));
            logEntry.put("source", "system");
            logs.add(logEntry);
        }
        
        response.put("status", "success");
        response.put("data", logs);
        response.put("count", logs.size());
        response.put("timestamp", System.currentTimeMillis());
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", e.getMessage());
    }
    
    return response;
}
```

#### 7.2.3 3. 配置管理API实现示例

```java
// 自身开发的配置管理API
@PostMapping("/config/save")
public Map<String, Object> saveConfig(@RequestBody Map<String, Object> config) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        // 保存配置到文件或数据库
        // 这里模拟配置保存过程
        Properties properties = new Properties();
        
        // 转换配置数据
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue().toString());
        }
        
        // 保存到文件
        try (OutputStream output = new FileOutputStream("mcp-agent.properties")) {
            properties.store(output, "MCP Agent Configuration");
        }
        
        response.put("status", "success");
        response.put("message", "配置保存成功");
        response.put("timestamp", System.currentTimeMillis());
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", e.getMessage());
    }
    
    return response;
}
```

## 8. 测试计划

### 8.1 SDK开发测试

#### 8.1.1 1. 单元测试

- **命令处理测试**：测试各种MCP命令的处理逻辑
- **终端管理测试**：测试终端设备的发现、注册和状态管理
- **路由管理测试**：测试路由的发现、计算和更新
- **网络管理测试**：测试网络链路和拓扑的管理
- **错误处理测试**：测试各种错误场景的处理

#### 8.1.2 2. SDK集成测试

- **SDK初始化测试**：测试Agent SDK的初始化和配置
- **命令集成测试**：测试命令处理与SDK的集成
- **网络集成测试**：测试网络管理与SDK的集成
- **终端集成测试**：测试终端管理与SDK的集成
- **路由集成测试**：测试路由管理与SDK的集成

### 8.2 自身开发测试

#### 8.2.1 1. 单元测试

- **API功能测试**：测试每个API的功能是否正常
- **参数验证测试**：测试API参数的验证和错误处理
- **响应格式测试**：测试API响应格式是否一致
- **边界情况测试**：测试各种边界情况的处理
- **异常处理测试**：测试异常场景的处理机制

#### 8.2.2 2. 集成测试

- **API集成测试**：测试API之间的集成
- **前端集成测试**：测试前端调用API的集成
- **数据集成测试**：测试数据存储和读取的集成
- **配置集成测试**：测试配置管理的集成
- **日志集成测试**：测试日志管理的集成

### 8.3 性能测试

#### 8.3.1 1. SDK性能测试

- **命令处理性能**：测试命令处理的响应时间和并发处理能力
- **终端发现性能**：测试终端发现的速度和效率
- **路由计算性能**：测试路由计算的速度和资源使用
- **网络拓扑性能**：测试网络拓扑构建和更新的性能

#### 8.3.2 2. 自身API性能测试

- **API响应时间**：测试API的响应时间
- **并发处理能力**：测试API的并发处理能力
- **大数据量处理**：测试API处理大数据量的性能
- **资源使用**：测试API的内存和CPU使用情况
- **稳定性测试**：测试API在长时间运行下的稳定性

## 9. 部署计划

### 9.1 SDK开发部署

#### 9.1.1 1. SDK配置

- **SDK版本管理**：确保使用正确版本的Agent SDK
- **SDK配置**：配置SDK的必要参数，如端点、心跳间隔等
- **依赖管理**：管理SDK的依赖关系，确保版本兼容
- **环境配置**：根据不同环境配置SDK参数

#### 9.1.2 2. 部署步骤

- **构建**：使用Maven构建包含SDK的项目
- **打包**：生成包含SDK依赖的可执行jar包
- **部署**：部署到目标环境
- **初始化**：初始化SDK并验证配置
- **测试**：测试SDK功能是否正常

### 9.2 自身开发部署

#### 9.2.1 1. 构建

- **代码构建**：使用Maven构建项目
- **依赖管理**：管理项目依赖
- **资源打包**：打包配置文件和静态资源
- **生成jar包**：生成可执行的jar包

#### 9.2.2 2. 部署

- **本地环境部署**：部署到本地开发环境进行测试
- **测试环境部署**：部署到测试环境进行集成测试
- **生产环境部署**：部署到生产环境
- **容器化部署**：使用Docker容器化部署（可选）

### 9.3 监控和维护

#### 9.3.1 1. SDK监控

- **命令处理监控**：监控命令处理的成功率和响应时间
- **网络状态监控**：监控网络链路和拓扑的状态
- **终端状态监控**：监控终端设备的状态
- **路由状态监控**：监控路由的状态和性能

#### 9.3.2 2. 自身API监控

- **API调用监控**：监控API的调用情况和错误率
- **系统性能监控**：监控系统的CPU、内存和磁盘使用情况
- **日志监控**：监控系统日志，及时发现问题
- **告警管理**：设置告警规则，及时通知异常情况

#### 9.3.3 3. 维护计划

- **定期更新**：定期更新SDK和依赖库
- **性能优化**：根据监控数据优化系统性能
- **安全加固**：定期进行安全检查和加固
- **故障演练**：定期进行故障演练，提高系统可靠性
- **文档更新**：及时更新API文档和使用指南

## 10. 总结

本开发文档详细描述了未实现API的开发计划和技术实现要点，特别将API分为SDK开发和自身开发两部分，为后续的API开发提供了清晰的指导。

### 10.1 SDK开发总结

**SDK开发API**主要包括：
- **网络链路管理**：完善网络链路的创建、移除、更新和查询功能
- **终端管理**：实现终端设备的发现、注册、状态查询和管理
- **路由管理**：实现路由的发现、更新、查询和管理
- **命令处理**：完善MCP命令的处理机制，支持更多命令类型
- **网络拓扑**：实现网络拓扑的构建、更新和查询

**技术要点**：
- 正确集成和使用Agent SDK
- 实现McpAgentSkill接口处理命令
- 利用SDK的网络管理能力
- 实现终端和路由的自动发现机制

### 10.2 自身开发总结

**自身开发API**主要包括：
- **移除链路API**：实现 `/api/mcp/network/link/{linkId}` (DELETE) 接口
- **日志管理API**：实现系统日志的收集、查询和管理
- **配置管理API**：实现系统配置的保存、加载和重置
- **系统监控API**：完善系统监控功能，包括指标采集和告警
- **批量操作API**：优化批量操作功能，提高处理效率

**技术要点**：
- 利用Spring Boot框架实现RESTful API
- 实现统一的错误处理机制
- 支持数据的持久化存储
- 优化API性能和资源使用
- 加强API的安全性

### 10.3 实施建议

1. **优先顺序**：先实现SDK开发API，再实现自身开发API
2. **测试重点**：重点测试SDK集成和API功能
3. **监控重点**：监控SDK性能和API调用情况
4. **维护计划**：定期更新SDK和依赖库，优化系统性能

通过实现这些API，可以完善MCP Agent的功能，提高系统的可靠性和易用性，为前端提供完整的API支持。

---

**文档版本**：1.1
**创建日期**：2026-01-30
**更新日期**：2026-01-30
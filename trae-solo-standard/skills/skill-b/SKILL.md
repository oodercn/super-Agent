# SKILL B：数据提交服务

## 技能概述

SKILL B 是一个专注于数据提交的服务，负责将处理后的数据提交到目标系统。它支持多种目标系统类型，包括 API、文件系统和数据库，提供了统一的接口来处理不同类型的数据提交需求。

## 核心功能

### 1. 数据提交

- **支持多种目标系统**：API、文件系统、数据库
- **事务管理**：确保数据提交的原子性
- **批量提交**：支持大批量数据的高效提交
- **状态跟踪**：实时跟踪提交状态和结果

### 2. 错误处理

- **重试机制**：自动重试失败的提交操作
- **异常处理**：优雅处理各种异常情况
- **错误报告**：详细的错误信息和日志

### 3. 配置管理

- **灵活配置**：支持通过参数和配置文件进行定制
- **环境适配**：根据不同环境自动调整配置
- **安全配置**：敏感信息的安全处理

## 使用指南

### 基本用法

```bash
# 提交数据到 API
bash scripts/submit-data.sh --target "api" --data '{"id": 1, "name": "Test"}' --options '{"url": "https://api.example.com/submit"}'

# 提交数据到文件
bash scripts/submit-data.sh --target "file" --data '{"id": 1, "name": "Test"}' --options '{"path": "output.json"}'

# 提交数据到数据库
bash scripts/submit-data.sh --target "database" --data '{"id": 1, "name": "Test"}' --options '{"connection": "localhost:3306", "table": "users"}'
```

### 参数说明

| 参数 | 类型 | 必需 | 描述 |
|------|------|------|------|
| target | string | 是 | 目标系统类型 (api/file/database) |
| data | object | 是 | 要提交的数据 |
| options | object | 否 | 提交选项，根据目标系统类型不同而不同 |

### 选项说明

#### API 目标系统选项

| 选项 | 类型 | 必需 | 描述 |
|------|------|------|------|
| url | string | 是 | API 端点 URL |
| method | string | 否 | HTTP 方法 (默认: POST) |
| headers | object | 否 | HTTP 头部 |
| timeout | number | 否 | 超时时间 (默认: 30秒) |

#### 文件目标系统选项

| 选项 | 类型 | 必需 | 描述 |
|------|------|------|------|
| path | string | 是 | 文件路径 |
| format | string | 否 | 文件格式 (默认: json) |
| mode | string | 否 | 写入模式 (默认: write) |

#### 数据库目标系统选项

| 选项 | 类型 | 必需 | 描述 |
|------|------|------|------|
| connection | string | 是 | 数据库连接字符串 |
| table | string | 是 | 目标表名 |
| mode | string | 否 | 操作模式 (默认: insert) |
| username | string | 否 | 数据库用户名 |
| password | string | 否 | 数据库密码 |

## 工作流程

1. **参数验证**：验证输入参数的有效性
2. **目标系统选择**：根据 target 参数选择对应的提交策略
3. **数据准备**：根据目标系统的要求格式化数据
4. **执行提交**：执行数据提交操作
5. **结果处理**：处理提交结果，包括成功和失败情况
6. **返回结果**：返回详细的提交结果信息

## 示例场景

### 场景 1：提交用户数据到 CRM 系统

```bash
bash scripts/submit-data.sh --target "api" --data '{"id": 123, "name": "张三", "email": "zhangsan@example.com", "phone": "13800138000"}' --options '{"url": "https://crm.example.com/api/users", "headers": {"Authorization": "Bearer token123"}}'
```

### 场景 2：生成并保存报表文件

```bash
bash scripts/submit-data.sh --target "file" --data '{"reportId": "RPT-2026-01", "title": "月度销售报表", "data": [{"product": "A", "sales": 1000}, {"product": "B", "sales": 2000}]}' --options '{"path": "reports/monthly-sales-2026-01.json", "format": "json"}'
```

### 场景 3：向数据库写入处理结果

```bash
bash scripts/submit-data.sh --target "database" --data '{"orderId": "ORD-001", "customerId": "CUST-001", "amount": 99.99, "status": "completed"}' --options '{"connection": "localhost:3306", "table": "orders", "mode": "insert", "username": "admin", "password": "password"}'
```

## 与其他服务的集成

### 与 SKILL A 集成

SKILL B 可以接收来自 SKILL A 的数据并将其提交到目标系统：

1. SKILL A 从数据源获取原始数据
2. 数据经过处理后传递给 SKILL B
3. SKILL B 将处理后的数据提交到目标系统
4. SKILL B 返回提交结果给调用方

### 与 SKILL C 集成

SKILL B 作为 SKILL C 协调的工作流中的一个环节：

1. SKILL C 启动工作流
2. SKILL C 调用 SKILL A 获取数据
3. SKILL C 处理数据
4. SKILL C 调用 SKILL B 提交数据
5. SKILL B 返回提交结果给 SKILL C
6. SKILL C 汇总结果并返回给用户

## 性能优化

### 1. 批量处理

对于大批量数据，建议使用批量提交模式，减少网络请求次数：

```bash
bash scripts/submit-data.sh --target "api" --data '{"batch": true, "records": [{"id": 1, "name": "Test1"}, {"id": 2, "name": "Test2"}]}' --options '{"url": "https://api.example.com/batch-submit"}'
```

### 2. 并发处理

对于多个独立的数据提交任务，可以考虑并发执行以提高效率：

```bash
# 并发提交多个数据
bash scripts/submit-data.sh --target "api" --data '{"id": 1, "name": "Test1"}' --options '{"url": "https://api.example.com/submit"}' &
bash scripts/submit-data.sh --target "api" --data '{"id": 2, "name": "Test2"}' --options '{"url": "https://api.example.com/submit"}' &
wait
```

### 3. 缓存策略

对于重复提交的数据，可以使用缓存来避免重复处理：

```bash
bash scripts/submit-data.sh --target "api" --data '{"id": 1, "name": "Test"}' --options '{"url": "https://api.example.com/submit", "cache": true}'
```

## 故障排除

### 常见问题

1. **提交失败**
   - 检查目标系统是否可用
   - 验证数据格式是否正确
   - 检查网络连接是否正常

2. **超时错误**
   - 增加超时时间
   - 减少单次提交的数据量
   - 检查目标系统的响应速度

3. **权限错误**
   - 验证目标系统的访问权限
   - 检查认证信息是否正确
   - 确认操作是否被目标系统允许

### 错误代码

| 错误代码 | 描述 | 解决方案 |
|----------|------|----------|
| 400 | 请求参数错误 | 检查数据格式和参数 |
| 401 | 未授权 | 检查认证信息 |
| 403 | 禁止访问 | 检查权限设置 |
| 404 | 目标不存在 | 检查目标系统地址 |
| 500 | 服务器错误 | 检查目标系统状态 |
| 502 | 网关错误 | 检查网络连接 |
| 503 | 服务不可用 | 检查目标系统状态 |

## 版本历史

| 版本 | 日期 | 主要变化 |
|------|------|----------|
| 0.1.0 | 2026-01-22 | 初始版本，实现基本数据提交功能 |

## 配置文件

SKILL B 支持通过配置文件进行定制，配置文件位于 `config/config.json`：

```json
{
  "timeout": 30,
  "retryCount": 3,
  "retryDelay": 1000,
  "defaultHeaders": {
    "Content-Type": "application/json"
  },
  "targets": {
    "api": {
      "defaultMethod": "POST",
      "timeout": 60
    },
    "file": {
      "defaultPath": "./output",
      "defaultFormat": "json"
    },
    "database": {
      "defaultMode": "insert"
    }
  }
}
```

## 安全注意事项

1. **敏感信息**：不要在命令行参数中传递敏感信息
2. **认证信息**：使用环境变量或配置文件存储认证信息
3. **数据加密**：对于敏感数据，确保使用加密传输
4. **访问控制**：限制对提交接口的访问权限
5. **日志安全**：确保日志中不包含敏感信息

## 扩展指南

### 添加新的目标系统类型

1. 在 `scripts/submit-data.sh` 中添加新的目标系统处理逻辑
2. 更新 `skill.yaml` 中的参数说明
3. 更新 `SKILL.md` 中的文档
4. 测试新的目标系统类型

### 自定义提交策略

1. 创建自定义提交脚本
2. 在 `scripts/submit-data.sh` 中集成自定义策略
3. 更新配置文件以支持新策略
4. 测试自定义策略

## 联系方式

- **作者**：OODER Team
- **邮箱**：team@ooder.ai
- **文档**：https://docs.ooder.ai/skills/skill-b
- **GitHub**：https://github.com/ooderai/ooder-public
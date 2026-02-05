# OODER A2UI Skill 配置文件

## 基本信息

```json
{
  "skillId": "ooder-a2ui",
  "name": "OODER A2UI",
  "description": "提供图生代码功能，采用ooder-a2ui格式生成前端界面",
  "version": "1.0.0",
  "ownerType": "ORGANIZATION",
  "ownerId": "ooder-org",
  "categories": ["ui", "frontend", "code-generation", "ai"],
  "skillType": "UI_SKILL",
  "techStack": "JavaScript, HTML, CSS, ES6, Spring Boot",
  "deploymentType": "local",
  "location": {
    "spaceId": "local-space",
    "spaceName": "local",
    "zoneId": "default-zone",
    "zoneName": "default"
  },
  "isStatic": true,
  "isAiServerAuthenticated": true,
  "publicKey": "-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp9e9e9e9e9e9e9e9e9e9e
9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9
e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e
9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9e9
9wIDAQAB
-----END PUBLIC KEY-----",
  "interfaces": [
    {
      "interfaceId": "generate-ui-001",
      "name": "生成UI界面",
      "description": "根据输入的图片生成ooder-a2ui格式的前端界面",
      "method": "POST",
      "path": "/generate-ui",
      "params": [
        {
          "name": "image",
          "type": "string",
          "required": true,
          "description": "UI设计图片的base64编码或URL"
        },
        {
          "name": "format",
          "type": "string",
          "required": false,
          "description": "输出格式: html, js, json"
        },
        {
          "name": "options",
          "type": "object",
          "required": false,
          "description": "生成选项，如组件库版本、主题等"
        }
      ],
      "returnType": "json",
      "categories": ["ui", "code-generation"]
    },
    {
      "interfaceId": "preview-ui-001",
      "name": "预览UI界面",
      "description": "预览生成的UI界面",
      "method": "POST",
      "path": "/preview-ui",
      "params": [
        {
          "name": "uiCode",
          "type": "string",
          "required": true,
          "description": "UI代码"
        },
        {
          "name": "format",
          "type": "string",
          "required": false,
          "description": "代码格式: html, js"
        }
      ],
      "returnType": "html",
      "categories": ["ui", "preview"]
    },
    {
      "interfaceId": "get-components-001",
      "name": "获取组件列表",
      "description": "获取ooder-a2ui支持的组件列表",
      "method": "GET",
      "path": "/components",
      "params": [],
      "returnType": "json",
      "categories": ["ui", "components"]
    }
  ],
  "endpoints": [
    {
      "endpointId": "endpoint-001",
      "protocol": "HTTP",
      "host": "localhost",
      "port": 8081,
      "path": "/api/a2ui",
      "description": "主接口端点"
    }
  ],
  "accessPolicy": {
    "policyId": "access-policy-001",
    "permissions": [
      {
        "resource": "*",
        "actions": ["read", "write", "execute"],
        "effect": "allow",
        "conditions": {
          "agentType": ["EndAgent", "RouteAgent"]
        }
      }
    ],
    "defaultEffect": "deny"
  },
  "vfsFilePath": "/skills/ooder-a2ui.json"
}
```

## 功能说明

OODER A2UI Skill 是一个提供图生代码功能的技能，其主要特点包括：

1. **图生代码**：根据输入的UI设计图片，自动生成ooder-a2ui格式的前端界面代码
2. **多种输出格式**：支持生成HTML、JavaScript、JSON等多种格式的代码
3. **组件库支持**：基于ooder-a2ui组件库，提供丰富的前端组件
4. **实时预览**：支持生成的UI界面的实时预览
5. **自定义选项**：支持设置组件库版本、主题等生成选项

## 使用示例

### 示例1：生成UI界面

```json
{
  "image": "base64编码的UI设计图片",
  "format": "html",
  "options": {
    "theme": "light",
    "componentVersion": "1.0.0"
  }
}
```

### 示例2：预览UI界面

```json
{
  "uiCode": "生成的HTML代码",
  "format": "html"
}
```

### 示例3：获取组件列表

```json
{
  "method": "GET",
  "path": "/components"
}
```

## 注册信息

- **Agent ID**: `ooder-a2ui-001`
- **Agent Name**: `OODERA2UI`
- **Agent Type**: `end`
- **Endpoint**: `http://localhost:8081`
- **UDP Port**: `9006`

## 依赖关系

- **Spring Boot**: 3.2.4
- **OODER UI**: 1.0.0
- **ES6 Modules**: 支持

## 部署说明

1. **编译打包**：`mvn clean package -DskipTests`
2. **运行**：`java -jar skills-a2ui-1.0.0.jar`
3. **访问**：`http://localhost:8081`
4. **调试**：访问 `http://localhost:8081/index.html` 进行前端调试

## 图生代码流程

1. **输入图片**：用户上传UI设计图片
2. **AI分析**：系统分析图片中的UI元素和布局
3. **组件识别**：识别图片中的UI组件类型
4. **代码生成**：生成对应的ooder-a2ui格式代码
5. **预览验证**：用户预览生成的界面
6. **代码导出**：导出最终的前端代码

## 支持的组件

- **基础组件**：Button, Input, Label, CheckBox, RadioBox
- **布局组件**：Layout, Panel, Group, ContentBlock
- **导航组件**：Tabs, MenuBar, ToolBar
- **数据展示**：List, TreeView, TreeGrid, Table
- **表单组件**：FormLayout, DatePicker, TimePicker, ColorPicker
- **弹窗组件**：Dialog, PopMenu
- **媒体组件**：Image, Audio, Video
- **图表组件**：ECharts, FusionChartsXT

## 安全配置

- **访问控制**: 基于策略的访问控制
- **认证方式**: 公钥认证
- **通信安全**: 支持HTTP和HTTPS通信

## 监控与日志

- **日志级别**: INFO
- **日志输出**: 控制台和文件
- **监控指标**: 代码生成时间、成功率、组件使用率

## 版本历史

- **1.0.0**: 初始版本，支持基本的图生代码功能

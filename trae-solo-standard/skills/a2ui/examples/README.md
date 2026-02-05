# A2UI 技能示例

本目录包含 A2UI 技能的使用示例，帮助用户快速了解如何使用该技能。

## 示例列表

### 1. 基础示例

#### 获取组件列表

```bash
# 执行命令
./scripts/get-components.sh

# 预期输出
{
  "success": true,
  "data": [
    {"id": "button", "name": "Button", "category": "基础组件", "description": "按钮组件"},
    {"id": "input", "name": "Input", "category": "基础组件", "description": "输入框组件"},
    {"id": "panel", "name": "Panel", "category": "容器组件", "description": "面板组件"},
    {"id": "tabs", "name": "Tabs", "category": "容器组件", "description": "标签页组件"},
    {"id": "form", "name": "Form", "category": "容器组件", "description": "表单组件"}
  ],
  "metadata": {
    "function": "get-components",
    "executionTime": 100,
    "skillId": "a2ui"
  }
}
```

#### 获取组件详细信息

```bash
# 执行命令
./scripts/get-component-details.sh --component-id "button"

# 预期输出
{
  "success": true,
  "data": {
    "id": "button",
    "name": "Button",
    "category": "基础组件",
    "description": "按钮组件",
    "properties": {
      "text": "按钮文本",
      "type": "按钮类型",
      "disabled": "是否禁用"
    },
    "styles": {
      "color": "按钮颜色",
      "fontSize": "字体大小",
      "padding": "内边距"
    },
    "events": {
      "click": "点击事件"
    },
    "behaviors": {
      "hover": "悬停效果",
      "active": "激活效果"
    }
  },
  "metadata": {
    "function": "get-component-details",
    "executionTime": 100,
    "skillId": "a2ui"
  }
}
```

### 2. 高级示例

#### 创建视图

```bash
# 执行命令
./scripts/create-view.sh \
  --view-name "LoginForm" \
  --components '[{"id": "input", "properties": {"name": "username", "placeholder": "用户名"}}, {"id": "input", "properties": {"name": "password", "type": "password", "placeholder": "密码"}}, {"id": "button", "properties": {"text": "登录", "type": "primary"}}]' \
  --layout '{"type": "form", "columns": 1}'

# 预期输出
{
  "success": true,
  "data": {
    "viewId": "1",
    "viewName": "LoginForm",
    "components": ["Input", "Button"],
    "layout": {
      "type": "form",
      "columns": 2
    },
    "status": "created"
  },
  "metadata": {
    "function": "create-view",
    "executionTime": 200,
    "skillId": "a2ui"
  }
}
```

#### 提交数据

```bash
# 执行命令
./scripts/submit-data.sh \
  --target "database" \
  --data '{"id": 1, "name": "Test User", "email": "test@example.com"}' \
  --options '{"mode": "insert"}'

# 预期输出
{
  "success": true,
  "data": {
    "success": true,
    "message": "数据提交成功",
    "data": {
      "id": 1,
      "status": "processed"
    }
  },
  "metadata": {
    "function": "submit-data",
    "executionTime": 150,
    "skillId": "a2ui"
  }
}
```

### 3. trae-solo IDE 集成示例

#### 在 IDE 中调用技能

**输入**：
```
使用 A2UI 技能生成登录界面，图片为 "https://example.com/login.png"
```

**预期输出**：
```
执行结果：
UI 代码已生成，包含以下组件：Button、Input、Panel、Label
执行时间：4567ms
预览地址：http://localhost:8081/api/a2ui/preview-ui?code=...
```

## 四分离设计原则示例

以下是一个遵循四分离设计原则的组件定义示例：

```javascript
ood.Class("ood.UI.CustomButton", ["ood.UI", "ood.absComponent"], {
    Static: {
        // 1. 属性（Properties）
        DataModel: {
            text: {
                defaultValue: "按钮",
                description: "按钮文本"
            },
            type: {
                defaultValue: "primary",
                description: "按钮类型"
            },
            disabled: {
                defaultValue: false,
                description: "是否禁用"
            }
        },
        
        // 2. 样式（Styles）
        Appearances: {
            '.ood-button': {
                padding: '8px 16px',
                borderRadius: '4px',
                cursor: 'pointer'
            },
            '.ood-button-primary': {
                backgroundColor: '#007bff',
                color: 'white'
            },
            '.ood-button-disabled': {
                backgroundColor: '#ccc',
                cursor: 'not-allowed'
            }
        },
        
        // 3. 事件（Events）
        EventHandlers: {
            '.ood-button': {
                click: function() {
                    this.fireEvent('click', this);
                }
            }
        },
        
        // 4. 行为（Behaviors）
        Behaviors: {
            '.ood-button': {
                hover: function() {
                    this.addClass('ood-button-hover');
                },
                mouseout: function() {
                    this.removeClass('ood-button-hover');
                }
            }
        }
    }
});
```

## 注意事项

1. **网络连接**：示例执行需要网络连接，确保网络正常
2. **服务可用性**：依赖 A2UI 服务和 ooder-agent 服务的可用性
3. **参数格式**：确保提供的参数格式正确，尤其是 JSON 格式的参数
4. **执行时间**：对于复杂的操作，可能需要较长的执行时间

## 故障排除

如果遇到问题，可以参考以下解决方案：

1. **服务不可用**：检查 A2UI 服务和 ooder-agent 服务是否正在运行
2. **参数错误**：确保提供的参数格式正确，尤其是 JSON 格式的参数
3. **执行超时**：对于复杂的操作，可能需要较长的执行时间
4. **结果格式错误**：检查服务返回的 JSON 格式是否正确

## 更多示例

更多使用示例，请参考 A2UI 技能的 [SKILL.md](../SKILL.md) 文件。
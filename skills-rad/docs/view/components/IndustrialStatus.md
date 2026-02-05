# IndustrialStatus 组件

## 1. 组件概述
IndustrialStatus 是 ooder-a2ui 框架中的工业主态图组件，用于展示工业控制系统的实时状态，包括设备运行状态、物料流动、系统状态和实时数据监控。该组件基于 SVGPaper 构建，提供直观的可视化界面，便于操作人员监控和管理工业生产流程。

## 2. 组件特性
- 直观的工业控制系统状态展示
- 支持多种设备类型的可视化表示
- 实时数据监控和状态指示
- 设备间连接关系可视化
- 紧急控制功能集成
- 系统状态实时反馈
- 可扩展的模块化设计
- 响应式布局，自适应不同屏幕尺寸

## 3. 组件创建

### 3.1 JSON格式创建
```json
{
  "type": "ooder.IndustrialStatus",
  "id": "industrialStatus1",
  "properties": {
    "caption": "工业主态图",
    "title": "工业主态图",
    "dock": "fill",
    "panelType": "block"
  }
}
```

### 3.2 JavaScript格式创建
```javascript
var industrialStatus = ood.create("ooder.IndustrialStatus")
    .setId("industrialStatus1")
    .setCaption("工业主态图")
    .setTitle("工业主态图")
    .setDock("fill")
    .setPanelType("block");
```

## 4. 组件属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| autoDestroy | Boolean | true | 组件是否自动销毁 |
| bindClass | Array | [] | 绑定的类 |
| caption | String | "工业主态图" | 组件标题 |
| currComponentAlias | String | "IndustrialStatus" | 当前组件别名 |
| dock | String | "fill" | 组件停靠方式 |
| panelType | String | "block" | 面板类型 |
| title | String | "工业主态图" | 组件标题 |

## 5. 组件结构

IndustrialStatus 组件主要由以下部分组成：

1. **SVGPaper 容器**：作为整个工业主态图的画布
2. **标题区域**：显示系统名称
3. **背景网格**：辅助定位和布局
4. **生产流水线区域**：展示主要生产设备和流程
5. **设备节点**：
   - 原料存储罐（圆形）
   - 混合器（矩形）
   - 加热器（矩形）
   - 冷却器（矩形）
   - 包装机（矩形）
   - 成品存储区（矩形）
6. **连接线**：表示设备间的物料流动关系
7. **状态指示器**：显示系统整体状态
8. **紧急控制区域**：包含紧急停止按钮
9. **数据监控面板**：显示实时生产数据
10. **图例**：说明各种图形元素的含义

## 6. 设备类型与状态

### 6.1 设备类型

| 设备类型 | 图形表示 | 颜色 | 说明 |
|----------|----------|------|------|
| 原料存储罐 | 圆形 | 绿色/黄色 | 存储生产原料 |
| 混合器 | 矩形 | 蓝色 | 混合多种原料 |
| 加热器 | 矩形 | 红色 | 加热处理物料 |
| 冷却器 | 矩形 | 青色 | 冷却处理物料 |
| 包装机 | 矩形 | 紫色 | 包装成品 |
| 成品存储区 | 矩形 | 黄色 | 存储成品 |

### 6.2 状态颜色编码

| 状态 | 颜色 | 说明 |
|------|------|------|
| 正常 | 绿色 (#2ecc71) | 设备运行正常 |
| 警告 | 黄色 (#f39c12) | 设备需要关注 |
| 故障 | 红色 (#e74c3c) | 设备出现故障 |
| 运行中 | 蓝色 (#3498db) | 设备正在运行 |
| 高温 | 红色 (#e74c3c) | 设备温度过高 |
| 已满 | 黄色 (#f1c40f) | 存储区域接近满载 |

## 7. 组件方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| initialize() | 无 | void | 初始化组件 |
| iniComponents() | 无 | Array | 初始化组件内部元素 |

## 8. 事件处理

IndustrialStatus 组件当前未定义特定事件，但可以通过扩展组件来自定义事件处理。

## 9. 组件示例

### 9.1 基本工业主态图
```json
{
  "type": "ooder.IndustrialStatus",
  "id": "industrialStatusBasic",
  "properties": {
    "caption": "工业主态图",
    "title": "工业生产监控系统",
    "dock": "fill"
  }
}
```

### 9.2 自定义工业主态图
```javascript
var customIndustrialStatus = ood.create("ooder.IndustrialStatus")
    .setId("customIndustrialStatus")
    .setCaption("自定义工业主态图")
    .setTitle("化工生产流程监控")
    .setPanelType("block");

// 可以通过扩展组件来添加自定义功能
customIndustrialStatus.extend({
    Instance: {
        // 添加自定义方法
        updateRealTimeData: function(data) {
            // 更新实时数据显示
            var monitorText = "实时数据: 产量: " + data.output + "/小时 | 合格率: " + data.qualifiedRate + "% | 能耗: " + data.energyConsumption + "kW";
            this.svgPaper.get("text_monitor_panel").setAttr({"KEY": {text: monitorText}});
        },
        
        // 更新设备状态
        updateDeviceStatus: function(deviceId, status) {
            // 根据设备ID和状态更新设备显示
            var device = this.svgPaper.get(deviceId);
            if (device) {
                // 根据status更新设备颜色和文本
                switch(status) {
                    case "normal":
                        device.setAttr({"KEY": {fill: "#2ecc71", stroke: "#27ae60"}});
                        break;
                    case "warning":
                        device.setAttr({"KEY": {fill: "#f39c12", stroke: "#e67e22"}});
                        break;
                    case "error":
                        device.setAttr({"KEY": {fill: "#e74c3c", stroke: "#c0392b"}});
                        break;
                }
            }
        }
    }
});
```

## 10. 组件扩展

IndustrialStatus 组件采用模块化设计，可以通过继承和扩展来添加自定义功能：

```javascript
// 扩展 IndustrialStatus 组件
ood.Class("custom.IndustrialStatus", "ooder.IndustrialStatus", {
    Instance: {
        // 重写初始化方法
        initialize: function() {
            // 调用父类初始化
            this.Super("initialize", arguments);
            
            // 添加自定义初始化逻辑
            this.initCustomComponents();
        },
        
        // 自定义组件初始化
        initCustomComponents: function() {
            // 添加自定义组件或功能
        },
        
        // 添加自定义方法
        customMethod: function() {
            // 自定义功能实现
        }
    }
});
```

## 11. 最佳实践

1. **布局设计**：
   - 合理组织设备布局，反映实际生产流程
   - 使用网格辅助定位，保持界面整洁
   - 分组相关设备，便于管理和监控

2. **颜色使用**：
   - 遵循工业标准的颜色编码
   - 保持颜色一致性，便于操作人员理解
   - 使用高对比度颜色，提高可读性

3. **实时数据**：
   - 定期更新实时数据，确保信息准确性
   - 突出显示关键指标，便于快速识别异常
   - 使用趋势图或仪表盘展示历史数据

4. **交互设计**：
   - 添加悬停效果，显示设备详细信息
   - 支持点击操作，查看设备详情
   - 集成紧急控制功能，确保生产安全

5. **性能优化**：
   - 合理控制SVG元素数量，避免性能问题
   - 使用事件委托，减少事件监听器数量
   - 实现数据缓存，减少不必要的更新

## 12. 浏览器兼容性

IndustrialStatus 组件基于 SVGPaper 和 Raphael.js，支持以下浏览器：

- Chrome 4+
- Firefox 3+
- Safari 3.1+
- Internet Explorer 6+
- Opera 9.5+

## 13. 常见问题

### 13.1 组件显示不完整
- 检查容器大小是否足够
- 调整组件的 dock 属性
- 检查 SVGPaper 的尺寸设置

### 13.2 实时数据不更新
- 检查数据更新机制是否正确
- 确保定时器或事件触发正常
- 检查数据绑定是否正确

### 13.3 设备状态不变化
- 检查状态更新方法是否调用
- 确保设备ID和状态值正确
- 检查SVG元素的属性设置是否正确

## 14. 总结

IndustrialStatus 组件是一个功能强大的工业主态图可视化组件，适用于各种工业控制系统的状态监控和展示。它提供了直观的可视化界面，支持多种设备类型和状态的展示，集成了实时数据监控和紧急控制功能。

通过 IndustrialStatus 组件，操作人员可以实时监控工业生产流程，快速识别异常情况，及时采取措施，提高生产效率和安全性。该组件采用模块化设计，便于扩展和定制，可以根据不同行业和应用场景进行调整和优化。

IndustrialStatus 组件的应用范围包括但不限于：
- 化工生产流程监控
- 制造业生产线管理
- 能源系统监控
- 水处理流程监控
- 食品加工流程管理
- 制药行业生产监控

作为 ooder-a2ui 框架的重要组成部分，IndustrialStatus 组件为工业物联网和智能制造提供了强大的可视化支持。
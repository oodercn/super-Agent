# OODDesigner 集成

## 1. 总览

OODDesigner 是 ooder-agent-rad 的核心组件，负责可视化设计功能。本文档将详细介绍 OODDesigner 如何集成到 ooder-agent-rad 中，包括集成设计、实现细节和通信机制。

## 2. OODDesigner 概述

### 2.1 核心功能

OODDesigner 是一个可视化设计器，支持以下核心功能：

- **拖拽式设计**：支持将组件从工具箱拖拽到设计画板
- **实时预览**：实时预览设计效果
- **属性配置**：通过属性面板配置组件属性
- **事件绑定**：为组件绑定事件处理逻辑
- **代码生成**：根据可视化设计生成代码
- **模板支持**：提供多种设计模板

### 2.2 核心实现

**核心实现文件**：[OODDesignerForm.java](../../src/main/java/net/ooder/editor/OODDesignerForm.java)

OODDesignerForm 是 OODDesigner 的核心实现类，负责处理设计器的初始化、组件管理、事件处理等功能。

## 3. 集成设计

### 3.1 模块化设计

OODDesigner 采用模块化设计，将复杂的设计功能拆分为多个独立的模块：

| 模块名称 | 职责 |
|---------|------|
| 工具箱模块 | 提供组件库，支持拖拽操作 |
| 设计画板模块 | 提供设计区域，支持组件的放置、调整和删除 |
| 属性面板模块 | 提供属性配置界面，支持组件属性的修改 |
| 事件面板模块 | 提供事件绑定界面，支持为组件绑定事件处理逻辑 |
| 样式面板模块 | 提供样式配置界面，支持组件样式的调整 |
| 代码生成模块 | 根据可视化设计生成代码 |

### 3.2 集成方式

OODDesigner 通过注解驱动的方式集成到 ooder-agent-rad 中，主要包括以下步骤：

1. **创建设计器控制器**：创建 OODDesigner 控制器，处理设计器相关的请求
2. **定义设计器视图**：使用注解定义设计器的视图结构
3. **实现设计器功能**：实现设计器的核心功能
4. **集成到主视图**：将设计器集成到主视图中

## 4. 实现细节

### 4.1 设计器控制器

创建 OODDesigner 控制器，处理设计器相关的请求：

```java
@Controller
@RequestMapping("/rad/designer/")
public class OODDesignerController {
    
    @RequestMapping(method = RequestMethod.POST, value = "embed")
    @ModuleAnnotation(caption = "OOD设计器", panelType = PanelType.panel)
    @PanelViewAnnotation()
    @ResponseBody
    public ModuleComponent embedDesigner() {
        ModuleComponent component = new ModuleComponent("RAD.OODDesigner");
        component.setClassName("RAD.OODDesigner");
        component.setProperties(Collections.singletonMap("embedMode", true));
        return component;
    }
    
    @ActionAnnotation(name = "saveDesign", caption = "保存设计", icon = "ri-save-line")
    @RequestMapping(method = RequestMethod.POST, value = "save")
    @ResponseBody
    public ResultModel<Boolean> saveDesign(@RequestBody DesignData data) {
        // 保存设计逻辑
        return ResultModel.success(true);
    }
}
```

### 4.2 设计器视图集成

将 OODDesigner 集成到主视图中：

```java
@RequestMapping(method = RequestMethod.POST, value = "designer")
@LayoutItemAnnotation(pos = PosType.main)
@ModuleAnnotation(caption = "设计器")
@PanelViewAnnotation()
@ResponseBody
public ModuleComponent getDesignerView() {
    ModuleComponent component = new ModuleComponent("RAD.OODDesigner");
    component.setClassName("RAD.OODDesigner");
    return component;
}
```

### 4.3 设计器初始化

OODDesigner 的初始化流程包括：

1. **加载设计器资源**：加载设计器所需的 CSS、JavaScript 和图像资源
2. **初始化工具箱**：初始化组件库，加载可用的组件
3. **初始化设计画板**：创建设计区域，设置网格和辅助线
4. **初始化属性面板**：创建属性配置界面
5. **初始化事件面板**：创建事件绑定界面
6. **初始化样式面板**：创建样式配置界面

## 5. 通信机制

### 5.1 前后端通信

OODDesigner 采用多种通信方式与后端进行交互：

| 通信方式 | 用途 | 实现方式 |
|---------|------|---------|
| REST API | 处理同步请求，如保存设计、加载设计等 | @RequestMapping 注解定义 API 接口 |
| WebSocket | 实现实时双向通信，如设计同步、协作编辑等 | Spring WebSocket 支持 |
| 事件总线 | 处理组件间的事件通信 | Spring Event 机制 |

### 5.2 组件间通信

OODDesigner 内部组件之间采用事件驱动的通信机制：

```java
// 发布组件选择事件
ood.Event.fire('componentSelected', component);

// 订阅组件选择事件
ood.Event.on('componentSelected', function(component) {
    // 处理组件选择事件
    updatePropertyPanel(component);
});
```

### 5.3 与主视图的通信

OODDesigner 与主视图之间通过事件总线进行通信：

```java
// 从设计器发送事件到主视图
ood.Event.fire('designSaved', designData);

// 从主视图发送事件到设计器
ood.Event.fire('openDesign', designId);
```

## 6. 核心 API

### 6.1 设计管理 API

| API 名称 | 用途 | HTTP 方法 | URL |
|---------|------|----------|-----|
| 嵌入设计器 | 将设计器嵌入到主视图 | POST | /rad/designer/embed |
| 保存设计 | 保存设计数据 | POST | /rad/designer/save |
| 加载设计 | 加载设计数据 | GET | /rad/designer/load |
| 导出设计 | 导出设计为代码 | POST | /rad/designer/export |
| 获取组件库 | 获取可用的组件库 | GET | /rad/designer/components |

### 6.2 组件管理 API

| API 名称 | 用途 | HTTP 方法 | URL |
|---------|------|----------|-----|
| 添加组件 | 向设计中添加组件 | POST | /rad/designer/component/add |
| 删除组件 | 从设计中删除组件 | DELETE | /rad/designer/component/{id} |
| 更新组件 | 更新组件属性 | PUT | /rad/designer/component/{id} |
| 移动组件 | 移动组件位置 | PUT | /rad/designer/component/{id}/move |
| 调整组件大小 | 调整组件大小 | PUT | /rad/designer/component/{id}/resize |

## 7. 最佳实践

### 7.1 设计器使用最佳实践

1. **模块化设计**：将复杂的设计拆分为多个独立的模块
2. **组件复用**：设计可复用的组件，减少重复设计
3. **属性配置**：合理配置组件属性，确保组件的功能和样式符合要求
4. **事件绑定**：为组件绑定必要的事件处理逻辑
5. **样式统一**：保持设计的样式统一，确保视觉效果的一致性
6. **实时保存**：开启自动保存功能，避免设计数据丢失
7. **版本管理**：使用版本管理功能，保存设计的不同版本

### 7.2 性能优化

1. **组件懒加载**：对于复杂的组件，采用懒加载方式，减少初始化时间
2. **事件防抖**：对于频繁触发的事件，如组件拖拽、属性调整等，使用防抖机制，减少事件处理次数
3. **数据缓存**：合理使用缓存，减少重复请求
4. **异步加载**：对于非核心资源，采用异步加载方式，提高初始化速度
5. **代码分割**：将设计器的代码分割为多个模块，按需加载

## 8. 集成示例

### 8.1 完整集成示例

```java
package net.ooder.editor.designer;

@Controller
@RequestMapping("/rad/designer/")
public class OODDesignerController {
    
    private static final Logger logger = LoggerFactory.getLogger(OODDesignerController.class);
    
    @Autowired
    private DesignService designService;
    
    @RequestMapping(method = RequestMethod.POST, value = "embed")
    @ModuleAnnotation(caption = "OOD设计器", panelType = PanelType.panel)
    @PanelViewAnnotation()
    @ResponseBody
    public ResultModel<ModuleComponent> embedDesigner() {
        ResultModel<ModuleComponent> result = new ResultModel<>();
        try {
            ModuleComponent component = new ModuleComponent("RAD.OODDesigner");
            component.setClassName("RAD.OODDesigner");
            component.setProperties(Collections.singletonMap("embedMode", true));
            result.setData(component);
            logger.info("成功嵌入OOD设计器");
        } catch (Exception e) {
            logger.error("嵌入OOD设计器失败: {}", e.getMessage(), e);
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }
    
    @ActionAnnotation(name = "saveDesign", caption = "保存设计", icon = "ri-save-line")
    @RequestMapping(method = RequestMethod.POST, value = "save")
    @ResponseBody
    public ResultModel<Boolean> saveDesign(@RequestBody DesignData data) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            designService.saveDesign(data);
            result.setData(true);
            logger.info("成功保存设计: {}", data.getDesignId());
        } catch (Exception e) {
            logger.error("保存设计失败: {}", e.getMessage(), e);
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "load")
    @ResponseBody
    public ResultModel<DesignData> loadDesign(@RequestParam String designId) {
        ResultModel<DesignData> result = new ResultModel<>();
        try {
            DesignData data = designService.loadDesign(designId);
            result.setData(data);
            logger.info("成功加载设计: {}", designId);
        } catch (Exception e) {
            logger.error("加载设计失败: {}", e.getMessage(), e);
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }
}
```

### 8.2 主视图集成示例

```java
@RequestMapping(method = RequestMethod.POST, value = "main")
@LayoutItemAnnotation(pos = PosType.main)
@ModuleAnnotation
@PanelViewAnnotation(caption = "主面板")
@ResponseBody
public ModuleComponent getMainView() {
    ModuleComponent component = new ModuleComponent("RAD.MainView");
    component.setClassName("RAD.MainView");
    
    // 嵌入OOD设计器
    ModuleComponent designerComponent = new ModuleComponent("RAD.OODDesigner");
    designerComponent.setClassName("RAD.OODDesigner");
    component.addChild(designerComponent);
    
    return component;
}
```

## 9. 总结

OODDesigner 是 ooder-agent-rad 的核心组件，通过注解驱动的方式集成到系统中。它采用模块化设计，支持拖拽式设计、实时预览、属性配置、事件绑定和代码生成等功能。

OODDesigner 与主视图之间通过事件总线进行通信，实现了组件间的松耦合。它的集成设计使得开发者可以快速、高效地开发可视化应用，提高开发效率，降低开发成本。

通过不断优化和扩展，OODDesigner 将继续演进，提供更加先进、高效的可视化设计体验，帮助开发者更快、更好地构建现代化 Web 应用。
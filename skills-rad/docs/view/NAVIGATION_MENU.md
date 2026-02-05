# 导航菜单设计

## 1. 总览

ooder-agent-rad 的导航菜单采用模块化、可配置的设计，支持动态扩展和定制化开发。本文档将详细介绍 ooder-agent-rad 的导航菜单设计，包括菜单类型、实现方式、配置方法等。

## 2. 菜单类型

ooder-agent-rad 支持多种菜单类型，包括：

| 菜单类型 | 用途 | 实现类 |
|---------|------|--------|
| 顶部菜单 | 应用程序的主要导航菜单，位于界面顶部 | [ProjectTopMenu.java](../../src/main/java/net/ooder/editor/menu/ProjectTopMenu.java) |
| 上下文菜单 | 右键点击组件时显示的菜单 | [FileContextMenu.java](../../src/main/java/net/ooder/editor/toolbox/file/menu/FileContextMenu.java) |
| 工具栏菜单 | 位于界面顶部或底部的工具栏 | [ProjectToolBar.java](../../src/main/java/net/ooder/editor/toolbox/file/menu/ProjectToolBar.java) |
| 树形菜单 | 位于侧边栏的树形导航菜单 | [SPAClassTree.java](../../src/main/java/net/ooder/editor/plugins/component/SPAClassTree.java) |

## 3. 顶部菜单设计

### 3.1 核心实现

**核心实现文件**：
- [ProjectTopMenu.java](../../src/main/java/net/ooder/editor/menu/ProjectTopMenu.java) - 顶部菜单的核心实现
- [FileMenuAction.java](../../src/main/java/net/ooder/editor/menu/FileMenuAction.java) - 文件菜单实现
- [EditMenuAction.java](../../src/main/java/net/ooder/editor/menu/EditMenuAction.java) - 编辑菜单实现
- [ViewMenuAction.java](../../src/main/java/net/ooder/editor/menu/ViewMenuAction.java) - 视图菜单实现
- [ProjectMangerAction.java](../../src/main/java/net/ooder/editor/menu/ProjectMangerAction.java) - 项目管理菜单实现
- [ResourceMangerAction.java](../../src/main/java/net/ooder/editor/menu/ResourceMangerAction.java) - 资源管理菜单实现
- [ServerMangerAction.java](../../src/main/java/net/ooder/editor/menu/ServerMangerAction.java) - 服务器管理菜单实现
- [ToolsMenuAction.java](../../src/main/java/net/ooder/editor/menu/ToolsMenuAction.java) - 工具菜单实现
- [HelpMenuAction.java](../../src/main/java/net/ooder/editor/menu/HelpMenuAction.java) - 帮助菜单实现

### 3.2 菜单结构

ooder-agent-rad 的顶部菜单采用以下结构：

```
┌─────────────────────────────────────────────────────────────────────────┐
│ 文件(F)   编辑(E)   视图(V)   项目管理(P)   资源管理(R)   服务器管理(S)   工具(T)   帮助(H) │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.3 菜单实现

顶部菜单通过 ProjectTopMenu 类实现，它集成了多个子菜单：

```java
public class ProjectTopMenu {
    
    private FileMenuAction fileMenuAction;
    private EditMenuAction editMenuAction;
    private ViewMenuAction viewMenuAction;
    private ProjectMangerAction projectMangerAction;
    private ResourceMangerAction resourceMangerAction;
    private ServerMangerAction serverMangerAction;
    private ToolsMenuAction toolsMenuAction;
    private HelpMenuAction helpMenuAction;
    
    // 菜单初始化和管理逻辑
}
```

## 4. 上下文菜单设计

### 4.1 核心实现

**核心实现文件**：
- [FileContextMenu.java](../../src/main/java/net/ooder/editor/toolbox/file/menu/FileContextMenu.java) - 文件上下文菜单
- [DomainContextMenu.java](../../src/main/java/net/ooder/editor/toolbox/file/menu/DomainContextMenu.java) - 领域上下文菜单
- [ModuleContextMenu.java](../../src/main/java/net/ooder/editor/toolbox/file/menu/ModuleContextMenu.java) - 模块上下文菜单
- [PageContextMenu.java](../../src/main/java/net/ooder/editor/toolbox/file/menu/PageContextMenu.java) - 页面上下文菜单

### 4.2 上下文菜单实现

上下文菜单根据不同的上下文显示不同的菜单项：

```java
public class FileContextMenu {
    
    /**
     * 生成文件上下文菜单
     * @param file 文件对象
     * @return 上下文菜单
     */
    public ContextMenu generateContextMenu(OODFile file) {
        ContextMenu menu = new ContextMenu();
        
        // 添加菜单项
        menu.addItem(new MenuItem("打开", "openFile", "ri-folder-open-line"));
        menu.addItem(new MenuItem("编辑", "editFile", "ri-edit-2-line"));
        menu.addItem(new MenuItem("删除", "deleteFile", "ri-delete-bin-line"));
        menu.addItem(new MenuItem("重命名", "renameFile", "ri-pencil-line"));
        
        // 添加分隔线
        menu.addSeparator();
        
        // 添加更多菜单项
        menu.addItem(new MenuItem("属性", "properties", "ri-information-line"));
        
        return menu;
    }
}
```

## 5. 工具栏菜单设计

### 5.1 核心实现

**核心实现文件**：[ProjectToolBar.java](../../src/main/java/net/ooder/editor/toolbox/file/menu/ProjectToolBar.java)

### 5.2 工具栏实现

工具栏菜单位于界面顶部或底部，提供常用功能的快捷访问：

```java
public class ProjectToolBar {
    
    /**
     * 生成工具栏
     * @return 工具栏
     */
    public ToolBar generateToolBar() {
        ToolBar toolBar = new ToolBar();
        
        // 添加工具栏按钮
        toolBar.addButton(new ToolBarButton("新建", "newProject", "ri-add-line"));
        toolBar.addButton(new ToolBarButton("打开", "openProject", "ri-folder-open-line"));
        toolBar.addButton(new ToolBarButton("保存", "saveProject", "ri-save-line"));
        
        // 添加分隔线
        toolBar.addSeparator();
        
        // 添加更多工具栏按钮
        toolBar.addButton(new ToolBarButton("导出", "exportProject", "ri-download-line"));
        toolBar.addButton(new ToolBarButton("预览", "previewProject", "ri-eye-line"));
        
        return toolBar;
    }
}
```

## 6. 树形菜单设计

### 6.1 核心实现

**核心实现文件**：[SPAClassTree.java](../../src/main/java/net/ooder/editor/plugins/component/SPAClassTree.java)

### 6.2 树形菜单实现

树形菜单位于侧边栏，用于导航项目的文件和组件结构：

```java
public class SPAClassTree {
    
    /**
     * 生成树形菜单
     * @param project 项目对象
     * @return 树形菜单
     */
    public Tree generateClassTree(OODProject project) {
        Tree tree = new Tree();
        
        // 生成树形节点
        TreeNode rootNode = new TreeNode("项目", project.getName(), "ri-folder-line");
        
        // 添加文件节点
        for (OODFile file : project.getFiles()) {
            TreeNode fileNode = new TreeNode("文件", file.getName(), "ri-file-line");
            rootNode.addChild(fileNode);
        }
        
        // 添加组件节点
        for (OODComponent component : project.getComponents()) {
            TreeNode componentNode = new TreeNode("组件", component.getName(), "ri-box-line");
            rootNode.addChild(componentNode);
        }
        
        tree.setRoot(rootNode);
        return tree;
    }
}
```

## 7. 菜单配置

### 7.1 注解配置

ooder-agent-rad 采用注解驱动的方式配置菜单：

```java
@MenuAnnotation(name = "file", caption = "文件", icon = "ri-file-line")
public class FileMenuAction {
    
    @MenuItemAnnotation(name = "new", caption = "新建", icon = "ri-add-line", order = 1)
    public void newFile() {
        // 新建文件逻辑
    }
    
    @MenuItemAnnotation(name = "open", caption = "打开", icon = "ri-folder-open-line", order = 2)
    public void openFile() {
        // 打开文件逻辑
    }
    
    @MenuItemAnnotation(name = "save", caption = "保存", icon = "ri-save-line", order = 3)
    public void saveFile() {
        // 保存文件逻辑
    }
}
```

### 7.2 配置文件

菜单也可以通过配置文件进行配置：

```json
{
  "menus": [
    {
      "name": "file",
      "caption": "文件",
      "icon": "ri-file-line",
      "items": [
        {
          "name": "new",
          "caption": "新建",
          "icon": "ri-add-line",
          "action": "newFile",
          "order": 1
        },
        {
          "name": "open",
          "caption": "打开",
          "icon": "ri-folder-open-line",
          "action": "openFile",
          "order": 2
        },
        {
          "name": "save",
          "caption": "保存",
          "icon": "ri-save-line",
          "action": "saveFile",
          "order": 3
        }
      ]
    }
  ]
}
```

## 8. 菜单事件处理

### 8.1 事件类型

ooder-agent-rad 支持多种菜单事件类型，包括：

| 事件类型 | 用途 |
|---------|------|
| 点击事件 | 点击菜单项时触发 |
| 鼠标悬停事件 | 鼠标悬停在菜单项上时触发 |
| 鼠标离开事件 | 鼠标离开菜单项时触发 |
| 键盘快捷键事件 | 按下键盘快捷键时触发 |

### 8.2 事件处理实现

菜单事件通过注解驱动的方式处理：

```java
@Controller
@RequestMapping("/rad/menu/")
public class MenuController {
    
    @RequestMapping(value = "file/new", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel<Boolean> newFile() {
        // 处理新建文件事件
        return ResultModel.success(true);
    }
    
    @RequestMapping(value = "file/open", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel<Boolean> openFile(@RequestBody OpenFileRequest request) {
        // 处理打开文件事件
        return ResultModel.success(true);
    }
    
    @RequestMapping(value = "file/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel<Boolean> saveFile() {
        // 处理保存文件事件
        return ResultModel.success(true);
    }
}
```

## 9. 菜单扩展机制

### 9.1 插件化扩展

ooder-agent-rad 的菜单支持插件化扩展，插件可以动态添加或修改菜单：

```java
public class MyPlugin implements Plugin {
    
    @Override
    public void initialize(PluginContext context) {
        // 获取菜单管理器
        MenuManager menuManager = context.getMenuManager();
        
        // 添加新的菜单项
        MenuItem menuItem = new MenuItem("我的插件", "myPluginAction", "ri-plugin-line");
        menuManager.addMenuItem("tools", menuItem);
        
        // 注册菜单项的事件处理
        context.registerEventHandler("myPluginAction", this::handleMyPluginAction);
    }
    
    /**
     * 处理插件菜单项点击事件
     * @param event 事件对象
     */
    private void handleMyPluginAction(MenuEvent event) {
        // 处理菜单项点击事件
        System.out.println("我的插件被点击了");
    }
}
```

### 9.2 动态菜单生成

菜单可以根据用户权限、角色或其他条件动态生成：

```java
public class DynamicMenuGenerator {
    
    /**
     * 根据用户权限生成菜单
     * @param user 用户对象
     * @return 菜单
     */
    public Menu generateMenuByUser(User user) {
        Menu menu = new Menu();
        
        // 添加基础菜单项
        menu.addItem(new MenuItem("文件", "file", "ri-file-line"));
        menu.addItem(new MenuItem("编辑", "edit", "ri-edit-2-line"));
        menu.addItem(new MenuItem("视图", "view", "ri-layout-grid-line"));
        
        // 根据用户权限添加菜单项
        if (user.hasPermission("project.manage")) {
            menu.addItem(new MenuItem("项目管理", "project.manage", "ri-dashboard-line"));
        }
        
        if (user.hasPermission("resource.manage")) {
            menu.addItem(new MenuItem("资源管理", "resource.manage", "ri-server-line"));
        }
        
        return menu;
    }
}
```

## 10. 最佳实践

### 10.1 菜单设计最佳实践

1. **简洁明了**：菜单结构清晰，层次分明，避免过多的层级
2. **常用功能优先**：将常用功能放在菜单的前面或顶部
3. **一致性**：保持菜单风格和命名的一致性
4. **可访问性**：为菜单项提供快捷键和图标，提高可访问性
5. **动态性**：根据用户权限和角色动态生成菜单
6. **可扩展性**：支持插件化扩展，便于功能扩展

### 10.2 菜单实现最佳实践

1. **模块化设计**：将菜单拆分为多个独立的模块，便于维护和扩展
2. **注解驱动**：使用注解驱动的方式实现菜单，提高开发效率
3. **事件驱动**：采用事件驱动的方式处理菜单事件，降低耦合度
4. **配置化**：支持通过配置文件配置菜单，便于定制化开发
5. **性能优化**：菜单生成和事件处理逻辑应高效，避免性能瓶颈

## 11. 总结

ooder-agent-rad 的导航菜单采用模块化、可配置的设计，支持多种菜单类型和扩展方式。它的核心特点包括：

1. **模块化设计**：将菜单拆分为多个独立的模块，便于维护和扩展
2. **注解驱动开发**：使用注解驱动的方式实现菜单，提高开发效率
3. **事件驱动通信**：采用事件驱动的方式处理菜单事件，降低耦合度
4. **插件化扩展**：支持插件化扩展，便于功能扩展
5. **动态菜单生成**：根据用户权限和角色动态生成菜单
6. **配置化设计**：支持通过配置文件配置菜单，便于定制化开发

通过不断优化和扩展，ooder-agent-rad 的导航菜单将继续演进，提供更加先进、高效的导航体验，帮助用户更快、更便捷地使用 ooder-agent-rad 进行应用开发。
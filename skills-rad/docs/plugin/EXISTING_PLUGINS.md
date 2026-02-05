# 现有插件介绍

## 1. 概述

ooder-agent-rad 支持多种插件，这些插件扩展了系统的功能，提供了更丰富的开发体验。本文档将详细介绍现有的主要插件，包括它们的功能、使用方法和配置。

## 2. 核心插件列表

### 2.1 DSM 插件

#### 2.1.1 功能介绍

DSM（Domain Specific Model）插件提供领域模型管理功能，允许开发者定义、编辑和管理领域模型。

#### 2.1.2 主要特性

- 模型定义和编辑
- 模型版本管理
- 模型导出和导入
- 模型与代码生成
- 模型可视化
- 模型验证和检查

#### 2.1.3 技术实现

DSM 插件基于以下技术实现：

```java
package net.ooder.plugins.dsm;

import net.ooder.editor.plugin.Plugin;
import net.ooder.editor.plugin.PluginContext;

public class DSMPlugin implements Plugin {
    // 插件实现
}
```

#### 2.1.4 使用示例

```java
package net.ooder.editor.service;

import net.ooder.plugins.dsm.DSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectManager {
    
    @Autowired
    private DSMService dsmService;
    
    public void generateCodeFromModel(String modelId) {
        // 从领域模型生成代码
        dsmService.generateCode(modelId);
    }
}
```

### 2.2 RIGHT 插件

#### 2.2.1 功能介绍

RIGHT 插件提供权限管理功能，包括用户管理、角色管理和权限分配。

#### 2.2.2 主要特性

- 用户管理
- 角色管理
- 权限分配
- 访问控制
- 权限继承
- 权限验证

#### 2.2.3 技术实现

RIGHT 插件基于以下技术实现：

```java
package net.ooder.plugins.right;

import net.ooder.editor.plugin.Plugin;
import net.ooder.editor.plugin.PluginContext;

public class RightPlugin implements Plugin {
    // 插件实现
}
```

#### 2.2.4 使用示例

```java
package net.ooder.editor.interceptor;

import net.ooder.plugins.right.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

public class PermissionInterceptor implements HandlerInterceptor {
    
    @Autowired
    private PermissionService permissionService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 权限验证
        return permissionService.hasPermission(request.getRequestURI(), request.getMethod());
    }
}
```

### 2.3 VFS 插件

#### 2.3.1 功能介绍

VFS（Virtual File System）插件提供虚拟文件系统功能，允许开发者管理虚拟文件和文件夹。

#### 2.3.2 主要特性

- 文件和文件夹管理
- 文件版本控制
- 文件搜索和过滤
- 文件操作日志
- 文件共享和协作
- 文件访问控制

#### 2.3.3 技术实现

VFS 插件基于以下技术实现：

```java
package net.ooder.plugins.vfs;

import net.ooder.editor.plugin.Plugin;
import net.ooder.editor.plugin.PluginContext;

public class VFSPlugin implements Plugin {
    // 插件实现
}
```

#### 2.3.4 使用示例

```java
package net.ooder.editor.service;

import net.ooder.plugins.vfs.VFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RADEditor {
    
    @Autowired
    private VFSService vfsService;
    
    public void exportProject(String projectId) {
        // 导出项目到虚拟文件系统
        vfsService.exportProject(projectId);
    }
}
```

### 2.4 FORMULA 插件

#### 2.4.1 功能介绍

FORMULA 插件提供公式计算功能，允许开发者在视图中定义和使用公式。

#### 2.4.2 主要特性

- 公式定义和编辑
- 公式验证和检查
- 公式计算和执行
- 公式函数库
- 公式与数据绑定
- 公式可视化

#### 2.4.3 技术实现

FORMULA 插件基于以下技术实现：

```java
package net.ooder.plugins.formula;

import net.ooder.editor.plugin.Plugin;
import net.ooder.editor.plugin.PluginContext;

public class FormulaPlugin implements Plugin {
    // 插件实现
}
```

#### 2.4.4 使用示例

```java
package net.ooder.editor.bean;

import net.ooder.plugins.formula.annotation.Formula;

public class OrderView {
    
    private double unitPrice;
    private int quantity;
    
    @Formula("unitPrice * quantity")
    private double totalPrice;
    
    // getter 和 setter 方法
}
```

## 3. 插件配置

### 3.1 插件配置文件

插件可以通过 `application.properties` 或 `application.yml` 配置：

```properties
# DSM 插件配置
dsm.plugin.enabled=true
dsm.plugin.model.storage.path=/data/models

# RIGHT 插件配置
right.plugin.enabled=true
right.plugin.cache.enabled=true

# VFS 插件配置
vfs.plugin.enabled=true
vfs.plugin.storage.path=/data/vfs

# FORMULA 插件配置
formula.plugin.enabled=true
formula.plugin.function.library=default,math,date
```

### 3.2 插件依赖管理

插件之间可能存在依赖关系，需要在插件配置中声明：

```properties
# 插件依赖配置
plugin.dependencies=dsm,right,vfs,formula
```

## 4. 插件集成示例

### 4.1 在服务中使用插件

```java
package net.ooder.editor.service;

import net.ooder.plugins.dsm.DSMService;
import net.ooder.plugins.right.PermissionService;
import net.ooder.plugins.vfs.VFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    
    @Autowired
    private DSMService dsmService;
    
    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private VFSService vfsService;
    
    public void createProject(String projectName) {
        // 权限验证
        permissionService.checkPermission("project.create");
        
        // 创建项目
        // ...
        
        // 生成初始领域模型
        dsmService.createInitialModel(projectName);
        
        // 创建项目文件结构
        vfsService.createProjectStructure(projectName);
    }
}
```

### 4.2 在控制器中使用插件

```java
package net.ooder.editor.controller;

import net.ooder.plugins.dsm.DSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dsm")
public class DSMController {
    
    @Autowired
    private DSMService dsmService;
    
    @GetMapping("/models")
    public List<ModelInfo> getModels() {
        // 获取所有领域模型
        return dsmService.getAllModels();
    }
}
```

## 5. 插件扩展点

每个插件都提供了扩展点，允许其他插件或应用程序扩展其功能：

### 5.1 DSM 插件扩展点

```java
package net.ooder.plugins.dsm.spi;

public interface ModelGenerator {
    String getLanguage();
    void generateCode(Model model, OutputStream outputStream);
}
```

### 5.2 RIGHT 插件扩展点

```java
package net.ooder.plugins.right.spi;

public interface PermissionProvider {
    boolean hasPermission(String userId, String permission);
}
```

### 5.3 VFS 插件扩展点

```java
package net.ooder.plugins.vfs.spi;

public interface FileSystemProvider {
    String getScheme();
    FileSystem createFileSystem();
}
```

## 6. 插件版本管理

每个插件都有自己的版本号，通过版本号可以管理插件的更新和兼容性：

| 插件名称 | 版本号 | 兼容的 ooder-agent-rad 版本 | 更新日期 |
|---------|--------|---------------------------|---------|
| DSM 插件 | 1.0.0 | 1.0 | 2026-01-24 |
| RIGHT 插件 | 1.0.0 | 1.0 | 2026-01-24 |
| VFS 插件 | 1.0.0 | 1.0 | 2026-01-24 |
| FORMULA 插件 | 1.0.0 | 1.0 | 2026-01-24 |

## 7. 插件最佳实践

### 7.1 插件选择原则

- 根据项目需求选择合适的插件
- 考虑插件的稳定性和兼容性
- 评估插件的性能和资源占用
- 检查插件的更新频率和维护状态

### 7.2 插件使用建议

- 只启用必要的插件
- 定期更新插件版本
- 配置插件的日志级别
- 监控插件的性能
- 遵循插件的使用规范

### 7.3 插件冲突解决

- 检查插件之间的依赖关系
- 调整插件的加载顺序
- 禁用冲突的插件
- 联系插件开发者解决冲突

## 8. 插件开发资源

### 8.1 插件开发文档

- [插件开发指南](PLUGIN_DEVELOPMENT_GUIDE.md)
- [插件 API 文档](https://ooder.github.io/ooder-agent-rad/plugin-api/)

### 8.2 插件示例代码

- [DSM 插件示例](https://github.com/ooder/ooder-plugin-dsm)
- [RIGHT 插件示例](https://github.com/ooder/ooder-plugin-right)
- [VFS 插件示例](https://github.com/ooder/ooder-plugin-vfs)
- [FORMULA 插件示例](https://github.com/ooder/ooder-plugin-formula)

## 9. 总结

ooder-agent-rad 的插件系统提供了丰富的功能扩展，允许开发者根据项目需求选择和配置合适的插件。现有的主要插件包括 DSM、RIGHT、VFS 和 FORMULA，它们分别提供了领域模型管理、权限管理、虚拟文件系统和公式计算功能。

通过合理使用这些插件，开发者可以提高开发效率，减少重复工作，构建更复杂、更强大的应用系统。
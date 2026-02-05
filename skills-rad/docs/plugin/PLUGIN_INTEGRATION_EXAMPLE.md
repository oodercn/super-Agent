# 插件集成示例

## 1. 概述

本文档提供了 ooder-agent-rad 插件集成的详细示例，包括插件安装、配置、使用和扩展等方面。通过这些示例，开发者可以快速了解如何在 ooder-agent-rad 中集成和使用插件。

## 2. 插件安装

### 2.1 下载插件

从插件仓库下载所需的插件 JAR 文件：

```bash
download-plugin --name dsm-plugin --version 1.0.0
download-plugin --name right-plugin --version 1.0.0
download-plugin --name vfs-plugin --version 1.0.0
download-plugin --name formula-plugin --version 1.0.0
```

### 2.2 安装插件

将下载的插件 JAR 文件复制到 ooder-agent-rad 的 `plugins` 目录：

```bash
cp dsm-plugin-1.0.0.jar right-plugin-1.0.0.jar vfs-plugin-1.0.0.jar formula-plugin-1.0.0.jar /path/to/ooder-agent-rad/plugins/
```

### 2.3 启用插件

在 `application.properties` 文件中启用插件：

```properties
# 启用插件
dsm.plugin.enabled=true
right.plugin.enabled=true
vfs.plugin.enabled=true
formula.plugin.enabled=true
```

## 3. 插件配置示例

### 3.1 DSM 插件配置

```properties
# DSM 插件配置
dsm.plugin.model.storage.path=/data/models
dsm.plugin.validation.enabled=true
dsm.plugin.generation.template.path=/data/templates
dsm.plugin.visualization.enabled=true
dsm.plugin.visualization.port=8081
```

### 3.2 RIGHT 插件配置

```properties
# RIGHT 插件配置
right.plugin.cache.enabled=true
right.plugin.cache.ttl=3600
right.plugin.audit.enabled=true
right.plugin.audit.log.path=/data/audit.log
right.plugin.permission.inheritance.enabled=true
```

### 3.3 VFS 插件配置

```properties
# VFS 插件配置
vfs.plugin.storage.path=/data/vfs
vfs.plugin.cache.enabled=true
vfs.plugin.cache.size=1000
vfs.plugin.versioning.enabled=true
vfs.plugin.versioning.max.versions=10
vfs.plugin.search.index.path=/data/vfs-index
```

### 3.4 FORMULA 插件配置

```properties
# FORMULA 插件配置
formula.plugin.function.library=default,math,date,string
formula.plugin.cache.enabled=true
formula.plugin.validation.enabled=true
formula.plugin.performance.monitoring.enabled=true
```

## 4. 插件使用示例

### 4.1 在服务中使用 DSM 插件

```java
package net.ooder.editor.service;

import net.ooder.plugins.dsm.DSMService;
import net.ooder.plugins.dsm.Model;
import net.ooder.plugins.dsm.ModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectManager {
    
    @Autowired
    private DSMService dsmService;
    
    /**
     * 创建项目并生成初始领域模型
     */
    public void createProject(String projectName, String description) {
        // 创建项目
        // ...
        
        // 生成初始领域模型
        Model model = dsmService.createInitialModel(projectName);
        System.out.println("Created initial model: " + model.getId());
        
        // 保存模型
        dsmService.saveModel(model);
    }
    
    /**
     * 从领域模型生成代码
     */
    public void generateCodeFromModel(String projectId, String modelId) {
        // 获取项目
        // ...
        
        // 从模型生成代码
        dsmService.generateCode(modelId);
        System.out.println("Generated code from model: " + modelId);
    }
    
    /**
     * 获取项目的所有领域模型
     */
    public List<ModelInfo> getProjectModels(String projectId) {
        // 获取项目
        // ...
        
        // 获取模型列表
        return dsmService.getModelsByProject(projectId);
    }
}
```

### 4.2 在控制器中使用 RIGHT 插件

```java
package net.ooder.editor.controller;

import net.ooder.plugins.right.PermissionService;
import net.ooder.plugins.right.UserService;
import net.ooder.plugins.right.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PermissionService permissionService;
    
    /**
     * 获取用户列表
     */
    @GetMapping
    public List<UserDTO> getUsers() {
        // 权限验证
        permissionService.checkPermission("user.list");
        
        return userService.getAllUsers();
    }
    
    /**
     * 创建用户
     */
    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        // 权限验证
        permissionService.checkPermission("user.create");
        
        return userService.createUser(userDTO);
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        // 权限验证
        permissionService.checkPermission("user.update");
        
        return userService.updateUser(userId, userDTO);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        // 权限验证
        permissionService.checkPermission("user.delete");
        
        userService.deleteUser(userId);
    }
}
```

### 4.3 在服务中使用 VFS 插件

```java
package net.ooder.editor.service;

import net.ooder.plugins.vfs.VFSService;
import net.ooder.plugins.vfs.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class RADEditor {
    
    @Autowired
    private VFSService vfsService;
    
    /**
     * 导出项目到虚拟文件系统
     */
    public void exportProject(String projectId, String targetPath) {
        // 获取项目
        // ...
        
        // 导出项目文件
        vfsService.exportProject(projectId, targetPath);
        System.out.println("Exported project to: " + targetPath);
    }
    
    /**
     * 从虚拟文件系统导入项目
     */
    public void importProject(String sourcePath, String projectName) {
        // 导入项目
        String projectId = vfsService.importProject(sourcePath, projectName);
        System.out.println("Imported project with ID: " + projectId);
    }
    
    /**
     * 获取文件列表
     */
    public List<FileInfo> getFiles(String path) {
        return vfsService.listFiles(path);
    }
    
    /**
     * 读取文件内容
     */
    public void readFile(String filePath, OutputStream outputStream) {
        vfsService.readFile(filePath, outputStream);
    }
    
    /**
     * 写入文件内容
     */
    public void writeFile(String filePath, InputStream inputStream) {
        vfsService.writeFile(filePath, inputStream);
    }
}
```

### 4.4 在视图中使用 FORMULA 插件

```java
package net.ooder.editor.bean;

import net.ooder.plugins.formula.annotation.Formula;
import net.ooder.editor.annotation.CustomAnnotation;
import net.ooder.editor.annotation.FormAnnotation;
import net.ooder.editor.annotation.InputAnnotation;

@FormAnnotation(
    caption = "订单表单",
    col = 2,
    row = 6
)
public class OrderView {
    
    @CustomAnnotation(caption = "订单号", index = 1)
    @InputAnnotation(required = true, maxlength = 20)
    private String orderNo;
    
    @CustomAnnotation(caption = "单价", index = 2)
    @InputAnnotation(required = true, type = "number", min = 0, step = 0.01)
    private double unitPrice;
    
    @CustomAnnotation(caption = "数量", index = 3)
    @InputAnnotation(required = true, type = "number", min = 1, step = 1)
    private int quantity;
    
    @CustomAnnotation(caption = "折扣", index = 4)
    @InputAnnotation(required = true, type = "number", min = 0, max = 1, step = 0.01)
    private double discount = 1.0;
    
    @CustomAnnotation(caption = "税率", index = 5)
    @InputAnnotation(required = true, type = "number", min = 0, max = 1, step = 0.01)
    private double taxRate = 0.13;
    
    @Formula("unitPrice * quantity")
    private double subtotal;
    
    @Formula("subtotal * discount")
    private double discountedTotal;
    
    @Formula("discountedTotal * taxRate")
    private double tax;
    
    @Formula("discountedTotal + tax")
    @CustomAnnotation(caption = "总计", index = 6)
    private double total;
    
    // getter 和 setter 方法
    // ...
}
```

## 5. 插件扩展示例

### 5.1 扩展 DSM 插件的模型生成器

```java
package com.example.plugins.dsm;

import net.ooder.plugins.dsm.spi.ModelGenerator;
import net.ooder.plugins.dsm.Model;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * 自定义 Python 代码生成器
 */
public class PythonModelGenerator implements ModelGenerator {
    
    @Override
    public String getLanguage() {
        return "python";
    }
    
    @Override
    public void generateCode(Model model, OutputStream outputStream) {
        try (PrintWriter writer = new PrintWriter(outputStream)) {
            // 生成 Python 代码
            writer.println("# Generated by PythonModelGenerator");
            writer.println("# Model: " + model.getName());
            writer.println();
            
            // 生成类定义
            writer.println("class " + model.getName() + ":");
            writer.println("    def __init__(self):");
            
            // 生成属性
            model.getAttributes().forEach(attr -> {
                writer.printf("        self.%s = None\n", attr.getName());
            });
            
            writer.println();
            writer.println("    def to_dict(self):");
            writer.println("        return {");
            
            model.getAttributes().forEach(attr -> {
                writer.printf("            '%s': self.%s,\n", attr.getName(), attr.getName());
            });
            
            writer.println("        }");
            
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Python code", e);
        }
    }
}
```

### 5.2 注册自定义模型生成器

在 `src/main/resources/META-INF/services/net.ooder.plugins.dsm.spi.ModelGenerator` 文件中注册：

```
com.example.plugins.dsm.PythonModelGenerator
```

### 5.3 扩展 RIGHT 插件的权限提供者

```java
package com.example.plugins.right;

import net.ooder.plugins.right.spi.PermissionProvider;

/**
 * 自定义权限提供者
 */
public class CustomPermissionProvider implements PermissionProvider {
    
    @Override
    public boolean hasPermission(String userId, String permission) {
        // 自定义权限验证逻辑
        // 示例：允许管理员访问所有权限
        if ("admin".equals(userId)) {
            return true;
        }
        
        // 其他权限验证逻辑
        // ...
        
        return false;
    }
}
```

### 5.4 注册自定义权限提供者

在 `src/main/resources/META-INF/services/net.ooder.plugins.right.spi.PermissionProvider` 文件中注册：

```
com.example.plugins.right.CustomPermissionProvider
```

## 6. 插件事件监听示例

```java
package net.ooder.editor.listener;

import net.ooder.plugins.dsm.event.ModelCreatedEvent;
import net.ooder.plugins.dsm.event.ModelUpdatedEvent;
import net.ooder.plugins.dsm.event.ModelDeletedEvent;
import net.ooder.plugins.right.event.PermissionGrantedEvent;
import net.ooder.plugins.right.event.PermissionRevokedEvent;
import net.ooder.plugins.vfs.event.FileCreatedEvent;
import net.ooder.plugins.vfs.event.FileUpdatedEvent;
import net.ooder.plugins.vfs.event.FileDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PluginEventListener {
    
    /**
     * 监听模型创建事件
     */
    @EventListener
    public void onModelCreated(ModelCreatedEvent event) {
        System.out.println("Model created: " + event.getModel().getId());
        // 处理模型创建事件
        // ...
    }
    
    /**
     * 监听模型更新事件
     */
    @EventListener
    public void onModelUpdated(ModelUpdatedEvent event) {
        System.out.println("Model updated: " + event.getModel().getId());
        // 处理模型更新事件
        // ...
    }
    
    /**
     * 监听模型删除事件
     */
    @EventListener
    public void onModelDeleted(ModelDeletedEvent event) {
        System.out.println("Model deleted: " + event.getModelId());
        // 处理模型删除事件
        // ...
    }
    
    /**
     * 监听权限授予事件
     */
    @EventListener
    public void onPermissionGranted(PermissionGrantedEvent event) {
        System.out.println("Permission granted: " + event.getPermission() + " to " + event.getUserId());
        // 处理权限授予事件
        // ...
    }
    
    /**
     * 监听权限撤销事件
     */
    @EventListener
    public void onPermissionRevoked(PermissionRevokedEvent event) {
        System.out.println("Permission revoked: " + event.getPermission() + " from " + event.getUserId());
        // 处理权限撤销事件
        // ...
    }
    
    /**
     * 监听文件创建事件
     */
    @EventListener
    public void onFileCreated(FileCreatedEvent event) {
        System.out.println("File created: " + event.getFilePath());
        // 处理文件创建事件
        // ...
    }
    
    /**
     * 监听文件更新事件
     */
    @EventListener
    public void onFileUpdated(FileUpdatedEvent event) {
        System.out.println("File updated: " + event.getFilePath());
        // 处理文件更新事件
        // ...
    }
    
    /**
     * 监听文件删除事件
     */
    @EventListener
    public void onFileDeleted(FileDeletedEvent event) {
        System.out.println("File deleted: " + event.getFilePath());
        // 处理文件删除事件
        // ...
    }
}
```

## 7. 插件测试示例

### 7.1 单元测试

```java
package net.ooder.editor.service;

import net.ooder.plugins.dsm.DSMService;
import net.ooder.plugins.dsm.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectManagerTest {
    
    @Mock
    private DSMService dsmService;
    
    @InjectMocks
    private ProjectManager projectManager;
    
    @Test
    public void testCreateProject() {
        // 准备数据
        String projectName = "Test Project";
        String description = "Test Description";
        Model model = new Model();
        model.setId("model-123");
        
        // 模拟行为
        when(dsmService.createInitialModel(projectName)).thenReturn(model);
        
        // 执行测试
        projectManager.createProject(projectName, description);
        
        // 验证结果
        verify(dsmService, times(1)).createInitialModel(projectName);
        verify(dsmService, times(1)).saveModel(model);
    }
}
```

### 7.2 集成测试

```java
package net.ooder.editor.integration;

import net.ooder.editor.service.ProjectManager;
import net.ooder.plugins.dsm.DSMService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProjectManagerIntegrationTest {
    
    @Autowired
    private ProjectManager projectManager;
    
    @Autowired
    private DSMService dsmService;
    
    @Test
    public void testCreateProject() {
        // 准备数据
        String projectName = "Integration Test Project";
        String description = "Integration Test Description";
        
        // 执行测试
        projectManager.createProject(projectName, description);
        
        // 验证结果
        // ...
    }
}
```

## 8. 插件性能监控示例

```java
package net.ooder.editor.monitor;

import net.ooder.plugins.dsm.metrics.DSMMetrics;
import net.ooder.plugins.right.metrics.RightMetrics;
import net.ooder.plugins.vfs.metrics.VFSMetrics;
import net.ooder.plugins.formula.metrics.FormulaMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PluginMetricsMonitor {
    
    @Autowired
    private DSMMetrics dsmMetrics;
    
    @Autowired
    private RightMetrics rightMetrics;
    
    @Autowired
    private VFSMetrics vfsMetrics;
    
    @Autowired
    private FormulaMetrics formulaMetrics;
    
    /**
     * 每 5 分钟收集一次插件指标
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void collectPluginMetrics() {
        System.out.println("=== Plugin Metrics ===");
        
        // 收集 DSM 插件指标
        System.out.println("DSM Plugin:");
        System.out.println("  Model Count: " + dsmMetrics.getModelCount());
        System.out.println("  Code Generation Count: " + dsmMetrics.getCodeGenerationCount());
        System.out.println("  Validation Error Count: " + dsmMetrics.getValidationErrorCount());
        
        // 收集 RIGHT 插件指标
        System.out.println("RIGHT Plugin:");
        System.out.println("  User Count: " + rightMetrics.getUserCount());
        System.out.println("  Role Count: " + rightMetrics.getRoleCount());
        System.out.println("  Permission Check Count: " + rightMetrics.getPermissionCheckCount());
        System.out.println("  Permission Denied Count: " + rightMetrics.getPermissionDeniedCount());
        
        // 收集 VFS 插件指标
        System.out.println("VFS Plugin:");
        System.out.println("  File Count: " + vfsMetrics.getFileCount());
        System.out.println("  Read Operation Count: " + vfsMetrics.getReadOperationCount());
        System.out.println("  Write Operation Count: " + vfsMetrics.getWriteOperationCount());
        System.out.println("  Average File Size: " + vfsMetrics.getAverageFileSize() + " bytes");
        
        // 收集 FORMULA 插件指标
        System.out.println("FORMULA Plugin:");
        System.out.println("  Formula Execution Count: " + formulaMetrics.getExecutionCount());
        System.out.println("  Average Execution Time: " + formulaMetrics.getAverageExecutionTime() + " ms");
        System.out.println("  Formula Error Count: " + formulaMetrics.getErrorCount());
        
        System.out.println("======================");
    }
}
```

## 9. 插件更新与升级示例

### 9.1 检查插件更新

```bash
# 检查插件更新
check-plugin-updates
```

### 9.2 更新插件

```bash
# 更新插件
update-plugin --name dsm-plugin
update-plugin --name right-plugin
update-plugin --name vfs-plugin
update-plugin --name formula-plugin
```

### 9.3 重启应用以应用更新

```bash
# 重启应用
systemctl restart ooder-agent-rad
```

## 10. 常见问题与解决方案

### 10.1 插件加载失败

**问题**：插件无法加载，日志中显示 "Plugin load failed: xxx"

**解决方案**：
1. 检查插件 JAR 文件是否损坏
2. 检查插件依赖是否完整
3. 检查插件版本是否与 ooder-agent-rad 兼容
4. 检查插件配置是否正确

### 10.2 插件服务调用失败

**问题**：调用插件服务时抛出异常

**解决方案**：
1. 检查插件是否启用
2. 检查插件配置是否正确
3. 检查插件服务是否正常运行
4. 查看插件日志获取详细错误信息

### 10.3 插件冲突

**问题**：多个插件之间存在冲突

**解决方案**：
1. 检查插件之间的依赖关系
2. 调整插件的加载顺序
3. 禁用冲突的插件
4. 更新插件到最新版本

### 10.4 插件性能问题

**问题**：插件导致系统性能下降

**解决方案**：
1. 检查插件配置，调整缓存大小和其他性能相关参数
2. 优化插件代码，减少资源占用
3. 禁用不必要的插件功能
4. 升级插件到最新版本

## 11. 总结

本文档提供了 ooder-agent-rad 插件集成的详细示例，包括插件安装、配置、使用、扩展、测试和监控等方面。通过这些示例，开发者可以快速了解如何在 ooder-agent-rad 中集成和使用插件，从而扩展系统功能，提高开发效率。

插件化设计是 ooder-agent-rad 的核心特性之一，它允许开发者根据项目需求灵活扩展系统功能，实现定制化开发。开发者可以根据本文档提供的示例，结合实际项目需求，开发和集成适合自己的插件。
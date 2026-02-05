# 插件开发指南

## 1. 概述

ooder-agent-rad 采用插件化设计，支持动态扩展和定制化开发。本指南将详细介绍如何开发和集成插件，以便开发者能够快速扩展系统功能。

## 2. 插件架构设计

### 2.1 插件核心接口

插件开发的核心是实现 `Plugin` 接口，该接口定义了插件的基本生命周期和行为：

```java
package net.ooder.editor.plugin;

public interface Plugin {
    String getId();
    String getName();
    String getVersion();
    void initialize(PluginContext context);
    void destroy();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
```

### 2.2 插件上下文

插件上下文提供了插件运行所需的环境信息和服务：

```java
package net.ooder.editor.plugin;

public interface PluginContext {
    ApplicationContext getApplicationContext();
    PluginManager getPluginManager();
    Configuration getConfiguration();
    <T> T getService(Class<T> serviceClass);
    <T> T getBean(String beanName, Class<T> beanClass);
}
```

## 3. 插件开发步骤

### 3.1 创建插件项目

1. 创建一个 Maven 项目，添加以下依赖：

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>ooder-agent-rad</artifactId>
    <version>1.0</version>
    <scope>provided</scope>
</dependency>
```

2. 创建插件主类，实现 `Plugin` 接口：

```java
package com.example.myplugin;

import net.ooder.editor.plugin.Plugin;
import net.ooder.editor.plugin.PluginContext;

public class MyPlugin implements Plugin {
    
    private boolean enabled = true;
    
    @Override
    public String getId() {
        return "my-plugin";
    }
    
    @Override
    public String getName() {
        return "My Plugin";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public void initialize(PluginContext context) {
        // 插件初始化逻辑
        System.out.println("MyPlugin initialized");
    }
    
    @Override
    public void destroy() {
        // 插件销毁逻辑
        System.out.println("MyPlugin destroyed");
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
```

### 3.2 注册插件

在 `src/main/resources` 目录下创建 `META-INF/services/net.ooder.editor.plugin.Plugin` 文件，内容为插件主类的全限定名：

```
com.example.myplugin.MyPlugin
```

### 3.3 开发插件功能

根据插件的功能需求，开发相应的组件：

1. **服务组件**：实现业务逻辑
2. **控制器**：处理 HTTP 请求
3. **视图组件**：定义前端视图
4. **工具类**：提供通用功能

### 3.4 打包插件

使用 Maven 打包插件：

```bash
mvn clean package
```

生成的 JAR 文件即为插件包。

## 4. 插件集成示例

### 4.1 集成到主应用

1. 将插件 JAR 文件复制到 `plugins` 目录
2. 启动应用，插件会自动加载

### 4.2 插件服务调用

在应用中调用插件服务：

```java
package net.ooder.editor.service;

import net.ooder.editor.plugin.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PluginIntegrationService {
    
    @Autowired
    private PluginManager pluginManager;
    
    public void usePluginService() {
        // 获取插件管理器
        // 调用插件服务
        pluginManager.getPlugins().forEach(plugin -> {
            System.out.println("Using plugin: " + plugin.getName());
        });
    }
}
```

## 5. 现有插件介绍

### 5.1 DSM 插件

DSM（Domain Specific Model）插件提供领域模型管理功能：

- 模型定义和编辑
- 模型版本管理
- 模型导出和导入
- 模型与代码生成

### 5.2 RIGHT 插件

RIGHT 插件提供权限管理功能：

- 用户管理
- 角色管理
- 权限分配
- 访问控制

### 5.3 VFS 插件

VFS（Virtual File System）插件提供虚拟文件系统功能：

- 文件和文件夹管理
- 文件版本控制
- 文件搜索和过滤
- 文件操作日志

## 6. 插件最佳实践

### 6.1 命名规范

- 插件 ID：使用小写字母和连字符，如 `my-plugin`
- 插件名称：使用清晰的描述性名称，如 `My Plugin`
- 版本号：遵循语义化版本规范，如 `1.0.0`

### 6.2 依赖管理

- 尽量减少外部依赖
- 使用 provided  scope 依赖主应用的库
- 确保依赖版本兼容

### 6.3 性能优化

- 插件初始化逻辑尽量轻量
- 避免阻塞操作
- 合理使用缓存
- 及时释放资源

### 6.4 安全性

- 验证所有输入参数
- 避免安全漏洞
- 遵循最小权限原则
- 保护敏感数据

## 7. 插件生命周期管理

### 7.1 加载

插件在应用启动时由插件管理器加载，加载顺序由插件依赖关系决定。

### 7.2 初始化

插件加载后，调用 `initialize` 方法进行初始化。

### 7.3 运行

插件在启用状态下正常运行，处理请求和事件。

### 7.4 禁用/启用

可以通过插件管理器动态禁用或启用插件。

### 7.5 销毁

应用关闭时，调用 `destroy` 方法销毁插件，释放资源。

## 8. 插件通信机制

### 8.1 事件驱动通信

插件之间通过事件机制进行通信，避免直接依赖：

```java
package net.ooder.editor.event;

public class PluginEvent {
    private String pluginId;
    private String eventType;
    private Object data;
    
    // 构造函数、getter 和 setter 方法
}
```

### 8.2 服务注册与发现

插件可以注册服务，其他插件可以通过插件管理器发现和使用这些服务。

## 9. 插件配置

### 9.1 配置文件

插件可以通过 `application.properties` 或 `application.yml` 配置：

```properties
# 插件配置示例
my-plugin.enabled=true
my-plugin.config.key=value
```

### 9.2 配置访问

插件可以通过 `PluginContext` 获取配置：

```java
@Override
public void initialize(PluginContext context) {
    Configuration config = context.getConfiguration();
    boolean enabled = config.getBoolean("my-plugin.enabled", true);
    String key = config.getString("my-plugin.config.key", "default");
}
```

## 10. 插件测试

### 10.1 单元测试

使用 JUnit 进行插件单元测试：

```java
package com.example.myplugin;

import net.ooder.editor.plugin.PluginContext;
import org.junit.Test;
import org.mockito.Mockito;

public class MyPluginTest {
    
    @Test
    public void testInitialize() {
        MyPlugin plugin = new MyPlugin();
        PluginContext context = Mockito.mock(PluginContext.class);
        plugin.initialize(context);
        // 验证初始化逻辑
    }
    
    @Test
    public void testDestroy() {
        MyPlugin plugin = new MyPlugin();
        plugin.destroy();
        // 验证销毁逻辑
    }
}
```

### 10.2 集成测试

将插件集成到主应用中进行测试，验证插件的功能和兼容性。

## 11. 插件发布

### 11.1 发布到插件仓库

1. 准备插件发布包
2. 编写插件元数据
3. 发布到插件仓库
4. 更新插件索引

### 11.2 插件更新

1. 检查插件更新
2. 下载更新包
3. 验证更新包
4. 安装更新
5. 重启插件

## 12. 常见问题与解决方案

### 12.1 插件加载失败

- 检查插件依赖是否完整
- 检查插件主类是否正确注册
- 检查插件 JAR 文件是否损坏

### 12.2 插件冲突

- 检查插件 ID 是否唯一
- 检查插件依赖版本是否兼容
- 检查插件服务是否冲突

### 12.3 插件性能问题

- 优化插件初始化逻辑
- 减少插件资源占用
- 避免阻塞操作
- 合理使用缓存

## 13. 示例插件

### 13.1 简单插件示例

```java
package net.ooder.tools.action;

import net.ooder.editor.plugin.Plugin;
import net.ooder.editor.plugin.PluginContext;
import org.springframework.stereotype.Component;

@Component
public class ActionAction implements Plugin {
    
    @Override
    public String getId() {
        return "action-plugin";
    }
    
    @Override
    public String getName() {
        return "Action Plugin";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public void initialize(PluginContext context) {
        // 插件初始化逻辑
    }
    
    @Override
    public void destroy() {
        // 插件销毁逻辑
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        // 启用/禁用逻辑
    }
}
```

## 14. 总结

ooder-agent-rad 的插件化设计为开发者提供了灵活的扩展机制。通过遵循本指南，开发者可以快速开发和集成插件，扩展系统功能。插件开发的核心是实现 `Plugin` 接口，并遵循插件开发规范。

插件化设计使 ooder-agent-rad 能够适应不同的业务场景和需求，提高了系统的灵活性和可扩展性。
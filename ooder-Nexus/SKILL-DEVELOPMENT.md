# AI 技能开发教程

## 1. 技能开发概述

Nexus 是一个基于 ooderAgent 架构的 P2P AI 能力分发枢纽产品，采用 MIT 开源协议。当前版本是在 ooderAgent 核心 0.6.6 开发的预览版程序，0.6.6 以后会独立建立分支。部分程序未经严格测试，正式发布版本前仅供研究学习。

### SDK 0.6.6 与示例程序说明

SDK 0.6.6 实现了完整的 ooderAgent 协议，为 Nexus 提供了稳定的底层通信能力。Nexus 在 SDK 基础的通讯和能力管理上，针对常见的路由器安装、智能设备安装提供了网络和路由管理示例页面，同时针对家庭网关的特殊设备也提供了相应的开发示例。

开发者可以通过 skillsCenter 下载相应的 skills 插件，安装在自己的路由器或网关上，就可以有针对性地进行开发，使其支持跨生态的 AI 分发服务。

### 1.2 技能概念

在 Nexus 系统中，**技能**是 AI 能力的封装单元，是可以被发布、分享和执行的功能模块。技能可以是简单的文本处理工具，也可以是复杂的机器学习模型应用。

### 1.3 技能特点

- **模块化**：技能是独立的功能模块，可单独开发和部署
- **可分享**：技能可以在 P2P 网络中分享给其他节点
- **可执行**：技能可以被远程或本地执行
- **可扩展**：系统支持通过技能扩展功能，无需修改核心代码

### 1.4 技能类型

- **本地技能**：在本地节点执行的技能
- **网络技能**：通过 P2P 网络从其他节点获取的技能
- **复合技能**：组合多个基础技能的复杂技能

## 2. 技能架构

### 2.1 技能核心组件

#### 2.1.1 技能接口

Nexus 技能系统基于 Java 接口定义，所有技能必须实现 `Skill` 接口。

```java
public interface Skill {
    // 获取技能信息
    SkillInfo getInfo();
    
    // 执行技能
    SkillResult execute(SkillContext context, SkillParam param);
    
    // 验证参数
    boolean validateParam(SkillParam param);
}
```

#### 2.1.2 核心数据结构

- **SkillInfo**：技能信息，包含名称、版本、描述等
- **SkillParam**：技能执行参数
- **SkillContext**：技能执行上下文
- **SkillResult**：技能执行结果
- **SkillException**：技能执行异常

### 2.2 技能生命周期

1. **开发**：编写技能代码，实现技能接口
2. **注册**：通过 SkillManager 注册技能
3. **发现**：系统自动发现或手动添加技能
4. **执行**：调用技能执行方法
5. **监控**：监控技能执行状态和性能
6. **更新**：更新技能版本
7. **卸载**：移除不需要的技能

## 3. 开发环境搭建

### 3.1 环境要求

- **Java**：JDK 8 或更高版本
- **Maven**：3.6 或更高版本
- **IDE**：推荐使用 IntelliJ IDEA 或 Eclipse
- **Nexus**：本地部署的 Nexus 实例

### 3.2 项目结构

```
my-skill/
├── pom.xml              # Maven 配置文件
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── example/
    │   │           └── skill/
    │   │               ├── MySkill.java      # 技能实现
    │   │               └── MySkillInfo.java   # 技能信息
    │   └── resources/
    │       └── skill.json                    # 技能描述文件
    └── test/
        └── java/
            └── com/
                └── example/
                    └── skill/
                        └── MySkillTest.java  # 技能测试
```

### 3.3 Maven 依赖

```xml
<dependencies>
    <!-- Nexus 核心依赖 -->
    <dependency>
        <groupId>net.ooder</groupId>
        <artifactId>ooder-nexus</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- 其他依赖 -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.36</version>
    </dependency>
</dependencies>
```

## 4. 技能开发流程

### 4.1 步骤 1：创建技能类

首先，创建一个实现 `Skill` 接口的技能类。

```java
package com.example.skill;

import net.ooder.nexus.model.Skill;
import net.ooder.nexus.model.SkillContext;
import net.ooder.nexus.model.SkillInfo;
import net.ooder.nexus.model.SkillParam;
import net.ooder.nexus.model.SkillResult;

public class MySkill implements Skill {
    
    @Override
    public SkillInfo getInfo() {
        return new MySkillInfo();
    }
    
    @Override
    public SkillResult execute(SkillContext context, SkillParam param) {
        // 技能执行逻辑
        String input = param.getString("input");
        String result = "Processed: " + input;
        
        return SkillResult.success(result);
    }
    
    @Override
    public boolean validateParam(SkillParam param) {
        return param.containsKey("input") && param.getString("input") != null;
    }
}
```

### 4.2 步骤 2：创建技能信息类

创建一个实现 `SkillInfo` 接口的类，提供技能的元数据信息。

```java
package com.example.skill;

import net.ooder.nexus.model.SkillInfo;

public class MySkillInfo implements SkillInfo {
    
    @Override
    public String getName() {
        return "My First Skill";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String getDescription() {
        return "A simple example skill";
    }
    
    @Override
    public String getAuthor() {
        return "Developer";
    }
    
    @Override
    public String getCategory() {
        return "Utilities";
    }
    
    @Override
    public String getIcon() {
        return "icon.png";
    }
    
    @Override
    public String getSkillId() {
        return "com.example.skill.MySkill";
    }
}
```

### 4.3 步骤 3：创建技能描述文件

在资源目录下创建 `skill.json` 文件，提供技能的详细描述。

```json
{
  "name": "My First Skill",
  "version": "1.0.0",
  "description": "A simple example skill that processes input text",
  "author": "Developer",
  "category": "Utilities",
  "icon": "icon.png",
  "skillId": "com.example.skill.MySkill",
  "parameters": [
    {
      "name": "input",
      "type": "string",
      "required": true,
      "description": "Text to process"
    }
  ],
  "returns": {
    "type": "string",
    "description": "Processed text"
  }
}
```

### 4.4 步骤 4：编译打包

使用 Maven 编译打包技能。

```bash
mvn clean package
```

### 4.5 步骤 5：注册技能

将技能 JAR 文件复制到 Nexus 的技能目录，或通过 API 注册技能。

#### 4.5.1 通过 API 注册

```bash
POST /api/personal/skills
Content-Type: multipart/form-data

# 表单数据
- skillFile: <skill.jar>
- skillJson: <skill.json>
```

#### 4.5.2 自动发现

如果配置了 `ooder.skill.auto-discovery=true`，系统会自动发现并注册位于 `skill.base-package` 下的技能。

## 5. 技能执行

### 5.1 本地执行

通过本地 API 执行技能。

```bash
POST /api/personal/execution/execute/{skillId}
Content-Type: application/json

{
  "parameters": {
    "input": "Hello, Nexus!"
  }
}
```

### 5.2 远程执行

通过 P2P 网络在远程节点执行技能。

```bash
POST /api/network/skill/execute
Content-Type: application/json

{
  "skillId": "com.example.skill.MySkill",
  "nodeId": "remote-node-1",
  "parameters": {
    "input": "Hello, Remote!"
  }
}
```

### 5.3 执行结果

技能执行后会返回 `SkillResult` 对象，包含执行状态和结果数据。

```json
{
  "success": true,
  "data": "Processed: Hello, Nexus!",
  "message": "Skill executed successfully",
  "executionTime": 123
}
```

## 6. 技能测试与调试

### 6.1 单元测试

为技能编写单元测试，确保功能正常。

```java
package com.example.skill;

import net.ooder.nexus.model.SkillContext;
import net.ooder.nexus.model.SkillParam;
import net.ooder.nexus.model.SkillResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MySkillTest {
    
    @Test
    public void testExecute() {
        MySkill skill = new MySkill();
        SkillParam param = new SkillParam();
        param.put("input", "Test input");
        
        SkillResult result = skill.execute(null, param);
        
        assertTrue(result.isSuccess());
        assertEquals("Processed: Test input", result.getData());
    }
    
    @Test
    public void testValidateParam() {
        MySkill skill = new MySkill();
        
        // 测试有效参数
        SkillParam validParam = new SkillParam();
        validParam.put("input", "Valid input");
        assertTrue(skill.validateParam(validParam));
        
        // 测试无效参数
        SkillParam invalidParam = new SkillParam();
        assertFalse(skill.validateParam(invalidParam));
    }
}
```

### 6.2 集成测试

在 Nexus 环境中测试技能的完整生命周期。

1. **部署技能**：将技能部署到 Nexus 实例
2. **执行测试**：通过 API 执行技能
3. **验证结果**：检查执行结果是否正确
4. **分享测试**：测试技能分享功能
5. **远程执行测试**：测试跨节点执行

### 6.3 调试技巧

- **日志记录**：在技能中添加详细的日志
- **参数验证**：严格验证输入参数
- **异常处理**：捕获并处理异常，返回有意义的错误信息
- **性能监控**：记录技能执行时间和资源使用情况

## 7. 技能发布与分享

### 7.1 技能发布

将开发完成的技能发布到 Nexus 系统中。

#### 7.1.1 通过控制台发布

1. 登录 Nexus 控制台
2. 导航到 "个人功能" → "我的技能"
3. 点击 "发布技能" 按钮
4. 上传技能 JAR 文件和描述文件
5. 填写技能信息
6. 点击 "发布" 完成操作

#### 7.1.2 通过 API 发布

```bash
POST /api/personal/skills
Content-Type: multipart/form-data

# 表单数据
- skillFile: <skill.jar>
- skillJson: <skill.json>
- name: "My Skill"
- description: "A useful skill"
```

### 7.2 技能分享

将技能分享给其他用户或群组。

#### 7.2.1 分享给个人

```bash
POST /api/personal/sharing
Content-Type: application/json

{
  "skillId": "com.example.skill.MySkill",
  "targetType": "user",
  "targetId": "user123"
}
```

#### 7.2.2 分享给群组

```bash
POST /api/personal/sharing
Content-Type: application/json

{
  "skillId": "com.example.skill.MySkill",
  "targetType": "group",
  "targetId": "group456"
}
```

### 7.3 技能市场

通过技能市场发现和安装其他用户分享的技能。

#### 7.3.1 浏览技能市场

```bash
GET /api/network/skill/market
```

#### 7.3.2 安装技能

```bash
POST /api/network/skill/subscribe
Content-Type: application/json

{
  "skillId": "com.example.skill.MySkill",
  "nodeId": "publisher-node"
}
```

## 8. 技能高级特性

### 8.1 技能依赖管理

技能可以声明依赖其他技能或库。

#### 8.1.1 依赖声明

在 `skill.json` 中添加依赖信息：

```json
{
  "name": "Advanced Skill",
  "version": "1.0.0",
  "dependencies": [
    {
      "skillId": "com.example.skill.BaseSkill",
      "version": "1.0.0"
    }
  ]
}
```

### 8.2 技能配置

技能可以通过配置文件自定义行为。

#### 8.2.1 配置文件

在技能资源目录下创建 `config.json`：

```json
{
  "timeout": 30000,
  "maxRetries": 3,
  "apiKey": "your-api-key"
}
```

#### 8.2.2 读取配置

```java
import java.io.InputStream;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurableSkill implements Skill {
    
    private Map<String, Object> config;
    
    public ConfigurableSkill() {
        try (InputStream is = getClass().getResourceAsStream("/config.json")) {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(is, Map.class);
        } catch (Exception e) {
            // 处理异常
        }
    }
    
    // 其他方法...
}
```

### 8.3 技能组合

创建复合技能，组合多个基础技能的功能。

```java
public class CompositeSkill implements Skill {
    
    private Skill skill1;
    private Skill skill2;
    
    public CompositeSkill() {
        this.skill1 = new TextProcessingSkill();
        this.skill2 = new SentimentAnalysisSkill();
    }
    
    @Override
    public SkillResult execute(SkillContext context, SkillParam param) {
        // 先执行第一个技能
        SkillResult result1 = skill1.execute(context, param);
        
        // 使用第一个技能的结果作为第二个技能的输入
        SkillParam param2 = new SkillParam();
        param2.put("text", result1.getData());
        
        // 执行第二个技能
        SkillResult result2 = skill2.execute(context, param2);
        
        return result2;
    }
    
    // 其他方法...
}
```

## 9. 最佳实践

### 9.1 代码规范

- **命名规范**：使用清晰、描述性的命名
- **代码风格**：遵循 Java 代码风格指南
- **注释**：添加详细的代码注释
- **异常处理**：妥善处理异常情况

### 9.2 性能优化

- **资源管理**：及时释放资源
- **缓存策略**：合理使用缓存
- **并行处理**：对于耗时操作，考虑并行处理
- **批处理**：对于多个相似操作，使用批处理减少开销

### 9.3 安全性

- **输入验证**：严格验证所有输入参数
- **权限检查**：检查执行权限
- **数据保护**：保护敏感数据
- **安全日志**：记录关键操作日志

### 9.4 可维护性

- **模块化**：将代码分解为小的、可管理的模块
- **测试覆盖**：编写全面的测试用例
- **文档**：提供详细的技能文档
- **版本控制**：使用版本控制管理技能代码

## 10. 常见问题与解决方案

### 10.1 开发问题

#### 10.1.1 技能注册失败
- **症状**：技能无法注册到系统中
- **原因**：技能接口实现错误、依赖缺失、权限不足
- **解决方案**：
  1. 检查技能是否正确实现了 `Skill` 接口
  2. 确保所有依赖都已正确添加
  3. 检查注册权限

#### 10.1.2 技能执行失败
- **症状**：技能执行时抛出异常或返回错误
- **原因**：参数错误、逻辑错误、资源不足
- **解决方案**：
  1. 检查输入参数是否正确
  2. 查看执行日志获取详细错误信息
  3. 检查技能逻辑是否正确
  4. 确保有足够的系统资源

### 10.2 部署问题

#### 10.2.1 技能无法分享
- **症状**：技能分享操作失败
- **原因**：网络问题、权限不足、目标节点不可达
- **解决方案**：
  1. 检查网络连接
  2. 确保有分享权限
  3. 验证目标节点是否在线

#### 10.2.2 远程执行延迟高
- **症状**：远程执行技能时响应时间过长
- **原因**：网络延迟、目标节点负载高、技能执行时间长
- **解决方案**：
  1. 选择网络延迟低的节点
  2. 避免在高负载节点执行技能
  3. 优化技能执行逻辑，减少执行时间

### 10.3 性能问题

#### 10.3.1 技能执行速度慢
- **症状**：技能执行时间过长
- **原因**：算法效率低、资源使用不当、外部依赖慢
- **解决方案**：
  1. 优化算法，提高执行效率
  2. 合理使用资源，避免资源浪费
  3. 缓存频繁使用的结果
  4. 考虑使用异步执行模式

#### 10.3.2 内存使用过高
- **症状**：技能执行时内存使用过高，可能导致 OOM
- **原因**：数据结构过大、内存泄漏、缓存策略不当
- **解决方案**：
  1. 使用更高效的数据结构
  2. 检查并修复内存泄漏
  3. 调整缓存策略，限制缓存大小
  4. 考虑使用流式处理大数据

## 11. 技能示例

### 11.1 文本处理技能

```java
public class TextProcessingSkill implements Skill {
    
    @Override
    public SkillInfo getInfo() {
        return new TextProcessingSkillInfo();
    }
    
    @Override
    public SkillResult execute(SkillContext context, SkillParam param) {
        String text = param.getString("text");
        String operation = param.getString("operation", "uppercase");
        
        String result;
        switch (operation) {
            case "uppercase":
                result = text.toUpperCase();
                break;
            case "lowercase":
                result = text.toLowerCase();
                break;
            case "reverse":
                result = new StringBuilder(text).reverse().toString();
                break;
            default:
                return SkillResult.error("Unknown operation: " + operation);
        }
        
        return SkillResult.success(result);
    }
    
    @Override
    public boolean validateParam(SkillParam param) {
        return param.containsKey("text") && param.getString("text") != null;
    }
}
```

### 11.2 数学计算技能

```java
public class MathSkill implements Skill {
    
    @Override
    public SkillInfo getInfo() {
        return new MathSkillInfo();
    }
    
    @Override
    public SkillResult execute(SkillContext context, SkillParam param) {
        double a = param.getDouble("a");
        double b = param.getDouble("b");
        String operation = param.getString("operation");
        
        double result;
        switch (operation) {
            case "add":
                result = a + b;
                break;
            case "subtract":
                result = a - b;
                break;
            case "multiply":
                result = a * b;
                break;
            case "divide":
                if (b == 0) {
                    return SkillResult.error("Division by zero");
                }
                result = a / b;
                break;
            default:
                return SkillResult.error("Unknown operation: " + operation);
        }
        
        return SkillResult.success(result);
    }
    
    @Override
    public boolean validateParam(SkillParam param) {
        return param.containsKey("a") && param.containsKey("b") && param.containsKey("operation");
    }
}
```

## 12. 总结

Nexus 技能开发是一个灵活、强大的系统，允许开发者创建和分享各种 AI 能力。通过遵循本教程中的指南和最佳实践，您可以开发出高质量、可靠的技能，为 Nexus 生态系统做出贡献。

相关应用推荐配合 ooder-skillsCenter 使用，以获得更好的技能管理和执行体验。

**注意**：本教程适用于 Nexus 预览版程序，部分功能可能在正式版本中有所变更。

---

**最后更新时间**：2026-02-02
**版本**：v1.0.0
**发布平台**：gitee 独家发布
# 代码规范

## 1. 概述

本文档定义了 ooder-agent-rad 项目的代码规范，包括命名约定、代码格式、注释规范、架构设计原则等方面。遵循这些规范可以提高代码的可读性、可维护性和可扩展性，促进团队协作。

## 2. 命名约定

### 2.1 包名

- 包名采用小写字母，使用点分隔符
- 包名应反映模块的功能和层次结构
- 示例：`net.ooder.editor.service`、`net.ooder.editor.annotation`

### 2.2 类名

- 类名采用 PascalCase 命名法
- 类名应清晰表达其功能和职责
- 示例：`ProjectManager`、`VFSService`、`AttendanceTypeEnum`

### 2.3 接口名

- 接口名采用 PascalCase 命名法
- 接口名应清晰表达其功能和职责
- 接口名通常不使用 `I` 前缀
- 示例：`Plugin`、`PluginContext`、`PermissionService`

### 2.4 方法名

- 方法名采用 camelCase 命名法
- 方法名应清晰表达其功能和操作
- 动词在前，名词在后
- 示例：`createProject`、`generateCode`、`getFiles`

### 2.5 变量名

- 变量名采用 camelCase 命名法
- 变量名应清晰表达其含义和用途
- 避免使用缩写和单个字符（循环变量除外）
- 示例：`projectName`、`filePath`、`permissionService`

### 2.6 常量名

- 常量名采用全大写字母，使用下划线分隔
- 常量名应清晰表达其含义和用途
- 示例：`MAX_VERSION_COUNT`、`DEFAULT_ENCODING`、`ERROR_CODE`

### 2.7 枚举名

- 枚举名采用 PascalCase 命名法
- 枚举值采用全大写字母，使用下划线分隔
- 示例：
  ```java
  public enum AttendanceType {
      NORMAL("正常"),
      LATE("迟到"),
      EARLY_LEAVE("早退"),
      ABSENCE("缺勤"),
      OVERTIME("加班");
      
      // ...
  }
  ```

### 2.8 注解名

- 注解名采用 PascalCase 命名法，以 `Annotation` 结尾
- 示例：`FormAnnotation`、`InputAnnotation`、`ModuleAnnotation`

## 3. 代码格式

### 3.1 缩进和空格

- 使用 4 个空格进行缩进
- 括号内不使用空格
- 运算符两侧使用空格
- 逗号后使用空格
- 示例：
  ```java
  public void createProject(String projectName, String description) {
      if (projectName != null && !projectName.isEmpty()) {
          // 代码逻辑
      }
  }
  ```

### 3.2 换行

- 每行代码不超过 120 个字符
- 左大括号 `{` 与语句在同一行
- 右大括号 `}` 单独一行
- 方法之间空一行
- 代码块之间空一行
- 示例：
  ```java
  public class ProjectManager {
      
      private DSMService dsmService;
      
      public void createProject(String projectName) {
          // 代码逻辑
      }
      
      public void generateCode(String modelId) {
          // 代码逻辑
      }
  }
  ```

### 3.3 导入语句

- 导入语句按以下顺序排列：
  1. Java 标准库
  2. 第三方库
  3. 项目内部包
- 相同包的导入语句放在一起
- 使用 `*` 通配符导入整个包时应谨慎，仅当导入的类超过 5 个时使用
- 示例：
  ```java
  import java.util.List;
  import java.util.Map;
  
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  
  import net.ooder.plugins.dsm.DSMService;
import net.ooder.editor.bean.ModelInfo;
  ```

## 4. 注释规范

### 4.1 类注释

- 类注释使用 Javadoc 格式
- 包含类的功能描述、作者、创建日期等信息
- 示例：
  ```java
  /**
   * 项目管理器，负责项目的创建、删除、更新等操作
   * 
   * @author ooderTeam
   * @since 1.0
   */
  @Service
  public class ProjectManager {
      // 代码逻辑
  }
  ```

### 4.2 方法注释

- 方法注释使用 Javadoc 格式
- 包含方法的功能描述、参数说明、返回值说明、异常说明等信息
- 示例：
  ```java
  /**
   * 创建项目并生成初始领域模型
   * 
   * @param projectName 项目名称
   * @param description 项目描述
   * @return 项目ID
   * @throws ProjectException 项目创建失败时抛出
   */
  public String createProject(String projectName, String description) throws ProjectException {
      // 代码逻辑
  }
  ```

### 4.3 字段注释

- 字段注释使用 Javadoc 格式
- 包含字段的功能描述、默认值等信息
- 示例：
  ```java
  /**
   * DSM 服务，用于领域模型管理
   */
  @Autowired
  private DSMService dsmService;
  ```

### 4.4 代码注释

- 代码注释应简洁明了，解释代码的逻辑和意图
- 避免不必要的注释，代码本身应具有自解释性
- 复杂的算法和业务逻辑应添加注释
- 示例：
  ```java
  // 生成初始领域模型
  Model model = dsmService.createInitialModel(projectName);
  System.out.println("Created initial model: " + model.getId());
  ```

### 4.5 TODO 注释

- 使用 `// TODO:` 标记需要完成的工作
- 包含具体的任务描述和负责人（可选）
- 示例：
  ```java
  // TODO: 实现项目权限管理
  // TODO(ooderTeam): 添加项目版本控制
  ```

## 5. 架构设计原则

### 5.1 单一职责原则

- 每个类和方法应只有一个明确的职责
- 类的大小应适中，通常不超过 500 行
- 方法应简洁，通常不超过 50 行

### 5.2 依赖倒置原则

- 依赖于抽象，而不是具体实现
- 优先使用接口和抽象类
- 示例：
  ```java
  // 推荐
  private DSMService dsmService;
  
  // 不推荐
  private DSMServiceImpl dsmServiceImpl;
  ```

### 5.3 接口隔离原则

- 接口应小而精，只包含必要的方法
- 避免臃肿的接口
- 示例：
  ```java
  // 推荐
  public interface Plugin {
      void initialize(PluginContext context);
      void destroy();
      boolean isEnabled();
  }
  ```

### 5.4 开闭原则

- 对扩展开放，对修改关闭
- 通过接口和抽象类实现扩展
- 示例：
  ```java
  // 推荐
  public interface ModelGenerator {
      void generateCode(Model model, OutputStream outputStream);
  }
  
  // 不推荐
  public class CodeGenerator {
      public void generateJavaCode(Model model) {
          // Java 代码生成逻辑
      }
      
      public void generatePythonCode(Model model) {
          // Python 代码生成逻辑
      }
  }
  ```

### 5.5 里氏替换原则

- 子类可以替换父类，而不会破坏原有功能
- 子类应遵循父类的约定和行为

## 6. 代码结构

### 6.1 类结构

- 类的成员顺序：
  1. 静态常量
  2. 实例字段
  3. 构造方法
  4. 静态方法
  5. 实例方法
  6. getter 和 setter 方法
  7. 内部类

### 6.2 方法结构

- 方法的执行顺序：
  1. 参数验证
  2. 业务逻辑
  3. 返回结果

### 6.3 异常处理

- 使用 try-with-resources 处理资源
- 捕获具体的异常，而不是通用的 `Exception`
- 异常信息应清晰表达错误原因
- 示例：
  ```java
  try (InputStream is = new FileInputStream(filePath);
       OutputStream os = new FileOutputStream(outputPath)) {
      // 代码逻辑
  } catch (FileNotFoundException e) {
      throw new RuntimeException("文件未找到: " + filePath, e);
  } catch (IOException e) {
      throw new RuntimeException("文件操作失败: " + e.getMessage(), e);
  }
  ```

## 7. 设计模式

### 7.1 单例模式

- 使用 Spring 的 `@Service`、`@Component` 等注解实现单例
- 避免手动实现单例模式

### 7.2 工厂模式

- 用于创建复杂对象
- 示例：
  ```java
  public interface ModelFactory {
      Model createModel(String modelName);
  }
  ```

### 7.3 观察者模式

- 用于事件驱动通信
- 使用 Spring 的事件机制
- 示例：
  ```java
  @Component
  public class PluginEventListener {
      
      @EventListener
      public void onModelCreated(ModelCreatedEvent event) {
          // 处理模型创建事件
      }
  }
  ```

### 7.4 策略模式

- 用于不同算法的切换
- 示例：
  ```java
  public interface CodeGenerator {
      void generateCode(Model model, OutputStream outputStream);
  }
  ```

### 7.5 模板方法模式

- 用于定义算法骨架
- 示例：
  ```java
  public abstract class AbstractGenerator {
      
      public final void generate(Model model, OutputStream outputStream) {
          // 固定算法步骤
          initialize();
          generateHeader(model);
          generateBody(model);
          generateFooter(model);
          finalize(outputStream);
      }
      
      protected abstract void generateHeader(Model model);
      protected abstract void generateBody(Model model);
      protected abstract void generateFooter(Model model);
      
      // 其他方法
  }
  ```

## 8. Spring 框架规范

### 8.1 依赖注入

- 使用构造函数注入，避免字段注入
- 示例：
  ```java
  // 推荐
  private final DSMService dsmService;
  
  @Autowired
  public ProjectManager(DSMService dsmService) {
      this.dsmService = dsmService;
  }
  
  // 不推荐
  @Autowired
  private DSMService dsmService;
  ```

### 8.2 注解使用

- 使用 Spring 提供的标准注解
- 合理使用 `@Service`、`@Component`、`@Controller`、`@RestController` 等注解
- 示例：
  ```java
  @RestController
  @RequestMapping("/api/dsm")
  public class DSMController {
      // 代码逻辑
  }
  ```

### 8.3 配置管理

- 使用 `application.properties` 或 `application.yml` 进行配置
- 使用 `@Value` 或 `@ConfigurationProperties` 读取配置
- 示例：
  ```java
  @Component
  @ConfigurationProperties(prefix = "dsm.plugin")
  public class DSMPluginConfig {
      
      private String modelStoragePath;
      private boolean validationEnabled;
      
      // getter 和 setter 方法
  }
  ```

## 9. 日志规范

### 9.1 日志框架

- 使用 SLF4J 作为日志门面
- 使用 Logback 或 Log4j2 作为日志实现

### 9.2 日志级别

- **ERROR**：记录系统错误，需要立即处理
- **WARN**：记录警告信息，可能会导致问题
- **INFO**：记录系统运行状态和关键操作
- **DEBUG**：记录调试信息，用于开发和测试
- **TRACE**：记录详细的跟踪信息，用于问题诊断

### 9.3 日志使用

- 日志对象使用类名作为 logger 名称
- 示例：
  ```java
  private static final Logger logger = LoggerFactory.getLogger(ProjectManager.class);
  
  public void createProject(String projectName) {
      logger.info("Creating project: {}", projectName);
      try {
          // 代码逻辑
          logger.info("Project created successfully: {}", projectName);
      } catch (Exception e) {
          logger.error("Failed to create project: {}", projectName, e);
          throw e;
      }
  }
  ```

## 10. 测试规范

### 10.1 测试框架

- 使用 JUnit 5 进行单元测试
- 使用 Mockito 进行模拟测试

### 10.2 测试命名

- 测试类名：被测试类名 + `Test`
- 测试方法名：`test` + 被测试方法名
- 示例：`ProjectManagerTest`、`testCreateProject`

### 10.3 测试覆盖

- 单元测试覆盖率应达到 80% 以上
- 重点测试核心业务逻辑和边界情况
- 示例：
  ```java
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
          Model model = new Model();
          model.setId("model-123");
          
          // 模拟行为
          when(dsmService.createInitialModel(projectName)).thenReturn(model);
          
          // 执行测试
          projectManager.createProject(projectName, "Test Description");
          
          // 验证结果
          verify(dsmService, times(1)).createInitialModel(projectName);
          verify(dsmService, times(1)).saveModel(model);
      }
  }
  ```

## 11. 性能优化

### 11.1 内存管理

- 避免创建不必要的对象
- 合理使用集合类，根据场景选择合适的集合类型
- 使用 try-with-resources 自动关闭资源

### 11.2 数据库操作

- 批量操作数据库，减少数据库连接次数
- 使用索引优化查询
- 避免 N+1 查询问题

### 11.3 缓存使用

- 合理使用缓存，减少重复计算和数据库查询
- 选择合适的缓存策略
- 示例：
  ```java
  @Cacheable(value = "projects", key = "#projectId")
  public Project getProject(String projectId) {
      // 从数据库查询项目
  }
  ```

### 11.4 并发处理

- 合理使用线程池，避免创建过多线程
- 线程安全的代码设计
- 示例：
  ```java
  private final ExecutorService executorService = Executors.newFixedThreadPool(10);
  
  public void processFiles(List<String> filePaths) {
      filePaths.forEach(filePath -> {
          executorService.submit(() -> {
              // 处理文件
          });
      });
  }
  ```

## 12. 安全规范

### 12.1 输入验证

- 验证所有输入参数
- 防止 SQL 注入、XSS 攻击等安全问题
- 示例：
  ```java
  public void createUser(String username, String password) {
      if (username == null || username.isEmpty()) {
          throw new IllegalArgumentException("用户名不能为空");
      }
      if (password == null || password.length() < 6) {
          throw new IllegalArgumentException("密码长度不能少于6位");
      }
      // 创建用户
  }
  ```

### 12.2 密码安全

- 使用安全的密码哈希算法（如 BCrypt）
- 避免明文存储密码

### 12.3 访问控制

- 实现适当的访问控制机制
- 验证用户权限
- 示例：
  ```java
  @PreAuthorize("hasPermission('project', 'create')")
  public void createProject(String projectName) {
      // 创建项目
  }
  ```

### 12.4 敏感数据保护

- 敏感数据加密存储
- 日志中避免记录敏感数据

## 13. 版本控制规范

### 13.1 分支管理

- **main**：主分支，用于发布稳定版本
- **develop**：开发分支，用于集成开发
- **feature/**：特性分支，用于开发新功能
- **hotfix/**：热修复分支，用于修复生产环境问题

### 13.2 提交规范

- 提交信息应清晰、简洁，不超过 50 个字符
- 提交信息应包含动词，如 "add"、"fix"、"update"、"remove" 等
- 示例：
  ```
  add: 新增项目创建功能
  fix: 修复模型生成bug
  update: 更新依赖版本
  remove: 删除无用代码
  ```

### 13.3 代码审查

- 所有代码变更必须经过代码审查
- 代码审查应关注：
  1. 代码规范
  2. 业务逻辑
  3. 性能优化
  4. 安全问题
  5. 测试覆盖

## 14. 持续集成与部署

### 14.1 构建脚本

- 使用 Maven 进行构建
- 构建脚本应包含：
  1. 代码编译
  2. 单元测试
  3. 代码质量检查
  4. 打包

### 14.2 代码质量检查

- 使用 SonarQube 进行代码质量检查
- 关注：
  1. 代码重复率
  2. 复杂度
  3. 潜在的 bug
  4. 安全漏洞

### 14.3 部署流程

- 自动化部署流程
- 部署环境：
  1. 开发环境
  2. 测试环境
  3. 预发布环境
  4. 生产环境

## 15. 总结

本文档定义了 ooder-agent-rad 项目的代码规范，包括命名约定、代码格式、注释规范、架构设计原则等方面。遵循这些规范可以提高代码的质量和可维护性，促进团队协作。

团队成员应认真学习和遵守这些规范，在开发过程中严格执行。定期进行代码审查和质量检查，不断改进和完善代码规范。
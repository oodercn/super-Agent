# 测试指南

## 1. 概述

本文档提供了 ooder-agent-rad 项目的测试指南，包括测试类型、测试框架、测试策略、测试流程和最佳实践等方面。测试是确保软件质量的重要手段，通过合理的测试策略和流程，可以提高软件的可靠性、稳定性和安全性。

## 2. 测试类型

### 2.1 单元测试

- **定义**：测试单个组件或模块的功能，验证其是否符合设计要求
- **范围**：类、方法、函数等最小单元
- **工具**：JUnit 5、Mockito
- **目标**：确保每个单元的功能正确，提高代码质量
- **示例**：
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

### 2.2 集成测试

- **定义**：测试多个组件或模块之间的交互，验证其是否协同工作
- **范围**：模块间的接口、服务间的调用
- **工具**：JUnit 5、Spring Boot Test
- **目标**：确保组件间的交互正确，发现集成问题
- **示例**：
  ```java
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
          // 从数据库或服务中验证项目是否创建成功
      }
  }
  ```

### 2.3 系统测试

- **定义**：测试整个系统的功能，验证其是否符合需求规格
- **范围**：整个应用系统
- **工具**：Selenium、Postman
- **目标**：确保系统的功能、性能、安全性等符合要求
- **示例**：
  ```java
  public class SystemTest {
      
      private WebDriver driver;
      
      @BeforeEach
      public void setUp() {
          driver = new ChromeDriver();
          driver.get("http://localhost:8080");
      }
      
      @Test
      public void testCreateProject() {
          // 模拟用户操作
          driver.findElement(By.id("projectName")).sendKeys("System Test Project");
          driver.findElement(By.id("description")).sendKeys("System Test Description");
          driver.findElement(By.id("createButton")).click();
          
          // 验证结果
          String message = driver.findElement(By.id("message")).getText();
          assertEquals("Project created successfully", message);
      }
      
      @AfterEach
      public void tearDown() {
          driver.quit();
      }
  }
  ```

### 2.4 性能测试

- **定义**：测试系统的性能，包括响应时间、吞吐量、资源利用率等
- **范围**：系统的性能指标
- **工具**：JMeter、Gatling
- **目标**：确保系统在高负载下的性能符合要求
- **示例**：
  ```java
  public class PerformanceTest {
      
      @Test
      public void testProjectCreationPerformance() {
          // 配置 JMeter 测试计划
          // 执行性能测试
          // 分析测试结果
      }
  }
  ```

### 2.5 安全测试

- **定义**：测试系统的安全性，包括身份验证、授权、输入验证等
- **范围**：系统的安全机制
- **工具**：OWASP ZAP、Burp Suite
- **目标**：发现系统的安全漏洞，确保系统的安全性
- **示例**：
  ```java
  public class SecurityTest {
      
      @Test
      public void testSqlInjection() {
          // 构造 SQL 注入攻击
          // 验证系统是否能够防御 SQL 注入
      }
      
      @Test
      public void testXssAttack() {
          // 构造 XSS 攻击
          // 验证系统是否能够防御 XSS 攻击
      }
  }
  ```

## 3. 测试框架

### 3.1 JUnit 5

- 最新的 JUnit 测试框架
- 支持 Java 8+ 特性
- 提供了更灵活的测试注解
- 示例：
  ```java
  @Test
  @DisplayName("测试项目创建功能")
  @Tag("unit")
  public void testCreateProject() {
      // 测试代码
  }
  ```

### 3.2 Mockito

- 用于模拟对象的测试框架
- 支持方法调用的模拟和验证
- 示例：
  ```java
  @Mock
  private DSMService dsmService;
  
  @InjectMocks
  private ProjectManager projectManager;
  
  @Test
  public void testCreateProject() {
      // 模拟行为
      when(dsmService.createInitialModel(anyString())).thenReturn(new Model());
      
      // 执行测试
      projectManager.createProject("Test Project", "Test Description");
      
      // 验证结果
      verify(dsmService, times(1)).createInitialModel(anyString());
  }
  ```

### 3.3 Spring Boot Test

- 用于 Spring Boot 应用的集成测试
- 支持自动配置和依赖注入
- 示例：
  ```java
  @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
  public class ProjectControllerTest {
      
      @Autowired
      private TestRestTemplate restTemplate;
      
      @Test
      public void testCreateProject() {
          ProjectDTO project = new ProjectDTO();
          project.setName("Test Project");
          project.setDescription("Test Description");
          
          ResponseEntity<ProjectDTO> response = restTemplate.postForEntity("/api/projects", project, ProjectDTO.class);
          
          assertEquals(HttpStatus.CREATED, response.getStatusCode());
          assertNotNull(response.getBody());
          assertEquals("Test Project", response.getBody().getName());
      }
  }
  ```

### 3.4 Selenium

- 用于 Web 应用的自动化测试
- 支持多种浏览器
- 示例：
  ```java
  public class WebTest {
      
      private WebDriver driver;
      
      @BeforeEach
      public void setUp() {
          driver = new ChromeDriver();
      }
      
      @Test
      public void testCreateProject() {
          driver.get("http://localhost:8080");
          
          // 模拟用户操作
          driver.findElement(By.id("projectName")).sendKeys("Web Test Project");
          driver.findElement(By.id("description")).sendKeys("Web Test Description");
          driver.findElement(By.id("createButton")).click();
          
          // 验证结果
          String successMessage = driver.findElement(By.className("success-message")).getText();
          assertEquals("Project created successfully", successMessage);
      }
      
      @AfterEach
      public void tearDown() {
          driver.quit();
      }
  }
  ```

### 3.5 Postman

- 用于 API 测试的工具
- 支持自动化测试和集合运行
- 示例：
  ```json
  {
      "name": "Create Project",
      "request": {
          "method": "POST",
          "url": "{{baseUrl}}/api/projects",
          "header": [
              {"key": "Content-Type", "value": "application/json"}
          ],
          "body": {
              "mode": "raw",
              "raw": {
                  "name": "Postman Test Project",
                  "description": "Postman Test Description"
              }
          }
      },
      "response": [
          {
              "test": "Status code is 201",
              "assertion": "responseCode.code === 201"
          },
          {
              "test": "Project name is correct",
              "assertion": "responseBody.name === 'Postman Test Project'"
          }
      ]
  }
  ```

## 4. 测试策略

### 4.1 测试金字塔

- **底部**：单元测试（70%）
- **中间**：集成测试（20%）
- **顶部**：系统测试（10%）
- 原则：底层测试成本低、执行快，应覆盖大部分功能

### 4.2 测试覆盖率

- **目标**：单元测试覆盖率达到 80% 以上
- **工具**：JaCoCo
- **指标**：
  - 行覆盖率：测试覆盖的代码行数占总代码行数的比例
  - 分支覆盖率：测试覆盖的分支占总分支的比例
  - 方法覆盖率：测试覆盖的方法占总方法的比例

### 4.3 测试数据管理

- 使用测试数据生成工具（如 Faker）生成测试数据
- 使用测试数据库或内存数据库（如 H2）
- 测试数据应包括正常数据、边界数据、异常数据

### 4.4 测试环境管理

- 开发环境：开发人员本地环境
- 测试环境：独立的测试环境，与生产环境相似
- 预发布环境：与生产环境相同的环境，用于最终验证
- 生产环境：实际运行环境，仅用于必要的验证

## 5. 测试流程

### 5.1 测试计划

- 定义测试目标和范围
- 确定测试类型和工具
- 制定测试策略和时间表
- 分配测试资源

### 5.2 测试设计

- 根据需求规格设计测试用例
- 覆盖功能、性能、安全性等方面
- 设计测试数据和测试环境

### 5.3 测试执行

- 运行测试用例
- 记录测试结果
- 报告测试发现的问题

### 5.4 缺陷管理

- 记录缺陷信息：描述、优先级、严重程度
- 跟踪缺陷状态：新建、处理中、已修复、已验证、已关闭
- 分析缺陷原因：代码问题、设计问题、需求问题

### 5.5 测试报告

- 汇总测试结果
- 分析测试覆盖率
- 报告缺陷情况
- 评估系统质量

## 6. 最佳实践

### 6.1 测试命名规范

- 测试类名：被测试类名 + `Test`
- 测试方法名：`test` + 被测试方法名 + 测试场景
- 示例：`ProjectManagerTest`、`testCreateProjectWithValidData`

### 6.2 测试用例设计

- 每个测试用例只测试一个功能点
- 测试用例应包括：前置条件、测试步骤、预期结果
- 测试用例应具有可重复性

### 6.3 测试代码质量

- 测试代码应遵循代码规范
- 测试代码应具有可读性和可维护性
- 测试代码应与生产代码分开管理

### 6.4 持续集成

- 将测试集成到持续集成流程中
- 每次代码提交都运行单元测试
- 定期运行集成测试和系统测试
- 使用工具如 Jenkins、GitHub Actions

### 6.5 测试自动化

- 尽可能自动化测试用例
- 自动化测试应覆盖核心功能和关键路径
- 定期维护和更新自动化测试用例

### 6.6 测试隔离

- 测试用例之间应相互隔离
- 每个测试用例应独立运行，不依赖其他测试用例的结果
- 使用 `@BeforeEach` 和 `@AfterEach` 注解设置和清理测试环境

## 7. 测试工具集成

### 7.1 Maven 配置

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>4.5.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>4.5.1</version>
    <scope>test</scope>
</dependency>

<!-- Spring Boot Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>2.7.0</version>
    <scope>test</scope>
</dependency>

<!-- JaCoCo -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 7.2 IDE 配置

- IntelliJ IDEA：
  1. 安装 JUnit 5 和 Mockito 插件
  2. 配置测试运行器
  3. 配置代码覆盖率工具

- Eclipse：
  1. 安装 JUnit 5 和 Mockito 插件
  2. 配置测试运行器
  3. 配置代码覆盖率工具

## 8. 常见问题与解决方案

### 8.1 测试执行慢

**问题**：测试用例执行时间长，影响开发效率

**解决方案**：
1. 优化测试代码，减少不必要的操作
2. 使用内存数据库（如 H2）替代真实数据库
3. 并行运行测试用例
4. 只运行相关的测试用例

### 8.2 测试覆盖率低

**问题**：测试覆盖率未达到目标要求

**解决方案**：
1. 分析覆盖率报告，找出未覆盖的代码
2. 补充测试用例，覆盖未覆盖的代码
3. 优化测试策略，提高覆盖率

### 8.3 测试用例维护困难

**问题**：测试用例数量多，维护成本高

**解决方案**：
1. 优化测试代码，提高可读性和可维护性
2. 使用测试数据生成工具，减少硬编码数据
3. 定期清理和更新测试用例
4. 采用测试驱动开发（TDD），先写测试用例，再写生产代码

### 8.4 测试环境问题

**问题**：测试环境不稳定，影响测试结果

**解决方案**：
1. 使用容器化技术（如 Docker）管理测试环境
2. 自动化测试环境的部署和配置
3. 定期清理和维护测试环境
4. 分离测试环境和开发环境

## 9. 总结

本文档提供了 ooder-agent-rad 项目的测试指南，包括测试类型、测试框架、测试策略、测试流程和最佳实践等方面。通过合理的测试策略和流程，可以提高软件的质量和可靠性，减少生产环境的问题。

测试是软件开发过程中的重要环节，团队成员应重视测试工作，遵循测试规范和最佳实践，不断提高测试质量和效率。
package net.ooder.skillcenter.model.impl;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillParam;
import net.ooder.skillcenter.model.SkillResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成技能实现
 * 用于将原型设计转换为实际的生产代码
 */
public class CodeGenerationSkill implements Skill {
    private static final String SKILL_ID = "code-generation-skill";
    private static final String SKILL_NAME = "代码生成技能";
    private static final String SKILL_DESCRIPTION = "将原型设计转换为实际的生产代码";
    
    private Map<String, SkillParam> params;
    
    public CodeGenerationSkill() {
        initParams();
    }
    
    /**
     * 初始化技能参数
     */
    private void initParams() {
        params = new HashMap<>();
        
        // 原型ID参数
        params.put("prototypeId", new SkillParam(
                "prototypeId",
                "原型ID",
                String.class,
                true
        ));
        
        // 原型详情参数
        params.put("prototypeDetails", new SkillParam(
                "prototypeDetails",
                "原型详情",
                Map.class,
                true
        ));
        
        // 目标语言参数
        params.put("targetLanguage", new SkillParam(
                "targetLanguage",
                "目标语言",
                String.class,
                false,
                "java"
        ));
        
        // 代码风格参数
        params.put("codeStyle", new SkillParam(
                "codeStyle",
                "代码风格",
                String.class,
                false,
                "spring-boot"
        ));
    }
    
    @Override
    public String getId() {
        return SKILL_ID;
    }
    
    @Override
    public String getName() {
        return SKILL_NAME;
    }
    
    @Override
    public String getDescription() {
        return SKILL_DESCRIPTION;
    }
    
    @Override
    public SkillResult execute(SkillContext context) throws SkillException {
        try {
            // 获取参数
            String prototypeId = (String) context.getParameter("prototypeId");
            Map<String, Object> prototypeDetails = (Map<String, Object>) context.getParameter("prototypeDetails");
            String targetLanguage = (String) context.getParameter("targetLanguage");
            String codeStyle = (String) context.getParameter("codeStyle");
            
            // 使用默认值
            if (targetLanguage == null) {
                targetLanguage = "java";
            }
            if (codeStyle == null) {
                codeStyle = "spring-boot";
            }
            
            // 验证必填参数
            if (prototypeId == null || prototypeId.isEmpty()) {
                throw new SkillException(getId(), "原型ID不能为空", SkillException.ErrorCode.PARAMETER_ERROR);
            }
            if (prototypeDetails == null || prototypeDetails.isEmpty()) {
                throw new SkillException(getId(), "原型详情不能为空", SkillException.ErrorCode.PARAMETER_ERROR);
            }
            
            // 执行核心逻辑：生成代码
            String projectId = generateProjectId();
            Map<String, Object> generatedCode = generateCode(prototypeId, prototypeDetails, targetLanguage, codeStyle);
            Map<String, Object> buildConfig = generateBuildConfig(prototypeDetails, codeStyle);
            Map<String, Object> deploymentConfig = generateDeploymentConfig(prototypeDetails);
            
            // 构建结果
            SkillResult result = new SkillResult();
            result.setMessage("代码生成成功");
            result.addData("projectId", projectId);
            result.addData("prototypeId", prototypeId);
            result.addData("targetLanguage", targetLanguage);
            result.addData("codeStyle", codeStyle);
            result.addData("generatedCode", generatedCode);
            result.addData("buildConfig", buildConfig);
            result.addData("deploymentConfig", deploymentConfig);
            
            return result;
        } catch (SkillException e) {
            throw e;
        } catch (Exception e) {
            throw new SkillException(getId(), "代码生成失败: " + e.getMessage(), 
                                     SkillException.ErrorCode.EXECUTION_EXCEPTION, e);
        }
    }
    
    /**
     * 生成项目ID
     */
    private String generateProjectId() {
        return "proj-" + System.currentTimeMillis();
    }
    
    /**
     * 生成代码
     */
    private Map<String, Object> generateCode(String prototypeId, Map<String, Object> prototypeDetails, 
                                            String targetLanguage, String codeStyle) {
        Map<String, Object> code = new HashMap<>();
        
        // 获取原型架构
        Map<String, Object> architecture = (Map<String, Object>) prototypeDetails.get("architecture");
        
        // 获取功能模块
        Map<String, Object> modules = (Map<String, Object>) prototypeDetails.get("modules");
        
        // 生成前端代码（如果是web应用）
        if (architecture != null && "web".equals(architecture.get("architectureStyle"))) {
            code.put("frontendCode", generateFrontendCode(modules, architecture));
        }
        
        // 生成后端代码
        code.put("backendCode", generateBackendCode(modules, architecture, targetLanguage, codeStyle));
        
        // 生成配置文件
        code.put("configFiles", generateConfigFiles(architecture, codeStyle));
        
        // 生成README文档
        code.put("readme", generateReadme(prototypeId, prototypeDetails));
        
        return code;
    }
    
    /**
     * 生成前端代码
     */
    private Map<String, Object> generateFrontendCode(Map<String, Object> modules, Map<String, Object> architecture) {
        Map<String, Object> frontendCode = new HashMap<>();
        String frontendFramework = (String) architecture.get("frontend");
        
        // 生成页面组件
        Map<String, String> pages = new HashMap<>();
        for (String moduleName : modules.keySet()) {
            pages.put(moduleName.toLowerCase() + ".jsx", generatePageComponent(moduleName, frontendFramework));
        }
        frontendCode.put("pages", pages);
        
        // 生成路由配置
        frontendCode.put("routes.js", generateRoutes(modules.keySet()));
        
        // 生成主应用入口
        frontendCode.put("App.jsx", generateAppEntry(frontendFramework));
        
        return frontendCode;
    }
    
    /**
     * 生成后端代码
     */
    private Map<String, Object> generateBackendCode(Map<String, Object> modules, Map<String, Object> architecture, 
                                                  String targetLanguage, String codeStyle) {
        Map<String, Object> backendCode = new HashMap<>();
        
        // 生成控制器
        Map<String, String> controllers = new HashMap<>();
        for (String moduleName : modules.keySet()) {
            controllers.put(capitalizeFirstLetter(moduleName) + "Controller." + getFileExtension(targetLanguage), 
                           generateController(moduleName, targetLanguage, codeStyle));
        }
        backendCode.put("controllers", controllers);
        
        // 生成服务层
        Map<String, String> services = new HashMap<>();
        for (String moduleName : modules.keySet()) {
            services.put(capitalizeFirstLetter(moduleName) + "Service." + getFileExtension(targetLanguage), 
                        generateService(moduleName, targetLanguage, codeStyle));
        }
        backendCode.put("services", services);
        
        // 生成数据模型
        backendCode.put("models", generateModels(modules, targetLanguage, codeStyle));
        
        // 生成主应用类
        backendCode.put("Application." + getFileExtension(targetLanguage), 
                       generateApplicationClass(targetLanguage, codeStyle));
        
        return backendCode;
    }
    
    /**
     * 生成配置文件
     */
    private Map<String, String> generateConfigFiles(Map<String, Object> architecture, String codeStyle) {
        Map<String, String> configFiles = new HashMap<>();
        
        if ("spring-boot".equals(codeStyle)) {
            configFiles.put("application.properties", generateSpringBootConfig());
            configFiles.put("pom.xml", generateMavenPom());
        } else if ("node.js".equals(codeStyle)) {
            configFiles.put("package.json", generatePackageJson());
            configFiles.put(".env", generateEnvFile());
        }
        
        return configFiles;
    }
    
    /**
     * 生成构建配置
     */
    private Map<String, Object> generateBuildConfig(Map<String, Object> prototypeDetails, String codeStyle) {
        Map<String, Object> buildConfig = new HashMap<>();
        
        buildConfig.put("buildTool", "spring-boot".equals(codeStyle) ? "maven" : "npm");
        buildConfig.put("buildCommand", "spring-boot".equals(codeStyle) ? "mvn clean package" : "npm run build");
        buildConfig.put("testCommand", "spring-boot".equals(codeStyle) ? "mvn test" : "npm test");
        
        return buildConfig;
    }
    
    /**
     * 生成部署配置
     */
    private Map<String, Object> generateDeploymentConfig(Map<String, Object> prototypeDetails) {
        Map<String, Object> deploymentConfig = new HashMap<>();
        
        deploymentConfig.put("containerized", true);
        deploymentConfig.put("dockerFile", generateDockerFile());
        deploymentConfig.put("deploymentType", "local");
        deploymentConfig.put("port", 8080);
        
        return deploymentConfig;
    }
    
    /**
     * 生成页面组件
     */
    private String generatePageComponent(String moduleName, String framework) {
        return "// " + moduleName + " Page Component\n" +
               "import React from 'react';\n\n" +
               "const " + capitalizeFirstLetter(moduleName) + "Page = () => {\n" +
               "  return (\n" +
               "    <div>\n" +
               "      <h1>" + moduleName + " Page</h1>\n" +
               "      <p>This is the " + moduleName + " page.</p>\n" +
               "    </div>\n" +
               "  );\n" +
               "}\n\n" +
               "export default " + capitalizeFirstLetter(moduleName) + "Page;";
    }
    
    /**
     * 生成路由配置
     */
    private String generateRoutes(Iterable<String> moduleNames) {
        StringBuilder routes = new StringBuilder("// Routes Configuration\n" +
                                               "import React from 'react';\n" +
                                               "import { Routes, Route } from 'react-router-dom';\n\n");
        
        // 生成导入语句
        for (String moduleName : moduleNames) {
            routes.append("import ")
                  .append(capitalizeFirstLetter(moduleName))
                  .append("Page from './pages/")
                  .append(moduleName.toLowerCase())
                  .append("';\n");
        }
        
        // 生成路由定义
        routes.append("\nconst AppRoutes = () => {\n" +
                      "  return (\n" +
                      "    <Routes>\n");
        
        for (String moduleName : moduleNames) {
            routes.append("      <Route path='/").append(moduleName.toLowerCase())
                  .append("' element={<").append(capitalizeFirstLetter(moduleName)).append("Page />} />\n");
        }
        
        routes.append("    </Routes>\n" +
                      "  );\n" +
                      "}\n\n" +
                      "export default AppRoutes;");
        
        return routes.toString();
    }
    
    /**
     * 生成主应用入口
     */
    private String generateAppEntry(String framework) {
        return "// App Entry Point\n" +
               "import React from 'react';\n" +
               "import { BrowserRouter as Router } from 'react-router-dom';\n" +
               "import AppRoutes from './routes';\n\n" +
               "function App() {\n" +
               "  return (\n" +
               "    <Router>\n" +
               "      <div className='App'>\n" +
               "        <AppRoutes />\n" +
               "      </div>\n" +
               "    </Router>\n" +
               "  );\n" +
               "}\n\n" +
               "export default App;";
    }
    
    /**
     * 生成控制器
     */
    private String generateController(String moduleName, String language, String style) {
        if ("java".equals(language) && "spring-boot".equals(style)) {
            return "package com.example.controller;\n\n" +
                   "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                   "import org.springframework.web.bind.annotation.RestController;\n\n" +
                   "@RestController\n" +
                   "@RequestMapping('/api/" + moduleName.toLowerCase() + "')\n" +
                   "public class " + capitalizeFirstLetter(moduleName) + "Controller {\n\n" +
                   "    // TODO: Implement " + moduleName + " controller methods\n\n" +
                   "}";
        }
        return "// " + moduleName + " Controller";
    }
    
    /**
     * 生成服务层
     */
    private String generateService(String moduleName, String language, String style) {
        if ("java".equals(language) && "spring-boot".equals(style)) {
            return "package com.example.service;\n\n" +
                   "import org.springframework.stereotype.Service;\n\n" +
                   "@Service\n" +
                   "public class " + capitalizeFirstLetter(moduleName) + "Service {\n\n" +
                   "    // TODO: Implement " + moduleName + " service logic\n\n" +
                   "}";
        }
        return "// " + moduleName + " Service";
    }
    
    /**
     * 生成数据模型
     */
    private Map<String, String> generateModels(Map<String, Object> modules, String language, String style) {
        Map<String, String> models = new HashMap<>();
        
        if ("java".equals(language) && "spring-boot".equals(style)) {
            for (String moduleName : modules.keySet()) {
                models.put(capitalizeFirstLetter(moduleName) + ".java", 
                          generateJavaModel(moduleName));
            }
        }
        
        return models;
    }
    
    /**
     * 生成Java数据模型
     */
    private String generateJavaModel(String moduleName) {
        return "package com.example.model;\n\n" +
               "import javax.persistence.Entity;\n" +
               "import javax.persistence.Id;\n" +
               "import javax.persistence.GeneratedValue;\n" +
               "import javax.persistence.GenerationType;\n\n" +
               "@Entity\n" +
               "public class " + capitalizeFirstLetter(moduleName) + " {\n\n" +
               "    @Id\n" +
               "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
               "    private Long id;\n\n" +
               "    // TODO: Add fields for " + moduleName + "\n\n" +
               "    // Getters and Setters\n\n" +
               "    public Long getId() {\n" +
               "        return id;\n" +
               "    }\n\n" +
               "    public void setId(Long id) {\n" +
               "        this.id = id;\n" +
               "    }\n\n" +
               "}";
    }
    
    /**
     * 生成主应用类
     */
    private String generateApplicationClass(String language, String style) {
        if ("java".equals(language) && "spring-boot".equals(style)) {
            return "package com.example;\n\n" +
                   "import org.springframework.boot.SpringApplication;\n" +
                   "import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n" +
                   "@SpringBootApplication\n" +
                   "public class Application {\n\n" +
                   "    public static void main(String[] args) {\n" +
                   "        SpringApplication.run(Application.class, args);\n" +
                   "    }\n\n" +
                   "}";
        }
        return "// Application Entry Point";
    }
    
    /**
     * 生成配置文件
     */
    private String generateSpringBootConfig() {
        return "# Application Configuration\n" +
               "spring.application.name=example-app\n" +
               "server.port=8080\n\n" +
               "# Database Configuration\n" +
               "spring.datasource.url=jdbc:mysql://localhost:3306/example_db\n" +
               "spring.datasource.username=root\n" +
               "spring.datasource.password=password\n" +
               "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n\n" +
               "# JPA Configuration\n" +
               "spring.jpa.hibernate.ddl-auto=update\n" +
               "spring.jpa.show-sql=true";
    }
    
    /**
     * 生成Maven POM文件
     */
    private String generateMavenPom() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
               "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
               "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
               "    <modelVersion>4.0.0</modelVersion>\n\n" +
               "    <groupId>com.example</groupId>\n" +
               "    <artifactId>example-app</artifactId>\n" +
               "    <version>1.0.0</version>\n\n" +
               "    <parent>\n" +
               "        <groupId>org.springframework.boot</groupId>\n" +
               "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
               "        <version>2.7.0</version>\n" +
               "    </parent>\n\n" +
               "    <dependencies>\n" +
               "        <dependency>\n" +
               "            <groupId>org.springframework.boot</groupId>\n" +
               "            <artifactId>spring-boot-starter-web</artifactId>\n" +
               "        </dependency>\n" +
               "        <dependency>\n" +
               "            <groupId>org.springframework.boot</groupId>\n" +
               "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
               "        </dependency>\n" +
               "        <dependency>\n" +
               "            <groupId>mysql</groupId>\n" +
               "            <artifactId>mysql-connector-java</artifactId>\n" +
               "            <scope>runtime</scope>\n" +
               "        </dependency>\n" +
               "    </dependencies>\n\n" +
               "    <build>\n" +
               "        <plugins>\n" +
               "            <plugin>\n" +
               "                <groupId>org.springframework.boot</groupId>\n" +
               "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
               "            </plugin>\n" +
               "        </plugins>\n" +
               "    </build>\n\n" +
               "</project>";
    }
    
    /**
     * 生成package.json文件
     */
    private String generatePackageJson() {
        return "{\n" +
               "  \"name\": \"example-app\",\n" +
               "  \"version\": \"1.0.0\",\n" +
               "  \"description\": \"Example Application\",\n" +
               "  \"main\": \"index.js\",\n" +
               "  \"scripts\": {\n" +
               "    \"start\": \"react-scripts start\",\n" +
               "    \"build\": \"react-scripts build\",\n" +
               "    \"test\": \"react-scripts test\"\n" +
               "  }\n" +
               "}";
    }
    
    /**
     * 生成.env文件
     */
    private String generateEnvFile() {
        return "# Environment Variables\n" +
               "PORT=3000\n" +
               "API_URL=http://localhost:8080/api\n";
    }
    
    /**
     * 生成README文档
     */
    private String generateReadme(String prototypeId, Map<String, Object> prototypeDetails) {
        return "# Example Application\n\n" +
               "## Prototype ID\n" +
               prototypeId + "\n\n" +
               "## Description\n" +
               "Generated application from prototype.\n\n" +
               "## Getting Started\n\n" +
               "### Prerequisites\n" +
               "- Java 11+\n" +
               "- Maven\n\n" +
               "### Installation\n" +
               "1. Clone the repository\n" +
               "2. Install dependencies: `mvn install`\n" +
               "3. Run the application: `mvn spring-boot:run`\n\n" +
               "## Usage\n" +
               "The application will be available at http://localhost:8080\n\n" +
               "## Built With\n" +
               "- Spring Boot\n" +
               "- Maven\n";
    }
    
    /**
     * 生成Dockerfile
     */
    private String generateDockerFile() {
        return "FROM openjdk:11-jre-slim\n\n" +
               "WORKDIR /app\n\n" +
               "COPY target/*.jar app.jar\n\n" +
               "EXPOSE 8080\n\n" +
               "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]";
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String language) {
        return "java".equals(language) ? "java" : "js";
    }
    
    /**
     * 首字母大写
     */
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    @Override
    public boolean isAvailable() {
        // 该技能不需要外部依赖，始终可用
        return true;
    }
    
    @Override
    public Map<String, SkillParam> getParams() {
        return params;
    }
}
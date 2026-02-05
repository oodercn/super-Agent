# 核心服务详解

## 1. 总览

ooder-agent-rad 包含多个核心服务，这些服务构成了系统的业务逻辑层，负责处理各种业务请求和数据操作。本文档将详细介绍以下核心服务：

1. RADEditor 服务
2. ProjectManager 服务
3. VFSService 服务

## 2. RADEditor 服务

### 2.1 服务概述

**核心实现**：[RADEditor.java](../../src/main/java/net/ooder/editor/service/RADEditor.java)

RADEditor 是 ooder-agent-rad 的核心服务之一，负责处理项目导出、打开项目、获取项目配置等核心功能。它是连接前端设计器和后端服务的重要桥梁。

### 2.2 主要功能

| 功能 | 描述 | 核心方法 |
|------|------|----------|
| 项目导出 | 将设计好的项目导出为可部署的代码 | `export()` |
| 打开项目 | 打开已存在的项目 | `openProject()` |
| 获取项目配置 | 获取项目的配置信息 | `getProjectConfig()` |
| 获取域列表 | 获取项目的域列表 | `getDomainList()` |

### 2.3 服务设计

RADEditor 服务采用了注解驱动的设计，使用了以下核心注解：

```java
/**
 * RAD编辑器服务类
 * 负责项目导出、打开项目、获取项目配置等功能
 */
@Controller
@RequestMapping(value = {"/RAD/"})
@MethodChinaName(cname = "OOD编辑器")
public class RADEditor {
    // 服务实现...
}
```

### 2.4 核心方法实现

#### 项目导出方法

```java
/**
 * 导出项目文件到本地临时目录
 * @param projectName 项目名称
 * @return 导出结果
 */
@RequestMapping(value = {"export"}, method = {RequestMethod.GET})
@ResponseBody
@MethodChinaName(cname = "导出项目")
public ResultModel<Boolean> export(String projectName) {
    ResultModel<Boolean> resultModel = new ResultModel<>();
    try {
        // 获取项目版本
        ProjectVersion version = getClient().getProjectVersionByName(projectName);
        // 获取所有非.cls文件并导出
        List<FileInfo> fileInfos = version.getRootFolder().getFileListRecursively();
        for (FileInfo fileInfo : fileInfos) {
            if (!fileInfo.getName().endsWith(".cls")) {
                exportFile(fileInfo, version);
            }
        }
        
        // 设置构建标志
        JDSActionContext.getActionContext().getContext().put("build", "false");
        
        // 导出所有模块文件
        Set<EUModule> modules = version.getAllModule();
        for (EUModule module : modules) {
            exportModule(module);
        }
        
        // 移除构建标志
        JDSActionContext.getActionContext().getContext().remove("build");
        
        // 设置导出成功
        resultModel.setData(true);
        resultModel.setSuccess(true);
    } catch (Exception e) {
        logger.error("导出项目失败: {}", e.getMessage(), e);
        resultModel.setSuccess(false);
        resultModel.setMessage(e.getMessage());
    }
    
    return resultModel;
}
```

#### 打开项目方法

```java
/**
 * 打开项目，获取项目信息
 * @param projectName 项目名称
 * @return 项目信息
 */
@MethodChinaName(cname = "装载工程")
@RequestMapping(value = {"openProject"}, method = {RequestMethod.GET})
public @ResponseBody
ResultModel<ProjectInfo> openProject(String projectName) {
    ResultModel<ProjectInfo> result = new ResultModel<>();
    
    try {
        // 获取项目版本
        ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
        Project project = projectVersion.getProject();
        
        // 获取域列表
        List<OODFile> modules = getDomainList(projectName);
        
        // 构建项目信息
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setFiles(modules);
        projectInfo.setIndex(project.getConfig().getIndex());
        
        // 设置结果
        result.setData(projectInfo);
        result.setSuccess(true);
    } catch (JDSException e) {
        logger.error("打开项目失败: {}", e.getMessage(), e);
        // 设置错误结果
        ErrorResultModel<ProjectInfo> errorResult = new ErrorResultModel<>();
        errorResult.setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
        errorResult.setErrdes(e.getMessage());
        return errorResult;
    }

    return result;
}
```

### 2.5 服务调用示例

```javascript
// 前端调用RADEditor服务导出项目
ood.Ajax.get('/RAD/export', {
    projectName: 'myProject'
}, function(result) {
    if (result.success) {
        alert('项目导出成功！');
    } else {
        alert('项目导出失败：' + result.message);
    }
});

// 前端调用RADEditor服务打开项目
ood.Ajax.get('/RAD/openProject', {
    projectName: 'myProject'
}, function(result) {
    if (result.success) {
        console.log('项目打开成功，项目信息：', result.data);
    } else {
        alert('项目打开失败：' + result.errdes);
    }
});
```

## 3. ProjectManager 服务

### 3.1 服务概述

**核心实现**：[ProjectManager.java](../../src/main/java/net/ooder/editor/service/ProjectManager.java)

ProjectManager 服务负责工程的创建、克隆、查询、版本管理等核心功能，是项目管理的核心服务。

### 3.2 主要功能

| 功能 | 描述 | 核心方法 |
|------|------|----------|
| 创建项目 | 创建新的项目 | `createProject()` |
| 克隆项目 | 从现有项目克隆新项目 | `cloneProject()` |
| 获取项目列表 | 获取项目列表 | `getProjectList()` |
| 获取项目信息 | 获取项目详细信息 | `getProjectInfo()` |
| 删除项目 | 删除项目 | `delProject()` |
| 版本管理 | 管理项目版本 | `versionStatusAction()` |
| API配置 | 配置项目API | `addAPI()` |

### 3.3 服务设计

ProjectManager 服务采用了注解驱动的设计，使用了以下核心注解：

```java
/**
 * 工程管理控制器类
 * 负责工程的创建、克隆、查询、版本管理等核心功能
 */
@Controller
@RequestMapping(value = {"/admin/"})
@MethodChinaName(cname = "RAD工程管理")
public class ProjectManager {
    // 服务实现...
}
```

### 3.4 核心方法实现

#### 创建项目方法

```java
/**
 * 创建工程
 * 支持从模板克隆或全新创建工程
 * 
 * @param newProjectName 新工程名称
 * @param projectName 原始工程名称
 * @param desc 工程描述
 * @param projectType 工程访问类型
 * @param tempName 模板名称
 * @param url 工程公共服务器URL
 * @return 创建结果
 */
@MethodChinaName(cname = "创建工程")
@RequestMapping(value = {"createProject"}, method = {RequestMethod.GET, RequestMethod.POST})
public @ResponseBody
ResultModel<OODProject> createProject(String newProjectName, String projectName, String desc, ProjectDefAccess projectType, String tempName, String url) {
    ResultModel<OODProject> result = new ResultModel<OODProject>();
    try {
        // 参数验证和默认值设置
        if (tempName != null && newProjectName.equals(tempName)) {
            newProjectName = newProjectName + "1";
        }
        if (projectType == null) {
            projectType = ProjectDefAccess.Public;
        }
        if (newProjectName == null && projectName != null) {
            newProjectName = projectName;
        }
        
        Project project = null;
        try {
            // 尝试从模板克隆工程
            project = getClient().cloneProject(newProjectName, desc, tempName, ProjectDefAccess.Public);
            logger.info("从模板克隆工程成功: {}", newProjectName);
        } catch (JDSException e) {
            logger.debug("从模板克隆工程失败，尝试全新创建: {}", e.getMessage());
        }

        if (project == null) {
            // 克隆失败则全新创建工程
            project = getClient().createProject(newProjectName, desc, projectType);
            logger.info("全新创建工程成功: {}", newProjectName);
        }
        
        // 设置工程公共服务器URL
        if (url != null) {
            project.getConfig().setPublicServerUrl(url);
        }
        
        // 添加当前用户为工程所有者
        project.getConfig().addDevPersons(ProjectRoleType.own, this.getClient().getConnectInfo().getUserID());
        project.updateConfig(project.getConfig());
        
        result.setData(new OODProject(project));
    } catch (JDSException e) {
        logger.error("创建工程失败: {}", e.getMessage(), e);
        result = new ErrorResultModel();
        ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
        ((ErrorResultModel) result).setErrdes(e.getMessage());
    }
    return result;
}
```

#### 获取项目列表方法

```java
/**
 * 获取工程列表
 * 
 * @param pattern 搜索模式
 * @return 项目列表
 */
@MethodChinaName(cname = "获取工程")
@RequestMapping(value = {"getProjectList"}, method = {RequestMethod.GET, RequestMethod.POST})
public @ResponseBody
ListResultModel<List<OODProject>> getProjectList(String pattern) {
    ListResultModel<List<OODProject>> result = new ListResultModel<List<OODProject>>();
    List<OODProject> modules = new ArrayList<OODProject>();
    try {
        List<Project> projects = getClient().getAllProject(ProjectDefAccess.Public);
        List<Project> projectList = new ArrayList<Project>();
        for (Project project : projects) {
            if (pattern != null && !pattern.equals("")) {
                Pattern p = Pattern.compile(pattern);
                Matcher matcher = p.matcher(project.getProjectName());
                Matcher descmatcher = p.matcher(project.getDesc());
                if (matcher.find() || descmatcher.find()) {
                    projectList.add(project);
                }
            } else {
                projectList.add(project);
            }

        }
        result = PageUtil.getDefaultPageList(projectList, OODProject.class);
    } catch (JDSException e) {
        e.printStackTrace();
    }
    return result;
}
```

## 4. VFSService 服务

### 4.1 服务概述

**核心实现**：[VFSService.java](../../src/main/java/net/ooder/editor/service/VFSService.java)

VFSService 是虚拟文件系统服务，负责处理文件和文件夹的创建、删除、复制、移动等操作，是连接前端文件树和后端文件系统的重要桥梁。

### 4.2 主要功能

| 功能 | 描述 | 核心方法 |
|------|------|----------|
| 添加文件夹 | 创建新文件夹 | `addFolder()` |
| 刷新项目 | 刷新项目文件列表 | `reLoadProject()` |
| 添加文件 | 上传文件到项目中 | `uploadFiles()` |
| 导入文件 | 导入外部文件到项目 | `importFile()` |
| 拷贝文件/文件夹 | 拷贝文件或文件夹 | `copy()` |
| 获取文件内容 | 获取文件内容 | `getFileContent()` |
| 删除文件 | 删除文件 | `delFile()` |
| 重命名文件/文件夹 | 重命名文件或文件夹 | `reName()` |
| 打开文件夹 | 获取文件夹内容 | `openFolder()` |
| 保存文件内容 | 保存文件内容 | `saveContent()` |
| 文件版本管理 | 管理文件版本 | `getAllFileVersion()` |

### 4.3 服务设计

VFSService 服务采用了注解驱动的设计，使用了以下核心注解：

```java
/**
 * 虚拟文件系统服务类
 * 负责文件和文件夹的创建、删除、复制、移动等操作
 */
@Controller
@RequestMapping(value = {"/RAD/"})
@MethodChinaName(cname = "OOD编辑器")
public class VFSService {
    // 服务实现...
}
```

### 4.4 核心方法实现

#### 获取文件内容方法

```java
/**
 * 获取文件内容
 * 根据路径或类名获取文件内容，支持版本文件、类文件和普通文件
 * 
 * @param path 文件路径
 * @param className 类名
 * @param projectName 工程名称
 * @return 文件内容结果
 */
@MethodChinaName(cname = "获取文件内容")
@RequestMapping(value = {"getFileContent"}, method = {RequestMethod.GET})
public @ResponseBody
ResultModel<OODModule> getFileContent(String path, String className, String projectName) {
    ResultModel<OODModule> result = new ResultModel<OODModule>();
    OODModule oodModule = new OODModule();
    String json = "{}";
    try {

        if (path != null && path.indexOf(VFSConstants.URLVERSION) > -1) {
            // 处理版本文件
            FileVersion version = CtVfsFactory.getCtVfsService().getFileVersionByPath(path);
            json = getClient().readFileAsString(path, projectName);
            ModuleComponent moduleComponent = JSONObject.parseObject(json, ModuleComponent.class);
            JDSActionContext.getActionContext().getContext().put("className", className + "V" + version.getIndex());
            moduleComponent.setClassName(className + "V" + version.getIndex());
            json = getClient().genJSON(moduleComponent, null, true).toString();
            logger.debug("获取版本文件内容成功: {}", path);

        } else if (className != null && !className.equals("")) {
            // 处理类文件
            if (className.endsWith(".cls")) {
                className = className.substring(0, className.length() - ".cls".length());
                JDSActionContext.getActionContext().getContext().put("className", className);
            }
            EUModule module = getClient().getModule(path, projectName, true);
            if (module == null) {
                module = getClient().createModule(className, projectName);
                getClient().saveModule(module, false);
                logger.info("创建新模块: {}", className);
            }
            json = getClient().genJSON(module, null, true).toString();
            logger.debug("获取类文件内容成功: {}", className);
        } else if (path.endsWith(".cls")) {
            // 处理类文件（通过路径）
            EUModule module = getClient().getModule(path, projectName, true);
            if (module == null) {
                EUModule euModule = getClient().createModule(className, projectName);
                getClient().saveModule(euModule, false);
                logger.info("通过路径创建新模块: {}", path);
            }
            json = getClient().genJSON(module, null, true).toString();
            logger.debug("通过路径获取类文件内容成功: {}", path);
        } else {
            // 处理普通文件
            json = getClient().readFileAsString(path, projectName);
            logger.debug("获取普通文件内容成功: {}", path);
        }
        oodModule.setContent(json);
        result.setData(oodModule);
    } catch (Exception e) {
        logger.error("获取文件内容失败: {}", e.getMessage(), e);
        result = new ErrorResultModel();
        ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
        ((ErrorResultModel) result).setErrdes(e.getMessage());
    }
    return result;
}
```

#### 保存文件内容方法

```java
/**
 * 保存文件内容
 * 根据文件类型保存不同类型的文件内容
 * 
 * @param projectName 工程名称
 * @param className 类名
 * @param content 文件内容
 * @param jscontent JS文件内容
 * @param path 文件路径
 * @param fileType 文件类型
 * @return 保存结果
 */
@MethodChinaName(cname = "保存文件内容")
@RequestMapping(value = {"saveContent"}, method = {RequestMethod.POST})
public @ResponseBody
ResultModel<OODModule> saveContent(String projectName, String className, String content, String jscontent, String path, EUFileType fileType) {
    ResultModel<OODModule> result = new ResultModel<OODModule>();
    OODModule oodModule = new OODModule();
    List<CustomModuleBean> moduleBeans = new ArrayList<>();
    try {
        ProjectVersion version = getClient().getProjectVersionByName(projectName);
        // 根据路径自动识别文件类型
        if (path != null && path.endsWith(".cls")) {
            fileType = EUFileType.EUClass;
        }
        
        switch (fileType) {
            case EUClass:
                // 保存类文件
                if (path.endsWith(".js") && jscontent != null) {
                    this.getClient().saveFile(new StringBuffer(jscontent), path, projectName);
                    logger.debug("保存JS文件成功: {}", path);
                }
                if (className != null && !className.equals("")) {
                    EUModule euModule = version.getModule(className);
                    if (euModule == null) {
                        euModule = version.createModule(path);
                        logger.info("创建新模块并保存: {}", path);
                    }
                    ModuleComponent omoduleComponent = euModule.getComponent();
                    ModuleComponent moduleComponent = JSONObject.parseObject(content, ModuleComponent.class);
                    // 保留原有公式
                    if (omoduleComponent != null && omoduleComponent.getFormulas() != null) {
                        moduleComponent.setFormulas(omoduleComponent.getFormulas());
                    }
                    euModule.setComponent(moduleComponent);
                    euModule.update(true);
                    logger.debug("保存类文件成功: {}", className);
                }
                break;
            case EUFile:
                // 保存普通文件
                this.getClient().saveFile(new StringBuffer(jscontent), path, projectName);
                logger.debug("保存普通文件成功: {}", path);
                break;
        }
        result.setData(oodModule);
    } catch (Exception e) {
        logger.error("保存文件内容失败: {}", e.getMessage(), e);
        result = new ErrorResultModel();
        ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
        ((ErrorResultModel) result).setErrdes(e.getMessage());
    }
    return result;
}
```

## 5. 服务间协作

ooder-agent-rad 的核心服务之间存在密切的协作关系，形成了一个完整的服务生态系统：

### 5.1 服务调用关系

```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│   RADEditor     │──────▶ ProjectManager  │──────▶   VFSService    │
└─────────────────┘      └─────────────────┘      └─────────────────┘
        ▲                        ▲                        ▲
        │                        │                        │
        └────────────────────────┴────────────────────────┘
                                 │
                                 ▼
                         ┌─────────────────┐
                         │    前端视图     │
                         └─────────────────┘
```

### 5.2 协作示例

1. **项目导出流程**：
   - 前端调用 `RADEditor.exportProject()` 方法
   - RADEditor 服务调用 `ProjectManager.getProjectInfo()` 获取项目信息
   - RADEditor 服务调用 `VFSService.getFileContent()` 获取文件内容
   - RADEditor 服务生成导出代码并保存到指定路径

2. **打开项目流程**：
   - 前端调用 `RADEditor.openProject()` 方法
   - RADEditor 服务调用 `ProjectManager.getProjectInfo()` 获取项目信息
   - RADEditor 服务调用 `VFSService.openFolder()` 获取项目文件列表
   - RADEditor 服务返回项目配置和文件列表给前端

## 6. 服务扩展机制

ooder-agent-rad 的服务设计支持扩展，可以通过以下方式扩展服务：

### 6.1 自定义服务开发

1. **创建服务类**：创建一个新的服务类，使用 `@Controller` 和 `@RequestMapping` 注解标记
2. **实现服务方法**：实现服务的核心方法，使用 `@ResponseBody` 注解标记返回结果
3. **注册服务**：将服务类放在指定的包下，Spring Boot 会自动扫描并注册

### 6.2 服务拦截器

可以通过实现 Spring Boot 的拦截器接口，对服务请求进行拦截和处理，实现日志记录、权限验证等功能。

### 6.3 服务事件

使用 Spring Boot 的事件机制，可以在服务方法执行前后触发事件，实现服务间的解耦通信。

## 7. 服务性能优化

### 7.1 缓存策略

- **数据缓存**：对频繁访问的数据进行缓存，减少数据库访问
- **结果缓存**：对计算结果进行缓存，减少重复计算
- **缓存失效策略**：实现合理的缓存失效策略，确保数据一致性

### 7.2 异步处理

- **异步方法**：使用 `@Async` 注解标记异步方法，提高并发处理能力
- **异步事件**：使用异步事件处理耗时操作，提高响应速度

### 7.3 数据库优化

- **查询优化**：优化数据库查询语句，使用索引
- **批量操作**：使用批量操作减少数据库连接次数
- **事务优化**：合理使用事务，避免长事务

## 8. 服务监控与日志

ooder-agent-rad 使用 SLF4J 日志框架，对服务的运行状态和错误信息进行记录。每个服务都配置了独立的日志记录器，可以通过日志级别控制日志输出。

### 8.1 日志配置

```java
/**
 * 日志记录器
 */
private static final Logger logger = LoggerFactory.getLogger(RADEditor.class);
```

### 8.2 日志使用

```java
// 记录信息日志
logger.info("开始导出项目: {}", projectName);

// 记录调试日志
logger.debug("获取文件内容成功: {}", path);

// 记录错误日志
logger.error("导出项目失败: {}", e.getMessage(), e);
```

## 9. 最佳实践

### 9.1 服务设计最佳实践

- **单一职责**：每个服务只负责一个功能领域
- **接口设计**：设计清晰的接口，参数和返回值类型明确
- **异常处理**：合理处理异常，返回友好的错误信息
- **日志记录**：记录关键操作和错误信息，便于调试和监控
- **性能优化**：考虑性能因素，优化服务实现

### 9.2 服务调用最佳实践

- **异步调用**：对耗时操作使用异步调用
- **批量处理**：对多个相似操作使用批量处理
- **缓存使用**：合理使用缓存，减少服务调用次数
- **错误处理**：正确处理服务调用异常，避免系统崩溃

## 10. 总结

ooder-agent-rad 的核心服务构成了系统的业务逻辑层，负责处理各种业务请求和数据操作。这些服务采用了注解驱动的设计，具有良好的可扩展性和可维护性。通过了解这些核心服务的实现和使用，可以更好地理解 ooder-agent-rad 的工作原理，便于进行二次开发和扩展。

RADEditor、ProjectManager 和 VFSService 是 ooder-agent-rad 的三个核心服务，它们之间密切协作，形成了一个完整的服务生态系统。通过这些服务，ooder-agent-rad 实现了项目设计、开发、部署的全流程支持，为开发者提供了高效、便捷的开发体验。

package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.scene.group.SceneGroupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.util.*;

@RestController
@RequestMapping("/api/v1/arch-check")
public class ArchCheckController {

    private static final Logger log = LoggerFactory.getLogger(ArchCheckController.class);

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired(required = false)
    private SceneGroupManager sceneGroupManager;
    
    @Autowired(required = false)
    private DataSource dataSource;

    @GetMapping("/controllers")
    public ResultModel<List<Map<String, Object>>> checkControllers() {
        log.info("[checkControllers] Starting architecture check");
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        RequestMappingHandlerMapping handlerMapping = applicationContext
            .getBean(RequestMappingHandlerMapping.class);
        
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        
        Map<String, List<Map<String, Object>>> controllerMethods = new LinkedHashMap<>();
        
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            Method method = handlerMethod.getMethod();
            Class<?> controllerClass = handlerMethod.getBeanType();
            
            String controllerName = controllerClass.getSimpleName();
            
            if (!controllerName.contains("Controller")) {
                continue;
            }
            
            if (controllerName.startsWith("org.springframework") || 
                controllerName.startsWith("org.springframework.boot")) {
                continue;
            }
            
            Map<String, Object> methodInfo = checkMethod(method);
            
            controllerMethods.computeIfAbsent(controllerName, k -> new ArrayList<>())
                .add(methodInfo);
        }
        
        for (Map.Entry<String, List<Map<String, Object>>> entry : controllerMethods.entrySet()) {
            String controllerName = entry.getKey();
            List<Map<String, Object>> methods = entry.getValue();
            
            boolean hasIssues = methods.stream()
                .anyMatch(m -> "fail".equals(m.get("status")));
            
            long failedCount = methods.stream()
                .filter(m -> "fail".equals(m.get("status")))
                .count();
            
            Map<String, Object> controllerResult = new LinkedHashMap<>();
            controllerResult.put("controller", controllerName);
            controllerResult.put("hasIssues", hasIssues);
            controllerResult.put("message", hasIssues ? 
                String.format("发现 %d 个问题", failedCount) : "符合规范");
            controllerResult.put("issues", hasIssues ? methods : null);
            
            results.add(controllerResult);
        }
        
        log.info("[checkControllers] Checked {} controllers", results.size());
        return ResultModel.success(results);
    }

    @GetMapping("/system")
    public ResultModel<Map<String, Object>> checkSystem() {
        log.info("[checkSystem] Starting system check");
        
        Map<String, Object> results = new LinkedHashMap<>();
        List<Map<String, Object>> checks = new ArrayList<>();
        
        checks.add(checkDatabase());
        checks.add(checkSeSdk());
        checks.add(checkSceneGroups());
        checks.add(checkControllersCount());
        
        long passCount = checks.stream().filter(c -> "pass".equals(c.get("status"))).count();
        long failCount = checks.stream().filter(c -> "fail".equals(c.get("status"))).count();
        
        results.put("checks", checks);
        results.put("total", checks.size());
        results.put("passed", passCount);
        results.put("failed", failCount);
        results.put("status", failCount == 0 ? "healthy" : "issues");
        
        return ResultModel.success(results);
    }
    
    private Map<String, Object> checkDatabase() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", "数据库连接");
        result.put("description", "检查数据库连接状态");
        
        if (dataSource == null) {
            result.put("status", "fail");
            result.put("message", "DataSource未配置");
            return result;
        }
        
        try (Connection conn = dataSource.getConnection()) {
            boolean valid = conn.isValid(5);
            if (valid) {
                result.put("status", "pass");
                result.put("message", "数据库连接正常");
            } else {
                result.put("status", "fail");
                result.put("message", "数据库连接无效");
            }
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", "数据库连接失败: " + e.getMessage());
        }
        
        return result;
    }
    
    private Map<String, Object> checkSeSdk() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", "SE SDK集成");
        result.put("description", "检查Scene Engine SDK集成状态");
        
        if (sceneGroupManager != null) {
            try {
                List<?> groups = sceneGroupManager.getAllSceneGroups();
                result.put("status", "pass");
                result.put("message", "SE SDK正常，当前场景组数: " + (groups != null ? groups.size() : 0));
                Map<String, Object> details = new LinkedHashMap<>();
                details.put("sceneGroupManager", "available");
                details.put("groupCount", groups != null ? groups.size() : 0);
                result.put("details", details);
            } catch (Exception e) {
                result.put("status", "fail");
                result.put("message", "SE SDK初始化异常: " + e.getMessage());
            }
        } else {
            result.put("status", "fail");
            result.put("message", "SceneGroupManager未注入");
        }
        
        return result;
    }
    
    private Map<String, Object> checkSceneGroups() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", "场景组状态");
        result.put("description", "检查场景组运行状态");
        
        if (sceneGroupManager != null) {
            try {
                List<?> groups = sceneGroupManager.getAllSceneGroups();
                int total = groups != null ? groups.size() : 0;
                int activeCount = 0;
                int suspendedCount = 0;
                
                result.put("status", "pass");
                result.put("message", String.format("场景组总数: %d", total));
                Map<String, Object> details = new LinkedHashMap<>();
                details.put("total", total);
                details.put("active", activeCount);
                details.put("suspended", suspendedCount);
                result.put("details", details);
            } catch (Exception e) {
                result.put("status", "fail");
                result.put("message", "获取场景组失败: " + e.getMessage());
            }
        } else {
            result.put("status", "fail");
            result.put("message", "SceneGroupManager不可用");
        }
        
        return result;
    }
    
    private Map<String, Object> checkControllersCount() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", "API端点");
        result.put("description", "检查API端点注册情况");
        
        try {
            RequestMappingHandlerMapping handlerMapping = (RequestMappingHandlerMapping) 
                applicationContext.getBean("requestMappingHandlerMapping");
            
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            
            Set<String> controllers = new HashSet<>();
            int endpointCount = 0;
            
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                HandlerMethod handlerMethod = entry.getValue();
                String controllerName = handlerMethod.getBeanType().getSimpleName();
                
                if (controllerName.contains("Controller") && 
                    !controllerName.startsWith("org.springframework")) {
                    controllers.add(controllerName);
                    endpointCount++;
                }
            }
            
            result.put("status", "pass");
            result.put("message", String.format("已注册 %d 个端点，%d 个Controller", endpointCount, controllers.size()));
            Map<String, Object> details = new LinkedHashMap<>();
            details.put("controllers", controllers.size());
            details.put("endpoints", endpointCount);
            result.put("details", details);
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", "获取端点信息失败: " + e.getMessage());
        }
        
        return result;
    }

    private Map<String, Object> checkMethod(Method method) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        String methodName = method.getName();
        result.put("method", methodName);
        
        List<String> issues = new ArrayList<>();
        
        boolean hasResponseBody = method.isAnnotationPresent(ResponseBody.class) ||
            method.getDeclaringClass().isAnnotationPresent(RestController.class);
        
        if (!hasResponseBody) {
            issues.add("缺少 @ResponseBody 或 @RestController");
        }
        
        Parameter[] parameters = method.getParameters();
        for (Parameter param : parameters) {
            String paramType = param.getType().getSimpleName();
            
            if (paramType.equals("Map") || 
                paramType.equals("HashMap") ||
                param.getType().getName().contains("Map")) {
                
                boolean hasRequestParam = param.isAnnotationPresent(RequestParam.class);
                boolean hasRequestBody = param.isAnnotationPresent(RequestBody.class);
                boolean hasPathVariable = param.isAnnotationPresent(PathVariable.class);
                
                if (!hasRequestParam && !hasRequestBody && !hasPathVariable) {
                    issues.add("参数 " + param.getName() + " 使用 Map 类型，建议使用 DTO");
                }
            }
        }
        
        if (issues.isEmpty()) {
            result.put("status", "pass");
            result.put("message", "符合规范");
        } else {
            result.put("status", "fail");
            result.put("message", String.join("; ", issues));
        }
        
        return result;
    }

    @GetMapping("/rules")
    public ResultModel<Map<String, Object>> getRules() {
        Map<String, Object> rules = new LinkedHashMap<>();
        
        List<Map<String, String>> frontendRules = Arrays.asList(
            createRule("脚本引用", "必须包含 nexus.js, menu.js, page-init.js, api.js"),
            createRule("页面结构", "必须使用 nx-page, nx-page__sidebar, nx-page__content"),
            createRule("图标系统", "仅使用 Remix Icon (ri-*)"),
            createRule("CSS变量", "使用 --ns-*, --nx-* 变量"),
            createRule("模态框", "使用 classList toggle"),
            createRule("API响应", "检查 status === 'success'")
        );
        
        List<Map<String, String>> backendRules = Arrays.asList(
            createRule("简单参数", "使用 @RequestParam"),
            createRule("复杂对象", "使用 @RequestBody DTO"),
            createRule("禁止Map参数", "Controller 方法禁止使用 Map 作为参数"),
            createRule("响应注解", "必须有 @ResponseBody 或 @RestController"),
            createRule("响应格式", "统一使用 ResultModel")
        );
        
        rules.put("frontend", frontendRules);
        rules.put("backend", backendRules);
        
        return ResultModel.success(rules);
    }

    private Map<String, String> createRule(String name, String description) {
        Map<String, String> rule = new LinkedHashMap<>();
        rule.put("name", name);
        rule.put("description", description);
        return rule;
    }
}

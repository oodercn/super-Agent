package net.ooder.mvp.skill.scene.config.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.AuditService;
import net.ooder.mvp.skill.scene.dto.audit.AuditLogDTO;
import net.ooder.mvp.skill.scene.dto.audit.AuditEventType;
import net.ooder.mvp.skill.scene.dto.audit.AuditResultType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthConfigController {

    private static final Logger log = LoggerFactory.getLogger(AuthConfigController.class);

    @Autowired(required = false)
    private AuditService auditService;

    private Map<String, Object> authConfig = new HashMap<>();

    public AuthConfigController() {
        authConfig.put("authType", "local");
        authConfig.put("sessionTimeout", 30);
        authConfig.put("maxLoginAttempts", 5);
        authConfig.put("minPasswordLength", 8);
        authConfig.put("passwordExpiry", 90);
        authConfig.put("requireUppercase", true);
        authConfig.put("requireLowercase", true);
        authConfig.put("requireNumber", true);
        authConfig.put("requireSpecial", true);
    }

    @GetMapping
    public ResultModel<Map<String, Object>> getConfig() {
        return ResultModel.success(authConfig);
    }

    @PutMapping
    public ResultModel<Map<String, Object>> saveConfig(@RequestBody Map<String, Object> config) {
        String authType = (String) config.getOrDefault("authType", "local");
        authConfig.putAll(config);
        authConfig.put("updatedAt", System.currentTimeMillis());
        
        logConfigChange("auth_config", "global", "update", "更新认证配置，认证方式: " + authType);
        
        return ResultModel.success(authConfig);
    }

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("authType", authConfig.get("authType"));
        stats.put("onlineUsers", 0);
        stats.put("todayLogins", 0);
        stats.put("sessionTimeout", authConfig.get("sessionTimeout"));
        return ResultModel.success(stats);
    }

    @GetMapping("/methods")
    public ResultModel<List<Map<String, Object>>> getMethods() {
        List<Map<String, Object>> methods = Arrays.asList(
            createMethodInfo("local", "本地认证", "使用本地用户名密码认证", true),
            createMethodInfo("ldap", "LDAP", "使用LDAP服务器认证", false),
            createMethodInfo("oauth2", "OAuth2", "使用OAuth2协议认证", false),
            createMethodInfo("saml", "SAML", "使用SAML单点登录", false)
        );
        return ResultModel.success(methods);
    }

    @PostMapping("/test")
    public ResultModel<Map<String, Object>> testAuth(@RequestBody Map<String, Object> request) {
        String authType = (String) request.getOrDefault("authType", "local");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("authType", authType);
        result.put("message", "认证配置测试成功");
        result.put("testedAt", System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    private Map<String, Object> createMethodInfo(String type, String name, String description, boolean active) {
        Map<String, Object> info = new HashMap<>();
        info.put("type", type);
        info.put("name", name);
        info.put("description", description);
        info.put("active", active);
        return info;
    }

    private void logConfigChange(String resourceType, String resourceId, String action, String detail) {
        if (auditService != null) {
            try {
                AuditLogDTO auditLog = new AuditLogDTO();
                auditLog.setEventType(AuditEventType.CONFIG_AUTH);
                auditLog.setResult(AuditResultType.SUCCESS);
                auditLog.setResourceType(resourceType);
                auditLog.setResourceId(resourceId);
                auditLog.setAction(action);
                auditLog.setDetail(detail);
                auditService.logEvent(auditLog);
            } catch (Exception e) {
                log.warn("Failed to log audit event", e);
            }
        }
    }
}

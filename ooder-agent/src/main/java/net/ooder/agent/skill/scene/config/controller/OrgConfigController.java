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
@RequestMapping("/api/v1/config/org")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrgConfigController {

    private static final Logger log = LoggerFactory.getLogger(OrgConfigController.class);

    @Autowired(required = false)
    private AuditService auditService;

    private Map<String, Object> orgConfig = new HashMap<>();

    public OrgConfigController() {
        orgConfig.put("sourceType", "local");
        orgConfig.put("defaultOrgName", "默认组织");
        orgConfig.put("maxMembers", 100);
    }

    @GetMapping
    public ResultModel<Map<String, Object>> getConfig() {
        return ResultModel.success(orgConfig);
    }

    @PutMapping
    public ResultModel<Map<String, Object>> saveConfig(@RequestBody Map<String, Object> config) {
        String sourceType = (String) config.getOrDefault("sourceType", "local");
        orgConfig.putAll(config);
        orgConfig.put("updatedAt", System.currentTimeMillis());
        
        logConfigChange("org_config", "global", "update", "更新组织配置，数据源: " + sourceType);
        
        return ResultModel.success(orgConfig);
    }

    @PostMapping("/sync")
    public ResultModel<Map<String, Object>> syncOrg(@RequestBody Map<String, Object> request) {
        String sourceType = (String) request.getOrDefault("sourceType", "local");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("syncedAt", System.currentTimeMillis());
        result.put("sourceType", sourceType);
        result.put("message", "组织数据同步成功");
        
        logConfigChange("org_sync", sourceType, "sync", "从" + getSourceName(sourceType) + "同步组织数据");
        
        return ResultModel.success(result);
    }

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("orgCount", 1);
        stats.put("memberCount", 0);
        stats.put("syncStatus", "未配置");
        stats.put("lastSync", null);
        return ResultModel.success(stats);
    }

    @GetMapping("/sources")
    public ResultModel<List<Map<String, Object>>> getSources() {
        List<Map<String, Object>> sources = Arrays.asList(
            createSourceInfo("local", "本地管理", "本地文件存储组织数据", true),
            createSourceInfo("dingding", "钉钉", "从钉钉同步组织架构", false),
            createSourceInfo("feishu", "飞书", "从飞书同步组织架构", false),
            createSourceInfo("wecom", "企业微信", "从企业微信同步组织架构", false),
            createSourceInfo("ldap", "LDAP", "从LDAP同步组织架构", false)
        );
        return ResultModel.success(sources);
    }

    private Map<String, Object> createSourceInfo(String type, String name, String description, boolean active) {
        Map<String, Object> info = new HashMap<>();
        info.put("type", type);
        info.put("name", name);
        info.put("description", description);
        info.put("active", active);
        return info;
    }

    private String getSourceName(String source) {
        Map<String, String> names = new HashMap<>();
        names.put("dingding", "钉钉");
        names.put("feishu", "飞书");
        names.put("wecom", "企业微信");
        names.put("ldap", "LDAP");
        names.put("local", "本地");
        return names.getOrDefault(source, source);
    }

    private void logConfigChange(String resourceType, String resourceId, String action, String detail) {
        if (auditService != null) {
            try {
                AuditLogDTO auditLog = new AuditLogDTO();
                auditLog.setEventType(AuditEventType.CONFIG_ORG);
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

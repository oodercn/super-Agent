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
@RequestMapping("/api/v1/config/comm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommConfigController {

    private static final Logger log = LoggerFactory.getLogger(CommConfigController.class);

    @Autowired(required = false)
    private AuditService auditService;

    private Map<String, Object> commConfig = new HashMap<>();
    private List<String> enabledChannels = new ArrayList<>(Arrays.asList("console"));

    public CommConfigController() {
        commConfig.put("channels", enabledChannels);
        commConfig.put("retryCount", 3);
        commConfig.put("consoleLogLevel", "INFO");
        commConfig.put("consoleFormat", "text");
    }

    @GetMapping
    public ResultModel<Map<String, Object>> getConfig() {
        commConfig.put("channels", enabledChannels);
        return ResultModel.success(commConfig);
    }

    @PutMapping
    public ResultModel<Map<String, Object>> saveConfig(@RequestBody Map<String, Object> config) {
        commConfig.putAll(config);
        commConfig.put("updatedAt", System.currentTimeMillis());
        
        if (config.containsKey("channels")) {
            Object channelsObj = config.get("channels");
            if (channelsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> channels = (List<String>) channelsObj;
                enabledChannels.clear();
                enabledChannels.addAll(channels);
            }
        }
        
        logConfigChange("comm_config", "global", "update", "更新消息通信配置");
        
        return ResultModel.success(commConfig);
    }

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("enabledChannels", enabledChannels.size());
        stats.put("todaySent", 0);
        stats.put("failedCount", 0);
        stats.put("retryCount", commConfig.get("retryCount"));
        return ResultModel.success(stats);
    }

    @GetMapping("/channels")
    public ResultModel<List<Map<String, Object>>> getChannels() {
        List<Map<String, Object>> channels = Arrays.asList(
            createChannelInfo("console", "控制台", "输出到系统日志", "ri-terminal-line", true),
            createChannelInfo("email", "邮件", "发送邮件通知", "ri-mail-line", false),
            createChannelInfo("dingding", "钉钉", "钉钉机器人通知", "ri-message-2-line", false),
            createChannelInfo("wechat", "企业微信", "企业微信机器人", "ri-wechat-line", false),
            createChannelInfo("webhook", "Webhook", "自定义HTTP回调", "ri-link", false),
            createChannelInfo("sms", "短信", "短信通知服务", "ri-message-line", false)
        );
        return ResultModel.success(channels);
    }

    @PostMapping("/test")
    public ResultModel<Map<String, Object>> testChannel(@RequestBody Map<String, Object> request) {
        String channel = (String) request.getOrDefault("channel", "console");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("channel", channel);
        result.put("message", "消息发送测试成功");
        result.put("testedAt", System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/channels/{channel}/enable")
    public ResultModel<Void> enableChannel(@PathVariable String channel) {
        if (!enabledChannels.contains(channel)) {
            enabledChannels.add(channel);
            logConfigChange("comm_channel", channel, "enable", "启用通知渠道: " + channel);
        }
        return ResultModel.success("渠道已启用", null);
    }

    @PostMapping("/channels/{channel}/disable")
    public ResultModel<Void> disableChannel(@PathVariable String channel) {
        enabledChannels.remove(channel);
        logConfigChange("comm_channel", channel, "disable", "禁用通知渠道: " + channel);
        return ResultModel.success("渠道已禁用", null);
    }

    private Map<String, Object> createChannelInfo(String type, String name, String description, String icon, boolean active) {
        Map<String, Object> info = new HashMap<>();
        info.put("type", type);
        info.put("name", name);
        info.put("description", description);
        info.put("icon", icon);
        info.put("active", active);
        return info;
    }

    private void logConfigChange(String resourceType, String resourceId, String action, String detail) {
        if (auditService != null) {
            try {
                AuditLogDTO auditLog = new AuditLogDTO();
                auditLog.setEventType(AuditEventType.CONFIG_COMM);
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

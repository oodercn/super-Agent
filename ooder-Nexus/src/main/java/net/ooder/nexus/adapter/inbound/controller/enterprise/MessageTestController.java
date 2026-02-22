package net.ooder.nexus.adapter.inbound.controller.enterprise;

import net.ooder.nexus.dto.enterprise.DingTalkConfigDTO;
import net.ooder.nexus.dto.enterprise.FeishuConfigDTO;
import net.ooder.nexus.dto.enterprise.WechatWorkConfigDTO;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.service.enterprise.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Message service test and configuration controller
 * Provides APIs for testing and configuring enterprise messaging services
 */
@RestController
@RequestMapping("/api/msg")
public class MessageTestController {

    private static final Logger log = LoggerFactory.getLogger(MessageTestController.class);

    @Autowired(required = false)
    private WechatWorkService wechatWorkService;

    @Autowired(required = false)
    private DingTalkService dingTalkService;

    @Autowired(required = false)
    private FeishuService feishuService;

    @Autowired(required = false)
    private MessageService messageService;

    /**
     * Get message service status
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> getServiceStatus() {
        log.info("Getting message service status");
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("wechatWork", wechatWorkService != null ? "enabled" : "disabled");
            status.put("dingTalk", dingTalkService != null ? "enabled" : "disabled");
            status.put("feishu", feishuService != null ? "enabled" : "disabled");
            status.put("jmq", messageService != null ? "enabled" : "disabled");
            return Result.success("Service status retrieved successfully", status);
        } catch (Exception e) {
            log.error("Failed to get service status", e);
            return Result.error("Failed to get service status: " + e.getMessage());
        }
    }

    /**
     * Test WeChat Work message
     */
    @PostMapping("/test/wechat")
    public Result<Boolean> testWechatMessage(@RequestBody MessageTestRequest request) {
        log.info("Testing WeChat Work message to user: {}", request.getUserId());
        try {
            if (wechatWorkService == null) {
                return Result.error("WeChat Work service is not enabled");
            }
            return wechatWorkService.sendTextMessage(request.getUserId(), request.getMessage());
        } catch (Exception e) {
            log.error("Failed to send WeChat Work message", e);
            return Result.error("Failed to send message: " + e.getMessage());
        }
    }

    /**
     * Test DingTalk message
     */
    @PostMapping("/test/dingtalk")
    public Result<Boolean> testDingTalkMessage(@RequestBody MessageTestRequest request) {
        log.info("Testing DingTalk message to user: {}", request.getUserId());
        try {
            if (dingTalkService == null) {
                return Result.error("DingTalk service is not enabled");
            }
            return dingTalkService.sendTextMessage(request.getUserId(), request.getMessage());
        } catch (Exception e) {
            log.error("Failed to send DingTalk message", e);
            return Result.error("Failed to send message: " + e.getMessage());
        }
    }

    /**
     * Test Feishu message
     */
    @PostMapping("/test/feishu")
    public Result<Boolean> testFeishuMessage(@RequestBody MessageTestRequest request) {
        log.info("Testing Feishu message to user: {}", request.getUserId());
        try {
            if (feishuService == null) {
                return Result.error("Feishu service is not enabled");
            }
            return feishuService.sendTextMessage(request.getUserId(), request.getMessage());
        } catch (Exception e) {
            log.error("Failed to send Feishu message", e);
            return Result.error("Failed to send message: " + e.getMessage());
        }
    }

    /**
     * Test JMQ message
     */
    @PostMapping("/test/jmq")
    public Result<MessageService.TopicMessage> testJmqMessage(@RequestBody JmqTestRequest request) {
        log.info("Testing JMQ message to topic: {}", request.getTopic());
        try {
            if (messageService == null) {
                return Result.error("JMQ service is not enabled");
            }
            return messageService.sendTopicMessage(request.getTopic(), request.getMessage());
        } catch (Exception e) {
            log.error("Failed to send JMQ message", e);
            return Result.error("Failed to send message: " + e.getMessage());
        }
    }

    /**
     * Get WeChat Work configuration
     */
    @GetMapping("/config/wechat")
    public Result<Map<String, String>> getWechatConfig() {
        log.info("Getting WeChat Work configuration");
        try {
            Map<String, String> config = new HashMap<>();
            config.put("corpid", "***"); // Masked for security
            config.put("agentid", "***");
            config.put("corpsecret", "***");
            return Result.success("Configuration retrieved successfully", config);
        } catch (Exception e) {
            log.error("Failed to get configuration", e);
            return Result.error("Failed to get configuration: " + e.getMessage());
        }
    }

    /**
     * Update WeChat Work configuration
     */
    @PostMapping("/config/wechat")
    public Result<Boolean> updateWechatConfig(@RequestBody WechatWorkConfigDTO config) {
        log.info("Updating WeChat Work configuration");
        try {
            // Configuration update logic here
            return Result.success("Configuration updated successfully", true);
        } catch (Exception e) {
            log.error("Failed to update configuration", e);
            return Result.error("Failed to update configuration: " + e.getMessage());
        }
    }

    /**
     * Get DingTalk configuration
     */
    @GetMapping("/config/dingtalk")
    public Result<Map<String, String>> getDingTalkConfig() {
        log.info("Getting DingTalk configuration");
        try {
            Map<String, String> config = new HashMap<>();
            config.put("appkey", "***");
            config.put("appsecret", "***");
            return Result.success("Configuration retrieved successfully", config);
        } catch (Exception e) {
            log.error("Failed to get configuration", e);
            return Result.error("Failed to get configuration: " + e.getMessage());
        }
    }

    /**
     * Update DingTalk configuration
     */
    @PostMapping("/config/dingtalk")
    public Result<Boolean> updateDingTalkConfig(@RequestBody DingTalkConfigDTO config) {
        log.info("Updating DingTalk configuration");
        try {
            // Configuration update logic here
            return Result.success("Configuration updated successfully", true);
        } catch (Exception e) {
            log.error("Failed to update configuration", e);
            return Result.error("Failed to update configuration: " + e.getMessage());
        }
    }

    /**
     * Get Feishu configuration
     */
    @GetMapping("/config/feishu")
    public Result<Map<String, String>> getFeishuConfig() {
        log.info("Getting Feishu configuration");
        try {
            Map<String, String> config = new HashMap<>();
            config.put("appId", "***");
            config.put("appSecret", "***");
            return Result.success("Configuration retrieved successfully", config);
        } catch (Exception e) {
            log.error("Failed to get configuration", e);
            return Result.error("Failed to get configuration: " + e.getMessage());
        }
    }

    /**
     * Update Feishu configuration
     */
    @PostMapping("/config/feishu")
    public Result<Boolean> updateFeishuConfig(@RequestBody FeishuConfigDTO config) {
        log.info("Updating Feishu configuration");
        try {
            // Configuration update logic here
            return Result.success("Configuration updated successfully", true);
        } catch (Exception e) {
            log.error("Failed to update configuration", e);
            return Result.error("Failed to update configuration: " + e.getMessage());
        }
    }

    /**
     * Message test request
     */
    public static class MessageTestRequest {
        private String userId;
        private String message;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * JMQ test request
     */
    public static class JmqTestRequest {
        private String topic;
        private String message;

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}

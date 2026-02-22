package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.nexus.common.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 防火墙管理控制器
 * 提供防火墙规则管理接口
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since 0.7.3
 */
@RestController
@RequestMapping("/api/firewall")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FirewallController {

    private static final Logger log = LoggerFactory.getLogger(FirewallController.class);

    private final ConcurrentHashMap<String, FirewallRule> rules = new ConcurrentHashMap<String, FirewallRule>();
    private final AtomicLong blockedCount = new AtomicLong(0);
    private final AtomicLong allowedCount = new AtomicLong(0);
    private volatile boolean firewallRunning = true;

    public FirewallController() {
        initializeDefaultRules();
    }

    private void initializeDefaultRules() {
        long now = System.currentTimeMillis();
        
        rules.put("rule-1", new FirewallRule(
            "rule-1", "input", "tcp", "0.0.0.0/0", "0.0.0.0/0", "80", "accept", true, "允许HTTP访问", now
        ));
        rules.put("rule-2", new FirewallRule(
            "rule-2", "input", "tcp", "0.0.0.0/0", "0.0.0.0/0", "443", "accept", true, "允许HTTPS访问", now
        ));
        rules.put("rule-3", new FirewallRule(
            "rule-3", "input", "tcp", "0.0.0.0/0", "0.0.0.0/0", "22", "drop", true, "阻止SSH暴力破解", now
        ));
        rules.put("rule-4", new FirewallRule(
            "rule-4", "output", "all", "0.0.0.0/0", "0.0.0.0/0", "any", "accept", true, "允许所有出站流量", now
        ));
        rules.put("rule-5", new FirewallRule(
            "rule-5", "forward", "tcp", "192.168.1.0/24", "10.0.0.0/24", "8080", "accept", false, "内部转发规则", now
        ));
        
        blockedCount.set(156);
        allowedCount.set(2847);
        
        log.info("FirewallController initialized with {} default rules", rules.size());
    }

    @GetMapping("/rules")
    public ResultModel<List<Map<String, Object>>> getRules() {
        log.info("Get firewall rules requested");
        try {
            List<Map<String, Object>> ruleList = new ArrayList<Map<String, Object>>();
            for (FirewallRule rule : rules.values()) {
                ruleList.add(rule.toMap());
            }
            return ResultModel.success("获取成功", ruleList);
        } catch (Exception e) {
            log.error("Error getting firewall rules", e);
            return ResultModel.error("获取规则列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResultModel<Map<String, Object>> getStatus() {
        log.info("Get firewall status requested");
        try {
            Map<String, Object> status = new HashMap<String, Object>();
            status.put("running", firewallRunning);
            status.put("totalRules", rules.size());
            status.put("blocked", blockedCount.get());
            status.put("allowed", allowedCount.get());
            status.put("lastUpdated", System.currentTimeMillis());
            return ResultModel.success("获取成功", status);
        } catch (Exception e) {
            log.error("Error getting firewall status", e);
            return ResultModel.error("获取状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/rules")
    public ResultModel<Map<String, Object>> addRule(@RequestBody Map<String, Object> request) {
        log.info("Add firewall rule requested: {}", request.get("type"));
        try {
            String ruleId = "rule-" + System.currentTimeMillis();
            
            String type = (String) request.get("type");
            String protocol = (String) request.get("protocol");
            String source = (String) request.get("source");
            String destination = (String) request.get("destination");
            String port = (String) request.get("port");
            String action = (String) request.get("action");
            String description = (String) request.get("description");
            
            if (type == null) type = "input";
            if (protocol == null) protocol = "all";
            if (source == null || source.isEmpty()) source = "0.0.0.0/0";
            if (destination == null || destination.isEmpty()) destination = "0.0.0.0/0";
            if (port == null || port.isEmpty()) port = "any";
            if (action == null) action = "accept";
            
            FirewallRule rule = new FirewallRule(
                ruleId, type, protocol, source, destination, port, action, true, description, System.currentTimeMillis()
            );
            
            rules.put(ruleId, rule);
            return ResultModel.success("添加成功", rule.toMap());
        } catch (Exception e) {
            log.error("Error adding firewall rule", e);
            return ResultModel.error("添加规则失败: " + e.getMessage());
        }
    }

    @PutMapping("/rules/{ruleId}")
    public ResultModel<Boolean> updateRule(@PathVariable String ruleId, @RequestBody Map<String, Object> request) {
        log.info("Update firewall rule requested: {}", ruleId);
        try {
            FirewallRule existingRule = rules.get(ruleId);
            if (existingRule == null) {
                return ResultModel.error("规则不存在", 404);
            }
            
            Boolean enabled = (Boolean) request.get("enabled");
            if (enabled != null) {
                FirewallRule updatedRule = new FirewallRule(
                    existingRule.getId(),
                    existingRule.getType(),
                    existingRule.getProtocol(),
                    existingRule.getSource(),
                    existingRule.getDestination(),
                    existingRule.getPort(),
                    existingRule.getAction(),
                    enabled,
                    existingRule.getDescription(),
                    existingRule.getCreateTime()
                );
                rules.put(ruleId, updatedRule);
            }
            
            return ResultModel.success("更新成功", true);
        } catch (Exception e) {
            log.error("Error updating firewall rule", e);
            return ResultModel.error("更新规则失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/rules/{ruleId}")
    public ResultModel<Boolean> deleteRule(@PathVariable String ruleId) {
        log.info("Delete firewall rule requested: {}", ruleId);
        try {
            FirewallRule removed = rules.remove(ruleId);
            if (removed == null) {
                return ResultModel.error("规则不存在", 404);
            }
            return ResultModel.success("删除成功", true);
        } catch (Exception e) {
            log.error("Error deleting firewall rule", e);
            return ResultModel.error("删除规则失败: " + e.getMessage());
        }
    }

    @PostMapping("/toggle")
    public ResultModel<Boolean> toggleFirewall() {
        log.info("Toggle firewall requested");
        try {
            firewallRunning = !firewallRunning;
            String message = firewallRunning ? "防火墙已启动" : "防火墙已停止";
            return ResultModel.success(message, firewallRunning);
        } catch (Exception e) {
            log.error("Error toggling firewall", e);
            return ResultModel.error("切换防火墙状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/reset-stats")
    public ResultModel<Boolean> resetStats() {
        log.info("Reset firewall stats requested");
        try {
            blockedCount.set(0);
            allowedCount.set(0);
            return ResultModel.success("统计数据已重置", true);
        } catch (Exception e) {
            log.error("Error resetting firewall stats", e);
            return ResultModel.error("重置统计数据失败: " + e.getMessage());
        }
    }

    private static class FirewallRule {
        private final String id;
        private final String type;
        private final String protocol;
        private final String source;
        private final String destination;
        private final String port;
        private final String action;
        private final boolean enabled;
        private final String description;
        private final long createTime;

        public FirewallRule(String id, String type, String protocol, String source,
                          String destination, String port, String action, boolean enabled,
                          String description, long createTime) {
            this.id = id;
            this.type = type;
            this.protocol = protocol;
            this.source = source;
            this.destination = destination;
            this.port = port;
            this.action = action;
            this.enabled = enabled;
            this.description = description;
            this.createTime = createTime;
        }

        public String getId() { return id; }
        public String getType() { return type; }
        public String getProtocol() { return protocol; }
        public String getSource() { return source; }
        public String getDestination() { return destination; }
        public String getPort() { return port; }
        public String getAction() { return action; }
        public boolean isEnabled() { return enabled; }
        public String getDescription() { return description; }
        public long getCreateTime() { return createTime; }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("type", type);
            map.put("protocol", protocol);
            map.put("source", source);
            map.put("destination", destination);
            map.put("port", port);
            map.put("action", action);
            map.put("enabled", enabled);
            map.put("description", description);
            map.put("createTime", createTime);
            return map;
        }
    }
}

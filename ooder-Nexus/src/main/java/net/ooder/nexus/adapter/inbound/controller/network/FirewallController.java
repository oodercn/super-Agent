package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/firewall")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FirewallController {

    private static final Logger log = LoggerFactory.getLogger(FirewallController.class);

    private final ConcurrentHashMap<String, FirewallRuleEntity> rules = new ConcurrentHashMap<String, FirewallRuleEntity>();
    private final AtomicLong blockedCount = new AtomicLong(0);
    private final AtomicLong allowedCount = new AtomicLong(0);
    private volatile boolean firewallRunning = true;

    public FirewallController() {
        initializeDefaultRules();
    }

    private void initializeDefaultRules() {
        long now = System.currentTimeMillis();
        
        rules.put("rule-1", new FirewallRuleEntity(
            "rule-1", "input", "tcp", "0.0.0.0/0", "0.0.0.0/0", "80", "accept", true, "允许HTTP访问", now
        ));
        rules.put("rule-2", new FirewallRuleEntity(
            "rule-2", "input", "tcp", "0.0.0.0/0", "0.0.0.0/0", "443", "accept", true, "允许HTTPS访问", now
        ));
        rules.put("rule-3", new FirewallRuleEntity(
            "rule-3", "input", "tcp", "0.0.0.0/0", "0.0.0.0/0", "22", "drop", true, "阻止SSH暴力破解", now
        ));
        rules.put("rule-4", new FirewallRuleEntity(
            "rule-4", "output", "all", "0.0.0.0/0", "0.0.0.0/0", "any", "accept", true, "允许所有出站流量", now
        ));
        rules.put("rule-5", new FirewallRuleEntity(
            "rule-5", "forward", "tcp", "192.168.1.0/24", "10.0.0.0/24", "8080", "accept", false, "内部转发规则", now
        ));
        
        blockedCount.set(156);
        allowedCount.set(2847);
        
        log.info("FirewallController initialized with {} default rules", rules.size());
    }

    @GetMapping("/rules")
    public ResultModel<List<FirewallRuleDTO>> getRules() {
        log.info("Get firewall rules requested");
        try {
            List<FirewallRuleDTO> ruleList = new ArrayList<FirewallRuleDTO>();
            for (FirewallRuleEntity rule : rules.values()) {
                ruleList.add(convertToDTO(rule));
            }
            return ResultModel.success("获取成功", ruleList);
        } catch (Exception e) {
            log.error("Error getting firewall rules", e);
            return ResultModel.error("获取规则列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResultModel<FirewallStatusDTO> getStatus() {
        log.info("Get firewall status requested");
        try {
            FirewallStatusDTO status = new FirewallStatusDTO();
            status.setRunning(firewallRunning);
            status.setTotalRules(rules.size());
            status.setBlocked(blockedCount.get());
            status.setAllowed(allowedCount.get());
            status.setLastUpdated(System.currentTimeMillis());
            return ResultModel.success("获取成功", status);
        } catch (Exception e) {
            log.error("Error getting firewall status", e);
            return ResultModel.error("获取状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/rules")
    public ResultModel<FirewallRuleDTO> addRule(@RequestBody FirewallRuleCreateDTO request) {
        log.info("Add firewall rule requested: {}", request.getType());
        try {
            String ruleId = "rule-" + System.currentTimeMillis();
            
            String type = request.getType() != null ? request.getType() : "input";
            String protocol = request.getProtocol() != null ? request.getProtocol() : "all";
            String source = (request.getSource() == null || request.getSource().isEmpty()) ? "0.0.0.0/0" : request.getSource();
            String destination = (request.getDestination() == null || request.getDestination().isEmpty()) ? "0.0.0.0/0" : request.getDestination();
            String port = (request.getPort() == null || request.getPort().isEmpty()) ? "any" : request.getPort();
            String action = request.getAction() != null ? request.getAction() : "accept";
            
            FirewallRuleEntity rule = new FirewallRuleEntity(
                ruleId, type, protocol, source, destination, port, action, true, request.getDescription(), System.currentTimeMillis()
            );
            
            rules.put(ruleId, rule);
            return ResultModel.success("添加成功", convertToDTO(rule));
        } catch (Exception e) {
            log.error("Error adding firewall rule", e);
            return ResultModel.error("添加规则失败: " + e.getMessage());
        }
    }

    @PutMapping("/rules/{ruleId}")
    public ResultModel<Boolean> updateRule(@PathVariable String ruleId, @RequestBody FirewallRuleUpdateDTO request) {
        log.info("Update firewall rule requested: {}", ruleId);
        try {
            FirewallRuleEntity existingRule = rules.get(ruleId);
            if (existingRule == null) {
                return ResultModel.error("规则不存在", 404);
            }
            
            Boolean enabled = request.getEnabled();
            if (enabled != null) {
                FirewallRuleEntity updatedRule = new FirewallRuleEntity(
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
            FirewallRuleEntity removed = rules.remove(ruleId);
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

    @PostMapping("/rules/toggle")
    public ResultModel<Boolean> toggleRule(@RequestParam String id) {
        log.info("Toggle rule requested: id={}", id);
        try {
            FirewallRuleEntity rule = rules.get(id);
            if (rule == null) {
                return ResultModel.error("规则不存在: " + id);
            }
            return ResultModel.success("规则状态已切换", true);
        } catch (Exception e) {
            log.error("Error toggling rule", e);
            return ResultModel.error("切换规则状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/rules/delete")
    public ResultModel<Boolean> deleteRuleByName(@RequestParam String id) {
        log.info("Delete rule by name requested: id={}", id);
        try {
            FirewallRuleEntity removed = rules.remove(id);
            if (removed == null) {
                return ResultModel.error("规则不存在: " + id);
            }
            return ResultModel.success("规则已删除", true);
        } catch (Exception e) {
            log.error("Error deleting rule", e);
            return ResultModel.error("删除规则失败: " + e.getMessage());
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

    private FirewallRuleDTO convertToDTO(FirewallRuleEntity entity) {
        FirewallRuleDTO dto = new FirewallRuleDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setProtocol(entity.getProtocol());
        dto.setSource(entity.getSource());
        dto.setDestination(entity.getDestination());
        dto.setPort(entity.getPort());
        dto.setAction(entity.getAction());
        dto.setEnabled(entity.isEnabled());
        dto.setDescription(entity.getDescription());
        dto.setCreateTime(entity.getCreateTime());
        return dto;
    }

    private static class FirewallRuleEntity {
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

        public FirewallRuleEntity(String id, String type, String protocol, String source,
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
    }
}

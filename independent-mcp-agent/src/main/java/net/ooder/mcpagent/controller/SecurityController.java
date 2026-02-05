package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);
    
    // 安全状态存储
    private final Map<String, SecurityStatus> securityStatus = new ConcurrentHashMap<>();
    
    // 访问控制列表
    private final List<AccessControlRule> accessControlRules = new ArrayList<>();
    
    // 安全事件日志
    private final List<SecurityEvent> securityEvents = new ArrayList<>();
    
    // 初始化默认安全状态
    public SecurityController() {
        initializeDefaultSecurityStatus();
        initializeDefaultAccessRules();
    }
    
    private void initializeDefaultSecurityStatus() {
        // 添加默认安全状态
        securityStatus.put("overall", new SecurityStatus(
            "overall", "整体安全状态", "normal", "系统安全状态正常", 
            95, System.currentTimeMillis()
        ));
        
        securityStatus.put("doors", new SecurityStatus(
            "doors", "门窗状态", "normal", "所有门窗已关闭", 
            100, System.currentTimeMillis()
        ));
        
        securityStatus.put("motion", new SecurityStatus(
            "motion", " motion", "normal", "未检测到异常动作", 
            90, System.currentTimeMillis()
        ));
        
        securityStatus.put("camera", new SecurityStatus(
            "camera", "监控状态", "warning", "部分摄像头离线", 
            75, System.currentTimeMillis()
        ));
        
        securityStatus.put("network", new SecurityStatus(
            "network", "网络安全", "normal", "网络连接安全", 
            98, System.currentTimeMillis()
        ));
    }
    
    private void initializeDefaultAccessRules() {
        // 添加默认访问控制规则
        accessControlRules.add(new AccessControlRule(
            "rule-1", "管理员访问", "admin", "all", true, 
            "允许管理员访问所有资源", "2024-01-01", null
        ));
        
        accessControlRules.add(new AccessControlRule(
            "rule-2", "用户访问", "user", "limited", true, 
            "允许普通用户访问有限资源", "2024-01-01", null
        ));
        
        accessControlRules.add(new AccessControlRule(
            "rule-3", "访客访问", "guest", "restricted", true, 
            "允许访客访问受限资源", "2024-01-01", null
        ));
        
        accessControlRules.add(new AccessControlRule(
            "rule-4", "黑名单IP", "ip", "blocked", false, 
            "阻止特定IP地址访问", "2024-01-01", null
        ));
    }
    
    /**
     * 获取安全状态
     */
    @GetMapping("/status")
    public Map<String, Object> getSecurityStatus(
            @RequestParam(required = false) String type) {
        log.info("Get security status requested: type={}", type);
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> statusData = new HashMap<>();
            
            if (type != null) {
                // 获取特定类型的安全状态
                SecurityStatus status = securityStatus.get(type);
                if (status == null) {
                    response.put("status", "error");
                    response.put("message", "Security status type not found");
                    response.put("code", "SECURITY_STATUS_NOT_FOUND");
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
                }
                statusData.put("status", status.toMap());
            } else {
                // 获取所有安全状态
                Map<String, Object> allStatus = new HashMap<>();
                for (Map.Entry<String, SecurityStatus> entry : securityStatus.entrySet()) {
                    allStatus.put(entry.getKey(), entry.getValue().toMap());
                }
                statusData.put("statuses", allStatus);
                
                // 计算整体安全评分
                double averageScore = securityStatus.values().stream()
                        .mapToInt(SecurityStatus::getScore)
                        .average()
                        .orElse(0);
                statusData.put("averageScore", Math.round(averageScore));
                
                // 安全状态统计
                long normalCount = securityStatus.values().stream()
                        .filter(s -> "normal".equals(s.getStatus())).count();
                long warningCount = securityStatus.values().stream()
                        .filter(s -> "warning".equals(s.getStatus())).count();
                long criticalCount = securityStatus.values().stream()
                        .filter(s -> "critical".equals(s.getStatus())).count();
                
                Map<String, Object> stats = new ConcurrentHashMap<>();
                stats.put("total", securityStatus.size());
                stats.put("normal", normalCount);
                stats.put("warning", warningCount);
                stats.put("critical", criticalCount);
                statusData.put("stats", stats);
            }
            
            response.put("status", "success");
            response.put("message", "Security status retrieved successfully");
            response.put("data", statusData);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting security status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SECURITY_STATUS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 更新安全状态
     */
    @PutMapping("/status/{statusId}")
    public Map<String, Object> updateSecurityStatus(
            @PathVariable String statusId, @RequestBody Map<String, Object> statusData) {
        log.info("Update security status requested: {}, data: {}", statusId, statusData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            SecurityStatus status = securityStatus.get(statusId);
            if (status == null) {
                response.put("status", "error");
                response.put("message", "Security status not found");
                response.put("code", "SECURITY_STATUS_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 更新状态
            if (statusData.containsKey("status")) {
                status.setStatus((String) statusData.get("status"));
            }
            if (statusData.containsKey("message")) {
                status.setMessage((String) statusData.get("message"));
            }
            if (statusData.containsKey("score")) {
                status.setScore(Integer.parseInt(statusData.get("score").toString()));
            }
            
            status.setLastUpdated(System.currentTimeMillis());
            
            // 记录安全事件
            addSecurityEvent("status_update", "更新安全状态", 
                "success", "Updated security status: " + statusId);
            
            response.put("status", "success");
            response.put("message", "Security status updated successfully");
            response.put("data", status.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error updating security status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SECURITY_STATUS_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取访问控制规则
     */
    @GetMapping("/access/rules")
    public Map<String, Object> getAccessControlRules(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean enabled) {
        log.info("Get access control rules requested: type={}, enabled={}", type, enabled);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<AccessControlRule> filteredRules = accessControlRules.stream()
                    .filter(rule -> (type == null || rule.getType().equals(type)))
                    .filter(rule -> (enabled == null || rule.isEnabled() == enabled))
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> ruleList = new ArrayList<>();
            for (AccessControlRule rule : filteredRules) {
                ruleList.add(rule.toMap());
            }
            
            // 规则统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", accessControlRules.size());
            stats.put("enabled", accessControlRules.stream().filter(AccessControlRule::isEnabled).count());
            stats.put("disabled", accessControlRules.stream().filter(r -> !r.isEnabled()).count());
            stats.put("types", accessControlRules.stream().map(AccessControlRule::getType).distinct().count());
            
            response.put("status", "success");
            response.put("message", "Access control rules retrieved successfully");
            response.put("data", ruleList);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting access control rules: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "ACCESS_RULES_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加访问控制规则
     */
    @PostMapping("/access/rules")
    public Map<String, Object> addAccessControlRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Add access control rule requested: {}", ruleData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            String id = "rule-" + (accessControlRules.size() + 1);
            String name = (String) ruleData.getOrDefault("name", "New Access Rule");
            String type = (String) ruleData.getOrDefault("type", "user");
            String permission = (String) ruleData.getOrDefault("permission", "limited");
            boolean enabled = ruleData.containsKey("enabled") ? 
                Boolean.parseBoolean(ruleData.get("enabled").toString()) : true;
            String description = (String) ruleData.getOrDefault("description", "");
            String startDate = (String) ruleData.getOrDefault("startDate", "2024-01-01");
            String endDate = (String) ruleData.getOrDefault("endDate", null);
            
            AccessControlRule rule = new AccessControlRule(
                id, name, type, permission, enabled, description, startDate, endDate
            );
            
            accessControlRules.add(rule);
            
            // 记录安全事件
            addSecurityEvent("access_rule_add", "添加访问控制规则", 
                "success", "Added access control rule: " + name);
            
            response.put("status", "success");
            response.put("message", "Access control rule added successfully");
            response.put("data", rule.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error adding access control rule: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "ACCESS_RULE_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 更新访问控制规则
     */
    @PutMapping("/access/rules/{ruleId}")
    public Map<String, Object> updateAccessControlRule(
            @PathVariable String ruleId, @RequestBody Map<String, Object> ruleData) {
        log.info("Update access control rule requested: {}, data: {}", ruleId, ruleData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            AccessControlRule rule = accessControlRules.stream()
                    .filter(r -> r.getId().equals(ruleId))
                    .findFirst()
                    .orElse(null);
            
            if (rule == null) {
                response.put("status", "error");
                response.put("message", "Access control rule not found");
                response.put("code", "ACCESS_RULE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 更新规则
            if (ruleData.containsKey("name")) {
                rule.setName((String) ruleData.get("name"));
            }
            if (ruleData.containsKey("type")) {
                rule.setType((String) ruleData.get("type"));
            }
            if (ruleData.containsKey("permission")) {
                rule.setPermission((String) ruleData.get("permission"));
            }
            if (ruleData.containsKey("enabled")) {
                rule.setEnabled(Boolean.parseBoolean(ruleData.get("enabled").toString()));
            }
            if (ruleData.containsKey("description")) {
                rule.setDescription((String) ruleData.get("description"));
            }
            if (ruleData.containsKey("startDate")) {
                rule.setStartDate((String) ruleData.get("startDate"));
            }
            if (ruleData.containsKey("endDate")) {
                rule.setEndDate((String) ruleData.get("endDate"));
            }
            
            // 记录安全事件
            addSecurityEvent("access_rule_update", "更新访问控制规则", 
                "success", "Updated access control rule: " + ruleId);
            
            response.put("status", "success");
            response.put("message", "Access control rule updated successfully");
            response.put("data", rule.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error updating access control rule: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "ACCESS_RULE_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 删除访问控制规则
     */
    @DeleteMapping("/access/rules/{ruleId}")
    public Map<String, Object> deleteAccessControlRule(@PathVariable String ruleId) {
        log.info("Delete access control rule requested: {}", ruleId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            AccessControlRule rule = accessControlRules.stream()
                    .filter(r -> r.getId().equals(ruleId))
                    .findFirst()
                    .orElse(null);
            
            if (rule == null) {
                response.put("status", "error");
                response.put("message", "Access control rule not found");
                response.put("code", "ACCESS_RULE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            accessControlRules.remove(rule);
            
            // 记录安全事件
            addSecurityEvent("access_rule_delete", "删除访问控制规则", 
                "success", "Deleted access control rule: " + ruleId);
            
            response.put("status", "success");
            response.put("message", "Access control rule deleted successfully");
            response.put("ruleId", ruleId);
            response.put("ruleName", rule.getName());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error deleting access control rule: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "ACCESS_RULE_DELETE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取安全事件日志
     */
    @GetMapping("/events")
    public Map<String, Object> getSecurityEvents(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        log.info("Get security events requested: limit={}, type={}, status={}", limit, type, status);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<SecurityEvent> filteredEvents = securityEvents.stream()
                    .filter(event -> (type == null || event.getType().equals(type)))
                    .filter(event -> (status == null || event.getStatus().equals(status)))
                    .sorted((e1, e2) -> Long.compare(e2.getTimestamp(), e1.getTimestamp()))
                    .limit(limit)
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> eventList = new ArrayList<>();
            for (SecurityEvent event : filteredEvents) {
                eventList.add(event.toMap());
            }
            
            // 事件统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", securityEvents.size());
            stats.put("success", securityEvents.stream().filter(e -> "success".equals(e.getStatus())).count());
            stats.put("warning", securityEvents.stream().filter(e -> "warning".equals(e.getStatus())).count());
            stats.put("error", securityEvents.stream().filter(e -> "error".equals(e.getStatus())).count());
            
            response.put("status", "success");
            response.put("message", "Security events retrieved successfully");
            response.put("data", eventList);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting security events: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SECURITY_EVENTS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加安全事件
     */
    private void addSecurityEvent(String type, String title, String status, String message) {
        SecurityEvent event = new SecurityEvent(
            "event-" + (securityEvents.size() + 1),
            type, title, status, message,
            System.currentTimeMillis()
        );
        securityEvents.add(event);
        
        // 限制事件数量
        if (securityEvents.size() > 1000) {
            securityEvents.remove(0);
        }
    }
    
    // 安全状态类
    private static class SecurityStatus {
        private final String id;
        private final String name;
        private String status;
        private String message;
        private int score;
        private long lastUpdated;
        
        public SecurityStatus(String id, String name, String status, String message, int score, long lastUpdated) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.message = message;
            this.score = score;
            this.lastUpdated = lastUpdated;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("status", status);
            map.put("message", message);
            map.put("score", score);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
    }
    
    // 访问控制规则类
    private static class AccessControlRule {
        private final String id;
        private String name;
        private String type;
        private String permission;
        private boolean enabled;
        private String description;
        private String startDate;
        private String endDate;
        private final long createdAt;
        private long lastUpdated;
        
        public AccessControlRule(String id, String name, String type, String permission, boolean enabled,
                               String description, String startDate, String endDate) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.permission = permission;
            this.enabled = enabled;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.createdAt = System.currentTimeMillis();
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("type", type);
            map.put("permission", permission);
            map.put("enabled", enabled);
            map.put("description", description);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("createdAt", createdAt);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; this.lastUpdated = System.currentTimeMillis(); }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; this.lastUpdated = System.currentTimeMillis(); }
        public String getPermission() { return permission; }
        public void setPermission(String permission) { this.permission = permission; this.lastUpdated = System.currentTimeMillis(); }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; this.lastUpdated = System.currentTimeMillis(); }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; this.lastUpdated = System.currentTimeMillis(); }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; this.lastUpdated = System.currentTimeMillis(); }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; this.lastUpdated = System.currentTimeMillis(); }
    }
    
    // 安全事件类
    private static class SecurityEvent {
        private final String id;
        private final String type;
        private final String title;
        private final String status;
        private final String message;
        private final long timestamp;
        
        public SecurityEvent(String id, String type, String title, String status, String message, long timestamp) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("type", type);
            map.put("title", title);
            map.put("status", status);
            map.put("message", message);
            map.put("timestamp", timestamp);
            return map;
        }
        
        public String getType() { return type; }
        public String getStatus() { return status; }
        public long getTimestamp() { return timestamp; }
    }
}

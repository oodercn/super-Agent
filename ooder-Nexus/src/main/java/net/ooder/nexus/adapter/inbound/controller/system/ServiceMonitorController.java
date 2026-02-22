package net.ooder.nexus.adapter.inbound.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/system/services")
public class ServiceMonitorController {

    private static final Logger log = LoggerFactory.getLogger(ServiceMonitorController.class);

    private final long startTime = System.currentTimeMillis();

    @GetMapping("/overview")
    public Map<String, Object> getServicesOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> services = getCoreServices();
            
            int total = services.size();
            int running = 0;
            int stopped = 0;
            int warning = 0;
            
            for (Map<String, Object> s : services) {
                String status = (String) s.get("status");
                if ("RUNNING".equals(status)) {
                    running++;
                } else if ("STOPPED".equals(status)) {
                    stopped++;
                } else if ("WARNING".equals(status)) {
                    warning++;
                }
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("running", running);
            data.put("stopped", stopped);
            data.put("warning", warning);
            data.put("services", services);
            
            result.put("requestStatus", 200);
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to get services overview", e);
            result.put("requestStatus", 500);
            result.put("message", "获取服务概览失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/skills")
    public Map<String, Object> getSkillServices() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> skills = getSkillServiceStatuses();
            
            int total = skills.size();
            int connected = 0;
            int disconnected = 0;
            int notConfigured = 0;
            
            for (Map<String, Object> s : skills) {
                String status = (String) s.get("status");
                if ("CONNECTED".equals(status)) {
                    connected++;
                } else if ("DISCONNECTED".equals(status) || "CONNECTION_FAILED".equals(status)) {
                    disconnected++;
                } else if ("NOT_CONFIGURED".equals(status) || "PENDING_CONFIG".equals(status)) {
                    notConfigured++;
                }
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("connected", connected);
            data.put("disconnected", disconnected);
            data.put("notConfigured", notConfigured);
            data.put("skills", skills);
            
            result.put("requestStatus", 200);
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to get skill services", e);
            result.put("requestStatus", 500);
            result.put("message", "获取Skill服务状态失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{serviceId}/check")
    public Map<String, Object> checkService(@PathVariable String serviceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> status = checkServiceStatus(serviceId);
            if (status != null) {
                result.put("requestStatus", 200);
                result.put("data", status);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "服务不存在");
            }
        } catch (Exception e) {
            log.error("Failed to check service", e);
            result.put("requestStatus", 500);
            result.put("message", "检查服务失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/{serviceId}/history")
    public Map<String, Object> getServiceHistory(
            @PathVariable String serviceId,
            @RequestParam(defaultValue = "24h") String period) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> history = generateMockHistory(period);
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("avgResponseTime", 6.5);
            statistics.put("maxResponseTime", 25);
            statistics.put("minResponseTime", 3);
            statistics.put("uptime", 99.9);
            
            Map<String, Object> data = new HashMap<>();
            data.put("serviceId", serviceId);
            data.put("period", period);
            data.put("dataPoints", history);
            data.put("statistics", statistics);
            
            result.put("requestStatus", 200);
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to get service history", e);
            result.put("requestStatus", 500);
            result.put("message", "获取历史数据失败: " + e.getMessage());
        }
        return result;
    }

    private List<Map<String, Object>> getCoreServices() {
        List<Map<String, Object>> services = new ArrayList<>();
        
        Map<String, Object> nexusCore = new HashMap<>();
        nexusCore.put("serviceId", "nexus-core");
        nexusCore.put("name", "Nexus Core Service");
        nexusCore.put("type", "core");
        nexusCore.put("port", 9876);
        nexusCore.put("status", "RUNNING");
        nexusCore.put("uptime", System.currentTimeMillis() - startTime);
        nexusCore.put("responseTime", 5L);
        nexusCore.put("lastChecked", new Date());
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("cpu", 15.5);
        metrics.put("memory", 256 * 1024 * 1024);
        metrics.put("connections", 10);
        nexusCore.put("metrics", metrics);
        services.add(nexusCore);
        
        Map<String, Object> sceneEngine = new HashMap<>();
        sceneEngine.put("serviceId", "scene-engine");
        sceneEngine.put("name", "Scene Engine");
        sceneEngine.put("type", "engine");
        sceneEngine.put("port", 8081);
        sceneEngine.put("status", "RUNNING");
        sceneEngine.put("uptime", System.currentTimeMillis() - startTime - 10000);
        sceneEngine.put("responseTime", 10L);
        sceneEngine.put("lastChecked", new Date());
        services.add(sceneEngine);
        
        Map<String, Object> msgQueue = new HashMap<>();
        msgQueue.put("serviceId", "message-queue");
        msgQueue.put("name", "Message Queue");
        msgQueue.put("type", "messaging");
        msgQueue.put("port", 5672);
        msgQueue.put("status", "STOPPED");
        msgQueue.put("uptime", 0L);
        msgQueue.put("lastChecked", new Date());
        services.add(msgQueue);
        
        return services;
    }

    private List<Map<String, Object>> getSkillServiceStatuses() {
        List<Map<String, Object>> skills = new ArrayList<>();
        
        Map<String, Object> dingding = new HashMap<>();
        dingding.put("skillId", "skill-org-dingding");
        dingding.put("name", "钉钉组织服务");
        dingding.put("type", "org");
        dingding.put("status", "CONNECTED");
        dingding.put("endpoint", "https://oapi.dingtalk.com");
        dingding.put("responseTime", 150L);
        dingding.put("lastChecked", new Date());
        skills.add(dingding);
        
        Map<String, Object> feishu = new HashMap<>();
        feishu.put("skillId", "skill-org-feishu");
        feishu.put("name", "飞书组织服务");
        feishu.put("type", "org");
        feishu.put("status", "DISCONNECTED");
        feishu.put("endpoint", "https://open.feishu.cn");
        feishu.put("responseTime", 0L);
        feishu.put("lastChecked", new Date());
        skills.add(feishu);
        
        Map<String, Object> qiwei = new HashMap<>();
        qiwei.put("skillId", "skill-org-qiwei");
        qiwei.put("name", "企业微信");
        qiwei.put("type", "org");
        qiwei.put("status", "PENDING_CONFIG");
        qiwei.put("lastChecked", new Date());
        skills.add(qiwei);
        
        Map<String, Object> dbMysql = new HashMap<>();
        dbMysql.put("skillId", "skill-db-mysql");
        dbMysql.put("name", "MySQL数据库");
        dbMysql.put("type", "database");
        dbMysql.put("status", "CONNECTED");
        dbMysql.put("endpoint", "jdbc:mysql://localhost:3306/nexus");
        dbMysql.put("responseTime", 5L);
        dbMysql.put("lastChecked", new Date());
        skills.add(dbMysql);
        
        return skills;
    }

    private Map<String, Object> checkServiceStatus(String serviceId) {
        for (Map<String, Object> s : getCoreServices()) {
            if (serviceId.equals(s.get("serviceId"))) {
                s.put("lastChecked", new Date());
                return s;
            }
        }
        return null;
    }

    private List<Map<String, Object>> generateMockHistory(String period) {
        List<Map<String, Object>> history = new ArrayList<>();
        Random random = new Random();
        
        int points = "24h".equals(period) ? 24 : "7d".equals(period) ? 168 : 6;
        long interval = "24h".equals(period) ? 3600000 : "7d".equals(period) ? 3600000 : 600000;
        for (int i = 0; i < points; i++) {
            Map<String, Object> point = new HashMap<>();
            point.put("timestamp", new Date(System.currentTimeMillis() - (points - i) * interval));
            point.put("status", "RUNNING");
            point.put("responseTime", 3 + random.nextInt(20));
            history.add(point);
        }
        
        return history;
    }
}

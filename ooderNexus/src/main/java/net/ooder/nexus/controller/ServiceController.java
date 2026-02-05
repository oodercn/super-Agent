package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private static final Logger log = LoggerFactory.getLogger(ServiceController.class);
    
    // 服务状态存储
    private final Map<String, ServiceInfo> services = new HashMap<>();
    
    // 操作日志队列
    private final ConcurrentLinkedQueue<OperationLog> operationLogs = new ConcurrentLinkedQueue<>();
    
    // 初始化服务数据
    public ServiceController() {
        initializeServices();
    }
    
    private void initializeServices() {
        // MCP Agent 服务
        services.put("mcp", new ServiceInfo(
            "mcp", "MCP Agent", "core", "running", "0.6.5",
            3600, 15.2, 256, 1024,
            "核心MCP Agent服务，负责网络管理和命令处理"
        ));
        
        // Skill Center 服务
        services.put("skillcenter", new ServiceInfo(
            "skillcenter", "Skill Center", "service", "running", "0.6.5",
            3500, 8.7, 192, 512,
            "技能中心服务，管理和协调各种技能模块"
        ));
        
        // Skill Flow 服务
        services.put("skillflow", new ServiceInfo(
            "skillflow", "Skill Flow", "workflow", "warning", "0.6.5",
            3200, 22.5, 384, 768,
            "技能工作流服务，处理复杂的技能执行流程"
        ));
        
        // VFS 服务
        services.put("vfs", new ServiceInfo(
            "vfs", "Virtual File System", "storage", "stopped", "0.6.5",
            0, 0, 0, 2048,
            "虚拟文件系统服务，管理文件存储和访问"
        ));
    }
    
    /**
     * 获取所有服务状态
     */
    @GetMapping("/status")
    public Map<String, Object> getAllServices() {
        log.info("Get all services status requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> serviceList = new ArrayList<>();
            for (ServiceInfo service : services.values()) {
                serviceList.add(service.toMap());
            }
            
            // 计算服务状态统计
            long runningCount = services.values().stream().filter(s -> "running".equals(s.getStatus())).count();
            long stoppedCount = services.values().stream().filter(s -> "stopped".equals(s.getStatus())).count();
            long warningCount = services.values().stream().filter(s -> "warning".equals(s.getStatus())).count();
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("total", services.size());
            summary.put("running", runningCount);
            summary.put("stopped", stoppedCount);
            summary.put("warning", warningCount);
            
            response.put("status", "success");
            response.put("message", "Services status retrieved successfully");
            response.put("summary", summary);
            response.put("data", serviceList);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting services status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SERVICES_STATUS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取单个服务详细信息
     */
    @GetMapping("/status/{serviceId}")
    public Map<String, Object> getServiceStatus(@PathVariable String serviceId) {
        log.info("Get service status requested: {}", serviceId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            ServiceInfo service = services.get(serviceId);
            if (service == null) {
                response.put("status", "error");
                response.put("message", "Service not found");
                response.put("code", "SERVICE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            response.put("status", "success");
            response.put("message", "Service status retrieved successfully");
            response.put("data", service.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting service status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SERVICE_STATUS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 启动服务
     */
    @PostMapping("/start/{serviceId}")
    public Map<String, Object> startService(@PathVariable String serviceId) {
        log.info("Start service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "start");
    }
    
    /**
     * 停止服务
     */
    @PostMapping("/stop/{serviceId}")
    public Map<String, Object> stopService(@PathVariable String serviceId) {
        log.info("Stop service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "stop");
    }
    
    /**
     * 重启服务
     */
    @PostMapping("/restart/{serviceId}")
    public Map<String, Object> restartService(@PathVariable String serviceId) {
        log.info("Restart service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "restart");
    }
    
    /**
     * 检查服务
     */
    @PostMapping("/check/{serviceId}")
    public Map<String, Object> checkService(@PathVariable String serviceId) {
        log.info("Check service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "check");
    }
    
    /**
     * 处理服务操作
     */
    private Map<String, Object> handleServiceOperation(String serviceId, String operation) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            ServiceInfo service = services.get(serviceId);
            if (service == null) {
                response.put("status", "error");
                response.put("message", "Service not found");
                response.put("code", "SERVICE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            String result = "success";
            String message = "";
            
            switch (operation) {
                case "start":
                    if (service.getStatus().equals("running")) {
                        result = "error";
                        message = "Service is already running";
                    } else {
                        service.setStatus("running");
                        service.setUptime(0);
                        service.setCpu(5.0);
                        service.setMemory(128);
                        message = "Service started successfully";
                    }
                    break;
                case "stop":
                    if (service.getStatus().equals("stopped")) {
                        result = "error";
                        message = "Service is already stopped";
                    } else {
                        service.setStatus("stopped");
                        service.setUptime(0);
                        service.setCpu(0);
                        service.setMemory(0);
                        message = "Service stopped successfully";
                    }
                    break;
                case "restart":
                    service.setStatus("running");
                    service.setUptime(0);
                    service.setCpu(5.0);
                    service.setMemory(128);
                    message = "Service restarted successfully";
                    break;
                case "check":
                    // 模拟健康检查
                    if (service.getStatus().equals("running")) {
                        service.setLastHeartbeat(System.currentTimeMillis());
                        message = "Service health check passed";
                    } else {
                        result = "error";
                        message = "Service is not running";
                    }
                    break;
            }
            
            // 记录操作日志
            addOperationLog(serviceId, operation, result, message);
            
            response.put("status", result);
            response.put("message", message);
            response.put("serviceId", serviceId);
            response.put("serviceName", service.getName());
            response.put("serviceStatus", service.getStatus());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error handling service operation: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SERVICE_OPERATION_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取操作日志
     */
    @GetMapping("/logs")
    public Map<String, Object> getOperationLogs(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String serviceId) {
        log.info("Get operation logs requested: limit={}, serviceId={}", limit, serviceId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> logs = new ArrayList<>();
            
            for (OperationLog log : operationLogs) {
                if (serviceId != null && !log.getServiceId().equals(serviceId)) {
                    continue;
                }
                logs.add(log.toMap());
                if (logs.size() >= limit) {
                    break;
                }
            }
            
            response.put("status", "success");
            response.put("message", "Operation logs retrieved successfully");
            response.put("data", logs);
            response.put("count", logs.size());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting operation logs: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOGS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加操作日志
     */
    private void addOperationLog(String serviceId, String operation, String status, String message) {
        OperationLog log = new OperationLog(
            serviceId,
            services.get(serviceId) != null ? services.get(serviceId).getName() : serviceId,
            operation,
            status,
            message
        );
        operationLogs.offer(log);
        
        // 限制日志数量
        if (operationLogs.size() > 1000) {
            operationLogs.poll();
        }
    }
    
    // 服务信息类
    private static class ServiceInfo {
        private final String id;
        private final String name;
        private final String type;
        private String status;
        private final String version;
        private long uptime;
        private double cpu;
        private int memory;
        private final int disk;
        private final String description;
        private long lastHeartbeat;
        
        public ServiceInfo(String id, String name, String type, String status, String version,
                         long uptime, double cpu, int memory, int disk, String description) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.version = version;
            this.uptime = uptime;
            this.cpu = cpu;
            this.memory = memory;
            this.disk = disk;
            this.description = description;
            this.lastHeartbeat = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("type", type);
            map.put("status", status);
            map.put("version", version);
            map.put("uptime", uptime);
            map.put("cpu", cpu);
            map.put("memory", memory);
            map.put("disk", disk);
            map.put("description", description);
            map.put("lastHeartbeat", lastHeartbeat);
            
            // 网络统计信息
            Map<String, Object> network = new HashMap<>();
            network.put("packetsSent", status.equals("running") ? 1000 + (long)(Math.random() * 1000) : 0);
            network.put("packetsReceived", status.equals("running") ? 800 + (long)(Math.random() * 800) : 0);
            network.put("bytesSent", status.equals("running") ? 1024000 + (long)(Math.random() * 1024000) : 0);
            network.put("bytesReceived", status.equals("running") ? 896000 + (long)(Math.random() * 896000) : 0);
            map.put("network", network);
            
            // 端点信息
            List<String> endpoints = new ArrayList<>();
            if (status.equals("running")) {
                switch (id) {
                    case "mcp":
                        endpoints.add("http://localhost:8091/api/mcp");
                        endpoints.add("udp://localhost:9876");
                        break;
                    case "skillcenter":
                        endpoints.add("http://localhost:8091/api/skills");
                        break;
                    case "skillflow":
                        endpoints.add("http://localhost:8091/api/workflow");
                        break;
                    case "vfs":
                        endpoints.add("http://localhost:8091/api/vfs");
                        break;
                }
            }
            map.put("endpoints", endpoints);
            
            return map;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getVersion() { return version; }
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
        public double getCpu() { return cpu; }
        public void setCpu(double cpu) { this.cpu = cpu; }
        public int getMemory() { return memory; }
        public void setMemory(int memory) { this.memory = memory; }
        public int getDisk() { return disk; }
        public String getDescription() { return description; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    }
    
    // 操作日志类
    private static class OperationLog {
        private final String serviceId;
        private final String serviceName;
        private final String operation;
        private final String status;
        private final String message;
        private final long timestamp;
        
        public OperationLog(String serviceId, String serviceName, String operation, String status, String message) {
            this.serviceId = serviceId;
            this.serviceName = serviceName;
            this.operation = operation;
            this.status = status;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("serviceId", serviceId);
            map.put("serviceName", serviceName);
            map.put("operation", operation);
            map.put("status", status);
            map.put("message", message);
            map.put("timestamp", timestamp);
            return map;
        }
        
        public String getServiceId() { return serviceId; }
    }
}
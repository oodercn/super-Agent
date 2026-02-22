package net.ooder.nexus.adapter.inbound.controller.service;

import net.ooder.nexus.common.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 服务管理控制器
 * 处理服务状态监控、启停控制、操作日志等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class ServiceController {

    private static final Logger log = LoggerFactory.getLogger(ServiceController.class);

    private final Map<String, ServiceInfo> services = new HashMap<>();
    private final ConcurrentLinkedQueue<OperationLog> operationLogs = new ConcurrentLinkedQueue<>();

    public ServiceController() {
        initializeServices();
    }

    private void initializeServices() {
        services.put("mcp", new ServiceInfo(
            "mcp", "MCP Agent", "core", "running", "0.6.5",
            3600, 15.2, 256, 1024,
            "核心MCP Agent服务，负责网络管理和命令处理"
        ));

        services.put("skillcenter", new ServiceInfo(
            "skillcenter", "Skill Center", "service", "running", "0.6.5",
            3500, 8.7, 192, 512,
            "技能中心服务，管理和协调各种技能模块"
        ));

        services.put("skillflow", new ServiceInfo(
            "skillflow", "Skill Flow", "workflow", "warning", "0.6.5",
            3200, 22.5, 384, 768,
            "技能工作流服务，处理复杂的技能执行流程"
        ));

        services.put("vfs", new ServiceInfo(
            "vfs", "Virtual File System", "storage", "stopped", "0.6.5",
            0, 0, 0, 2048,
            "虚拟文件系统服务，管理文件存储和访问"
        ));
    }

    @PostMapping("/list")
    @ResponseBody
    public ResultModel<Map<String, Object>> getAllServices() {
        log.info("Get all services status requested");

        try {
            List<Map<String, Object>> serviceList = new ArrayList<>();
            for (ServiceInfo service : services.values()) {
                serviceList.add(service.toMap());
            }

            long runningCount = services.values().stream().filter(s -> "running".equals(s.getStatus())).count();
            long stoppedCount = services.values().stream().filter(s -> "stopped".equals(s.getStatus())).count();
            long warningCount = services.values().stream().filter(s -> "warning".equals(s.getStatus())).count();

            Map<String, Object> summary = new HashMap<>();
            summary.put("total", services.size());
            summary.put("running", runningCount);
            summary.put("stopped", stoppedCount);
            summary.put("warning", warningCount);

            Map<String, Object> data = new HashMap<>();
            data.put("summary", summary);
            data.put("services", serviceList);

            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error getting services status: {}", e.getMessage(), e);
            return ResultModel.error("获取服务状态失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/detail")
    @ResponseBody
    public ResultModel<Map<String, Object>> getServiceStatus(@RequestBody Map<String, String> request) {
        String serviceId = request.get("serviceId");
        log.info("Get service status requested: {}", serviceId);

        try {
            ServiceInfo service = services.get(serviceId);
            if (service == null) {
                return ResultModel.error("服务不存在", 404);
            }

            return ResultModel.success(service.toMap());
        } catch (Exception e) {
            log.error("Error getting service status: {}", e.getMessage(), e);
            return ResultModel.error("获取服务状态失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/start")
    @ResponseBody
    public ResultModel<Map<String, Object>> startService(@RequestBody Map<String, String> request) {
        String serviceId = request.get("serviceId");
        log.info("Start service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "start");
    }

    @PostMapping("/stop")
    @ResponseBody
    public ResultModel<Map<String, Object>> stopService(@RequestBody Map<String, String> request) {
        String serviceId = request.get("serviceId");
        log.info("Stop service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "stop");
    }

    @PostMapping("/restart")
    @ResponseBody
    public ResultModel<Map<String, Object>> restartService(@RequestBody Map<String, String> request) {
        String serviceId = request.get("serviceId");
        log.info("Restart service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "restart");
    }

    @PostMapping("/check")
    @ResponseBody
    public ResultModel<Map<String, Object>> checkService(@RequestBody Map<String, String> request) {
        String serviceId = request.get("serviceId");
        log.info("Check service requested: {}", serviceId);
        return handleServiceOperation(serviceId, "check");
    }

    private ResultModel<Map<String, Object>> handleServiceOperation(String serviceId, String operation) {
        try {
            ServiceInfo service = services.get(serviceId);
            if (service == null) {
                return ResultModel.error("服务不存在", 404);
            }

            String result = "success";
            String message = "";

            switch (operation) {
                case "start":
                    if (service.getStatus().equals("running")) {
                        result = "error";
                        message = "服务已在运行中";
                    } else {
                        service.setStatus("running");
                        service.setUptime(0);
                        service.setCpu(5.0);
                        service.setMemory(128);
                        message = "服务启动成功";
                    }
                    break;
                case "stop":
                    if (service.getStatus().equals("stopped")) {
                        result = "error";
                        message = "服务已停止";
                    } else {
                        service.setStatus("stopped");
                        service.setUptime(0);
                        service.setCpu(0);
                        service.setMemory(0);
                        message = "服务停止成功";
                    }
                    break;
                case "restart":
                    service.setStatus("running");
                    service.setUptime(0);
                    service.setCpu(5.0);
                    service.setMemory(128);
                    message = "服务重启成功";
                    break;
                case "check":
                    if (service.getStatus().equals("running")) {
                        service.setLastHeartbeat(System.currentTimeMillis());
                        message = "服务健康检查通过";
                    } else {
                        result = "error";
                        message = "服务未运行";
                    }
                    break;
            }

            addOperationLog(serviceId, operation, result, message);

            Map<String, Object> data = new HashMap<>();
            data.put("serviceId", serviceId);
            data.put("serviceName", service.getName());
            data.put("serviceStatus", service.getStatus());
            data.put("operationResult", result);
            data.put("message", message);

            if ("error".equals(result)) {
                return ResultModel.error(message, 400);
            }
            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error handling service operation: {}", e.getMessage(), e);
            return ResultModel.error("服务操作失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/logs")
    @ResponseBody
    public ResultModel<Map<String, Object>> getOperationLogs(@RequestBody Map<String, Object> request) {
        int limit = request.get("limit") != null ? (Integer) request.get("limit") : 50;
        String serviceId = (String) request.get("serviceId");

        log.info("Get operation logs requested: limit={}, serviceId={}", limit, serviceId);

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

            Map<String, Object> data = new HashMap<>();
            data.put("logs", logs);
            data.put("count", logs.size());

            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error getting operation logs: {}", e.getMessage(), e);
            return ResultModel.error("获取操作日志失败: " + e.getMessage(), 500);
        }
    }

    private void addOperationLog(String serviceId, String operation, String status, String message) {
        OperationLog log = new OperationLog(
            serviceId,
            services.get(serviceId) != null ? services.get(serviceId).getName() : serviceId,
            operation,
            status,
            message
        );
        operationLogs.offer(log);

        if (operationLogs.size() > 1000) {
            operationLogs.poll();
        }
    }

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

            Map<String, Object> network = new HashMap<>();
            network.put("packetsSent", status.equals("running") ? 1000 + (long)(Math.random() * 1000) : 0);
            network.put("packetsReceived", status.equals("running") ? 800 + (long)(Math.random() * 800) : 0);
            network.put("bytesSent", status.equals("running") ? 1024000 + (long)(Math.random() * 1024000) : 0);
            network.put("bytesReceived", status.equals("running") ? 896000 + (long)(Math.random() * 896000) : 0);
            map.put("network", network);

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

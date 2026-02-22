package net.ooder.nexus.adapter.inbound.controller.system;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.domain.system.model.SystemInfo;
import net.ooder.nexus.domain.system.model.ServiceStatus;
import net.ooder.nexus.domain.system.model.ResourceUsage;
import net.ooder.nexus.domain.system.model.SystemHealthData;
import net.ooder.nexus.domain.system.model.SystemLoadData;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.*;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SystemStatusController {

    private static final Logger log = LoggerFactory.getLogger(SystemStatusController.class);

    private final long startTime = System.currentTimeMillis();

    @Autowired
    private NexusServiceFactory serviceFactory;

    public SystemStatusController() {
        log.info("SystemStatusController initialized");
    }

    private INexusService getService() {
        return serviceFactory.getService();
    }

    @PostMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSystemStatus() {
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> status = new LinkedHashMap<String, Object>();
            
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            
            double cpuUsage = osBean.getSystemLoadAverage();
            if (cpuUsage < 0) cpuUsage = 0;
            
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            double memoryUsage = (double) usedMemory / maxMemory * 100;
            
            long uptime = System.currentTimeMillis() - startTime;
            String uptimeStr = formatUptime(uptime);
            
            status.put("cpuUsage", Math.round(cpuUsage * 100.0) / 100.0);
            status.put("memoryUsage", Math.round(memoryUsage * 100.0) / 100.0);
            status.put("diskUsage", getDiskUsage());
            status.put("uptime", uptimeStr);
            status.put("uptimeMillis", uptime);
            status.put("networkIn", 0L);
            status.put("networkOut", 0L);
            
            List<Map<String, Object>> services = new ArrayList<Map<String, Object>>();
            services.add(createService("Nexus Core", "running", "Active"));
            services.add(createService("H2 Database", "running", "Connected"));
            services.add(createService("Protocol Hub", "running", "MCP, ROUTE, END active"));
            services.add(createService("Skill Manager", "running", "SDK 0.7.0"));
            status.put("services", services);
            
            List<Map<String, Object>> events = new ArrayList<Map<String, Object>>();
            events.add(createEvent("info", "System started", new Date(startTime)));
            events.add(createEvent("success", "Database connected", new Date(startTime + 1000)));
            events.add(createEvent("info", "Protocol adapters initialized", new Date(startTime + 2000)));
            status.put("events", events);
            
            status.put("javaVersion", System.getProperty("java.version"));
            status.put("javaVendor", System.getProperty("java.vendor"));
            status.put("osName", System.getProperty("os.name"));
            status.put("osVersion", System.getProperty("os.version"));
            status.put("availableProcessors", osBean.getAvailableProcessors());
            
            result.setData(status);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting system status", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/info")
    @ResponseBody
    public ResultModel<SystemInfo> getSystemInfo() {
        ResultModel<SystemInfo> result = new ResultModel<SystemInfo>();
        try {
            SystemInfo info = getService().getSystemInfo().getData();
            result.setData(info);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting system info", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/health")
    @ResponseBody
    public ResultModel<SystemHealthData> getSystemHealth() {
        ResultModel<SystemHealthData> result = new ResultModel<SystemHealthData>();
        try {
            SystemHealthData health = getService().getSystemHealth().getData();
            result.setData(health);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting system health", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/services/list")
    @ResponseBody
    public ListResultModel<List<ServiceStatus>> getServiceStatuses() {
        ListResultModel<List<ServiceStatus>> result = new ListResultModel<List<ServiceStatus>>();
        try {
            List<ServiceStatus> services = getService().getServiceStatuses().getData();
            result.setData(services);
            result.setSize(services != null ? services.size() : 0);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting service statuses", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/resources")
    @ResponseBody
    public ResultModel<ResourceUsage> getResourceUsage() {
        ResultModel<ResourceUsage> result = new ResultModel<ResourceUsage>();
        try {
            ResourceUsage usage = getService().getResourceUsage().getData();
            result.setData(usage);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting resource usage", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/load")
    @ResponseBody
    public ResultModel<SystemLoadData> getSystemLoad() {
        ResultModel<SystemLoadData> result = new ResultModel<SystemLoadData>();
        try {
            SystemLoadData load = getService().getSystemLoad().getData();
            result.setData(load);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting system load", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> createService(String name, String status, String message) {
        Map<String, Object> service = new LinkedHashMap<String, Object>();
        service.put("name", name);
        service.put("status", status);
        service.put("message", message);
        service.put("lastCheck", new Date());
        return service;
    }

    private Map<String, Object> createEvent(String type, String message, Date time) {
        Map<String, Object> event = new LinkedHashMap<String, Object>();
        event.put("type", type);
        event.put("message", message);
        event.put("time", time);
        return event;
    }

    private String formatUptime(long uptimeMillis) {
        long seconds = uptimeMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours % 24 > 0) {
            sb.append(hours % 24).append("h ");
        }
        if (minutes % 60 > 0) {
            sb.append(minutes % 60).append("m ");
        }
        if (sb.length() == 0) {
            sb.append(seconds % 60).append("s");
        }
        return sb.toString().trim();
    }

    private double getDiskUsage() {
        java.io.File root = new java.io.File("/");
        if (!root.exists()) {
            root = new java.io.File(".");
        }
        long total = root.getTotalSpace();
        long free = root.getFreeSpace();
        if (total > 0) {
            return Math.round((double) (total - free) / total * 1000.0) / 10.0;
        }
        return 0.0;
    }
}

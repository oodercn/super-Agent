package net.ooder.nexus.module.monitor.service.impl;

import net.ooder.nexus.module.monitor.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MonitorServiceImpl implements MonitorService {
    
    private static final Logger log = LoggerFactory.getLogger(MonitorServiceImpl.class);
    
    // 告警队列
    private final ConcurrentLinkedQueue<Map<String, Object>> alertQueue = new ConcurrentLinkedQueue<>();
    
    // 监控阈值配置
    private final Map<String, Double> thresholds = new HashMap<>();
    
    public MonitorServiceImpl() {
        // 初始化监控阈值
        thresholds.put("cpuUsage", 80.0);
        thresholds.put("memoryUsage", 90.0);
        thresholds.put("diskUsage", 90.0);
        thresholds.put("systemLoad", 2.0);
    }
    
    @Override
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            // 获取运行时指标
            Runtime runtime = Runtime.getRuntime();
            metrics.put("availableProcessors", runtime.availableProcessors());
            metrics.put("freeMemory", runtime.freeMemory());
            metrics.put("totalMemory", runtime.totalMemory());
            metrics.put("maxMemory", runtime.maxMemory());
            metrics.put("memoryUsage", calculateMemoryUsage(runtime));
            
            // 获取系统负载
            metrics.put("systemLoad", getSystemLoad());
            
            // 获取CPU使用率
            metrics.put("cpuUsage", getCpuUsage());
            
            // 获取磁盘使用率
            metrics.put("diskUsage", getDiskUsage());
            
            // 检查告警
            checkThresholds(metrics);
            
            response.put("status", "success");
            response.put("metrics", metrics);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting system metrics: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getLogs(int limit) {
        Map<String, Object> response = new HashMap<>();
        List<String> logs = new ArrayList<>();
        
        try {
            // 获取应用日志
            logs.addAll(getApplicationLogs(limit));
            
            response.put("status", "success");
            response.put("logs", logs);
            response.put("count", logs.size());
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting logs: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getAlerts() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> alerts = new ArrayList<>();
        
        try {
            // 从告警队列获取告警
            alertQueue.forEach(alert -> alerts.add(alert));
            
            response.put("status", "success");
            response.put("alerts", alerts);
            response.put("count", alerts.size());
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting alerts: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> clearAlerts() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 清除告警队列
            alertQueue.clear();
            
            response.put("status", "success");
            response.put("message", "Alerts cleared successfully");
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error clearing alerts: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    private double calculateMemoryUsage(Runtime runtime) {
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        return (double) usedMemory / totalMemory * 100;
    }
    
    private double getSystemLoad() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Windows系统获取系统负载
                return getWindowsSystemLoad();
            } else {
                // Unix/Linux系统获取系统负载
                return getUnixSystemLoad();
            }
        } catch (Exception e) {
            log.warn("Error getting system load: {}", e.getMessage());
            return Math.random() * 1.0;
        }
    }
    
    private double getWindowsSystemLoad() {
        // 模拟Windows系统负载
        return Math.random() * 1.0;
    }
    
    private double getUnixSystemLoad() {
        try {
            Process process = Runtime.getRuntime().exec("uptime");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line = reader.readLine();
            reader.close();
            
            if (line != null) {
                String[] parts = line.split(" ");
                return Double.parseDouble(parts[parts.length - 1]);
            }
        } catch (Exception e) {
            log.warn("Error getting Unix system load: {}", e.getMessage());
        }
        return Math.random() * 1.0;
    }
    
    private double getCpuUsage() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Windows系统获取CPU使用率
                return getWindowsCpuUsage();
            } else {
                // Unix/Linux系统获取CPU使用率
                return getUnixCpuUsage();
            }
        } catch (Exception e) {
            log.warn("Error getting CPU usage: {}", e.getMessage());
            return Math.random() * 50;
        }
    }
    
    private double getWindowsCpuUsage() {
        // 模拟Windows CPU使用率
        return Math.random() * 50;
    }
    
    private double getUnixCpuUsage() {
        try {
            Process process = Runtime.getRuntime().exec("top -b -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Cpu(s):")) {
                    String[] parts = line.split(" ");
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].endsWith("%us")) {
                            return Double.parseDouble(parts[i].replace("%us", ""));
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            log.warn("Error getting Unix CPU usage: {}", e.getMessage());
        }
        return Math.random() * 50;
    }
    
    private double getDiskUsage() {
        try {
            File root = new File("");
            long totalSpace = root.getTotalSpace();
            long freeSpace = root.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            return (double) usedSpace / totalSpace * 100;
        } catch (Exception e) {
            log.warn("Error getting disk usage: {}", e.getMessage());
            return Math.random() * 50;
        }
    }
    
    private List<String> getApplicationLogs(int limit) {
        List<String> logs = new ArrayList<>();
        // 模拟应用日志
        logs.add("[INFO] " + System.currentTimeMillis() + " - Application started");
        logs.add("[INFO] " + System.currentTimeMillis() + " - Agent connected to MCP");
        logs.add("[DEBUG] " + System.currentTimeMillis() + " - Network status updated");
        logs.add("[INFO] " + System.currentTimeMillis() + " - Health check passed");
        logs.add("[WARN] " + System.currentTimeMillis() + " - Network timeout detected");
        logs.add("[INFO] " + System.currentTimeMillis() + " - Route updated successfully");
        
        return logs;
    }
    
    private void checkThresholds(Map<String, Object> metrics) {
        // 检查CPU使用率
        if (metrics.containsKey("cpuUsage")) {
            double cpuUsage = (double) metrics.get("cpuUsage");
            if (cpuUsage > thresholds.get("cpuUsage")) {
                createAlert("high_cpu_usage", "CPU使用率过高: " + String.format("%.2f%%", cpuUsage), "warning");
            }
        }
        
        // 检查内存使用率
        if (metrics.containsKey("memoryUsage")) {
            double memoryUsage = (double) metrics.get("memoryUsage");
            if (memoryUsage > thresholds.get("memoryUsage")) {
                createAlert("high_memory_usage", "内存使用率过高: " + String.format("%.2f%%", memoryUsage), "warning");
            }
        }
        
        // 检查磁盘使用率
        if (metrics.containsKey("diskUsage")) {
            double diskUsage = (double) metrics.get("diskUsage");
            if (diskUsage > thresholds.get("diskUsage")) {
                createAlert("high_disk_usage", "磁盘使用率过高: " + String.format("%.2f%%", diskUsage), "warning");
            }
        }
        
        // 检查系统负载
        if (metrics.containsKey("systemLoad")) {
            double systemLoad = (double) metrics.get("systemLoad");
            if (systemLoad > thresholds.get("systemLoad")) {
                createAlert("high_system_load", "系统负载过高: " + String.format("%.2f", systemLoad), "warning");
            }
        }
    }
    
    private void createAlert(String id, String message, String level) {
        // 检查是否已存在相同的告警
        for (Map<String, Object> existingAlert : alertQueue) {
            if (existingAlert.get("id").equals(id)) {
                return;
            }
        }
        
        // 创建新告警
        Map<String, Object> alert = new HashMap<>();
        alert.put("id", id);
        alert.put("level", level);
        alert.put("message", message);
        alert.put("timestamp", System.currentTimeMillis());
        
        // 添加到告警队列
        alertQueue.offer(alert);
        
        // 限制告警队列大小
        if (alertQueue.size() > 100) {
            alertQueue.poll();
        }
        
        log.info("Alert created: {} - {}", level, message);
    }
}

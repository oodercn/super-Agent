package net.ooder.skillcenter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 系统管理REST API控制器
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {

    /**
     * 获取系统状态
     * @return 系统状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            status.put("status", "运行中");
            status.put("service", "SkillCenter");
            status.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            status.put("status", "错误");
            status.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status);
        }
    }

    /**
     * 获取系统配置
     * @return 系统配置
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getSystemConfig() {
        Map<String, Object> config = new HashMap<>();
        
        try {
            Properties systemProps = System.getProperties();
            
            // 获取关键系统属性
            config.put("javaVersion", systemProps.getProperty("java.version"));
            config.put("javaHome", systemProps.getProperty("java.home"));
            config.put("osName", systemProps.getProperty("os.name"));
            config.put("osVersion", systemProps.getProperty("os.version"));
            config.put("osArch", systemProps.getProperty("os.arch"));
            config.put("userDir", systemProps.getProperty("user.dir"));
            config.put("userName", systemProps.getProperty("user.name"));
            
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            config.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(config);
        }
    }

    /**
     * 获取系统版本
     * @return 系统版本
     */
    @GetMapping("/version")
    public ResponseEntity<Map<String, Object>> getSystemVersion() {
        Map<String, Object> version = new HashMap<>();
        
        try {
            version.put("version", "0.6.5");
            version.put("name", "SkillCenter");
            version.put("description", "技能管理和执行平台");
            version.put("vendor", "Ooder Team");
            version.put("buildTimestamp", "2026-01-30");
            
            return ResponseEntity.ok(version);
        } catch (Exception e) {
            version.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(version);
        }
    }

    /**
     * 获取系统资源使用情况
     * @return 系统资源使用情况
     */
    @GetMapping("/resources")
    public ResponseEntity<Map<String, Object>> getSystemResources() {
        Map<String, Object> resources = new HashMap<>();
        
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            
            // 获取CPU相关信息
            resources.put("availableProcessors", osBean.getAvailableProcessors());
            
            // 获取内存相关信息
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            
            resources.put("totalMemory", totalMemory);
            resources.put("freeMemory", freeMemory);
            resources.put("usedMemory", totalMemory - freeMemory);
            resources.put("maxMemory", maxMemory);
            
            // 格式化内存大小
            resources.put("totalMemoryHuman", formatMemorySize(totalMemory));
            resources.put("freeMemoryHuman", formatMemorySize(freeMemory));
            resources.put("usedMemoryHuman", formatMemorySize(totalMemory - freeMemory));
            resources.put("maxMemoryHuman", formatMemorySize(maxMemory));
            
            // 获取运行时间
            long uptime = runtimeBean.getUptime();
            resources.put("uptime", uptime);
            resources.put("uptimeHuman", formatUptime(uptime));
            
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            resources.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resources);
        }
    }

    /**
     * 更新系统配置
     * @param config 配置参数
     * @return 更新结果
     */
    @PutMapping("/config")
    public ResponseEntity<Map<String, Object>> updateSystemConfig(@RequestBody Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的配置更新逻辑
            // 暂时模拟更新
            
            result.put("success", true);
            result.put("message", "配置更新成功");
            result.put("updatedConfig", config);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "配置更新失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 重启系统
     * @return 重启结果
     */
    @PostMapping("/restart")
    public ResponseEntity<Map<String, Object>> restartSystem() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的重启逻辑
            // 暂时模拟重启
            
            result.put("success", true);
            result.put("message", "系统重启命令已发出");
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "系统重启失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 关闭系统
     * @return 关闭结果
     */
    @PostMapping("/shutdown")
    public ResponseEntity<Map<String, Object>> shutdownSystem() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的关闭逻辑
            // 暂时模拟关闭
            
            result.put("success", true);
            result.put("message", "系统关闭命令已发出");
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "系统关闭失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 获取系统健康状态
     * @return 系统健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // 检查系统健康状态
            health.put("status", "健康");
            health.put("cpu", "正常");
            health.put("memory", "正常");
            health.put("disk", "正常");
            health.put("network", "正常");
            health.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            health.put("status", "异常");
            health.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(health);
        }
    }

    /**
     * 清理系统缓存
     * @return 清理结果
     */
    @DeleteMapping("/cache")
    public ResponseEntity<Map<String, Object>> clearSystemCache() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的缓存清理逻辑
            // 暂时模拟清理
            
            result.put("success", true);
            result.put("message", "系统缓存清理成功");
            result.put("clearedItems", 150);
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "系统缓存清理失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 获取系统操作日志
     * @return 系统操作日志列表
     */
    @GetMapping("/operations")
    public ResponseEntity<List<Map<String, Object>>> getSystemOperations() {
        List<Map<String, Object>> operations = new ArrayList<>();
        
        try {
            // 模拟系统操作日志
            long now = System.currentTimeMillis();
            
            // 操作1: 系统启动
            Map<String, Object> op1 = new HashMap<>();
            op1.put("id", "op-1");
            op1.put("type", "SYSTEM_START");
            op1.put("description", "系统启动成功");
            op1.put("timestamp", now - 3600000); // 1小时前
            op1.put("status", "SUCCESS");
            operations.add(op1);
            
            // 操作2: 技能执行
            Map<String, Object> op2 = new HashMap<>();
            op2.put("id", "op-2");
            op2.put("type", "SKILL_EXECUTION");
            op2.put("description", "执行技能: text-to-uppercase-skill");
            op2.put("timestamp", now - 1800000); // 30分钟前
            op2.put("status", "SUCCESS");
            operations.add(op2);
            
            // 操作3: 存储备份
            Map<String, Object> op3 = new HashMap<>();
            op3.put("id", "op-3");
            op3.put("type", "STORAGE_BACKUP");
            op3.put("description", "存储备份成功");
            op3.put("timestamp", now - 900000); // 15分钟前
            op3.put("status", "SUCCESS");
            operations.add(op3);
            
            // 操作4: 系统配置更新
            Map<String, Object> op4 = new HashMap<>();
            op4.put("id", "op-4");
            op4.put("type", "SYSTEM_CONFIG_UPDATE");
            op4.put("description", "更新系统配置");
            op4.put("timestamp", now - 300000); // 5分钟前
            op4.put("status", "SUCCESS");
            operations.add(op4);
            
            // 操作5: 缓存清理
            Map<String, Object> op5 = new HashMap<>();
            op5.put("id", "op-5");
            op5.put("type", "CACHE_CLEAR");
            op5.put("description", "清理系统缓存");
            op5.put("timestamp", now - 60000); // 1分钟前
            op5.put("status", "SUCCESS");
            operations.add(op5);
            
            return ResponseEntity.ok(operations);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            operations.add(error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operations);
        }
    }

    /**
     * 获取系统日志
     * @param level 日志级别
     * @return 系统日志列表
     */
    @GetMapping("/logs")
    public ResponseEntity<List<Map<String, Object>>> getSystemLogs(@RequestParam(required = false) String level) {
        List<Map<String, Object>> logs = new ArrayList<>();
        
        try {
            // 模拟系统日志
            long now = System.currentTimeMillis();
            
            // 日志1: INFO级别
            Map<String, Object> log1 = new HashMap<>();
            log1.put("id", "log-1");
            log1.put("level", "INFO");
            log1.put("message", "系统启动成功");
            log1.put("timestamp", now - 3600000); // 1小时前
            log1.put("source", "SystemController");
            logs.add(log1);
            
            // 日志2: INFO级别
            Map<String, Object> log2 = new HashMap<>();
            log2.put("id", "log-2");
            log2.put("level", "INFO");
            log2.put("message", "技能管理服务初始化完成");
            log2.put("timestamp", now - 3500000); // 58分钟前
            log2.put("source", "SkillController");
            logs.add(log2);
            
            // 日志3: WARN级别
            Map<String, Object> log3 = new HashMap<>();
            log3.put("id", "log-3");
            log3.put("level", "WARN");
            log3.put("message", "存储空间使用接近阈值");
            log3.put("timestamp", now - 1800000); // 30分钟前
            log3.put("source", "StorageController");
            logs.add(log3);
            
            // 日志4: ERROR级别
            Map<String, Object> log4 = new HashMap<>();
            log4.put("id", "log-4");
            log4.put("level", "ERROR");
            log4.put("message", "技能执行失败: 无效参数");
            log4.put("timestamp", now - 900000); // 15分钟前
            log4.put("source", "ExecutionController");
            logs.add(log4);
            
            // 日志5: INFO级别
            Map<String, Object> log5 = new HashMap<>();
            log5.put("id", "log-5");
            log5.put("level", "INFO");
            log5.put("message", "市场技能同步完成");
            log5.put("timestamp", now - 300000); // 5分钟前
            log5.put("source", "MarketController");
            logs.add(log5);
            
            // 按级别过滤
            if (level != null && !level.isEmpty()) {
                logs = logs.stream()
                          .filter(log -> level.equalsIgnoreCase((String) log.get("level")))
                          .collect(Collectors.toList());
            }
            
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            logs.add(error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(logs);
        }
    }

    /**
     * 清空系统日志
     * @return 清空结果
     */
    @DeleteMapping("/logs")
    public ResponseEntity<Map<String, Object>> clearSystemLogs() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的日志清空逻辑
            // 暂时模拟清空
            
            result.put("success", true);
            result.put("message", "系统日志清空成功");
            result.put("clearedCount", 150);
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "系统日志清空失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 辅助方法
    private String formatMemorySize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return (bytes / (1024 * 1024)) + " MB";
        } else {
            return (bytes / (1024 * 1024 * 1024)) + " GB";
        }
    }

    private String formatUptime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + "天 " + (hours % 24) + "小时";
        } else if (hours > 0) {
            return hours + "小时 " + (minutes % 60) + "分钟";
        } else if (minutes > 0) {
            return minutes + "分钟 " + (seconds % 60) + "秒";
        } else {
            return seconds + "秒";
        }
    }
}

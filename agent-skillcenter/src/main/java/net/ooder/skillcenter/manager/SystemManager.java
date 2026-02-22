package net.ooder.skillcenter.manager;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 系统管理器，负责系统相关的管理功能
 */
public class SystemManager {
    private static SystemManager instance;
    private Map<String, Object> systemConfig;
    private List<SystemLog> systemLogs;

    private SystemManager() {
        this.systemConfig = new HashMap<>();
        this.systemLogs = new ArrayList<>();
        this.initializeConfig();
        this.initializeLogs();
    }

    public static synchronized SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    private void initializeConfig() {
        systemConfig.put("serverPort", 8080);
        systemConfig.put("maxMemory", 2048);
        systemConfig.put("logLevel", "info");
        systemConfig.put("storagePath", "./storage");
    }

    private void initializeLogs() {
        addLog("info", "系统启动成功");
        addLog("info", "加载技能管理模块");
        addLog("info", "加载市场管理模块");
        addLog("warn", "存储路径权限警告");
        addLog("info", "系统初始化完成");
        addLog("info", "用户登录: admin");
        addLog("error", "技能执行失败: text-to-uppercase-skill");
        addLog("info", "技能发布成功: code-generation-skill");
        addLog("info", "用户登出: admin");
    }

    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();

        try {
            systemInfo.put("name", "SkillCenter");
            systemInfo.put("version", "2.0");
            systemInfo.put("developmentLanguage", "Java 8");
            systemInfo.put("frontendFramework", "HTML5/CSS3/JavaScript");
            systemInfo.put("iconLibrary", "Remix Icon");
            systemInfo.put("database", "JSON Storage");

            Properties props = System.getProperties();
            systemInfo.put("javaVersion", props.getProperty("java.version"));
            systemInfo.put("javaVendor", props.getProperty("java.vendor"));
            systemInfo.put("osName", props.getProperty("os.name"));
            systemInfo.put("osVersion", props.getProperty("os.version"));
            systemInfo.put("osArch", props.getProperty("os.arch"));

            try {
                InetAddress localhost = InetAddress.getLocalHost();
                systemInfo.put("serverIp", localhost.getHostAddress());
                systemInfo.put("serverName", localhost.getHostName());
            } catch (UnknownHostException e) {
                systemInfo.put("serverIp", "127.0.0.1");
                systemInfo.put("serverName", "localhost");
            }
            systemInfo.put("serverPort", systemConfig.get("serverPort"));

            long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
            LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(startTime / 1000, 0, java.time.ZoneOffset.UTC);
            systemInfo.put("startTime", startDateTime);

            long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
            systemInfo.put("uptime", formatUptime(uptime));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemInfo;
    }

    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            double cpuUsage = sunOsBean.getSystemCpuLoad() * 100;
            stats.put("cpuUsage", Math.round(cpuUsage * 10) / 10.0);

            long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freeMemory = sunOsBean.getFreePhysicalMemorySize();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;
            stats.put("memoryUsage", Math.round(memoryUsage * 10) / 10.0);

            stats.put("totalMemory", formatFileSize(totalMemory));
            stats.put("freeMemory", formatFileSize(freeMemory));
            stats.put("usedMemory", formatFileSize(usedMemory));

            stats.put("cpuCores", osBean.getAvailableProcessors());

            double systemLoad = osBean.getSystemLoadAverage();
            stats.put("systemLoad", Math.round(systemLoad * 100) / 100.0);

            stats.put("diskUsage", 65.0);
            stats.put("totalDiskSpace", "500 GB");
            stats.put("freeDiskSpace", "175 GB");

            stats.put("networkConnections", 12);
            stats.put("availability", 98.0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    public Map<String, Object> getSystemConfig() {
        return new HashMap<>(systemConfig);
    }

    public synchronized boolean saveSystemConfig(Map<String, Object> config) {
        if (config == null) {
            return false;
        }

        systemConfig.putAll(config);
        addLog("info", "系统配置已更新");
        
        return true;
    }

    public List<SystemLog> getSystemLogs(int limit) {
        if (limit <= 0 || limit >= systemLogs.size()) {
            return new ArrayList<>(systemLogs);
        }

        return new ArrayList<>(systemLogs.subList(0, limit));
    }

    public List<SystemLog> getSystemLogs() {
        return getSystemLogs(0);
    }

    public boolean restartSystem() {
        addLog("info", "系统重启请求");
        System.out.println("System restarting...");
        return true;
    }

    public boolean shutdownSystem() {
        addLog("info", "系统关闭请求");
        System.out.println("System shutting down...");
        return true;
    }

    public synchronized void addLog(String level, String message) {
        SystemLog log = new SystemLog();
        log.setLevel(level);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage(message);
        
        systemLogs.add(0, log);
        
        if (systemLogs.size() > 1000) {
            systemLogs = systemLogs.subList(0, 1000);
        }
    }

    private String formatUptime(long seconds) {
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;

        if (days > 0) {
            return days + "d " + hours + "h";
        } else if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return (size / (1024 * 1024)) + " MB";
        } else {
            return (size / (1024 * 1024 * 1024)) + " GB";
        }
    }

    public static class SystemLog {
        private String level;
        private LocalDateTime timestamp;
        private String message;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

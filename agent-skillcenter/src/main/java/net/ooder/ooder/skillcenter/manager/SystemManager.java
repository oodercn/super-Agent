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
    // 单例实例
    private static SystemManager instance;
    
    // 系统配置
    private Map<String, Object> systemConfig;
    
    // 系统日志
    private List<SystemLog> systemLogs;

    /**
     * 私有构造方法
     */
    private SystemManager() {
        this.systemConfig = new HashMap<>();
        this.systemLogs = new ArrayList<>();
        this.initializeConfig();
        this.initializeLogs();
    }

    /**
     * 获取实例
     * @return 系统管理器实例
     */
    public static synchronized SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    /**
     * 初始化系统配置
     */
    private void initializeConfig() {
        systemConfig.put("serverPort", 8080);
        systemConfig.put("maxMemory", 2048);
        systemConfig.put("logLevel", "info");
        systemConfig.put("storagePath", "./storage");
    }

    /**
     * 初始化系统日志
     */
    private void initializeLogs() {
        // 创建示例日志数据
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

    /**
     * 获取系统信息
     * @return 系统信息
     */
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();

        try {
            // 系统基本信息
            systemInfo.put("name", "SkillCenter");
            systemInfo.put("version", "0.6.5");
            systemInfo.put("developmentLanguage", "Java 8");
            systemInfo.put("frontendFramework", "HTML5/CSS3/JavaScript");
            systemInfo.put("iconLibrary", "Remix Icon");
            systemInfo.put("database", "JSON Storage");

            // 运行环境信息
            Properties props = System.getProperties();
            systemInfo.put("javaVersion", props.getProperty("java.version"));
            systemInfo.put("javaVendor", props.getProperty("java.vendor"));
            systemInfo.put("osName", props.getProperty("os.name"));
            systemInfo.put("osVersion", props.getProperty("os.version"));
            systemInfo.put("osArch", props.getProperty("os.arch"));

            // 服务器信息
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                systemInfo.put("serverIp", localhost.getHostAddress());
                systemInfo.put("serverName", localhost.getHostName());
            } catch (UnknownHostException e) {
                systemInfo.put("serverIp", "127.0.0.1");
                systemInfo.put("serverName", "localhost");
            }
            systemInfo.put("serverPort", systemConfig.get("serverPort"));

            // 启动时间
            long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
            LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(startTime / 1000, 0, java.time.ZoneOffset.UTC);
            systemInfo.put("startTime", startDateTime);

            // 运行时间
            long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
            systemInfo.put("uptime", formatUptime(uptime));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemInfo;
    }

    /**
     * 获取系统统计数据
     * @return 系统统计数据
     */
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 操作系统MXBean
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            // CPU使用率
            double cpuUsage = sunOsBean.getSystemCpuLoad() * 100;
            stats.put("cpuUsage", Math.round(cpuUsage * 10) / 10.0);

            // 内存使用率
            long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freeMemory = sunOsBean.getFreePhysicalMemorySize();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;
            stats.put("memoryUsage", Math.round(memoryUsage * 10) / 10.0);

            // 内存信息
            stats.put("totalMemory", formatFileSize(totalMemory));
            stats.put("freeMemory", formatFileSize(freeMemory));
            stats.put("usedMemory", formatFileSize(usedMemory));

            // CPU核心数
            stats.put("cpuCores", osBean.getAvailableProcessors());

            // 系统负载
            double systemLoad = osBean.getSystemLoadAverage();
            stats.put("systemLoad", Math.round(systemLoad * 100) / 100.0);

            // 磁盘使用率（模拟值）
            stats.put("diskUsage", 65.0);
            stats.put("totalDiskSpace", "500 GB");
            stats.put("freeDiskSpace", "175 GB");

            // 网络连接数（模拟值）
            stats.put("networkConnections", 12);

            // 服务可用性（模拟值）
            stats.put("availability", 98.0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    /**
     * 获取系统配置
     * @return 系统配置
     */
    public Map<String, Object> getSystemConfig() {
        return new HashMap<>(systemConfig);
    }

    /**
     * 保存系统配置
     * @param config 系统配置
     * @return 保存是否成功
     */
    public synchronized boolean saveSystemConfig(Map<String, Object> config) {
        if (config == null) {
            return false;
        }

        // 更新配置
        systemConfig.putAll(config);
        
        // 记录日志
        addLog("info", "系统配置已更新");
        
        return true;
    }

    /**
     * 获取系统日志
     * @param limit 限制数量
     * @return 系统日志
     */
    public List<SystemLog> getSystemLogs(int limit) {
        if (limit <= 0 || limit >= systemLogs.size()) {
            return new ArrayList<>(systemLogs);
        }

        return new ArrayList<>(systemLogs.subList(0, limit));
    }

    /**
     * 获取系统日志
     * @return 系统日志
     */
    public List<SystemLog> getSystemLogs() {
        return getSystemLogs(0);
    }

    /**
     * 重启系统
     * @return 重启是否成功
     */
    public boolean restartSystem() {
        // 记录日志
        addLog("info", "系统重启请求");
        
        // 模拟重启系统
        System.out.println("System restarting...");
        
        // 实际项目中，这里应该实现真正的系统重启逻辑
        // 注意：在生产环境中，这可能需要特殊权限和处理
        
        return true;
    }

    /**
     * 关闭系统
     * @return 关闭是否成功
     */
    public boolean shutdownSystem() {
        // 记录日志
        addLog("info", "系统关闭请求");
        
        // 模拟关闭系统
        System.out.println("System shutting down...");
        
        // 实际项目中，这里应该实现真正的系统关闭逻辑
        // 注意：在生产环境中，这可能需要特殊权限和处理
        
        return true;
    }

    /**
     * 添加系统日志
     * @param level 日志级别
     * @param message 日志消息
     */
    public synchronized void addLog(String level, String message) {
        SystemLog log = new SystemLog();
        log.setLevel(level);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage(message);
        
        systemLogs.add(0, log); // 添加到列表开头
        
        // 限制日志数量
        if (systemLogs.size() > 1000) {
            systemLogs = systemLogs.subList(0, 1000);
        }
    }

    /**
     * 格式化运行时间
     * @param seconds 秒数
     * @return 格式化后的运行时间
     */
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

    /**
     * 格式化文件大小
     * @param size 字节大小
     * @return 格式化后的文件大小
     */
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

    /**
     * 系统日志类
     */
    public static class SystemLog {
        private String level;
        private LocalDateTime timestamp;
        private String message;

        // getters and setters
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

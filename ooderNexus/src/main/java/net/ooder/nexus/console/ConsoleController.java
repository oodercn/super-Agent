package net.ooder.nexus.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.ooder.nexus.skill.NexusSkill;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@RestController
@RequestMapping("/api/console")
public class ConsoleController {
    
    @Autowired
    private NexusSkill nexusSkill;
    
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Dashboard data retrieved successfully");
        response.put("timestamp", System.currentTimeMillis());
        
        // 仪表盘数据
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("summary", "MCP Agent Console Dashboard");
        dashboard.put("version", "v0.6.5");
        dashboard.put("title", "Ooder Rad Style Dashboard");
        
        // 系统概览
        dashboard.put("systemOverview", getSystemOverview());
        
        // 服务状态
        dashboard.put("serviceStatus", getServiceStatus());
        
        // 资源使用
        dashboard.put("resourceUsage", getResourceUsage());
        
        // 网络状态
        dashboard.put("networkStatus", getNetworkStatus());
        
        // 最近活动
        dashboard.put("recentActivity", getRecentActivity());
        
        // 警报信息
        dashboard.put("alerts", getAlerts());
        
        response.put("data", dashboard);
        
        return response;
    }
    
    @GetMapping("/nav")
    public Map<String, Object> getNavigation() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Navigation data retrieved successfully");
        response.put("timestamp", System.currentTimeMillis());
        
        // 导航菜单数据
        Map<String, Object> nav = new HashMap<>();
        nav.put("title", "MCP Agent Console");
        nav.put("version", "v0.6.5");
        nav.put("logo", "Ooder Agent");
        nav.put("favicon", "/favicon.ico");
        
        // 主菜单
        List<Map<String, Object>> mainMenu = new ArrayList<>();
        
        // 仪表盘
        Map<String, Object> dashboardMenu = new HashMap<>();
        dashboardMenu.put("id", "dashboard");
        dashboardMenu.put("name", "仪表盘");
        dashboardMenu.put("icon", "ri-dashboard-line");
        dashboardMenu.put("url", "pages/dashboard.html");
        dashboardMenu.put("priority", "high");
        dashboardMenu.put("status", "implemented");
        dashboardMenu.put("badge", null);
        mainMenu.add(dashboardMenu);
        
        // 家庭功能
        Map<String, Object> homeMenu = new HashMap<>();
        homeMenu.put("id", "home-function");
        homeMenu.put("name", "家庭功能");
        homeMenu.put("icon", "ri-home-line");
        homeMenu.put("priority", "high");
        homeMenu.put("status", "implemented");
        
        List<Map<String, Object>> homeSubMenu = new ArrayList<>();
        Map<String, Object> homeDashboardMenu = new HashMap<>();
        homeDashboardMenu.put("id", "home-dashboard");
        homeDashboardMenu.put("name", "家庭仪表盘");
        homeDashboardMenu.put("icon", "ri-home-smile-line");
        homeDashboardMenu.put("url", "pages/home/home-dashboard.html");
        homeDashboardMenu.put("priority", "high");
        homeDashboardMenu.put("status", "implemented");
        homeSubMenu.add(homeDashboardMenu);
        
        Map<String, Object> deviceListMenu = new HashMap<>();
        deviceListMenu.put("id", "device-list");
        deviceListMenu.put("name", "设备管理");
        deviceListMenu.put("icon", "ri-device-line");
        deviceListMenu.put("url", "pages/home/device-list.html");
        deviceListMenu.put("priority", "high");
        deviceListMenu.put("status", "implemented");
        homeSubMenu.add(deviceListMenu);
        
        Map<String, Object> securityStatusMenu = new HashMap<>();
        securityStatusMenu.put("id", "security-status");
        securityStatusMenu.put("name", "家庭安全");
        securityStatusMenu.put("icon", "ri-shield-home-line");
        securityStatusMenu.put("url", "pages/home/security-status.html");
        securityStatusMenu.put("priority", "high");
        securityStatusMenu.put("status", "implemented");
        homeSubMenu.add(securityStatusMenu);
        homeMenu.put("children", homeSubMenu);
        mainMenu.add(homeMenu);
        
        // 小局域网功能
        Map<String, Object> lanMenu = new HashMap<>();
        lanMenu.put("id", "lan-function");
        lanMenu.put("name", "小局域网功能");
        lanMenu.put("icon", "ri-wifi-line");
        lanMenu.put("priority", "high");
        lanMenu.put("status", "implemented");
        
        List<Map<String, Object>> lanSubMenu = new ArrayList<>();
        Map<String, Object> lanDashboardMenu = new HashMap<>();
        lanDashboardMenu.put("id", "lan-dashboard");
        lanDashboardMenu.put("name", "局域网仪表盘");
        lanDashboardMenu.put("icon", "ri-network-line");
        lanDashboardMenu.put("url", "pages/lan/lan-dashboard.html");
        lanDashboardMenu.put("priority", "high");
        lanDashboardMenu.put("status", "implemented");
        lanSubMenu.add(lanDashboardMenu);
        
        Map<String, Object> networkDevicesMenu = new HashMap<>();
        networkDevicesMenu.put("id", "network-devices");
        networkDevicesMenu.put("name", "网络设备");
        networkDevicesMenu.put("icon", "ri-router-wireless-line");
        networkDevicesMenu.put("url", "pages/lan/network-devices.html");
        networkDevicesMenu.put("priority", "high");
        networkDevicesMenu.put("status", "implemented");
        lanSubMenu.add(networkDevicesMenu);
        
        Map<String, Object> networkSettingsMenu = new HashMap<>();
        networkSettingsMenu.put("id", "network-settings");
        networkSettingsMenu.put("name", "网络配置");
        networkSettingsMenu.put("icon", "ri-settings-3-line");
        networkSettingsMenu.put("url", "pages/lan/network-settings.html");
        networkSettingsMenu.put("priority", "high");
        networkSettingsMenu.put("status", "implemented");
        lanSubMenu.add(networkSettingsMenu);
        lanMenu.put("children", lanSubMenu);
        mainMenu.add(lanMenu);
        
        // 命令体系管理
        Map<String, Object> commandMenu = new HashMap<>();
        commandMenu.put("id", "command-system");
        commandMenu.put("name", "命令体系管理");
        commandMenu.put("icon", "ri-command-line");
        commandMenu.put("priority", "high");
        commandMenu.put("status", "implemented");
        
        List<Map<String, Object>> commandSubMenu = new ArrayList<>();
        Map<String, Object> commandDashboardMenu = new HashMap<>();
        commandDashboardMenu.put("id", "command-dashboard");
        commandDashboardMenu.put("name", "命令仪表盘");
        commandDashboardMenu.put("icon", "ri-dashboard-2-line");
        commandDashboardMenu.put("url", "pages/command/command-dashboard.html");
        commandDashboardMenu.put("priority", "high");
        commandDashboardMenu.put("status", "implemented");
        commandSubMenu.add(commandDashboardMenu);
        
        Map<String, Object> commandSendMenu = new HashMap<>();
        commandSendMenu.put("id", "command-send");
        commandSendMenu.put("name", "命令发送");
        commandSendMenu.put("icon", "ri-send-plane-line");
        commandSendMenu.put("url", "pages/command/command-send.html");
        commandSendMenu.put("priority", "high");
        commandSendMenu.put("status", "implemented");
        commandSubMenu.add(commandSendMenu);
        
        Map<String, Object> networkLinksMenu = new HashMap<>();
        networkLinksMenu.put("id", "network-links");
        networkLinksMenu.put("name", "网络链接管理");
        networkLinksMenu.put("icon", "ri-link-line");
        networkLinksMenu.put("url", "pages/command/network-links.html");
        networkLinksMenu.put("priority", "high");
        networkLinksMenu.put("status", "implemented");
        commandSubMenu.add(networkLinksMenu);
        commandMenu.put("children", commandSubMenu);
        mainMenu.add(commandMenu);
        
        // 企业级功能
        Map<String, Object> enterpriseMenu = new HashMap<>();
        enterpriseMenu.put("id", "enterprise-function");
        enterpriseMenu.put("name", "企业级全功能应用");
        enterpriseMenu.put("icon", "ri-building-line");
        enterpriseMenu.put("priority", "medium");
        enterpriseMenu.put("status", "implemented");
        
        List<Map<String, Object>> enterpriseSubMenu = new ArrayList<>();
        Map<String, Object> enterpriseDashboardMenu = new HashMap<>();
        enterpriseDashboardMenu.put("id", "enterprise-dashboard");
        enterpriseDashboardMenu.put("name", "企业仪表盘");
        enterpriseDashboardMenu.put("icon", "ri-dashboard-2-line");
        enterpriseDashboardMenu.put("url", "pages/enterprise/enterprise-dashboard.html");
        enterpriseDashboardMenu.put("priority", "medium");
        enterpriseDashboardMenu.put("status", "planned");
        enterpriseSubMenu.add(enterpriseDashboardMenu);
        
        // 系统管理子菜单
        Map<String, Object> systemMenu = new HashMap<>();
        systemMenu.put("id", "system-management");
        systemMenu.put("name", "系统管理");
        systemMenu.put("icon", "ri-server-line");
        systemMenu.put("priority", "high");
        systemMenu.put("status", "implemented");
        
        List<Map<String, Object>> systemSubMenu = new ArrayList<>();
        Map<String, Object> serviceManagementMenu = new HashMap<>();
        serviceManagementMenu.put("id", "service-management");
        serviceManagementMenu.put("name", "服务管理");
        serviceManagementMenu.put("icon", "ri-server-fill");
        serviceManagementMenu.put("url", "pages/system/service-management.html");
        serviceManagementMenu.put("priority", "high");
        serviceManagementMenu.put("status", "implemented");
        Map<String, Object> badge = new HashMap<>();
        badge.put("text", "New");
        badge.put("color", "#3b82f6");
        serviceManagementMenu.put("badge", badge);
        systemSubMenu.add(serviceManagementMenu);
        
        Map<String, Object> mcpAgentMenu = new HashMap<>();
        mcpAgentMenu.put("id", "mcp-agent");
        mcpAgentMenu.put("name", "MCPAgent管理");
        mcpAgentMenu.put("icon", "ri-computer-line");
        mcpAgentMenu.put("url", "pages/system/request-management.html");
        mcpAgentMenu.put("priority", "medium");
        mcpAgentMenu.put("status", "planned");
        systemSubMenu.add(mcpAgentMenu);
        
        Map<String, Object> systemConfigMenu = new HashMap<>();
        systemConfigMenu.put("id", "system-config");
        systemConfigMenu.put("name", "系统配置");
        systemConfigMenu.put("icon", "ri-settings-3-line");
        systemConfigMenu.put("url", "pages/system/basic-config.html");
        systemConfigMenu.put("priority", "medium");
        systemConfigMenu.put("status", "planned");
        systemSubMenu.add(systemConfigMenu);
        systemMenu.put("children", systemSubMenu);
        enterpriseSubMenu.add(systemMenu);
        
        // 监控与分析
        Map<String, Object> monitoringMenu = new HashMap<>();
        monitoringMenu.put("id", "monitoring-analysis");
        monitoringMenu.put("name", "监控与分析");
        monitoringMenu.put("icon", "ri-bar-chart-3-line");
        monitoringMenu.put("priority", "medium");
        monitoringMenu.put("status", "implemented");
        
        List<Map<String, Object>> monitoringSubMenu = new ArrayList<>();
        Map<String, Object> healthCheckMenu = new HashMap<>();
        healthCheckMenu.put("id", "health-check");
        healthCheckMenu.put("name", "健康检查");
        healthCheckMenu.put("icon", "ri-heart-pulse-line");
        healthCheckMenu.put("url", "pages/monitoring/health-check.html");
        healthCheckMenu.put("priority", "medium");
        healthCheckMenu.put("status", "implemented");
        monitoringSubMenu.add(healthCheckMenu);
        
        Map<String, Object> logManagementMenu = new HashMap<>();
        logManagementMenu.put("id", "log-management");
        logManagementMenu.put("name", "日志管理");
        logManagementMenu.put("icon", "ri-file-text-line");
        logManagementMenu.put("url", "pages/monitoring/log-management.html");
        logManagementMenu.put("priority", "medium");
        logManagementMenu.put("status", "implemented");
        monitoringSubMenu.add(logManagementMenu);
        monitoringMenu.put("children", monitoringSubMenu);
        enterpriseSubMenu.add(monitoringMenu);
        
        enterpriseMenu.put("children", enterpriseSubMenu);
        mainMenu.add(enterpriseMenu);
        
        nav.put("mainMenu", mainMenu);
        
        // 快捷操作
        List<Map<String, Object>> quickActions = new ArrayList<>();
        Map<String, Object> quickRestartAction = new HashMap<>();
        quickRestartAction.put("id", "quick-restart");
        quickRestartAction.put("name", "重启服务");
        quickRestartAction.put("icon", "ri-refresh-line");
        quickRestartAction.put("color", "#3b82f6");
        quickRestartAction.put("action", "restartServices");
        quickActions.add(quickRestartAction);
        
        Map<String, Object> quickHealthAction = new HashMap<>();
        quickHealthAction.put("id", "quick-health");
        quickHealthAction.put("name", "健康检查");
        quickHealthAction.put("icon", "ri-health-line");
        quickHealthAction.put("color", "#10b981");
        quickHealthAction.put("action", "runHealthCheck");
        quickActions.add(quickHealthAction);
        
        Map<String, Object> quickLogAction = new HashMap<>();
        quickLogAction.put("id", "quick-log");
        quickLogAction.put("name", "查看日志");
        quickLogAction.put("icon", "ri-file-text-line");
        quickLogAction.put("color", "#f59e0b");
        quickLogAction.put("action", "viewLogs");
        quickActions.add(quickLogAction);
        
        Map<String, Object> quickConfigAction = new HashMap<>();
        quickConfigAction.put("id", "quick-config");
        quickConfigAction.put("name", "系统配置");
        quickConfigAction.put("icon", "ri-settings-3-line");
        quickConfigAction.put("color", "#6366f1");
        quickConfigAction.put("action", "openConfig");
        quickActions.add(quickConfigAction);
        nav.put("quickActions", quickActions);
        
        // 用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", "Administrator");
        userInfo.put("role", "系统管理员");
        userInfo.put("avatar", "https://ui-avatars.com/api/?name=Admin&background=3b82f6&color=fff");
        userInfo.put("lastLogin", System.currentTimeMillis() - 3600000);
        nav.put("userInfo", userInfo);
        
        response.put("data", nav);
        
        return response;
    }
    
    /**
     * 获取系统概览
     */
    private Map<String, Object> getSystemOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("systemName", "Independent MCP Agent");
        overview.put("systemVersion", "v0.6.5");
        overview.put("osName", System.getProperty("os.name"));
        overview.put("osVersion", System.getProperty("os.version"));
        overview.put("javaVersion", System.getProperty("java.version"));
        overview.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        overview.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        return overview;
    }
    
    /**
     * 获取服务状态
     */
    private Map<String, Object> getServiceStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("total", 4);
        status.put("running", 2);
        status.put("stopped", 1);
        status.put("warning", 1);
        status.put("error", 0);
        
        List<Map<String, Object>> services = new ArrayList<>();
        Map<String, Object> mcpService = new HashMap<>();
        mcpService.put("id", "mcp");
        mcpService.put("name", "MCP Agent");
        mcpService.put("status", "running");
        mcpService.put("version", "0.6.5");
        services.add(mcpService);
        
        Map<String, Object> skillCenterService = new HashMap<>();
        skillCenterService.put("id", "skillcenter");
        skillCenterService.put("name", "Skill Center");
        skillCenterService.put("status", "running");
        skillCenterService.put("version", "0.6.5");
        services.add(skillCenterService);
        
        Map<String, Object> skillFlowService = new HashMap<>();
        skillFlowService.put("id", "skillflow");
        skillFlowService.put("name", "Skill Flow");
        skillFlowService.put("status", "warning");
        skillFlowService.put("version", "0.6.5");
        services.add(skillFlowService);
        
        Map<String, Object> vfsService = new HashMap<>();
        vfsService.put("id", "vfs");
        vfsService.put("name", "Virtual File System");
        vfsService.put("status", "stopped");
        vfsService.put("version", "0.6.5");
        services.add(vfsService);
        
        status.put("services", services);
        return status;
    }
    
    /**
     * 获取资源使用情况
     */
    private Map<String, Object> getResourceUsage() {
        Map<String, Object> usage = new HashMap<>();
        
        // 内存使用
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double memoryUsagePercent = (double) usedMemory / maxMemory * 100;
        
        Map<String, Object> memoryMap = new HashMap<>();
        memoryMap.put("used", usedMemory);
        memoryMap.put("free", freeMemory);
        memoryMap.put("total", maxMemory);
        memoryMap.put("usagePercent", Math.round(memoryUsagePercent * 100) / 100.0);
        usage.put("memory", memoryMap);
        
        // CPU使用（模拟）
        double cpuUsage = 30 + Math.random() * 40;
        Map<String, Object> cpuMap = new HashMap<>();
        cpuMap.put("usagePercent", Math.round(cpuUsage * 100) / 100.0);
        cpuMap.put("cores", Runtime.getRuntime().availableProcessors());
        usage.put("cpu", cpuMap);
        
        // 磁盘使用（模拟）
        Map<String, Object> diskMap = new HashMap<>();
        diskMap.put("used", 1024 * 1024 * 1024 * 5L); // 5GB
        diskMap.put("free", 1024 * 1024 * 1024 * 15L); // 15GB
        diskMap.put("total", 1024 * 1024 * 1024 * 20L); // 20GB
        diskMap.put("usagePercent", 25.0);
        usage.put("disk", diskMap);
        
        return usage;
    }
    
    /**
     * 获取网络状态
     */
    private Map<String, Object> getNetworkStatus() {
        Map<String, Object> network = new HashMap<>();
        network.put("status", "healthy");
        network.put("message", "网络连接正常");
        network.put("connections", 15);
        network.put("packetsSent", 12000);
        network.put("packetsReceived", 10500);
        network.put("bytesSent", 10240000);
        network.put("bytesReceived", 8960000);
        network.put("latency", 50);
        network.put("jitter", 10);
        
        List<Map<String, Object>> interfaces = new ArrayList<>();
        Map<String, Object> eth0Interface = new HashMap<>();
        eth0Interface.put("name", "eth0");
        eth0Interface.put("status", "up");
        eth0Interface.put("ip", "192.168.1.100");
        eth0Interface.put("mac", "AA:BB:CC:DD:EE:FF");
        interfaces.add(eth0Interface);
        
        Map<String, Object> loInterface = new HashMap<>();
        loInterface.put("name", "lo");
        loInterface.put("status", "up");
        loInterface.put("ip", "127.0.0.1");
        loInterface.put("mac", "00:00:00:00:00:00");
        interfaces.add(loInterface);
        network.put("interfaces", interfaces);
        
        return network;
    }
    
    /**
     * 获取最近活动
     */
    private List<Map<String, Object>> getRecentActivity() {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("id", "1");
        activity1.put("type", "service_start");
        activity1.put("message", "MCP Agent 服务启动");
        activity1.put("timestamp", System.currentTimeMillis() - 300000L);
        activity1.put("user", "system");
        activity1.put("status", "success");
        activity1.put("icon", "ri-play-circle-line");
        activity1.put("color", "#10b981");
        activities.add(activity1);
        
        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("id", "2");
        activity2.put("type", "service_stop");
        activity2.put("message", "VFS 服务停止");
        activity2.put("timestamp", System.currentTimeMillis() - 600000L);
        activity2.put("user", "system");
        activity2.put("status", "success");
        activity2.put("icon", "ri-stop-circle-line");
        activity2.put("color", "#ef4444");
        activities.add(activity2);
        
        Map<String, Object> activity3 = new HashMap<>();
        activity3.put("id", "3");
        activity3.put("type", "health_check");
        activity3.put("message", "系统健康检查通过");
        activity3.put("timestamp", System.currentTimeMillis() - 900000L);
        activity3.put("user", "admin");
        activity3.put("status", "success");
        activity3.put("icon", "ri-health-line");
        activity3.put("color", "#3b82f6");
        activities.add(activity3);
        
        Map<String, Object> activity4 = new HashMap<>();
        activity4.put("id", "4");
        activity4.put("type", "config_update");
        activity4.put("message", "系统配置更新");
        activity4.put("timestamp", System.currentTimeMillis() - 1200000L);
        activity4.put("user", "admin");
        activity4.put("status", "success");
        activity4.put("icon", "ri-settings-3-line");
        activity4.put("color", "#6366f1");
        activities.add(activity4);
        
        return activities;
    }
    
    /**
     * 获取警报信息
     */
    private List<Map<String, Object>> getAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        
        Map<String, Object> alert1 = new HashMap<>();
        alert1.put("id", "alert-1");
        alert1.put("type", "warning");
        alert1.put("message", "Skill Flow 服务运行异常");
        alert1.put("timestamp", System.currentTimeMillis() - 1800000L);
        alert1.put("priority", "high");
        alert1.put("icon", "ri-alert-line");
        alert1.put("color", "#f59e0b");
        alerts.add(alert1);
        
        Map<String, Object> alert2 = new HashMap<>();
        alert2.put("id", "alert-2");
        alert2.put("type", "info");
        alert2.put("message", "系统资源使用正常");
        alert2.put("timestamp", System.currentTimeMillis() - 2400000L);
        alert2.put("priority", "low");
        alert2.put("icon", "ri-info-line");
        alert2.put("color", "#3b82f6");
        alerts.add(alert2);
        
        return alerts;
    }
}

package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;

/**
 * Admin Dashboard Service Interface
 * Provides dashboard statistics for admin module
 *
 * @author ooder Team
 * @version 0.7.0
 */
public interface AdminDashboardService {

    DashboardStatistics getDashboardStats();

    List<Map<String, Object>> getRecentActivities(int limit);

    List<Map<String, Object>> getSystemAlerts();

    List<Map<String, Object>> getTopSkills(int limit);

    List<Map<String, Object>> getActiveUsers(int limit);

    SystemHealth getSystemHealth();

    public static class DashboardStatistics {
        private int totalUsers;
        private int activeUsers;
        private int totalSkills;
        private int totalGroups;
        private int totalExecutions;
        private int successfulExecutions;
        private int failedExecutions;
        private double successRate;

        public int getTotalUsers() { return totalUsers; }
        public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
        public int getActiveUsers() { return activeUsers; }
        public void setActiveUsers(int activeUsers) { this.activeUsers = activeUsers; }
        public int getTotalSkills() { return totalSkills; }
        public void setTotalSkills(int totalSkills) { this.totalSkills = totalSkills; }
        public int getTotalGroups() { return totalGroups; }
        public void setTotalGroups(int totalGroups) { this.totalGroups = totalGroups; }
        public int getTotalExecutions() { return totalExecutions; }
        public void setTotalExecutions(int totalExecutions) { this.totalExecutions = totalExecutions; }
        public int getSuccessfulExecutions() { return successfulExecutions; }
        public void setSuccessfulExecutions(int successfulExecutions) { this.successfulExecutions = successfulExecutions; }
        public int getFailedExecutions() { return failedExecutions; }
        public void setFailedExecutions(int failedExecutions) { this.failedExecutions = failedExecutions; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
    }

    public static class SystemHealth {
        private String status;
        private double cpuUsage;
        private double memoryUsage;
        private double diskUsage;
        private long uptime;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public double getCpuUsage() { return cpuUsage; }
        public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
        public double getMemoryUsage() { return memoryUsage; }
        public void setMemoryUsage(double memoryUsage) { this.memoryUsage = memoryUsage; }
        public double getDiskUsage() { return diskUsage; }
        public void setDiskUsage(double diskUsage) { this.diskUsage = diskUsage; }
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
    }
}

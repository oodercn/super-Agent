package net.ooder.nexus.dto.admin;

import java.io.Serializable;

public class AdminDashboardStatsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int totalUsers;
    private int activeUsers;
    private int totalSkills;
    private int totalGroups;
    private int totalExecutions;
    private int successfulExecutions;
    private int failedExecutions;
    private double successRate;
    private String systemStatus;
    private double cpuUsage;
    private double memoryUsage;
    private long uptime;

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
    public String getSystemStatus() { return systemStatus; }
    public void setSystemStatus(String systemStatus) { this.systemStatus = systemStatus; }
    public double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
    public double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(double memoryUsage) { this.memoryUsage = memoryUsage; }
    public long getUptime() { return uptime; }
    public void setUptime(long uptime) { this.uptime = uptime; }
}

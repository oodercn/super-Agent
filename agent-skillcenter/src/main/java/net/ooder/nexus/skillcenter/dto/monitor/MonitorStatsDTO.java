package net.ooder.nexus.skillcenter.dto.monitor;

public class MonitorStatsDTO {
    private int totalSkills;
    private int runningSkills;
    private long totalExecutions;
    private double successRate;
    private double avgCpuUsage;
    private double avgMemoryUsage;
    private int activeAlerts;

    public int getTotalSkills() { return totalSkills; }
    public void setTotalSkills(int totalSkills) { this.totalSkills = totalSkills; }
    public int getRunningSkills() { return runningSkills; }
    public void setRunningSkills(int runningSkills) { this.runningSkills = runningSkills; }
    public long getTotalExecutions() { return totalExecutions; }
    public void setTotalExecutions(long totalExecutions) { this.totalExecutions = totalExecutions; }
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
    public double getAvgCpuUsage() { return avgCpuUsage; }
    public void setAvgCpuUsage(double avgCpuUsage) { this.avgCpuUsage = avgCpuUsage; }
    public double getAvgMemoryUsage() { return avgMemoryUsage; }
    public void setAvgMemoryUsage(double avgMemoryUsage) { this.avgMemoryUsage = avgMemoryUsage; }
    public int getActiveAlerts() { return activeAlerts; }
    public void setActiveAlerts(int activeAlerts) { this.activeAlerts = activeAlerts; }
}

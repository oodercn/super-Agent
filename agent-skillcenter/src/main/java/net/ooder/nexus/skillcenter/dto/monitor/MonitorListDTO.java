package net.ooder.nexus.skillcenter.dto.monitor;

import java.util.List;

public class MonitorListDTO {
    private List<SkillMonitorDTO> monitors;
    private int total;
    private int running;
    private int stopped;
    private int error;
    private double avgCpuUsage;
    private double avgMemoryUsage;

    public List<SkillMonitorDTO> getMonitors() { return monitors; }
    public void setMonitors(List<SkillMonitorDTO> monitors) { this.monitors = monitors; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public int getRunning() { return running; }
    public void setRunning(int running) { this.running = running; }
    public int getStopped() { return stopped; }
    public void setStopped(int stopped) { this.stopped = stopped; }
    public int getError() { return error; }
    public void setError(int error) { this.error = error; }
    public double getAvgCpuUsage() { return avgCpuUsage; }
    public void setAvgCpuUsage(double avgCpuUsage) { this.avgCpuUsage = avgCpuUsage; }
    public double getAvgMemoryUsage() { return avgMemoryUsage; }
    public void setAvgMemoryUsage(double avgMemoryUsage) { this.avgMemoryUsage = avgMemoryUsage; }
}

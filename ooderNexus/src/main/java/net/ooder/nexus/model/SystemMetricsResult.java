package net.ooder.nexus.model;

public class SystemMetricsResult {
    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;
    private long networkIn;
    private long networkOut;
    private int processCount;
    private String timestamp;
    private String message;

    public SystemMetricsResult() {
    }

    public SystemMetricsResult(double cpuUsage, double memoryUsage, double diskUsage, long networkIn, long networkOut, int processCount, String timestamp, String message) {
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
        this.networkIn = networkIn;
        this.networkOut = networkOut;
        this.processCount = processCount;
        this.timestamp = timestamp;
        this.message = message;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public long getNetworkIn() {
        return networkIn;
    }

    public void setNetworkIn(long networkIn) {
        this.networkIn = networkIn;
    }

    public long getNetworkOut() {
        return networkOut;
    }

    public void setNetworkOut(long networkOut) {
        this.networkOut = networkOut;
    }

    public int getProcessCount() {
        return processCount;
    }

    public void setProcessCount(int processCount) {
        this.processCount = processCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
package net.ooder.nexus.domain.system.model;

import java.io.Serializable;

public class SystemLoadData implements Serializable {
    private static final long serialVersionUID = 1L;

    private double cpuLoad;
    private double memoryUsage;
    private double diskUsage;
    private double networkLoad;
    private int processCount;
    private int threadCount;
    private long timestamp;

    public SystemLoadData() {
    }

    public SystemLoadData(double cpuLoad, double memoryUsage, double diskUsage, double networkLoad, int processCount, int threadCount, long timestamp) {
        this.cpuLoad = cpuLoad;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
        this.networkLoad = networkLoad;
        this.processCount = processCount;
        this.threadCount = threadCount;
        this.timestamp = timestamp;
    }

    public double getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
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

    public double getNetworkLoad() {
        return networkLoad;
    }

    public void setNetworkLoad(double networkLoad) {
        this.networkLoad = networkLoad;
    }

    public int getProcessCount() {
        return processCount;
    }

    public void setProcessCount(int processCount) {
        this.processCount = processCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

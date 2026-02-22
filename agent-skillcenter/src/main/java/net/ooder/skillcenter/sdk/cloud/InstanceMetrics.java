package net.ooder.skillcenter.sdk.cloud;

public class InstanceMetrics {
    private String instanceId;
    private double cpuUsage;
    private double memoryUsage;
    private long networkIn;
    private long networkOut;
    private long diskRead;
    private long diskWrite;
    private int requestCount;
    private double avgLatency;
    private long timestamp;
    private int replicas;

    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
    public double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(double memoryUsage) { this.memoryUsage = memoryUsage; }
    public long getNetworkIn() { return networkIn; }
    public void setNetworkIn(long networkIn) { this.networkIn = networkIn; }
    public long getNetworkOut() { return networkOut; }
    public void setNetworkOut(long networkOut) { this.networkOut = networkOut; }
    public long getDiskRead() { return diskRead; }
    public void setDiskRead(long diskRead) { this.diskRead = diskRead; }
    public long getDiskWrite() { return diskWrite; }
    public void setDiskWrite(long diskWrite) { this.diskWrite = diskWrite; }
    public int getRequestCount() { return requestCount; }
    public void setRequestCount(int requestCount) { this.requestCount = requestCount; }
    public double getAvgLatency() { return avgLatency; }
    public void setAvgLatency(double avgLatency) { this.avgLatency = avgLatency; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public int getReplicas() { return replicas; }
    public void setReplicas(int replicas) { this.replicas = replicas; }
}

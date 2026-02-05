package net.ooder.sdk.network.udp.monitoring;

import java.util.concurrent.atomic.AtomicInteger;

public class PortMetrics {
    private final int port;
    private final AtomicInteger allocationCount = new AtomicInteger(0);
    private final AtomicInteger releaseCount = new AtomicInteger(0);
    private volatile long lastAllocationTime;
    private volatile long lastReleaseTime;
    private volatile long lastUsageTime;
    
    public PortMetrics(int port) {
        this.port = port;
    }
    
    public int getPort() {
        return port;
    }
    
    public void recordAllocation() {
        allocationCount.incrementAndGet();
        lastAllocationTime = System.currentTimeMillis();
        lastUsageTime = System.currentTimeMillis();
    }
    
    public void recordRelease() {
        releaseCount.incrementAndGet();
        lastReleaseTime = System.currentTimeMillis();
        lastUsageTime = System.currentTimeMillis();
    }
    
    public int getAllocationCount() {
        return allocationCount.get();
    }
    
    public int getReleaseCount() {
        return releaseCount.get();
    }
    
    public long getLastAllocationTime() {
        return lastAllocationTime;
    }
    
    public long getLastReleaseTime() {
        return lastReleaseTime;
    }
    
    public long getLastUsageTime() {
        return lastUsageTime;
    }
    
    public boolean isActive() {
        long idleTime = System.currentTimeMillis() - lastUsageTime;
        return idleTime < 300000; // 5分钟内有活动
    }
}

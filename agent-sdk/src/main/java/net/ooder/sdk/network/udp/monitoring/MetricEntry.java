package net.ooder.sdk.network.udp.monitoring;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class MetricEntry {
    private final String operation;
    private final AtomicLong packetsSent = new AtomicLong(0);
    private final AtomicLong packetsReceived = new AtomicLong(0);
    private final AtomicLong bytesSent = new AtomicLong(0);
    private final AtomicLong bytesReceived = new AtomicLong(0);
    private final AtomicLong errors = new AtomicLong(0);
    private final AtomicLong retries = new AtomicLong(0);
    private final AtomicLong timeouts = new AtomicLong(0);
    private final AtomicReference<Double> averageLatency = new AtomicReference<>(0.0);
    private volatile long lastUpdateTime;
    
    public MetricEntry(String operation) {
        this.operation = operation;
        this.lastUpdateTime = System.currentTimeMillis();
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void recordPacketSent(int bytes) {
        packetsSent.incrementAndGet();
        bytesSent.addAndGet(bytes);
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public void recordPacketReceived(int bytes) {
        packetsReceived.incrementAndGet();
        bytesReceived.addAndGet(bytes);
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public void recordError(String errorType, String details) {
        errors.incrementAndGet();
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public void recordRetry(int retryCount) {
        retries.addAndGet(retryCount);
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public void recordTimeout(long timeoutMs) {
        timeouts.incrementAndGet();
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public void recordLatency(long latencyMs) {
        double current = averageLatency.get();
        double newValue = (current * 0.9) + (latencyMs * 0.1);
        averageLatency.set(newValue);
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public long getPacketsSent() {
        return packetsSent.get();
    }
    
    public long getPacketsReceived() {
        return packetsReceived.get();
    }
    
    public long getBytesSent() {
        return bytesSent.get();
    }
    
    public long getBytesReceived() {
        return bytesReceived.get();
    }
    
    public long getErrors() {
        return errors.get();
    }
    
    public long getRetries() {
        return retries.get();
    }
    
    public long getTimeouts() {
        return timeouts.get();
    }
    
    public double getAverageLatency() {
        return averageLatency.get();
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
}

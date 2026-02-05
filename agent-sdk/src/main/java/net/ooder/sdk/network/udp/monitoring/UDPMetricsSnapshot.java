package net.ooder.sdk.network.udp.monitoring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UDPMetricsSnapshot {
    private long timestamp;
    
    private long totalPacketsSent;
    private long totalPacketsReceived;
    private long totalBytesSent;
    private long totalBytesReceived;
    private long totalErrors;
    private long totalRetries;
    private long totalTimeouts;
    
    private double averageLatency;
    private double averageThroughput;
    private double errorRate;
    
    private Map<Integer, PortMetrics> portMetrics;
    private Map<String, MetricEntry> operationMetrics;
    
    public UDPMetricsSnapshot() {
        this.timestamp = System.currentTimeMillis();
        this.portMetrics = new ConcurrentHashMap<>();
        this.operationMetrics = new ConcurrentHashMap<>();
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public long getTotalPacketsSent() {
        return totalPacketsSent;
    }
    
    public void setTotalPacketsSent(long totalPacketsSent) {
        this.totalPacketsSent = totalPacketsSent;
    }
    
    public long getTotalPacketsReceived() {
        return totalPacketsReceived;
    }
    
    public void setTotalPacketsReceived(long totalPacketsReceived) {
        this.totalPacketsReceived = totalPacketsReceived;
    }
    
    public long getTotalBytesSent() {
        return totalBytesSent;
    }
    
    public void setTotalBytesSent(long totalBytesSent) {
        this.totalBytesSent = totalBytesSent;
    }
    
    public long getTotalBytesReceived() {
        return totalBytesReceived;
    }
    
    public void setTotalBytesReceived(long totalBytesReceived) {
        this.totalBytesReceived = totalBytesReceived;
    }
    
    public long getTotalErrors() {
        return totalErrors;
    }
    
    public void setTotalErrors(long totalErrors) {
        this.totalErrors = totalErrors;
    }
    
    public long getTotalRetries() {
        return totalRetries;
    }
    
    public void setTotalRetries(long totalRetries) {
        this.totalRetries = totalRetries;
    }
    
    public long getTotalTimeouts() {
        return totalTimeouts;
    }
    
    public void setTotalTimeouts(long totalTimeouts) {
        this.totalTimeouts = totalTimeouts;
    }
    
    public double getAverageLatency() {
        return averageLatency;
    }
    
    public void setAverageLatency(double averageLatency) {
        this.averageLatency = averageLatency;
    }
    
    public double getAverageThroughput() {
        return averageThroughput;
    }
    
    public void setAverageThroughput(double averageThroughput) {
        this.averageThroughput = averageThroughput;
    }
    
    public double getErrorRate() {
        return errorRate;
    }
    
    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }
    
    public Map<Integer, PortMetrics> getPortMetrics() {
        return portMetrics;
    }
    
    public void setPortMetrics(Map<Integer, PortMetrics> portMetrics) {
        this.portMetrics = portMetrics;
    }
    
    public Map<String, MetricEntry> getOperationMetrics() {
        return operationMetrics;
    }
    
    public void setOperationMetrics(Map<String, MetricEntry> operationMetrics) {
        this.operationMetrics = operationMetrics;
    }
    
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"timestamp\":").append(timestamp).append(",");
        json.append("\"totalPacketsSent\":").append(totalPacketsSent).append(",");
        json.append("\"totalPacketsReceived\":").append(totalPacketsReceived).append(",");
        json.append("\"totalBytesSent\":").append(totalBytesSent).append(",");
        json.append("\"totalBytesReceived\":").append(totalBytesReceived).append(",");
        json.append("\"totalErrors\":").append(totalErrors).append(",");
        json.append("\"totalRetries\":").append(totalRetries).append(",");
        json.append("\"totalTimeouts\":").append(totalTimeouts).append(",");
        json.append("\"averageLatency\":").append(averageLatency).append(",");
        json.append("\"averageThroughput\":").append(averageThroughput).append(",");
        json.append("\"errorRate\":").append(errorRate);
        json.append("}");
        return json.toString();
    }
}

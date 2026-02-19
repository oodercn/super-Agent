package net.ooder.sdk.capability.model;

public class MetricQuery {
    
    private String metricType;
    private long startTime;
    private long endTime;
    private String aggregation;
    private int limit;
    
    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public String getAggregation() { return aggregation; }
    public void setAggregation(String aggregation) { this.aggregation = aggregation; }
    
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
}

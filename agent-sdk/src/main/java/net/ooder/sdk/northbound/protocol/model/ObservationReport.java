package net.ooder.sdk.northbound.protocol.model;

import java.util.List;
import java.util.Map;

public class ObservationReport {
    
    private String reportId;
    private String targetId;
    private String reportType;
    private long startTime;
    private long endTime;
    private long generatedAt;
    private Map<String, Object> summary;
    private List<ObservationMetric> metrics;
    private List<AlertInfo> alerts;
    private String content;
    
    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public long getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(long generatedAt) { this.generatedAt = generatedAt; }
    
    public Map<String, Object> getSummary() { return summary; }
    public void setSummary(Map<String, Object> summary) { this.summary = summary; }
    
    public List<ObservationMetric> getMetrics() { return metrics; }
    public void setMetrics(List<ObservationMetric> metrics) { this.metrics = metrics; }
    
    public List<AlertInfo> getAlerts() { return alerts; }
    public void setAlerts(List<AlertInfo> alerts) { this.alerts = alerts; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

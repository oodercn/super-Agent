package net.ooder.sdk.northbound.protocol.model;

import java.util.List;

public class ReportConfig {
    
    private String reportType;
    private long startTime;
    private long endTime;
    private List<String> sections;
    private String format;
    
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public List<String> getSections() { return sections; }
    public void setSections(List<String> sections) { this.sections = sections; }
    
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}

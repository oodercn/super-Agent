package net.ooder.nexus.skillcenter.dto.monitor;

import java.util.List;
import java.util.Map;

public class MetricsHistoryDTO {
    private String skillId;
    private String instanceId;
    private long startTime;
    private long endTime;
    private String resolution;
    
    private List<MetricPoint> cpuMetrics;
    private List<MetricPoint> memoryMetrics;
    private List<MetricPoint> networkInMetrics;
    private List<MetricPoint> networkOutMetrics;
    private List<MetricPoint> executionTimeMetrics;
    private List<MetricPoint> throughputMetrics;
    
    private MetricSummary summary;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public List<MetricPoint> getCpuMetrics() { return cpuMetrics; }
    public void setCpuMetrics(List<MetricPoint> cpuMetrics) { this.cpuMetrics = cpuMetrics; }
    public List<MetricPoint> getMemoryMetrics() { return memoryMetrics; }
    public void setMemoryMetrics(List<MetricPoint> memoryMetrics) { this.memoryMetrics = memoryMetrics; }
    public List<MetricPoint> getNetworkInMetrics() { return networkInMetrics; }
    public void setNetworkInMetrics(List<MetricPoint> networkInMetrics) { this.networkInMetrics = networkInMetrics; }
    public List<MetricPoint> getNetworkOutMetrics() { return networkOutMetrics; }
    public void setNetworkOutMetrics(List<MetricPoint> networkOutMetrics) { this.networkOutMetrics = networkOutMetrics; }
    public List<MetricPoint> getExecutionTimeMetrics() { return executionTimeMetrics; }
    public void setExecutionTimeMetrics(List<MetricPoint> executionTimeMetrics) { this.executionTimeMetrics = executionTimeMetrics; }
    public List<MetricPoint> getThroughputMetrics() { return throughputMetrics; }
    public void setThroughputMetrics(List<MetricPoint> throughputMetrics) { this.throughputMetrics = throughputMetrics; }
    public MetricSummary getSummary() { return summary; }
    public void setSummary(MetricSummary summary) { this.summary = summary; }

    public static class MetricPoint {
        private long timestamp;
        private double value;
        private Map<String, Object> tags;

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        public Map<String, Object> getTags() { return tags; }
        public void setTags(Map<String, Object> tags) { this.tags = tags; }
    }

    public static class MetricSummary {
        private double avg;
        private double min;
        private double max;
        private double p50;
        private double p90;
        private double p99;
        private double stdDev;

        public double getAvg() { return avg; }
        public void setAvg(double avg) { this.avg = avg; }
        public double getMin() { return min; }
        public void setMin(double min) { this.min = min; }
        public double getMax() { return max; }
        public void setMax(double max) { this.max = max; }
        public double getP50() { return p50; }
        public void setP50(double p50) { this.p50 = p50; }
        public double getP90() { return p90; }
        public void setP90(double p90) { this.p90 = p90; }
        public double getP99() { return p99; }
        public void setP99(double p99) { this.p99 = p99; }
        public double getStdDev() { return stdDev; }
        public void setStdDev(double stdDev) { this.stdDev = stdDev; }
    }
}

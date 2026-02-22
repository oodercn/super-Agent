package net.ooder.nexus.skillcenter.dto.protocol;

import java.util.List;
import java.util.Map;

public class ObservationDTO {

    public static class ObservationConfigDTO {
        private boolean enableMetrics;
        private boolean enableLogs;
        private boolean enableTraces;
        private int metricsInterval;
        private int logLevel;
        private int retentionDays;
        private List<String> metricTypes;

        public boolean isEnableMetrics() { return enableMetrics; }
        public void setEnableMetrics(boolean enableMetrics) { this.enableMetrics = enableMetrics; }
        public boolean isEnableLogs() { return enableLogs; }
        public void setEnableLogs(boolean enableLogs) { this.enableLogs = enableLogs; }
        public boolean isEnableTraces() { return enableTraces; }
        public void setEnableTraces(boolean enableTraces) { this.enableTraces = enableTraces; }
        public int getMetricsInterval() { return metricsInterval; }
        public void setMetricsInterval(int metricsInterval) { this.metricsInterval = metricsInterval; }
        public int getLogLevel() { return logLevel; }
        public void setLogLevel(int logLevel) { this.logLevel = logLevel; }
        public int getRetentionDays() { return retentionDays; }
        public void setRetentionDays(int retentionDays) { this.retentionDays = retentionDays; }
        public List<String> getMetricTypes() { return metricTypes; }
        public void setMetricTypes(List<String> metricTypes) { this.metricTypes = metricTypes; }
    }

    public static class ObservationStatusDTO {
        private String targetId;
        private boolean observing;
        private long startTime;
        private long lastUpdate;
        private int metricsCount;
        private int logsCount;
        private int tracesCount;
        private int alertsCount;

        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public boolean isObserving() { return observing; }
        public void setObserving(boolean observing) { this.observing = observing; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
        public int getMetricsCount() { return metricsCount; }
        public void setMetricsCount(int metricsCount) { this.metricsCount = metricsCount; }
        public int getLogsCount() { return logsCount; }
        public void setLogsCount(int logsCount) { this.logsCount = logsCount; }
        public int getTracesCount() { return tracesCount; }
        public void setTracesCount(int tracesCount) { this.tracesCount = tracesCount; }
        public int getAlertsCount() { return alertsCount; }
        public void setAlertsCount(int alertsCount) { this.alertsCount = alertsCount; }
    }

    public static class MetricQueryDTO {
        private String metricType;
        private long startTime;
        private long endTime;
        private int limit;
        private String aggregation;

        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
        public String getAggregation() { return aggregation; }
        public void setAggregation(String aggregation) { this.aggregation = aggregation; }
    }

    public static class LogQueryDTO {
        private int level;
        private String keyword;
        private long startTime;
        private long endTime;
        private int limit;

        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }

    public static class TraceQueryDTO {
        private String operationType;
        private long startTime;
        private long endTime;
        private int limit;

        public String getOperationType() { return operationType; }
        public void setOperationType(String operationType) { this.operationType = operationType; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }

    public static class ObservationMetricDTO {
        private String metricId;
        private String metricType;
        private String targetId;
        private double value;
        private String unit;
        private Map<String, String> tags;
        private long timestamp;

        public String getMetricId() { return metricId; }
        public void setMetricId(String metricId) { this.metricId = metricId; }
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public Map<String, String> getTags() { return tags; }
        public void setTags(Map<String, String> tags) { this.tags = tags; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    public static class ObservationLogDTO {
        private String logId;
        private String targetId;
        private int level;
        private String message;
        private Map<String, Object> context;
        private long timestamp;

        public String getLogId() { return logId; }
        public void setLogId(String logId) { this.logId = logId; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    public static class ObservationTraceDTO {
        private String traceId;
        private String targetId;
        private String operationType;
        private String operationName;
        private long duration;
        private boolean success;
        private String errorMessage;
        private List<TraceSpanDTO> spans;
        private long timestamp;

        public String getTraceId() { return traceId; }
        public void setTraceId(String traceId) { this.traceId = traceId; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getOperationType() { return operationType; }
        public void setOperationType(String operationType) { this.operationType = operationType; }
        public String getOperationName() { return operationName; }
        public void setOperationName(String operationName) { this.operationName = operationName; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public List<TraceSpanDTO> getSpans() { return spans; }
        public void setSpans(List<TraceSpanDTO> spans) { this.spans = spans; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    public static class TraceSpanDTO {
        private String spanId;
        private String parentSpanId;
        private String name;
        private long startTime;
        private long endTime;
        private Map<String, String> attributes;

        public String getSpanId() { return spanId; }
        public void setSpanId(String spanId) { this.spanId = spanId; }
        public String getParentSpanId() { return parentSpanId; }
        public void setParentSpanId(String parentSpanId) { this.parentSpanId = parentSpanId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public Map<String, String> getAttributes() { return attributes; }
        public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
    }

    public static class AlertRuleConfigDTO {
        private String ruleId;
        private String targetId;
        private String metricType;
        private String condition;
        private double threshold;
        private int duration;
        private String severity;
        private String message;
        private boolean enabled;

        public String getRuleId() { return ruleId; }
        public void setRuleId(String ruleId) { this.ruleId = ruleId; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        public double getThreshold() { return threshold; }
        public void setThreshold(double threshold) { this.threshold = threshold; }
        public int getDuration() { return duration; }
        public void setDuration(int duration) { this.duration = duration; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class AlertInfoDTO {
        private String alertId;
        private String ruleId;
        private String targetId;
        private String metricType;
        private double value;
        private double threshold;
        private String severity;
        private String message;
        private long triggeredAt;
        private boolean acknowledged;
        private String acknowledgedBy;
        private long acknowledgedAt;

        public String getAlertId() { return alertId; }
        public void setAlertId(String alertId) { this.alertId = alertId; }
        public String getRuleId() { return ruleId; }
        public void setRuleId(String ruleId) { this.ruleId = ruleId; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        public double getThreshold() { return threshold; }
        public void setThreshold(double threshold) { this.threshold = threshold; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getTriggeredAt() { return triggeredAt; }
        public void setTriggeredAt(long triggeredAt) { this.triggeredAt = triggeredAt; }
        public boolean isAcknowledged() { return acknowledged; }
        public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
        public String getAcknowledgedBy() { return acknowledgedBy; }
        public void setAcknowledgedBy(String acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }
        public long getAcknowledgedAt() { return acknowledgedAt; }
        public void setAcknowledgedAt(long acknowledgedAt) { this.acknowledgedAt = acknowledgedAt; }
    }

    public static class ObservationSnapshotDTO {
        private String targetId;
        private long timestamp;
        private Map<String, ObservationMetricDTO> latestMetrics;
        private List<ObservationLogDTO> recentLogs;
        private List<AlertInfoDTO> activeAlerts;
        private String healthStatus;

        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public Map<String, ObservationMetricDTO> getLatestMetrics() { return latestMetrics; }
        public void setLatestMetrics(Map<String, ObservationMetricDTO> latestMetrics) { this.latestMetrics = latestMetrics; }
        public List<ObservationLogDTO> getRecentLogs() { return recentLogs; }
        public void setRecentLogs(List<ObservationLogDTO> recentLogs) { this.recentLogs = recentLogs; }
        public List<AlertInfoDTO> getActiveAlerts() { return activeAlerts; }
        public void setActiveAlerts(List<AlertInfoDTO> activeAlerts) { this.activeAlerts = activeAlerts; }
        public String getHealthStatus() { return healthStatus; }
        public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    }

    public static class ReportConfigDTO {
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

    public static class ObservationReportDTO {
        private String reportId;
        private String targetId;
        private String reportType;
        private long startTime;
        private long endTime;
        private long generatedAt;
        private Map<String, Object> summary;
        private List<ObservationMetricDTO> metrics;
        private List<AlertInfoDTO> alerts;
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
        public List<ObservationMetricDTO> getMetrics() { return metrics; }
        public void setMetrics(List<ObservationMetricDTO> metrics) { this.metrics = metrics; }
        public List<AlertInfoDTO> getAlerts() { return alerts; }
        public void setAlerts(List<AlertInfoDTO> alerts) { this.alerts = alerts; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}

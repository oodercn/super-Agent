package net.ooder.nexus.service.north;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 观测协议接口
 *
 * <p>SDK 0.7.2 北向协议，提供观测能力。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface ObservationProtocol {

    CompletableFuture<Void> startObservation(String targetId, ObservationConfig config);

    CompletableFuture<Void> stopObservation(String targetId);

    CompletableFuture<ObservationStatus> getObservationStatus(String targetId);

    CompletableFuture<List<ObservationMetric>> getMetrics(String targetId, MetricQuery query);

    CompletableFuture<ObservationSnapshot> getSnapshot(String targetId);

    void addObservationListener(ObservationListener listener);

    void removeObservationListener(ObservationListener listener);

    class ObservationConfig {
        private boolean enableMetrics;
        private boolean enableLogs;
        private boolean enableTraces;
        private int metricsInterval;
        private int retentionDays;

        public boolean isEnableMetrics() { return enableMetrics; }
        public void setEnableMetrics(boolean enableMetrics) { this.enableMetrics = enableMetrics; }
        public boolean isEnableLogs() { return enableLogs; }
        public void setEnableLogs(boolean enableLogs) { this.enableLogs = enableLogs; }
        public boolean isEnableTraces() { return enableTraces; }
        public void setEnableTraces(boolean enableTraces) { this.enableTraces = enableTraces; }
        public int getMetricsInterval() { return metricsInterval; }
        public void setMetricsInterval(int metricsInterval) { this.metricsInterval = metricsInterval; }
        public int getRetentionDays() { return retentionDays; }
        public void setRetentionDays(int retentionDays) { this.retentionDays = retentionDays; }
    }

    class ObservationStatus {
        private String targetId;
        private boolean observing;
        private long startTime;
        private long lastUpdate;
        private int metricsCount;

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
    }

    class MetricQuery {
        private String metricType;
        private long startTime;
        private long endTime;
        private int limit;

        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }

    class ObservationMetric {
        private String metricId;
        private String metricType;
        private String targetId;
        private double value;
        private String unit;
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
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    class ObservationSnapshot {
        private String targetId;
        private long timestamp;
        private Map<String, ObservationMetric> latestMetrics;
        private String healthStatus;

        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public Map<String, ObservationMetric> getLatestMetrics() { return latestMetrics; }
        public void setLatestMetrics(Map<String, ObservationMetric> latestMetrics) { this.latestMetrics = latestMetrics; }
        public String getHealthStatus() { return healthStatus; }
        public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    }

    interface ObservationListener {
        void onMetricCollected(String targetId, ObservationMetric metric);
        void onHealthChanged(String targetId, String oldStatus, String newStatus);
    }
}

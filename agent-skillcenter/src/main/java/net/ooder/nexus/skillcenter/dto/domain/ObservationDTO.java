package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class ObservationDTO {
    private String observationId;
    private String domainId;
    private String observerId;
    private ObservationType type;
    private ObservationScope scope;
    private List<ObservationMetricDTO> metrics;
    private List<ObservationEventDTO> events;
    private long timestamp;
    private long startTime;
    private long endTime;

    public String getObservationId() { return observationId; }
    public void setObservationId(String observationId) { this.observationId = observationId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public String getObserverId() { return observerId; }
    public void setObserverId(String observerId) { this.observerId = observerId; }
    public ObservationType getType() { return type; }
    public void setType(ObservationType type) { this.type = type; }
    public ObservationScope getScope() { return scope; }
    public void setScope(ObservationScope scope) { this.scope = scope; }
    public List<ObservationMetricDTO> getMetrics() { return metrics; }
    public void setMetrics(List<ObservationMetricDTO> metrics) { this.metrics = metrics; }
    public List<ObservationEventDTO> getEvents() { return events; }
    public void setEvents(List<ObservationEventDTO> events) { this.events = events; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public enum ObservationType {
        STATIC_TOPOLOGY,
        DYNAMIC_ROUTING,
        HISTORY_LOG,
        PERFORMANCE,
        SECURITY
    }

    public enum ObservationScope {
        DOMAIN,
        NODE,
        SKILL,
        EXECUTION
    }

    public static class ObservationMetricDTO {
        private String metricId;
        private String name;
        private String unit;
        private double value;
        private double threshold;
        private String status;
        private Map<String, Object> labels;

        public String getMetricId() { return metricId; }
        public void setMetricId(String metricId) { this.metricId = metricId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        public double getThreshold() { return threshold; }
        public void setThreshold(double threshold) { this.threshold = threshold; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Map<String, Object> getLabels() { return labels; }
        public void setLabels(Map<String, Object> labels) { this.labels = labels; }
    }

    public static class ObservationEventDTO {
        private String eventId;
        private String eventType;
        private String severity;
        private String message;
        private String sourceNodeId;
        private long eventTime;
        private Map<String, Object> details;

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getSourceNodeId() { return sourceNodeId; }
        public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
        public long getEventTime() { return eventTime; }
        public void setEventTime(long eventTime) { this.eventTime = eventTime; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }
}

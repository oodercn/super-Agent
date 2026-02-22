package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class HistoryLogDTO {
    private String logId;
    private String domainId;
    private LogScope scope;
    private List<LogEntryDTO> entries;
    private LogStatistics statistics;
    private long startTime;
    private long endTime;

    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public LogScope getScope() { return scope; }
    public void setScope(LogScope scope) { this.scope = scope; }
    public List<LogEntryDTO> getEntries() { return entries; }
    public void setEntries(List<LogEntryDTO> entries) { this.entries = entries; }
    public LogStatistics getStatistics() { return statistics; }
    public void setStatistics(LogStatistics statistics) { this.statistics = statistics; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public enum LogScope {
        DOMAIN,
        NODE,
        SKILL,
        EXECUTION,
        POLICY
    }

    public static class LogEntryDTO {
        private String entryId;
        private String eventType;
        private String severity;
        private String sourceNodeId;
        private String sourceType;
        private String message;
        private long timestamp;
        private Map<String, Object> context;
        private CorrectionDTO correction;

        public String getEntryId() { return entryId; }
        public void setEntryId(String entryId) { this.entryId = entryId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getSourceNodeId() { return sourceNodeId; }
        public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
        public String getSourceType() { return sourceType; }
        public void setSourceType(String sourceType) { this.sourceType = sourceType; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
        public CorrectionDTO getCorrection() { return correction; }
        public void setCorrection(CorrectionDTO correction) { this.correction = correction; }
    }

    public static class CorrectionDTO {
        private String correctionId;
        private String correctionType;
        private String description;
        private String correctedBy;
        private long correctedAt;
        private Map<String, Object> beforeState;
        private Map<String, Object> afterState;

        public String getCorrectionId() { return correctionId; }
        public void setCorrectionId(String correctionId) { this.correctionId = correctionId; }
        public String getCorrectionType() { return correctionType; }
        public void setCorrectionType(String correctionType) { this.correctionType = correctionType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCorrectedBy() { return correctedBy; }
        public void setCorrectedBy(String correctedBy) { this.correctedBy = correctedBy; }
        public long getCorrectedAt() { return correctedAt; }
        public void setCorrectedAt(long correctedAt) { this.correctedAt = correctedAt; }
        public Map<String, Object> getBeforeState() { return beforeState; }
        public void setBeforeState(Map<String, Object> beforeState) { this.beforeState = beforeState; }
        public Map<String, Object> getAfterState() { return afterState; }
        public void setAfterState(Map<String, Object> afterState) { this.afterState = afterState; }
    }

    public static class LogStatistics {
        private long totalEntries;
        private long errorCount;
        private long warningCount;
        private long infoCount;
        private long correctionCount;
        private Map<String, Long> eventTypeCounts;

        public long getTotalEntries() { return totalEntries; }
        public void setTotalEntries(long totalEntries) { this.totalEntries = totalEntries; }
        public long getErrorCount() { return errorCount; }
        public void setErrorCount(long errorCount) { this.errorCount = errorCount; }
        public long getWarningCount() { return warningCount; }
        public void setWarningCount(long warningCount) { this.warningCount = warningCount; }
        public long getInfoCount() { return infoCount; }
        public void setInfoCount(long infoCount) { this.infoCount = infoCount; }
        public long getCorrectionCount() { return correctionCount; }
        public void setCorrectionCount(long correctionCount) { this.correctionCount = correctionCount; }
        public Map<String, Long> getEventTypeCounts() { return eventTypeCounts; }
        public void setEventTypeCounts(Map<String, Long> eventTypeCounts) { this.eventTypeCounts = eventTypeCounts; }
    }
}

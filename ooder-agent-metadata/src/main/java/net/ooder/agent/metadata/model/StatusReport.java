package net.ooder.agent.metadata.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP状态报告模型
 * 统一处理SKILL故障、断联、数据重试、恢复、链路恢复等状态报告
 */
public class StatusReport implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 报告类型
     */
    @JSONField(name = "report_type")
    private String reportType;

    /**
     * 报告ID
     */
    @JSONField(name = "report_id")
    private String reportId;

    /**
     * 主体类型（skill、agent、link等）
     */
    @JSONField(name = "entity_type")
    private String entityType;

    /**
     * 主体ID
     */
    @JSONField(name = "entity_id")
    private String entityId;

    /**
     * 状态类型
     */
    @JSONField(name = "status_type")
    private String statusType;

    /**
     * 当前状态
     */
    @JSONField(name = "current_status")
    private String currentStatus;

    /**
     * 前一状态
     */
    @JSONField(name = "previous_status")
    private String previousStatus;

    /**
     * 状态详情
     */
    @JSONField(name = "details")
    private Map<String, Object> details;

    /**
     * 故障信息（如果有）
     */
    @JSONField(name = "error")
    private StatusError error;

    /**
     * 重试信息
     */
    @JSONField(name = "retry_info")
    private RetryInfo retryInfo;

    /**
     * 恢复信息
     */
    @JSONField(name = "recovery_info")
    private RecoveryInfo recoveryInfo;

    /**
     * 链路信息
     */
    @JSONField(name = "link_info")
    private LinkInfo linkInfo;

    /**
     * 元数据
     */
    @JSONField(name = "metadata")
    private ReportMetadata metadata;

    public StatusReport() {
        this.details = new HashMap<>();
        this.metadata = new ReportMetadata();
    }

    public StatusReport(String reportType, String entityType, String entityId, String statusType) {
        this();
        this.reportType = reportType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.statusType = statusType;
    }

    // Getters and Setters
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public void addDetail(String key, Object value) {
        this.details.put(key, value);
    }

    public StatusError getError() {
        return error;
    }

    public void setError(StatusError error) {
        this.error = error;
    }

    public RetryInfo getRetryInfo() {
        return retryInfo;
    }

    public void setRetryInfo(RetryInfo retryInfo) {
        this.retryInfo = retryInfo;
    }

    public RecoveryInfo getRecoveryInfo() {
        return recoveryInfo;
    }

    public void setRecoveryInfo(RecoveryInfo recoveryInfo) {
        this.recoveryInfo = recoveryInfo;
    }

    public LinkInfo getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(LinkInfo linkInfo) {
        this.linkInfo = linkInfo;
    }

    public ReportMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportMetadata metadata) {
        this.metadata = metadata;
    }

    // 内部类定义

    /**
     * 状态错误信息
     */
    public static class StatusError implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "code")
        private int code;

        @JSONField(name = "message")
        private String message;

        @JSONField(name = "type")
        private String type;

        @JSONField(name = "details")
        private String details;

        @JSONField(name = "timestamp")
        private long timestamp;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    /**
     * 重试信息
     */
    public static class RetryInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "retry_count")
        private int retryCount;

        @JSONField(name = "max_retries")
        private int maxRetries;

        @JSONField(name = "next_retry_time")
        private long nextRetryTime;

        @JSONField(name = "retry_interval")
        private long retryInterval;

        @JSONField(name = "retry_strategy")
        private String retryStrategy;

        public int getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(int retryCount) {
            this.retryCount = retryCount;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        public long getNextRetryTime() {
            return nextRetryTime;
        }

        public void setNextRetryTime(long nextRetryTime) {
            this.nextRetryTime = nextRetryTime;
        }

        public long getRetryInterval() {
            return retryInterval;
        }

        public void setRetryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
        }

        public String getRetryStrategy() {
            return retryStrategy;
        }

        public void setRetryStrategy(String retryStrategy) {
            this.retryStrategy = retryStrategy;
        }
    }

    /**
     * 恢复信息
     */
    public static class RecoveryInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "recovery_status")
        private String recoveryStatus;

        @JSONField(name = "recovery_time")
        private long recoveryTime;

        @JSONField(name = "recovery_method")
        private String recoveryMethod;

        @JSONField(name = "recovery_details")
        private String recoveryDetails;

        public String getRecoveryStatus() {
            return recoveryStatus;
        }

        public void setRecoveryStatus(String recoveryStatus) {
            this.recoveryStatus = recoveryStatus;
        }

        public long getRecoveryTime() {
            return recoveryTime;
        }

        public void setRecoveryTime(long recoveryTime) {
            this.recoveryTime = recoveryTime;
        }

        public String getRecoveryMethod() {
            return recoveryMethod;
        }

        public void setRecoveryMethod(String recoveryMethod) {
            this.recoveryMethod = recoveryMethod;
        }

        public String getRecoveryDetails() {
            return recoveryDetails;
        }

        public void setRecoveryDetails(String recoveryDetails) {
            this.recoveryDetails = recoveryDetails;
        }
    }

    /**
     * 链路信息
     */
    public static class LinkInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "link_id")
        private String linkId;

        @JSONField(name = "link_type")
        private String linkType;

        @JSONField(name = "source")
        private String source;

        @JSONField(name = "target")
        private String target;

        @JSONField(name = "link_status")
        private String linkStatus;

        @JSONField(name = "latency")
        private long latency;

        @JSONField(name = "bandwidth")
        private long bandwidth;

        @JSONField(name = "packet_loss")
        private double packetLoss;

        public String getLinkId() {
            return linkId;
        }

        public void setLinkId(String linkId) {
            this.linkId = linkId;
        }

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getLinkStatus() {
            return linkStatus;
        }

        public void setLinkStatus(String linkStatus) {
            this.linkStatus = linkStatus;
        }

        public long getLatency() {
            return latency;
        }

        public void setLatency(long latency) {
            this.latency = latency;
        }

        public long getBandwidth() {
            return bandwidth;
        }

        public void setBandwidth(long bandwidth) {
            this.bandwidth = bandwidth;
        }

        public double getPacketLoss() {
            return packetLoss;
        }

        public void setPacketLoss(double packetLoss) {
            this.packetLoss = packetLoss;
        }
    }

    /**
     * 报告元数据
     */
    public static class ReportMetadata implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "timestamp")
        private long timestamp;

        @JSONField(name = "source_id")
        private String sourceId;

        @JSONField(name = "trace_id")
        private String traceId;

        @JSONField(name = "priority")
        private String priority;

        @JSONField(name = "category")
        private String category;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    @Override
    public String toString() {
        return "StatusReport{" +
                "reportType='" + reportType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId='" + entityId + '\'' +
                ", statusType='" + statusType + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                '}';
    }
}

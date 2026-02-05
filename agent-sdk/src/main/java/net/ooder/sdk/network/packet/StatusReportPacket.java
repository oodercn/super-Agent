package net.ooder.sdk.network.packet;

import com.alibaba.fastjson.JSON;

public class StatusReportPacket extends UDPPacket {
    private String reportType;
    private String entityType;
    private String entityId;
    private String statusType;
    private String currentStatus;
    private String previousStatus;
    private StatusError error;
    private RetryInfo retryInfo;
    private RecoveryInfo recoveryInfo;
    private LinkInfo linkInfo;

    public StatusReportPacket() {
        super();
    }
    
    public static StatusReportPacket fromJson(String json) {
        return JSON.parseObject(json, StatusReportPacket.class);
    }
    
    @Override
    public String getType() {
        return "status_report";
    }
    
    // Getter and Setter methods
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StatusReportPacket that = (StatusReportPacket) o;

        if (reportType != null ? !reportType.equals(that.reportType) : that.reportType != null) return false;
        if (entityType != null ? !entityType.equals(that.entityType) : that.entityType != null) return false;
        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
        if (statusType != null ? !statusType.equals(that.statusType) : that.statusType != null) return false;
        if (currentStatus != null ? !currentStatus.equals(that.currentStatus) : that.currentStatus != null) return false;
        if (previousStatus != null ? !previousStatus.equals(that.previousStatus) : that.previousStatus != null) return false;
        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        if (retryInfo != null ? !retryInfo.equals(that.retryInfo) : that.retryInfo != null) return false;
        if (recoveryInfo != null ? !recoveryInfo.equals(that.recoveryInfo) : that.recoveryInfo != null) return false;
        return linkInfo != null ? linkInfo.equals(that.linkInfo) : that.linkInfo == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (reportType != null ? reportType.hashCode() : 0);
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0);
        result = 31 * result + (entityId != null ? entityId.hashCode() : 0);
        result = 31 * result + (statusType != null ? statusType.hashCode() : 0);
        result = 31 * result + (currentStatus != null ? currentStatus.hashCode() : 0);
        result = 31 * result + (previousStatus != null ? previousStatus.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (retryInfo != null ? retryInfo.hashCode() : 0);
        result = 31 * result + (recoveryInfo != null ? recoveryInfo.hashCode() : 0);
        result = 31 * result + (linkInfo != null ? linkInfo.hashCode() : 0);
        return result;
    }

    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private StatusReportPacket packet = new StatusReportPacket();

        public Builder reportType(String reportType) {
            packet.setReportType(reportType);
            return this;
        }

        public Builder entityType(String entityType) {
            packet.setEntityType(entityType);
            return this;
        }

        public Builder entityId(String entityId) {
            packet.setEntityId(entityId);
            return this;
        }

        public Builder statusType(String statusType) {
            packet.setStatusType(statusType);
            return this;
        }

        public Builder currentStatus(String currentStatus) {
            packet.setCurrentStatus(currentStatus);
            return this;
        }

        public Builder previousStatus(String previousStatus) {
            packet.setPreviousStatus(previousStatus);
            return this;
        }

        public Builder error(StatusError error) {
            packet.setError(error);
            return this;
        }

        public Builder retryInfo(RetryInfo retryInfo) {
            packet.setRetryInfo(retryInfo);
            return this;
        }

        public Builder recoveryInfo(RecoveryInfo recoveryInfo) {
            packet.setRecoveryInfo(recoveryInfo);
            return this;
        }

        public Builder linkInfo(LinkInfo linkInfo) {
            packet.setLinkInfo(linkInfo);
            return this;
        }

        public StatusReportPacket build() {
            return packet;
        }
    }
}

package net.ooder.nexus.dto.health;

import java.io.Serializable;
import java.util.List;

/**
 * Health check schedule DTO
 * Used for scheduling health checks
 */
public class HealthScheduleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Schedule type: daily, weekly, monthly, custom
     */
    private String scheduleType;

    /**
     * Schedule time in format "HH:mm"
     */
    private String scheduleTime;

    /**
     * Schedule expression (for custom type, cron expression)
     */
    private String scheduleExpression;

    /**
     * Check items to run
     */
    private List<String> checkItems;

    /**
     * Whether to send notification on failure
     */
    private Boolean notifyOnFailure;

    /**
     * Notification targets (email, webhook, etc.)
     */
    private List<String> notificationTargets;

    /**
     * Whether the schedule is enabled
     */
    private Boolean enabled;

    public HealthScheduleDTO() {
        this.scheduleType = "daily";
        this.notifyOnFailure = true;
        this.enabled = true;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleExpression() {
        return scheduleExpression;
    }

    public void setScheduleExpression(String scheduleExpression) {
        this.scheduleExpression = scheduleExpression;
    }

    public List<String> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<String> checkItems) {
        this.checkItems = checkItems;
    }

    public Boolean getNotifyOnFailure() {
        return notifyOnFailure;
    }

    public void setNotifyOnFailure(Boolean notifyOnFailure) {
        this.notifyOnFailure = notifyOnFailure;
    }

    public List<String> getNotificationTargets() {
        return notificationTargets;
    }

    public void setNotificationTargets(List<String> notificationTargets) {
        this.notificationTargets = notificationTargets;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HealthScheduleDTO dto = new HealthScheduleDTO();

        public Builder scheduleType(String scheduleType) {
            dto.setScheduleType(scheduleType);
            return this;
        }

        public Builder scheduleTime(String scheduleTime) {
            dto.setScheduleTime(scheduleTime);
            return this;
        }

        public Builder scheduleExpression(String scheduleExpression) {
            dto.setScheduleExpression(scheduleExpression);
            return this;
        }

        public Builder checkItems(List<String> checkItems) {
            dto.setCheckItems(checkItems);
            return this;
        }

        public Builder notifyOnFailure(Boolean notifyOnFailure) {
            dto.setNotifyOnFailure(notifyOnFailure);
            return this;
        }

        public Builder notificationTargets(List<String> notificationTargets) {
            dto.setNotificationTargets(notificationTargets);
            return this;
        }

        public Builder enabled(Boolean enabled) {
            dto.setEnabled(enabled);
            return this;
        }

        public HealthScheduleDTO build() {
            return dto;
        }
    }
}

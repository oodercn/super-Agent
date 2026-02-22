package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.provider.BaseProvider;

import java.util.List;
import java.util.Map;

/**
 * 健康检查Provider接口
 *
 * <p>定义健康检查相关的操作接口</p>
 * <p>注：此接口在 scene-engine 0.7.3 中不存在，由 ooderNexus 自行定义</p>
 */
public interface HealthProvider extends BaseProvider {

    /**
     * 运行健康检查
     */
    Result<HealthReport> runHealthCheck(Map<String, Object> params);

    /**
     * 导出健康报告
     */
    Result<HealthReport> exportHealthReport();

    /**
     * 调度健康检查
     */
    Result<HealthCheckSchedule> scheduleHealthCheck(Map<String, Object> params);

    /**
     * 检查服务状态
     */
    Result<ServiceHealth> checkService(String serviceName);

    /**
     * 获取健康检查历史
     */
    Result<PageResult<HealthCheckResult>> getHealthCheckHistory(int page, int size);

    /**
     * 获取健康检查计划列表
     */
    Result<List<HealthCheckSchedule>> listSchedules();

    /**
     * 取消健康检查计划
     */
    Result<Boolean> cancelSchedule(String scheduleId);

    /**
     * 健康报告
     */
    class HealthReport {
        private String reportId;
        private String status;
        private long timestamp;
        private List<HealthCheckResult> results;
        private int totalChecks;
        private int passedChecks;
        private int failedChecks;
        private long duration;
        private String summary;

        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public List<HealthCheckResult> getResults() { return results; }
        public void setResults(List<HealthCheckResult> results) { this.results = results; }
        public int getTotalChecks() { return totalChecks; }
        public void setTotalChecks(int totalChecks) { this.totalChecks = totalChecks; }
        public int getPassedChecks() { return passedChecks; }
        public void setPassedChecks(int passedChecks) { this.passedChecks = passedChecks; }
        public int getFailedChecks() { return failedChecks; }
        public void setFailedChecks(int failedChecks) { this.failedChecks = failedChecks; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
    }

    /**
     * 健康检查结果
     */
    class HealthCheckResult {
        private String checkId;
        private String name;
        private String status;
        private String message;
        private long duration;
        private long timestamp;
        private String details;

        public HealthCheckResult() {}

        public HealthCheckResult(String checkId, String name, String status, String message, 
                                 long duration, long timestamp, String details) {
            this.checkId = checkId;
            this.name = name;
            this.status = status;
            this.message = message;
            this.duration = duration;
            this.timestamp = timestamp;
            this.details = details;
        }

        public String getCheckId() { return checkId; }
        public void setCheckId(String checkId) { this.checkId = checkId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
    }

    /**
     * 健康检查计划
     */
    class HealthCheckSchedule {
        private String scheduleId;
        private String name;
        private String cronExpression;
        private List<String> checkItems;
        private boolean enabled;
        private String description;
        private long createdAt;
        private long lastRunAt;
        private Long nextRunAt;
        private String lastStatus;

        public HealthCheckSchedule() {}

        public HealthCheckSchedule(String scheduleId, String name, String cronExpression,
                                   List<String> checkItems, boolean enabled, String description,
                                   long createdAt, long lastRunAt, Long nextRunAt, String lastStatus) {
            this.scheduleId = scheduleId;
            this.name = name;
            this.cronExpression = cronExpression;
            this.checkItems = checkItems;
            this.enabled = enabled;
            this.description = description;
            this.createdAt = createdAt;
            this.lastRunAt = lastRunAt;
            this.nextRunAt = nextRunAt;
            this.lastStatus = lastStatus;
        }

        public String getScheduleId() { return scheduleId; }
        public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCronExpression() { return cronExpression; }
        public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
        public List<String> getCheckItems() { return checkItems; }
        public void setCheckItems(List<String> checkItems) { this.checkItems = checkItems; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getLastRunAt() { return lastRunAt; }
        public void setLastRunAt(long lastRunAt) { this.lastRunAt = lastRunAt; }
        public Long getNextRunAt() { return nextRunAt; }
        public void setNextRunAt(Long nextRunAt) { this.nextRunAt = nextRunAt; }
        public String getLastStatus() { return lastStatus; }
        public void setLastStatus(String lastStatus) { this.lastStatus = lastStatus; }
    }

    /**
     * 服务健康状态
     */
    class ServiceHealth {
        private String serviceName;
        private String status;
        private String message;
        private long responseTime;
        private long timestamp;
        private String endpoint;
        private String version;

        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getResponseTime() { return responseTime; }
        public void setResponseTime(long responseTime) { this.responseTime = responseTime; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }
}

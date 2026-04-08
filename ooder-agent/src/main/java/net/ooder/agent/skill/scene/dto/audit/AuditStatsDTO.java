package net.ooder.mvp.skill.scene.dto.audit;

public class AuditStatsDTO {
    
    private long totalEvents;
    private long successCount;
    private long failureCount;
    private long deniedCount;
    private long todayCount;
    private long weekCount;
    private long monthCount;

    public AuditStatsDTO() {
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(long failureCount) {
        this.failureCount = failureCount;
    }

    public long getDeniedCount() {
        return deniedCount;
    }

    public void setDeniedCount(long deniedCount) {
        this.deniedCount = deniedCount;
    }

    public long getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(long todayCount) {
        this.todayCount = todayCount;
    }

    public long getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(long weekCount) {
        this.weekCount = weekCount;
    }

    public long getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(long monthCount) {
        this.monthCount = monthCount;
    }
}

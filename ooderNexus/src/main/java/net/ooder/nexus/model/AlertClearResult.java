package net.ooder.nexus.model;

/**
 * 告警清除结果实体Bean
 * 用于HealthCheckController中clearAlerts方法的返回类型
 */
public class AlertClearResult {
    
    private int clearedCount;
    private int remainingCount;

    public int getClearedCount() {
        return clearedCount;
    }

    public void setClearedCount(int clearedCount) {
        this.clearedCount = clearedCount;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void setRemainingCount(int remainingCount) {
        this.remainingCount = remainingCount;
    }
}

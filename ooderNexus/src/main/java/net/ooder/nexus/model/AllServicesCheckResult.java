package net.ooder.nexus.model;

import net.ooder.nexus.controller.HealthCheckController;
import java.util.Date;
import java.util.List;

/**
 * 所有服务检查结果实体Bean
 * 用于HealthCheckController中checkAllServices方法的返回类型
 */
public class AllServicesCheckResult {
    
    private List<HealthCheckController.ServiceCheckResult> results;
    private int healthyCount;
    private int totalCount;
    private Date checkTime;

    public List<HealthCheckController.ServiceCheckResult> getResults() {
        return results;
    }

    public void setResults(List<HealthCheckController.ServiceCheckResult> results) {
        this.results = results;
    }

    public int getHealthyCount() {
        return healthyCount;
    }

    public void setHealthyCount(int healthyCount) {
        this.healthyCount = healthyCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
}

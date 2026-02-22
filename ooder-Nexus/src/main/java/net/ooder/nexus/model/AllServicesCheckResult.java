package net.ooder.nexus.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 所有服务检查结果实体Bean
 */
public class AllServicesCheckResult {
    
    private List<Map<String, Object>> results;
    private int healthyCount;
    private int totalCount;
    private Date checkTime;

    public List<Map<String, Object>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, Object>> results) {
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

package net.ooder.nexus.debug.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行汇总
 */
public class ExecutionSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private int totalSteps;
    private int successSteps;
    private int failedSteps;
    private double avgResponseTime;

    public ExecutionSummary() {}

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("totalSteps", totalSteps);
        map.put("successSteps", successSteps);
        map.put("failedSteps", failedSteps);
        map.put("avgResponseTime", avgResponseTime);
        return map;
    }

    // Getters and Setters
    public int getTotalSteps() { return totalSteps; }
    public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }
    public int getSuccessSteps() { return successSteps; }
    public void setSuccessSteps(int successSteps) { this.successSteps = successSteps; }
    public int getFailedSteps() { return failedSteps; }
    public void setFailedSteps(int failedSteps) { this.failedSteps = failedSteps; }
    public double getAvgResponseTime() { return avgResponseTime; }
    public void setAvgResponseTime(double avgResponseTime) { this.avgResponseTime = avgResponseTime; }
}

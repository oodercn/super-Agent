package net.ooder.nexus.skillcenter.dto.dashboard;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.Map;

public class ExecutionStatsDTO extends BaseDTO {

    private int totalExecutions;
    private int successfulExecutions;
    private int failedExecutions;
    private double successRate;
    private double averageExecutionTime;
    private Map<String, Integer> topExecutedSkills;
    private Map<String, Integer> executionTrend;

    public ExecutionStatsDTO() {}

    public int getTotalExecutions() {
        return totalExecutions;
    }

    public void setTotalExecutions(int totalExecutions) {
        this.totalExecutions = totalExecutions;
    }

    public int getSuccessfulExecutions() {
        return successfulExecutions;
    }

    public void setSuccessfulExecutions(int successfulExecutions) {
        this.successfulExecutions = successfulExecutions;
    }

    public int getFailedExecutions() {
        return failedExecutions;
    }

    public void setFailedExecutions(int failedExecutions) {
        this.failedExecutions = failedExecutions;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public double getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public void setAverageExecutionTime(double averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }

    public Map<String, Integer> getTopExecutedSkills() {
        return topExecutedSkills;
    }

    public void setTopExecutedSkills(Map<String, Integer> topExecutedSkills) {
        this.topExecutedSkills = topExecutedSkills;
    }

    public Map<String, Integer> getExecutionTrend() {
        return executionTrend;
    }

    public void setExecutionTrend(Map<String, Integer> executionTrend) {
        this.executionTrend = executionTrend;
    }
}

package net.ooder.nexus.debug.model;

import net.ooder.nexus.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行结果模型
 */
public class ExecutionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String executionId;
    private String simulatorId;
    private String scenarioId;
    private String status;
    private long startTime;
    private long endTime;
    private long duration;
    private List<StepResult> steps;
    private ExecutionSummary summary;
    private List<ExecutionLog> logs;
    private Map<String, Object> response;

    public ExecutionResult() {
        this.steps = new ArrayList<>();
        this.logs = new ArrayList<>();
    }

    public ExecutionResult(String executionId, String simulatorId, String scenarioId) {
        this();
        this.executionId = executionId;
        this.simulatorId = simulatorId;
        this.scenarioId = scenarioId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("executionId", executionId);
        map.put("simulatorId", simulatorId);
        map.put("scenarioId", scenarioId);
        map.put("status", status);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("duration", duration);
        map.put("steps", steps);
        map.put("summary", summary != null ? summary.toMap() : null);
        map.put("logs", logs);
        return map;
    }

    public String toJson() {
        return JsonUtils.toJson(this);
    }

    // Getters and Setters
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    public String getSimulatorId() { return simulatorId; }
    public void setSimulatorId(String simulatorId) { this.simulatorId = simulatorId; }
    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    public List<StepResult> getSteps() { return steps; }
    public void setSteps(List<StepResult> steps) { this.steps = steps; }
    public ExecutionSummary getSummary() { return summary; }
    public void setSummary(ExecutionSummary summary) { this.summary = summary; }
    public List<ExecutionLog> getLogs() { return logs; }
    public void setLogs(List<ExecutionLog> logs) { this.logs = logs; }
    public Map<String, Object> getResponse() { return response; }
    public void setResponse(Map<String, Object> response) { this.response = response; }
}

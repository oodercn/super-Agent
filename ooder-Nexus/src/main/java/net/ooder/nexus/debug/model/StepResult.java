package net.ooder.nexus.debug.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 步骤执行结果
 */
public class StepResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private int stepId;
    private String action;
    private Map<String, Object> request;
    private Map<String, Object> response;
    private long timestamp;
    private long duration;
    private boolean success;
    private String error;

    public StepResult() {}

    public StepResult(int stepId, String action, String description) {
        this.stepId = stepId;
        this.action = action;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("stepId", stepId);
        map.put("action", action);
        map.put("request", request);
        map.put("response", response);
        map.put("timestamp", timestamp);
        map.put("duration", duration);
        map.put("success", success);
        map.put("error", error);
        return map;
    }

    // Getters and Setters
    public int getStepId() { return stepId; }
    public void setStepId(int stepId) { this.stepId = stepId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Map<String, Object> getRequest() { return request; }
    public void setRequest(Map<String, Object> request) { this.request = request; }
    public Map<String, Object> getResponse() { return response; }
    public void setResponse(Map<String, Object> response) { this.response = response; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class OrchestrationResult {
    
    private String executionId;
    private String orchestrationId;
    private boolean success;
    private Map<String, Object> output;
    private List<StepResult> stepResults;
    private String errorMessage;
    private long startTime;
    private long endTime;
    private long duration;
    
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    
    public String getOrchestrationId() { return orchestrationId; }
    public void setOrchestrationId(String orchestrationId) { this.orchestrationId = orchestrationId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public Map<String, Object> getOutput() { return output; }
    public void setOutput(Map<String, Object> output) { this.output = output; }
    
    public List<StepResult> getStepResults() { return stepResults; }
    public void setStepResults(List<StepResult> stepResults) { this.stepResults = stepResults; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
}

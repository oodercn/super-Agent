package net.ooder.sdk.api.protocol;

import java.util.ArrayList;
import java.util.List;

public class CommandTrace {
    
    private String commandId;
    private CommandDirection direction;
    private CommandStatus status;
    private long createdTime;
    private long startTime;
    private long endTime;
    private long duration;
    private String source;
    private String target;
    private List<TraceStep> steps = new ArrayList<TraceStep>();
    private String errorMessage;
    private int retryCount;
    
    public String getCommandId() { return commandId; }
    public void setCommandId(String commandId) { this.commandId = commandId; }
    
    public CommandDirection getDirection() { return direction; }
    public void setDirection(CommandDirection direction) { this.direction = direction; }
    
    public CommandStatus getStatus() { return status; }
    public void setStatus(CommandStatus status) { this.status = status; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public List<TraceStep> getSteps() { return steps; }
    public void setSteps(List<TraceStep> steps) { this.steps = steps; }
    
    public void addStep(TraceStep step) { this.steps.add(step); }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public boolean isSuccess() { return status == CommandStatus.SUCCESS; }
    
    public boolean isFailed() { return status == CommandStatus.FAILED; }
    
    public boolean isTimeout() { return status == CommandStatus.TIMEOUT; }
    
    public static class TraceStep {
        private String stepName;
        private long timestamp;
        private String description;
        private boolean success;
        private String error;
        
        public String getStepName() { return stepName; }
        public void setStepName(String stepName) { this.stepName = stepName; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}

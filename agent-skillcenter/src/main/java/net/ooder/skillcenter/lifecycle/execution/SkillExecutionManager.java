package net.ooder.skillcenter.lifecycle.execution;

import java.util.List;
import java.util.Map;
import net.ooder.skillcenter.model.SkillContext;

public interface SkillExecutionManager {
    
    ExecutionResult executeSkill(String skillId, SkillContext context);
    
    void executeSkillAsync(String skillId, SkillContext context, ExecutionCallback callback);
    
    ExecutionStatus getExecutionStatus(String executionId);
    
    List<ExecutionRecord> getExecutionHistory(String skillId);
    
    List<ExecutionRecord> getExecutionHistory(String skillId, long startTime, long endTime);
    
    void cancelExecution(String executionId);
    
    void pauseExecution(String executionId);
    
    void resumeExecution(String executionId);
    
    class ExecutionResult {
        private String executionId;
        private String skillId;
        private boolean success;
        private Map<String, Object> result;
        private String errorMessage;
        private long startTime;
        private long endTime;
        private long duration;
        
        public String getExecutionId() {
            return executionId;
        }
        
        public void setExecutionId(String executionId) {
            this.executionId = executionId;
        }
        
        public String getSkillId() {
            return skillId;
        }
        
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public Map<String, Object> getResult() {
            return result;
        }
        
        public void setResult(Map<String, Object> result) {
            this.result = result;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        
        public long getEndTime() {
            return endTime;
        }
        
        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
        
        public long getDuration() {
            return duration;
        }
        
        public void setDuration(long duration) {
            this.duration = duration;
        }
    }
    
    class ExecutionStatus {
        private String executionId;
        private String skillId;
        private String status;
        private double progress;
        private long startTime;
        private long estimatedEndTime;
        
        public String getExecutionId() {
            return executionId;
        }
        
        public void setExecutionId(String executionId) {
            this.executionId = executionId;
        }
        
        public String getSkillId() {
            return skillId;
        }
        
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public double getProgress() {
            return progress;
        }
        
        public void setProgress(double progress) {
            this.progress = progress;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        
        public long getEstimatedEndTime() {
            return estimatedEndTime;
        }
        
        public void setEstimatedEndTime(long estimatedEndTime) {
            this.estimatedEndTime = estimatedEndTime;
        }
    }
    
    class ExecutionRecord {
        private String executionId;
        private String skillId;
        private String status;
        private long startTime;
        private long endTime;
        private long duration;
        private boolean success;
        private String errorMessage;
        private Map<String, Object> input;
        private Map<String, Object> output;
        
        public String getExecutionId() {
            return executionId;
        }
        
        public void setExecutionId(String executionId) {
            this.executionId = executionId;
        }
        
        public String getSkillId() {
            return skillId;
        }
        
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        
        public long getEndTime() {
            return endTime;
        }
        
        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
        
        public long getDuration() {
            return duration;
        }
        
        public void setDuration(long duration) {
            this.duration = duration;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        public Map<String, Object> getInput() {
            return input;
        }
        
        public void setInput(Map<String, Object> input) {
            this.input = input;
        }
        
        public Map<String, Object> getOutput() {
            return output;
        }
        
        public void setOutput(Map<String, Object> output) {
            this.output = output;
        }
    }
    
    interface ExecutionCallback {
        void onSuccess(ExecutionResult result);
        void onFailure(ExecutionResult result);
        void onProgress(String executionId, double progress);
    }
}

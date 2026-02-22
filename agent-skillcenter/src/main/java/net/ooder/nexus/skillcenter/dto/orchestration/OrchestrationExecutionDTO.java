package net.ooder.nexus.skillcenter.dto.orchestration;

import java.util.List;
import java.util.Map;

public class OrchestrationExecutionDTO {
    private String executionId;
    private String templateId;
    private String name;
    private String status;
    private long startTime;
    private long endTime;
    private long duration;
    
    private List<NodeExecution> nodeExecutions;
    private Map<String, Object> input;
    private Map<String, Object> output;
    private String errorMessage;
    private ExecutionStats stats;

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    public List<NodeExecution> getNodeExecutions() { return nodeExecutions; }
    public void setNodeExecutions(List<NodeExecution> nodeExecutions) { this.nodeExecutions = nodeExecutions; }
    public Map<String, Object> getInput() { return input; }
    public void setInput(Map<String, Object> input) { this.input = input; }
    public Map<String, Object> getOutput() { return output; }
    public void setOutput(Map<String, Object> output) { this.output = output; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public ExecutionStats getStats() { return stats; }
    public void setStats(ExecutionStats stats) { this.stats = stats; }

    public static class NodeExecution {
        private String nodeId;
        private String skillId;
        private String status;
        private long startTime;
        private long endTime;
        private long duration;
        private Map<String, Object> input;
        private Map<String, Object> output;
        private String errorMessage;
        private int retryCount;

        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public Map<String, Object> getInput() { return input; }
        public void setInput(Map<String, Object> input) { this.input = input; }
        public Map<String, Object> getOutput() { return output; }
        public void setOutput(Map<String, Object> output) { this.output = output; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    }

    public static class ExecutionStats {
        private int totalNodes;
        private int completedNodes;
        private int failedNodes;
        private int skippedNodes;
        private double avgNodeDuration;
        private long totalDataTransferred;

        public int getTotalNodes() { return totalNodes; }
        public void setTotalNodes(int totalNodes) { this.totalNodes = totalNodes; }
        public int getCompletedNodes() { return completedNodes; }
        public void setCompletedNodes(int completedNodes) { this.completedNodes = completedNodes; }
        public int getFailedNodes() { return failedNodes; }
        public void setFailedNodes(int failedNodes) { this.failedNodes = failedNodes; }
        public int getSkippedNodes() { return skippedNodes; }
        public void setSkippedNodes(int skippedNodes) { this.skippedNodes = skippedNodes; }
        public double getAvgNodeDuration() { return avgNodeDuration; }
        public void setAvgNodeDuration(double avgNodeDuration) { this.avgNodeDuration = avgNodeDuration; }
        public long getTotalDataTransferred() { return totalDataTransferred; }
        public void setTotalDataTransferred(long totalDataTransferred) { this.totalDataTransferred = totalDataTransferred; }
    }
}

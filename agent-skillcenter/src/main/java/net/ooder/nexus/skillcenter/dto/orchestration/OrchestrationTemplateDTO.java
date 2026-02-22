package net.ooder.nexus.skillcenter.dto.orchestration;

import java.util.List;
import java.util.Map;

public class OrchestrationTemplateDTO {
    private String templateId;
    private String name;
    private String description;
    private String version;
    private String status;
    private String category;
    private long createdAt;
    private long updatedAt;
    private String author;
    
    private List<SkillNode> skills;
    private List<DataFlow> dataFlows;
    private ExecutionConfig execution;
    private ErrorHandling errorHandling;
    private Map<String, Object> metadata;

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public List<SkillNode> getSkills() { return skills; }
    public void setSkills(List<SkillNode> skills) { this.skills = skills; }
    public List<DataFlow> getDataFlows() { return dataFlows; }
    public void setDataFlows(List<DataFlow> dataFlows) { this.dataFlows = dataFlows; }
    public ExecutionConfig getExecution() { return execution; }
    public void setExecution(ExecutionConfig execution) { this.execution = execution; }
    public ErrorHandling getErrorHandling() { return errorHandling; }
    public void setErrorHandling(ErrorHandling errorHandling) { this.errorHandling = errorHandling; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public static class SkillNode {
        private String nodeId;
        private String skillId;
        private String name;
        private String description;
        private Map<String, Object> parameters;
        private List<String> dependencies;
        private RetryPolicy retryPolicy;
        private TimeoutConfig timeout;
        private String executionMode;

        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public List<String> getDependencies() { return dependencies; }
        public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
        public RetryPolicy getRetryPolicy() { return retryPolicy; }
        public void setRetryPolicy(RetryPolicy retryPolicy) { this.retryPolicy = retryPolicy; }
        public TimeoutConfig getTimeout() { return timeout; }
        public void setTimeout(TimeoutConfig timeout) { this.timeout = timeout; }
        public String getExecutionMode() { return executionMode; }
        public void setExecutionMode(String executionMode) { this.executionMode = executionMode; }
    }

    public static class DataFlow {
        private String flowId;
        private String sourceNode;
        private String sourceOutput;
        private String targetNode;
        private String targetInput;
        private DataTransform transform;
        private String condition;

        public String getFlowId() { return flowId; }
        public void setFlowId(String flowId) { this.flowId = flowId; }
        public String getSourceNode() { return sourceNode; }
        public void setSourceNode(String sourceNode) { this.sourceNode = sourceNode; }
        public String getSourceOutput() { return sourceOutput; }
        public void setSourceOutput(String sourceOutput) { this.sourceOutput = sourceOutput; }
        public String getTargetNode() { return targetNode; }
        public void setTargetNode(String targetNode) { this.targetNode = targetNode; }
        public String getTargetInput() { return targetInput; }
        public void setTargetInput(String targetInput) { this.targetInput = targetInput; }
        public DataTransform getTransform() { return transform; }
        public void setTransform(DataTransform transform) { this.transform = transform; }
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
    }

    public static class ExecutionConfig {
        private String executionMode;
        private int maxConcurrency;
        private long defaultTimeout;
        private boolean continueOnError;
        private boolean enableCaching;
        private Map<String, Object> env;

        public String getExecutionMode() { return executionMode; }
        public void setExecutionMode(String executionMode) { this.executionMode = executionMode; }
        public int getMaxConcurrency() { return maxConcurrency; }
        public void setMaxConcurrency(int maxConcurrency) { this.maxConcurrency = maxConcurrency; }
        public long getDefaultTimeout() { return defaultTimeout; }
        public void setDefaultTimeout(long defaultTimeout) { this.defaultTimeout = defaultTimeout; }
        public boolean isContinueOnError() { return continueOnError; }
        public void setContinueOnError(boolean continueOnError) { this.continueOnError = continueOnError; }
        public boolean isEnableCaching() { return enableCaching; }
        public void setEnableCaching(boolean enableCaching) { this.enableCaching = enableCaching; }
        public Map<String, Object> getEnv() { return env; }
        public void setEnv(Map<String, Object> env) { this.env = env; }
    }

    public static class ErrorHandling {
        private String strategy;
        private int maxRetries;
        private long retryDelay;
        private String fallbackNode;
        private boolean notifyOnError;

        public String getStrategy() { return strategy; }
        public void setStrategy(String strategy) { this.strategy = strategy; }
        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        public long getRetryDelay() { return retryDelay; }
        public void setRetryDelay(long retryDelay) { this.retryDelay = retryDelay; }
        public String getFallbackNode() { return fallbackNode; }
        public void setFallbackNode(String fallbackNode) { this.fallbackNode = fallbackNode; }
        public boolean isNotifyOnError() { return notifyOnError; }
        public void setNotifyOnError(boolean notifyOnError) { this.notifyOnError = notifyOnError; }
    }

    public static class RetryPolicy {
        private int maxRetries;
        private long initialDelay;
        private long maxDelay;
        private double backoffMultiplier;
        private List<String> retryableErrors;

        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        public long getInitialDelay() { return initialDelay; }
        public void setInitialDelay(long initialDelay) { this.initialDelay = initialDelay; }
        public long getMaxDelay() { return maxDelay; }
        public void setMaxDelay(long maxDelay) { this.maxDelay = maxDelay; }
        public double getBackoffMultiplier() { return backoffMultiplier; }
        public void setBackoffMultiplier(double backoffMultiplier) { this.backoffMultiplier = backoffMultiplier; }
        public List<String> getRetryableErrors() { return retryableErrors; }
        public void setRetryableErrors(List<String> retryableErrors) { this.retryableErrors = retryableErrors; }
    }

    public static class TimeoutConfig {
        private long duration;
        private String action;

        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }

    public static class DataTransform {
        private String type;
        private String expression;
        private Map<String, Object> mapping;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getExpression() { return expression; }
        public void setExpression(String expression) { this.expression = expression; }
        public Map<String, Object> getMapping() { return mapping; }
        public void setMapping(Map<String, Object> mapping) { this.mapping = mapping; }
    }
}

package net.ooder.skillcenter.orchestration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Capability Orchestration Service Interface
 *
 * @author ooder Team
 * @since 2.0
 */
public interface CapabilityOrchestrationService {

    CompletableFuture<OrchestrationDefinition> createOrchestration(Map<String, Object> definition);

    CompletableFuture<OrchestrationDefinition> getOrchestration(String orchestrationId);

    CompletableFuture<Void> updateOrchestration(String orchestrationId, Map<String, Object> updates);

    CompletableFuture<Void> deleteOrchestration(String orchestrationId);

    CompletableFuture<List<OrchestrationDefinition>> listOrchestrations();

    CompletableFuture<OrchestrationExecution> executeOrchestration(String orchestrationId, Map<String, Object> input);

    CompletableFuture<OrchestrationExecution> getExecutionStatus(String executionId);

    CompletableFuture<Void> cancelExecution(String executionId);

    CompletableFuture<List<OrchestrationExecution>> getExecutionHistory(String orchestrationId, int limit);

    CompletableFuture<OrchestrationValidation> validateOrchestration(Map<String, Object> definition);
}

class OrchestrationDefinition {
    private String orchestrationId;
    private String name;
    private String description;
    private List<OrchestrationStep> steps;
    private Map<String, Object> variables;
    private String version;
    private long createTime;
    private long updateTime;

    public String getOrchestrationId() { return orchestrationId; }
    public void setOrchestrationId(String orchestrationId) { this.orchestrationId = orchestrationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<OrchestrationStep> getSteps() { return steps; }
    public void setSteps(List<OrchestrationStep> steps) { this.steps = steps; }
    public Map<String, Object> getVariables() { return variables; }
    public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
}

class OrchestrationStep {
    private String stepId;
    private String name;
    private String skillId;
    private Map<String, Object> input;
    private List<String> dependencies;
    private String condition;
    private int timeout;

    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public Map<String, Object> getInput() { return input; }
    public void setInput(Map<String, Object> input) { this.input = input; }
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
}

class OrchestrationExecution {
    private String executionId;
    private String orchestrationId;
    private String status;
    private Map<String, Object> input;
    private Map<String, Object> output;
    private List<StepExecution> stepExecutions;
    private long startTime;
    private long endTime;
    private long executionTime;

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    public String getOrchestrationId() { return orchestrationId; }
    public void setOrchestrationId(String orchestrationId) { this.orchestrationId = orchestrationId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, Object> getInput() { return input; }
    public void setInput(Map<String, Object> input) { this.input = input; }
    public Map<String, Object> getOutput() { return output; }
    public void setOutput(Map<String, Object> output) { this.output = output; }
    public List<StepExecution> getStepExecutions() { return stepExecutions; }
    public void setStepExecutions(List<StepExecution> stepExecutions) { this.stepExecutions = stepExecutions; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
}

class StepExecution {
    private String stepId;
    private String skillId;
    private String status;
    private Map<String, Object> input;
    private Map<String, Object> output;
    private long startTime;
    private long endTime;
    private String error;

    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, Object> getInput() { return input; }
    public void setInput(Map<String, Object> input) { this.input = input; }
    public Map<String, Object> getOutput() { return output; }
    public void setOutput(Map<String, Object> output) { this.output = output; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

class OrchestrationValidation {
    private boolean valid;
    private List<String> errors;
    private List<String> warnings;
    private List<String> missingSkills;

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
}

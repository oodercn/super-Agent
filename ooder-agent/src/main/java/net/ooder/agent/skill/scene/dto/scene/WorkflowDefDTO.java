package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class WorkflowDefDTO {
    private String workflowId;
    private String name;
    private String description;
    private List<TriggerConfigDTO> triggers;
    private List<WorkflowStepDefDTO> steps;
    private List<String> phases;
    private long timeout;

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<TriggerConfigDTO> getTriggers() { return triggers; }
    public void setTriggers(List<TriggerConfigDTO> triggers) { this.triggers = triggers; }
    public List<WorkflowStepDefDTO> getSteps() { return steps; }
    public void setSteps(List<WorkflowStepDefDTO> steps) { this.steps = steps; }
    public List<String> getPhases() { return phases; }
    public void setPhases(List<String> phases) { this.phases = phases; }
    public long getTimeout() { return timeout; }
    public void setTimeout(long timeout) { this.timeout = timeout; }
}

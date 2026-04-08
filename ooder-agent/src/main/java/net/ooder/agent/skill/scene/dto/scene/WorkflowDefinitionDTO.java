package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class WorkflowDefinitionDTO {
    private List<TriggerDefinitionDTO> triggers;
    private List<StepDefinitionDTO> steps;

    public List<TriggerDefinitionDTO> getTriggers() { return triggers; }
    public void setTriggers(List<TriggerDefinitionDTO> triggers) { this.triggers = triggers; }
    public List<StepDefinitionDTO> getSteps() { return steps; }
    public void setSteps(List<StepDefinitionDTO> steps) { this.steps = steps; }
}

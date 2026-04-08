package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;
import java.util.Map;

public class AutomationRuleDTO {
    private String name;
    private TriggerDefDTO trigger;
    private List<ActionDefDTO> actions;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TriggerDefDTO getTrigger() { return trigger; }
    public void setTrigger(TriggerDefDTO trigger) { this.trigger = trigger; }
    public List<ActionDefDTO> getActions() { return actions; }
    public void setActions(List<ActionDefDTO> actions) { this.actions = actions; }
}

package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;
import java.util.Map;

public class StepDefinitionDTO {
    private String id;
    private String name;
    private String type;
    private String capability;
    private String executor;
    private Long timeout;
    private String condition;
    private List<String> dependsOn;
    private Map<String, Object> input;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCapability() { return capability; }
    public void setCapability(String capability) { this.capability = capability; }
    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }
    public Long getTimeout() { return timeout; }
    public void setTimeout(Long timeout) { this.timeout = timeout; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public List<String> getDependsOn() { return dependsOn; }
    public void setDependsOn(List<String> dependsOn) { this.dependsOn = dependsOn; }
    public Map<String, Object> getInput() { return input; }
    public void setInput(Map<String, Object> input) { this.input = input; }
}

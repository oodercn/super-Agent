package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;
import java.util.Map;

public class WorkflowStepDefDTO {
    private String stepId;
    private String name;
    private String capability;
    private String executor;
    private String type;
    private Map<String, Object> input;
    private String output;
    private List<String> dependsOn;
    private boolean parallel;
    private long delay;
    private long timeout;

    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCapability() { return capability; }
    public void setCapability(String capability) { this.capability = capability; }
    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Map<String, Object> getInput() { return input; }
    public void setInput(Map<String, Object> input) { this.input = input; }
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
    public List<String> getDependsOn() { return dependsOn; }
    public void setDependsOn(List<String> dependsOn) { this.dependsOn = dependsOn; }
    public boolean isParallel() { return parallel; }
    public void setParallel(boolean parallel) { this.parallel = parallel; }
    public long getDelay() { return delay; }
    public void setDelay(long delay) { this.delay = delay; }
    public long getTimeout() { return timeout; }
    public void setTimeout(long timeout) { this.timeout = timeout; }
}

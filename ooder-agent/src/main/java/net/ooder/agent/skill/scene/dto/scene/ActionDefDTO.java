package net.ooder.mvp.skill.scene.dto.scene;

import java.util.Map;

public class ActionDefDTO {
    private String capability;
    private String target;
    private Map<String, Object> params;

    public String getCapability() { return capability; }
    public void setCapability(String capability) { this.capability = capability; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public Map<String, Object> getParams() { return params; }
    public void setParams(Map<String, Object> params) { this.params = params; }
}

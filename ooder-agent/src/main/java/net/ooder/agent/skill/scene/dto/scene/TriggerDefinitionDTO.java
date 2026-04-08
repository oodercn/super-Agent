package net.ooder.mvp.skill.scene.dto.scene;

import java.util.Map;

public class TriggerDefinitionDTO {
    private String type;
    private String cron;
    private String action;
    private Map<String, Object> config;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCron() { return cron; }
    public void setCron(String cron) { this.cron = cron; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
}

package net.ooder.mvp.skill.scene.dto.scene;

public class TriggerConfigDTO {
    private String type;
    private String cron;
    private String action;
    private Object condition;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCron() { return cron; }
    public void setCron(String cron) { this.cron = cron; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Object getCondition() { return condition; }
    public void setCondition(Object condition) { this.condition = condition; }
}

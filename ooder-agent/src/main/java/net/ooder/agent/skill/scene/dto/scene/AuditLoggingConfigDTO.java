package net.ooder.mvp.skill.scene.dto.scene;

public class AuditLoggingConfigDTO {
    private String level;
    private long retention;

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public long getRetention() { return retention; }
    public void setRetention(long retention) { this.retention = retention; }
}

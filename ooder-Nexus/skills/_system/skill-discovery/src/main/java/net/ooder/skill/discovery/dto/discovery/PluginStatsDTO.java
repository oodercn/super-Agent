package net.ooder.skill.discovery.dto.discovery;

public class PluginStatsDTO {
    private Integer installed;
    private Integer running;
    private Integer stopped;
    private Integer available;
    private Integer active;
    private Integer configured;
    private Integer loaded;
    private Integer dependency;
    private Long timestamp;

    public Integer getInstalled() { return installed; }
    public void setInstalled(Integer installed) { this.installed = installed; }
    public Integer getRunning() { return running; }
    public void setRunning(Integer running) { this.running = running; }
    public Integer getStopped() { return stopped; }
    public void setStopped(Integer stopped) { this.stopped = stopped; }
    public Integer getAvailable() { return available; }
    public void setAvailable(Integer available) { this.available = available; }
    public Integer getActive() { return active; }
    public void setActive(Integer active) { this.active = active; }
    public Integer getConfigured() { return configured; }
    public void setConfigured(Integer configured) { this.configured = configured; }
    public Integer getLoaded() { return loaded; }
    public void setLoaded(Integer loaded) { this.loaded = loaded; }
    public Integer getDependency() { return dependency; }
    public void setDependency(Integer dependency) { this.dependency = dependency; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}

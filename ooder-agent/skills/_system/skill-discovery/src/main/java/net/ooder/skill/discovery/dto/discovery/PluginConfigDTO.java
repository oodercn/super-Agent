package net.ooder.skill.discovery.dto.discovery;

public class PluginConfigDTO {
    private Boolean autoStart;
    private Boolean hotReload;
    private String pluginDirectory;
    private Integer maxPlugins;
    private Boolean checkUpdate;
    private String updateSource;

    public Boolean getAutoStart() { return autoStart; }
    public void setAutoStart(Boolean autoStart) { this.autoStart = autoStart; }
    public Boolean getHotReload() { return hotReload; }
    public void setHotReload(Boolean hotReload) { this.hotReload = hotReload; }
    public String getPluginDirectory() { return pluginDirectory; }
    public void setPluginDirectory(String pluginDirectory) { this.pluginDirectory = pluginDirectory; }
    public Integer getMaxPlugins() { return maxPlugins; }
    public void setMaxPlugins(Integer maxPlugins) { this.maxPlugins = maxPlugins; }
    public Boolean getCheckUpdate() { return checkUpdate; }
    public void setCheckUpdate(Boolean checkUpdate) { this.checkUpdate = checkUpdate; }
    public String getUpdateSource() { return updateSource; }
    public void setUpdateSource(String updateSource) { this.updateSource = updateSource; }
}

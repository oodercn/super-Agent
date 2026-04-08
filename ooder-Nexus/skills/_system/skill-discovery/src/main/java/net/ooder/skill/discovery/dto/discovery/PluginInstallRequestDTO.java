package net.ooder.skill.discovery.dto.discovery;

public class PluginInstallRequestDTO {
    private String pluginId;
    private String source;
    private String version;

    public String getPluginId() { return pluginId; }
    public void setPluginId(String pluginId) { this.pluginId = pluginId; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}

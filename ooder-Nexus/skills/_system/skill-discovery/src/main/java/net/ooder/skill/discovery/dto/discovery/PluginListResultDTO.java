package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class PluginListResultDTO {
    private List<PluginDTO> plugins;
    private Integer total;
    private Long timestamp;
    private String errorMessage;

    public List<PluginDTO> getPlugins() { return plugins; }
    public void setPlugins(List<PluginDTO> plugins) { this.plugins = plugins; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

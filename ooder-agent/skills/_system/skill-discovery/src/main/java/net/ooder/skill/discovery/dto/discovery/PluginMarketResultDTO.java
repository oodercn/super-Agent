package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class PluginMarketResultDTO {
    private List<PluginDTO> plugins;
    private Integer total;
    private Integer pageNum;
    private Integer pageSize;
    private Long timestamp;

    public List<PluginDTO> getPlugins() { return plugins; }
    public void setPlugins(List<PluginDTO> plugins) { this.plugins = plugins; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}

package net.ooder.skill.discovery.dto.discovery;

import java.util.List;
import java.util.Map;

public class DiscoveryResultDTO {
    
    private List<CapabilityDetailDTO> capabilities;
    private int total;
    private Map<String, Integer> stats;
    private String source;
    private long timestamp;

    public List<CapabilityDetailDTO> getCapabilities() { return capabilities; }
    public void setCapabilities(List<CapabilityDetailDTO> capabilities) { this.capabilities = capabilities; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public Map<String, Integer> getStats() { return stats; }
    public void setStats(Map<String, Integer> stats) { this.stats = stats; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

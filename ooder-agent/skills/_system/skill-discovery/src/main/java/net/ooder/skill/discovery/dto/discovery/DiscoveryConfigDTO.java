package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class DiscoveryConfigDTO {
    
    private boolean autoScan;
    private int scanInterval;
    private List<String> sources;

    public boolean isAutoScan() { return autoScan; }
    public void setAutoScan(boolean autoScan) { this.autoScan = autoScan; }
    public int getScanInterval() { return scanInterval; }
    public void setScanInterval(int scanInterval) { this.scanInterval = scanInterval; }
    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }
}


package net.ooder.sdk.southbound.adapter.model;

import java.util.List;

public class DiscoveryConfig {
    
    private String discoveryId;
    private String discoveryType;
    private int timeout;
    private int interval;
    private List<String> targetTypes;
    private boolean autoRefresh;
    private int maxResults;
    
    public String getDiscoveryId() { return discoveryId; }
    public void setDiscoveryId(String discoveryId) { this.discoveryId = discoveryId; }
    
    public String getDiscoveryType() { return discoveryType; }
    public void setDiscoveryType(String discoveryType) { this.discoveryType = discoveryType; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
    
    public List<String> getTargetTypes() { return targetTypes; }
    public void setTargetTypes(List<String> targetTypes) { this.targetTypes = targetTypes; }
    
    public boolean isAutoRefresh() { return autoRefresh; }
    public void setAutoRefresh(boolean autoRefresh) { this.autoRefresh = autoRefresh; }
    
    public int getMaxResults() { return maxResults; }
    public void setMaxResults(int maxResults) { this.maxResults = maxResults; }
}

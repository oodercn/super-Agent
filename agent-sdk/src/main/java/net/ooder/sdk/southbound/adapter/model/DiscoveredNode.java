
package net.ooder.sdk.southbound.adapter.model;

import java.util.List;
import java.util.Map;

public class DiscoveredNode {
    
    private String nodeId;
    private String nodeName;
    private String nodeType;
    private String address;
    private int port;
    private long discoveredTime;
    private long lastSeenTime;
    private List<String> capabilities;
    private Map<String, Object> metadata;
    private String status;
    
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    
    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public long getDiscoveredTime() { return discoveredTime; }
    public void setDiscoveredTime(long discoveredTime) { this.discoveredTime = discoveredTime; }
    
    public long getLastSeenTime() { return lastSeenTime; }
    public void setLastSeenTime(long lastSeenTime) { this.lastSeenTime = lastSeenTime; }
    
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public boolean isOnline() {
        return "online".equals(status);
    }
    
    public boolean hasCapability(String capability) {
        return capabilities != null && capabilities.contains(capability);
    }
}

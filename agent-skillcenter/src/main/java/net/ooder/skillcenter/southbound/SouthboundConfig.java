package net.ooder.skillcenter.southbound;

import java.util.Map;

public class SouthboundConfig {
    private String nodeId;
    private String nodeName;
    private int udpPort = 9000;
    private int discoveryInterval = 30000;
    private boolean autoDiscovery = true;
    private String storagePath;
    private Map<String, Object> properties;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }

    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }

    public int getUdpPort() { return udpPort; }
    public void setUdpPort(int udpPort) { this.udpPort = udpPort; }

    public int getDiscoveryInterval() { return discoveryInterval; }
    public void setDiscoveryInterval(int discoveryInterval) { this.discoveryInterval = discoveryInterval; }

    public boolean isAutoDiscovery() { return autoDiscovery; }
    public void setAutoDiscovery(boolean autoDiscovery) { this.autoDiscovery = autoDiscovery; }

    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}

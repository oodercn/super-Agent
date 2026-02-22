package net.ooder.nexus.service;

import java.util.Map;

/**
 * Nexus 配置
 */
public class NexusConfig {
    
    private String nodeId;
    private String nodeName;
    private String networkId;
    private int udpPort;
    private boolean p2pEnabled;
    private boolean offlineModeEnabled;
    private Map<String, Object> extensions;

    public NexusConfig() {}

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }
    
    public String getNetworkId() { return networkId; }
    public void setNetworkId(String networkId) { this.networkId = networkId; }
    
    public int getUdpPort() { return udpPort; }
    public void setUdpPort(int udpPort) { this.udpPort = udpPort; }
    
    public boolean isP2pEnabled() { return p2pEnabled; }
    public void setP2pEnabled(boolean p2pEnabled) { this.p2pEnabled = p2pEnabled; }
    
    public boolean isOfflineModeEnabled() { return offlineModeEnabled; }
    public void setOfflineModeEnabled(boolean offlineModeEnabled) { this.offlineModeEnabled = offlineModeEnabled; }
    
    public Map<String, Object> getExtensions() { return extensions; }
    public void setExtensions(Map<String, Object> extensions) { this.extensions = extensions; }
}

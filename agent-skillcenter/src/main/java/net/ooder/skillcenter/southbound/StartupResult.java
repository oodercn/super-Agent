package net.ooder.skillcenter.southbound;

import java.util.List;

public class StartupResult {
    private boolean success;
    private String nodeId;
    private String mode;
    private List<String> discoveredPeers;
    private String errorMessage;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public List<String> getDiscoveredPeers() { return discoveredPeers; }
    public void setDiscoveredPeers(List<String> discoveredPeers) { this.discoveredPeers = discoveredPeers; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

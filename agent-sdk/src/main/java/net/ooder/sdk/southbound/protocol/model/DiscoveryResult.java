package net.ooder.sdk.southbound.protocol.model;

import java.util.List;

public class DiscoveryResult {
    
    private String requestId;
    private boolean success;
    private List<PeerInfo> peers;
    private PeerInfo mcp;
    private String errorMessage;
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public List<PeerInfo> getPeers() { return peers; }
    public void setPeers(List<PeerInfo> peers) { this.peers = peers; }
    
    public PeerInfo getMcp() { return mcp; }
    public void setMcp(PeerInfo mcp) { this.mcp = mcp; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

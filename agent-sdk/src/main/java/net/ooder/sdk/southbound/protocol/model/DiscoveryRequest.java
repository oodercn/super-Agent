package net.ooder.sdk.southbound.protocol.model;

public class DiscoveryRequest {
    
    private String requestId;
    private DiscoveryType type;
    private int timeout;
    private String targetNetwork;
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public DiscoveryType getType() { return type; }
    public void setType(DiscoveryType type) { this.type = type; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public String getTargetNetwork() { return targetNetwork; }
    public void setTargetNetwork(String targetNetwork) { this.targetNetwork = targetNetwork; }
}

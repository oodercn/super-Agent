package net.ooder.sdk.capability.model;

public class NodeDistStatus {
    
    private String nodeId;
    private NodeDistState state;
    private long startTime;
    private long endTime;
    private String errorMessage;
    
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public NodeDistState getState() { return state; }
    public void setState(NodeDistState state) { this.state = state; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    private long deliveredTime;
    
    public long getDeliveredTime() { return deliveredTime; }
    public void setDeliveredTime(long deliveredTime) { this.deliveredTime = deliveredTime; }
}

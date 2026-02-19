package net.ooder.sdk.capability.model;

import java.util.List;

public class DistStatus {
    
    private String distId;
    private String specId;
    private DistState state;
    private int totalNodes;
    private int completedNodes;
    private int failedNodes;
    private long startTime;
    private long endTime;
    private List<NodeDistStatus> nodeStatuses;
    
    public String getDistId() { return distId; }
    public void setDistId(String distId) { this.distId = distId; }
    
    public String getSpecId() { return specId; }
    public void setSpecId(String specId) { this.specId = specId; }
    
    public DistState getState() { return state; }
    public void setState(DistState state) { this.state = state; }
    
    public int getTotalNodes() { return totalNodes; }
    public void setTotalNodes(int totalNodes) { this.totalNodes = totalNodes; }
    
    public int getCompletedNodes() { return completedNodes; }
    public void setCompletedNodes(int completedNodes) { this.completedNodes = completedNodes; }
    
    public int getFailedNodes() { return failedNodes; }
    public void setFailedNodes(int failedNodes) { this.failedNodes = failedNodes; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public List<NodeDistStatus> getNodeStatuses() { return nodeStatuses; }
    public void setNodeStatuses(List<NodeDistStatus> nodeStatuses) { this.nodeStatuses = nodeStatuses; }
}

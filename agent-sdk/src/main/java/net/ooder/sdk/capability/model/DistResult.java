package net.ooder.sdk.capability.model;

import java.util.Map;

public class DistResult {
    
    private String distId;
    private boolean success;
    private int totalNodes;
    private int successNodes;
    private int failedNodes;
    private Map<String, String> nodeResults;
    private String errorMessage;
    
    public String getDistId() { return distId; }
    public void setDistId(String distId) { this.distId = distId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public int getTotalNodes() { return totalNodes; }
    public void setTotalNodes(int totalNodes) { this.totalNodes = totalNodes; }
    
    public int getSuccessNodes() { return successNodes; }
    public void setSuccessNodes(int successNodes) { this.successNodes = successNodes; }
    
    public int getFailedNodes() { return failedNodes; }
    public void setFailedNodes(int failedNodes) { this.failedNodes = failedNodes; }
    
    public Map<String, String> getNodeResults() { return nodeResults; }
    public void setNodeResults(Map<String, String> nodeResults) { this.nodeResults = nodeResults; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

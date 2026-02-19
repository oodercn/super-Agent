package net.ooder.sdk.capability.model;

public class CapabilityQuery {
    
    private String specId;
    private String nodeId;
    private CapabilityState state;
    private int page;
    private int pageSize;
    
    public String getSpecId() { return specId; }
    public void setSpecId(String specId) { this.specId = specId; }
    
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    
    public CapabilityState getState() { return state; }
    public void setState(CapabilityState state) { this.state = state; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}

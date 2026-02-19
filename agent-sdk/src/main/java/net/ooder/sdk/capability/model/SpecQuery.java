package net.ooder.sdk.capability.model;

import java.util.List;

public class SpecQuery {
    
    private CapabilityType type;
    private String status;
    private String keyword;
    private List<String> tags;
    private int page;
    private int pageSize;
    
    public CapabilityType getType() { return type; }
    public void setType(CapabilityType type) { this.type = type; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}

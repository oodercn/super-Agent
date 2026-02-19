package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class ChainDef {
    
    private String chainId;
    private String name;
    private String description;
    private List<ChainLink> links;
    private ChainErrorHandling errorHandling;
    private long createdTime;
    private ChainStatus status;
    
    public String getChainId() { return chainId; }
    public void setChainId(String chainId) { this.chainId = chainId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<ChainLink> getLinks() { return links; }
    public void setLinks(List<ChainLink> links) { this.links = links; }
    
    public ChainErrorHandling getErrorHandling() { return errorHandling; }
    public void setErrorHandling(ChainErrorHandling errorHandling) { this.errorHandling = errorHandling; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public ChainStatus getStatus() { return status; }
    public void setStatus(ChainStatus status) { this.status = status; }
}

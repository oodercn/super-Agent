package net.ooder.sdk.capability.model;

import java.util.List;

public class ChainConfig {
    
    private String name;
    private String description;
    private List<ChainLink> links;
    private ChainErrorHandling errorHandling;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<ChainLink> getLinks() { return links; }
    public void setLinks(List<ChainLink> links) { this.links = links; }
    
    public ChainErrorHandling getErrorHandling() { return errorHandling; }
    public void setErrorHandling(ChainErrorHandling errorHandling) { this.errorHandling = errorHandling; }
}

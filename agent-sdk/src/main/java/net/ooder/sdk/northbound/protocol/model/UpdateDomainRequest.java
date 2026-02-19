package net.ooder.sdk.northbound.protocol.model;

import java.util.Map;

public class UpdateDomainRequest {
    
    private String domainName;
    private Map<String, Object> config;
    
    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
}

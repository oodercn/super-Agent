
package net.ooder.sdk.service.network.link;

public enum LinkStatus {
    
    ACTIVE("active", "Link is active and healthy"),
    INACTIVE("inactive", "Link is inactive"),
    DEGRADED("degraded", "Link is degraded but functional"),
    FAILED("failed", "Link has failed");
    
    private final String code;
    private final String description;
    
    LinkStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
}

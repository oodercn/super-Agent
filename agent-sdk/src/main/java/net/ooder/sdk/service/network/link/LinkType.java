
package net.ooder.sdk.service.network.link;

public enum LinkType {
    
    DIRECT("direct", "Direct connection"),
    RELAY("relay", "Relayed through intermediate node"),
    TUNNEL("tunnel", "Encrypted tunnel"),
    MULTICAST("multicast", "Multicast group");
    
    private final String code;
    private final String description;
    
    LinkType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
}

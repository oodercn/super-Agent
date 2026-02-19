
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;

public class Context implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final IdentitySnapshot identity;
    private final LocationSnapshot location;
    private final ResourceSnapshot resource;
    private final long timestamp;
    
    public Context(IdentitySnapshot identity, LocationSnapshot location, ResourceSnapshot resource) {
        this.identity = identity;
        this.location = location;
        this.resource = resource;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Context(IdentityInfo identity, LocationInfo location, ResourceInfo resource) {
        this.identity = identity != null ? identity.snapshot() : null;
        this.location = location != null ? location.snapshot() : null;
        this.resource = resource != null ? resource.snapshot() : null;
        this.timestamp = System.currentTimeMillis();
    }
    
    public IdentitySnapshot getIdentity() { return identity; }
    public LocationSnapshot getLocation() { return location; }
    public ResourceSnapshot getResource() { return resource; }
    public long getTimestamp() { return timestamp; }
    
    public String getAgentId() {
        return identity != null ? identity.getAgentId() : null;
    }
    
    public String getAddress() {
        return location != null ? location.getAddress() : null;
    }
    
    public String getStatus() {
        return resource != null ? resource.getStatus() : null;
    }
    
    public boolean hasCapability(String capability) {
        return resource != null && resource.hasCapability(capability);
    }
    
    public boolean isValid() {
        return identity != null && identity.getAgentId() != null;
    }
    
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Context[");
        sb.append("agent=").append(getAgentId() != null ? getAgentId() : "unknown");
        sb.append(", addr=").append(getAddress() != null ? getAddress() : "unknown");
        sb.append(", status=").append(getStatus() != null ? getStatus() : "unknown");
        sb.append("]");
        return sb.toString();
    }
    
    public static Context empty() {
        return new Context((IdentitySnapshot) null, null, null);
    }
}

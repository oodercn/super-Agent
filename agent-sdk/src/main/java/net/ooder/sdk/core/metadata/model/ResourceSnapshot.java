
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResourceSnapshot implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final String resourceType;
    private final String resourceId;
    private final String resourceName;
    private final String description;
    private final String version;
    private final String status;
    private final String action;
    private final List<String> capabilities;
    private final List<String> dependencies;
    private final Set<String> tags;
    private final Map<String, Object> properties;
    private final Map<String, Object> metrics;
    private final long snapshotTime;
    
    public ResourceSnapshot(ResourceInfo resource) {
        this.resourceType = resource.getResourceType();
        this.resourceId = resource.getResourceId();
        this.resourceName = resource.getResourceName();
        this.description = resource.getDescription();
        this.version = resource.getVersion();
        this.status = resource.getStatus();
        this.action = resource.getAction();
        this.capabilities = new ArrayList<>(resource.getCapabilities());
        this.dependencies = new ArrayList<>(resource.getDependencies());
        this.tags = new HashSet<>(resource.getTags());
        this.properties = new HashMap<>(resource.getProperties());
        this.metrics = new HashMap<>(resource.getMetrics());
        this.snapshotTime = System.currentTimeMillis();
    }
    
    public String getResourceType() { return resourceType; }
    public String getResourceId() { return resourceId; }
    public String getResourceName() { return resourceName; }
    public String getDescription() { return description; }
    public String getVersion() { return version; }
    public String getStatus() { return status; }
    public String getAction() { return action; }
    public List<String> getCapabilities() { return new ArrayList<>(capabilities); }
    public List<String> getDependencies() { return new ArrayList<>(dependencies); }
    public Set<String> getTags() { return new HashSet<>(tags); }
    public Map<String, Object> getProperties() { return new HashMap<>(properties); }
    public Map<String, Object> getMetrics() { return new HashMap<>(metrics); }
    public long getSnapshotTime() { return snapshotTime; }
    
    public boolean hasCapability(String capability) {
        return capabilities.contains(capability);
    }
    
    public boolean isActive() { return "ACTIVE".equalsIgnoreCase(status) || "RUNNING".equalsIgnoreCase(status); }
    public boolean isIdle() { return "IDLE".equalsIgnoreCase(status) || "INITIALIZED".equalsIgnoreCase(status); }
    public boolean isError() { return "ERROR".equalsIgnoreCase(status) || "FAILED".equalsIgnoreCase(status); }
    
    public String getResourceKey() {
        return resourceId != null ? resourceId : "unknown";
    }
}

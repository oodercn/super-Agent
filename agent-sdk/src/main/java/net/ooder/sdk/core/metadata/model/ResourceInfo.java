
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResourceInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String resourceType;
    private String resourceId;
    private String resourceName;
    private String description;
    private String version;
    private String status;
    private String action;
    private List<String> capabilities;
    private List<String> dependencies;
    private Set<String> tags;
    private Map<String, Object> properties;
    private Map<String, Object> metrics;
    
    public ResourceInfo() {
        this.capabilities = new ArrayList<>();
        this.dependencies = new ArrayList<>();
        this.tags = new HashSet<>();
        this.properties = new HashMap<>();
        this.metrics = new HashMap<>();
        this.status = "INITIALIZED";
    }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { 
        this.capabilities = capabilities != null ? new ArrayList<>(capabilities) : new ArrayList<>(); 
    }
    
    public void addCapability(String capability) {
        if (capability != null && !capabilities.contains(capability)) {
            capabilities.add(capability);
        }
    }
    
    public boolean hasCapability(String capability) {
        return capabilities.contains(capability);
    }
    
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { 
        this.dependencies = dependencies != null ? new ArrayList<>(dependencies) : new ArrayList<>(); 
    }
    
    public void addDependency(String dependency) {
        if (dependency != null && !dependencies.contains(dependency)) {
            dependencies.add(dependency);
        }
    }
    
    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { 
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>(); 
    }
    
    public void addTag(String tag) {
        if (tag != null) tags.add(tag);
    }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { 
        this.properties = properties != null ? new HashMap<>(properties) : new HashMap<>(); 
    }
    
    public void setProperty(String key, Object value) { properties.put(key, value); }
    public Object getProperty(String key) { return properties.get(key); }
    
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, T defaultValue) {
        Object value = properties.get(key);
        if (value == null) return defaultValue;
        try {
            return (T) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
    
    public String getPropertyAsString(String key) {
        Object value = properties.get(key);
        return value != null ? value.toString() : null;
    }
    
    public int getPropertyAsInt(String key, int defaultValue) {
        Object value = properties.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    public long getPropertyAsLong(String key, long defaultValue) {
        Object value = properties.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return defaultValue;
    }
    
    public boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        Object value = properties.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { 
        this.metrics = metrics != null ? new HashMap<>(metrics) : new HashMap<>(); 
    }
    
    public void setMetric(String key, Object value) { metrics.put(key, value); }
    public Object getMetric(String key) { return metrics.get(key); }
    
    public double getMetricAsDouble(String key, double defaultValue) {
        Object value = metrics.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    public boolean isActive() { return "ACTIVE".equalsIgnoreCase(status) || "RUNNING".equalsIgnoreCase(status); }
    public boolean isIdle() { return "IDLE".equalsIgnoreCase(status) || "INITIALIZED".equalsIgnoreCase(status); }
    public boolean isError() { return "ERROR".equalsIgnoreCase(status) || "FAILED".equalsIgnoreCase(status); }
    public boolean isBusy() { return "BUSY".equalsIgnoreCase(status); }
    
    public void markActive() { this.status = "ACTIVE"; }
    public void markIdle() { this.status = "IDLE"; }
    public void markError() { this.status = "ERROR"; }
    public void markBusy() { this.status = "BUSY"; }
    
    public String getResourceKey() {
        return resourceId != null ? resourceId : "unknown";
    }
    
    public ResourceSnapshot snapshot() {
        return new ResourceSnapshot(this);
    }
    
    public static ResourceInfo of(String resourceType, String resourceId) {
        ResourceInfo resource = new ResourceInfo();
        resource.setResourceType(resourceType);
        resource.setResourceId(resourceId);
        return resource;
    }
    
    public static ResourceInfo of(String resourceType, String resourceId, String status) {
        ResourceInfo resource = new ResourceInfo();
        resource.setResourceType(resourceType);
        resource.setResourceId(resourceId);
        resource.setStatus(status);
        return resource;
    }
}

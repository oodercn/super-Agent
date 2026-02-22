package net.ooder.skillcenter.sdk.cloud;

import java.util.Map;

public class CloudInstance {
    private String id;
    private String name;
    private String provider;
    private String providerType;
    private String region;
    private String status;
    private int replicas;
    private String endpoint;
    private long createdAt;
    private Map<String, String> labels;
    private ResourceConfig resources;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getReplicas() { return replicas; }
    public void setReplicas(int replicas) { this.replicas = replicas; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public Map<String, String> getLabels() { return labels; }
    public void setLabels(Map<String, String> labels) { this.labels = labels; }
    public ResourceConfig getResources() { return resources; }
    public void setResources(ResourceConfig resources) { this.resources = resources; }
}

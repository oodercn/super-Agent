package net.ooder.skillcenter.sdk.cloud;

import java.util.List;
import java.util.Map;

public class CloudHostingConfig {
    private String provider;
    private String providerType;
    private String region;
    private String resourceGroup;
    private String instanceName;
    private ContainerConfig container;
    private ResourceConfig resources;
    private NetworkConfig network;
    private ScaleConfig scaling;
    private Map<String, String> labels;
    private Map<String, String> annotations;

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getResourceGroup() { return resourceGroup; }
    public void setResourceGroup(String resourceGroup) { this.resourceGroup = resourceGroup; }
    public String getInstanceName() { return instanceName; }
    public void setInstanceName(String instanceName) { this.instanceName = instanceName; }
    public ContainerConfig getContainer() { return container; }
    public void setContainer(ContainerConfig container) { this.container = container; }
    public ResourceConfig getResources() { return resources; }
    public void setResources(ResourceConfig resources) { this.resources = resources; }
    public NetworkConfig getNetwork() { return network; }
    public void setNetwork(NetworkConfig network) { this.network = network; }
    public ScaleConfig getScaling() { return scaling; }
    public void setScaling(ScaleConfig scaling) { this.scaling = scaling; }
    public Map<String, String> getLabels() { return labels; }
    public void setLabels(Map<String, String> labels) { this.labels = labels; }
    public Map<String, String> getAnnotations() { return annotations; }
    public void setAnnotations(Map<String, String> annotations) { this.annotations = annotations; }
}

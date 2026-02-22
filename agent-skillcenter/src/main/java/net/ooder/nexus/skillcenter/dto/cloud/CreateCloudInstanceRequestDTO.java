package net.ooder.nexus.skillcenter.dto.cloud;

public class CreateCloudInstanceRequestDTO {
    private String provider;
    private String providerType;
    private String region;
    private String instanceName;
    private String image;
    private double cpuLimit;
    private long memoryLimit;
    private int minReplicas;
    private int maxReplicas;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(double cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public int getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(int minReplicas) {
        this.minReplicas = minReplicas;
    }

    public int getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(int maxReplicas) {
        this.maxReplicas = maxReplicas;
    }
}

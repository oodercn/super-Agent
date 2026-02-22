package net.ooder.nexus.skillcenter.dto.cloud;

public class CloudInstanceRequestDTO {
    private String provider;
    private String instanceId;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}

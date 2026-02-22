package net.ooder.nexus.skillcenter.dto.cloud;

public class ScaleCloudInstanceRequestDTO extends CloudInstanceRequestDTO {
    private int replicas;

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }
}

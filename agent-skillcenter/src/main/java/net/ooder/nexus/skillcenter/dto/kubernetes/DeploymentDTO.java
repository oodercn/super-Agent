package net.ooder.nexus.skillcenter.dto.kubernetes;

public class DeploymentDTO {
    private String name;
    private String namespace;
    private String cluster;
    private String image;
    private int replicas;
    private int readyReplicas;
    private int availableReplicas;
    private String status;
    private String strategy;
    private long createdAt;
    private long updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public int getReadyReplicas() {
        return readyReplicas;
    }

    public void setReadyReplicas(int readyReplicas) {
        this.readyReplicas = readyReplicas;
    }

    public int getAvailableReplicas() {
        return availableReplicas;
    }

    public void setAvailableReplicas(int availableReplicas) {
        this.availableReplicas = availableReplicas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

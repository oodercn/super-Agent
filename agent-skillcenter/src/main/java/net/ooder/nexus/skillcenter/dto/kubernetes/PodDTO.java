package net.ooder.nexus.skillcenter.dto.kubernetes;

import java.util.List;

public class PodDTO {
    private String name;
    private String namespace;
    private String cluster;
    private String deployment;
    private String nodeName;
    private String status;
    private String podIP;
    private List<ContainerInfo> containers;
    private long createdAt;

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

    public String getDeployment() {
        return deployment;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPodIP() {
        return podIP;
    }

    public void setPodIP(String podIP) {
        this.podIP = podIP;
    }

    public List<ContainerInfo> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerInfo> containers) {
        this.containers = containers;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public static class ContainerInfo {
        private String name;
        private String image;
        private String status;
        private int restartCount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getRestartCount() {
            return restartCount;
        }

        public void setRestartCount(int restartCount) {
            this.restartCount = restartCount;
        }
    }
}

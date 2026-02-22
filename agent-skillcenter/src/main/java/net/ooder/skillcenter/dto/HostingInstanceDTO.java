package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 托管实例DTO - 符合v0.7.0协议规范
 */
public class HostingInstanceDTO {
    
    private String id;
    private String name;
    private String skillId;
    private String skillName;
    private String status;
    private String endpoint;
    private String host;
    private int port;
    private String protocol;
    private String deploymentMode;
    private int maxInstances;
    private int currentInstances;
    private double cpuLimit;
    private long memoryLimit;
    private Date createdAt;
    private Date updatedAt;
    private Date lastHeartbeat;
    private String healthStatus;
    private String owner;
    private String description;

    public HostingInstanceDTO() {}

    public static HostingInstanceDTO of(String id, String name, String skillId) {
        HostingInstanceDTO dto = new HostingInstanceDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setSkillId(skillId);
        dto.setStatus("stopped");
        dto.setDeploymentMode("local-deployed");
        dto.setMaxInstances(1);
        dto.setCurrentInstances(0);
        dto.setHealthStatus("unknown");
        dto.setCreatedAt(new Date());
        return dto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    public String getDeploymentMode() { return deploymentMode; }
    public void setDeploymentMode(String deploymentMode) { this.deploymentMode = deploymentMode; }

    public int getMaxInstances() { return maxInstances; }
    public void setMaxInstances(int maxInstances) { this.maxInstances = maxInstances; }

    public int getCurrentInstances() { return currentInstances; }
    public void setCurrentInstances(int currentInstances) { this.currentInstances = currentInstances; }

    public double getCpuLimit() { return cpuLimit; }
    public void setCpuLimit(double cpuLimit) { this.cpuLimit = cpuLimit; }

    public long getMemoryLimit() { return memoryLimit; }
    public void setMemoryLimit(long memoryLimit) { this.memoryLimit = memoryLimit; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public Date getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(Date lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

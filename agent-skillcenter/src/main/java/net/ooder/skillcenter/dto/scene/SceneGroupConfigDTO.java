package net.ooder.skillcenter.dto.scene;

public class SceneGroupConfigDTO {
    private String name;
    private Integer maxMembers;
    private String securityPolicy;
    private Long heartbeatInterval;
    private Long heartbeatTimeout;

    public SceneGroupConfigDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getMaxMembers() { return maxMembers; }
    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }
    public String getSecurityPolicy() { return securityPolicy; }
    public void setSecurityPolicy(String securityPolicy) { this.securityPolicy = securityPolicy; }
    public Long getHeartbeatInterval() { return heartbeatInterval; }
    public void setHeartbeatInterval(Long heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
    public Long getHeartbeatTimeout() { return heartbeatTimeout; }
    public void setHeartbeatTimeout(Long heartbeatTimeout) { this.heartbeatTimeout = heartbeatTimeout; }
}

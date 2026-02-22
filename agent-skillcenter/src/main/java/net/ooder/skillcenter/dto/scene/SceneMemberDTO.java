package net.ooder.skillcenter.dto.scene;

public class SceneMemberDTO {
    private String agentId;
    private String agentName;
    private String role;
    private String sceneGroupId;
    private long joinTime;
    private long lastHeartbeat;
    private String status;

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public long getJoinTime() { return joinTime; }
    public void setJoinTime(long joinTime) { this.joinTime = joinTime; }
    public long getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

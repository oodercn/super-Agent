package net.ooder.skillcenter.dto.scene;

public class SceneMemberInfoDTO {
    private String memberId;
    private String sceneGroupId;
    private String agentId;
    private String role;
    private String status;
    private Long joinedAt;

    public SceneMemberInfoDTO() {}

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Long joinedAt) { this.joinedAt = joinedAt; }
}

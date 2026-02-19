
package net.ooder.sdk.api.scene;

import net.ooder.sdk.common.enums.MemberRole;

public class SceneMember {
    
    private String agentId;
    private String agentName;
    private String endpoint;
    private MemberRole role;
    private String sceneGroupId;
    private String status;
    private long joinTime;
    private long lastHeartbeat;
    private int heartbeatMissed;
    private double evaluationScore;
    
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public MemberRole getRole() {
        return role;
    }
    
    public void setRole(MemberRole role) {
        this.role = role;
    }
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public long getJoinTime() {
        return joinTime;
    }
    
    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }
    
    public long getLastHeartbeat() {
        return lastHeartbeat;
    }
    
    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
    
    public int getHeartbeatMissed() {
        return heartbeatMissed;
    }
    
    public void setHeartbeatMissed(int heartbeatMissed) {
        this.heartbeatMissed = heartbeatMissed;
    }
    
    public double getEvaluationScore() {
        return evaluationScore;
    }
    
    public void setEvaluationScore(double evaluationScore) {
        this.evaluationScore = evaluationScore;
    }
    
    public boolean isPrimary() {
        return role == MemberRole.PRIMARY;
    }
    
    public boolean isBackup() {
        return role == MemberRole.BACKUP;
    }
    
    public boolean isHealthy() {
        return "online".equals(status) && heartbeatMissed < 3;
    }
}

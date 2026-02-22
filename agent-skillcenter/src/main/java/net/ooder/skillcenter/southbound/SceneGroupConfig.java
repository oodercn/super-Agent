package net.ooder.skillcenter.southbound;

public class SceneGroupConfig {
    private String sceneId;
    private String groupName;
    private int minMembers = 1;
    private int maxMembers = 10;
    private int heartbeatInterval = 30000;
    private String inviteCode;

    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public int getMinMembers() { return minMembers; }
    public void setMinMembers(int minMembers) { this.minMembers = minMembers; }

    public int getMaxMembers() { return maxMembers; }
    public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }

    public int getHeartbeatInterval() { return heartbeatInterval; }
    public void setHeartbeatInterval(int heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }

    public String getInviteCode() { return inviteCode; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
}

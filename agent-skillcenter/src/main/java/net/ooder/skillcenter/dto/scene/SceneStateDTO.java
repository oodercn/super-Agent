package net.ooder.skillcenter.dto.scene;

import java.util.List;

public class SceneStateDTO {
    private String sceneId;
    private boolean active;
    private int memberCount;
    private List<String> installedSkills;
    private long createTime;
    private long lastUpdateTime;

    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public List<String> getInstalledSkills() { return installedSkills; }
    public void setInstalledSkills(List<String> installedSkills) { this.installedSkills = installedSkills; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
}

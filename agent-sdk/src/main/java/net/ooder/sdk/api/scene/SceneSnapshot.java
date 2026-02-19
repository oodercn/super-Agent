
package net.ooder.sdk.api.scene;

import java.util.List;
import java.util.Map;

public class SceneSnapshot {
    
    private String sceneId;
    private String snapshotId;
    private long createTime;
    private List<String> skills;
    private List<String> members;
    private Map<String, Object> config;
    private Map<String, Object> state;
    private String version;
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getSnapshotId() {
        return snapshotId;
    }
    
    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public List<String> getSkills() {
        return skills;
    }
    
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    
    public List<String> getMembers() {
        return members;
    }
    
    public void setMembers(List<String> members) {
        this.members = members;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
    
    public Map<String, Object> getState() {
        return state;
    }
    
    public void setState(Map<String, Object> state) {
        this.state = state;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
}

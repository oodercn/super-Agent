package net.ooder.nexus.skillcenter.dto.skill;

import java.util.List;

public class DiscoveryScanDTO {
    private String source;
    private int limit;
    private String sceneId;
    private List<String> capabilities;
    private String owner;
    private String skillsPath;
    private String repoName;

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getSkillsPath() { return skillsPath; }
    public void setSkillsPath(String skillsPath) { this.skillsPath = skillsPath; }
    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
}

package net.ooder.skillcenter.southbound;

import java.util.List;

public class InstalledSkill {
    private String skillId;
    private String name;
    private String version;
    private String description;
    private long installedAt;
    private String source;
    private List<String> capabilities;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getInstalledAt() { return installedAt; }
    public void setInstalledAt(long installedAt) { this.installedAt = installedAt; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
}

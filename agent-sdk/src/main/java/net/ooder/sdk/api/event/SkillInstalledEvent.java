package net.ooder.sdk.api.event;

public class SkillInstalledEvent extends Event {
    
    private String skillId;
    private String skillName;
    private String version;
    
    public SkillInstalledEvent() {
        super();
    }
    
    public SkillInstalledEvent(String skillId, String skillName, String version) {
        super("SkillPackageManager");
        this.skillId = skillId;
        this.skillName = skillName;
        this.version = version;
    }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}

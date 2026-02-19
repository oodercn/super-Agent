package net.ooder.sdk.api.event;

public class SkillUninstalledEvent extends Event {
    
    private String skillId;
    
    public SkillUninstalledEvent() {
        super();
    }
    
    public SkillUninstalledEvent(String skillId) {
        super("SkillPackageManager");
        this.skillId = skillId;
    }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
}

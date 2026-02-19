package net.ooder.sdk.api.event;

public class SkillStartedEvent extends Event {
    
    private String skillId;
    
    public SkillStartedEvent() {
        super();
    }
    
    public SkillStartedEvent(String skillId) {
        super("SkillPackageManager");
        this.skillId = skillId;
    }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
}

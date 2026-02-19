package net.ooder.sdk.api.event;

public class SkillStoppedEvent extends Event {
    
    private String skillId;
    private String reason;
    
    public SkillStoppedEvent() {
        super();
    }
    
    public SkillStoppedEvent(String skillId, String reason) {
        super("SkillPackageManager");
        this.skillId = skillId;
        this.reason = reason;
    }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

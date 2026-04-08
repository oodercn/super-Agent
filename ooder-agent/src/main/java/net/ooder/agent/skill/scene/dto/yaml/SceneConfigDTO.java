package net.ooder.mvp.skill.scene.dto.yaml;

public class SceneConfigDTO {
    
    private String type;
    private String visibility;
    private String participantMode;
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    
    public String getParticipantMode() { return participantMode; }
    public void setParticipantMode(String participantMode) { this.participantMode = participantMode; }
}

package net.ooder.mvp.skill.scene.dto.install;

import net.ooder.mvp.skill.scene.dto.yaml.SkillMetadataDTO;
import java.nio.file.Path;

public class DownloadResultDTO {
    
    private boolean success;
    private String skillId;
    private String message;
    private Path skillPath;
    private SkillMetadataDTO skillMetadata;
    
    public DownloadResultDTO() {
    }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Path getSkillPath() { return skillPath; }
    public void setSkillPath(Path skillPath) { this.skillPath = skillPath; }
    
    public SkillMetadataDTO getSkillMetadata() { return skillMetadata; }
    public void setSkillMetadata(SkillMetadataDTO skillMetadata) { this.skillMetadata = skillMetadata; }
}

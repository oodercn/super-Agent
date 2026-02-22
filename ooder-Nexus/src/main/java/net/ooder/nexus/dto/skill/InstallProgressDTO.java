package net.ooder.nexus.dto.skill;

import java.util.List;
import java.util.Map;

/**
 * 安装进度 DTO
 */
public class InstallProgressDTO {
    
    private String installId;
    private String skillId;
    private String skillName;
    private String version;
    private String stage;
    private int progress;
    private String status;
    private String message;
    private List<ProgressStep> steps;
    private long startTime;
    private long estimatedTime;
    private String error;

    public String getInstallId() { return installId; }
    public void setInstallId(String installId) { this.installId = installId; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public List<ProgressStep> getSteps() { return steps; }
    public void setSteps(List<ProgressStep> steps) { this.steps = steps; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(long estimatedTime) { this.estimatedTime = estimatedTime; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public static class ProgressStep {
        private String name;
        private String status;
        private int progress;
        private String message;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}

package net.ooder.nexus.skillcenter.dto.install;

import java.util.List;

public class InstallProgressDTO {
    
    private String installId;
    private String skillId;
    private String skillName;
    private String status;
    private int progress;
    private String currentStage;
    private List<InstallStageDTO> stages;
    private String errorMessage;
    private String errorCode;
    private long startTime;
    private long endTime;
    private long duration;

    public String getInstallId() { return installId; }
    public void setInstallId(String installId) { this.installId = installId; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public List<InstallStageDTO> getStages() { return stages; }
    public void setStages(List<InstallStageDTO> stages) { this.stages = stages; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
}

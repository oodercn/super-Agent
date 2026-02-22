package net.ooder.nexus.skillcenter.dto.install;

public class InstallStageDTO {
    
    private String stageId;
    private String name;
    private String description;
    private String status;
    private int progress;
    private long startTime;
    private long endTime;
    private String message;

    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

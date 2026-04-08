package net.ooder.mvp.skill.scene.dto.installer;

public class InstallerStatusRequestDTO {
    
    private String loop;
    
    private String status;
    
    private String completedAt;

    public InstallerStatusRequestDTO() {}

    public String getLoop() {
        return loop;
    }

    public void setLoop(String loop) {
        this.loop = loop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}

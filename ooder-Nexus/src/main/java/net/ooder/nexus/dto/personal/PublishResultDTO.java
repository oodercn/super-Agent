package net.ooder.nexus.dto.personal;

import net.ooder.nexus.domain.personal.model.PlatformResult;

import java.io.Serializable;
import java.util.List;

public class PublishResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String taskId;
    private String status;
    private List<PlatformResult> platforms;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlatformResult> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<PlatformResult> platforms) {
        this.platforms = platforms;
    }
}

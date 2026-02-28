package net.ooder.nexus.dto.personal;

import java.io.Serializable;

public class ExecutionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String status;
    private String executeTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }
}

package net.ooder.nexus.domain.security.model;

import java.io.Serializable;
import java.util.List;

public class SecurityLogsResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<SecurityLog> logs;
    private int count;

    public SecurityLogsResult() {
    }

    public SecurityLogsResult(List<SecurityLog> logs, int count) {
        this.logs = logs;
        this.count = count;
    }

    public List<SecurityLog> getLogs() {
        return logs;
    }

    public void setLogs(List<SecurityLog> logs) {
        this.logs = logs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

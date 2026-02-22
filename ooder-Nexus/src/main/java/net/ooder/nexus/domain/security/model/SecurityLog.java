package net.ooder.nexus.domain.security.model;

import java.io.Serializable;

public class SecurityLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String timestamp;
    private String event;
    private String user;
    private String ip;

    public SecurityLog() {
    }

    public SecurityLog(String timestamp, String event, String user, String ip) {
        this.timestamp = timestamp;
        this.event = event;
        this.user = user;
        this.ip = ip;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

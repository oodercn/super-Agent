package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

public class ConfigHistoryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String timestamp;
    private String user;
    private String action;
    private String details;

    public ConfigHistoryItem() {
    }

    public ConfigHistoryItem(int id, String timestamp, String user, String action, String details) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.action = action;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

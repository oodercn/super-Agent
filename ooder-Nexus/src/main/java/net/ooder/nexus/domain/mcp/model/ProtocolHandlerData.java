package net.ooder.nexus.domain.mcp.model;

import java.util.Date;
import java.util.Map;

public class ProtocolHandlerData {
    private String id;
    private String commandType;
    private String name;
    private String description;
    private String status;
    private String version;
    private Map<String, Object> config;
    private Date createdAt;
    private Date lastUpdated;
    private Date lastUsed;
    private boolean enabled;

    public ProtocolHandlerData() {
    }

    public ProtocolHandlerData(String id, String commandType, String name, String description, String status, String version, Map<String, Object> config, Date createdAt, Date lastUpdated, Date lastUsed, boolean enabled) {
        this.id = id;
        this.commandType = commandType;
        this.name = name;
        this.description = description;
        this.status = status;
        this.version = version;
        this.config = config;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.lastUsed = lastUsed;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
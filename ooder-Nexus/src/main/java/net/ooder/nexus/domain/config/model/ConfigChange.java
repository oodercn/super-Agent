package net.ooder.nexus.domain.config.model;

import java.io.Serializable;
import java.util.Map;

public class ConfigChange implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String configType;
    private Map<String, Object> changes;
    private long timestamp;

    public ConfigChange() {
    }

    public ConfigChange(String id, String configType, Map<String, Object> changes, long timestamp) {
        this.id = id;
        this.configType = configType;
        this.changes = changes;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public Map<String, Object> getChanges() {
        return changes;
    }

    public void setChanges(Map<String, Object> changes) {
        this.changes = changes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
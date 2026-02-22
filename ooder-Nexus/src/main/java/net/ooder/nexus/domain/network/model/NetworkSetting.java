package net.ooder.nexus.domain.network.model;

import java.io.Serializable;

public class NetworkSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String type;
    private String value;
    private String description;
    private long lastUpdated;

    public NetworkSetting() {
    }

    public NetworkSetting(String id, String name, String type, String value, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.description = description;
        this.lastUpdated = System.currentTimeMillis();
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

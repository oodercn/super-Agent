package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

public class ConfigItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private Object config;

    public ConfigItem() {
    }

    public ConfigItem(String type, Object config) {
        this.type = type;
        this.config = config;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }
}

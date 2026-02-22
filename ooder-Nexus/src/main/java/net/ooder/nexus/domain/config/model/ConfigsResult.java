package net.ooder.nexus.domain.config.model;

import java.io.Serializable;
import java.util.List;

public class ConfigsResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ConfigItem> configs;
    private int count;

    public ConfigsResult() {
    }

    public ConfigsResult(List<ConfigItem> configs, int count) {
        this.configs = configs;
        this.count = count;
    }

    public List<ConfigItem> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ConfigItem> configs) {
        this.configs = configs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package net.ooder.nexus.domain.skill.model;

import java.util.List;

public class ConfigSchema {
    private List<ConfigField> required;
    private List<ConfigField> optional;

    public ConfigSchema() {}

    public List<ConfigField> getRequired() {
        return required;
    }

    public void setRequired(List<ConfigField> required) {
        this.required = required;
    }

    public List<ConfigField> getOptional() {
        return optional;
    }

    public void setOptional(List<ConfigField> optional) {
        this.optional = optional;
    }
}

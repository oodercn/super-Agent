package net.ooder.nexus.domain.system.model;

import java.io.Serializable;

public class VersionInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String version;
    private String name;
    private String description;

    public VersionInfo() {
    }

    public VersionInfo(String version, String name, String description) {
        this.version = version;
        this.name = name;
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}

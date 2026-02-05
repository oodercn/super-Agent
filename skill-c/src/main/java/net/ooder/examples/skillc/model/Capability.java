/**
 * 作者：ooderAI agent team
 * 版本：V0.6.0
 * 日期：2026-01-18
 */
package net.ooder.examples.skillc.model;

import java.util.ArrayList;
import java.util.List;

public class Capability {
    private String capabilityId;
    private String name;
    private String description;
    private String type;
    private List<String> supportedScenes;

    public Capability() {
        this.supportedScenes = new ArrayList<>();
    }

    public Capability(String capabilityId, String name, String description) {
        this.capabilityId = capabilityId;
        this.name = name;
        this.description = description;
        this.type = "general";
        this.supportedScenes = new ArrayList<>();
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getSupportedScenes() {
        return new ArrayList<>(supportedScenes);
    }

    public void setSupportedScenes(List<String> supportedScenes) {
        this.supportedScenes = supportedScenes;
    }

    public void addSupportedScene(String sceneType) {
        this.supportedScenes.add(sceneType);
    }

    public boolean supportsScene(String sceneType) {
        return this.supportedScenes.contains(sceneType);
    }
}
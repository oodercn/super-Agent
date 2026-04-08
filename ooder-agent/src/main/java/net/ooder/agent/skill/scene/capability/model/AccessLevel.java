package net.ooder.mvp.skill.scene.capability.model;

public enum AccessLevel {
    PRIVATE("私有", "仅所有者及其授权Agent可访问"),
    DOMAIN("域内", "同域可访问"),
    SCENE("场景内", "同场景可访问"),
    PUBLIC("公共", "全局可访问");

    private final String name;
    private final String description;

    AccessLevel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

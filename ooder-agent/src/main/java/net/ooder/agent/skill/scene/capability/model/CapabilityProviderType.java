package net.ooder.mvp.skill.scene.capability.model;

public enum CapabilityProviderType {
    SKILL("Skill提供", "由技能包提供"),
    AGENT("Agent提供", "由Agent提供"),
    SUPER_AGENT("SuperAgent涌现", "由SuperAgent涌现产生"),
    DEVICE("设备提供", "由设备提供"),
    PLATFORM("平台提供", "由平台内置"),
    CROSS_SCENE("跨场景引用", "引用其他场景的能力");

    private final String name;
    private final String description;

    CapabilityProviderType(String name, String description) {
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

package net.ooder.mvp.skill.scene.capability.model;

public enum CapabilityBindingStatus {
    PENDING("等待绑定", "等待绑定到场景"),
    BINDING("绑定中", "正在创建绑定"),
    ACTIVE("正常运行", "能力正常可用"),
    INACTIVE("暂停使用", "暂停能力调用"),
    ERROR("故障状态", "能力故障"),
    RELEASED("已释放", "绑定已释放");

    private final String name;
    private final String description;

    CapabilityBindingStatus(String name, String description) {
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

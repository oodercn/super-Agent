package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "capability_provider_type", name = "能力提供者类型", description = "能力提供者的类型")
public enum CapabilityProviderType implements DictItem {
    
    PLATFORM("PLATFORM", "平台", "平台服务", "ri-cloud-line", 1),
    AGENT("AGENT", "Agent", "智能代理", "ri-robot-line", 2),
    EXTERNAL("EXTERNAL", "外部", "外部服务", "ri-external-link-line", 3),
    HYBRID("HYBRID", "混合", "混合模式", "ri-git-merge-line", 4),
    SKILL("SKILL", "技能", "可安装的技能包", "ri-flashlight-line", 5),
    SUPER_AGENT("SUPER_AGENT", "超级Agent", "超级智能代理", "ri-robot-2-line", 6),
    DEVICE("DEVICE", "设备", "物联网设备", "ri-device-line", 7),
    CROSS_SCENE("CROSS_SCENE", "跨场景", "跨场景能力", "ri-git-merge-line", 8);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    CapabilityProviderType(String code, String name, String description, String icon, int sort) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public int getSort() {
        return sort;
    }
}

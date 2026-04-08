package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "capability_ownership", name = "能力归属类型", description = "能力归属分类 v2.3.3")
public enum CapabilityOwnership implements DictItem {
    
    SCENE_INTERNAL("SCENE_INTERNAL", "场景内部能力", 
        "定义在场景技能包内，仅场景内可见，生命周期绑定场景", "ri-lock-line", 1),
    
    INDEPENDENT("INDEPENDENT", "独立能力", 
        "可独立部署的能力，支持多场景组，任务独立、数据隔离", "ri-puzzle-line", 2),
    
    PLATFORM("PLATFORM", "平台能力", 
        "平台内置能力，全局可见，无需绑定场景组", "ri-global-line", 3);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    CapabilityOwnership(String code, String name, String description, String icon, int sort) {
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
    
    public boolean isSceneInternal() {
        return this == SCENE_INTERNAL;
    }
    
    public boolean isIndependent() {
        return this == INDEPENDENT;
    }
    
    public boolean isPlatform() {
        return this == PLATFORM;
    }
    
    public static CapabilityOwnership fromCode(String code) {
        for (CapabilityOwnership ownership : values()) {
            if (ownership.code.equals(code)) {
                return ownership;
            }
        }
        return null;
    }
}

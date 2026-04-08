package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "scene_type", name = "场景类型", description = "场景技能的运行类型")
public enum SceneType implements DictItem {

    AUTO("AUTO", "自主场景", "自驱动运行，无需外部触发", "ri-robot-line", 1, true, false),
    TRIGGER("TRIGGER", "触发场景", "等待外部触发，被动响应", "ri-hand-coin-line", 2, false, true),
    HYBRID("HYBRID", "混合场景", "既可主动也可被动", "ri-shuffle-line", 3, true, true);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;
    private final boolean canSelfDrive;
    private final boolean canBeTriggered;

    SceneType(String code, String name, String description, String icon, int sort, 
              boolean canSelfDrive, boolean canBeTriggered) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
        this.canSelfDrive = canSelfDrive;
        this.canBeTriggered = canBeTriggered;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String getCode() { return code; }

    @Override
    public String getIcon() { return icon; }

    @Override
    public int getSort() { return sort; }

    public boolean canSelfDrive() { return canSelfDrive; }

    public boolean canBeTriggered() { return canBeTriggered; }

    public boolean isPureAuto() { return canSelfDrive && !canBeTriggered; }

    public boolean isPureTrigger() { return !canSelfDrive && canBeTriggered; }

    public boolean isHybrid() { return canSelfDrive && canBeTriggered; }

    public static SceneType fromCode(String code) {
        if (code == null) return TRIGGER;
        for (SceneType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return TRIGGER;
    }

    public static SceneType fromLegacyCode(String legacyCode) {
        if (legacyCode == null) return null;
        String code = legacyCode.toUpperCase();
        if ("ABS".equals(code) || "ASS".equals(code)) {
            return AUTO;
        } else if ("TBS".equals(code)) {
            return TRIGGER;
        }
        return null;
    }

    public static SceneType fromSeSdk(String seSceneType) {
        if (seSceneType == null) return TRIGGER;
        if ("AUTO".equalsIgnoreCase(seSceneType)) return AUTO;
        if ("TRIGGER".equalsIgnoreCase(seSceneType)) return TRIGGER;
        if ("HYBRID".equalsIgnoreCase(seSceneType)) return HYBRID;
        return TRIGGER;
    }
}

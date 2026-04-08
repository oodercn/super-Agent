package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "skill_form", name = "技能形态", description = "技能的形态分类")
public enum SkillForm implements DictItem {

    SCENE("SCENE", "场景技能", "容器型技能，可包含子技能", "ri-layout-grid-line", 1, true),
    STANDALONE("STANDALONE", "独立技能", "原子型技能，直接提供服务", "ri-cpu-line", 2, false),
    PROVIDER("PROVIDER", "能力服务", "提供基础能力的技能（兼容旧版）", "ri-service-line", 3, false),
    DRIVER("DRIVER", "驱动适配", "驱动场景运行的技能（兼容旧版）", "ri-steering-line", 4, false),
    INTERNAL("INTERNAL", "内部服务", "系统内部使用的技能（兼容旧版）", "ri-settings-3-line", 5, false);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;
    private final boolean container;

    SkillForm(String code, String name, String description, String icon, int sort, boolean container) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
        this.container = container;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String getCode() { return code; }

    @Override
    public String getIcon() { return icon; }

    @Override
    public int getSort() { return sort; }

    public boolean isContainer() { return container; }

    public boolean isScene() { return this == SCENE; }

    public boolean isStandalone() { return this == STANDALONE; }

    public static SkillForm fromCode(String code) {
        if (code == null) return STANDALONE;
        for (SkillForm form : values()) {
            if (form.code.equalsIgnoreCase(code)) {
                return form;
            }
        }
        if (code.toUpperCase().contains("SCENE")) return SCENE;
        if ("SCENE".equalsIgnoreCase(code)) return SCENE;
        if ("STANDALONE".equalsIgnoreCase(code)) return STANDALONE;
        return STANDALONE;
    }

    public static SkillForm fromSeSdk(String seForm) {
        if (seForm == null) return STANDALONE;
        if ("SCENE".equalsIgnoreCase(seForm)) return SCENE;
        if ("STANDALONE".equalsIgnoreCase(seForm)) return STANDALONE;
        return STANDALONE;
    }

    public String toSeSdkForm() {
        if (this == SCENE) return "SCENE";
        return "STANDALONE";
    }
}

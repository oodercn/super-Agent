package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "visibility", name = "可见性", description = "能力的可见性级别")
public enum Visibility implements DictItem {

    PUBLIC("public", "普通用户可见", "所有用户可见", "ri-global-line", 1),
    DEVELOPER("developer", "开发者可见", "仅开发者可见", "ri-code-line", 2),
    INTERNAL("internal", "系统内部", "系统内部使用", "ri-shield-keyhole-line", 3);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    Visibility(String code, String name, String description, String icon, int sort) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String getCode() { return code; }

    @Override
    public String getIcon() { return icon; }

    @Override
    public int getSort() { return sort; }

    public static Visibility fromCode(String code) {
        for (Visibility v : values()) {
            if (v.code.equalsIgnoreCase(code)) {
                return v;
            }
        }
        return PUBLIC;
    }
}

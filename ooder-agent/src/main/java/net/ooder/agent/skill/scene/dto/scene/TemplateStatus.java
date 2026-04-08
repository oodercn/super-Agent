package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "template_status", name = "模板状态", description = "场景模板的状态")
public enum TemplateStatus implements DictItem {
    
    DRAFT("draft", "草稿", "模板草稿状态", "ri-draft-line", 1),
    PUBLISHED("published", "已发布", "模板已发布", "ri-send-plane-line", 2),
    ARCHIVED("archived", "已归档", "模板已归档", "ri-archive-line", 3),
    DEPRECATED("deprecated", "已废弃", "模板已废弃", "ri-delete-bin-line", 4);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    TemplateStatus(String code, String name, String description, String icon, int sort) {
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

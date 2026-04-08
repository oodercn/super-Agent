package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "template_category", name = "模板分类", description = "场景模板的分类")
public enum TemplateCategory implements DictItem {
    
    BUSINESS("business", "业务场景", "业务相关场景模板", "ri-briefcase-line", 1),
    IOT("iot", "物联网场景", "物联网相关场景模板", "ri-cpu-line", 2),
    COLLABORATION("collaboration", "协作场景", "团队协作场景模板", "ri-team-line", 3),
    GOVERNANCE("governance", "治理场景", "治理相关场景模板", "ri-government-line", 4),
    NOTIFICATION("notification", "通知推送", "通知推送场景模板", "ri-notification-line", 5),
    DATA_INPUT("data-input", "数据输入", "数据输入场景模板", "ri-input-method-line", 6),
    DATA_PROCESSING("data-processing", "数据处理", "数据处理场景模板", "ri-file-chart-line", 7),
    INTELLIGENCE("intelligence", "智能分析", "智能分析场景模板", "ri-brain-line", 8),
    UI("ui", "界面展示", "界面展示场景模板", "ri-layout-line", 9),
    ACTUATION("actuation", "设备控制", "设备控制场景模板", "ri-remote-control-line", 10),
    SENSING("sensing", "传感器读取", "传感器读取场景模板", "ri-temp-hot-line", 11);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    TemplateCategory(String code, String name, String description, String icon, int sort) {
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

package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "capability_binding_status", name = "能力绑定状态", description = "能力绑定的状态")
public enum CapabilityBindingStatus implements DictItem {
    
    PENDING("PENDING", "待激活", "能力绑定等待激活", "ri-time-line", 1),
    ACTIVE("ACTIVE", "已激活", "能力绑定正常工作", "ri-checkbox-circle-line", 2),
    INACTIVE("INACTIVE", "未激活", "能力绑定未激活", "ri-checkbox-blank-circle-line", 3),
    ERROR("ERROR", "错误", "能力绑定出错", "ri-error-warning-line", 4),
    DISABLED("DISABLED", "已禁用", "能力绑定已禁用", "ri-forbid-line", 5);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    CapabilityBindingStatus(String code, String name, String description, String icon, int sort) {
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

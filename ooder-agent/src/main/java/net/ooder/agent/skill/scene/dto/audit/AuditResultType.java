package net.ooder.mvp.skill.scene.dto.audit;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "audit_result_type", name = "审计结果类型", description = "审计操作的结果状态")
public enum AuditResultType implements DictItem {
    
    SUCCESS("SUCCESS", "成功", "操作成功执行", "ri-check-line", 1),
    FAILURE("FAILURE", "失败", "操作执行失败", "ri-close-line", 2),
    DENIED("DENIED", "拒绝", "操作被拒绝执行", "ri-forbid-line", 3),
    TIMEOUT("TIMEOUT", "超时", "操作执行超时", "ri-timer-line", 4);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    AuditResultType(String code, String name, String description, String icon, int sort) {
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

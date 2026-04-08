package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "capability_status", name = "能力状态", description = "能力生命周期状态 v2.3")
public enum CapabilityStatus implements DictItem {
    
    DEFINED("DEFINED", "已定义", "能力已定义但未注册", "ri-file-text-line", 1),
    REGISTERED("REGISTERED", "已注册", "已注册到能力注册表", "ri-checkbox-circle-line", 2),
    PUBLISHED("PUBLISHED", "已发布", "已对外发布", "ri-send-plane-line", 3),
    ENABLED("ENABLED", "已启用", "允许能力被调用", "ri-toggle-line", 4),
    DISABLED("DISABLED", "已禁用", "暂停能力调用", "ri-toggle-line", 5),
    DEPRECATED("DEPRECATED", "已废弃", "标记为废弃", "ri-delete-bin-line", 6),
    ARCHIVED("ARCHIVED", "已归档", "归档历史记录", "ri-archive-line", 7),
    
    DRAFT("DRAFT", "草稿状态", "场景技能创建后未激活", "ri-draft-line", 10),
    PENDING("PENDING", "待处理", "等待触发/激活（半自动特有）", "ri-time-line", 11),
    SCHEDULED("SCHEDULED", "已调度", "定时任务已排期", "ri-calendar-schedule-line", 12),
    RUNNING("RUNNING", "运行中", "场景正在执行", "ri-play-circle-line", 13),
    PAUSED("PAUSED", "暂停状态", "临时停止（完整/半自动特有）", "ri-pause-circle-line", 14),
    ERROR("ERROR", "错误状态", "运行异常", "ri-error-warning-line", 15),
    COMPLETED("COMPLETED", "完成状态", "正常结束（完整/半自动特有）", "ri-checkbox-circle-line", 16),
    WAITING("WAITING", "等待中", "等待人工干预（半自动特有）", "ri-user-received-line", 17),
    INITIALIZING("INITIALIZING", "初始化中", "正在初始化（技术场景特有）", "ri-loader-4-line", 18);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    CapabilityStatus(String code, String name, String description, String icon, int sort) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public int getSort() {
        return sort;
    }

    public boolean isSceneSkillStatus() {
        return sort >= 10;
    }

    public boolean isBaseStatus() {
        return sort < 10;
    }

    public boolean isActive() {
        return this == ENABLED || this == RUNNING || this == SCHEDULED;
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == ARCHIVED || this == ERROR;
    }

    public boolean canTransitionTo(CapabilityStatus target) {
        if (this == target) {
            return false;
        }
        switch (this) {
            case DRAFT:
                return target == PENDING || target == SCHEDULED || target == ENABLED;
            case PENDING:
                return target == WAITING || target == RUNNING || target == DRAFT;
            case SCHEDULED:
                return target == RUNNING || target == PAUSED || target == ERROR;
            case RUNNING:
                return target == COMPLETED || target == PAUSED || target == ERROR || target == SCHEDULED;
            case PAUSED:
                return target == RUNNING || target == ARCHIVED;
            case ERROR:
                return target == RUNNING || target == DRAFT || target == ARCHIVED;
            case WAITING:
                return target == RUNNING || target == PAUSED || target == COMPLETED;
            case INITIALIZING:
                return target == SCHEDULED || target == ERROR;
            default:
                return false;
        }
    }
}

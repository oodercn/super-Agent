package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "driver_type", name = "驱动能力类型", description = "驱动能力的分类类型 v2.3")
public enum DriverType implements DictItem {

    INTENT_RECEIVER("INTENT_RECEIVER", "意图接收", "接收用户意图并触发场景启动", "ri-user-voice-line", 1),
    SCHEDULER("SCHEDULER", "时间驱动", "监听时间事件并触发能力调用", "ri-timer-line", 2),
    EVENT_LISTENER("EVENT_LISTENER", "事件监听", "监听业务事件并触发能力调用", "ri-notification-3-line", 3),
    CAPABILITY_INVOKER("CAPABILITY_INVOKER", "能力调用", "管理能力调用链的执行", "ri-play-circle-line", 4),
    COLLABORATION_COORDINATOR("COLLABORATION_COORDINATOR", "协作协调", "协调协作场景的启动和停止", "ri-team-line", 5);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    DriverType(String code, String name, String description, String icon, int sort) {
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

    public boolean isTrigger() {
        return this == INTENT_RECEIVER || this == SCHEDULER || this == EVENT_LISTENER;
    }

    public boolean isExecutor() {
        return this == CAPABILITY_INVOKER || this == COLLABORATION_COORDINATOR;
    }
}

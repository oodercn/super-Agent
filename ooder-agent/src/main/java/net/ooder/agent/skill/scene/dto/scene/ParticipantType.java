package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "participant_type", name = "参与者类型", description = "场景参与者的类型")
public enum ParticipantType implements DictItem {
    
    USER("USER", "用户", "人类用户参与者", "ri-user-line", 1),
    AGENT("AGENT", "Agent", "智能代理参与者", "ri-robot-line", 2),
    SUPER_AGENT("SUPER_AGENT", "超级Agent", "超级智能代理参与者", "ri-robot-2-line", 3);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    ParticipantType(String code, String name, String description, String icon, int sort) {
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

package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "participant_role", name = "参与者角色", description = "场景参与者的角色类型")
public enum ParticipantRole implements DictItem {
    
    MANAGER("manager", "管理者", "场景管理者，拥有完整管理权限", "ri-user-star-line", 1),
    EMPLOYEE("employee", "员工", "普通员工，参与场景执行", "ri-user-line", 2),
    HR("hr", "HR", "人力资源，管理人事相关", "ri-team-line", 3),
    LLM_ASSISTANT("llm-assistant", "LLM助手", "AI分析助手", "ri-brain-line", 4),
    COORDINATOR("coordinator", "协调Agent", "任务协调Agent", "ri-git-merge-line", 5),
    SUPER_AGENT("super-agent", "超级Agent", "超级智能代理", "ri-robot-2-line", 6);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    ParticipantRole(String code, String name, String description, String icon, int sort) {
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

package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "participant_status", name = "参与者状态", description = "场景参与者的状态")
public enum ParticipantStatus implements DictItem {
    
    PENDING("PENDING", "待确认", "等待参与者确认加入", "ri-time-line", 1),
    JOINED("JOINED", "已加入", "参与者已加入场景", "ri-user-add-line", 2),
    ACTIVE("ACTIVE", "活跃", "参与者正常活跃", "ri-user-heart-line", 3),
    INACTIVE("INACTIVE", "非活跃", "参与者非活跃状态", "ri-user-unfollow-line", 4),
    LEFT("LEFT", "已离开", "参与者已离开场景", "ri-logout-box-line", 5),
    SUSPENDED("SUSPENDED", "已暂停", "参与者已暂停", "ri-pause-circle-line", 6),
    REMOVED("REMOVED", "已移除", "参与者已移除", "ri-user-unfollow-line", 7);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    ParticipantStatus(String code, String name, String description, String icon, int sort) {
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

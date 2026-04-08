package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "scene_group_status", name = "场景组状态", description = "场景组的运行状态")
public enum SceneGroupStatus implements DictItem {
    
    DRAFT("DRAFT", "草稿", "场景组草稿状态", "ri-draft-line", 1),
    CREATING("CREATING", "创建中", "场景组正在创建", "ri-loader-4-line", 2),
    CONFIGURING("CONFIGURING", "配置中", "场景组正在配置", "ri-settings-4-line", 3),
    PENDING("PENDING", "待激活", "场景组等待激活", "ri-time-line", 4),
    ACTIVE("ACTIVE", "运行中", "场景组正常运行", "ri-play-circle-line", 5),
    SUSPENDED("SUSPENDED", "已暂停", "场景组已暂停", "ri-pause-circle-line", 6),
    SCALING("SCALING", "扩缩容中", "场景组正在扩缩容", "ri-expand-diagonal-line", 7),
    MIGRATING("MIGRATING", "迁移中", "场景组正在迁移", "ri-route-line", 8),
    DESTROYING("DESTROYING", "销毁中", "场景组正在销毁", "ri-delete-bin-line", 9),
    DESTROYED("DESTROYED", "已销毁", "场景组已销毁", "ri-skull-line", 10),
    ERROR("ERROR", "错误", "场景组运行错误", "ri-error-warning-line", 11);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    SceneGroupStatus(String code, String name, String description, String icon, int sort) {
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

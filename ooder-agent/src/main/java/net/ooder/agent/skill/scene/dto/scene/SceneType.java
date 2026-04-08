package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "scene_type", name = "场景类型", description = "场景的类型")
public enum SceneType implements DictItem {
    
    PRIMARY("PRIMARY", "主场景", "主要场景，独立运行", "ri-layout-top-line", 1),
    COLLABORATIVE("COLLABORATIVE", "协作场景", "协作场景，与其他场景配合", "ri-layout-bottom-line", 2),
    ENTERPRISE("ENTERPRISE", "企业网络", "企业级网络场景", "ri-building-line", 3),
    PERSONAL("PERSONAL", "个人网络", "个人级网络场景", "ri-user-line", 4),
    TEST("TEST", "测试网络", "测试环境场景", "ri-flask-line", 5),
    DEVELOPMENT("DEVELOPMENT", "开发环境", "开发环境场景", "ri-code-line", 6);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    SceneType(String code, String name, String description, String icon, int sort) {
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

package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "key_status", name = "密钥状态", description = "密钥的状态")
public enum KeyStatus implements DictItem {

    ACTIVE("ACTIVE", "激活", "密钥已激活可用", "ri-check-line", 1),
    INACTIVE("INACTIVE", "未激活", "密钥未激活", "ri-pause-line", 2),
    EXPIRED("EXPIRED", "已过期", "密钥已过期", "ri-time-line", 3),
    REVOKED("REVOKED", "已撤销", "密钥已被撤销", "ri-forbid-line", 4),
    SUSPENDED("SUSPENDED", "已暂停", "密钥已暂停使用", "ri-stop-line", 5);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    KeyStatus(String code, String name, String description, String icon, int sort) {
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

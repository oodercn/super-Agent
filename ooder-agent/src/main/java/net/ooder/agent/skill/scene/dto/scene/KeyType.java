package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;

@Dict(code = "key_type", name = "密钥类型", description = "密钥的类型分类")
public enum KeyType implements DictItem {

    LLM_API_KEY("LLM_API_KEY", "LLM API密钥", "大语言模型API密钥", "ri-brain-line", 1),
    CLOUD_API_KEY("CLOUD_API_KEY", "云服务密钥", "云服务API密钥", "ri-cloud-line", 2),
    DATABASE_KEY("DATABASE_KEY", "数据库密钥", "数据库连接密钥", "ri-database-line", 3),
    SERVICE_TOKEN("SERVICE_TOKEN", "服务令牌", "服务访问令牌", "ri-server-line", 4),
    AGENT_KEY("AGENT_KEY", "代理密钥", "智能体密钥", "ri-robot-line", 5),
    ENCRYPTION_KEY("ENCRYPTION_KEY", "加密密钥", "数据加密密钥", "ri-lock-line", 6);

    private final String code;
    private final String name;
    private final String description;
    private final String icon;
    private final int sort;

    KeyType(String code, String name, String description, String icon, int sort) {
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

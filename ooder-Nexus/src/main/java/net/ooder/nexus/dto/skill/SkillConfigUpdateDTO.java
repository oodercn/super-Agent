package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.Map;

/**
 * 技能配置更新DTO
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public class SkillConfigUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Map<String, Object> config;

    public SkillConfigUpdateDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}

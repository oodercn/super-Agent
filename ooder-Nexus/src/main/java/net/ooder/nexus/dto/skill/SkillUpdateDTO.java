package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.List;

/**
 * 技能更新DTO
 * 用于编辑已安装技能的基本信息
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public class SkillUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String skillName;
    private String description;
    private String version;
    private String category;
    private String author;
    private String icon;
    private List<String> tags;

    public SkillUpdateDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

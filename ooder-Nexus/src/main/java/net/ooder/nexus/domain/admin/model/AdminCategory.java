package net.ooder.nexus.domain.admin.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin_category")
public class AdminCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String name;

    private String description;
    private String icon;
    private Integer skillCount = 0;
    private Long createTime;
    private Long updateTime;

    public AdminCategory() {}

    public AdminCategory(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
        this.createTime = System.currentTimeMillis();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getSkillCount() { return skillCount; }
    public void setSkillCount(Integer skillCount) { this.skillCount = skillCount; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}

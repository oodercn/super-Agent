package net.ooder.skillcenter.model.impl;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillParam;
import net.ooder.skillcenter.model.SkillResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用技能实现
 * 用于动态创建的技能
 */
public class GenericSkill implements Skill {
    private String id;
    private String name;
    private String description;
    private String category;
    private String version;
    private String downloadUrl;
    private Map<String, SkillParam> params;
    
    public GenericSkill() {
        this.params = new HashMap<>();
    }
    
    public GenericSkill(String id, String name, String description, String category, String version, String downloadUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.params = new HashMap<>();
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    @Override
    public Map<String, SkillParam> getParams() {
        return params;
    }
    
    public void setParams(Map<String, SkillParam> params) {
        this.params = params;
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
    @Override
    public SkillResult execute(SkillContext context) throws SkillException {
        // 通用技能执行逻辑
        return new SkillResult(SkillResult.Status.SUCCESS, "Skill executed successfully");
    }
}

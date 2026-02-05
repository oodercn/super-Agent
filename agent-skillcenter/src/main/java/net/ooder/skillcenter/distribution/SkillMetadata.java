package net.ooder.skillcenter.distribution;

import net.ooder.skillcenter.model.SkillParam;

import java.util.Map;

/**
 * 技能元数据，用于描述技能的基本信息
 * 用于技能分发过程中的元数据交换
 */
public class SkillMetadata {
    private String skillId;
    private String skillName;
    private String skillDescription;
    private Map<String, SkillParam> params;
    private boolean available;
    
    /**
     * 获取技能ID
     * @return 技能ID
     */
    public String getSkillId() {
        return skillId;
    }
    
    /**
     * 设置技能ID
     * @param skillId 技能ID
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    /**
     * 获取技能名称
     * @return 技能名称
     */
    public String getSkillName() {
        return skillName;
    }
    
    /**
     * 设置技能名称
     * @param skillName 技能名称
     */
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    /**
     * 获取技能描述
     * @return 技能描述
     */
    public String getSkillDescription() {
        return skillDescription;
    }
    
    /**
     * 设置技能描述
     * @param skillDescription 技能描述
     */
    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }
    
    /**
     * 获取技能参数
     * @return 技能参数映射
     */
    public Map<String, SkillParam> getParams() {
        return params;
    }
    
    /**
     * 设置技能参数
     * @param params 技能参数映射
     */
    public void setParams(Map<String, SkillParam> params) {
        this.params = params;
    }
    
    /**
     * 技能是否可用
     * @return true如果可用，false否则
     */
    public boolean isAvailable() {
        return available;
    }
    
    /**
     * 设置技能可用性
     * @param available 是否可用
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}

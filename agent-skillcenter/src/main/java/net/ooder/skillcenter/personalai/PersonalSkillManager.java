package net.ooder.skillcenter.personalai;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillParam;
import net.ooder.skillcenter.model.SkillResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 个人技能管理器，管理个人的AI技能
 */
public class PersonalSkillManager {
    // 个人技能映射，key为技能ID，value为个人技能
    private Map<String, PersonalSkill> personalSkills;
    
    /**
     * 构造方法
     */
    public PersonalSkillManager() {
        this.personalSkills = new ConcurrentHashMap<>();
    }
    
    /**
     * 加载个人技能
     * @return 个人技能列表
     */
    public List<PersonalSkill> loadPersonalSkills() {
        // 加载本地存储的个人技能
        // 这里简单实现，返回空列表
        return new ArrayList<>();
    }
    
    /**
     * 注册个人技能
     * @param skill 个人技能
     */
    public void registerPersonalSkill(PersonalSkill skill) {
        personalSkills.put(skill.getId(), skill);
        // 可选：持久化存储个人技能
    }
    
    /**
     * 卸载个人技能
     * @param skillId 技能ID
     */
    public void unregisterPersonalSkill(String skillId) {
        personalSkills.remove(skillId);
        // 可选：从持久化存储中删除个人技能
    }
    
    /**
     * 获取个人技能
     * @param skillId 技能ID
     * @return 个人技能
     */
    public PersonalSkill getPersonalSkill(String skillId) {
        return personalSkills.get(skillId);
    }
    
    /**
     * 获取所有个人技能
     * @return 个人技能列表
     */
    public List<PersonalSkill> getAllPersonalSkills() {
        return new ArrayList<>(personalSkills.values());
    }
    
    /**
     * 获取可共享的个人技能
     * @return 可共享的个人技能列表
     */
    public List<PersonalSkill> getShareableSkills() {
        List<PersonalSkill> shareableSkills = new ArrayList<>();
        for (PersonalSkill skill : personalSkills.values()) {
            if (skill.isShareable()) {
                shareableSkills.add(skill);
            }
        }
        return shareableSkills;
    }
    
    /**
     * 获取个人技能数量
     * @return 个人技能数量
     */
    public int getSkillCount() {
        return personalSkills.size();
    }
    
    /**
     * 个人技能接口，继承自Skill接口
     */
    public interface PersonalSkill extends Skill {
        /**
         * 检查技能是否可共享
         * @return 是否可共享
         */
        boolean isShareable();
        
        /**
         * 设置技能是否可共享
         * @param shareable 是否可共享
         */
        void setShareable(boolean shareable);
        
        /**
         * 获取技能所有者
         * @return 技能所有者
         */
        String getOwner();
        
        /**
         * 设置技能所有者
         * @param owner 技能所有者
         */
        void setOwner(String owner);
        
        /**
         * 获取技能创建时间
         * @return 技能创建时间
         */
        long getCreatedAt();
        
        /**
         * 获取技能最后更新时间
         * @return 技能最后更新时间
         */
        long getLastUpdatedAt();
        
        /**
         * 更新技能
         * @param name 技能名称
         * @param description 技能描述
         * @param params 技能参数
         */
        void update(String name, String description, Map<String, SkillParam> params);
    }
}
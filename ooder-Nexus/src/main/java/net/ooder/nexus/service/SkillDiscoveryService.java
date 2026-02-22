package net.ooder.nexus.service;

import net.ooder.sdk.api.skill.SkillPackage;

import java.util.List;
import java.util.Map;

/**
 * 技能发现服务接口
 *
 * @author ooder Team
 * @version 0.7.1
 * @since 0.7.0
 */
public interface SkillDiscoveryService {

    /**
     * 获取所有可用技能
     * @return 技能列表
     */
    List<SkillPackage> getAllSkills();

    /**
     * 根据ID获取技能
     * @param skillId 技能ID
     * @return 技能信息
     */
    SkillPackage getSkillById(String skillId);

    /**
     * 搜索技能
     * @param keyword 关键词
     * @param scene 场景
     * @param capability 能力
     * @param type 类型
     * @return 匹配的技能列表
     */
    List<SkillPackage> searchSkills(String keyword, String scene, String capability, String type);

    /**
     * 获取技能详情（包含配置模板）
     * @param skillId 技能ID
     * @return 技能详情
     */
    Map<String, Object> getSkillDetail(String skillId);

    /**
     * 刷新技能列表
     */
    void refreshSkills();
}

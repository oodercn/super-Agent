package net.ooder.nexus.service.skill;

import net.ooder.nexus.domain.skill.model.SkillPackage;
import net.ooder.nexus.domain.skill.model.SkillPackage.AuthStatus;

import java.util.List;
import java.util.Map;

/**
 * 技能市场服务接口
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface SkillMarketService {

    /**
     * 获取技能列表
     *
     * @param category 分类（可选）
     * @param page     页码
     * @param pageSize 每页大小
     * @return 技能列表
     */
    List<SkillPackage> getSkillList(String category, int page, int pageSize);

    /**
     * 搜索技能
     *
     * @param keyword  关键词
     * @param category 分类（可选）
     * @param page     页码
     * @param pageSize 每页大小
     * @return 技能列表
     */
    List<SkillPackage> searchSkills(String keyword, String category, int page, int pageSize);

    /**
     * 获取技能详情
     *
     * @param skillId 技能ID
     * @return 技能详情
     */
    SkillPackage getSkillDetail(String skillId);

    /**
     * 安装技能
     *
     * @param skillId  技能ID
     * @param userId   用户ID
     * @param options  安装选项
     * @return 安装结果
     */
    Map<String, Object> installSkill(String skillId, String userId, Map<String, Object> options);

    /**
     * 卸载技能
     *
     * @param skillId 技能ID
     * @param userId  用户ID
     * @return 是否成功
     */
    boolean uninstallSkill(String skillId, String userId);

    /**
     * 更新技能
     *
     * @param skillId 技能ID
     * @param userId  用户ID
     * @return 更新结果
     */
    Map<String, Object> updateSkill(String skillId, String userId);

    /**
     * 获取技能认证状态
     *
     * @param skillId 技能ID
     * @return 认证状态
     */
    AuthStatus getAuthStatus(String skillId);

    /**
     * 获取技能总数
     *
     * @param category 分类（可选）
     * @return 总数
     */
    int getSkillCount(String category);

    /**
     * 获取分类列表
     *
     * @return 分类列表
     */
    List<Map<String, Object>> getCategories();
}

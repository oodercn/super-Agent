package net.ooder.skillcenter.service;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.model.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * 技能服务接口 - 符合 ooderNexus 规范
 */
public interface SkillService {

    /**
     * 获取所有技能（支持分页和排序）
     * @param page 当前页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页技能列表
     */
    PageResponse<Skill> getAllSkills(int page, int size, String sortBy, String sortDirection);

    /**
     * 根据ID获取技能
     * @param id 技能ID
     * @return 技能实例
     */
    Skill getSkill(String id);

    /**
     * 执行技能
     * @param id 技能ID
     * @param parameters 执行参数
     * @param attributes 执行属性
     * @return 执行结果
     */
    SkillResult executeSkill(String id, Map<String, Object> parameters, Map<String, Object> attributes);

    /**
     * 获取技能分类列表
     * @return 分类列表
     */
    List<String> getCategories();

    /**
     * 根据分类获取技能（支持分页和排序）
     * @param category 分类名称
     * @param page 当前页码
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页技能列表
     */
    PageResponse<Skill> getSkillsByCategory(String category, int page, int size, String sortBy, String sortDirection);

    /**
     * 删除技能
     * @param id 技能ID
     * @return 是否删除成功
     */
    boolean deleteSkill(String id);

    /**
     * 发布技能
     * @param skill 技能实例
     * @return 是否发布成功
     */
    boolean publishSkill(Skill skill);

    /**
     * 更新技能
     * @param id 技能ID
     * @param skill 技能实例
     * @return 是否更新成功
     */
    boolean updateSkill(String id, Skill skill);

    /**
     * 批量获取技能
     * @param ids 技能ID列表
     * @return 技能列表
     */
    List<Skill> getSkillsByIds(List<String> ids);

    /**
     * 搜索技能
     * @param keyword 搜索关键词
     * @param page 当前页码
     * @param size 每页大小
     * @return 分页技能列表
     */
    PageResponse<Skill> searchSkills(String keyword, int page, int size);
}

package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.model.SpecValidationModels.SpecValidationResult;
import net.ooder.skillcenter.model.SpecValidationModels.VersionHistory;
import net.ooder.skillcenter.model.SpecValidationModels.SpecValidationReport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 技能服务接口
 */
public interface SkillService {
    
    /**
     * 获取所有技能
     */
    PageResult<SkillDTO> getAllSkills(String category, String status, String keyword, int pageNum, int pageSize);
    
    /**
     * 根据ID获取技能
     */
    SkillDTO getSkillById(String skillId);
    
    /**
     * 添加技能
     */
    SkillDTO addSkill(SkillDTO skillDTO);
    
    /**
     * 更新技能
     */
    SkillDTO updateSkill(String skillId, SkillDTO skillDTO);
    
    /**
     * 删除技能
     */
    boolean deleteSkill(String skillId);
    
    /**
     * 审核技能
     */
    boolean approveSkill(String skillId);
    
    /**
     * 拒绝技能
     */
    boolean rejectSkill(String skillId);
    
    /**
     * 获取市场技能列表
     */
    PageResult<SkillDTO> getMarketSkills(String category, String status, String keyword, int pageNum, int pageSize);
    
    /**
     * 添加市场技能
     */
    SkillDTO addMarketSkill(SkillDTO skillDTO);
    
    /**
     * 更新市场技能
     */
    SkillDTO updateMarketSkill(String skillId, SkillDTO skillDTO);
    
    /**
     * 移除市场技能
     */
    boolean removeMarketSkill(String skillId);
    
    /**
     * 获取技能总数
     */
    int getSkillCount();
    
    /**
     * 获取执行总数
     */
    int getExecutionCount();
    
    /**
     * 获取成功执行数
     */
    int getSuccessfulExecutionCount();
    
    /**
     * 获取失败执行数
     */
    int getFailedExecutionCount();
    
    /**
     * 获取共享技能数
     */
    int getSharedSkillCount();
    
    /**
     * 验证技能规范
     */
    CompletableFuture<SpecValidationResult> validateSpec(String skillId);
    
    /**
     * 验证技能定义
     */
    CompletableFuture<SpecValidationResult> validateDefinition(Map<String, Object> definition);
    
    /**
     * 获取技能版本历史
     */
    CompletableFuture<List<VersionHistory>> getVersionHistory(String skillId, int limit);
    
    /**
     * 获取规范验证报告
     */
    CompletableFuture<SpecValidationReport> getValidationReport(String skillId);
}

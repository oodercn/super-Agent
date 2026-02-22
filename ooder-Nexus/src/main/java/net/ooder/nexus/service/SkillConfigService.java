package net.ooder.nexus.service;

import net.ooder.nexus.dto.skill.ConfigValidationResult;
import net.ooder.nexus.dto.skill.SkillConfigDTO;

import java.util.Map;

/**
 * 技能配置管理服务接口
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public interface SkillConfigService {

    /**
     * 获取技能配置
     * @param skillId 技能ID
     * @return 配置信息
     */
    Map<String, Object> getConfig(String skillId);

    /**
     * 更新技能配置
     * @param dto 配置DTO
     * @return 更新后的配置
     */
    Map<String, Object> updateConfig(SkillConfigDTO dto);

    /**
     * 验证配置
     * @param dto 配置DTO
     * @return 验证结果
     */
    ConfigValidationResult validateConfig(SkillConfigDTO dto);

    /**
     * 获取配置模板
     * @param skillId 技能ID
     * @return 配置模板
     */
    Map<String, Object> getConfigTemplate(String skillId);

    /**
     * 重置配置为默认值
     * @param skillId 技能ID
     * @return 重置后的配置
     */
    Map<String, Object> resetConfig(String skillId);

    /**
     * 获取环境变量
     * @param skillId 技能ID
     * @return 环境变量
     */
    Map<String, String> getEnvVars(String skillId);

    /**
     * 设置环境变量
     * @param skillId 技能ID
     * @param envVars 环境变量
     * @return 操作结果
     */
    boolean setEnvVars(String skillId, Map<String, String> envVars);

    /**
     * 应用配置
     * @param dto 配置DTO
     * @return 操作结果
     */
    boolean applyConfig(SkillConfigDTO dto);
}

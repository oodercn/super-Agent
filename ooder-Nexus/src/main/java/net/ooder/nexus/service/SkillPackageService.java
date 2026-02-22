package net.ooder.nexus.service;

import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.skill.InstalledSkill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 技能包管理服务接口
 * 封装SDK 0.7.1的SkillPackageManager功能
 *
 * @author ooder Team
 * @version 0.7.1
 * @since 0.7.0
 */
public interface SkillPackageService {

    /**
     * 安装技能
     * @param skillId 技能ID
     * @param version 版本号（可选）
     * @param config 配置参数
     * @return 安装结果
     */
    CompletableFuture<Map<String, Object>> installSkill(String skillId, String version, Map<String, String> config);

    /**
     * 卸载技能
     * @param skillId 技能ID
     * @return 卸载结果
     */
    CompletableFuture<Map<String, Object>> uninstallSkill(String skillId);

    /**
     * 更新技能
     * @param skillId 技能ID
     * @param version 目标版本
     * @return 更新结果
     */
    CompletableFuture<Map<String, Object>> updateSkill(String skillId, String version);

    /**
     * 发现技能
     * @param scene 场景名称（可选）
     * @param capability 能力（可选）
     * @param keyword 关键词（可选）
     * @return 技能包列表
     */
    CompletableFuture<List<SkillPackage>> discoverSkills(String scene, String capability, String keyword);

    /**
     * 获取技能信息
     * @param skillId 技能ID
     * @return 技能包信息
     */
    CompletableFuture<SkillPackage> getSkillInfo(String skillId);

    /**
     * 获取技能连接信息
     * @param skillId 技能ID
     * @return 连接信息
     */
    Map<String, Object> getSkillConnection(String skillId);

    /**
     * 获取技能状态
     * @param skillId 技能ID
     * @return 状态字符串
     */
    String getSkillStatus(String skillId);

    /**
     * 测试技能连接
     * @param skillId 技能ID
     * @return 连接是否成功
     */
    CompletableFuture<Boolean> testConnection(String skillId);

    /**
     * 启动技能
     * @param skillId 技能ID
     * @return 操作结果
     */
    CompletableFuture<Void> startSkill(String skillId);

    /**
     * 停止技能
     * @param skillId 技能ID
     * @return 操作结果
     */
    CompletableFuture<Void> stopSkill(String skillId);

    /**
     * 获取所有已安装技能
     * @return 已安装技能列表
     */
    List<InstalledSkill> getInstalledSkills();

    /**
     * 获取指定技能详情
     * @param skillId 技能ID
     * @return 技能详情
     */
    InstalledSkill getInstalledSkill(String skillId);
}

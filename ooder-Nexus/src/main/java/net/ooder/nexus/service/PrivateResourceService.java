package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 私有资源服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供私有资源管理能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>技能安装与管理</li>
 *   <li>存储配置</li>
 *   <li>数据存储与检索</li>
 *   <li>分享链接管理</li>
 * </ul>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface PrivateResourceService {

    /**
     * 安装技能
     *
     * @param skillPackage 技能包信息
     * @return 安装操作结果
     */
    CompletableFuture<Void> installSkill(SkillPackage skillPackage);

    /**
     * 卸载技能
     *
     * @param skillId 技能 ID
     * @return 卸载操作结果
     */
    CompletableFuture<Void> uninstallSkill(String skillId);

    /**
     * 列出已安装的技能
     *
     * @return 技能列表
     */
    CompletableFuture<List<SkillInfo>> listInstalledSkills();

    /**
     * 执行技能
     *
     * @param skillId 技能 ID
     * @param params 执行参数
     * @return 执行结果
     */
    CompletableFuture<SkillResult> executeSkill(String skillId, Map<String, Object> params);

    /**
     * 配置存储
     *
     * @param config 存储配置
     * @return 配置操作结果
     */
    CompletableFuture<Void> configureStorage(StorageConfig config);

    /**
     * 获取存储状态
     *
     * @return 存储状态
     */
    CompletableFuture<StorageStatus> getStorageStatus();

    /**
     * 存储数据
     *
     * @param key 数据键
     * @param data 数据内容
     * @return 存储操作结果
     */
    CompletableFuture<Void> storeData(String key, byte[] data);

    /**
     * 检索数据
     *
     * @param key 数据键
     * @return 数据内容
     */
    CompletableFuture<byte[]> retrieveData(String key);

    /**
     * 创建分享链接
     *
     * @param resourceId 资源 ID
     * @param config 分享配置
     * @return 分享链接
     */
    CompletableFuture<ShareLink> createShareLink(String resourceId, ShareConfig config);

    /**
     * 撤销分享链接
     *
     * @param shareId 分享 ID
     * @return 撤销操作结果
     */
    CompletableFuture<Void> revokeShareLink(String shareId);
}

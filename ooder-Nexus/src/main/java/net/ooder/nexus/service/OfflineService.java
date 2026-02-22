package net.ooder.nexus.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 离线服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供离线运行能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>离线模式管理</li>
 *   <li>离线能力查询</li>
 *   <li>数据同步</li>
 *   <li>网络状态监听</li>
 * </ul>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface OfflineService {

    /**
     * 启用离线模式
     *
     * @return 启用操作结果
     */
    CompletableFuture<Void> enableOfflineMode();

    /**
     * 禁用离线模式
     *
     * @return 禁用操作结果
     */
    CompletableFuture<Void> disableOfflineMode();

    /**
     * 检查是否处于离线模式
     *
     * @return 是否离线
     */
    boolean isOfflineMode();

    /**
     * 获取离线能力列表
     *
     * @return 离线能力列表
     */
    CompletableFuture<List<OfflineCapability>> getOfflineCapabilities();

    /**
     * 获取待同步项列表
     *
     * @return 待同步项列表
     */
    CompletableFuture<List<PendingSync>> getPendingSyncItems();

    /**
     * 立即同步
     *
     * @return 同步结果
     */
    CompletableFuture<SyncResult> syncNow();

    /**
     * 添加网络状态监听器
     *
     * @param listener 监听器
     */
    void addNetworkListener(NetworkStateListener listener);

    /**
     * 添加同步状态监听器
     *
     * @param listener 监听器
     */
    void addSyncListener(SyncStateListener listener);
}

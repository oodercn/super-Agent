package net.ooder.sdk.storage.sync;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * VFS同步服务接口，提供轻量级的数据同步功能
 */
public interface VfsSyncService {
    /**
     * 同步单个文件到VFS
     * @param key 存储键
     * @return 同步是否成功
     */
    boolean syncToVfs(String key);

    /**
     * 同步多个文件到VFS
     * @param keys 存储键列表
     * @return 同步是否成功
     */
    boolean syncBatchToVfs(List<String> keys);

    /**
     * 从VFS同步单个文件到本地
     * @param key 存储键
     * @return 同步是否成功
     */
    boolean syncFromVfs(String key);

    /**
     * 从VFS同步多个文件到本地
     * @param keys 存储键列表
     * @return 同步是否成功
     */
    boolean syncBatchFromVfs(List<String> keys);

    /**
     * 同步所有本地差异文件到VFS
     * @return 同步是否成功
     */
    boolean syncAllToVfs();

    /**
     * 从VFS同步所有差异文件到本地
     * @return 同步是否成功
     */
    boolean syncAllFromVfs();

    /**
     * 检查并获取需要同步到VFS的差异文件列表
     * @return 需要同步到VFS的差异文件列表
     */
    List<String> getDiffFilesToVfs();

    /**
     * 检查并获取需要从VFS同步的差异文件列表
     * @return 需要从VFS同步的差异文件列表
     */
    List<String> getDiffFilesFromVfs();

    /**
     * 异步同步单个文件到VFS
     * @param key 存储键
     * @return 同步结果的CompletableFuture
     */
    CompletableFuture<Boolean> syncToVfsAsync(String key);

    /**
     * 异步同步单个文件从VFS到本地
     * @param key 存储键
     * @return 同步结果的CompletableFuture
     */
    CompletableFuture<Boolean> syncFromVfsAsync(String key);

    /**
     * 异步同步所有差异文件到VFS
     * @return 同步结果的CompletableFuture
     */
    CompletableFuture<Boolean> syncAllToVfsAsync();

    /**
     * 异步同步所有差异文件从VFS到本地
     * @return 同步结果的CompletableFuture
     */
    CompletableFuture<Boolean> syncAllFromVfsAsync();

    /**
     * 启动定期同步任务
     * @param intervalSeconds 同步间隔（秒）
     */
    void startPeriodicSync(int intervalSeconds);

    /**
     * 停止定期同步任务
     */
    void stopPeriodicSync();

    /**
     * 检查VFS与本地存储之间的数据一致性
     * @return 数据是否一致
     */
    boolean checkConsistency();

    /**
     * 获取同步统计信息
     * @return 同步统计信息
     */
    SyncStats getSyncStats();

    /**
     * 重置同步统计信息
     */
    void resetSyncStats();

    /**
     * 关闭同步服务，释放资源
     */
    void shutdown();
}

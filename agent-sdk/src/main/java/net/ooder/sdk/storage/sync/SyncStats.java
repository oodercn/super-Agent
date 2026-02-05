package net.ooder.sdk.storage.sync;

/**
 * 同步统计信息类，记录同步操作的统计数据
 */
public class SyncStats {
    private long totalSyncToVfsCount = 0;
    private long totalSyncFromVfsCount = 0;
    private long successfulSyncToVfsCount = 0;
    private long successfulSyncFromVfsCount = 0;
    private long failedSyncToVfsCount = 0;
    private long failedSyncFromVfsCount = 0;
    private long totalBytesSyncedToVfs = 0;
    private long totalBytesSyncedFromVfs = 0;
    private long lastSyncTime = 0;
    private long lastSuccessfulSyncTime = 0;

    /**
     * 获取总同步到VFS的次数
     * @return 总同步到VFS的次数
     */
    public long getTotalSyncToVfsCount() {
        return totalSyncToVfsCount;
    }

    /**
     * 设置总同步到VFS的次数
     * @param totalSyncToVfsCount 总同步到VFS的次数
     */
    public void setTotalSyncToVfsCount(long totalSyncToVfsCount) {
        this.totalSyncToVfsCount = totalSyncToVfsCount;
    }

    /**
     * 增加总同步到VFS的次数
     */
    public void incrementTotalSyncToVfsCount() {
        this.totalSyncToVfsCount++;
    }

    /**
     * 获取总从VFS同步的次数
     * @return 总从VFS同步的次数
     */
    public long getTotalSyncFromVfsCount() {
        return totalSyncFromVfsCount;
    }

    /**
     * 设置总从VFS同步的次数
     * @param totalSyncFromVfsCount 总从VFS同步的次数
     */
    public void setTotalSyncFromVfsCount(long totalSyncFromVfsCount) {
        this.totalSyncFromVfsCount = totalSyncFromVfsCount;
    }

    /**
     * 增加总从VFS同步的次数
     */
    public void incrementTotalSyncFromVfsCount() {
        this.totalSyncFromVfsCount++;
    }

    /**
     * 获取成功同步到VFS的次数
     * @return 成功同步到VFS的次数
     */
    public long getSuccessfulSyncToVfsCount() {
        return successfulSyncToVfsCount;
    }

    /**
     * 设置成功同步到VFS的次数
     * @param successfulSyncToVfsCount 成功同步到VFS的次数
     */
    public void setSuccessfulSyncToVfsCount(long successfulSyncToVfsCount) {
        this.successfulSyncToVfsCount = successfulSyncToVfsCount;
    }

    /**
     * 增加成功同步到VFS的次数
     */
    public void incrementSuccessfulSyncToVfsCount() {
        this.successfulSyncToVfsCount++;
    }

    /**
     * 获取成功从VFS同步的次数
     * @return 成功从VFS同步的次数
     */
    public long getSuccessfulSyncFromVfsCount() {
        return successfulSyncFromVfsCount;
    }

    /**
     * 设置成功从VFS同步的次数
     * @param successfulSyncFromVfsCount 成功从VFS同步的次数
     */
    public void setSuccessfulSyncFromVfsCount(long successfulSyncFromVfsCount) {
        this.successfulSyncFromVfsCount = successfulSyncFromVfsCount;
    }

    /**
     * 增加成功从VFS同步的次数
     */
    public void incrementSuccessfulSyncFromVfsCount() {
        this.successfulSyncFromVfsCount++;
    }

    /**
     * 获取失败同步到VFS的次数
     * @return 失败同步到VFS的次数
     */
    public long getFailedSyncToVfsCount() {
        return failedSyncToVfsCount;
    }

    /**
     * 设置失败同步到VFS的次数
     * @param failedSyncToVfsCount 失败同步到VFS的次数
     */
    public void setFailedSyncToVfsCount(long failedSyncToVfsCount) {
        this.failedSyncToVfsCount = failedSyncToVfsCount;
    }

    /**
     * 增加失败同步到VFS的次数
     */
    public void incrementFailedSyncToVfsCount() {
        this.failedSyncToVfsCount++;
    }

    /**
     * 获取失败从VFS同步的次数
     * @return 失败从VFS同步的次数
     */
    public long getFailedSyncFromVfsCount() {
        return failedSyncFromVfsCount;
    }

    /**
     * 设置失败从VFS同步的次数
     * @param failedSyncFromVfsCount 失败从VFS同步的次数
     */
    public void setFailedSyncFromVfsCount(long failedSyncFromVfsCount) {
        this.failedSyncFromVfsCount = failedSyncFromVfsCount;
    }

    /**
     * 增加失败从VFS同步的次数
     */
    public void incrementFailedSyncFromVfsCount() {
        this.failedSyncFromVfsCount++;
    }

    /**
     * 获取总同步到VFS的字节数
     * @return 总同步到VFS的字节数
     */
    public long getTotalBytesSyncedToVfs() {
        return totalBytesSyncedToVfs;
    }

    /**
     * 设置总同步到VFS的字节数
     * @param totalBytesSyncedToVfs 总同步到VFS的字节数
     */
    public void setTotalBytesSyncedToVfs(long totalBytesSyncedToVfs) {
        this.totalBytesSyncedToVfs = totalBytesSyncedToVfs;
    }

    /**
     * 增加总同步到VFS的字节数
     * @param bytes 要增加的字节数
     */
    public void addTotalBytesSyncedToVfs(long bytes) {
        this.totalBytesSyncedToVfs += bytes;
    }

    /**
     * 获取总从VFS同步的字节数
     * @return 总从VFS同步的字节数
     */
    public long getTotalBytesSyncedFromVfs() {
        return totalBytesSyncedFromVfs;
    }

    /**
     * 设置总从VFS同步的字节数
     * @param totalBytesSyncedFromVfs 总从VFS同步的字节数
     */
    public void setTotalBytesSyncedFromVfs(long totalBytesSyncedFromVfs) {
        this.totalBytesSyncedFromVfs = totalBytesSyncedFromVfs;
    }

    /**
     * 增加总从VFS同步的字节数
     * @param bytes 要增加的字节数
     */
    public void addTotalBytesSyncedFromVfs(long bytes) {
        this.totalBytesSyncedFromVfs += bytes;
    }

    /**
     * 获取最后同步时间
     * @return 最后同步时间（毫秒时间戳）
     */
    public long getLastSyncTime() {
        return lastSyncTime;
    }

    /**
     * 设置最后同步时间
     * @param lastSyncTime 最后同步时间（毫秒时间戳）
     */
    public void setLastSyncTime(long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    /**
     * 获取最后成功同步时间
     * @return 最后成功同步时间（毫秒时间戳）
     */
    public long getLastSuccessfulSyncTime() {
        return lastSuccessfulSyncTime;
    }

    /**
     * 设置最后成功同步时间
     * @param lastSuccessfulSyncTime 最后成功同步时间（毫秒时间戳）
     */
    public void setLastSuccessfulSyncTime(long lastSuccessfulSyncTime) {
        this.lastSuccessfulSyncTime = lastSuccessfulSyncTime;
    }

    /**
     * 重置所有统计信息
     */
    public void reset() {
        this.totalSyncToVfsCount = 0;
        this.totalSyncFromVfsCount = 0;
        this.successfulSyncToVfsCount = 0;
        this.successfulSyncFromVfsCount = 0;
        this.failedSyncToVfsCount = 0;
        this.failedSyncFromVfsCount = 0;
        this.totalBytesSyncedToVfs = 0;
        this.totalBytesSyncedFromVfs = 0;
        this.lastSyncTime = 0;
        this.lastSuccessfulSyncTime = 0;
    }

    @Override
    public String toString() {
        return "SyncStats{" +
                "totalSyncToVfsCount=" + totalSyncToVfsCount +
                ", totalSyncFromVfsCount=" + totalSyncFromVfsCount +
                ", successfulSyncToVfsCount=" + successfulSyncToVfsCount +
                ", successfulSyncFromVfsCount=" + successfulSyncFromVfsCount +
                ", failedSyncToVfsCount=" + failedSyncToVfsCount +
                ", failedSyncFromVfsCount=" + failedSyncFromVfsCount +
                ", totalBytesSyncedToVfs=" + totalBytesSyncedToVfs +
                ", totalBytesSyncedFromVfs=" + totalBytesSyncedFromVfs +
                ", lastSyncTime=" + lastSyncTime +
                ", lastSuccessfulSyncTime=" + lastSuccessfulSyncTime +
                '}';
    }
}
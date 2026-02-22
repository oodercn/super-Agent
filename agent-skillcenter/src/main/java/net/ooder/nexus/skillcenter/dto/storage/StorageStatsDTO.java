package net.ooder.nexus.skillcenter.dto.storage;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class StorageStatsDTO extends BaseDTO {

    private long totalSize;
    private String totalSizeHuman;
    private long totalFiles;
    private long totalDirectories;
    private String path;

    public StorageStatsDTO() {}

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public String getTotalSizeHuman() {
        return totalSizeHuman;
    }

    public void setTotalSizeHuman(String totalSizeHuman) {
        this.totalSizeHuman = totalSizeHuman;
    }

    public long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public long getTotalDirectories() {
        return totalDirectories;
    }

    public void setTotalDirectories(long totalDirectories) {
        this.totalDirectories = totalDirectories;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

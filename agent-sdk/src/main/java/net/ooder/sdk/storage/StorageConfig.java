package net.ooder.sdk.storage;

/**
 * 存储配置类，用于配置存储服务的各种参数
 */
public class StorageConfig {
    private String rootDirectory = "ooder-agent";
    private boolean enableCompression = false;
    private boolean enableBackup = false;
    private int backupInterval = 3600000; // 默认1小时
    private boolean enableFileLock = true;
    private int maxFileSize = 104857600; // 默认100MB
    private VfsConfig vfsConfig = new VfsConfig(); // VFS配置

    public StorageConfig() {
    }

    public StorageConfig(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    // Getters and setters
    public String getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public boolean isEnableCompression() {
        return enableCompression;
    }

    public void setEnableCompression(boolean enableCompression) {
        this.enableCompression = enableCompression;
    }

    public boolean isEnableBackup() {
        return enableBackup;
    }

    public void setEnableBackup(boolean enableBackup) {
        this.enableBackup = enableBackup;
    }

    public int getBackupInterval() {
        return backupInterval;
    }

    public void setBackupInterval(int backupInterval) {
        this.backupInterval = backupInterval;
    }

    public boolean isEnableFileLock() {
        return enableFileLock;
    }

    public void setEnableFileLock(boolean enableFileLock) {
        this.enableFileLock = enableFileLock;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public VfsConfig getVfsConfig() {
        return vfsConfig;
    }

    public void setVfsConfig(VfsConfig vfsConfig) {
        this.vfsConfig = vfsConfig;
    }

    // Builder pattern
    public static StorageConfigBuilder builder() {
        return new StorageConfigBuilder();
    }

    public static class StorageConfigBuilder {
        private StorageConfig config = new StorageConfig();

        public StorageConfigBuilder rootDirectory(String rootDirectory) {
            config.setRootDirectory(rootDirectory);
            return this;
        }

        public StorageConfigBuilder enableCompression(boolean enableCompression) {
            config.setEnableCompression(enableCompression);
            return this;
        }

        public StorageConfigBuilder enableBackup(boolean enableBackup) {
            config.setEnableBackup(enableBackup);
            return this;
        }

        public StorageConfigBuilder backupInterval(int backupInterval) {
            config.setBackupInterval(backupInterval);
            return this;
        }

        public StorageConfigBuilder enableFileLock(boolean enableFileLock) {
            config.setEnableFileLock(enableFileLock);
            return this;
        }

        public StorageConfigBuilder maxFileSize(int maxFileSize) {
            config.setMaxFileSize(maxFileSize);
            return this;
        }

        public StorageConfigBuilder vfsConfig(VfsConfig vfsConfig) {
            config.setVfsConfig(vfsConfig);
            return this;
        }

        public StorageConfig build() {
            return config;
        }
    }
}
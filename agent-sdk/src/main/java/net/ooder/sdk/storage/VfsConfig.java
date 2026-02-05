package net.ooder.sdk.storage;

/**
 * VFS配置类，用于存储VFS服务器的连接信息和相关配置
 */
public class VfsConfig {
    private String vfsServerUrl = "http://localhost:8080/vfs";
    private String vfsServerAddress = "127.0.0.1";
    private int vfsServerPort = 8080;
    private String groupName = "default-group";
    private boolean enableVfs = true;
    private long vfsConnectionTimeout = 5000; // 5秒
    private long vfsReadTimeout = 30000; // 30秒
    private int vfsRetryCount = 3;
    private boolean enableVfsFallback = true; // VFS不可用时是否回退到本地存储

    public VfsConfig() {
    }

    public VfsConfig(String vfsServerUrl, String groupName) {
        this.vfsServerUrl = vfsServerUrl;
        this.groupName = groupName;
    }

    // Getters and setters
    public String getVfsServerUrl() {
        return vfsServerUrl;
    }

    public void setVfsServerUrl(String vfsServerUrl) {
        this.vfsServerUrl = vfsServerUrl;
    }

    public String getVfsServerAddress() {
        return vfsServerAddress;
    }

    public void setVfsServerAddress(String vfsServerAddress) {
        this.vfsServerAddress = vfsServerAddress;
    }

    public int getVfsServerPort() {
        return vfsServerPort;
    }

    public void setVfsServerPort(int vfsServerPort) {
        this.vfsServerPort = vfsServerPort;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isEnableVfs() {
        return enableVfs;
    }

    public void setEnableVfs(boolean enableVfs) {
        this.enableVfs = enableVfs;
    }

    public long getVfsConnectionTimeout() {
        return vfsConnectionTimeout;
    }

    public void setVfsConnectionTimeout(long vfsConnectionTimeout) {
        this.vfsConnectionTimeout = vfsConnectionTimeout;
    }

    public long getVfsReadTimeout() {
        return vfsReadTimeout;
    }

    public void setVfsReadTimeout(long vfsReadTimeout) {
        this.vfsReadTimeout = vfsReadTimeout;
    }

    public int getVfsRetryCount() {
        return vfsRetryCount;
    }

    public void setVfsRetryCount(int vfsRetryCount) {
        this.vfsRetryCount = vfsRetryCount;
    }

    public boolean isEnableVfsFallback() {
        return enableVfsFallback;
    }

    public void setEnableVfsFallback(boolean enableVfsFallback) {
        this.enableVfsFallback = enableVfsFallback;
    }

    // Builder pattern
    public static VfsConfigBuilder builder() {
        return new VfsConfigBuilder();
    }

    public static class VfsConfigBuilder {
        private VfsConfig config = new VfsConfig();

        public VfsConfigBuilder vfsServerUrl(String vfsServerUrl) {
            config.setVfsServerUrl(vfsServerUrl);
            return this;
        }

        public VfsConfigBuilder vfsServerAddress(String vfsServerAddress) {
            config.setVfsServerAddress(vfsServerAddress);
            return this;
        }

        public VfsConfigBuilder vfsServerPort(int vfsServerPort) {
            config.setVfsServerPort(vfsServerPort);
            return this;
        }

        public VfsConfigBuilder groupName(String groupName) {
            config.setGroupName(groupName);
            return this;
        }

        public VfsConfigBuilder enableVfs(boolean enableVfs) {
            config.setEnableVfs(enableVfs);
            return this;
        }

        public VfsConfigBuilder vfsConnectionTimeout(long vfsConnectionTimeout) {
            config.setVfsConnectionTimeout(vfsConnectionTimeout);
            return this;
        }

        public VfsConfigBuilder vfsReadTimeout(long vfsReadTimeout) {
            config.setVfsReadTimeout(vfsReadTimeout);
            return this;
        }

        public VfsConfigBuilder vfsRetryCount(int vfsRetryCount) {
            config.setVfsRetryCount(vfsRetryCount);
            return this;
        }

        public VfsConfigBuilder enableVfsFallback(boolean enableVfsFallback) {
            config.setEnableVfsFallback(enableVfsFallback);
            return this;
        }

        public VfsConfig build() {
            return config;
        }
    }
}
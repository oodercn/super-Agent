package net.ooder.nexus.skillcenter.dto.storage;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class StorageSettingsDTO extends BaseDTO {

    private String storagePath;
    private BackupSettingsDTO backup;
    private LimitSettingsDTO limits;
    private boolean storageExists;

    public StorageSettingsDTO() {}

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public BackupSettingsDTO getBackup() {
        return backup;
    }

    public void setBackup(BackupSettingsDTO backup) {
        this.backup = backup;
    }

    public LimitSettingsDTO getLimits() {
        return limits;
    }

    public void setLimits(LimitSettingsDTO limits) {
        this.limits = limits;
    }

    public boolean isStorageExists() {
        return storageExists;
    }

    public void setStorageExists(boolean storageExists) {
        this.storageExists = storageExists;
    }

    public static class BackupSettingsDTO {
        private String backupPath;
        private boolean autoBackup;
        private int backupInterval;
        private int maxBackupCount;

        public String getBackupPath() {
            return backupPath;
        }

        public void setBackupPath(String backupPath) {
            this.backupPath = backupPath;
        }

        public boolean isAutoBackup() {
            return autoBackup;
        }

        public void setAutoBackup(boolean autoBackup) {
            this.autoBackup = autoBackup;
        }

        public int getBackupInterval() {
            return backupInterval;
        }

        public void setBackupInterval(int backupInterval) {
            this.backupInterval = backupInterval;
        }

        public int getMaxBackupCount() {
            return maxBackupCount;
        }

        public void setMaxBackupCount(int maxBackupCount) {
            this.maxBackupCount = maxBackupCount;
        }
    }

    public static class LimitSettingsDTO {
        private String maxStorageSize;
        private String maxFileSize;
        private String[] allowedFileTypes;

        public String getMaxStorageSize() {
            return maxStorageSize;
        }

        public void setMaxStorageSize(String maxStorageSize) {
            this.maxStorageSize = maxStorageSize;
        }

        public String getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(String maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public String[] getAllowedFileTypes() {
            return allowedFileTypes;
        }

        public void setAllowedFileTypes(String[] allowedFileTypes) {
            this.allowedFileTypes = allowedFileTypes;
        }
    }
}

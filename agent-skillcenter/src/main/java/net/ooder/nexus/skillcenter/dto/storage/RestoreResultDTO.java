package net.ooder.nexus.skillcenter.dto.storage;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class RestoreResultDTO extends BaseDTO {

    private boolean success;
    private String message;
    private String backupFile;

    public RestoreResultDTO() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(String backupFile) {
        this.backupFile = backupFile;
    }
}

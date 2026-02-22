package net.ooder.nexus.skillcenter.dto.storage;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class CleanBackupsResultDTO extends BaseDTO {

    private boolean success;
    private String message;
    private int deletedCount;

    public CleanBackupsResultDTO() {}

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

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }
}

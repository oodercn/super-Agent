package net.ooder.nexus.skillcenter.dto.system;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class ClearCacheResultDTO extends BaseDTO {

    private boolean success;
    private String message;
    private int clearedItems;
    private Long timestamp;

    public ClearCacheResultDTO() {}

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

    public int getClearedItems() {
        return clearedItems;
    }

    public void setClearedItems(int clearedItems) {
        this.clearedItems = clearedItems;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

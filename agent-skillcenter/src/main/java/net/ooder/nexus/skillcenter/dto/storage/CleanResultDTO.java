package net.ooder.nexus.skillcenter.dto.storage;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class CleanResultDTO extends BaseDTO {

    private boolean success;
    private String message;

    public CleanResultDTO() {}

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
}

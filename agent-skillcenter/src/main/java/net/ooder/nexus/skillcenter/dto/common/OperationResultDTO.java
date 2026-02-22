package net.ooder.nexus.skillcenter.dto.common;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class OperationResultDTO extends BaseDTO {

    private boolean success;
    private String message;
    private Long timestamp;

    public OperationResultDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    public OperationResultDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static OperationResultDTO success(String message) {
        return new OperationResultDTO(true, message);
    }

    public static OperationResultDTO failure(String message) {
        return new OperationResultDTO(false, message);
    }

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

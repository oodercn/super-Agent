package net.ooder.sdk.api.protocol;

/**
 * Command Result for Protocol Hub
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class CommandResult {

    private boolean success;
    private String message;
    private Object data;
    private String errorCode;
    private long processingTime;
    private String responderId;

    public CommandResult() {
        this.processingTime = 0;
    }

    public static CommandResult success() {
        CommandResult result = new CommandResult();
        result.setSuccess(true);
        result.setMessage("Success");
        return result;
    }

    public static CommandResult success(Object data) {
        CommandResult result = success();
        result.setData(data);
        return result;
    }

    public static CommandResult success(String message, Object data) {
        CommandResult result = success(data);
        result.setMessage(message);
        return result;
    }

    public static CommandResult failure(String message) {
        CommandResult result = new CommandResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    public static CommandResult failure(String errorCode, String message) {
        CommandResult result = failure(message);
        result.setErrorCode(errorCode);
        return result;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public long getProcessingTime() { return processingTime; }
    public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }

    public String getResponderId() { return responderId; }
    public void setResponderId(String responderId) { this.responderId = responderId; }

    @Override
    public String toString() {
        return "CommandResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", processingTime=" + processingTime +
                '}';
    }
}

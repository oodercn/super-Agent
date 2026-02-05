
package net.ooder.sdk.enums;

public enum ErrorCode {
    SERVICE_UNAVAILABLE(1001, "Service unavailable", RetryStrategy.EXPONENTIAL_BACKOFF),
    PARAMETER_ERROR(1002, "Parameter error", RetryStrategy.NO_RETRY),
    EXECUTION_TIMEOUT(1003, "Execution timeout", RetryStrategy.LIMITED_RETRY),
    PERMISSION_DENIED(1004, "Permission denied", RetryStrategy.NO_RETRY),
    NETWORK_ERROR(1005, "Network error", RetryStrategy.EXPONENTIAL_BACKOFF),
    RESOURCE_INSUFFICIENT(1006, "Resource insufficient", RetryStrategy.DELAYED_RETRY),
    COMMAND_NOT_SUPPORTED(1007, "Command not supported", RetryStrategy.NO_RETRY),
    INTERNAL_ERROR(1008, "Internal error", RetryStrategy.LIMITED_RETRY),
    VFS_ERROR(1009, "VFS operation failed", RetryStrategy.LIMITED_RETRY),
    SCENE_ERROR(1010, "Scene operation failed", RetryStrategy.LIMITED_RETRY);

    private final int code;
    private final String message;
    private final RetryStrategy retryStrategy;

    ErrorCode(int code, String message, RetryStrategy retryStrategy) {
        this.code = code;
        this.message = message;
        this.retryStrategy = retryStrategy;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    @Override
    public String toString() {
        return code + ": " + message;
    }

    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("Unknown error code: " + code);
    }

    public enum RetryStrategy {
        NO_RETRY,
        LIMITED_RETRY,
        EXPONENTIAL_BACKOFF,
        DELAYED_RETRY
    }
}

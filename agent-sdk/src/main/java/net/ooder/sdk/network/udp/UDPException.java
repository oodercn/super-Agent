package net.ooder.sdk.network.udp;

import com.alibaba.fastjson.annotation.JSONField;

public class UDPException extends Exception {
    private ErrorCode errorCode;
    private String details;

    public enum ErrorCode {
        @JSONField(ordinal = 0) INVALID_VERSION("版本号无效"),
        @JSONField(ordinal = 1) INVALID_MESSAGE_ID("消息ID无效"),
        @JSONField(ordinal = 2) INVALID_TIMESTAMP("时间戳无效"),
        @JSONField(ordinal = 3) INVALID_PACKET_SIZE("包大小超出限制"),
        @JSONField(ordinal = 4) INVALID_FORMAT("格式无效"),
        @JSONField(ordinal = 5) VALIDATION_FAILED("验证失败"),
        @JSONField(ordinal = 6) SERIALIZATION_ERROR("序列化错误"),
        @JSONField(ordinal = 7) DESERIALIZATION_ERROR("反序列化错误");

        private final String description;

        ErrorCode(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public UDPException(ErrorCode errorCode, String details) {
        super(errorCode.getDescription() + ": " + details);
        this.errorCode = errorCode;
        this.details = details;
    }

    public UDPException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getDescription() + ": " + details, cause);
        this.errorCode = errorCode;
        this.details = details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}
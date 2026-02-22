package net.ooder.nexus.skillcenter.exception;

/**
 * 业务异常类 - 符合 ooderNexus 规范
 * 用于处理业务逻辑中的异常情况
 */
public class BusinessException extends RuntimeException {
    
    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
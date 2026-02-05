package net.ooder.examples.skillc.model;

import java.io.Serializable;

public class ErrorResponse implements Serializable {
    private String status;
    private int code;
    private int httpStatus;
    private String message;
    private ErrorDetail detail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorDetail getDetail() {
        return detail;
    }

    public void setDetail(ErrorDetail detail) {
        this.detail = detail;
    }
}
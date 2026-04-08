package net.ooder.skill.discovery.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResultModel<T> {
    
    private int code;
    private String status;
    private String message;
    private T data;
    private String timestamp;
    private String requestId;

    public ResultModel() {
        this.code = CODE_SUCCESS;
        this.status = "success";
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.requestId = generateRequestId();
    }

    public ResultModel(int code, String message, T data, boolean success, String requestId) {
        this.code = code;
        this.status = success ? "success" : "error";
        this.message = message;
        this.data = data;
        this.requestId = requestId;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_METHOD_NOT_ALLOWED = 405;
    public static final int CODE_CONFLICT = 409;
    public static final int CODE_INTERNAL_SERVER_ERROR = 500;
    public static final int CODE_SERVICE_UNAVAILABLE = 503;

    public static <T> ResultModel<T> success(T data) {
        return new ResultModel<>(CODE_SUCCESS, "操作成功", data, true, generateRequestId());
    }

    public static <T> ResultModel<T> success() {
        return new ResultModel<>(CODE_SUCCESS, "操作成功", null, true, generateRequestId());
    }

    public static <T> ResultModel<T> success(String message, T data) {
        return new ResultModel<>(CODE_SUCCESS, message, data, true, generateRequestId());
    }

    public static <T> ResultModel<T> error(int code, String message) {
        return new ResultModel<>(code, message, null, false, generateRequestId());
    }

    public static <T> ResultModel<T> error(String message) {
        return new ResultModel<>(CODE_INTERNAL_SERVER_ERROR, message, null, false, generateRequestId());
    }

    public static <T> ResultModel<T> badRequest(String message) {
        return new ResultModel<>(CODE_BAD_REQUEST, message, null, false, generateRequestId());
    }

    public static <T> ResultModel<T> unauthorized(String message) {
        return new ResultModel<>(CODE_UNAUTHORIZED, message, null, false, generateRequestId());
    }

    public static <T> ResultModel<T> forbidden(String message) {
        return new ResultModel<>(CODE_FORBIDDEN, message, null, false, generateRequestId());
    }

    public static <T> ResultModel<T> notFound(String message) {
        return new ResultModel<>(CODE_NOT_FOUND, message, null, false, generateRequestId());
    }

    public static <T> ResultModel<T> conflict(String message) {
        return new ResultModel<>(CODE_CONFLICT, message, null, false, generateRequestId());
    }

    public static <T> ResultModel<T> serviceUnavailable(String message) {
        return new ResultModel<>(CODE_SERVICE_UNAVAILABLE, message, null, false, generateRequestId());
    }

    private static String generateRequestId() {
        return "REQ_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }
}

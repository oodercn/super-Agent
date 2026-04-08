package net.ooder.skill.install.model;

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

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_INTERNAL_SERVER_ERROR = 500;

    public static <T> ResultModel<T> success(T data) {
        return new ResultModel<>(CODE_SUCCESS, "操作成功", data, true, generateRequestId());
    }

    public static <T> ResultModel<T> success() {
        return new ResultModel<>(CODE_SUCCESS, "操作成功", null, true, generateRequestId());
    }

    public static <T> ResultModel<T> error(String message) {
        return new ResultModel<>(CODE_INTERNAL_SERVER_ERROR, message, null, false, generateRequestId());
    }

    private static String generateRequestId() {
        return "REQ_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }
}

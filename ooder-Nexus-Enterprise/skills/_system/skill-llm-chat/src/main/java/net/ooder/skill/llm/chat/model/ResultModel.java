package net.ooder.skill.llm.chat.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResultModel<T> {
    
    private int code;
    private String status;
    private String message;
    private T data;
    private String timestamp;
    
    public ResultModel() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
    
    public ResultModel(int code, String status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
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
    
    public static <T> ResultModel<T> success(T data) {
        return new ResultModel<>(200, "success", "操作成功", data);
    }
    
    public static <T> ResultModel<T> success(String message, T data) {
        return new ResultModel<>(200, "success", message, data);
    }
    
    public static <T> ResultModel<T> error(int code, String message) {
        return new ResultModel<>(code, "error", message, null);
    }
    
    public static <T> ResultModel<T> error(String message) {
        return new ResultModel<>(500, "error", message, null);
    }
    
    public static <T> ResultModel<T> badRequest(String message) {
        return new ResultModel<>(400, "error", message, null);
    }
    
    public static <T> ResultModel<T> notFound(String message) {
        return new ResultModel<>(404, "error", message, null);
    }
}

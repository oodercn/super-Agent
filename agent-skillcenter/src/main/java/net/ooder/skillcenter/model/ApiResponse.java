package net.ooder.skillcenter.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一API响应模型 - 符合 ooderNexus 规范
 * @param <T> 数据类型
 */
public class ApiResponse<T> {
    
    private int code; // 状态码
    private String status; // 状态
    private String message; // 消息
    private T data; // 数据
    private String timestamp; // 时间戳
    private String requestId; // 请求ID
    private boolean success; // 是否成功

    // 构造方法
    public ApiResponse(int code, String message, T data, boolean success, String requestId) {
        this.code = code;
        this.status = success ? "success" : "error";
        this.message = message;
        this.data = data;
        this.success = success;
        this.requestId = requestId;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    // Getters and Setters
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // 响应状态码常量
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_METHOD_NOT_ALLOWED = 405;
    public static final int CODE_CONFLICT = 409;
    public static final int CODE_INTERNAL_SERVER_ERROR = 500;
    public static final int CODE_SERVICE_UNAVAILABLE = 503;

    /**
     * 成功响应
     * @param data 数据
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(CODE_SUCCESS, "success", data, true, generateRequestId());
    }

    /**
     * 成功响应（带消息）
     * @param message 消息
     * @param data 数据
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(CODE_SUCCESS, message, data, true, generateRequestId());
    }

    /**
     * 错误响应
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null, false, generateRequestId());
    }

    /**
     * 错误响应（默认错误码）
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(CODE_INTERNAL_SERVER_ERROR, message, null, false, generateRequestId());
    }

    /**
     * 400错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(CODE_BAD_REQUEST, message, null, false, generateRequestId());
    }

    /**
     * 401错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(CODE_UNAUTHORIZED, message, null, false, generateRequestId());
    }

    /**
     * 403错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(CODE_FORBIDDEN, message, null, false, generateRequestId());
    }

    /**
     * 404错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(CODE_NOT_FOUND, message, null, false, generateRequestId());
    }

    /**
     * 409错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(CODE_CONFLICT, message, null, false, generateRequestId());
    }

    /**
     * 503错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> serviceUnavailable(String message) {
        return new ApiResponse<>(CODE_SERVICE_UNAVAILABLE, message, null, false, generateRequestId());
    }

    /**
     * 生成请求ID
     * @return 请求ID
     */
    private static String generateRequestId() {
        return "REQ_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }
}


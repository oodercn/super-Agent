package net.ooder.nexus.common;

import java.io.Serializable;

/**
 * 统一响应结果模型 (2.0 规范)
 *
 * <p>所有 API 接口统一使用此模型返回数据</p>
 *
 * <p><strong>规范说明：</strong></p>
 * <ul>
 *   <li>code: 状态码，200 表示成功，其他表示错误</li>
 *   <li>status: 状态标识，"success" 或 "error"</li>
 *   <li>message: 响应消息</li>
 *   <li>data: 响应数据</li>
 *   <li>timestamp: 时间戳</li>
 * </ul>
 *
 * @param <T> 数据类型
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
public class ResultModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码：200 表示成功，其他表示错误 */
    private int code;

    /** 状态标识：success 或 error */
    private String status;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳 */
    private long timestamp;

    /**
     * 默认构造函数
     */
    public ResultModel() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 全参数构造函数
     */
    public ResultModel(int code, String status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 创建成功响应（无数据）
     */
    public static <T> ResultModel<T> success() {
        return new ResultModel<>(200, "success", "操作成功", null);
    }

    /**
     * 创建成功响应（带数据）
     */
    public static <T> ResultModel<T> success(T data) {
        return new ResultModel<>(200, "success", "操作成功", data);
    }

    /**
     * 创建成功响应（带消息和数据）
     */
    public static <T> ResultModel<T> success(String message, T data) {
        return new ResultModel<>(200, "success", message, data);
    }

    /**
     * 创建错误响应（默认消息）
     */
    public static <T> ResultModel<T> error() {
        return new ResultModel<>(500, "error", "操作失败", null);
    }

    /**
     * 创建错误响应（带消息）
     */
    public static <T> ResultModel<T> error(String message) {
        return new ResultModel<>(500, "error", message, null);
    }

    /**
     * 创建错误响应（带消息和状态码）
     */
    public static <T> ResultModel<T> error(String message, int code) {
        return new ResultModel<>(code, "error", message, null);
    }

    /**
     * 创建错误响应（带消息、数据和状态码）
     */
    public static <T> ResultModel<T> error(String message, T data, int code) {
        return new ResultModel<>(code, "error", message, data);
    }

    // ==================== Getter 和 Setter ====================

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return "success".equals(this.status) && this.code == 200;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}

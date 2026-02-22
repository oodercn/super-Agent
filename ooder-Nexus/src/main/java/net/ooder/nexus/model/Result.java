package net.ooder.nexus.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用响应结果封装类
 *
 * <p>用于统一封装 API 响应结果，包含状态、消息、数据和状态码。</p>
 *
 * <p><strong>使用示例：</strong></p>
 * <pre>
 * // 成功响应
 * return Result.success(data);
 *
 * // 错误响应
 * return Result.error("操作失败", 500);
 *
 * // 自定义消息
 * return Result.success("查询成功", data);
 * </pre>
 *
 * @param <T> 数据类型
 * @author ooder Team
 * @version 2.0.0-openwrt-preview
 * @since 1.0.0
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 响应状态：success 或 error */
    private String status;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 状态码：200 表示成功，其他表示错误 */
    private int code;

    /** 时间戳 */
    private long timestamp;

    /**
     * 默认构造函数
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 全参数构造函数
     *
     * @param status  响应状态
     * @param message 响应消息
     * @param data    响应数据
     * @param code    状态码
     */
    public Result(String status, String message, T data, int code) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 创建成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        return new Result<>("success", "操作成功", null, 200);
    }

    /**
     * 创建成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>("success", "操作成功", data, 200);
    }

    /**
     * 创建成功响应（带消息和数据）
     *
     * @param message 响应消息
     * @param data    响应数据
     * @param <T>     数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>("success", message, data, 200);
    }

    /**
     * 创建错误响应（默认消息）
     *
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error() {
        return new Result<>("error", "操作失败", null, 500);
    }

    /**
     * 创建错误响应（带消息）
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>("error", message, null, 500);
    }

    /**
     * 创建错误响应（带消息和状态码）
     *
     * @param message 错误消息
     * @param code    状态码
     * @param <T>     数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(String message, int code) {
        return new Result<>("error", message, null, code);
    }

    /**
     * 创建错误响应（带消息、数据和状态码）
     *
     * @param message 错误消息
     * @param data    错误数据
     * @param code    状态码
     * @param <T>     数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(String message, T data, int code) {
        return new Result<>("error", message, data, code);
    }

    // ==================== Getter 和 Setter ====================

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return "success".equals(status);
    }

    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", code=" + code +
                ", timestamp=" + timestamp +
                '}';
    }
}

package net.ooder.nexus.model;

import java.io.Serializable;
import java.util.Date;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
    private T data;
    private int code;
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(String status, String message, T data, int code) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success() {
        return new Result<>("success", "操作成功", null, 200);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>("success", "操作成功", data, 200);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>("success", message, data, 200);
    }

    public static <T> Result<T> error() {
        return new Result<>("error", "操作失败", null, 500);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>("error", message, null, 500);
    }

    public static <T> Result<T> error(String message, int code) {
        return new Result<>("error", message, null, code);
    }

    public static <T> Result<T> error(String message, T data, int code) {
        return new Result<>("error", message, data, code);
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
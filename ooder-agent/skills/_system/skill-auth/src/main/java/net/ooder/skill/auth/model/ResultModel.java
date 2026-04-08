package net.ooder.skill.auth.model;

import java.io.Serializable;

public class ResultModel<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public ResultModel() {}

    public ResultModel(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultModel<T> success(T data) {
        return new ResultModel<>(200, "success", data);
    }

    public static <T> ResultModel<T> success() {
        return new ResultModel<>(200, "success", null);
    }

    public static <T> ResultModel<T> error(String message) {
        return new ResultModel<>(500, message, null);
    }

    public static <T> ResultModel<T> notFound(String message) {
        return new ResultModel<>(404, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
}

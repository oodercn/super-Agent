package net.ooder.enexus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultModel<T> {
    private int code;
    private String status;
    private String message;
    private T data;
    private String error;

    public static <T> ResultModel<T> success(T data) {
        ResultModel<T> result = new ResultModel<>();
        result.code = 200;
        result.status = "success";
        result.data = data;
        return result;
    }

    public static <T> ResultModel<T> error(String message) {
        ResultModel<T> result = new ResultModel<>();
        result.code = 500;
        result.status = "error";
        result.message = message;
        result.error = message;
        return result;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

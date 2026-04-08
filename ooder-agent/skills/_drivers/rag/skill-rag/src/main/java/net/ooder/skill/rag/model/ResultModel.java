package net.ooder.skill.rag.model;

public class ResultModel<T> {

    private int code;
    private String message;
    private T data;
    private long timestamp;

    public ResultModel() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResultModel<T> success(T data) {
        ResultModel<T> result = new ResultModel<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> ResultModel<T> success(T data, String message) {
        ResultModel<T> result = new ResultModel<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> ResultModel<T> error(String message) {
        ResultModel<T> result = new ResultModel<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

package net.ooder.nexus.model;

import java.util.HashMap;
import java.util.Map;

public class SkillResult {
    public enum Status {
        SUCCESS,
        FAILED,
        PARTIAL
    }

    private Status status;
    private String message;
    private Map<String, Object> data;
    private Exception exception;

    public SkillResult() {
        this.status = Status.SUCCESS;
        this.data = new HashMap<>();
    }

    public SkillResult(Status status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    public Object getData(String key) {
        return this.data.get(key);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
        if (exception != null) {
            this.status = Status.FAILED;
            if (this.message == null) {
                this.message = exception.getMessage();
            }
        }
    }

    public boolean isSuccess() {
        return this.status == Status.SUCCESS;
    }

    public boolean isFailed() {
        return this.status == Status.FAILED;
    }

    public boolean isPartial() {
        return this.status == Status.PARTIAL;
    }
}

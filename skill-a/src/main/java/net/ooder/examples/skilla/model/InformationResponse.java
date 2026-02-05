package net.ooder.examples.skilla.model;

import java.io.Serializable;
import java.util.Map;

public class InformationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Map<String, Object> data;
    private String requestId;
    private long timestamp;

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "InformationResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", requestId='" + requestId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

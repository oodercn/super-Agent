package net.ooder.examples.skillb.model;

import java.io.Serializable;
import java.util.Map;

public class DataSubmissionResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private String submissionId;
    private Map<String, Object> result;
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

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
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
        return "DataSubmissionResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", submissionId='" + submissionId + '\'' +
                ", result=" + result +
                ", requestId='" + requestId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

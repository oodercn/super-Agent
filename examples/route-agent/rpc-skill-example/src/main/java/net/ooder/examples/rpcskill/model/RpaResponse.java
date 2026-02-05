package net.ooder.examples.rpaskill.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

/**
 * RPA Response Model
 * Returns data in the specified JSON format
 * 作者：ooderAI agent team   V0.6.0
 */
public class RpaResponse {

    @JSONField(name = "success")
    private boolean success;

    @JSONField(name = "data")
    private Map<String, Object> data;

    @JSONField(name = "message")
    private String message;

    // Default constructor
    public RpaResponse() {
    }

    // Parameterized constructor for success response
    public RpaResponse(boolean success, Map<String, Object> data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // Static factory method for success response
    public static RpaResponse success(Map<String, Object> data, String message) {
        return new RpaResponse(true, data, message);
    }

    // Static factory method for error response
    public static RpaResponse error(String message) {
        return new RpaResponse(false, null, message);
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpaResponse{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + "'" +
                '}';
    }
}

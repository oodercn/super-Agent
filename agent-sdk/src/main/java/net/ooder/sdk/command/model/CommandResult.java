package net.ooder.sdk.command.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令执行结果类，用于封装命令执行的结果
 */
public class CommandResult {
    private String status; // success, failed, parameter_error, execution_error
    private String message;
    private Map<String, Object> data;
    private long timestamp;
    private String errorCode;
    private String errorMessage;
    
    public CommandResult() {
        this.status = "success";
        this.message = "Command executed successfully";
        this.data = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
    }
    
    public CommandResult(String status, String message) {
        this();
        this.status = status;
        this.message = message;
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
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public void putData(String key, Object value) {
        this.data.put(key, value);
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public static CommandResult success() {
        return new CommandResult("success", "Command executed successfully");
    }
    
    public static CommandResult failed(String message) {
        CommandResult result = new CommandResult("failed", message);
        result.setErrorMessage(message);
        return result;
    }
    
    public static CommandResult parameterError(String message) {
        CommandResult result = new CommandResult("parameter_error", message);
        result.setErrorCode("INVALID_PARAMETERS");
        result.setErrorMessage(message);
        return result;
    }
    
    public static CommandResult executionError(String message) {
        CommandResult result = new CommandResult("execution_error", message);
        result.setErrorCode("EXECUTION_FAILED");
        result.setErrorMessage(message);
        return result;
    }
    
    // 带数据的成功结果
    public static CommandResult success(Map<String, Object> data, String message) {
        CommandResult result = new CommandResult("success", message);
        result.setData(data);
        return result;
    }
    
    // 带数据的失败结果
    public static CommandResult failed(String message, Map<String, Object> data) {
        CommandResult result = new CommandResult("failed", message);
        result.setData(data);
        result.setErrorMessage(message);
        return result;
    }
    
    // 带数据的参数错误结果
    public static CommandResult parameterError(String message, Map<String, Object> data) {
        CommandResult result = new CommandResult("parameter_error", message);
        result.setData(data);
        result.setErrorCode("INVALID_PARAMETERS");
        result.setErrorMessage(message);
        return result;
    }
    
    // 带数据的执行错误结果
    public static CommandResult executionError(String message, Map<String, Object> data) {
        CommandResult result = new CommandResult("execution_error", message);
        result.setData(data);
        result.setErrorCode("EXECUTION_FAILED");
        result.setErrorMessage(message);
        return result;
    }
    
    // 安全错误结果
    public static CommandResult securityError(String message, Map<String, Object> data) {
        CommandResult result = new CommandResult("security_error", message);
        result.setData(data);
        result.setErrorCode("SECURITY_VIOLATION");
        result.setErrorMessage(message);
        return result;
    }
    
    /**
     * 将结果转换为Map，用于存储到CommandRecord
     * @return 结果的Map表示
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", data);
        map.put("timestamp", timestamp);
        map.put("errorCode", errorCode);
        map.put("errorMessage", errorMessage);
        return map;
    }
}
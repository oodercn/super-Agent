package net.ooder.skill.config.dto;

public class DbConnectionTestResultDTO {
    
    private boolean success;
    private String message;
    private long responseTime;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getResponseTime() { return responseTime; }
    public void setResponseTime(long responseTime) { this.responseTime = responseTime; }
}

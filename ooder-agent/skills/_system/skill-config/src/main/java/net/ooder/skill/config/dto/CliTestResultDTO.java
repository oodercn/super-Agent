package net.ooder.skill.config.dto;

public class CliTestResultDTO {
    
    private boolean success;
    private String message;
    private String cliId;
    private long timestamp;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCliId() { return cliId; }
    public void setCliId(String cliId) { this.cliId = cliId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

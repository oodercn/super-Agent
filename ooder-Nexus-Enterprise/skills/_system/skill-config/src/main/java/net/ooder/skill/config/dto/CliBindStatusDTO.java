package net.ooder.skill.config.dto;

public class CliBindStatusDTO {
    
    private String cliId;
    private boolean bound;
    private String message;
    private String userId;
    private long bindTime;

    public String getCliId() { return cliId; }
    public void setCliId(String cliId) { this.cliId = cliId; }
    public boolean isBound() { return bound; }
    public void setBound(boolean bound) { this.bound = bound; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public long getBindTime() { return bindTime; }
    public void setBindTime(long bindTime) { this.bindTime = bindTime; }
}

package net.ooder.skill.discovery.dto.discovery;

public class RefreshResultDTO {
    
    private Integer discovered;
    
    private Integer registered;
    
    private String source;
    
    private Long timestamp;
    
    private String error;
    
    private String message;

    public RefreshResultDTO() {}

    public Integer getDiscovered() {
        return discovered;
    }

    public void setDiscovered(Integer discovered) {
        this.discovered = discovered;
    }

    public Integer getRegistered() {
        return registered;
    }

    public void setRegistered(Integer registered) {
        this.registered = registered;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

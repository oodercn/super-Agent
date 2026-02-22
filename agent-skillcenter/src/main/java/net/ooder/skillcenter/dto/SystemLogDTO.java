package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 系统日志数据传输对象
 */
public class SystemLogDTO {

    private String id;
    private String level;
    private String message;
    private String source;
    private Date timestamp;
    private String details;

    public SystemLogDTO() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}

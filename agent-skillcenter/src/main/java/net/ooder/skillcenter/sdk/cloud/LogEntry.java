package net.ooder.skillcenter.sdk.cloud;

public class LogEntry {
    private long timestamp;
    private String level;
    private String message;
    private String source;

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}

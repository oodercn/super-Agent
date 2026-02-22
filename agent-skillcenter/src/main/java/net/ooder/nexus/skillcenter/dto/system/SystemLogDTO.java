package net.ooder.nexus.skillcenter.dto.system;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class SystemLogDTO extends BaseDTO {

    private String id;
    private String level;
    private String message;
    private Long timestamp;
    private String source;

    public SystemLogDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

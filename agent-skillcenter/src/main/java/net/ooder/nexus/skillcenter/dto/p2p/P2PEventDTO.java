package net.ooder.nexus.skillcenter.dto.p2p;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class P2PEventDTO extends BaseDTO {

    private String type;
    private String description;
    private Long timestamp;
    private String level;

    public P2PEventDTO() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

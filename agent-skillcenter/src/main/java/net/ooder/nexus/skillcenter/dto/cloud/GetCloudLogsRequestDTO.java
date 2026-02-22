package net.ooder.nexus.skillcenter.dto.cloud;

public class GetCloudLogsRequestDTO extends CloudInstanceRequestDTO {
    private int limit;
    private String level;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

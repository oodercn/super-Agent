package net.ooder.nexus.dto.personal;

import java.io.Serializable;

public class MediaPublishRecordsQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String platform;
    private String status;
    private Integer page;
    private Integer size;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

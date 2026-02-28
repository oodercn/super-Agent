package net.ooder.nexus.dto.scene;

import java.io.Serializable;

public class SceneListQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private String status;
    private Integer page;
    private Integer size;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

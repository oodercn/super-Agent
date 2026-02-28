package net.ooder.nexus.dto.scene;

import java.io.Serializable;

public class SceneInstanceListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ownerId;
    private Integer page;
    private Integer size;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

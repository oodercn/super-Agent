package net.ooder.nexus.skillcenter.dto.market;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class DownloadRequestDTO extends BaseDTO {

    private String id;

    public DownloadRequestDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

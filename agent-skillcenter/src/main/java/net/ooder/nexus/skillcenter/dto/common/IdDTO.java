package net.ooder.nexus.skillcenter.dto.common;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class IdDTO extends BaseDTO {

    private String id;

    public IdDTO() {}

    public IdDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

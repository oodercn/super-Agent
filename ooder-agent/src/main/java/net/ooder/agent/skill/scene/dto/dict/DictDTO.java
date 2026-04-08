package net.ooder.mvp.skill.scene.dto.dict;

import java.util.List;

public class DictDTO {
    
    private String code;
    private String name;
    private String description;
    private List<DictItemDTO> items;

    public DictDTO() {
    }

    public DictDTO(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DictItemDTO> getItems() {
        return items;
    }

    public void setItems(List<DictItemDTO> items) {
        this.items = items;
    }
}

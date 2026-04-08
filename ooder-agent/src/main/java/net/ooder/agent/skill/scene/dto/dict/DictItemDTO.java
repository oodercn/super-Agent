package net.ooder.mvp.skill.scene.dto.dict;

public class DictItemDTO {
    
    private String code;
    private String name;
    private String description;
    private String icon;
    private int sort;
    private Object extra;

    public DictItemDTO() {
    }

    public DictItemDTO(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public DictItemDTO(String code, String name, String description, String icon, int sort) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.sort = sort;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}

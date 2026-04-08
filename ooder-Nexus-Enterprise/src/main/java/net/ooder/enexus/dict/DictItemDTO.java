package net.ooder.enexus.dict;

public class DictItemDTO {
    private String code;
    private String name;
    private String value;
    private String description;
    private int sort;
    private boolean enabled;

    public DictItemDTO() {}

    public DictItemDTO(String code, String name, String value, int sort) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.sort = sort;
        this.enabled = true;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getSort() { return sort; }
    public void setSort(int sort) { this.sort = sort; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

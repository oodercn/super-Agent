package net.ooder.nexus.dto.capability;

public class CapabilityCategoryDistributionDTO {
    private String name;
    private String code;
    private Integer count;
    private String color;

    public CapabilityCategoryDistributionDTO() {}

    public CapabilityCategoryDistributionDTO(String code, String name, Integer count, String color) {
        this.code = code;
        this.name = name;
        this.count = count;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

package net.ooder.enexus.dto.capability;

public class CapabilityTopDTO {
    private String capabilityId;
    private String name;
    private Integer invokeCount;
    private String type;
    private String category;
    private Double successRate;
    private Boolean installed;

    public CapabilityTopDTO() {}

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInvokeCount() {
        return invokeCount;
    }

    public void setInvokeCount(Integer invokeCount) {
        this.invokeCount = invokeCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Boolean getInstalled() {
        return installed;
    }

    public void setInstalled(Boolean installed) {
        this.installed = installed;
    }
}

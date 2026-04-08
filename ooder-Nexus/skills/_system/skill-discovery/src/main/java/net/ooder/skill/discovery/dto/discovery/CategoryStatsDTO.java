package net.ooder.skill.discovery.dto.discovery;

import java.util.Map;

public class CategoryStatsDTO {
    
    private Integer total;
    
    private Map<String, Long> categories;
    
    private Long sceneCount;
    
    private Long providerCount;
    
    private Long driverCount;
    
    private Long installed;
    
    private Long notInstalled;
    
    private String message;

    public CategoryStatsDTO() {}

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Map<String, Long> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Long> categories) {
        this.categories = categories;
    }

    public Long getSceneCount() {
        return sceneCount;
    }

    public void setSceneCount(Long sceneCount) {
        this.sceneCount = sceneCount;
    }

    public Long getProviderCount() {
        return providerCount;
    }

    public void setProviderCount(Long providerCount) {
        this.providerCount = providerCount;
    }

    public Long getDriverCount() {
        return driverCount;
    }

    public void setDriverCount(Long driverCount) {
        this.driverCount = driverCount;
    }

    public Long getInstalled() {
        return installed;
    }

    public void setInstalled(Long installed) {
        this.installed = installed;
    }

    public Long getNotInstalled() {
        return notInstalled;
    }

    public void setNotInstalled(Long notInstalled) {
        this.notInstalled = notInstalled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

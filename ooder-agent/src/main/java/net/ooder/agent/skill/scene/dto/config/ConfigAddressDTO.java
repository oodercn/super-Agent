package net.ooder.mvp.skill.scene.dto.config;

public class ConfigAddressDTO {
    
    private String address;
    private String name;
    private String category;
    private String driver;
    private Boolean active;
    private String status;
    
    public ConfigAddressDTO() {
    }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

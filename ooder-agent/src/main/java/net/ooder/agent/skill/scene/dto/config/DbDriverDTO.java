package net.ooder.mvp.skill.scene.dto.config;

public class DbDriverDTO {
    
    private String type;
    private String name;
    private String driverClass;
    private Integer defaultPort;
    private String description;
    
    public DbDriverDTO() {
    }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDriverClass() { return driverClass; }
    public void setDriverClass(String driverClass) { this.driverClass = driverClass; }
    
    public Integer getDefaultPort() { return defaultPort; }
    public void setDefaultPort(Integer defaultPort) { this.defaultPort = defaultPort; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

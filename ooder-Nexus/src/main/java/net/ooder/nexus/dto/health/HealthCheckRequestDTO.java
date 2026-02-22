package net.ooder.nexus.dto.health;

import java.io.Serializable;

/**
 * Health check request DTO
 */
public class HealthCheckRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String checkType;
    private Boolean deepCheck;
    private String[] services;

    public String getCheckType() { return checkType; }
    public void setCheckType(String checkType) { this.checkType = checkType; }
    public Boolean getDeepCheck() { return deepCheck; }
    public void setDeepCheck(Boolean deepCheck) { this.deepCheck = deepCheck; }
    public String[] getServices() { return services; }
    public void setServices(String[] services) { this.services = services; }
}

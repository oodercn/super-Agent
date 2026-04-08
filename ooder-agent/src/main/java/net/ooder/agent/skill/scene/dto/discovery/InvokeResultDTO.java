package net.ooder.mvp.skill.scene.dto.discovery;

import java.util.Map;

public class InvokeResultDTO {
    
    private String capabilityId;
    private String name;
    private Long invokedAt;
    private Map<String, Object> params;
    private String status;
    private String message;
    
    public InvokeResultDTO() {
    }
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Long getInvokedAt() { return invokedAt; }
    public void setInvokedAt(Long invokedAt) { this.invokedAt = invokedAt; }
    
    public Map<String, Object> getParams() { return params; }
    public void setParams(Map<String, Object> params) { this.params = params; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

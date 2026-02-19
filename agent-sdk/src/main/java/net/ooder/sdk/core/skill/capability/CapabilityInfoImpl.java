
package net.ooder.sdk.core.skill.capability;

import net.ooder.sdk.api.skill.CapabilityInfo;
import java.util.Map;

public class CapabilityInfoImpl implements CapabilityInfo {
    
    private String capabilityId;
    private String capabilityName;
    private String description;
    private String interfaceId;
    private String endpoint;
    private String protocol;
    private Map<String, Object> config;
    private CapabilityType type;
    private String sceneName;
    
    public CapabilityInfoImpl() {}
    
    public CapabilityInfoImpl(String capabilityId, String capabilityName) {
        this.capabilityId = capabilityId;
        this.capabilityName = capabilityName;
    }
    
    @Override
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    @Override
    public String getCapabilityName() { return capabilityName; }
    public void setCapabilityName(String capabilityName) { this.capabilityName = capabilityName; }
    
    @Override
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String getInterfaceId() { return interfaceId; }
    public void setInterfaceId(String interfaceId) { this.interfaceId = interfaceId; }
    
    @Override
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    
    @Override
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    
    @Override
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    
    @Override
    public CapabilityType getType() { return type; }
    public void setType(CapabilityType type) { this.type = type; }
    
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
}

package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;

import java.util.List;

public interface CapabilityBindingService {

    CapabilityBinding bind(String sceneGroupId, CapabilityBindingRequest request);

    void unbind(String bindingId);

    CapabilityBinding findById(String bindingId);

    List<CapabilityBinding> listBySceneGroup(String sceneGroupId);

    List<CapabilityBinding> listByCapability(String capabilityId);
    
    List<CapabilityBinding> listByAgent(String agentId);
    
    List<CapabilityBinding> listByLink(String linkId);
    
    List<CapabilityBinding> listAll();

    void updateStatus(String bindingId, String status);

    CapabilityBinding findByCapId(String sceneGroupId, String capId);
    
    CapabilityBinding getByCapId(String capId);
    
    void updateInvokeStats(String bindingId, boolean success);

    public static class CapabilityBindingRequest {
        private String capabilityId;
        private String capDefId;
        private String capId;
        private String providerType;
        private String providerId;
        private String connectorType;
        private int priority;
        private boolean fallback;

        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getCapDefId() { return capDefId; }
        public void setCapDefId(String capDefId) { this.capDefId = capDefId; }
        public String getCapId() { return capId; }
        public void setCapId(String capId) { this.capId = capId; }
        public String getProviderType() { return providerType; }
        public void setProviderType(String providerType) { this.providerType = providerType; }
        public String getProviderId() { return providerId; }
        public void setProviderId(String providerId) { this.providerId = providerId; }
        public String getConnectorType() { return connectorType; }
        public void setConnectorType(String connectorType) { this.connectorType = connectorType; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public boolean isFallback() { return fallback; }
        public void setFallback(boolean fallback) { this.fallback = fallback; }
    }
}

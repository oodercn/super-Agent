package net.ooder.mvp.skill.scene.capability.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CapabilityBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bindingId;
    private String sceneGroupId;
    private String capDefId;
    private String capabilityId;
    private String capId;
    private String capAddress;
    private String agentId;
    private String linkId;
    private CapabilityProviderType providerType;
    private String providerId;
    private ConnectorType connectorType;
    private Map<String, Object> connectorConfig;
    private int priority;
    private boolean fallback;
    private String fallbackBindingId;
    private CapabilityBindingStatus status;
    private long createTime;
    private long lastInvokeTime;
    private int successCount;
    private int failureCount;
    private String endpoint;
    private int timeout;
    private String method;

    public CapabilityBinding() {
        this.connectorConfig = new HashMap<String, Object>();
        this.priority = 1;
        this.fallback = true;
        this.status = CapabilityBindingStatus.PENDING;
        this.createTime = System.currentTimeMillis();
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public String getSceneGroupId() {
        return sceneGroupId;
    }

    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }

    public String getCapDefId() {
        return capDefId;
    }

    public void setCapDefId(String capDefId) {
        this.capDefId = capDefId;
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getCapId() {
        return capId;
    }

    public void setCapId(String capId) {
        this.capId = capId;
    }

    public String getCapAddress() {
        return capAddress;
    }

    public void setCapAddress(String capAddress) {
        this.capAddress = capAddress;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public CapabilityProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(CapabilityProviderType providerType) {
        this.providerType = providerType;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public ConnectorType getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(ConnectorType connectorType) {
        this.connectorType = connectorType;
    }

    public Map<String, Object> getConnectorConfig() {
        return connectorConfig;
    }

    public void setConnectorConfig(Map<String, Object> connectorConfig) {
        this.connectorConfig = connectorConfig;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }

    public String getFallbackBindingId() {
        return fallbackBindingId;
    }

    public void setFallbackBindingId(String fallbackBindingId) {
        this.fallbackBindingId = fallbackBindingId;
    }

    public CapabilityBindingStatus getStatus() {
        return status;
    }

    public void setStatus(CapabilityBindingStatus status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastInvokeTime() {
        return lastInvokeTime;
    }

    public void setLastInvokeTime(long lastInvokeTime) {
        this.lastInvokeTime = lastInvokeTime;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

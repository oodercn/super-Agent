package net.ooder.sdk.system.security;

import java.util.Set;

public class ResourcePermission extends Permission {
    private String agentId;
    private String resourceId;
    private String resourceType;
    private String resourcePath;
    private Set<String> allowedActions;
    private long maxResourceUsage;
    private long maxBandwidth;
    private boolean allowWrite;
    private boolean allowDelete;

    public ResourcePermission() {
    }

    public ResourcePermission(String id, String name, String description, boolean enabled,
                            String agentId, String resourceId, String resourceType,
                            String resourcePath, Set<String> allowedActions,
                            long maxResourceUsage, long maxBandwidth,
                            boolean allowWrite, boolean allowDelete) {
        super(id, name, description, enabled);
        this.agentId = agentId;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.resourcePath = resourcePath;
        this.allowedActions = allowedActions;
        this.maxResourceUsage = maxResourceUsage;
        this.maxBandwidth = maxBandwidth;
        this.allowWrite = allowWrite;
        this.allowDelete = allowDelete;
    }

    @Override
    public boolean isGranted() {
        // 实现资源权限的授予逻辑
        return isEnabled() && agentId != null && resourceId != null;
    }

    /**
     * 检查是否允许特定操作
     */
    public boolean isActionAllowed(String action) {
        return allowedActions != null && allowedActions.contains(action);
    }

    /**
     * 检查资源使用是否超过限制
     */
    public boolean isResourceUsageAllowed(long currentUsage) {
        return maxResourceUsage <= 0 || currentUsage <= maxResourceUsage;
    }

    /**
     * 检查带宽使用是否超过限制
     */
    public boolean isBandwidthAllowed(long currentBandwidth) {
        return maxBandwidth <= 0 || currentBandwidth <= maxBandwidth;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Set<String> getAllowedActions() {
        return allowedActions;
    }

    public void setAllowedActions(Set<String> allowedActions) {
        this.allowedActions = allowedActions;
    }

    public long getMaxResourceUsage() {
        return maxResourceUsage;
    }

    public void setMaxResourceUsage(long maxResourceUsage) {
        this.maxResourceUsage = maxResourceUsage;
    }

    public long getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(long maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public boolean isAllowWrite() {
        return allowWrite;
    }

    public void setAllowWrite(boolean allowWrite) {
        this.allowWrite = allowWrite;
    }

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }
}
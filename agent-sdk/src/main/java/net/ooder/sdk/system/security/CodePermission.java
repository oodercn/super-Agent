package net.ooder.sdk.system.security;

import java.util.Set;

public class CodePermission extends Permission {
    private String agentId;
    private String codeId;
    private String codeType;
    private Set<String> allowedOperations;
    private boolean allowNetworkAccess;
    private boolean allowFileAccess;
    private boolean allowSystemCalls;

    public CodePermission() {
    }

    public CodePermission(String id, String name, String description, boolean enabled, 
                         String agentId, String codeId, String codeType, 
                         Set<String> allowedOperations, boolean allowNetworkAccess,
                         boolean allowFileAccess, boolean allowSystemCalls) {
        super(id, name, description, enabled);
        this.agentId = agentId;
        this.codeId = codeId;
        this.codeType = codeType;
        this.allowedOperations = allowedOperations;
        this.allowNetworkAccess = allowNetworkAccess;
        this.allowFileAccess = allowFileAccess;
        this.allowSystemCalls = allowSystemCalls;
    }

    @Override
    public boolean isGranted() {
        // 实现代码权限的授予逻辑
        return isEnabled() && agentId != null && codeId != null;
    }

    /**
     * 检查是否允许特定操作
     */
    public boolean isOperationAllowed(String operation) {
        return allowedOperations != null && allowedOperations.contains(operation);
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public Set<String> getAllowedOperations() {
        return allowedOperations;
    }

    public void setAllowedOperations(Set<String> allowedOperations) {
        this.allowedOperations = allowedOperations;
    }

    public boolean isAllowNetworkAccess() {
        return allowNetworkAccess;
    }

    public void setAllowNetworkAccess(boolean allowNetworkAccess) {
        this.allowNetworkAccess = allowNetworkAccess;
    }

    public boolean isAllowFileAccess() {
        return allowFileAccess;
    }

    public void setAllowFileAccess(boolean allowFileAccess) {
        this.allowFileAccess = allowFileAccess;
    }

    public boolean isAllowSystemCalls() {
        return allowSystemCalls;
    }

    public void setAllowSystemCalls(boolean allowSystemCalls) {
        this.allowSystemCalls = allowSystemCalls;
    }
}
package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class DomainPolicyDTO {
    private String policyId;
    private String domainId;
    private AccessPolicyDTO accessPolicy;
    private ExecutionPolicyDTO executionPolicy;
    private SharingPolicyDTO sharingPolicy;
    private SecurityPolicyDTO securityPolicy;
    private long createdAt;
    private long updatedAt;

    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public AccessPolicyDTO getAccessPolicy() { return accessPolicy; }
    public void setAccessPolicy(AccessPolicyDTO accessPolicy) { this.accessPolicy = accessPolicy; }
    public ExecutionPolicyDTO getExecutionPolicy() { return executionPolicy; }
    public void setExecutionPolicy(ExecutionPolicyDTO executionPolicy) { this.executionPolicy = executionPolicy; }
    public SharingPolicyDTO getSharingPolicy() { return sharingPolicy; }
    public void setSharingPolicy(SharingPolicyDTO sharingPolicy) { this.sharingPolicy = sharingPolicy; }
    public SecurityPolicyDTO getSecurityPolicy() { return securityPolicy; }
    public void setSecurityPolicy(SecurityPolicyDTO securityPolicy) { this.securityPolicy = securityPolicy; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public static class AccessPolicyDTO {
        private boolean allowGuestAccess;
        private List<String> allowedAgentTypes;
        private int maxMembers;
        private String authenticationMode;

        public boolean isAllowGuestAccess() { return allowGuestAccess; }
        public void setAllowGuestAccess(boolean allowGuestAccess) { this.allowGuestAccess = allowGuestAccess; }
        public List<String> getAllowedAgentTypes() { return allowedAgentTypes; }
        public void setAllowedAgentTypes(List<String> allowedAgentTypes) { this.allowedAgentTypes = allowedAgentTypes; }
        public int getMaxMembers() { return maxMembers; }
        public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
        public String getAuthenticationMode() { return authenticationMode; }
        public void setAuthenticationMode(String authenticationMode) { this.authenticationMode = authenticationMode; }
    }

    public static class ExecutionPolicyDTO {
        private boolean requireApproval;
        private int maxConcurrentExecutions;
        private long executionTimeout;
        private boolean allowRemoteExecution;

        public boolean isRequireApproval() { return requireApproval; }
        public void setRequireApproval(boolean requireApproval) { this.requireApproval = requireApproval; }
        public int getMaxConcurrentExecutions() { return maxConcurrentExecutions; }
        public void setMaxConcurrentExecutions(int maxConcurrentExecutions) { this.maxConcurrentExecutions = maxConcurrentExecutions; }
        public long getExecutionTimeout() { return executionTimeout; }
        public void setExecutionTimeout(long executionTimeout) { this.executionTimeout = executionTimeout; }
        public boolean isAllowRemoteExecution() { return allowRemoteExecution; }
        public void setAllowRemoteExecution(boolean allowRemoteExecution) { this.allowRemoteExecution = allowRemoteExecution; }
    }

    public static class SharingPolicyDTO {
        private boolean allowSkillSharing;
        private boolean allowDataSharing;
        private List<String> trustedDomains;
        private String sharingMode;

        public boolean isAllowSkillSharing() { return allowSkillSharing; }
        public void setAllowSkillSharing(boolean allowSkillSharing) { this.allowSkillSharing = allowSkillSharing; }
        public boolean isAllowDataSharing() { return allowDataSharing; }
        public void setAllowDataSharing(boolean allowDataSharing) { this.allowDataSharing = allowDataSharing; }
        public List<String> getTrustedDomains() { return trustedDomains; }
        public void setTrustedDomains(List<String> trustedDomains) { this.trustedDomains = trustedDomains; }
        public String getSharingMode() { return sharingMode; }
        public void setSharingMode(String sharingMode) { this.sharingMode = sharingMode; }
    }

    public static class SecurityPolicyDTO {
        private String encryptionLevel;
        private boolean requireSignature;
        private int sessionTimeout;
        private String auditLevel;

        public String getEncryptionLevel() { return encryptionLevel; }
        public void setEncryptionLevel(String encryptionLevel) { this.encryptionLevel = encryptionLevel; }
        public boolean isRequireSignature() { return requireSignature; }
        public void setRequireSignature(boolean requireSignature) { this.requireSignature = requireSignature; }
        public int getSessionTimeout() { return sessionTimeout; }
        public void setSessionTimeout(int sessionTimeout) { this.sessionTimeout = sessionTimeout; }
        public String getAuditLevel() { return auditLevel; }
        public void setAuditLevel(String auditLevel) { this.auditLevel = auditLevel; }
    }
}

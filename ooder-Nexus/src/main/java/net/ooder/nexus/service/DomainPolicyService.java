package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 域策略服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供域策略获取和应用能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>域策略获取</li>
 *   <li>策略应用</li>
 *   <li>策略更新监听</li>
 *   <li>策略冲突处理</li>
 * </ul>
 *
 * @author ooder Team
 * @version 1.0
 * @since SDK 0.7.2
 */
public interface DomainPolicyService {

    /**
     * 获取域策略
     *
     * @param domainId 域ID
     * @return 域策略
     */
    CompletableFuture<DomainPolicy> getDomainPolicy(String domainId);

    /**
     * 应用域策略
     *
     * @param policy 域策略
     * @return 应用结果
     */
    CompletableFuture<PolicyApplyResult> applyPolicy(DomainPolicy policy);

    /**
     * 获取当前应用的策略
     *
     * @return 当前策略
     */
    DomainPolicy getCurrentPolicy();

    /**
     * 检查策略是否已应用
     *
     * @return 是否已应用
     */
    boolean isPolicyApplied();

    /**
     * 重置为默认策略
     *
     * @return 重置结果
     */
    CompletableFuture<Void> resetToDefault();

    /**
     * 添加策略变更监听器
     *
     * @param listener 监听器
     */
    void addPolicyChangeListener(PolicyChangeListener listener);

    /**
     * 移除策略变更监听器
     *
     * @param listener 监听器
     */
    void removePolicyChangeListener(PolicyChangeListener listener);

    /**
     * 验证策略兼容性
     *
     * @param policy 策略
     * @return 验证结果
     */
    CompletableFuture<PolicyValidationResult> validatePolicy(DomainPolicy policy);

    /**
     * 策略变更监听器
     */
    interface PolicyChangeListener {
        void onPolicyChanged(DomainPolicy oldPolicy, DomainPolicy newPolicy);
        void onPolicyApplyProgress(int progress, String stage);
    }

    /**
     * 域策略
     */
    class DomainPolicy {
        private String policyId;
        private String domainId;
        private String policyName;
        private String policyVersion;
        private StoragePolicy storagePolicy;
        private SkillPolicy skillPolicy;
        private SecurityPolicy securityPolicy;
        private NetworkPolicy networkPolicy;
        private long updateTime;

        public String getPolicyId() { return policyId; }
        public void setPolicyId(String policyId) { this.policyId = policyId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getPolicyName() { return policyName; }
        public void setPolicyName(String policyName) { this.policyName = policyName; }
        public String getPolicyVersion() { return policyVersion; }
        public void setPolicyVersion(String policyVersion) { this.policyVersion = policyVersion; }
        public StoragePolicy getStoragePolicy() { return storagePolicy; }
        public void setStoragePolicy(StoragePolicy storagePolicy) { this.storagePolicy = storagePolicy; }
        public SkillPolicy getSkillPolicy() { return skillPolicy; }
        public void setSkillPolicy(SkillPolicy skillPolicy) { this.skillPolicy = skillPolicy; }
        public SecurityPolicy getSecurityPolicy() { return securityPolicy; }
        public void setSecurityPolicy(SecurityPolicy securityPolicy) { this.securityPolicy = securityPolicy; }
        public NetworkPolicy getNetworkPolicy() { return networkPolicy; }
        public void setNetworkPolicy(NetworkPolicy networkPolicy) { this.networkPolicy = networkPolicy; }
        public long getUpdateTime() { return updateTime; }
        public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    }

    /**
     * 存储策略
     */
    class StoragePolicy {
        private String storageType;
        private String storageEndpoint;
        private long maxStorageSize;
        private boolean encryptionEnabled;
        private List<String> allowedPaths;

        public String getStorageType() { return storageType; }
        public void setStorageType(String storageType) { this.storageType = storageType; }
        public String getStorageEndpoint() { return storageEndpoint; }
        public void setStorageEndpoint(String storageEndpoint) { this.storageEndpoint = storageEndpoint; }
        public long getMaxStorageSize() { return maxStorageSize; }
        public void setMaxStorageSize(long maxStorageSize) { this.maxStorageSize = maxStorageSize; }
        public boolean isEncryptionEnabled() { return encryptionEnabled; }
        public void setEncryptionEnabled(boolean encryptionEnabled) { this.encryptionEnabled = encryptionEnabled; }
        public List<String> getAllowedPaths() { return allowedPaths; }
        public void setAllowedPaths(List<String> allowedPaths) { this.allowedPaths = allowedPaths; }
    }

    /**
     * 技能策略
     */
    class SkillPolicy {
        private List<String> allowedSkills;
        private List<String> requiredSkills;
        private List<String> blockedSkills;
        private String skillSource;
        private boolean autoUpdate;

        public List<String> getAllowedSkills() { return allowedSkills; }
        public void setAllowedSkills(List<String> allowedSkills) { this.allowedSkills = allowedSkills; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
        public List<String> getBlockedSkills() { return blockedSkills; }
        public void setBlockedSkills(List<String> blockedSkills) { this.blockedSkills = blockedSkills; }
        public String getSkillSource() { return skillSource; }
        public void setSkillSource(String skillSource) { this.skillSource = skillSource; }
        public boolean isAutoUpdate() { return autoUpdate; }
        public void setAutoUpdate(boolean autoUpdate) { this.autoUpdate = autoUpdate; }
    }

    /**
     * 安全策略
     */
    class SecurityPolicy {
        private String authMode;
        private int sessionTimeout;
        private boolean mfaRequired;
        private List<String> allowedIpRanges;
        private Map<String, String> permissions;

        public String getAuthMode() { return authMode; }
        public void setAuthMode(String authMode) { this.authMode = authMode; }
        public int getSessionTimeout() { return sessionTimeout; }
        public void setSessionTimeout(int sessionTimeout) { this.sessionTimeout = sessionTimeout; }
        public boolean isMfaRequired() { return mfaRequired; }
        public void setMfaRequired(boolean mfaRequired) { this.mfaRequired = mfaRequired; }
        public List<String> getAllowedIpRanges() { return allowedIpRanges; }
        public void setAllowedIpRanges(List<String> allowedIpRanges) { this.allowedIpRanges = allowedIpRanges; }
        public Map<String, String> getPermissions() { return permissions; }
        public void setPermissions(Map<String, String> permissions) { this.permissions = permissions; }
    }

    /**
     * 网络策略
     */
    class NetworkPolicy {
        private boolean p2pEnabled;
        private boolean relayEnabled;
        private List<String> allowedProtocols;
        private int maxConnections;
        private long bandwidthLimit;

        public boolean isP2pEnabled() { return p2pEnabled; }
        public void setP2pEnabled(boolean p2pEnabled) { this.p2pEnabled = p2pEnabled; }
        public boolean isRelayEnabled() { return relayEnabled; }
        public void setRelayEnabled(boolean relayEnabled) { this.relayEnabled = relayEnabled; }
        public List<String> getAllowedProtocols() { return allowedProtocols; }
        public void setAllowedProtocols(List<String> allowedProtocols) { this.allowedProtocols = allowedProtocols; }
        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        public long getBandwidthLimit() { return bandwidthLimit; }
        public void setBandwidthLimit(long bandwidthLimit) { this.bandwidthLimit = bandwidthLimit; }
    }

    /**
     * 策略应用结果
     */
    class PolicyApplyResult {
        private boolean success;
        private String message;
        private List<String> appliedComponents;
        private List<String> failedComponents;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public List<String> getAppliedComponents() { return appliedComponents; }
        public void setAppliedComponents(List<String> appliedComponents) { this.appliedComponents = appliedComponents; }
        public List<String> getFailedComponents() { return failedComponents; }
        public void setFailedComponents(List<String> failedComponents) { this.failedComponents = failedComponents; }
    }

    /**
     * 策略验证结果
     */
    class PolicyValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }
}

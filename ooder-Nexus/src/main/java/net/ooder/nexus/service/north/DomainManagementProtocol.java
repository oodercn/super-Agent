package net.ooder.nexus.service.north;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 域管理协议接口
 *
 * <p>SDK 0.7.2 北向协议，提供域管理能力。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface DomainManagementProtocol {

    CompletableFuture<DomainInfo> createDomain(CreateDomainRequest request);

    CompletableFuture<Void> deleteDomain(String domainId);

    CompletableFuture<DomainInfo> getDomain(String domainId);

    CompletableFuture<List<DomainInfo>> listDomains(DomainQuery query);

    CompletableFuture<Void> updateDomain(String domainId, UpdateDomainRequest request);

    CompletableFuture<Void> addDomainMember(String domainId, AddMemberRequest request);

    CompletableFuture<Void> removeDomainMember(String domainId, String memberId);

    CompletableFuture<List<DomainMember>> listDomainMembers(String domainId);

    CompletableFuture<Void> setDomainPolicy(String domainId, DomainPolicyConfig policy);

    CompletableFuture<DomainPolicyConfig> getDomainPolicy(String domainId);

    void addDomainListener(DomainListener listener);

    void removeDomainListener(DomainListener listener);

    class DomainInfo {
        private String domainId;
        private String domainName;
        private String domainType;
        private String ownerId;
        private int memberCount;
        private String status;
        private long createdAt;
        private Map<String, Object> config;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getDomainType() { return domainType; }
        public void setDomainType(String domainType) { this.domainType = domainType; }
        public String getOwnerId() { return ownerId; }
        public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    class CreateDomainRequest {
        private String domainName;
        private String domainType;
        private String ownerId;
        private Map<String, Object> config;

        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getDomainType() { return domainType; }
        public void setDomainType(String domainType) { this.domainType = domainType; }
        public String getOwnerId() { return ownerId; }
        public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    class UpdateDomainRequest {
        private String domainName;
        private Map<String, Object> config;

        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    class DomainQuery {
        private String domainType;
        private String ownerId;
        private int page;
        private int pageSize;

        public String getDomainType() { return domainType; }
        public void setDomainType(String domainType) { this.domainType = domainType; }
        public String getOwnerId() { return ownerId; }
        public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    class DomainMember {
        private String memberId;
        private String memberName;
        private String domainRole;
        private String status;
        private long joinedAt;

        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getMemberName() { return memberName; }
        public void setMemberName(String memberName) { this.memberName = memberName; }
        public String getDomainRole() { return domainRole; }
        public void setDomainRole(String domainRole) { this.domainRole = domainRole; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getJoinedAt() { return joinedAt; }
        public void setJoinedAt(long joinedAt) { this.joinedAt = joinedAt; }
    }

    class AddMemberRequest {
        private String memberId;
        private String memberName;
        private String domainRole;

        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getMemberName() { return memberName; }
        public void setMemberName(String memberName) { this.memberName = memberName; }
        public String getDomainRole() { return domainRole; }
        public void setDomainRole(String domainRole) { this.domainRole = domainRole; }
    }

    class DomainPolicyConfig {
        private String domainId;
        private List<String> allowedSkills;
        private List<String> requiredSkills;
        private Map<String, Object> storageConfig;
        private Map<String, Object> securityConfig;
        private Map<String, Object> networkConfig;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public List<String> getAllowedSkills() { return allowedSkills; }
        public void setAllowedSkills(List<String> allowedSkills) { this.allowedSkills = allowedSkills; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
        public Map<String, Object> getStorageConfig() { return storageConfig; }
        public void setStorageConfig(Map<String, Object> storageConfig) { this.storageConfig = storageConfig; }
        public Map<String, Object> getSecurityConfig() { return securityConfig; }
        public void setSecurityConfig(Map<String, Object> securityConfig) { this.securityConfig = securityConfig; }
        public Map<String, Object> getNetworkConfig() { return networkConfig; }
        public void setNetworkConfig(Map<String, Object> networkConfig) { this.networkConfig = networkConfig; }
    }

    interface DomainListener {
        void onDomainCreated(DomainInfo domain);
        void onDomainUpdated(DomainInfo domain);
        void onDomainDeleted(String domainId);
        void onMemberAdded(String domainId, DomainMember member);
        void onMemberRemoved(String domainId, String memberId);
        void onPolicyUpdated(String domainId, DomainPolicyConfig policy);
    }
}

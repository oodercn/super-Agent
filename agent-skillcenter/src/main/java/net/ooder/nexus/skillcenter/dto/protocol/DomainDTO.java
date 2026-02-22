package net.ooder.nexus.skillcenter.dto.protocol;

import java.util.List;
import java.util.Map;

public class DomainDTO {

    public static class DomainInfoDTO {
        private String domainId;
        private String domainName;
        private String domainType;
        private String ownerId;
        private int memberCount;
        private String status;
        private long createdAt;
        private long updatedAt;
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
        public long getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    public static class CreateDomainRequestDTO {
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

    public static class UpdateDomainRequestDTO {
        private String domainName;
        private Map<String, Object> config;

        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    public static class DomainQueryDTO {
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

    public static class DomainMemberDTO {
        private String memberId;
        private String memberName;
        private String domainRole;
        private String status;
        private long joinedAt;
        private long lastActiveAt;

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
        public long getLastActiveAt() { return lastActiveAt; }
        public void setLastActiveAt(long lastActiveAt) { this.lastActiveAt = lastActiveAt; }
    }

    public static class AddMemberRequestDTO {
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

    public static class DomainPolicyConfigDTO {
        private String domainId;
        private List<String> allowedSkills;
        private List<String> requiredSkills;
        private Map<String, Object> storageConfig;
        private Map<String, Object> securityConfig;
        private Map<String, Object> networkConfig;
        private Map<String, Object> collaborationConfig;

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
        public Map<String, Object> getCollaborationConfig() { return collaborationConfig; }
        public void setCollaborationConfig(Map<String, Object> collaborationConfig) { this.collaborationConfig = collaborationConfig; }
    }

    public static class InvitationRequestDTO {
        private String targetId;
        private String targetName;
        private String message;

        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getTargetName() { return targetName; }
        public void setTargetName(String targetName) { this.targetName = targetName; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class DomainInvitationDTO {
        private String invitationId;
        private String domainId;
        private String domainName;
        private String inviterId;
        private String targetId;
        private long createdAt;
        private long expiresAt;
        private String status;

        public String getInvitationId() { return invitationId; }
        public void setInvitationId(String invitationId) { this.invitationId = invitationId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getInviterId() { return inviterId; }
        public void setInviterId(String inviterId) { this.inviterId = inviterId; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getExpiresAt() { return expiresAt; }
        public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}

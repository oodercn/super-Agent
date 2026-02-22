package net.ooder.nexus.service.south;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 角色协议接口
 *
 * <p>SDK 0.7.2 南向协议，提供角色决策能力。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface RoleProtocol {

    CompletableFuture<RoleDecision> decideRole(RoleContext context);

    CompletableFuture<Void> registerRole(RoleRegistration registration);

    CompletableFuture<Void> unregisterRole(String agentId);

    CompletableFuture<RoleInfo> getRoleInfo(String agentId);

    CompletableFuture<Void> upgradeRole(String agentId, String newRole);

    CompletableFuture<Void> downgradeRole(String agentId, String newRole);

    void addRoleListener(RoleListener listener);

    void removeRoleListener(RoleListener listener);

    class RoleDecision {
        private String agentId;
        private String decidedRole;
        private String reason;
        private RoleContext context;
        private long decidedTime;

        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getDecidedRole() { return decidedRole; }
        public void setDecidedRole(String decidedRole) { this.decidedRole = decidedRole; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public RoleContext getContext() { return context; }
        public void setContext(RoleContext context) { this.context = context; }
        public long getDecidedTime() { return decidedTime; }
        public void setDecidedTime(long decidedTime) { this.decidedTime = decidedTime; }
    }

    class RoleContext {
        private String agentId;
        private boolean hasMcp;
        private boolean hasNetwork;
        private boolean hasDomain;
        private int peerCount;
        private Map<String, Object> properties;

        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public boolean isHasMcp() { return hasMcp; }
        public void setHasMcp(boolean hasMcp) { this.hasMcp = hasMcp; }
        public boolean isHasNetwork() { return hasNetwork; }
        public void setHasNetwork(boolean hasNetwork) { this.hasNetwork = hasNetwork; }
        public boolean isHasDomain() { return hasDomain; }
        public void setHasDomain(boolean hasDomain) { this.hasDomain = hasDomain; }
        public int getPeerCount() { return peerCount; }
        public void setPeerCount(int peerCount) { this.peerCount = peerCount; }
        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }

    class RoleRegistration {
        private String agentId;
        private String roleType;
        private String mcpId;
        private String domainId;
        private Map<String, Object> capabilities;

        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getRoleType() { return roleType; }
        public void setRoleType(String roleType) { this.roleType = roleType; }
        public String getMcpId() { return mcpId; }
        public void setMcpId(String mcpId) { this.mcpId = mcpId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
    }

    class RoleInfo {
        private String agentId;
        private String roleType;
        private String status;
        private String mcpId;
        private String domainId;
        private long registeredTime;
        private long lastHeartbeat;

        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getRoleType() { return roleType; }
        public void setRoleType(String roleType) { this.roleType = roleType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMcpId() { return mcpId; }
        public void setMcpId(String mcpId) { this.mcpId = mcpId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public long getRegisteredTime() { return registeredTime; }
        public void setRegisteredTime(long registeredTime) { this.registeredTime = registeredTime; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    }

    interface RoleListener {
        void onRoleChanged(String agentId, String oldRole, String newRole);
        void onRoleRegistered(RoleInfo roleInfo);
        void onRoleUnregistered(String agentId);
        void onRoleStatusChanged(String agentId, String oldStatus, String newStatus);
    }
}

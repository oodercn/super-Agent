package net.ooder.sdk.southbound.protocol.model;

public interface RoleListener {
    
    void onRoleChanged(String agentId, RoleType oldRole, RoleType newRole);
    
    void onRoleRegistered(RoleInfo roleInfo);
    
    void onRoleUnregistered(String agentId);
    
    void onRoleStatusChanged(String agentId, RoleStatus oldStatus, RoleStatus newStatus);
}

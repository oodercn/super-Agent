
package net.ooder.sdk.service.security.permission;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermissionManager {
    
    private static final Logger log = LoggerFactory.getLogger(PermissionManager.class);
    
    private final Map<String, Set<String>> rolePermissions;
    private final Map<String, Set<String>> agentRoles;
    
    public PermissionManager() {
        this.rolePermissions = new ConcurrentHashMap<>();
        this.agentRoles = new ConcurrentHashMap<>();
        
        initializeDefaultRoles();
    }
    
    private void initializeDefaultRoles() {
        rolePermissions.put("admin", createSet("*"));
        rolePermissions.put("primary", createSet("read", "write", "execute", "invoke"));
        rolePermissions.put("backup", createSet("read", "invoke"));
        rolePermissions.put("observer", createSet("read"));
    }
    
    private Set<String> createSet(String... elements) {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, elements);
        return set;
    }
    
    public void createRole(String role, Set<String> permissions) {
        rolePermissions.put(role, ConcurrentHashMap.newKeySet());
        rolePermissions.get(role).addAll(permissions);
        log.info("Created role: {} with permissions: {}", role, permissions);
    }
    
    public void deleteRole(String role) {
        rolePermissions.remove(role);
        log.info("Deleted role: {}", role);
    }
    
    public void assignRole(String agentId, String role) {
        agentRoles.computeIfAbsent(agentId, k -> ConcurrentHashMap.newKeySet()).add(role);
        log.info("Assigned role {} to agent {}", role, agentId);
    }
    
    public void revokeRole(String agentId, String role) {
        Set<String> roles = agentRoles.get(agentId);
        if (roles != null) {
            roles.remove(role);
            log.info("Revoked role {} from agent {}", role, agentId);
        }
    }
    
    public boolean hasPermission(String agentId, String permission) {
        Set<String> roles = agentRoles.get(agentId);
        if (roles == null) {
            return false;
        }
        
        for (String role : roles) {
            Set<String> permissions = rolePermissions.get(role);
            if (permissions != null) {
                if (permissions.contains("*") || permissions.contains(permission)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public Set<String> getAgentRoles(String agentId) {
        Set<String> roles = agentRoles.get(agentId);
        return roles != null ? new HashSet<>(roles) : Collections.<String>emptySet();
    }
    
    public Set<String> getRolePermissions(String role) {
        Set<String> perms = rolePermissions.get(role);
        return perms != null ? new HashSet<>(perms) : Collections.<String>emptySet();
    }
    
    public void addPermissionToRole(String role, String permission) {
        Set<String> permissions = rolePermissions.get(role);
        if (permissions != null) {
            permissions.add(permission);
            log.debug("Added permission {} to role {}", permission, role);
        }
    }
    
    public void removePermissionFromRole(String role, String permission) {
        Set<String> permissions = rolePermissions.get(role);
        if (permissions != null) {
            permissions.remove(permission);
            log.debug("Removed permission {} from role {}", permission, role);
        }
    }
}

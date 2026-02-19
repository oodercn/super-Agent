
package net.ooder.sdk.service.security.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleManager {
    
    private static final Logger log = LoggerFactory.getLogger(RoleManager.class);
    
    private final Map<String, Role> roles;
    private final Map<String, Set<String>> agentRoles;
    private final Map<String, Set<String>> roleHierarchy;
    
    public RoleManager() {
        this.roles = new ConcurrentHashMap<>();
        this.agentRoles = new ConcurrentHashMap<>();
        this.roleHierarchy = new ConcurrentHashMap<>();
        
        initDefaultRoles();
    }
    
    private void initDefaultRoles() {
        createRole("admin", "Administrator with full access");
        createRole("operator", "Operator with management access");
        createRole("viewer", "Viewer with read-only access");
        createRole("agent", "Agent with basic access");
        
        addRolePermission("admin", "*");
        addRolePermission("operator", "read:*");
        addRolePermission("operator", "write:*");
        addRolePermission("viewer", "read:*");
        addRolePermission("agent", "read:self");
        addRolePermission("agent", "write:self");
        
        setRoleParent("operator", "viewer");
        setRoleParent("admin", "operator");
    }
    
    public Role createRole(String roleId, String description) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setDescription(description);
        role.setPermissions(new HashSet<>());
        
        roles.put(roleId, role);
        log.debug("Created role: {}", roleId);
        return role;
    }
    
    public void deleteRole(String roleId) {
        if (isDefaultRole(roleId)) {
            log.warn("Cannot delete default role: {}", roleId);
            return;
        }
        
        roles.remove(roleId);
        roleHierarchy.remove(roleId);
        
        for (Set<String> parents : roleHierarchy.values()) {
            parents.remove(roleId);
        }
        
        for (Set<String> assigned : agentRoles.values()) {
            assigned.remove(roleId);
        }
        
        log.info("Deleted role: {}", roleId);
    }
    
    public Role getRole(String roleId) {
        return roles.get(roleId);
    }
    
    public List<Role> getAllRoles() {
        return new ArrayList<>(roles.values());
    }
    
    public void addRolePermission(String roleId, String permission) {
        Role role = roles.get(roleId);
        if (role != null) {
            role.getPermissions().add(permission);
            log.debug("Added permission {} to role {}", permission, roleId);
        }
    }
    
    public void removeRolePermission(String roleId, String permission) {
        Role role = roles.get(roleId);
        if (role != null) {
            role.getPermissions().remove(permission);
            log.debug("Removed permission {} from role {}", permission, roleId);
        }
    }
    
    public void setRoleParent(String roleId, String parentRoleId) {
        if (roles.containsKey(roleId) && roles.containsKey(parentRoleId)) {
            roleHierarchy.computeIfAbsent(roleId, k -> new HashSet<>()).add(parentRoleId);
            log.debug("Set parent {} for role {}", parentRoleId, roleId);
        }
    }
    
    public void assignRole(String agentId, String roleId) {
        if (!roles.containsKey(roleId)) {
            log.warn("Role not found: {}", roleId);
            return;
        }
        
        agentRoles.computeIfAbsent(agentId, k -> new HashSet<>()).add(roleId);
        log.info("Assigned role {} to agent {}", roleId, agentId);
    }
    
    public void revokeRole(String agentId, String roleId) {
        Set<String> assigned = agentRoles.get(agentId);
        if (assigned != null) {
            assigned.remove(roleId);
            log.info("Revoked role {} from agent {}", roleId, agentId);
        }
    }
    
    public Set<String> getAgentRoles(String agentId) {
        Set<String> assigned = agentRoles.get(agentId);
        return assigned != null ? new HashSet<>(assigned) : new HashSet<>();
    }
    
    public Set<String> getEffectivePermissions(String agentId) {
        Set<String> permissions = new HashSet<>();
        Set<String> visited = new HashSet<>();
        
        for (String roleId : getAgentRoles(agentId)) {
            collectPermissions(roleId, permissions, visited);
        }
        
        return permissions;
    }
    
    private void collectPermissions(String roleId, Set<String> permissions, Set<String> visited) {
        if (visited.contains(roleId)) {
            return;
        }
        visited.add(roleId);
        
        Role role = roles.get(roleId);
        if (role != null) {
            permissions.addAll(role.getPermissions());
            
            Set<String> parents = roleHierarchy.get(roleId);
            if (parents != null) {
                for (String parent : parents) {
                    collectPermissions(parent, permissions, visited);
                }
            }
        }
    }
    
    public boolean hasPermission(String agentId, String permission) {
        Set<String> permissions = getEffectivePermissions(agentId);
        
        if (permissions.contains("*")) {
            return true;
        }
        
        if (permissions.contains(permission)) {
            return true;
        }
        
        for (String p : permissions) {
            if (matchesPermission(p, permission)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean matchesPermission(String pattern, String permission) {
        if (pattern.endsWith(":*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return permission.startsWith(prefix);
        }
        return false;
    }
    
    private boolean isDefaultRole(String roleId) {
        return "admin".equals(roleId) || "operator".equals(roleId) 
            || "viewer".equals(roleId) || "agent".equals(roleId);
    }
    
    public static class Role {
        private String roleId;
        private String description;
        private Set<String> permissions;
        
        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Set<String> getPermissions() { return permissions; }
        public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
    }
}

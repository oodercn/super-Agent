
package net.ooder.sdk.service.storage.vfs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VfsPermission {
    
    private static final Logger log = LoggerFactory.getLogger(VfsPermission.class);
    
    private final Map<String, String> pathPermissions = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> agentPermissions = new ConcurrentHashMap<>();
    
    public void setPermission(String path, String mode) {
        pathPermissions.put(path, mode);
        log.debug("Set permission for {}: {}", path, mode);
    }
    
    public String getPermission(String path) {
        return pathPermissions.get(path);
    }
    
    public void grantAccess(String path, String agentId, String permissions) {
        agentPermissions.computeIfAbsent(path, k -> new ConcurrentHashMap<>())
            .put(agentId, permissions);
        log.debug("Granted {} access to {} for {}", permissions, path, agentId);
    }
    
    public void revokeAccess(String path, String agentId) {
        Map<String, String> perms = agentPermissions.get(path);
        if (perms != null) {
            perms.remove(agentId);
            log.debug("Revoked access to {} for {}", path, agentId);
        }
    }
    
    public boolean checkAccess(String path, String agentId, String operation) {
        Map<String, String> perms = agentPermissions.get(path);
        if (perms == null) {
            String defaultPerm = pathPermissions.get(path);
            return defaultPerm != null && defaultPerm.contains(String.valueOf(operation.charAt(0)));
        }
        
        String agentPerm = perms.get(agentId);
        if (agentPerm == null) {
            return false;
        }
        
        return agentPerm.contains(String.valueOf(operation.charAt(0)));
    }
    
    public Set<String> getAgentsWithAccess(String path) {
        Map<String, String> perms = agentPermissions.get(path);
        return perms != null ? new HashSet<>(perms.keySet()) : Collections.<String>emptySet();
    }
    
    public void clearPermissions(String path) {
        pathPermissions.remove(path);
        agentPermissions.remove(path);
    }
}

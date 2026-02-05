package net.ooder.sdk.system.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PermissionManager {
    private static final Logger log = LoggerFactory.getLogger(PermissionManager.class);
    private final Map<String, CodePermission> codePermissions = new HashMap<>();
    private final Map<String, ResourcePermission> resourcePermissions = new HashMap<>();
    private final Map<String, Map<String, CodePermission>> agentCodePermissions = new HashMap<>();
    private final Map<String, Map<String, ResourcePermission>> agentResourcePermissions = new HashMap<>();

    /**
     * 添加代码权限
     */
    public CompletableFuture<Boolean> addCodePermission(CodePermission permission) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String permissionId = permission.getId();
                if (permissionId == null) {
                    log.error("Permission ID is required");
                    return false;
                }

                codePermissions.put(permissionId, permission);
                
                // 按Agent ID索引权限
                agentCodePermissions.computeIfAbsent(permission.getAgentId(), k -> new HashMap<>())
                        .put(permissionId, permission);
                
                log.info("Code permission added: {} for agent: {}", permissionId, permission.getAgentId());
                return true;
            } catch (Exception e) {
                log.error("Error adding code permission: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 获取代码权限
     */
    public CompletableFuture<CodePermission> getCodePermission(String permissionId) {
        return CompletableFuture.supplyAsync(() -> {
            return codePermissions.get(permissionId);
        });
    }

    /**
     * 删除代码权限
     */
    public CompletableFuture<Boolean> removeCodePermission(String permissionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CodePermission permission = codePermissions.remove(permissionId);
                if (permission != null) {
                    // 从Agent索引中移除
                    Map<String, CodePermission> agentPerms = agentCodePermissions.get(permission.getAgentId());
                    if (agentPerms != null) {
                        agentPerms.remove(permissionId);
                        if (agentPerms.isEmpty()) {
                            agentCodePermissions.remove(permission.getAgentId());
                        }
                    }
                    log.info("Code permission removed: {}", permissionId);
                    return true;
                }
                return false;
            } catch (Exception e) {
                log.error("Error removing code permission: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 获取Agent的所有代码权限
     */
    public CompletableFuture<Map<String, CodePermission>> getAgentCodePermissions(String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            return agentCodePermissions.getOrDefault(agentId, new HashMap<>());
        });
    }

    /**
     * 验证代码操作权限
     */
    public CompletableFuture<Boolean> verifyCodePermission(String agentId, String codeId, String operation) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, CodePermission> agentPerms = agentCodePermissions.get(agentId);
                if (agentPerms == null) {
                    log.error("No code permissions found for agent: {}", agentId);
                    return false;
                }

                // 查找匹配的代码权限
                for (CodePermission permission : agentPerms.values()) {
                    if (permission.getCodeId().equals(codeId) && 
                        permission.isEnabled() && 
                        permission.isOperationAllowed(operation)) {
                        return true;
                    }
                }

                log.error("Code permission not found for agent: {}, code: {}, operation: {}", 
                        agentId, codeId, operation);
                return false;
            } catch (Exception e) {
                log.error("Error verifying code permission: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 添加资源权限
     */
    public CompletableFuture<Boolean> addResourcePermission(ResourcePermission permission) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String permissionId = permission.getId();
                if (permissionId == null) {
                    log.error("Permission ID is required");
                    return false;
                }

                resourcePermissions.put(permissionId, permission);
                
                // 按Agent ID索引权限
                agentResourcePermissions.computeIfAbsent(permission.getAgentId(), k -> new HashMap<>())
                        .put(permissionId, permission);
                
                log.info("Resource permission added: {} for agent: {}", permissionId, permission.getAgentId());
                return true;
            } catch (Exception e) {
                log.error("Error adding resource permission: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 获取资源权限
     */
    public CompletableFuture<ResourcePermission> getResourcePermission(String permissionId) {
        return CompletableFuture.supplyAsync(() -> {
            return resourcePermissions.get(permissionId);
        });
    }

    /**
     * 删除资源权限
     */
    public CompletableFuture<Boolean> removeResourcePermission(String permissionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResourcePermission permission = resourcePermissions.remove(permissionId);
                if (permission != null) {
                    // 从Agent索引中移除
                    Map<String, ResourcePermission> agentPerms = agentResourcePermissions.get(permission.getAgentId());
                    if (agentPerms != null) {
                        agentPerms.remove(permissionId);
                        if (agentPerms.isEmpty()) {
                            agentResourcePermissions.remove(permission.getAgentId());
                        }
                    }
                    log.info("Resource permission removed: {}", permissionId);
                    return true;
                }
                return false;
            } catch (Exception e) {
                log.error("Error removing resource permission: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 获取Agent的所有资源权限
     */
    public CompletableFuture<Map<String, ResourcePermission>> getAgentResourcePermissions(String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            return agentResourcePermissions.getOrDefault(agentId, new HashMap<>());
        });
    }

    /**
     * 验证资源操作权限
     */
    public CompletableFuture<Boolean> verifyResourcePermission(String agentId, String resourceId, String action) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, ResourcePermission> agentPerms = agentResourcePermissions.get(agentId);
                if (agentPerms == null) {
                    log.error("No resource permissions found for agent: {}", agentId);
                    return false;
                }

                // 查找匹配的资源权限
                for (ResourcePermission permission : agentPerms.values()) {
                    if (permission.getResourceId().equals(resourceId) && 
                        permission.isEnabled() && 
                        permission.isActionAllowed(action)) {
                        return true;
                    }
                }

                log.error("Resource permission not found for agent: {}, resource: {}, action: {}", 
                        agentId, resourceId, action);
                return false;
            } catch (Exception e) {
                log.error("Error verifying resource permission: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 验证资源使用限制
     */
    public CompletableFuture<Boolean> verifyResourceUsage(String agentId, String resourceId, long usage) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, ResourcePermission> agentPerms = agentResourcePermissions.get(agentId);
                if (agentPerms == null) {
                    return false;
                }

                for (ResourcePermission permission : agentPerms.values()) {
                    if (permission.getResourceId().equals(resourceId) && 
                        permission.isEnabled() && 
                        !permission.isResourceUsageAllowed(usage)) {
                        log.warn("Resource usage exceeded for agent: {}, resource: {}, usage: {}", 
                                agentId, resourceId, usage);
                        return false;
                    }
                }

                return true;
            } catch (Exception e) {
                log.error("Error verifying resource usage: {}", e.getMessage());
                return false;
            }
        });
    }
}
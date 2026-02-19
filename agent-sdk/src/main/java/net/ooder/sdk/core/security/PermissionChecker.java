package net.ooder.sdk.core.security;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PermissionChecker {
    
    CompletableFuture<Boolean> checkPermission(CoreIdentity identity, String resource, String action);
    
    CompletableFuture<List<CorePermission>> getPermissions(CoreIdentity identity);
    
    CompletableFuture<Void> grantPermission(CoreIdentity identity, CorePermission permission);
    
    CompletableFuture<Void> revokePermission(CoreIdentity identity, String permissionId);
}

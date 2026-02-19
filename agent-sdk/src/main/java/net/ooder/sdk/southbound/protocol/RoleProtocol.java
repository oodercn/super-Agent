package net.ooder.sdk.southbound.protocol;

import net.ooder.sdk.southbound.protocol.model.*;
import java.util.concurrent.CompletableFuture;

public interface RoleProtocol {
    
    CompletableFuture<RoleDecision> decideRole(RoleContext context);
    
    CompletableFuture<Void> registerRole(RoleRegistration registration);
    
    CompletableFuture<Void> unregisterRole(String agentId);
    
    CompletableFuture<RoleInfo> getRoleInfo(String agentId);
    
    CompletableFuture<Void> upgradeRole(String agentId, RoleType newRole);
    
    CompletableFuture<Void> downgradeRole(String agentId, RoleType newRole);
    
    void addRoleListener(RoleListener listener);
    
    void removeRoleListener(RoleListener listener);
}

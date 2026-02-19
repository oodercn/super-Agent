package net.ooder.sdk.nexus;

import net.ooder.sdk.nexus.model.*;
import net.ooder.sdk.southbound.protocol.model.LoginRequest;
import net.ooder.sdk.southbound.protocol.model.PeerInfo;
import net.ooder.sdk.southbound.protocol.model.RoleDecision;
import net.ooder.sdk.southbound.protocol.model.SceneGroupInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NexusService {
    
    CompletableFuture<NexusStatus> start(NexusConfig config);
    
    CompletableFuture<Void> stop();
    
    CompletableFuture<NexusStatus> getStatus();
    
    CompletableFuture<Void> login(LoginRequest request);
    
    CompletableFuture<Void> logout();
    
    CompletableFuture<UserSession> getCurrentSession();
    
    CompletableFuture<List<PeerInfo>> discoverPeers();
    
    CompletableFuture<RoleDecision> getCurrentRole();
    
    CompletableFuture<Void> joinSceneGroup(String groupId);
    
    CompletableFuture<Void> leaveSceneGroup(String groupId);
    
    CompletableFuture<List<SceneGroupInfo>> listSceneGroups();
    
    void addNexusListener(NexusListener listener);
    
    void removeNexusListener(NexusListener listener);
    
    void shutdown();
}

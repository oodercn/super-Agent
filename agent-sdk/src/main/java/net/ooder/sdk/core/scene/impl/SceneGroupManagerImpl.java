
package net.ooder.sdk.core.scene.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.scene.SceneGroup;
import net.ooder.sdk.api.scene.SceneGroupKey;
import net.ooder.sdk.api.scene.SceneGroupManager;
import net.ooder.sdk.api.scene.SceneMember;
import net.ooder.sdk.common.enums.MemberRole;
import net.ooder.sdk.core.scene.failover.FailoverManager;
import net.ooder.sdk.core.scene.group.KeyShareManager;
import net.ooder.sdk.infra.exception.SceneGroupException;

public class SceneGroupManagerImpl implements SceneGroupManager {
    
    private static final Logger log = LoggerFactory.getLogger(SceneGroupManagerImpl.class);
    
    private final Map<String, SceneGroup> sceneGroups = new ConcurrentHashMap<>();
    private final FailoverManager failoverManager;
    private final KeyShareManager keyShareManager;
    
    public SceneGroupManagerImpl() {
        this.failoverManager = new FailoverManager();
        this.keyShareManager = new KeyShareManager();
    }
    
    @Override
    public CompletableFuture<SceneGroup> create(String sceneId, SceneGroupConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            String sceneGroupId = generateSceneGroupId(sceneId);
            
            SceneGroup group = new SceneGroup();
            group.setSceneGroupId(sceneGroupId);
            group.setSceneId(sceneId);
            group.setMembers(new ArrayList<>());
            group.setStatus("created");
            group.setCreateTime(System.currentTimeMillis());
            group.setLastUpdateTime(System.currentTimeMillis());
            
            sceneGroups.put(sceneGroupId, group);
            
            log.info("Scene group created: {} for scene: {}", sceneGroupId, sceneId);
            
            return group;
        });
    }
    
    @Override
    public CompletableFuture<Void> destroy(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            SceneGroup group = sceneGroups.remove(sceneGroupId);
            if (group != null) {
                failoverManager.stopHeartbeat(sceneGroupId);
                log.info("Scene group destroyed: {}", sceneGroupId);
            }
        });
    }
    
    @Override
    public CompletableFuture<SceneGroup> get(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> sceneGroups.get(sceneGroupId));
    }
    
    @Override
    public CompletableFuture<List<SceneGroup>> listAll() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<>(sceneGroups.values()));
    }
    
    @Override
    public CompletableFuture<List<SceneGroup>> listByScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            List<SceneGroup> result = new ArrayList<>();
            for (SceneGroup group : sceneGroups.values()) {
                if (sceneId.equals(group.getSceneId())) {
                    result.add(group);
                }
            }
            return result;
        });
    }
    
    @Override
    public CompletableFuture<Void> join(String sceneGroupId, String agentId, MemberRole role) {
        return CompletableFuture.runAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                throw new SceneGroupException(sceneGroupId, "Scene group not found");
            }
            
            SceneMember member = new SceneMember();
            member.setAgentId(agentId);
            member.setRole(role);
            member.setSceneGroupId(sceneGroupId);
            member.setStatus("online");
            member.setJoinTime(System.currentTimeMillis());
            member.setLastHeartbeat(System.currentTimeMillis());
            
            group.getMembers().add(member);
            group.setLastUpdateTime(System.currentTimeMillis());
            
            log.info("Agent {} joined scene group {} as {}", agentId, sceneGroupId, role.getCode());
        });
    }
    
    @Override
    public CompletableFuture<Void> leave(String sceneGroupId, String agentId) {
        return CompletableFuture.runAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                return;
            }
            
            group.getMembers().removeIf(m -> agentId.equals(m.getAgentId()));
            group.setLastUpdateTime(System.currentTimeMillis());
            
            log.info("Agent {} left scene group {}", agentId, sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<Void> changeRole(String sceneGroupId, String agentId, MemberRole newRole) {
        return CompletableFuture.runAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                throw new SceneGroupException(sceneGroupId, "Scene group not found");
            }
            
            for (SceneMember member : group.getMembers()) {
                if (agentId.equals(member.getAgentId())) {
                    member.setRole(newRole);
                    member.setLastHeartbeat(System.currentTimeMillis());
                    break;
                }
            }
            
            group.setLastUpdateTime(System.currentTimeMillis());
            log.info("Agent {} role changed to {} in scene group {}", agentId, newRole.getCode(), sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<MemberRole> getRole(String sceneGroupId, String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                return null;
            }
            
            for (SceneMember member : group.getMembers()) {
                if (agentId.equals(member.getAgentId())) {
                    return member.getRole();
                }
            }
            
            return null;
        });
    }
    
    @Override
    public CompletableFuture<List<SceneMember>> listMembers(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            return group != null ? new ArrayList<>(group.getMembers()) : new ArrayList<>();
        });
    }
    
    @Override
    public CompletableFuture<SceneMember> getPrimary(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            return group != null ? group.getPrimary() : null;
        });
    }
    
    @Override
    public CompletableFuture<List<SceneMember>> getBackups(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            return group != null ? group.getBackups() : new ArrayList<>();
        });
    }
    
    @Override
    public CompletableFuture<Void> handleFailover(String sceneGroupId, String failedMemberId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Handling failover for {} in scene group {}", failedMemberId, sceneGroupId);
            
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                throw new SceneGroupException(sceneGroupId, "Scene group not found");
            }
            
            SceneMember failedMember = group.getMember(failedMemberId);
            if (failedMember == null) {
                throw new SceneGroupException(sceneGroupId, "Failed member not found: " + failedMemberId);
            }
            
            List<SceneMember> backups = group.getBackups();
            if (backups.isEmpty()) {
                throw new SceneGroupException(sceneGroupId, "No backup available for failover");
            }
            
            SceneMember newPrimary = failoverManager.selectNewPrimary(backups);
            
            failedMember.setRole(MemberRole.OBSERVER);
            failedMember.setStatus("failed");
            newPrimary.setRole(MemberRole.PRIMARY);
            
            group.setLastUpdateTime(System.currentTimeMillis());
            
            FailoverStatus status = new FailoverStatus();
            status.setSceneGroupId(sceneGroupId);
            status.setFailedMemberId(failedMemberId);
            status.setNewPrimaryId(newPrimary.getAgentId());
            status.setInProgress(false);
            status.setStartTime(System.currentTimeMillis());
            status.setPhase("completed");
            failoverManager.updateStatus(sceneGroupId, status);
            
            log.info("Failover completed: {} replaced {} as PRIMARY in {}", 
                newPrimary.getAgentId(), failedMemberId, sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<FailoverStatus> getFailoverStatus(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            return failoverManager.getStatus(sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<Void> startHeartbeat(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            failoverManager.startHeartbeat(sceneGroupId, () -> {
                SceneGroup group = sceneGroups.get(sceneGroupId);
                if (group != null) {
                    for (SceneMember member : group.getMembers()) {
                        member.setLastHeartbeat(System.currentTimeMillis());
                    }
                }
            });
            log.info("Heartbeat started for scene group: {}", sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<Void> stopHeartbeat(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            failoverManager.stopHeartbeat(sceneGroupId);
            log.info("Heartbeat stopped for scene group: {}", sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<SceneGroupKey> generateKey(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> {
            return keyShareManager.generateKey(sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<SceneGroupKey> reconstructKey(String sceneGroupId, List<KeyShare> shares) {
        return CompletableFuture.supplyAsync(() -> {
            List<SceneGroupKey.KeyShare> keyShares = new ArrayList<>();
            for (KeyShare share : shares) {
                SceneGroupKey.KeyShare keyShare = new SceneGroupKey.KeyShare(
                    share.getAgentId(), share.getShareIndex(), share.getShareData());
                keyShares.add(keyShare);
            }
            return keyShareManager.reconstructKey(sceneGroupId, keyShares);
        });
    }
    
    @Override
    public CompletableFuture<Void> distributeKeyShares(String sceneGroupId, SceneGroupKey key) {
        return CompletableFuture.runAsync(() -> {
            SceneGroup group = sceneGroups.get(sceneGroupId);
            if (group == null) {
                throw new SceneGroupException(sceneGroupId, "Scene group not found");
            }
            
            keyShareManager.distributeShares(sceneGroupId, key, group.getMembers());
            log.info("Key shares distributed for scene group: {}", sceneGroupId);
        });
    }
    
    @Override
    public CompletableFuture<VfsPermission> getVfsPermission(String sceneGroupId, String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            VfsPermission permission = new VfsPermission();
            permission.setAgentId(agentId);
            permission.setSceneGroupId(sceneGroupId);
            permission.setFullAccess(true);
            permission.setReadablePaths(new ArrayList<>());
            permission.setWritablePaths(new ArrayList<>());
            return permission;
        });
    }
    
    private String generateSceneGroupId(String sceneId) {
        return sceneId + "-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}

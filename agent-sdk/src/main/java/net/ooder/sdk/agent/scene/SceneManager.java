package net.ooder.sdk.agent.scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class SceneManager {
    private static final Logger log = LoggerFactory.getLogger(SceneManager.class);
    private final Map<String, SceneDefinition> scenes = new HashMap<>();
    private final Map<String, Map<String, SceneMember>> sceneMembers = new HashMap<>();
    private final Map<String, SceneState> sceneStates = new HashMap<>();
    private final Map<String, List<SceneSnapshot>> sceneSnapshots = new HashMap<>();

    /**
     * 创建新场景
     */
    public CompletableFuture<Boolean> createScene(SceneDefinition sceneDefinition) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (scenes.containsKey(sceneDefinition.getSceneId())) {
                    log.error("Scene already exists: {}", sceneDefinition.getSceneId());
                    return false;
                }

                scenes.put(sceneDefinition.getSceneId(), sceneDefinition);
                sceneMembers.put(sceneDefinition.getSceneId(), new HashMap<>());
                
                // 初始化场景状态
                SceneState initialState = new SceneState(sceneDefinition.getSceneId(), SceneState.StateType.CREATING);
                sceneStates.put(sceneDefinition.getSceneId(), initialState);
                
                // 初始化快照列表
                sceneSnapshots.put(sceneDefinition.getSceneId(), new ArrayList<>());
                
                // 创建初始快照
                createSnapshot(sceneDefinition.getSceneId(), "Initial scene creation", "system");
                
                // 如果场景已准备好（所有必填角色都已满足），则更新状态为READY
                if (isSceneReady(sceneDefinition.getSceneId())) {
                    updateSceneState(sceneDefinition.getSceneId(), SceneState.StateType.READY, null);
                }
                
                log.info("Scene created successfully: {}", sceneDefinition.getSceneId());
                return true;
            } catch (Exception e) {
                log.error("Error creating scene: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 获取场景定义
     */
    public SceneDefinition getScene(String sceneId) {
        return scenes.get(sceneId);
    }

    /**
     * 删除场景
     */
    public CompletableFuture<Boolean> deleteScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!scenes.containsKey(sceneId)) {
                    log.error("Scene not found: {}", sceneId);
                    return false;
                }

                // 创建删除前的快照
                createSnapshot(sceneId, "Scene deletion", "system");
                
                // 清理所有相关资源
                scenes.remove(sceneId);
                sceneMembers.remove(sceneId);
                sceneStates.remove(sceneId);
                sceneSnapshots.remove(sceneId);
                
                log.info("Scene deleted successfully: {}", sceneId);
                return true;
            } catch (Exception e) {
                log.error("Error deleting scene: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 向场景添加成员
     */
    public CompletableFuture<Boolean> addMember(String sceneId, SceneMember member) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!scenes.containsKey(sceneId)) {
                    log.error("Scene not found: {}", sceneId);
                    return false;
                }

                Map<String, SceneMember> members = sceneMembers.get(sceneId);
                if (members.containsKey(member.getAgentId())) {
                    log.error("Member already exists in scene: {} - {}", sceneId, member.getAgentId());
                    return false;
                }

                // 检查场景添加成员前是否已准备好
                boolean wasReady = isSceneReady(sceneId);
                
                members.put(member.getAgentId(), member);
                
                // 更新成员状态
                SceneState sceneState = sceneStates.get(sceneId);
                if (sceneState != null) {
                    SceneState.MemberState memberState = new SceneState.MemberState(
                            member.getAgentId(), SceneState.MemberState.MemberStateType.JOINING);
                    sceneState.updateMemberState(member.getAgentId(), memberState);
                }
                
                // 检查场景添加成员后是否已准备好（所有必填角色都已满足）
                boolean isReadyNow = isSceneReady(sceneId);
                if (isReadyNow && !wasReady) {
                    updateSceneState(sceneId, SceneState.StateType.READY, null);
                }
                
                log.info("Member added to scene: {} - {}", sceneId, member.getAgentId());
                return true;
            } catch (Exception e) {
                log.error("Error adding member to scene: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 从场景移除成员
     */
    public CompletableFuture<Boolean> removeMember(String sceneId, String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!scenes.containsKey(sceneId)) {
                    log.error("Scene not found: {}", sceneId);
                    return false;
                }

                Map<String, SceneMember> members = sceneMembers.get(sceneId);
                if (!members.containsKey(agentId)) {
                    log.error("Member not found in scene: {} - {}", sceneId, agentId);
                    return false;
                }

                members.remove(agentId);
                
                // 更新场景状态，移除成员状态
                SceneState sceneState = sceneStates.get(sceneId);
                if (sceneState != null) {
                    sceneState.removeMemberState(agentId);
                }
                
                // 检查场景是否仍然就绪
                if (!isSceneReady(sceneId)) {
                    updateSceneState(sceneId, SceneState.StateType.PAUSED, "Required member removed");
                }
                
                log.info("Member removed from scene: {} - {}", sceneId, agentId);
                return true;
            } catch (Exception e) {
                log.error("Error removing member from scene: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 获取场景中的成员
     */
    public Map<String, SceneMember> getMembers(String sceneId) {
        return sceneMembers.getOrDefault(sceneId, new HashMap<>());
    }

    /**
     * 验证成员角色是否符合场景要求
     */
    public boolean validateMemberRole(String sceneId, SceneMember member) {
        SceneDefinition scene = scenes.get(sceneId);
        if (scene == null) {
            return false;
        }

        return scene.getMemberRoles().stream()
                .anyMatch(role -> role.getRoleId().equals(member.getRole()) && 
                                 member.getCapabilities().containsAll(role.getCapabilities()));
    }

    /**
     * 检查场景是否已满足所有必填角色要求
     */
    public boolean isSceneReady(String sceneId) {
        SceneDefinition scene = scenes.get(sceneId);
        if (scene == null) {
            return false;
        }

        Map<String, SceneMember> members = sceneMembers.get(sceneId);
        if (members == null) {
            return false;
        }

        return scene.getMemberRoles().stream()
                .filter(MemberRole::isRequired)
                .allMatch(requiredRole -> members.values().stream()
                        .anyMatch(member -> member.getRole().equals(requiredRole.getRoleId()) && 
                                          member.getCapabilities().containsAll(requiredRole.getCapabilities())));
    }

    // -------------------------- 场景状态管理方法 --------------------------

    /**
     * 获取场景状态
     */
    public SceneState getSceneState(String sceneId) {
        return sceneStates.get(sceneId);
    }

    /**
     * 更新场景状态
     */
    public boolean updateSceneState(String sceneId, SceneState.StateType newState, String errorMessage) {
        SceneState sceneState = sceneStates.get(sceneId);
        if (sceneState == null) {
            log.error("Scene state not found for scene: {}", sceneId);
            return false;
        }

        SceneState.StateType oldState = sceneState.getCurrentState();
        sceneState.setCurrentState(newState);
        if (errorMessage != null) {
            sceneState.setLastError(errorMessage);
        }

        log.info("Scene state updated: {} - {} → {}, error: {}", 
                sceneId, oldState, newState, errorMessage);
        return true;
    }

    /**
     * 更新成员状态
     */
    public boolean updateMemberState(String sceneId, String agentId, 
                                   SceneState.MemberState.MemberStateType newState, String errorMessage) {
        SceneState sceneState = sceneStates.get(sceneId);
        if (sceneState == null) {
            log.error("Scene state not found for scene: {}", sceneId);
            return false;
        }

        SceneState.MemberState memberState = sceneState.getMemberState(agentId);
        if (memberState == null) {
            // 如果成员状态不存在，创建新的
            memberState = new SceneState.MemberState(agentId, newState);
        } else {
            memberState.setState(newState);
        }

        if (errorMessage != null) {
            memberState.setLastError(errorMessage);
        }

        sceneState.updateMemberState(agentId, memberState);
        log.info("Member state updated: {} - {} → {}, error: {}", 
                sceneId, agentId, newState, errorMessage);
        return true;
    }

    // -------------------------- 场景快照方法 --------------------------

    /**
     * 创建场景快照
     */
    public SceneSnapshot createSnapshot(String sceneId, String description, String createdBy) {
        if (!scenes.containsKey(sceneId)) {
            log.error("Scene not found for snapshot: {}", sceneId);
            return null;
        }

        try {
            SceneState sceneState = sceneStates.get(sceneId);
            SceneDefinition sceneDefinition = scenes.get(sceneId);
            Map<String, SceneMember> members = sceneMembers.get(sceneId);

            // 创建快照
            SceneSnapshot snapshot = new SceneSnapshot(sceneId, sceneState, sceneDefinition, members);
            snapshot.setDescription(description);
            snapshot.setCreatedBy(createdBy);

            // 保存快照
            List<SceneSnapshot> snapshots = sceneSnapshots.get(sceneId);
            if (snapshots != null) {
                snapshots.add(snapshot);
                log.info("Snapshot created: {} for scene: {}", snapshot.getSnapshotId(), sceneId);
            }

            return snapshot;
        } catch (Exception e) {
            log.error("Error creating snapshot for scene {}: {}", sceneId, e.getMessage());
            return null;
        }
    }

    /**
     * 获取特定快照
     */
    public SceneSnapshot getSnapshot(String sceneId, String snapshotId) {
        List<SceneSnapshot> snapshots = sceneSnapshots.get(sceneId);
        if (snapshots == null) {
            return null;
        }

        return snapshots.stream()
                .filter(snapshot -> snapshot.getSnapshotId().equals(snapshotId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 列出场景的所有快照
     */
    public List<SceneSnapshot> listSnapshots(String sceneId) {
        return sceneSnapshots.getOrDefault(sceneId, new ArrayList<>());
    }

    /**
     * 恢复场景到指定快照
     */
    public CompletableFuture<Boolean> restoreSnapshot(String sceneId, String snapshotId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SceneSnapshot snapshot = getSnapshot(sceneId, snapshotId);
                if (snapshot == null) {
                    log.error("Snapshot not found: {} for scene: {}", snapshotId, sceneId);
                    return false;
                }

                // 创建恢复前的快照
                createSnapshot(sceneId, "Before restoring to snapshot: " + snapshotId, "system");

                // 恢复场景定义
                scenes.put(sceneId, snapshot.getSceneDefinition());

                // 恢复场景成员
                Map<String, SceneMember> restoredMembers = new HashMap<>(snapshot.getSceneMembers());
                sceneMembers.put(sceneId, restoredMembers);

                // 恢复场景状态
                sceneStates.put(sceneId, snapshot.getSceneState());

                log.info("Scene restored from snapshot: {} to snapshot: {}", sceneId, snapshotId);
                return true;
            } catch (Exception e) {
                log.error("Error restoring snapshot {} for scene {}: {}", snapshotId, sceneId, e.getMessage());
                return false;
            }
        });
    }

    /**
     * 删除指定快照
     */
    public boolean deleteSnapshot(String sceneId, String snapshotId) {
        List<SceneSnapshot> snapshots = sceneSnapshots.get(sceneId);
        if (snapshots == null) {
            log.error("No snapshots found for scene: {}", sceneId);
            return false;
        }

        boolean removed = snapshots.removeIf(snapshot -> snapshot.getSnapshotId().equals(snapshotId));
        if (removed) {
            log.info("Snapshot deleted: {} for scene: {}", snapshotId, sceneId);
        } else {
            log.error("Snapshot not found: {} for scene: {}", snapshotId, sceneId);
        }
        return removed;
    }

    /**
     * 删除场景的所有快照
     */
    public boolean deleteAllSnapshots(String sceneId) {
        List<SceneSnapshot> snapshots = sceneSnapshots.get(sceneId);
        if (snapshots == null) {
            log.error("No snapshots found for scene: {}", sceneId);
            return false;
        }

        int count = snapshots.size();
        snapshots.clear();
        log.info("All {} snapshots deleted for scene: {}", count, sceneId);
        return true;
    }
}
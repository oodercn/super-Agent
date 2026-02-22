package net.ooder.nexus.service.scene.impl;

import net.ooder.nexus.service.scene.SceneGroupService;
import net.ooder.nexus.service.scene.SceneEngineService;
import net.ooder.scene.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class SceneGroupServiceImpl implements SceneGroupService {

    private static final Logger log = LoggerFactory.getLogger(SceneGroupServiceImpl.class);

    private final SceneEngineService sceneEngineService;
    private final List<SceneGroupEventListener> listeners = new CopyOnWriteArrayList<SceneGroupEventListener>();
    
    private String localNodeId;

    @Autowired
    public SceneGroupServiceImpl(@Autowired(required = false) SceneEngineService sceneEngineService) {
        this.sceneEngineService = sceneEngineService;
        this.localNodeId = UUID.randomUUID().toString();
        log.info("SceneGroupServiceImpl initialized with SceneEngineService: {}", 
            sceneEngineService != null && sceneEngineService.isAvailable() ? "available" : "not available");
    }

    @Override
    public CompletableFuture<SceneDefinitionDTO> createSceneDefinition(SceneDefinitionDTO definition) {
        log.info("Creating scene definition: {}", definition.getSceneName());
        
        return CompletableFuture.supplyAsync(() -> {
            String sceneId = definition.getSceneId();
            if (sceneId == null || sceneId.isEmpty()) {
                sceneId = "scene-" + UUID.randomUUID().toString().substring(0, 8);
                definition.setSceneId(sceneId);
            }
            
            definition.setCreatedAt(System.currentTimeMillis());
            return definition;
        });
    }

    @Override
    public CompletableFuture<SceneDefinitionDTO> getSceneDefinition(String sceneId) {
        log.info("Getting scene definition: {}", sceneId);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<SceneDefinitionDTO>> listSceneDefinitions() {
        log.info("Listing scene definitions");
        return CompletableFuture.completedFuture(new ArrayList<SceneDefinitionDTO>());
    }

    @Override
    public CompletableFuture<SceneGroupDTO> createSceneGroup(String sceneId, SceneGroupConfigDTO config) {
        log.info("Creating scene group for scene: {}", sceneId);
        
        if (sceneEngineService == null || !sceneEngineService.isAvailable()) {
            return createLocalSceneGroup(sceneId, config);
        }
        
        try {
            String groupName = config.getGroupName() != null ? config.getGroupName() : "Group-" + sceneId;
            int maxMembers = config.getMaxMembers() > 0 ? config.getMaxMembers() : 10;
            
            return sceneEngineService.createSceneGroup(sceneId, groupName, maxMembers)
                .thenApply(this::convertToSceneGroupDTO);
        } catch (Exception e) {
            log.error("Failed to create scene group via SceneEngine, falling back to local", e);
            return createLocalSceneGroup(sceneId, config);
        }
    }

    @Override
    public CompletableFuture<SceneGroupDTO> getSceneGroup(String groupId) {
        log.info("Getting scene group: {}", groupId);
        
        if (sceneEngineService == null || !sceneEngineService.isAvailable()) {
            return CompletableFuture.completedFuture(null);
        }
        
        return sceneEngineService.getSceneGroup(groupId)
            .thenApply(this::convertToSceneGroupDTO);
    }

    @Override
    public CompletableFuture<List<SceneGroupDTO>> listSceneGroups() {
        log.info("Listing scene groups");
        
        if (sceneEngineService == null || !sceneEngineService.isAvailable()) {
            return CompletableFuture.completedFuture(new ArrayList<SceneGroupDTO>());
        }
        
        return sceneEngineService.listMySceneGroups()
            .thenApply(groups -> groups.stream()
                .map(this::convertToSceneGroupDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<String> generateInviteCode(String groupId) {
        log.info("Generating invite code for group: {}", groupId);
        
        return CompletableFuture.supplyAsync(() -> {
            return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        });
    }

    @Override
    public CompletableFuture<Void> inviteMember(String groupId, String peerId) {
        log.info("Inviting member {} to group: {}", peerId, groupId);
        
        if (sceneEngineService != null && sceneEngineService.isAvailable()) {
            String inviteCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            return sceneEngineService.joinSceneGroup(groupId, inviteCode)
                .thenApply(success -> null);
        }
        
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> removeMember(String groupId, String memberId) {
        log.info("Removing member {} from group: {}", memberId, groupId);
        
        if (sceneEngineService != null && sceneEngineService.isAvailable()) {
            return sceneEngineService.removeSceneGroupMember(groupId, memberId)
                .thenApply(success -> null);
        }
        
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<HeartbeatResultDTO> sendHeartbeat(String groupId) {
        log.info("Sending heartbeat for group: {}", groupId);
        
        if (sceneEngineService != null && sceneEngineService.isAvailable()) {
            return sceneEngineService.startHeartbeat(groupId)
                .thenApply(this::convertToHeartbeatResultDTO);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            HeartbeatResultDTO result = new HeartbeatResultDTO();
            result.setGroupId(groupId);
            result.setSuccess(true);
            result.setActiveMembers(new ArrayList<String>());
            result.setInactiveMembers(new ArrayList<String>());
            return result;
        });
    }

    @Override
    public CompletableFuture<SceneGroupStateDTO> getSceneGroupState(String groupId) {
        log.info("Getting state for group: {}", groupId);
        
        return CompletableFuture.supplyAsync(() -> {
            SceneGroupStateDTO state = new SceneGroupStateDTO();
            state.setGroupId(groupId);
            state.setStatus("ACTIVE");
            state.setMemberCount(1);
            state.setActiveCount(1);
            state.setPrimaryId(localNodeId);
            state.setSharedState(new HashMap<String, Object>());
            state.setLastUpdate(System.currentTimeMillis());
            return state;
        });
    }

    @Override
    public void addSceneGroupListener(SceneGroupEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeSceneGroupListener(SceneGroupEventListener listener) {
        listeners.remove(listener);
    }

    private CompletableFuture<SceneGroupDTO> createLocalSceneGroup(String sceneId, SceneGroupConfigDTO config) {
        return CompletableFuture.supplyAsync(() -> {
            String groupId = "group-" + UUID.randomUUID().toString().substring(0, 8);
            
            SceneGroupDTO group = new SceneGroupDTO();
            group.setGroupId(groupId);
            group.setSceneId(sceneId);
            group.setSceneName("Scene " + sceneId);
            group.setGroupName(config.getGroupName() != null ? config.getGroupName() : "Group " + groupId);
            group.setPrimaryId(localNodeId);
            group.setMembers(new ArrayList<SceneMemberDTO>());
            group.setInviteCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            group.setStatus("ACTIVE");
            group.setCreatedAt(System.currentTimeMillis());
            
            SceneMemberDTO primaryMember = new SceneMemberDTO();
            primaryMember.setMemberId(localNodeId);
            primaryMember.setMemberName("Local Node");
            primaryMember.setRole("PRIMARY");
            primaryMember.setStatus("ONLINE");
            primaryMember.setJoinedAt(System.currentTimeMillis());
            primaryMember.setLastHeartbeat(System.currentTimeMillis());
            group.getMembers().add(primaryMember);
            
            return group;
        });
    }

    private SceneGroupDTO convertToSceneGroupDTO(SceneGroupInfo groupInfo) {
        if (groupInfo == null) {
            return null;
        }
        
        SceneGroupDTO dto = new SceneGroupDTO();
        dto.setGroupId(groupInfo.getGroupId());
        dto.setSceneId(groupInfo.getSceneId());
        dto.setGroupName(groupInfo.getName());
        dto.setStatus(groupInfo.getStatus() != null ? groupInfo.getStatus() : "UNKNOWN");
        dto.setCreatedAt(groupInfo.getCreatedAt());
        
        if (groupInfo.getPrimaryMember() != null) {
            dto.setPrimaryId(groupInfo.getPrimaryMember());
        }
        
        if (groupInfo.getMembers() != null) {
            List<SceneMemberDTO> members = groupInfo.getMembers().stream()
                .map(this::convertToSceneMemberDTO)
                .collect(Collectors.toList());
            dto.setMembers(members);
        } else {
            dto.setMembers(new ArrayList<SceneMemberDTO>());
        }
        
        return dto;
    }

    private SceneMemberDTO convertToSceneMemberDTO(SceneMemberInfo memberInfo) {
        if (memberInfo == null) {
            return null;
        }
        
        SceneMemberDTO dto = new SceneMemberDTO();
        dto.setMemberId(memberInfo.getMemberId());
        dto.setMemberName(memberInfo.getMemberName());
        dto.setRole(memberInfo.getRole() != null ? memberInfo.getRole().name() : "MEMBER");
        dto.setStatus(memberInfo.getStatus() != null ? memberInfo.getStatus() : "UNKNOWN");
        dto.setJoinedAt(memberInfo.getJoinedAt());
        dto.setLastHeartbeat(memberInfo.getLastHeartbeat());
        return dto;
    }

    private HeartbeatResultDTO convertToHeartbeatResultDTO(HeartbeatResult result) {
        if (result == null) {
            HeartbeatResultDTO dto = new HeartbeatResultDTO();
            dto.setSuccess(false);
            dto.setActiveMembers(new ArrayList<String>());
            dto.setInactiveMembers(new ArrayList<String>());
            return dto;
        }
        
        HeartbeatResultDTO dto = new HeartbeatResultDTO();
        dto.setGroupId(result.getGroupId());
        dto.setSuccess(result.isSuccess());
        dto.setActiveMembers(result.getActiveMembers() != null ? result.getActiveMembers() : new ArrayList<String>());
        dto.setInactiveMembers(result.getInactiveMembers() != null ? result.getInactiveMembers() : new ArrayList<String>());
        dto.setPrimaryId(result.getPrimaryId());
        return dto;
    }
}

package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.UserSceneGroupDTO;
import net.ooder.mvp.skill.scene.dto.scene.SceneGroupDTO;
import net.ooder.mvp.skill.scene.dto.scene.SceneParticipantDTO;
import net.ooder.mvp.skill.scene.dto.scene.ParticipantType;
import net.ooder.mvp.skill.scene.dto.scene.ParticipantStatus;
import net.ooder.mvp.skill.scene.service.UserSceneGroupService;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import net.ooder.scene.group.SceneGroup;
import net.ooder.scene.group.SceneGroupManager;
import net.ooder.scene.participant.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSceneGroupServiceImpl implements UserSceneGroupService {
    
    private static final Logger log = LoggerFactory.getLogger(UserSceneGroupServiceImpl.class);
    
    @Autowired(required = false)
    private SceneGroupManager sceneGroupManager;
    
    @Autowired
    private SceneGroupService sceneGroupService;
    
    @Override
    public UserSceneGroupDTO getUserSceneGroup(String sceneGroupId, String userId) {
        log.info("[getUserSceneGroup] sceneGroupId={}, userId={}", sceneGroupId, userId);
        
        SceneGroup sceneGroup = sceneGroupManager != null ? 
            sceneGroupManager.getSceneGroup(sceneGroupId) : null;
        
        if (sceneGroup == null) {
            return null;
        }
        
        Participant participant = sceneGroup.getParticipant(userId);
        if (participant == null) {
            return null;
        }
        
        return convertToDTO(sceneGroup, participant);
    }
    
    @Override
    public List<UserSceneGroupDTO> getUserSceneGroups(String userId) {
        log.info("[getUserSceneGroups] userId={}", userId);
        
        List<UserSceneGroupDTO> result = new ArrayList<>();
        
        if (sceneGroupManager == null) {
            log.warn("[getUserSceneGroups] SceneGroupManager not available");
            return result;
        }
        
        List<SceneGroup> allGroups = sceneGroupManager.getAllSceneGroups();
        
        for (SceneGroup group : allGroups) {
            Participant participant = group.getParticipant(userId);
            if (participant != null) {
                result.add(convertToDTO(group, participant));
            }
        }
        
        return result;
    }
    
    @Override
    public UserSceneGroupDTO joinSceneGroup(String sceneGroupId, String userId, String role) {
        log.info("[joinSceneGroup] sceneGroupId={}, userId={}, role={}", sceneGroupId, userId, role);
        
        SceneParticipantDTO participantDTO = new SceneParticipantDTO();
        participantDTO.setParticipantId(userId);
        participantDTO.setRole(role);
        participantDTO.setParticipantType(ParticipantType.USER);
        
        boolean joined = sceneGroupService.join(sceneGroupId, participantDTO);
        
        if (!joined) {
            return null;
        }
        
        return getUserSceneGroup(sceneGroupId, userId);
    }
    
    @Override
    public boolean leaveSceneGroup(String sceneGroupId, String userId) {
        log.info("[leaveSceneGroup] sceneGroupId={}, userId={}", sceneGroupId, userId);
        return sceneGroupService.leave(sceneGroupId, userId);
    }
    
    @Override
    public UserSceneGroupDTO updateRole(String sceneGroupId, String userId, String newRole) {
        log.info("[updateRole] sceneGroupId={}, userId={}, newRole={}", sceneGroupId, userId, newRole);
        
        boolean updated = sceneGroupService.changeRole(sceneGroupId, userId, newRole);
        
        if (!updated) {
            return null;
        }
        
        return getUserSceneGroup(sceneGroupId, userId);
    }
    
    @Override
    public PageResult<UserSceneGroupDTO> listSceneGroupMembers(String sceneGroupId, int pageNum, int pageSize) {
        log.info("[listSceneGroupMembers] sceneGroupId={}", sceneGroupId);
        
        PageResult<SceneParticipantDTO> participants = 
            sceneGroupService.listParticipants(sceneGroupId, pageNum, pageSize);
        
        List<UserSceneGroupDTO> members = participants.getList().stream()
            .map(p -> {
                UserSceneGroupDTO dto = new UserSceneGroupDTO();
                dto.setSceneGroupId(sceneGroupId);
                dto.setUserId(p.getParticipantId());
                dto.setRole(p.getRole());
                dto.setStatus(p.getStatus() != null ? p.getStatus().getCode() : "ACTIVE");
                dto.setJoinTime(p.getJoinTime());
                return dto;
            })
            .collect(Collectors.toList());
        
        PageResult<UserSceneGroupDTO> result = new PageResult<>();
        result.setList(members);
        result.setTotal(participants.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }
    
    private UserSceneGroupDTO convertToDTO(SceneGroup group, Participant participant) {
        UserSceneGroupDTO dto = new UserSceneGroupDTO();
        dto.setSceneGroupId(group.getSceneGroupId());
        dto.setUserId(participant.getParticipantId());
        dto.setRole(participant.getRole() != null ? participant.getRole().name() : "MEMBER");
        dto.setStatus(participant.getStatus() != null ? participant.getStatus().name() : "ACTIVE");
        dto.setJoinTime(toEpochMillis(participant.getJoinTime()));
        dto.setLastActiveTime(toEpochMillis(participant.getLastHeartbeat()));
        
        SceneGroupDTO groupDTO = new SceneGroupDTO();
        groupDTO.setSceneGroupId(group.getSceneGroupId());
        groupDTO.setName(group.getName());
        groupDTO.setDescription(group.getDescription());
        groupDTO.setCreateTime(toEpochMillis(group.getCreateTime()));
        
        dto.setSceneGroup(groupDTO);
        
        return dto;
    }
    
    private long toEpochMillis(Instant instant) {
        return instant != null ? instant.toEpochMilli() : 0L;
    }
    
    private long toEpochMillis(Long timestamp) {
        return timestamp != null ? timestamp : 0L;
    }
}

package net.ooder.skillcenter.sdk;

import net.ooder.skillcenter.config.SdkConfig;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Primary
public class SceneGroupSdkAdapterImpl implements SceneGroupSdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(SceneGroupSdkAdapterImpl.class);

    @Autowired
    private SdkConfig sdkConfig;

    @Autowired
    private SceneEngineAdapter sceneEngineAdapter;

    @PostConstruct
    public void init() {
        log.info("[SceneGroupSdkAdapter] Initialized with SceneEngineAdapter");
    }

    @Override
    public boolean isAvailable() {
        return sceneEngineAdapter.isAvailable();
    }

    @Override
    public SceneGroupDTO createSceneGroup(String sceneId, SceneGroupConfigDTO config) {
        SceneGroupInfoDTO group = sceneEngineAdapter.createSceneGroup(sceneId, config);
        return convertToDTO(group);
    }

    @Override
    public boolean destroySceneGroup(String sceneGroupId) {
        return sceneEngineAdapter.destroySceneGroup(sceneGroupId);
    }

    @Override
    public SceneGroupDTO getSceneGroup(String sceneGroupId) {
        List<SceneGroupInfoDTO> groups = sceneEngineAdapter.listSceneGroups();
        for (SceneGroupInfoDTO g : groups) {
            if (g.getSceneGroupId().equals(sceneGroupId)) {
                return convertToDTO(g);
            }
        }
        return null;
    }

    @Override
    public PageResult<SceneGroupDTO> listSceneGroups(int pageNum, int pageSize) {
        PageResult<SceneGroupInfoDTO> result = sceneEngineAdapter.listSceneGroupsPaged(pageNum, pageSize);
        List<SceneGroupDTO> list = new ArrayList<>();
        for (SceneGroupInfoDTO g : result.getList()) {
            list.add(convertToDTO(g));
        }
        return new PageResult<>(list, result.getTotal(), result.getPageNum(), result.getPageSize());
    }

    @Override
    public boolean joinSceneGroup(String sceneGroupId, String agentId, String role) {
        return sceneEngineAdapter.joinSceneGroup(sceneGroupId, agentId, role);
    }

    @Override
    public boolean leaveSceneGroup(String sceneGroupId, String agentId) {
        return sceneEngineAdapter.leaveSceneGroup(sceneGroupId, agentId);
    }

    @Override
    public PageResult<SceneMemberDTO> listMembers(String sceneGroupId, int pageNum, int pageSize) {
        PageResult<SceneMemberInfoDTO> result = sceneEngineAdapter.listMembersPaged(sceneGroupId, pageNum, pageSize);
        List<SceneMemberDTO> list = new ArrayList<>();
        for (SceneMemberInfoDTO m : result.getList()) {
            list.add(convertMemberToDTO(m));
        }
        return new PageResult<>(list, result.getTotal(), result.getPageNum(), result.getPageSize());
    }

    @Override
    public SceneMemberDTO getPrimaryMember(String sceneGroupId) {
        SceneMemberInfoDTO member = sceneEngineAdapter.getPrimaryMember(sceneGroupId);
        return member != null ? convertMemberToDTO(member) : null;
    }

    @Override
    public boolean handleFailover(String sceneGroupId, String failedMemberId) {
        return sceneEngineAdapter.handleFailover(sceneGroupId, failedMemberId);
    }

    @Override
    public FailoverStatusDTO getFailoverStatus(String sceneGroupId) {
        FailoverStatusDTO dto = new FailoverStatusDTO();
        dto.setSceneGroupId(sceneGroupId);
        dto.setStatus("stable");
        dto.setLastFailoverTime(0L);
        dto.setFailoverCount(0);
        return dto;
    }

    @Override
    public SceneGroupKeyDTO generateKey(String sceneGroupId) {
        SceneGroupKeyDTO dto = new SceneGroupKeyDTO();
        dto.setKeyId("key-" + UUID.randomUUID().toString().substring(0, 8));
        dto.setSceneGroupId(sceneGroupId);
        dto.setCreatedAt(System.currentTimeMillis());
        return dto;
    }

    @Override
    public SceneGroupKeyDTO reconstructKey(String sceneGroupId, List<KeyShareDTO> shares) {
        SceneGroupKeyDTO dto = new SceneGroupKeyDTO();
        dto.setKeyId("key-reconstructed");
        dto.setSceneGroupId(sceneGroupId);
        dto.setCreatedAt(System.currentTimeMillis());
        return dto;
    }

    @Override
    public boolean distributeKeyShares(String sceneGroupId, SceneGroupKeyDTO key) {
        log.debug("[SceneGroupSdkAdapter] Distributing key shares for group: {}", sceneGroupId);
        return true;
    }

    @Override
    public VfsPermissionDTO getVfsPermission(String sceneGroupId, String agentId) {
        VfsPermissionDTO dto = new VfsPermissionDTO();
        dto.setSceneGroupId(sceneGroupId);
        dto.setAgentId(agentId);
        dto.setPermissionType("read-write");
        return dto;
    }

    @Override
    public PageResult<VfsPermissionDTO> listVfsPermissions(String sceneGroupId, int pageNum, int pageSize) {
        return new PageResult<>(new ArrayList<>(), 0, pageNum, pageSize);
    }

    @Override
    public VfsPermissionDTO addVfsPermission(String sceneGroupId, String agentId, String permissionType, String path) {
        VfsPermissionDTO dto = new VfsPermissionDTO();
        dto.setSceneGroupId(sceneGroupId);
        dto.setAgentId(agentId);
        dto.setPermissionType(permissionType);
        dto.setPath(path);
        return dto;
    }

    private SceneGroupDTO convertToDTO(SceneGroupInfoDTO info) {
        SceneGroupDTO dto = new SceneGroupDTO();
        dto.setSceneGroupId(info.getSceneGroupId());
        dto.setSceneId(info.getSceneId());
        dto.setName(info.getName());
        dto.setStatus(info.getStatus());
        dto.setMemberCount(info.getMemberCount() != null ? info.getMemberCount() : 0);
        if (info.getCreatedAt() != null) {
            dto.setCreateTime(info.getCreatedAt());
        }
        return dto;
    }

    private SceneMemberDTO convertMemberToDTO(SceneMemberInfoDTO info) {
        SceneMemberDTO dto = new SceneMemberDTO();
        dto.setAgentId(info.getAgentId());
        dto.setRole(info.getRole());
        dto.setSceneGroupId(info.getSceneGroupId());
        dto.setStatus(info.getStatus());
        if (info.getJoinedAt() != null) {
            dto.setJoinTime(info.getJoinedAt());
        }
        return dto;
    }
}

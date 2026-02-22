package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.GroupDTO;
import net.ooder.skillcenter.manager.GroupManager;
import net.ooder.skillcenter.service.GroupService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class GroupServiceSdkImpl implements GroupService {

    private GroupManager groupManager;

    @PostConstruct
    public void init() {
        groupManager = GroupManager.getInstance();
    }

    private GroupDTO convertToDTO(GroupManager.Group group) {
        if (group == null) return null;
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setMemberCount(group.getMemberCount());
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        return dto;
    }

    @Override
    public PageResult<GroupDTO> getAllGroups(int pageNum, int pageSize) {
        List<GroupDTO> list = groupManager.getAllGroups().stream()
            .map(this::convertToDTO)
            .sorted(Comparator.comparing(GroupDTO::getCreatedAt).reversed())
            .collect(Collectors.toList());
        return paginate(list, pageNum, pageSize);
    }

    @Override
    public PageResult<GroupDTO> searchGroups(String keyword, int pageNum, int pageSize) {
        List<GroupDTO> filtered = groupManager.searchGroups(keyword).stream()
            .map(this::convertToDTO)
            .sorted(Comparator.comparing(GroupDTO::getCreatedAt).reversed())
            .collect(Collectors.toList());
        return paginate(filtered, pageNum, pageSize);
    }

    @Override
    public GroupDTO getGroupById(String groupId) {
        GroupManager.Group group = groupManager.getGroup(groupId);
        return convertToDTO(group);
    }

    @Override
    public GroupDTO createGroup(GroupDTO groupDTO) {
        GroupManager.Group group = new GroupManager.Group();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
        group.setMembers(new ArrayList<>());
        group.setSkills(new ArrayList<>());
        GroupManager.Group created = groupManager.createGroup(group);
        return convertToDTO(created);
    }

    @Override
    public GroupDTO updateGroup(String groupId, GroupDTO groupDTO) {
        GroupManager.Group existing = groupManager.getGroup(groupId);
        if (existing == null) return null;
        existing.setName(groupDTO.getName());
        existing.setDescription(groupDTO.getDescription());
        GroupManager.Group updated = groupManager.updateGroup(existing);
        return convertToDTO(updated);
    }

    @Override
    public boolean deleteGroup(String groupId) {
        return groupManager.deleteGroup(groupId);
    }

    private PageResult<GroupDTO> paginate(List<GroupDTO> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return PageResult.empty();
        }

        List<GroupDTO> pageList = list.subList(start, end);
        return PageResult.of(pageList, total, pageNum, pageSize);
    }
}

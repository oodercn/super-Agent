package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.UserDTO;
import net.ooder.skillcenter.manager.UserManager;
import net.ooder.skillcenter.service.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class UserServiceSdkImpl implements UserService {

    private UserManager userManager;

    @PostConstruct
    public void init() {
        userManager = UserManager.getInstance();
    }

    private UserDTO convertToDTO(UserManager.User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(user.getName());
        dto.setActive("active".equals(user.getStatus()));
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        return dto;
    }

    @Override
    public int getUserCount() {
        return userManager.getAllUsers().size();
    }

    @Override
    public int getActiveUserCount() {
        return (int) userManager.getAllUsers().stream()
            .filter(user -> "active".equals(user.getStatus()))
            .count();
    }

    @Override
    public PageResult<UserDTO> getAllUsers(int pageNum, int pageSize) {
        List<UserDTO> list = userManager.getAllUsers().stream()
            .map(this::convertToDTO)
            .sorted(Comparator.comparing(UserDTO::getCreatedAt).reversed())
            .collect(Collectors.toList());
        return paginate(list, pageNum, pageSize);
    }

    @Override
    public PageResult<UserDTO> searchUsers(String keyword, int pageNum, int pageSize) {
        List<UserDTO> filtered = userManager.searchUsers(keyword).stream()
            .map(this::convertToDTO)
            .sorted(Comparator.comparing(UserDTO::getCreatedAt).reversed())
            .collect(Collectors.toList());
        return paginate(filtered, pageNum, pageSize);
    }

    @Override
    public PageResult<UserDTO> getUsersByGroup(String groupId, int pageNum, int pageSize) {
        List<UserDTO> filtered = userManager.getUsersByGroupId(groupId).stream()
            .map(this::convertToDTO)
            .sorted(Comparator.comparing(UserDTO::getCreatedAt).reversed())
            .collect(Collectors.toList());
        return paginate(filtered, pageNum, pageSize);
    }

    @Override
    public UserDTO getUserById(String userId) {
        UserManager.User user = userManager.getUser(userId);
        return convertToDTO(user);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserManager.User user = new UserManager.User();
        user.setName(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole("user");
        user.setStatus("active");
        UserManager.User created = userManager.createUser(user);
        return convertToDTO(created);
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        UserManager.User existing = userManager.getUser(userId);
        if (existing == null) return null;
        existing.setName(userDTO.getUsername());
        existing.setEmail(userDTO.getEmail());
        UserManager.User updated = userManager.updateUser(existing);
        return convertToDTO(updated);
    }

    @Override
    public boolean deleteUser(String userId) {
        return userManager.deleteUser(userId);
    }

    private PageResult<UserDTO> paginate(List<UserDTO> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return PageResult.empty();
        }

        List<UserDTO> pageList = list.subList(start, end);
        return PageResult.of(pageList, total, pageNum, pageSize);
    }
}

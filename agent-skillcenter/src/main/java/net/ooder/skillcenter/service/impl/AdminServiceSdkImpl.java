package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.*;
import net.ooder.skillcenter.manager.*;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.service.AdminService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class AdminServiceSdkImpl implements AdminService {

    private SkillManager skillManager;
    private UserManager userManager;
    private GroupManager groupManager;
    private HostingManager hostingManager;
    private StorageManager storageManager;

    private final Map<String, SkillAuthenticationDTO> authStore = new ConcurrentHashMap<>();
    private final List<SystemLogDTO> logStore = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        skillManager = SkillManager.getInstance();
        userManager = UserManager.getInstance();
        groupManager = GroupManager.getInstance();
        hostingManager = HostingManager.getInstance();
        storageManager = StorageManager.getInstance();
    }

    private SkillDTO convertSkillToDTO(Skill skill) {
        if (skill == null) return null;
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setDescription(skill.getDescription());
        dto.setVersion("1.0.0");
        dto.setAvailable(skill.isAvailable());
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        
        if (skill instanceof SkillManager.SkillInfo) {
            SkillManager.SkillInfo info = (SkillManager.SkillInfo) skill;
            dto.setCategory(info.getCategory());
            dto.setStatus(info.getStatus());
        } else {
            dto.setCategory("general");
            dto.setStatus(skill.isAvailable() ? "active" : "inactive");
        }
        return dto;
    }

    private UserDTO convertUserToDTO(UserManager.User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getName());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(new Date());
        return dto;
    }

    private GroupDTO convertGroupToDTO(GroupManager.Group group) {
        if (group == null) return null;
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setMemberCount(group.getMemberCount());
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        dto.setStatus("active");
        return dto;
    }

    private HostingInstanceDTO convertHostingToDTO(HostingManager.HostingInstance instance) {
        if (instance == null) return null;
        HostingInstanceDTO dto = new HostingInstanceDTO();
        dto.setId(instance.getId());
        dto.setName(instance.getName());
        dto.setSkillId(instance.getSkillId());
        dto.setDescription(instance.getDescription());
        dto.setStatus(instance.getStatus());
        dto.setHealthStatus("running".equals(instance.getStatus()) ? "healthy" : "unhealthy");
        return dto;
    }

    private StorageItemDTO convertStorageToDTO(StorageManager.StorageItem item) {
        if (item == null) return null;
        StorageItemDTO dto = new StorageItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setType(item.getType());
        dto.setPath(item.getPath());
        dto.setStatus("active");
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        return dto;
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSkills", skillManager.getAllSkills().size());
        stats.put("totalUsers", userManager.getAllUsers().size());
        stats.put("totalGroups", groupManager.getAllGroups().size());
        stats.put("totalHosting", hostingManager.getAllHostingInstances().size());
        stats.put("activeHosting", hostingManager.getAllHostingInstances().stream()
            .filter(h -> "running".equals(h.getStatus())).count());
        return stats;
    }

    @Override
    public PageResult<SkillDTO> getAllSkills(String category, String status, String keyword, int pageNum, int pageSize) {
        List<SkillDTO> filtered = skillManager.getAllSkills().stream()
            .map(this::convertSkillToDTO)
            .filter(skill -> category == null || category.isEmpty() || category.equals(skill.getCategory()))
            .filter(skill -> status == null || status.isEmpty() || status.equals(skill.getStatus()))
            .filter(skill -> keyword == null || keyword.isEmpty() ||
                skill.getName().toLowerCase().contains(keyword.toLowerCase()))
            .sorted(Comparator.comparing(SkillDTO::getCreatedAt).reversed())
            .collect(Collectors.toList());
        return paginate(filtered, pageNum, pageSize);
    }

    @Override
    public SkillDTO getSkillById(String skillId) {
        return convertSkillToDTO(skillManager.getSkill(skillId));
    }

    @Override
    public SkillDTO addSkill(SkillDTO skillDTO) {
        SkillManager.SkillInfo skillInfo = new SkillManager.SkillInfo();
        skillInfo.setName(skillDTO.getName());
        skillInfo.setDescription(skillDTO.getDescription());
        skillInfo.setCategory(skillDTO.getCategory() != null ? skillDTO.getCategory() : "general");
        skillInfo.setStatus(skillDTO.getStatus() != null ? skillDTO.getStatus() : "pending");
        skillInfo.setAvailable("active".equals(skillDTO.getStatus()));
        skillManager.registerSkill(skillInfo);
        return convertSkillToDTO(skillInfo);
    }

    @Override
    public SkillDTO updateSkill(String skillId, SkillDTO skillDTO) {
        Skill existing = skillManager.getSkill(skillId);
        if (existing == null) return null;
        if (existing instanceof SkillManager.SkillInfo) {
            SkillManager.SkillInfo info = (SkillManager.SkillInfo) existing;
            info.setName(skillDTO.getName());
            info.setDescription(skillDTO.getDescription());
        }
        return convertSkillToDTO(existing);
    }

    @Override
    public boolean deleteSkill(String skillId) {
        skillManager.unregisterSkill(skillId);
        return true;
    }

    @Override
    public boolean approveSkill(String skillId) {
        return skillManager.approveSkill(skillId);
    }

    @Override
    public boolean rejectSkill(String skillId) {
        return skillManager.rejectSkill(skillId);
    }

    @Override
    public PageResult<SkillDTO> getMarketSkills(String category, String status, String keyword, int pageNum, int pageSize) {
        return getAllSkills(category, status, keyword, pageNum, pageSize);
    }

    @Override
    public SkillDTO getMarketSkill(String skillId) {
        return getSkillById(skillId);
    }

    @Override
    public SkillDTO addMarketSkill(SkillDTO skillDTO) {
        return addSkill(skillDTO);
    }

    @Override
    public SkillDTO updateMarketSkill(String skillId, SkillDTO skillDTO) {
        return updateSkill(skillId, skillDTO);
    }

    @Override
    public boolean removeMarketSkill(String skillId) {
        return deleteSkill(skillId);
    }

    @Override
    public List<SkillDTO> getMarketSkillsByCategory(String category) {
        return skillManager.getAllSkills().stream()
            .map(this::convertSkillToDTO)
            .filter(s -> category.equals(s.getCategory()))
            .collect(Collectors.toList());
    }

    @Override
    public List<SkillDTO> getPopularMarketSkills(int limit) {
        return skillManager.getAllSkills().stream()
            .map(this::convertSkillToDTO)
            .limit(limit)
            .collect(Collectors.toList());
    }

    @Override
    public List<SkillDTO> getLatestMarketSkills(int limit) {
        return skillManager.getAllSkills().stream()
            .map(this::convertSkillToDTO)
            .limit(limit)
            .collect(Collectors.toList());
    }

    @Override
    public List<SkillAuthenticationDTO> getAuthenticationRequests() {
        return new ArrayList<>(authStore.values());
    }

    @Override
    public SkillAuthenticationDTO getAuthentication(String id) {
        return authStore.get(id);
    }

    @Override
    public SkillAuthenticationDTO createAuthentication(SkillAuthenticationDTO authentication) {
        String id = "auth-" + idGenerator.getAndIncrement();
        authentication.setId(id);
        authentication.setSubmittedAt(new Date());
        authStore.put(id, authentication);
        return authentication;
    }

    @Override
    public SkillAuthenticationDTO updateAuthenticationStatus(String id, String status, String reviewer, String comments) {
        SkillAuthenticationDTO auth = authStore.get(id);
        if (auth != null) {
            auth.setStatus(status);
            auth.setReviewer(reviewer);
            auth.setComments(comments);
            auth.setReviewedAt(new Date());
        }
        return auth;
    }

    @Override
    public boolean deleteAuthentication(String id) {
        return authStore.remove(id) != null;
    }

    @Override
    public SkillAuthenticationDTO issueCertificate(SkillAuthenticationDTO request) {
        request.setStatus("approved");
        request.setCertificateId("CERT-" + idGenerator.getAndIncrement());
        request.setReviewedAt(new Date());
        return request;
    }

    @Override
    public List<GroupDTO> getAllGroups() {
        return groupManager.getAllGroups().stream()
            .map(this::convertGroupToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public GroupDTO getGroup(String groupId) {
        return convertGroupToDTO(groupManager.getGroup(groupId));
    }

    @Override
    public GroupDTO createGroup(GroupDTO group) {
        GroupManager.Group g = new GroupManager.Group();
        g.setName(group.getName());
        g.setDescription(group.getDescription());
        g.setMembers(new ArrayList<>());
        g.setSkills(new ArrayList<>());
        return convertGroupToDTO(groupManager.createGroup(g));
    }

    @Override
    public GroupDTO updateGroup(String groupId, GroupDTO group) {
        GroupManager.Group existing = groupManager.getGroup(groupId);
        if (existing == null) return null;
        existing.setName(group.getName());
        existing.setDescription(group.getDescription());
        return convertGroupToDTO(groupManager.updateGroup(existing));
    }

    @Override
    public boolean deleteGroup(String groupId) {
        return groupManager.deleteGroup(groupId);
    }

    @Override
    public List<GroupDTO> searchGroups(String keyword) {
        return groupManager.searchGroups(keyword).stream()
            .map(this::convertGroupToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userManager.getAllUsers().stream()
            .map(this::convertUserToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUser(String userId) {
        return convertUserToDTO(userManager.getUser(userId));
    }

    @Override
    public UserDTO createUser(UserDTO user) {
        UserManager.User u = new UserManager.User();
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        u.setRole(user.getRole() != null ? user.getRole() : "user");
        u.setStatus("active");
        return convertUserToDTO(userManager.createUser(u));
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO user) {
        UserManager.User existing = userManager.getUser(userId);
        if (existing == null) return null;
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        return convertUserToDTO(userManager.updateUser(existing));
    }

    @Override
    public boolean deleteUser(String userId) {
        return userManager.deleteUser(userId);
    }

    @Override
    public List<UserDTO> searchUsers(String keyword) {
        return userManager.searchUsers(keyword).stream()
            .map(this::convertUserToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getGroupMembers(String groupId) {
        return userManager.getUsersByGroupId(groupId).stream()
            .map(this::convertUserToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<HostingInstanceDTO> getHostingInstances() {
        return hostingManager.getAllHostingInstances().stream()
            .map(this::convertHostingToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public HostingInstanceDTO getHostingInstance(String instanceId) {
        return convertHostingToDTO(hostingManager.getHostingInstance(instanceId));
    }

    @Override
    public HostingInstanceDTO createHostingInstance(HostingInstanceDTO instance) {
        HostingManager.HostingInstance h = new HostingManager.HostingInstance();
        h.setName(instance.getName());
        h.setSkillId(instance.getSkillId());
        h.setDescription(instance.getDescription());
        return convertHostingToDTO(hostingManager.createHostingInstance(h));
    }

    @Override
    public HostingInstanceDTO updateHostingInstance(String instanceId, HostingInstanceDTO instance) {
        HostingManager.HostingInstance existing = hostingManager.getHostingInstance(instanceId);
        if (existing == null) return null;
        existing.setName(instance.getName());
        existing.setSkillId(instance.getSkillId());
        existing.setDescription(instance.getDescription());
        return convertHostingToDTO(hostingManager.updateHostingInstance(existing));
    }

    @Override
    public boolean deleteHostingInstance(String instanceId) {
        return hostingManager.deleteHostingInstance(instanceId);
    }

    @Override
    public HostingInstanceDTO startHostingInstance(String instanceId) {
        return convertHostingToDTO(hostingManager.startHostingInstance(instanceId));
    }

    @Override
    public HostingInstanceDTO stopHostingInstance(String instanceId) {
        return convertHostingToDTO(hostingManager.stopHostingInstance(instanceId));
    }

    @Override
    public List<HostingInstanceDTO> searchHostingInstances(String keyword) {
        return hostingManager.searchHostingInstances(keyword).stream()
            .map(this::convertHostingToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getHostingStats() {
        return hostingManager.getHostingStats();
    }

    @Override
    public List<StorageItemDTO> getStorageList() {
        return storageManager.getAllStorageItems().stream()
            .map(this::convertStorageToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<StorageItemDTO> getStorageListByType(String type) {
        return storageManager.getStorageItemsByType(type).stream()
            .map(this::convertStorageToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public StorageItemDTO getStorageItem(String storageId) {
        return convertStorageToDTO(storageManager.getStorageItem(storageId));
    }

    @Override
    public StorageItemDTO createStorageItem(StorageItemDTO storageItem) {
        StorageManager.StorageItem item = new StorageManager.StorageItem();
        item.setName(storageItem.getName());
        item.setType(storageItem.getType());
        item.setPath(storageItem.getPath());
        return convertStorageToDTO(storageManager.createStorageItem(item));
    }

    @Override
    public boolean deleteStorageItem(String storageId) {
        return storageManager.deleteStorageItem(storageId);
    }

    @Override
    public Map<String, Object> getStorageStats() {
        return storageManager.getStorageStats();
    }

    @Override
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("version", "2.1");
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));
        return info;
    }

    @Override
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        stats.put("totalMemory", runtime.totalMemory());
        stats.put("freeMemory", runtime.freeMemory());
        stats.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        return stats;
    }

    @Override
    public Map<String, Object> getSystemConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("maintenanceMode", false);
        config.put("maxUploadSize", 10485760);
        return config;
    }

    @Override
    public boolean saveSystemConfig(Map<String, Object> config) {
        return true;
    }

    @Override
    public List<SystemLogDTO> getSystemLogs(int limit) {
        return logStore.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public boolean restartSystem() {
        return true;
    }

    @Override
    public boolean shutdownSystem() {
        return true;
    }

    private <T> PageResult<T> paginate(List<T> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return PageResult.empty();
        }

        List<T> pageList = list.subList(start, end);
        return PageResult.of(pageList, total, pageNum, pageSize);
    }
}

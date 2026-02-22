package net.ooder.skillcenter.sdk;

import net.ooder.scene.core.*;
import net.ooder.scene.provider.SecurityProvider;
import net.ooder.scene.provider.NetworkProvider;
import net.ooder.scene.provider.HostingProvider;
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
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class SceneEngineAdapterImpl implements SceneEngineAdapter {

    private static final Logger log = LoggerFactory.getLogger(SceneEngineAdapterImpl.class);

    @Autowired
    private SdkConfig sdkConfig;

    @Autowired(required = false)
    private SceneEngine sceneEngine;

    @Autowired(required = false)
    private SecurityProvider securityProvider;

    @Autowired(required = false)
    private NetworkProvider networkProvider;

    @Autowired(required = false)
    private HostingProvider hostingProvider;

    private final Map<String, SkillInfoDTO> localSkills = new ConcurrentHashMap<>();
    private final Map<String, SceneInfoDTO> localScenes = new ConcurrentHashMap<>();
    private final Map<String, SceneGroupInfoDTO> localSceneGroups = new ConcurrentHashMap<>();
    private final Map<String, List<SceneMemberInfoDTO>> localGroupMembers = new ConcurrentHashMap<>();
    private final Map<String, List<CapabilityInfoDTO>> localSceneCapabilities = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("[SceneEngineAdapter] Initializing with scene-engine 0.7.3...");
        if (sceneEngine != null) {
            log.info("[SceneEngineAdapter] SceneEngine available: {} v{}", sceneEngine.getName(), sceneEngine.getVersion());
        } else {
            log.info("[SceneEngineAdapter] SceneEngine not available, using local fallback");
            initLocalData();
        }
        log.info("[SceneEngineAdapter] Providers - Security: {}, Network: {}, Hosting: {}",
            securityProvider != null, networkProvider != null, hostingProvider != null);
    }

    private void initLocalData() {
        for (int i = 1; i <= 20; i++) {
            SkillInfoDTO skill = new SkillInfoDTO();
            skill.setSkillId("skill-" + String.format("%04d", i));
            skill.setName("Skill " + i);
            skill.setVersion("1.0." + (i % 10));
            skill.setDescription("Skill " + i + " description");
            skill.setAuthor("ooder");
            skill.setCategory(i % 2 == 0 ? "utility" : "ai");
            skill.setStatus("installed");
            skill.setInstalledAt(System.currentTimeMillis() - i * 3600000L);
            skill.setUpdatedAt(System.currentTimeMillis() - i * 1800000L);
            localSkills.put(skill.getSkillId(), skill);
        }

        for (int i = 1; i <= 5; i++) {
            SceneInfoDTO scene = new SceneInfoDTO();
            scene.setSceneId("scene-" + String.format("%04d", i));
            scene.setName("Scene " + i);
            scene.setDescription("Scene " + i + " description");
            scene.setStatus(i % 2 == 0 ? "active" : "inactive");
            scene.setCreatedAt(System.currentTimeMillis() - i * 86400000L);
            scene.setUpdatedAt(System.currentTimeMillis() - i * 43200000L);
            localScenes.put(scene.getSceneId(), scene);

            List<CapabilityInfoDTO> caps = new ArrayList<>();
            for (int j = 1; j <= 3; j++) {
                CapabilityInfoDTO cap = new CapabilityInfoDTO();
                cap.setCapId("cap-" + i + "-" + j);
                cap.setSceneId(scene.getSceneId());
                cap.setName("Capability " + j);
                cap.setDescription("Capability " + j + " for scene " + i);
                cap.setType(j % 2 == 0 ? "input" : "output");
                cap.setStatus("active");
                caps.add(cap);
            }
            localSceneCapabilities.put(scene.getSceneId(), caps);
        }

        for (int i = 1; i <= 3; i++) {
            SceneGroupInfoDTO group = new SceneGroupInfoDTO();
            group.setSceneGroupId("group-" + String.format("%04d", i));
            group.setSceneId("scene-" + String.format("%04d", i));
            group.setName("Group " + i);
            group.setStatus("active");
            group.setMemberCount(i + 1);
            group.setCreatedAt(System.currentTimeMillis() - i * 172800000L);
            localSceneGroups.put(group.getSceneGroupId(), group);

            List<SceneMemberInfoDTO> members = new ArrayList<>();
            for (int j = 1; j <= i + 1; j++) {
                SceneMemberInfoDTO member = new SceneMemberInfoDTO();
                member.setMemberId("member-" + i + "-" + j);
                member.setSceneGroupId(group.getSceneGroupId());
                member.setAgentId("agent-" + j);
                member.setRole(j == 1 ? "primary" : "secondary");
                member.setStatus("online");
                member.setJoinedAt(System.currentTimeMillis() - j * 3600000L);
                members.add(member);
            }
            localGroupMembers.put(group.getSceneGroupId(), members);
        }
    }

    @Override
    public boolean isAvailable() {
        return sceneEngine != null || !localSkills.isEmpty();
    }

    @Override
    public SecurityProvider getSecurityProvider() {
        return securityProvider;
    }

    @Override
    public NetworkProvider getNetworkProvider() {
        return networkProvider;
    }

    @Override
    public HostingProvider getHostingProvider() {
        return hostingProvider;
    }

    @Override
    public List<SkillInfoDTO> listInstalledSkills() {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    List<InstalledSkillInfo> skills = client.listMySkills();
                    if (skills != null) {
                        List<SkillInfoDTO> result = new ArrayList<>();
                        for (InstalledSkillInfo skill : skills) {
                            result.add(convertSkillToDTO(skill));
                        }
                        return result;
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to list skills from engine: {}", e.getMessage());
            }
        }
        return new ArrayList<>(localSkills.values());
    }

    @Override
    public SkillInfoDTO getSkill(String skillId) {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    SkillInfo skill = client.findSkill(skillId);
                    if (skill != null) {
                        return convertSkillInfoToDTO(skill);
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to get skill from engine: {}", e.getMessage());
            }
        }
        return localSkills.get(skillId);
    }

    @Override
    public SkillInstallResultDTO installSkill(String skillId) {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    SkillInstallResult result = client.installSkill(skillId);
                    if (result != null) {
                        return convertInstallResultToDTO(result);
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to install skill via engine: {}", e.getMessage());
            }
        }
        if (localSkills.containsKey(skillId)) {
            return SkillInstallResultDTO.failure("Skill already installed");
        }

        SkillInfoDTO skill = new SkillInfoDTO();
        skill.setSkillId(skillId);
        skill.setName("New Skill " + skillId);
        skill.setVersion("1.0.0");
        skill.setDescription("Newly installed skill");
        skill.setStatus("installed");
        skill.setInstalledAt(System.currentTimeMillis());
        localSkills.put(skillId, skill);

        return SkillInstallResultDTO.success(skillId);
    }

    @Override
    public SkillUninstallResultDTO uninstallSkill(String skillId) {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    SkillUninstallResult result = client.uninstallSkill(skillId);
                    if (result != null) {
                        return convertUninstallResultToDTO(result);
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to uninstall skill via engine: {}", e.getMessage());
            }
        }
        if (!localSkills.containsKey(skillId)) {
            return SkillUninstallResultDTO.failure("Skill not found");
        }

        localSkills.remove(skillId);
        return SkillUninstallResultDTO.success(skillId);
    }

    @Override
    public SceneInfoDTO createScene(SceneInfoDTO scene) {
        if (sceneEngine != null) {
            try {
                AdminClient admin = sceneEngine.adminLogin("admin", "admin");
                if (admin != null) {
                    SceneInfo sceneInfo = convertDTOToSceneInfo(scene);
                    return convertSceneInfoToDTO(sceneInfo);
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to create scene via engine: {}", e.getMessage());
            }
        }
        String sceneId = "scene-" + UUID.randomUUID().toString().substring(0, 8);
        scene.setSceneId(sceneId);
        scene.setStatus("inactive");
        scene.setCreatedAt(System.currentTimeMillis());
        scene.setUpdatedAt(System.currentTimeMillis());
        localScenes.put(sceneId, scene);
        localSceneCapabilities.put(sceneId, new ArrayList<>());
        return scene;
    }

    @Override
    public SceneInfoDTO getScene(String sceneId) {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    List<SceneInfo> scenes = client.listAvailableScenes();
                    if (scenes != null) {
                        for (SceneInfo scene : scenes) {
                            if (sceneId.equals(scene.getSceneId())) {
                                return convertSceneInfoToDTO(scene);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to get scene from engine: {}", e.getMessage());
            }
        }
        return localScenes.get(sceneId);
    }

    @Override
    public List<SceneInfoDTO> listScenes() {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    List<SceneInfo> scenes = client.listAvailableScenes();
                    if (scenes != null) {
                        List<SceneInfoDTO> result = new ArrayList<>();
                        for (SceneInfo scene : scenes) {
                            result.add(convertSceneInfoToDTO(scene));
                        }
                        return result;
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to list scenes from engine: {}", e.getMessage());
            }
        }
        return new ArrayList<>(localScenes.values());
    }

    @Override
    public boolean activateScene(String sceneId) {
        SceneInfoDTO scene = localScenes.get(sceneId);
        if (scene != null) {
            scene.setStatus("active");
            scene.setUpdatedAt(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivateScene(String sceneId) {
        SceneInfoDTO scene = localScenes.get(sceneId);
        if (scene != null) {
            scene.setStatus("inactive");
            scene.setUpdatedAt(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    @Override
    public boolean addCapability(String sceneId, CapabilityInfoDTO capability) {
        List<CapabilityInfoDTO> caps = localSceneCapabilities.computeIfAbsent(sceneId, k -> new ArrayList<>());
        capability.setCapId("cap-" + UUID.randomUUID().toString().substring(0, 8));
        capability.setSceneId(sceneId);
        capability.setStatus("active");
        caps.add(capability);
        return true;
    }

    @Override
    public boolean removeCapability(String sceneId, String capabilityId) {
        List<CapabilityInfoDTO> caps = localSceneCapabilities.get(sceneId);
        if (caps != null) {
            caps.removeIf(c -> c.getCapId().equals(capabilityId));
            return true;
        }
        return false;
    }

    @Override
    public List<CapabilityInfoDTO> listCapabilities(String sceneId) {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    List<CapabilityInfo> caps = client.listCapabilities(sceneId);
                    if (caps != null) {
                        List<CapabilityInfoDTO> result = new ArrayList<>();
                        for (CapabilityInfo cap : caps) {
                            result.add(convertCapabilityInfoToDTO(cap, sceneId));
                        }
                        return result;
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to list capabilities from engine: {}", e.getMessage());
            }
        }
        return localSceneCapabilities.getOrDefault(sceneId, new ArrayList<>());
    }

    @Override
    public CapabilityInfoDTO getCapability(String sceneId, String capabilityId) {
        List<CapabilityInfoDTO> caps = localSceneCapabilities.get(sceneId);
        if (caps != null) {
            return caps.stream()
                .filter(c -> c.getCapId().equals(capabilityId))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    @Override
    public SceneGroupInfoDTO createSceneGroup(String sceneId, SceneGroupConfigDTO config) {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    SceneGroupInfo group = client.joinSceneGroup(sceneId);
                    if (group != null) {
                        return convertSceneGroupInfoToDTO(group);
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to create scene group via engine: {}", e.getMessage());
            }
        }
        String groupId = "group-" + UUID.randomUUID().toString().substring(0, 8);
        SceneGroupInfoDTO group = new SceneGroupInfoDTO();
        group.setSceneGroupId(groupId);
        group.setSceneId(sceneId);
        group.setName(config != null ? config.getName() : "New Group");
        group.setStatus("active");
        group.setMemberCount(0);
        group.setCreatedAt(System.currentTimeMillis());
        localSceneGroups.put(groupId, group);
        localGroupMembers.put(groupId, new ArrayList<>());
        return group;
    }

    @Override
    public boolean destroySceneGroup(String sceneGroupId) {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    client.leaveSceneGroup(sceneGroupId);
                    return true;
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to destroy scene group via engine: {}", e.getMessage());
            }
        }
        localSceneGroups.remove(sceneGroupId);
        localGroupMembers.remove(sceneGroupId);
        return true;
    }

    @Override
    public List<SceneGroupInfoDTO> listSceneGroups() {
        if (sceneEngine != null) {
            try {
                SceneClient client = sceneEngine.login("system");
                if (client != null) {
                    List<SceneGroupInfo> groups = client.listMySceneGroups();
                    if (groups != null) {
                        List<SceneGroupInfoDTO> result = new ArrayList<>();
                        for (SceneGroupInfo group : groups) {
                            result.add(convertSceneGroupInfoToDTO(group));
                        }
                        return result;
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to list scene groups from engine: {}", e.getMessage());
            }
        }
        return new ArrayList<>(localSceneGroups.values());
    }

    @Override
    public boolean joinSceneGroup(String sceneGroupId, String agentId, String role) {
        SceneGroupInfoDTO group = localSceneGroups.get(sceneGroupId);
        if (group == null) return false;

        List<SceneMemberInfoDTO> members = localGroupMembers.get(sceneGroupId);
        if (members == null) return false;

        SceneMemberInfoDTO member = new SceneMemberInfoDTO();
        member.setMemberId("member-" + UUID.randomUUID().toString().substring(0, 8));
        member.setSceneGroupId(sceneGroupId);
        member.setAgentId(agentId);
        member.setRole(role != null ? role : "secondary");
        member.setStatus("online");
        member.setJoinedAt(System.currentTimeMillis());
        members.add(member);

        group.setMemberCount(members.size());
        return true;
    }

    @Override
    public boolean leaveSceneGroup(String sceneGroupId, String agentId) {
        List<SceneMemberInfoDTO> members = localGroupMembers.get(sceneGroupId);
        if (members == null) return false;

        boolean removed = members.removeIf(m -> m.getAgentId().equals(agentId));

        SceneGroupInfoDTO group = localSceneGroups.get(sceneGroupId);
        if (group != null) {
            group.setMemberCount(members.size());
        }
        return removed;
    }

    @Override
    public List<SceneMemberInfoDTO> listMembers(String sceneGroupId) {
        if (sceneEngine != null) {
            try {
                AdminClient admin = sceneEngine.adminLogin("admin", "admin");
                if (admin != null) {
                    List<SceneMemberInfo> members = admin.listSceneGroupMembers(sceneGroupId);
                    if (members != null) {
                        List<SceneMemberInfoDTO> result = new ArrayList<>();
                        for (SceneMemberInfo member : members) {
                            result.add(convertSceneMemberInfoToDTO(member));
                        }
                        return result;
                    }
                }
            } catch (Exception e) {
                log.warn("[SceneEngineAdapter] Failed to list members from engine: {}", e.getMessage());
            }
        }
        return localGroupMembers.getOrDefault(sceneGroupId, new ArrayList<>());
    }

    @Override
    public SceneMemberInfoDTO getPrimaryMember(String sceneGroupId) {
        List<SceneMemberInfoDTO> members = localGroupMembers.get(sceneGroupId);
        if (members != null) {
            return members.stream()
                .filter(m -> "primary".equals(m.getRole()))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    @Override
    public boolean handleFailover(String sceneGroupId, String failedMemberId) {
        List<SceneMemberInfoDTO> members = localGroupMembers.get(sceneGroupId);
        if (members == null || members.isEmpty()) return false;

        SceneMemberInfoDTO failed = members.stream()
            .filter(m -> m.getMemberId().equals(failedMemberId))
            .findFirst()
            .orElse(null);

        if (failed == null) return false;

        if ("primary".equals(failed.getRole())) {
            members.stream()
                .filter(m -> !"primary".equals(m.getRole()))
                .findFirst()
                .ifPresent(newPrimary -> newPrimary.setRole("primary"));
        }

        failed.setStatus("offline");
        return true;
    }

    @Override
    public PageResult<SceneInfoDTO> listScenesPaged(int pageNum, int pageSize) {
        return paginate(listScenes(), pageNum, pageSize);
    }

    @Override
    public PageResult<SceneGroupInfoDTO> listSceneGroupsPaged(int pageNum, int pageSize) {
        return paginate(listSceneGroups(), pageNum, pageSize);
    }

    @Override
    public PageResult<SceneMemberInfoDTO> listMembersPaged(String sceneGroupId, int pageNum, int pageSize) {
        return paginate(listMembers(sceneGroupId), pageNum, pageSize);
    }

    @Override
    public PageResult<CapabilityInfoDTO> listCapabilitiesPaged(String sceneId, int pageNum, int pageSize) {
        return paginate(listCapabilities(sceneId), pageNum, pageSize);
    }

    private SkillInfoDTO convertSkillToDTO(InstalledSkillInfo skill) {
        SkillInfoDTO dto = new SkillInfoDTO();
        dto.setSkillId(skill.getSkillId());
        dto.setName(skill.getName());
        dto.setVersion(skill.getVersion());
        dto.setStatus(skill.getStatus());
        dto.setInstalledAt(skill.getInstalledAt());
        return dto;
    }

    private SkillInfoDTO convertSkillInfoToDTO(SkillInfo skill) {
        SkillInfoDTO dto = new SkillInfoDTO();
        dto.setSkillId(skill.getSkillId());
        dto.setName(skill.getName());
        dto.setVersion(skill.getVersion());
        dto.setDescription(skill.getDescription());
        dto.setAuthor(skill.getAuthor());
        dto.setCategory(skill.getCategory());
        dto.setStatus(skill.getStatus());
        dto.setUpdatedAt(skill.getUpdatedAt());
        return dto;
    }

    private SkillInstallResultDTO convertInstallResultToDTO(SkillInstallResult result) {
        if (result.isSuccess()) {
            return SkillInstallResultDTO.success(result.getSkillId());
        } else {
            return SkillInstallResultDTO.failure(result.getMessage());
        }
    }

    private SkillUninstallResultDTO convertUninstallResultToDTO(SkillUninstallResult result) {
        if (result.isSuccess()) {
            return SkillUninstallResultDTO.success(result.getSkillId());
        } else {
            return SkillUninstallResultDTO.failure(result.getMessage());
        }
    }

    private SceneInfoDTO convertSceneInfoToDTO(SceneInfo scene) {
        SceneInfoDTO dto = new SceneInfoDTO();
        dto.setSceneId(scene.getSceneId());
        dto.setName(scene.getName());
        dto.setDescription(scene.getDescription());
        dto.setStatus(scene.getStatus());
        dto.setCreatedAt(scene.getCreatedAt());
        dto.setUpdatedAt(scene.getUpdatedAt());
        return dto;
    }

    private SceneInfo convertDTOToSceneInfo(SceneInfoDTO dto) {
        SceneInfo info = new SceneInfo();
        info.setSceneId(dto.getSceneId());
        info.setName(dto.getName());
        info.setDescription(dto.getDescription());
        info.setStatus(dto.getStatus());
        return info;
    }

    private SceneGroupInfoDTO convertSceneGroupInfoToDTO(SceneGroupInfo group) {
        SceneGroupInfoDTO dto = new SceneGroupInfoDTO();
        dto.setSceneGroupId(group.getGroupId());
        dto.setSceneId(group.getSceneId());
        dto.setName(group.getName());
        dto.setStatus(group.getStatus());
        dto.setMemberCount(group.getMemberCount());
        dto.setCreatedAt(group.getCreatedAt());
        return dto;
    }

    private SceneMemberInfoDTO convertSceneMemberInfoToDTO(SceneMemberInfo member) {
        SceneMemberInfoDTO dto = new SceneMemberInfoDTO();
        dto.setMemberId(member.getMemberId());
        dto.setSceneGroupId(member.getGroupId());
        dto.setAgentId(member.getUserId());
        dto.setRole(member.getRole() != null ? member.getRole().name().toLowerCase() : "secondary");
        dto.setStatus(member.getStatus());
        dto.setJoinedAt(member.getJoinedAt());
        return dto;
    }

    private CapabilityInfoDTO convertCapabilityInfoToDTO(CapabilityInfo cap, String sceneId) {
        CapabilityInfoDTO dto = new CapabilityInfoDTO();
        dto.setCapId(cap.getName());
        dto.setSceneId(sceneId);
        dto.setName(cap.getName());
        dto.setDescription(cap.getDescription());
        dto.setType(cap.isAsync() ? "async" : "sync");
        dto.setStatus("active");
        return dto;
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

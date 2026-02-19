package net.ooder.sdk.core.scene.store;

import net.ooder.sdk.api.scene.store.*;
import net.ooder.sdk.service.storage.vfs.VfsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DualSceneStore implements SceneStore, GroupStore, SkillStore, LinkStore, AgentStore {
    
    private static final Logger log = LoggerFactory.getLogger(DualSceneStore.class);
    
    private final LocalSceneStore localStore;
    private final VfsClient vfsClient;
    private final String basePath;
    private final ConflictStrategy conflictStrategy;
    
    private final Map<String, PendingSync> pendingSyncs;
    private final List<SyncListener> syncListeners;
    private final StorageStatus status;
    
    private volatile boolean remoteAvailable = false;
    
    public enum ConflictStrategy {
        LOCAL_WINS,
        REMOTE_WINS,
        MERGE,
        LATEST_WINS
    }
    
    public DualSceneStore(LocalSceneStore localStore, VfsClient vfsClient, String basePath) {
        this(localStore, vfsClient, basePath, ConflictStrategy.REMOTE_WINS);
    }
    
    public DualSceneStore(LocalSceneStore localStore, VfsClient vfsClient, String basePath, ConflictStrategy conflictStrategy) {
        this.localStore = localStore;
        this.vfsClient = vfsClient;
        this.basePath = basePath;
        this.conflictStrategy = conflictStrategy;
        this.pendingSyncs = new ConcurrentHashMap<>();
        this.syncListeners = new CopyOnWriteArrayList<>();
        this.status = new StorageStatus();
        
        checkRemoteAvailability();
    }
    
    private void checkRemoteAvailability() {
        if (vfsClient != null) {
            remoteAvailable = vfsClient.isConnected();
            status.setRemoteAvailable(remoteAvailable);
        }
    }
    
    // ==================== SceneStore ====================
    
    @Override
    public void saveScene(String sceneId, Map<String, Object> config) {
        localStore.saveScene(sceneId, config);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/scenes/" + sceneId + ".yaml";
                byte[] data = mapToYaml(config).getBytes(StandardCharsets.UTF_8);
                vfsClient.writeFile(path, data);
                log.debug("Scene synced to VFS: {}", sceneId);
            } catch (Exception e) {
                log.warn("Failed to sync scene to VFS: {}", sceneId, e);
                addPendingSync("scene", sceneId, config);
            }
        } else {
            addPendingSync("scene", sceneId, config);
        }
    }
    
    @Override
    public Map<String, Object> loadScene(String sceneId) {
        Map<String, Object> localConfig = localStore.loadScene(sceneId);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/scenes/" + sceneId + ".yaml";
                byte[] data = vfsClient.readFile(path).join();
                if (data != null) {
                    Map<String, Object> remoteConfig = yamlToMap(new String(data, StandardCharsets.UTF_8));
                    
                    if (localConfig != null && remoteConfig != null) {
                        ConflictInfo conflict = detectConflict(localConfig, remoteConfig);
                        if (conflict != null) {
                            return resolveConflict(localConfig, remoteConfig, conflict);
                        }
                    }
                    
                    localStore.saveScene(sceneId, remoteConfig);
                    return remoteConfig;
                }
            } catch (Exception e) {
                log.warn("Failed to load scene from VFS: {}", sceneId, e);
            }
        }
        
        return localConfig;
    }
    
    @Override
    public void deleteScene(String sceneId) {
        localStore.deleteScene(sceneId);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/scenes/" + sceneId + ".yaml";
                vfsClient.delete(path);
                log.debug("Scene deleted from VFS: {}", sceneId);
            } catch (Exception e) {
                log.warn("Failed to delete scene from VFS: {}", sceneId, e);
            }
        }
    }
    
    @Override
    public List<String> listScenes() {
        return localStore.listScenes();
    }
    
    @Override
    public boolean sceneExists(String sceneId) {
        return localStore.sceneExists(sceneId);
    }
    
    @Override
    public void updateSceneConfig(String sceneId, String key, Object value) {
        localStore.updateSceneConfig(sceneId, key, value);
    }
    
    @Override
    public Object getSceneConfigValue(String sceneId, String key) {
        return localStore.getSceneConfigValue(sceneId, key);
    }
    
    // ==================== GroupStore ====================
    
    @Override
    public void saveGroup(String sceneId, String groupId, Map<String, Object> config) {
        String fullGroupId = sceneId + ":" + groupId;
        localStore.saveScene(fullGroupId, config);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/groups/" + sceneId + "_" + groupId + ".yaml";
                byte[] data = mapToYaml(config).getBytes(StandardCharsets.UTF_8);
                vfsClient.writeFile(path, data);
            } catch (Exception e) {
                log.warn("Failed to sync group to VFS: {}", fullGroupId, e);
                addPendingSync("group", fullGroupId, config);
            }
        } else {
            addPendingSync("group", fullGroupId, config);
        }
    }
    
    @Override
    public Map<String, Object> loadGroup(String sceneId, String groupId) {
        String fullGroupId = sceneId + ":" + groupId;
        return localStore.loadScene(fullGroupId);
    }
    
    @Override
    public void deleteGroup(String sceneId, String groupId) {
        String fullGroupId = sceneId + ":" + groupId;
        localStore.deleteScene(fullGroupId);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/groups/" + sceneId + "_" + groupId + ".yaml";
                vfsClient.delete(path);
            } catch (Exception e) {
                log.warn("Failed to delete group from VFS: {}", fullGroupId, e);
            }
        }
    }
    
    @Override
    public List<String> listGroups(String sceneId) {
        List<String> allKeys = localStore.listScenes();
        List<String> groups = new ArrayList<>();
        String prefix = sceneId + ":";
        for (String key : allKeys) {
            if (key.startsWith(prefix)) {
                groups.add(key);
            }
        }
        return groups;
    }
    
    @Override
    public boolean groupExists(String sceneId, String groupId) {
        return localStore.sceneExists(sceneId + ":" + groupId);
    }
    
    @Override
    public void updateGroupConfig(String sceneId, String groupId, String key, Object value) {
        Map<String, Object> config = loadGroup(sceneId, groupId);
        if (config == null) {
            config = new LinkedHashMap<>();
        }
        config.put(key, value);
        saveGroup(sceneId, groupId, config);
    }
    
    @Override
    public Object getGroupConfigValue(String sceneId, String groupId, String key) {
        Map<String, Object> config = loadGroup(sceneId, groupId);
        return config != null ? config.get(key) : null;
    }
    
    @Override
    public void addSkillToGroup(String sceneId, String groupId, String skillId) {
        Map<String, Object> config = loadGroup(sceneId, groupId);
        if (config == null) {
            config = new LinkedHashMap<>();
        }
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) config.get("skillIds");
        if (skills == null) {
            skills = new ArrayList<>();
            config.put("skillIds", skills);
        }
        if (!skills.contains(skillId)) {
            skills.add(skillId);
            saveGroup(sceneId, groupId, config);
        }
    }
    
    @Override
    public void removeSkillFromGroup(String sceneId, String groupId, String skillId) {
        Map<String, Object> config = loadGroup(sceneId, groupId);
        if (config != null) {
            @SuppressWarnings("unchecked")
            List<String> skills = (List<String>) config.get("skillIds");
            if (skills != null) {
                skills.remove(skillId);
                saveGroup(sceneId, groupId, config);
            }
        }
    }
    
    @Override
    public List<String> getGroupSkills(String sceneId, String groupId) {
        Map<String, Object> config = loadGroup(sceneId, groupId);
        if (config != null) {
            @SuppressWarnings("unchecked")
            List<String> skills = (List<String>) config.get("skillIds");
            return skills != null ? new ArrayList<>(skills) : new ArrayList<>();
        }
        return new ArrayList<>();
    }
    
    // ==================== SkillStore ====================
    
    private final Map<String, SkillRegistration> skillCache = new ConcurrentHashMap<>();
    
    @Override
    public void saveSkill(SkillRegistration registration) {
        if (registration == null || registration.getSkillId() == null) {
            return;
        }
        
        skillCache.put(registration.getSkillId(), registration);
        
        Map<String, Object> config = skillToMap(registration);
        localStore.saveScene("skill:" + registration.getSkillId(), config);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/skills/" + registration.getSkillId() + ".yaml";
                byte[] data = mapToYaml(config).getBytes(StandardCharsets.UTF_8);
                vfsClient.writeFile(path, data);
            } catch (Exception e) {
                log.warn("Failed to sync skill to VFS: {}", registration.getSkillId(), e);
                addPendingSync("skill", registration.getSkillId(), config);
            }
        } else {
            addPendingSync("skill", registration.getSkillId(), config);
        }
    }
    
    @Override
    public SkillRegistration loadSkill(String skillId) {
        SkillRegistration cached = skillCache.get(skillId);
        if (cached != null) {
            return cached;
        }
        
        Map<String, Object> config = localStore.loadScene("skill:" + skillId);
        if (config != null) {
            SkillRegistration registration = mapToSkill(config);
            skillCache.put(skillId, registration);
            return registration;
        }
        
        return null;
    }
    
    @Override
    public void deleteSkill(String skillId) {
        skillCache.remove(skillId);
        localStore.deleteScene("skill:" + skillId);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/skills/" + skillId + ".yaml";
                vfsClient.delete(path);
            } catch (Exception e) {
                log.warn("Failed to delete skill from VFS: {}", skillId, e);
            }
        }
    }
    
    @Override
    public List<SkillRegistration> listSkills(String sceneId, String groupId) {
        List<SkillRegistration> result = new ArrayList<>();
        for (SkillRegistration reg : skillCache.values()) {
            if ((sceneId == null || sceneId.equals(reg.getSceneId())) &&
                (groupId == null || groupId.equals(reg.getGroupId()))) {
                result.add(reg);
            }
        }
        return result;
    }
    
    @Override
    public List<SkillRegistration> listSkillsByScene(String sceneId) {
        return listSkills(sceneId, null);
    }
    
    @Override
    public List<SkillRegistration> listSkillsByType(String sceneId, String skillType) {
        List<SkillRegistration> result = new ArrayList<>();
        for (SkillRegistration reg : skillCache.values()) {
            if ((sceneId == null || sceneId.equals(reg.getSceneId())) &&
                (skillType == null || skillType.equals(reg.getSkillType()))) {
                result.add(reg);
            }
        }
        return result;
    }
    
    @Override
    public boolean skillExists(String skillId) {
        return skillCache.containsKey(skillId) || localStore.sceneExists("skill:" + skillId);
    }
    
    @Override
    public void updateSkillHeartbeat(String skillId, long timestamp) {
        SkillRegistration registration = loadSkill(skillId);
        if (registration != null) {
            registration.setLastHeartbeat(timestamp);
            saveSkill(registration);
        }
    }
    
    @Override
    public Long getSkillLastHeartbeat(String skillId) {
        SkillRegistration registration = loadSkill(skillId);
        return registration != null ? registration.getLastHeartbeat() : null;
    }
    
    @Override
    public void updateSkillStatus(String skillId, String status) {
        SkillRegistration registration = loadSkill(skillId);
        if (registration != null) {
            registration.setStatus(status);
            saveSkill(registration);
        }
    }
    
    @Override
    public void updateSkillEndpoints(String skillId, Map<String, Object> endpoints) {
        SkillRegistration registration = loadSkill(skillId);
        if (registration != null) {
            registration.setEndpoints(endpoints);
            saveSkill(registration);
        }
    }
    
    // ==================== LinkStore ====================
    
    private final Map<String, LinkConfig> linkCache = new ConcurrentHashMap<>();
    
    @Override
    public void saveLink(LinkConfig link) {
        if (link == null || link.getLinkId() == null) {
            return;
        }
        
        linkCache.put(link.getLinkId(), link);
        
        Map<String, Object> config = linkToMap(link);
        localStore.saveScene("link:" + link.getLinkId(), config);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/links/" + link.getLinkId() + ".yaml";
                byte[] data = mapToYaml(config).getBytes(StandardCharsets.UTF_8);
                vfsClient.writeFile(path, data);
            } catch (Exception e) {
                log.warn("Failed to sync link to VFS: {}", link.getLinkId(), e);
                addPendingSync("link", link.getLinkId(), config);
            }
        } else {
            addPendingSync("link", link.getLinkId(), config);
        }
    }
    
    @Override
    public LinkConfig loadLink(String linkId) {
        LinkConfig cached = linkCache.get(linkId);
        if (cached != null) {
            return cached;
        }
        
        Map<String, Object> config = localStore.loadScene("link:" + linkId);
        if (config != null) {
            LinkConfig link = mapToLink(config);
            linkCache.put(linkId, link);
            return link;
        }
        
        return null;
    }
    
    @Override
    public void deleteLink(String linkId) {
        linkCache.remove(linkId);
        localStore.deleteScene("link:" + linkId);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/links/" + linkId + ".yaml";
                vfsClient.delete(path);
            } catch (Exception e) {
                log.warn("Failed to delete link from VFS: {}", linkId, e);
            }
        }
    }
    
    @Override
    public List<LinkConfig> listLinks(String sceneId) {
        List<LinkConfig> result = new ArrayList<>();
        for (LinkConfig link : linkCache.values()) {
            if (sceneId == null || sceneId.equals(link.getSceneId())) {
                result.add(link);
            }
        }
        return result;
    }
    
    @Override
    public List<LinkConfig> listAllLinks() {
        return new ArrayList<>(linkCache.values());
    }
    
    @Override
    public List<LinkConfig> getLinksBySource(String sourceId) {
        List<LinkConfig> result = new ArrayList<>();
        for (LinkConfig link : linkCache.values()) {
            if (sourceId.equals(link.getSourceId())) {
                result.add(link);
            }
        }
        return result;
    }
    
    @Override
    public List<LinkConfig> getLinksByTarget(String targetId) {
        List<LinkConfig> result = new ArrayList<>();
        for (LinkConfig link : linkCache.values()) {
            if (targetId.equals(link.getTargetId())) {
                result.add(link);
            }
        }
        return result;
    }
    
    @Override
    public List<LinkConfig> getLinksByType(String sceneId, String linkType) {
        List<LinkConfig> result = new ArrayList<>();
        for (LinkConfig link : linkCache.values()) {
            if ((sceneId == null || sceneId.equals(link.getSceneId())) &&
                (linkType == null || linkType.equals(link.getLinkType()))) {
                result.add(link);
            }
        }
        return result;
    }
    
    @Override
    public boolean linkExists(String linkId) {
        return linkCache.containsKey(linkId) || localStore.sceneExists("link:" + linkId);
    }
    
    @Override
    public void updateLinkStatus(String linkId, String status) {
        LinkConfig link = loadLink(linkId);
        if (link != null) {
            link.setStatus(status);
            saveLink(link);
        }
    }
    
    // ==================== AgentStore ====================
    
    private final Map<String, AgentRegistration> agentCache = new ConcurrentHashMap<>();
    
    @Override
    public void saveAgent(AgentRegistration registration) {
        if (registration == null || registration.getAgentId() == null) {
            return;
        }
        
        agentCache.put(registration.getAgentId(), registration);
        
        Map<String, Object> config = agentToMap(registration);
        localStore.saveScene("agent:" + registration.getAgentId(), config);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/agents/" + registration.getAgentId() + ".yaml";
                byte[] data = mapToYaml(config).getBytes(StandardCharsets.UTF_8);
                vfsClient.writeFile(path, data);
            } catch (Exception e) {
                log.warn("Failed to sync agent to VFS: {}", registration.getAgentId(), e);
                addPendingSync("agent", registration.getAgentId(), config);
            }
        } else {
            addPendingSync("agent", registration.getAgentId(), config);
        }
    }
    
    @Override
    public AgentRegistration loadAgent(String agentId) {
        AgentRegistration cached = agentCache.get(agentId);
        if (cached != null) {
            return cached;
        }
        
        Map<String, Object> config = localStore.loadScene("agent:" + agentId);
        if (config != null) {
            AgentRegistration registration = mapToAgent(config);
            agentCache.put(agentId, registration);
            return registration;
        }
        
        return null;
    }
    
    @Override
    public void deleteAgent(String agentId) {
        agentCache.remove(agentId);
        localStore.deleteScene("agent:" + agentId);
        
        if (remoteAvailable) {
            try {
                String path = basePath + "/agents/" + agentId + ".yaml";
                vfsClient.delete(path);
            } catch (Exception e) {
                log.warn("Failed to delete agent from VFS: {}", agentId, e);
            }
        }
    }
    
    @Override
    public List<AgentRegistration> listAgents(String sceneId) {
        List<AgentRegistration> result = new ArrayList<>();
        for (AgentRegistration reg : agentCache.values()) {
            if (sceneId == null || sceneId.equals(reg.getSceneId())) {
                result.add(reg);
            }
        }
        return result;
    }
    
    @Override
    public List<AgentRegistration> listAgentsByGroup(String sceneId, String groupId) {
        List<AgentRegistration> result = new ArrayList<>();
        for (AgentRegistration reg : agentCache.values()) {
            if ((sceneId == null || sceneId.equals(reg.getSceneId())) &&
                (groupId == null || groupId.equals(reg.getGroupId()))) {
                result.add(reg);
            }
        }
        return result;
    }
    
    @Override
    public List<AgentRegistration> listAgentsByRole(String sceneId, String role) {
        List<AgentRegistration> result = new ArrayList<>();
        for (AgentRegistration reg : agentCache.values()) {
            if ((sceneId == null || sceneId.equals(reg.getSceneId())) &&
                (role == null || role.equals(reg.getRole()))) {
                result.add(reg);
            }
        }
        return result;
    }
    
    @Override
    public boolean agentExists(String agentId) {
        return agentCache.containsKey(agentId) || localStore.sceneExists("agent:" + agentId);
    }
    
    @Override
    public void updateAgentHeartbeat(String agentId, long timestamp) {
        AgentRegistration registration = loadAgent(agentId);
        if (registration != null) {
            registration.setLastHeartbeat(timestamp);
            saveAgent(registration);
        }
    }
    
    @Override
    public Long getAgentLastHeartbeat(String agentId) {
        AgentRegistration registration = loadAgent(agentId);
        return registration != null ? registration.getLastHeartbeat() : null;
    }
    
    @Override
    public void updateAgentStatus(String agentId, String status) {
        AgentRegistration registration = loadAgent(agentId);
        if (registration != null) {
            registration.setStatus(status);
            saveAgent(registration);
        }
    }
    
    @Override
    public void updateAgentRole(String agentId, String role) {
        AgentRegistration registration = loadAgent(agentId);
        if (registration != null) {
            registration.setRole(role);
            saveAgent(registration);
        }
    }
    
    // ==================== Sync Methods ====================
    
    public void sync() {
        if (!remoteAvailable) {
            log.warn("Remote storage not available, skip sync");
            return;
        }
        
        SyncEvent event = new SyncEvent(basePath, SyncEvent.SyncType.FULL_SYNC);
        event.setTotalItems(pendingSyncs.size());
        notifySyncStarted(event);
        
        int processed = 0;
        for (PendingSync pending : pendingSyncs.values()) {
            try {
                syncPendingItem(pending);
                pendingSyncs.remove(pending.syncId);
                processed++;
                notifySyncProgress(event, (processed * 100) / event.getTotalItems());
            } catch (Exception e) {
                log.error("Failed to sync pending item: {}", pending.syncId, e);
            }
        }
        
        event.setEndTime(System.currentTimeMillis());
        event.setStatus("completed");
        notifySyncCompleted(event);
        
        status.setLastSyncTime(System.currentTimeMillis());
        status.setPendingChanges(pendingSyncs.size());
    }
    
    private void syncPendingItem(PendingSync pending) {
        String path = basePath + "/" + pending.configType + "s/" + pending.configId + ".yaml";
        byte[] data = mapToYaml(pending.config).getBytes(StandardCharsets.UTF_8);
        vfsClient.writeFile(path, data);
    }
    
    private void addPendingSync(String configType, String configId, Map<String, Object> config) {
        String syncId = configType + ":" + configId;
        PendingSync pending = new PendingSync();
        pending.syncId = syncId;
        pending.configType = configType;
        pending.configId = configId;
        pending.config = config;
        pending.timestamp = System.currentTimeMillis();
        
        pendingSyncs.put(syncId, pending);
        status.incrementPendingChanges();
    }
    
    // ==================== Helper Methods ====================
    
    private ConflictInfo detectConflict(Map<String, Object> local, Map<String, Object> remote) {
        Long localTime = (Long) local.get("updateTime");
        Long remoteTime = (Long) remote.get("updateTime");
        
        if (localTime != null && remoteTime != null && !localTime.equals(remoteTime)) {
            ConflictInfo conflict = new ConflictInfo();
            conflict.setLocalData(local);
            conflict.setRemoteData(remote);
            conflict.setLocalUpdateTime(localTime);
            conflict.setRemoteUpdateTime(remoteTime);
            conflict.setType(ConflictInfo.ConflictType.DATA_MISMATCH);
            return conflict;
        }
        
        return null;
    }
    
    private Map<String, Object> resolveConflict(Map<String, Object> local, Map<String, Object> remote, ConflictInfo conflict) {
        switch (conflictStrategy) {
            case LOCAL_WINS:
                conflict.setResolution("local_wins");
                return local;
            case REMOTE_WINS:
                conflict.setResolution("remote_wins");
                return remote;
            case LATEST_WINS:
                if (conflict.isLocalNewer()) {
                    conflict.setResolution("local_newer");
                    return local;
                } else {
                    conflict.setResolution("remote_newer");
                    return remote;
                }
            case MERGE:
                Map<String, Object> merged = new LinkedHashMap<>(local);
                merged.putAll(remote);
                conflict.setResolution("merged");
                return merged;
            default:
                return remote;
        }
    }
    
    private Map<String, Object> skillToMap(SkillRegistration skill) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("skillId", skill.getSkillId());
        map.put("sceneId", skill.getSceneId());
        map.put("groupId", skill.getGroupId());
        map.put("skillType", skill.getSkillType());
        map.put("endpoints", skill.getEndpoints());
        map.put("registerTime", skill.getRegisterTime());
        map.put("lastHeartbeat", skill.getLastHeartbeat());
        map.put("status", skill.getStatus());
        map.put("metadata", skill.getMetadata());
        return map;
    }
    
    private SkillRegistration mapToSkill(Map<String, Object> map) {
        SkillRegistration skill = new SkillRegistration();
        skill.setSkillId((String) map.get("skillId"));
        skill.setSceneId((String) map.get("sceneId"));
        skill.setGroupId((String) map.get("groupId"));
        skill.setSkillType((String) map.get("skillType"));
        skill.setRegisterTime(getLong(map, "registerTime"));
        skill.setLastHeartbeat(getLong(map, "lastHeartbeat"));
        skill.setStatus((String) map.get("status"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> endpoints = (Map<String, Object>) map.get("endpoints");
        if (endpoints != null) {
            skill.setEndpoints(endpoints);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> metadata = (Map<String, Object>) map.get("metadata");
        if (metadata != null) {
            skill.setMetadata(metadata);
        }
        
        return skill;
    }
    
    private Map<String, Object> linkToMap(LinkConfig link) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("linkId", link.getLinkId());
        map.put("sceneId", link.getSceneId());
        map.put("sourceId", link.getSourceId());
        map.put("targetId", link.getTargetId());
        map.put("linkType", link.getLinkType());
        map.put("direction", link.getDirection() != null ? link.getDirection().name() : null);
        map.put("config", link.getConfig());
        map.put("createTime", link.getCreateTime());
        map.put("updateTime", link.getUpdateTime());
        map.put("status", link.getStatus());
        return map;
    }
    
    private LinkConfig mapToLink(Map<String, Object> map) {
        LinkConfig link = new LinkConfig();
        link.setLinkId((String) map.get("linkId"));
        link.setSceneId((String) map.get("sceneId"));
        link.setSourceId((String) map.get("sourceId"));
        link.setTargetId((String) map.get("targetId"));
        link.setLinkType((String) map.get("linkType"));
        link.setCreateTime(getLong(map, "createTime"));
        link.setUpdateTime(getLong(map, "updateTime"));
        link.setStatus((String) map.get("status"));
        
        String direction = (String) map.get("direction");
        if (direction != null) {
            link.setDirection(LinkConfig.LinkDirection.valueOf(direction));
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> config = (Map<String, Object>) map.get("config");
        if (config != null) {
            link.setConfig(config);
        }
        
        return link;
    }
    
    private Map<String, Object> agentToMap(AgentRegistration agent) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("agentId", agent.getAgentId());
        map.put("agentName", agent.getAgentName());
        map.put("sceneId", agent.getSceneId());
        map.put("groupId", agent.getGroupId());
        map.put("endpoint", agent.getEndpoint());
        map.put("role", agent.getRole());
        map.put("status", agent.getStatus());
        map.put("composition", agent.getComposition());
        map.put("capabilities", agent.getCapabilities());
        map.put("registerTime", agent.getRegisterTime());
        map.put("lastHeartbeat", agent.getLastHeartbeat());
        map.put("metadata", agent.getMetadata());
        return map;
    }
    
    private AgentRegistration mapToAgent(Map<String, Object> map) {
        AgentRegistration agent = new AgentRegistration();
        agent.setAgentId((String) map.get("agentId"));
        agent.setAgentName((String) map.get("agentName"));
        agent.setSceneId((String) map.get("sceneId"));
        agent.setGroupId((String) map.get("groupId"));
        agent.setEndpoint((String) map.get("endpoint"));
        agent.setRole((String) map.get("role"));
        agent.setStatus((String) map.get("status"));
        agent.setRegisterTime(getLong(map, "registerTime"));
        agent.setLastHeartbeat(getLong(map, "lastHeartbeat"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> composition = (Map<String, Object>) map.get("composition");
        if (composition != null) {
            agent.setComposition(composition);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> capabilities = (Map<String, Object>) map.get("capabilities");
        if (capabilities != null) {
            agent.setCapabilities(capabilities);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> metadata = (Map<String, Object>) map.get("metadata");
        if (metadata != null) {
            agent.setMetadata(metadata);
        }
        
        return agent;
    }
    
    private long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }
    
    private String mapToYaml(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        mapToYamlInternal(map, sb, 0);
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    private void mapToYamlInternal(Map<String, Object> map, StringBuilder sb, int indent) {
        String indentStr = repeat("  ", indent);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                sb.append(indentStr).append(entry.getKey()).append(":\n");
                mapToYamlInternal((Map<String, Object>) value, sb, indent + 1);
            } else if (value instanceof List) {
                sb.append(indentStr).append(entry.getKey()).append(":\n");
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        sb.append(indentStr).append("  -\n");
                        mapToYamlInternal((Map<String, Object>) item, sb, indent + 2);
                    } else {
                        sb.append(indentStr).append("  - ").append(item).append("\n");
                    }
                }
            } else if (value != null) {
                sb.append(indentStr).append(entry.getKey()).append(": ").append(value).append("\n");
            }
        }
    }
    
    private Map<String, Object> yamlToMap(String yaml) {
        Map<String, Object> result = new LinkedHashMap<>();
        String[] lines = yaml.split("\n");
        for (String line : lines) {
            if (line.contains(":") && !line.trim().startsWith("-")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (!value.isEmpty()) {
                        result.put(key, parseValue(value));
                    }
                }
            }
        }
        return result;
    }
    
    private Object parseValue(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    private String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    // ==================== Listener Methods ====================
    
    public void addSyncListener(SyncListener listener) {
        syncListeners.add(listener);
    }
    
    public void removeSyncListener(SyncListener listener) {
        syncListeners.remove(listener);
    }
    
    private void notifySyncStarted(SyncEvent event) {
        for (SyncListener listener : syncListeners) {
            try {
                listener.onSyncStarted(event);
            } catch (Exception e) {
                log.warn("SyncListener error", e);
            }
        }
    }
    
    private void notifySyncProgress(SyncEvent event, int progress) {
        for (SyncListener listener : syncListeners) {
            try {
                listener.onSyncProgress(event, progress);
            } catch (Exception e) {
                log.warn("SyncListener error", e);
            }
        }
    }
    
    private void notifySyncCompleted(SyncEvent event) {
        for (SyncListener listener : syncListeners) {
            try {
                listener.onSyncCompleted(event);
            } catch (Exception e) {
                log.warn("SyncListener error", e);
            }
        }
    }
    
    public StorageStatus getStatus() {
        return status;
    }
    
    public boolean isRemoteAvailable() {
        return remoteAvailable;
    }
    
    public void setRemoteAvailable(boolean available) {
        this.remoteAvailable = available;
        status.setRemoteAvailable(available);
    }
    
    private static class PendingSync {
        String syncId;
        String configType;
        String configId;
        Map<String, Object> config;
        long timestamp;
        int retryCount;
    }
}

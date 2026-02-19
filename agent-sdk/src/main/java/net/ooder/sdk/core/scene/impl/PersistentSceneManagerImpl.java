package net.ooder.sdk.core.scene.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.scene.SceneDefinition;
import net.ooder.sdk.api.scene.SceneManager;
import net.ooder.sdk.api.scene.SceneSnapshot;
import net.ooder.sdk.api.scene.SceneManager.SceneState;
import net.ooder.sdk.api.scene.store.SceneStore;
import net.ooder.sdk.api.skill.Capability;

public class PersistentSceneManagerImpl implements SceneManager {
    
    private static final Logger log = LoggerFactory.getLogger(PersistentSceneManagerImpl.class);
    
    private final Map<String, SceneDefinition> scenes = new ConcurrentHashMap<>();
    private final Map<String, SceneState> sceneStates = new ConcurrentHashMap<>();
    
    private final SceneStore sceneStore;
    private final boolean persistenceEnabled;
    
    public PersistentSceneManagerImpl() {
        this(null, false);
    }
    
    public PersistentSceneManagerImpl(SceneStore sceneStore, boolean persistenceEnabled) {
        this.sceneStore = sceneStore;
        this.persistenceEnabled = persistenceEnabled;
    }
    
    public void start() {
        if (persistenceEnabled && sceneStore != null) {
            loadFromPersistence().join();
        }
    }
    
    public void shutdown() {
        if (persistenceEnabled && sceneStore != null) {
            for (String sceneId : scenes.keySet()) {
                try {
                    SceneDefinition definition = scenes.get(sceneId);
                    if (definition != null) {
                        sceneStore.saveScene(sceneId, definitionToConfig(definition));
                    }
                } catch (Exception e) {
                    log.error("Failed to persist scene on shutdown: {}", sceneId, e);
                }
            }
        }
    }
    
    @Override
    public CompletableFuture<SceneDefinition> create(SceneDefinition definition) {
        return CompletableFuture.supplyAsync(() -> {
            if (definition == null) {
                throw new IllegalArgumentException("SceneDefinition cannot be null");
            }
            
            String sceneId = definition.getSceneId();
            if (sceneId == null || sceneId.trim().isEmpty()) {
                throw new IllegalArgumentException("SceneId cannot be null or empty");
            }
            
            if (scenes.containsKey(sceneId)) {
                log.warn("Scene already exists, overwriting: {}", sceneId);
            }
            
            scenes.put(sceneId, definition);
            
            SceneState state = new SceneState();
            state.setSceneId(sceneId);
            state.setActive(false);
            state.setMemberCount(0);
            state.setInstalledSkills(new ArrayList<String>());
            state.setCreateTime(System.currentTimeMillis());
            state.setLastUpdateTime(System.currentTimeMillis());
            
            sceneStates.put(sceneId, state);
            
            if (persistenceEnabled && sceneStore != null) {
                try {
                    Map<String, Object> config = definitionToConfig(definition);
                    config.put("createTime", state.getCreateTime());
                    config.put("updateTime", state.getLastUpdateTime());
                    sceneStore.saveScene(sceneId, config);
                    log.debug("Scene persisted: {}", sceneId);
                } catch (Exception e) {
                    log.error("Failed to persist scene: {}", sceneId, e);
                }
            }
            
            log.info("Scene created: {}", sceneId);
            
            return definition;
        });
    }
    
    @Override
    public CompletableFuture<Void> delete(String sceneId) {
        return CompletableFuture.runAsync(() -> {
            if (sceneId == null || sceneId.trim().isEmpty()) {
                log.warn("Cannot delete scene with null or empty id");
                return;
            }
            
            SceneDefinition removed = scenes.remove(sceneId);
            sceneStates.remove(sceneId);
            
            if (persistenceEnabled && sceneStore != null) {
                try {
                    sceneStore.deleteScene(sceneId);
                    log.debug("Scene persistence deleted: {}", sceneId);
                } catch (Exception e) {
                    log.error("Failed to delete scene persistence: {}", sceneId, e);
                }
            }
            
            if (removed != null) {
                log.info("Scene deleted: {}", sceneId);
            } else {
                log.debug("Scene not found for deletion: {}", sceneId);
            }
        });
    }
    
    @Override
    public CompletableFuture<SceneDefinition> get(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            if (sceneId == null || sceneId.trim().isEmpty()) {
                return null;
            }
            return scenes.get(sceneId);
        });
    }
    
    @Override
    public CompletableFuture<List<SceneDefinition>> listAll() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<SceneDefinition>(scenes.values()));
    }
    
    @Override
    public CompletableFuture<Void> activate(String sceneId) {
        return CompletableFuture.runAsync(() -> {
            if (sceneId == null || sceneId.trim().isEmpty()) {
                log.warn("Cannot activate scene with null or empty id");
                return;
            }
            
            SceneState state = sceneStates.get(sceneId);
            if (state == null) {
                log.warn("Scene not found for activation: {}", sceneId);
                return;
            }
            
            if (state.isActive()) {
                log.debug("Scene already active: {}", sceneId);
                return;
            }
            
            state.setActive(true);
            state.setLastUpdateTime(System.currentTimeMillis());
            
            updatePersistenceState(sceneId, state);
            
            log.info("Scene activated: {}", sceneId);
        });
    }
    
    @Override
    public CompletableFuture<Void> deactivate(String sceneId) {
        return CompletableFuture.runAsync(() -> {
            if (sceneId == null || sceneId.trim().isEmpty()) {
                log.warn("Cannot deactivate scene with null or empty id");
                return;
            }
            
            SceneState state = sceneStates.get(sceneId);
            if (state == null) {
                log.warn("Scene not found for deactivation: {}", sceneId);
                return;
            }
            
            if (!state.isActive()) {
                log.debug("Scene already inactive: {}", sceneId);
                return;
            }
            
            state.setActive(false);
            state.setLastUpdateTime(System.currentTimeMillis());
            
            updatePersistenceState(sceneId, state);
            
            log.info("Scene deactivated: {}", sceneId);
        });
    }
    
    @Override
    public CompletableFuture<SceneState> getState(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            if (sceneId == null || sceneId.trim().isEmpty()) {
                return null;
            }
            return sceneStates.get(sceneId);
        });
    }
    
    @Override
    public CompletableFuture<Void> addCapability(String sceneId, Capability capability) {
        return CompletableFuture.runAsync(() -> {
            if (sceneId == null || sceneId.trim().isEmpty()) {
                log.warn("Cannot add capability to scene with null or empty id");
                return;
            }
            
            if (capability == null) {
                log.warn("Cannot add null capability to scene: {}", sceneId);
                return;
            }
            
            SceneDefinition definition = scenes.get(sceneId);
            if (definition != null) {
                if (definition.getCapabilities() == null) {
                    definition.setCapabilities(new ArrayList<Capability>());
                }
                definition.getCapabilities().add(capability);
                
                updatePersistence(sceneId, definition);
                
                log.info("Capability {} added to scene {}", capability.getCapId(), sceneId);
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> removeCapability(String sceneId, String capId) {
        return CompletableFuture.runAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            if (definition != null && definition.getCapabilities() != null) {
                definition.getCapabilities().removeIf(c -> capId.equals(c.getCapId()));
                
                updatePersistence(sceneId, definition);
                
                log.info("Capability {} removed from scene {}", capId, sceneId);
            }
        });
    }
    
    @Override
    public CompletableFuture<List<Capability>> listCapabilities(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            return definition != null && definition.getCapabilities() != null 
                ? new ArrayList<>(definition.getCapabilities()) 
                : new ArrayList<>();
        });
    }
    
    @Override
    public CompletableFuture<Capability> getCapability(String sceneId, String capId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            if (definition != null && definition.getCapabilities() != null) {
                for (Capability cap : definition.getCapabilities()) {
                    if (capId.equals(cap.getCapId())) {
                        return cap;
                    }
                }
            }
            return null;
        });
    }
    
    @Override
    public CompletableFuture<Void> addCollaborativeScene(String sceneId, String collaborativeSceneId) {
        return CompletableFuture.runAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            if (definition != null) {
                if (definition.getCollaborativeScenes() == null) {
                    definition.setCollaborativeScenes(new ArrayList<>());
                }
                if (!definition.getCollaborativeScenes().contains(collaborativeSceneId)) {
                    definition.getCollaborativeScenes().add(collaborativeSceneId);
                    
                    updatePersistence(sceneId, definition);
                    
                    log.info("Collaborative scene {} added to {}", collaborativeSceneId, sceneId);
                }
            }
        });
    }
    
    @Override
    public CompletableFuture<Void> removeCollaborativeScene(String sceneId, String collaborativeSceneId) {
        return CompletableFuture.runAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            if (definition != null && definition.getCollaborativeScenes() != null) {
                definition.getCollaborativeScenes().remove(collaborativeSceneId);
                
                updatePersistence(sceneId, definition);
                
                log.info("Collaborative scene {} removed from {}", collaborativeSceneId, sceneId);
            }
        });
    }
    
    @Override
    public CompletableFuture<List<String>> listCollaborativeScenes(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            return definition != null && definition.getCollaborativeScenes() != null 
                ? new ArrayList<>(definition.getCollaborativeScenes()) 
                : new ArrayList<>();
        });
    }
    
    @Override
    public CompletableFuture<Void> updateConfig(String sceneId, Map<String, Object> config) {
        return CompletableFuture.runAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            if (definition != null) {
                if (definition.getConfig() == null) {
                    definition.setConfig(new ConcurrentHashMap<>());
                }
                definition.getConfig().putAll(config);
                
                SceneState state = sceneStates.get(sceneId);
                if (state != null) {
                    state.setLastUpdateTime(System.currentTimeMillis());
                }
                
                updatePersistence(sceneId, definition);
                
                log.info("Config updated for scene {}", sceneId);
            }
        });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getConfig(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            return definition != null ? definition.getConfig() : null;
        });
    }
    
    @Override
    public CompletableFuture<SceneSnapshot> createSnapshot(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            SceneDefinition definition = scenes.get(sceneId);
            SceneState state = sceneStates.get(sceneId);
            
            if (definition == null) {
                return null;
            }
            
            SceneSnapshot snapshot = new SceneSnapshot();
            snapshot.setSceneId(sceneId);
            snapshot.setSnapshotId(java.util.UUID.randomUUID().toString());
            snapshot.setCreateTime(System.currentTimeMillis());
            snapshot.setVersion(definition.getVersion());
            snapshot.setSkills(state != null ? new ArrayList<>(state.getInstalledSkills()) : new ArrayList<>());
            snapshot.setConfig(new ConcurrentHashMap<>(definition.getConfig()));
            
            log.info("Snapshot created for scene {}: {}", sceneId, snapshot.getSnapshotId());
            
            return snapshot;
        });
    }
    
    @Override
    public CompletableFuture<Void> restoreSnapshot(String sceneId, SceneSnapshot snapshot) {
        return CompletableFuture.runAsync(() -> {
            if (snapshot == null || !sceneId.equals(snapshot.getSceneId())) {
                return;
            }
            
            SceneDefinition definition = scenes.get(sceneId);
            SceneState state = sceneStates.get(sceneId);
            
            if (definition != null && snapshot.getConfig() != null) {
                definition.setConfig(new ConcurrentHashMap<>(snapshot.getConfig()));
            }
            
            if (state != null && snapshot.getSkills() != null) {
                state.setInstalledSkills(new ArrayList<>(snapshot.getSkills()));
                state.setLastUpdateTime(System.currentTimeMillis());
            }
            
            updatePersistence(sceneId, definition);
            
            log.info("Snapshot {} restored for scene {}", snapshot.getSnapshotId(), sceneId);
        });
    }
    
    private CompletableFuture<Void> loadFromPersistence() {
        return CompletableFuture.runAsync(() -> {
            if (sceneStore == null) {
                return;
            }
            
            List<String> sceneIds = sceneStore.listScenes();
            log.info("Loading {} scenes from persistence", sceneIds.size());
            
            for (String sceneId : sceneIds) {
                try {
                    Map<String, Object> config = sceneStore.loadScene(sceneId);
                    if (config != null) {
                        SceneDefinition definition = configToDefinition(config);
                        scenes.put(sceneId, definition);
                        
                        SceneState state = new SceneState();
                        state.setSceneId(sceneId);
                        state.setActive(getBoolean(config, "active", false));
                        state.setMemberCount(getInt(config, "memberCount", 0));
                        state.setInstalledSkills(getStringList(config, "installedSkills"));
                        state.setCreateTime(getLong(config, "createTime", System.currentTimeMillis()));
                        state.setLastUpdateTime(getLong(config, "updateTime", System.currentTimeMillis()));
                        
                        sceneStates.put(sceneId, state);
                        
                        log.debug("Scene loaded from persistence: {}", sceneId);
                    }
                } catch (Exception e) {
                    log.error("Failed to load scene from persistence: {}", sceneId, e);
                }
            }
        });
    }
    
    private void updatePersistence(String sceneId, SceneDefinition definition) {
        if (!persistenceEnabled || sceneStore == null) {
            return;
        }
        
        try {
            Map<String, Object> config = definitionToConfig(definition);
            
            SceneState state = sceneStates.get(sceneId);
            if (state != null) {
                config.put("active", state.isActive());
                config.put("memberCount", state.getMemberCount());
                config.put("installedSkills", state.getInstalledSkills());
                config.put("updateTime", state.getLastUpdateTime());
            }
            
            sceneStore.saveScene(sceneId, config);
        } catch (Exception e) {
            log.error("Failed to update scene persistence: {}", sceneId, e);
        }
    }
    
    private void updatePersistenceState(String sceneId, SceneState state) {
        if (!persistenceEnabled || sceneStore == null) {
            return;
        }
        
        try {
            sceneStore.updateSceneConfig(sceneId, "active", state.isActive());
            sceneStore.updateSceneConfig(sceneId, "updateTime", state.getLastUpdateTime());
        } catch (Exception e) {
            log.error("Failed to update scene state persistence: {}", sceneId, e);
        }
    }
    
    private Map<String, Object> definitionToConfig(SceneDefinition definition) {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("sceneId", definition.getSceneId());
        config.put("name", definition.getName());
        config.put("description", definition.getDescription());
        config.put("version", definition.getVersion());
        config.put("scenePrefix", definition.getScenePrefix());
        
        if (definition.getType() != null) {
            config.put("type", definition.getType().name());
        }
        
        if (definition.getConfig() != null) {
            config.put("config", definition.getConfig());
        }
        if (definition.getVfsConfig() != null) {
            config.put("vfsConfig", definition.getVfsConfig());
        }
        if (definition.getAuthConfig() != null) {
            config.put("authConfig", definition.getAuthConfig());
        }
        if (definition.getCollaborativeScenes() != null) {
            config.put("collaborativeScenes", definition.getCollaborativeScenes());
        }
        
        return config;
    }
    
    private SceneDefinition configToDefinition(Map<String, Object> config) {
        SceneDefinition definition = new SceneDefinition();
        definition.setSceneId((String) config.get("sceneId"));
        definition.setName((String) config.get("name"));
        definition.setDescription((String) config.get("description"));
        definition.setVersion((String) config.get("version"));
        definition.setScenePrefix((String) config.get("scenePrefix"));
        
        String typeStr = (String) config.get("type");
        if (typeStr != null) {
            try {
                definition.setType(net.ooder.sdk.common.enums.SceneType.valueOf(typeStr));
            } catch (Exception e) {
                definition.setType(net.ooder.sdk.common.enums.SceneType.PRIMARY);
            }
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> sceneConfig = (Map<String, Object>) config.get("config");
        if (sceneConfig != null) {
            definition.setConfig(sceneConfig);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> vfsConfig = (Map<String, Object>) config.get("vfsConfig");
        if (vfsConfig != null) {
            definition.setVfsConfig(vfsConfig);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> authConfig = (Map<String, Object>) config.get("authConfig");
        if (authConfig != null) {
            definition.setAuthConfig(authConfig);
        }
        
        @SuppressWarnings("unchecked")
        List<String> collaborativeScenes = (List<String>) config.get("collaborativeScenes");
        if (collaborativeScenes != null) {
            definition.setCollaborativeScenes(collaborativeScenes);
        }
        
        return definition;
    }
    
    private boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
        Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    private int getInt(Map<String, Object> map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    private long getLong(Map<String, Object> map, String key, long defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return defaultValue;
    }
    
    @SuppressWarnings("unchecked")
    private List<String> getStringList(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof List) {
            return new ArrayList<>((List<String>) value);
        }
        return new ArrayList<>();
    }
    
    public int getSceneCount() {
        return scenes.size();
    }
    
    public boolean isPersistenceEnabled() {
        return persistenceEnabled;
    }
}

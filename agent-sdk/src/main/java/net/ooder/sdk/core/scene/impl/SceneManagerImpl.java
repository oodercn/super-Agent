
package net.ooder.sdk.core.scene.impl;

import java.util.ArrayList;
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
import net.ooder.sdk.api.skill.Capability;

public class SceneManagerImpl implements SceneManager {
    
    private static final Logger log = LoggerFactory.getLogger(SceneManagerImpl.class);
    
    private final Map<String, SceneDefinition> scenes = new ConcurrentHashMap<>();
    private final Map<String, SceneState> sceneStates = new ConcurrentHashMap<>();
    
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
            
            log.info("Snapshot {} restored for scene {}", snapshot.getSnapshotId(), sceneId);
        });
    }
}

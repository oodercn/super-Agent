package net.ooder.mvp.skill.scene.capability.service.impl;

import net.ooder.mvp.skill.scene.capability.model.CapabilityState;
import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;
import net.ooder.mvp.skill.scene.capability.service.CapabilityStateService;
import net.ooder.skill.common.storage.JsonStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CapabilityStateServiceImpl implements CapabilityStateService {

    private static final Logger log = LoggerFactory.getLogger(CapabilityStateServiceImpl.class);
    
    private static final String COLLECTION_NAME = "capability_states";
    
    private final Map<String, CapabilityState> stateCache = new ConcurrentHashMap<>();
    
    private JsonStorageService jsonStorageService;

    public CapabilityStateServiceImpl() {
    }

    @Autowired
    public void setJsonStorageService(JsonStorageService jsonStorageService) {
        this.jsonStorageService = jsonStorageService;
    }

    @PostConstruct
    public void init() {
        reload();
        syncFromRegistryProperties();
    }
    
    private void syncFromRegistryProperties() {
        String[] registryPaths = {
            "./data/installed-skills/registry.properties",
            "./.ooder/installed/registry.properties",
            "./.ooder/registry.properties"
        };
        
        for (String registryPath : registryPaths) {
            File registryFile = new File(registryPath);
            if (registryFile.exists()) {
                try {
                    Properties props = new Properties();
                    try (FileInputStream fis = new FileInputStream(registryFile)) {
                        props.load(fis);
                    }
                    
                    int syncedCount = 0;
                    for (String key : props.stringPropertyNames()) {
                        if (key.endsWith(".id")) {
                            String skillId = props.getProperty(key);
                            if (skillId != null && !skillId.isEmpty()) {
                                if (!isInstalled(skillId)) {
                                    CapabilityState state = stateCache.computeIfAbsent(skillId, CapabilityState::new);
                                    state.setInstalled(true);
                                    state.setStatus(CapabilityStatus.REGISTERED);
                                    
                                    String installedAt = props.getProperty(skillId + ".installedAt");
                                    if (installedAt != null && !installedAt.isEmpty()) {
                                        try {
                                            state.setInstallTime(Long.parseLong(installedAt));
                                        } catch (NumberFormatException e) {
                                            state.setInstallTime(System.currentTimeMillis());
                                        }
                                    } else {
                                        state.setInstallTime(System.currentTimeMillis());
                                    }
                                    state.setInstallSource("registry");
                                    syncedCount++;
                                    log.debug("[syncFromRegistryProperties] Synced installed state for: {}", skillId);
                                }
                            }
                        }
                    }
                    
                    if (syncedCount > 0) {
                        persistAllStates();
                        log.info("[syncFromRegistryProperties] Synced {} installed skills from {}", syncedCount, registryPath);
                    }
                    return;
                } catch (Exception e) {
                    log.warn("[syncFromRegistryProperties] Failed to sync from {}: {}", registryPath, e.getMessage());
                }
            }
        }
        log.info("[syncFromRegistryProperties] No registry file found to sync");
    }

    @Override
    public CapabilityState getState(String capabilityId) {
        return stateCache.get(capabilityId);
    }

    @Override
    public boolean isInstalled(String capabilityId) {
        CapabilityState state = stateCache.get(capabilityId);
        return state != null && state.isInstalled();
    }

    @Override
    public CapabilityStatus getStatus(String capabilityId) {
        CapabilityState state = stateCache.get(capabilityId);
        return state != null ? state.getStatus() : CapabilityStatus.DRAFT;
    }

    @Override
    public void setInstalled(String capabilityId, boolean installed) {
        CapabilityState state = stateCache.computeIfAbsent(capabilityId, CapabilityState::new);
        state.setInstalled(installed);
        if (!installed) {
            state.setStatus(CapabilityStatus.DRAFT);
        }
        persistState(state);
        log.info("[setInstalled] Capability {} installed={}", capabilityId, installed);
    }

    @Override
    public void setInstalled(String capabilityId, boolean installed, String userId, String source) {
        CapabilityState state = stateCache.computeIfAbsent(capabilityId, CapabilityState::new);
        if (installed) {
            state.markInstalled(userId, source);
        } else {
            state.markUninstalled();
        }
        persistState(state);
        log.info("[setInstalled] Capability {} installed={} by user={} source={}", 
            capabilityId, installed, userId, source);
    }

    @Override
    public void setStatus(String capabilityId, CapabilityStatus status) {
        CapabilityState state = stateCache.computeIfAbsent(capabilityId, CapabilityState::new);
        state.setStatus(status);
        persistState(state);
        log.info("[setStatus] Capability {} status={}", capabilityId, status);
    }

    @Override
    public void updateState(String capabilityId, boolean installed, CapabilityStatus status) {
        updateState(capabilityId, installed, status, null, null, null);
    }

    @Override
    public void updateState(String capabilityId, boolean installed, CapabilityStatus status,
                           String userId, String source, String sceneGroupId) {
        CapabilityState state = stateCache.computeIfAbsent(capabilityId, CapabilityState::new);
        state.setInstalled(installed);
        state.setStatus(status);
        state.setInstalledBy(userId);
        state.setInstallSource(source);
        state.setSceneGroupId(sceneGroupId);
        if (installed) {
            state.setInstallTime(System.currentTimeMillis());
        }
        state.setUpdateTime(System.currentTimeMillis());
        persistState(state);
        log.info("[updateState] Capability {} installed={} status={} user={}", 
            capabilityId, installed, status, userId);
    }

    @Override
    public void removeState(String capabilityId) {
        CapabilityState removed = stateCache.remove(capabilityId);
        if (removed != null) {
            persistAllStates();
            log.info("[removeState] Removed state for capability {}", capabilityId);
        }
    }

    @Override
    public List<CapabilityState> listAllStates() {
        return new ArrayList<>(stateCache.values());
    }

    @Override
    public List<CapabilityState> listInstalledStates() {
        return stateCache.values().stream()
            .filter(CapabilityState::isInstalled)
            .collect(Collectors.toList());
    }

    @Override
    public List<CapabilityState> listByStatus(CapabilityStatus status) {
        return stateCache.values().stream()
            .filter(s -> s.getStatus() == status)
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, CapabilityState> getAllStates() {
        return new HashMap<>(stateCache);
    }

    @Override
    public void saveAll() {
        persistAllStates();
    }

    @Override
    public void reload() {
        try {
            List<CapabilityState> states = jsonStorageService.loadList(COLLECTION_NAME, CapabilityState.class);
            stateCache.clear();
            for (CapabilityState state : states) {
                if (state.getCapabilityId() != null) {
                    stateCache.put(state.getCapabilityId(), state);
                }
            }
            log.info("[reload] Loaded {} capability states", stateCache.size());
        } catch (Exception e) {
            log.warn("[reload] Failed to load capability states, starting with empty cache: {}", e.getMessage());
        }
    }

    @Override
    public String getSceneGroupId(String capabilityId) {
        CapabilityState state = stateCache.get(capabilityId);
        return state != null ? state.getSceneGroupId() : null;
    }

    @Override
    public void setSceneGroupId(String capabilityId, String sceneGroupId) {
        CapabilityState state = stateCache.computeIfAbsent(capabilityId, CapabilityState::new);
        state.setSceneGroupId(sceneGroupId);
        state.setUpdateTime(System.currentTimeMillis());
        persistState(state);
        log.info("[setSceneGroupId] Capability {} sceneGroupId={}", capabilityId, sceneGroupId);
    }
    
    private void persistState(CapabilityState state) {
        if (state.getCapabilityId() != null) {
            stateCache.put(state.getCapabilityId(), state);
        }
        persistAllStates();
    }
    
    private void persistAllStates() {
        try {
            List<CapabilityState> states = new ArrayList<>(stateCache.values());
            jsonStorageService.saveList(COLLECTION_NAME, states);
            log.debug("[persistAllStates] Saved {} capability states", states.size());
        } catch (Exception e) {
            log.error("[persistAllStates] Failed to persist capability states: {}", e.getMessage());
        }
    }
}

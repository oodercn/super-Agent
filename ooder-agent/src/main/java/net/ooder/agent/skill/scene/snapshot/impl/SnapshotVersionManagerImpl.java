package net.ooder.mvp.skill.scene.snapshot.impl;

import net.ooder.mvp.skill.scene.snapshot.SnapshotVersionManager;
import net.ooder.mvp.skill.scene.snapshot.SnapshotVersionDTO;
import net.ooder.mvp.skill.scene.snapshot.SnapshotDiffDTO;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import net.ooder.mvp.skill.scene.dto.scene.SceneSnapshotDTO;
import net.ooder.skill.common.storage.JsonStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SnapshotVersionManagerImpl implements SnapshotVersionManager {
    
    private static final Logger log = LoggerFactory.getLogger(SnapshotVersionManagerImpl.class);
    
    private static final String STORAGE_KEY_VERSIONS = "snapshot-versions";
    
    @Autowired
    private JsonStorageService storage;
    
    @Autowired(required = false)
    private SceneGroupService sceneGroupService;
    
    private final Map<String, SnapshotVersionDTO> versions = new ConcurrentHashMap<>();
    private final Map<String, List<String>> snapshotVersions = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        loadFromStorage();
        log.info("[SnapshotVersionManager] Initialized with {} versions", versions.size());
    }
    
    private void loadFromStorage() {
        try {
            Map<String, SnapshotVersionDTO> storedVersions = storage.getAll(STORAGE_KEY_VERSIONS);
            if (storedVersions != null) {
                versions.putAll(storedVersions);
                
                for (SnapshotVersionDTO version : versions.values()) {
                    String snapshotId = version.getSnapshotId();
                    snapshotVersions.computeIfAbsent(snapshotId, k -> new ArrayList<>())
                        .add(version.getVersionId());
                }
                
                for (List<String> versionList : snapshotVersions.values()) {
                    versionList.sort((a, b) -> {
                        SnapshotVersionDTO va = versions.get(a);
                        SnapshotVersionDTO vb = versions.get(b);
                        return Long.compare(vb.getCreateTime(), va.getCreateTime());
                    });
                }
            }
        } catch (Exception e) {
            log.warn("[SnapshotVersionManager] Failed to load versions: {}", e.getMessage());
        }
    }
    
    @Override
    public String createVersion(String snapshotId, String versionName) {
        log.info("[createVersion] Creating version for snapshot: {}", snapshotId);
        
        String versionId = "ver-" + UUID.randomUUID().toString().substring(0, 12);
        
        List<String> existingVersions = snapshotVersions.getOrDefault(snapshotId, new ArrayList<>());
        int versionNumber = existingVersions.size() + 1;
        
        for (String vid : existingVersions) {
            SnapshotVersionDTO v = versions.get(vid);
            if (v != null) {
                v.setLatest(false);
                persistVersion(v);
            }
        }
        
        SnapshotVersionDTO version = new SnapshotVersionDTO();
        version.setVersionId(versionId);
        version.setSnapshotId(snapshotId);
        version.setVersionName(versionName != null ? versionName : "Version " + versionNumber);
        version.setVersionNumber("v" + versionNumber);
        version.setCreateTime(System.currentTimeMillis());
        version.setLatest(true);
        
        if (!existingVersions.isEmpty()) {
            version.setParentVersionId(existingVersions.get(0));
        }
        
        versions.put(versionId, version);
        snapshotVersions.computeIfAbsent(snapshotId, k -> new ArrayList<>()).add(0, versionId);
        
        persistVersion(version);
        
        log.info("[createVersion] Version created: {} ({})", versionId, version.getVersionNumber());
        return versionId;
    }
    
    @Override
    public SnapshotVersionDTO getVersion(String versionId) {
        return versions.get(versionId);
    }
    
    @Override
    public List<SnapshotVersionDTO> getVersionHistory(String snapshotId) {
        List<String> versionIds = snapshotVersions.get(snapshotId);
        if (versionIds == null || versionIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        return versionIds.stream()
            .map(versions::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    @Override
    public SnapshotDiffDTO compareVersions(String versionId1, String versionId2) {
        log.info("[compareVersions] Comparing {} vs {}", versionId1, versionId2);
        
        SnapshotVersionDTO v1 = versions.get(versionId1);
        SnapshotVersionDTO v2 = versions.get(versionId2);
        
        if (v1 == null || v2 == null) {
            log.warn("[compareVersions] One or both versions not found");
            return null;
        }
        
        SnapshotDiffDTO diff = new SnapshotDiffDTO();
        diff.setVersionId1(versionId1);
        diff.setVersionId2(versionId2);
        diff.setSnapshotId(v1.getSnapshotId());
        diff.setCompareTime(System.currentTimeMillis());
        
        if (v1.getChecksum() != null && v1.getChecksum().equals(v2.getChecksum())) {
            diff.setIdentical(true);
            diff.setAddedCount(0);
            diff.setRemovedCount(0);
            diff.setModifiedCount(0);
            return diff;
        }
        
        diff.setIdentical(false);
        
        List<SnapshotDiffDTO.FieldDiff> fieldDiffs = new ArrayList<>();
        diff.setFieldDiffs(fieldDiffs);
        
        Map<String, Object> added = new HashMap<>();
        Map<String, Object> removed = new HashMap<>();
        Map<String, Object> modified = new HashMap<>();
        
        if (v1.getMetadata() != null && v2.getMetadata() != null) {
            for (Map.Entry<String, Object> entry : v1.getMetadata().entrySet()) {
                String key = entry.getKey();
                Object val1 = entry.getValue();
                Object val2 = v2.getMetadata().get(key);
                
                if (!v2.getMetadata().containsKey(key)) {
                    removed.put(key, val1);
                } else if (!Objects.equals(val1, val2)) {
                    modified.put(key, val2);
                    SnapshotDiffDTO.FieldDiff fieldDiff = new SnapshotDiffDTO.FieldDiff();
                    fieldDiff.setFieldPath(key);
                    fieldDiff.setOldValue(val1);
                    fieldDiff.setNewValue(val2);
                    fieldDiff.setDiffType("MODIFIED");
                    fieldDiffs.add(fieldDiff);
                }
            }
            
            for (Map.Entry<String, Object> entry : v2.getMetadata().entrySet()) {
                if (!v1.getMetadata().containsKey(entry.getKey())) {
                    added.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        diff.setAddedData(added);
        diff.setRemovedData(removed);
        diff.setModifiedData(modified);
        diff.setAddedCount(added.size());
        diff.setRemovedCount(removed.size());
        diff.setModifiedCount(modified.size());
        
        return diff;
    }
    
    @Override
    public boolean rollbackToVersion(String sceneGroupId, String versionId) {
        log.info("[rollbackToVersion] Rolling back {} to version {}", sceneGroupId, versionId);
        
        SnapshotVersionDTO version = versions.get(versionId);
        if (version == null) {
            log.warn("[rollbackToVersion] Version not found: {}", versionId);
            return false;
        }
        
        if (sceneGroupService != null) {
            try {
                SceneSnapshotDTO snapshot = new SceneSnapshotDTO();
                snapshot.setSnapshotId(version.getSnapshotId());
                snapshot.setSceneGroupId(version.getSceneGroupId());
                return sceneGroupService.restoreSnapshot(version.getSceneGroupId(), snapshot);
            } catch (Exception e) {
                log.error("[rollbackToVersion] Rollback failed: {}", e.getMessage());
                return false;
            }
        }
        
        log.warn("[rollbackToVersion] SceneGroupService not available");
        return false;
    }
    
    @Override
    public boolean deleteVersion(String versionId) {
        log.info("[deleteVersion] Deleting version: {}", versionId);
        
        SnapshotVersionDTO version = versions.remove(versionId);
        if (version == null) {
            return false;
        }
        
        String snapshotId = version.getSnapshotId();
        List<String> versionList = snapshotVersions.get(snapshotId);
        if (versionList != null) {
            versionList.remove(versionId);
        }
        
        storage.remove(STORAGE_KEY_VERSIONS, versionId);
        
        return true;
    }
    
    @Override
    public String getLatestVersionId(String snapshotId) {
        List<String> versionList = snapshotVersions.get(snapshotId);
        if (versionList == null || versionList.isEmpty()) {
            return null;
        }
        
        for (String versionId : versionList) {
            SnapshotVersionDTO version = versions.get(versionId);
            if (version != null && version.isLatest()) {
                return versionId;
            }
        }
        
        return versionList.get(0);
    }
    
    @Override
    public int getVersionCount(String snapshotId) {
        List<String> versionList = snapshotVersions.get(snapshotId);
        return versionList != null ? versionList.size() : 0;
    }
    
    private void persistVersion(SnapshotVersionDTO version) {
        try {
            storage.put(STORAGE_KEY_VERSIONS, version.getVersionId(), version);
        } catch (Exception e) {
            log.error("[persistVersion] Failed to persist version: {}", e.getMessage());
        }
    }
}

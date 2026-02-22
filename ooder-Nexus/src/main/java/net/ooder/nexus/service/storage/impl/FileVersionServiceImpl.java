package net.ooder.nexus.service.storage.impl;

import net.ooder.nexus.core.storage.vfs.FileVersion;
import net.ooder.nexus.service.storage.FileVersionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FileVersionServiceImpl implements FileVersionService {

    private static final Logger log = LoggerFactory.getLogger(FileVersionServiceImpl.class);

    private final ConcurrentHashMap<String, List<FileVersion>> fileVersions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, FileVersion> versionStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> currentVersions = new ConcurrentHashMap<>();

    @Override
    public FileVersion createVersion(String fileId, String personId, String changeNote) {
        log.info("Creating version for file: {}, person: {}", fileId, personId);
        
        String versionId = "ver-" + UUID.randomUUID().toString();
        FileVersion version = new SimpleFileVersion(versionId, fileId, personId, changeNote);
        
        List<FileVersion> versions = fileVersions.computeIfAbsent(fileId, k -> new ArrayList<>());
        
        if (!versions.isEmpty()) {
            FileVersion lastVersion = versions.get(versions.size() - 1);
            version.setParentVersionId(lastVersion.getVersionID());
        }
        
        version.setVersionNumber("v" + (versions.size() + 1));
        versions.add(version);
        versionStore.put(versionId, version);
        currentVersions.put(fileId, versionId);
        
        return version;
    }

    @Override
    public FileVersion getVersion(String versionId) {
        return versionStore.get(versionId);
    }

    @Override
    public List<FileVersion> getVersionHistory(String fileId) {
        log.info("Getting version history for file: {}", fileId);
        List<FileVersion> versions = fileVersions.getOrDefault(fileId, new ArrayList<>());
        return versions.stream()
                .sorted(Comparator.comparingLong(FileVersion::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public FileVersion getCurrentVersion(String fileId) {
        String versionId = currentVersions.get(fileId);
        if (versionId != null) {
            return versionStore.get(versionId);
        }
        
        List<FileVersion> versions = fileVersions.get(fileId);
        if (versions != null && !versions.isEmpty()) {
            return versions.get(versions.size() - 1);
        }
        return null;
    }

    @Override
    public boolean setCurrentVersion(String fileId, String versionId) {
        log.info("Setting current version: fileId={}, versionId={}", fileId, versionId);
        
        FileVersion version = versionStore.get(versionId);
        if (version == null || !version.getFileId().equals(fileId)) {
            return false;
        }
        
        currentVersions.put(fileId, versionId);
        return true;
    }

    @Override
    public boolean deleteVersion(String versionId) {
        log.info("Deleting version: {}", versionId);
        
        FileVersion version = versionStore.get(versionId);
        if (version == null) {
            return false;
        }
        
        String fileId = version.getFileId();
        List<FileVersion> versions = fileVersions.get(fileId);
        if (versions != null) {
            versions.removeIf(v -> v.getVersionID().equals(versionId));
        }
        
        versionStore.remove(versionId);
        
        if (versionId.equals(currentVersions.get(fileId))) {
            if (versions != null && !versions.isEmpty()) {
                currentVersions.put(fileId, versions.get(versions.size() - 1).getVersionID());
            } else {
                currentVersions.remove(fileId);
            }
        }
        
        return true;
    }

    @Override
    public boolean archiveVersion(String versionId) {
        FileVersion version = versionStore.get(versionId);
        if (version == null) {
            return false;
        }
        version.archive();
        return true;
    }

    @Override
    public boolean restoreVersion(String versionId) {
        FileVersion version = versionStore.get(versionId);
        if (version == null) {
            return false;
        }
        version.restore();
        return true;
    }

    @Override
    public Map<String, Object> compareVersions(String versionId1, String versionId2) {
        log.info("Comparing versions: {} vs {}", versionId1, versionId2);
        
        FileVersion v1 = versionStore.get(versionId1);
        FileVersion v2 = versionStore.get(versionId2);
        
        if (v1 == null || v2 == null) {
            return null;
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("version1", v1.toMap());
        result.put("version2", v2.toMap());
        result.put("sizeDiff", (v2.getSize() != null ? v2.getSize() : 0) - (v1.getSize() != null ? v1.getSize() : 0));
        result.put("timeDiff", (v2.getCreateTime() != null ? v2.getCreateTime() : 0L) - (v1.getCreateTime() != null ? v1.getCreateTime() : 0L));
        result.put("sameHash", v1.getHash() != null && v1.getHash().equals(v2.getHash()));
        
        return result;
    }

    @Override
    public List<FileVersion> getVersionsByPerson(String personId) {
        log.info("Getting versions by person: {}", personId);
        return versionStore.values().stream()
                .filter(v -> personId.equals(v.getPersonId()))
                .sorted(Comparator.comparingLong(FileVersion::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<FileVersion> getVersionsByTimeRange(String fileId, long startTime, long endTime) {
        log.info("Getting versions by time range: fileId={}, start={}, end={}", fileId, startTime, endTime);
        List<FileVersion> versions = fileVersions.getOrDefault(fileId, new ArrayList<>());
        return versions.stream()
                .filter(v -> v.getCreateTime() >= startTime && v.getCreateTime() <= endTime)
                .sorted(Comparator.comparingLong(FileVersion::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public long getVersionCount(String fileId) {
        List<FileVersion> versions = fileVersions.get(fileId);
        return versions != null ? versions.size() : 0;
    }

    @Override
    public boolean hasVersions(String fileId) {
        List<FileVersion> versions = fileVersions.get(fileId);
        return versions != null && !versions.isEmpty();
    }

    @Override
    public Map<String, Object> getVersionStatistics(String fileId) {
        log.info("Getting version statistics for file: {}", fileId);
        
        Map<String, Object> stats = new HashMap<>();
        List<FileVersion> versions = fileVersions.getOrDefault(fileId, new ArrayList<>());
        
        stats.put("totalVersions", versions.size());
        stats.put("activeVersions", versions.stream().filter(FileVersion::isActive).count());
        stats.put("archivedVersions", versions.stream().filter(FileVersion::isArchived).count());
        
        if (!versions.isEmpty()) {
            long totalSize = versions.stream()
                    .mapToLong(v -> v.getSize() != null ? v.getSize() : 0)
                    .sum();
            stats.put("totalSize", totalSize);
            stats.put("avgSize", totalSize / versions.size());
            
            FileVersion latest = versions.get(versions.size() - 1);
            FileVersion oldest = versions.get(0);
            stats.put("latestVersion", latest.toMap());
            stats.put("oldestVersion", oldest.toMap());
            stats.put("firstCreateTime", oldest.getCreateTime());
            stats.put("lastCreateTime", latest.getCreateTime());
        }
        
        return stats;
    }
    
    private static class SimpleFileVersion implements FileVersion {
        private String versionID;
        private String fileId;
        private String personId;
        private String changeNote;
        private String versionNumber;
        private String parentVersionId;
        private Long createTime;
        private Long updateTime;
        private Long size;
        private String status;
        private String hash;
        private String description;
        private String mimeType;
        private List<String> tags;
        private Map<String, Object> metadata;
        
        public SimpleFileVersion(String versionID, String fileId, String personId, String changeNote) {
            this.versionID = versionID;
            this.fileId = fileId;
            this.personId = personId;
            this.changeNote = changeNote;
            this.createTime = System.currentTimeMillis();
            this.updateTime = System.currentTimeMillis();
            this.status = "active";
            this.tags = new ArrayList<>();
            this.metadata = new HashMap<>();
        }
        
        @Override public String getVersionID() { return versionID; }
        @Override public String getFileId() { return fileId; }
        @Override public String getFileObjectId() { return null; }
        @Override public String getSourceId() { return null; }
        @Override public Long getCreateTime() { return createTime; }
        @Override public Long getUpdateTime() { return updateTime; }
        @Override public String getPersonId() { return personId; }
        @Override public String getHash() { return hash; }
        @Override public net.ooder.nexus.core.storage.vfs.FileObject getFileObject() { return null; }
        @Override public void setFileId(String fileId) { this.fileId = fileId; }
        @Override public void setCreateTime(Long createTime) { this.createTime = createTime; }
        @Override public void setPersonId(String personId) { this.personId = personId; }
        @Override public void setFileObjectId(String fileObjectId) { }
        @Override public void setSourceId(String sourceId) { }
        @Override public String getVersionNumber() { return versionNumber; }
        @Override public void setVersionNumber(String versionNumber) { this.versionNumber = versionNumber; }
        @Override public String getDescription() { return description; }
        @Override public void setDescription(String description) { this.description = description; }
        @Override public String getChangeNote() { return changeNote; }
        @Override public void setChangeNote(String changeNote) { this.changeNote = changeNote; }
        @Override public Long getSize() { return size; }
        @Override public void setSize(Long size) { this.size = size; }
        @Override public String getStatus() { return status; }
        @Override public void setStatus(String status) { this.status = status; }
        @Override public String getParentVersionId() { return parentVersionId; }
        @Override public void setParentVersionId(String parentVersionId) { this.parentVersionId = parentVersionId; }
        @Override public String getMimeType() { return mimeType; }
        @Override public void setMimeType(String mimeType) { this.mimeType = mimeType; }
        @Override public List<String> getTags() { return tags; }
        @Override public void setTags(List<String> tags) { this.tags = tags; }
        @Override public Map<String, Object> getMetadata() { return metadata; }
        @Override public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        @Override public boolean isActive() { return "active".equals(status); }
        @Override public boolean isArchived() { return "archived".equals(status); }
        @Override public void archive() { this.status = "archived"; this.updateTime = System.currentTimeMillis(); }
        @Override public void restore() { this.status = "active"; this.updateTime = System.currentTimeMillis(); }
        
        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("versionID", versionID);
            map.put("fileId", fileId);
            map.put("versionNumber", versionNumber);
            map.put("description", description);
            map.put("changeNote", changeNote);
            map.put("size", size);
            map.put("status", status);
            map.put("createTime", createTime);
            map.put("updateTime", updateTime);
            map.put("personId", personId);
            map.put("hash", hash);
            map.put("mimeType", mimeType);
            map.put("tags", tags);
            map.put("metadata", metadata);
            return map;
        }
    }
}

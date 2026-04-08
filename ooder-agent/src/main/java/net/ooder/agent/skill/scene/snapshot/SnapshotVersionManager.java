package net.ooder.mvp.skill.scene.snapshot;

import java.util.List;
import java.util.Map;

public interface SnapshotVersionManager {
    
    String createVersion(String snapshotId, String versionName);
    
    SnapshotVersionDTO getVersion(String versionId);
    
    List<SnapshotVersionDTO> getVersionHistory(String snapshotId);
    
    SnapshotDiffDTO compareVersions(String versionId1, String versionId2);
    
    boolean rollbackToVersion(String sceneGroupId, String versionId);
    
    boolean deleteVersion(String versionId);
    
    String getLatestVersionId(String snapshotId);
    
    int getVersionCount(String snapshotId);
}

package net.ooder.nexus.service.storage;

import net.ooder.nexus.core.storage.vfs.FileVersion;

import java.util.List;
import java.util.Map;

/**
 * 文件版本管理服务接口
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface FileVersionService {
    
    FileVersion createVersion(String fileId, String personId, String changeNote);
    
    FileVersion getVersion(String versionId);
    
    List<FileVersion> getVersionHistory(String fileId);
    
    FileVersion getCurrentVersion(String fileId);
    
    boolean setCurrentVersion(String fileId, String versionId);
    
    boolean deleteVersion(String versionId);
    
    boolean archiveVersion(String versionId);
    
    boolean restoreVersion(String versionId);
    
    Map<String, Object> compareVersions(String versionId1, String versionId2);
    
    List<FileVersion> getVersionsByPerson(String personId);
    
    List<FileVersion> getVersionsByTimeRange(String fileId, long startTime, long endTime);
    
    long getVersionCount(String fileId);
    
    boolean hasVersions(String fileId);
    
    Map<String, Object> getVersionStatistics(String fileId);
}

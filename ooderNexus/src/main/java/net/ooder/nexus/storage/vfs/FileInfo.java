package net.ooder.nexus.storage.vfs;

import java.util.List;
import java.util.Set;

public interface FileInfo {
    String getID();
    String getName();
    String getPath();
    String getPersonId();
    Integer getFileType();
    Long getCreateTime();
    String getDescription();
    String getOldFolderId();
    String getFolderId();
    String getRight();
    String getCurrentVersionId();
    String getCurrentVersionFileHash();
    int getCachedSize();
    Set<String> getVersionIds();
    FileVersion getCurrentVersion();
    List<FileVersion> getVersionList();
    List<FileView> getCurrentViews();
    List<FileLink> getLinks();
    Folder getFolder();
    Object getCurrentVersionInputStream();
    Set<String> getCurrentViewIds();
    Set<String> getLinkIds();
    void setOldFolderId(String oldFolderId);
}

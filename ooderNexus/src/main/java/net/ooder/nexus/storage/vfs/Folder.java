package net.ooder.nexus.storage.vfs;

import java.util.List;
import java.util.Set;

public interface Folder {
    String getID();
    String getName();
    String getPath();
    String getPersonId();
    String getDescription();
    String getParentId();
    String getOldFolderId();
    String getFolderType();
    Long getCreateTime();
    Long getUpdateTime();
    Set<String> getChildrenIdList();
    Set<String> getFileIdList();
    List<Folder> getChildrenList();
    List<FileInfo> getFileList();
    Folder createChildFolder(String name, String personId);
    FileInfo createFile(String name, String personId);
    void setName(String name);
    void setDescription(String description);
    void setFolderType(String folderType);
    void setState(String state);
}

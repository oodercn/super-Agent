package net.ooder.nexus.storage.vfs;

import java.io.File;
import java.util.*;

public class LocalFile implements FileInfo {
    private String ID;
    private String name;
    private String path = "";
    private Integer fileType;
    private String personId;
    private Long createTime;
    private String description;
    private String folderId;
    private String right;
    private String oldFolderId;
    private String currentVersionId;
    private String currentVersionFileHash;
    private Set<String> fileIdVersionList;
    private Set<String> fileIdLinkList;
    private Set<String> currViewIdList;
    private Set<String> roleIds;
    public Long updateTime;
    boolean isModified = false;

    public LocalFile(File file) {
        this.ID = file.getAbsolutePath();
        this.ID = this.ID.replace("\\", "/");
        this.name = file.getName();
        this.folderId = file.getParentFile().getAbsolutePath().replace("\\", "/");
        this.personId = "system";
        this.createTime = file.lastModified();
        this.updateTime = file.lastModified();
        this.description = file.getName();
        this.fileType = 1;
        this.fileIdLinkList = new HashSet<>();
        this.fileIdVersionList = new HashSet<>();
        this.currentVersionId = ID;
        this.path = file.getPath().replace("\\", "/");
        if (path == null) {
            path = getFolder().getPath().replace("\\", "/") + "/" + name;
        }
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int getCachedSize() {
        return 0;
    }

    @Override
    public String getID() {
        return this.ID;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getPersonId() {
        return this.personId;
    }

    @Override
    public Integer getFileType() {
        return this.fileType;
    }

    @Override
    public Long getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getDescription() {
        return this.description == null ? this.name : this.description;
    }

    @Override
    public String getOldFolderId() {
        return this.oldFolderId;
    }

    public String getRight() {
        return right;
    }

    @Override
    public String getFolderId() {
        return this.folderId;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public void setCurrentVersionId(String currentVersionId) {
        this.currentVersionId = currentVersionId;
    }

    public void setCurrentVersionFileHash(String currentVersionFileHash) {
        this.currentVersionFileHash = currentVersionFileHash;
    }

    public Set<String> getFileIdVersionList() {
        return fileIdVersionList;
    }

    public void setFileIdVersionList(Set<String> fileIdVersionList) {
        this.fileIdVersionList = fileIdVersionList;
    }

    public Set<String> getFileIdLinkList() {
        return fileIdLinkList;
    }

    public void setFileIdLinkList(Set<String> fileIdLinkList) {
        this.fileIdLinkList = fileIdLinkList;
    }

    public Set<String> getCurrViewIdList() {
        return currViewIdList;
    }

    public void setCurrViewIdList(Set<String> currViewIdList) {
        this.currViewIdList = currViewIdList;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    @Override
    public Set<String> getVersionIds() {
        return this.fileIdVersionList;
    }

    @Override
    public FileVersion getCurrentVersion() {
        return null;
    }

    public synchronized FileVersion createFileVersion() {
        LocalVFSManager cacheManager = LocalVFSManager.getInstance();
        LocalFileVersion version = cacheManager.createFileVersion(this);
        version.setFileId(this.ID);
        version.setCreateTime(System.currentTimeMillis());
        return version;
    }

    @Override
    public List<FileVersion> getVersionList() {
        return new ArrayList<>();
    }

    @Override
    public List<FileView> getCurrentViews() {
        return new ArrayList<>();
    }

    @Override
    public List<FileLink> getLinks() {
        return new ArrayList<>();
    }

    @Override
    public Folder getFolder() {
        return LocalVFSManager.getInstance().getFolderByID(this.getFolderId());
    }

    @Override
    public Object getCurrentVersionInputStream() {
        return null;
    }

    @Override
    public Set<String> getCurrentViewIds() {
        return this.currViewIdList;
    }

    @Override
    public Set<String> getLinkIds() {
        return this.fileIdLinkList;
    }

    @Override
    public void setOldFolderId(String oldFolderId) {
        this.oldFolderId = oldFolderId;
    }

    @Override
    public String getCurrentVersionId() {
        return this.currentVersionId;
    }

    @Override
    public String getCurrentVersionFileHash() {
        return this.currentVersionFileHash;
    }

    @Override
    public String toString() {
        return path;
    }
}

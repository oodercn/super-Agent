package net.ooder.web.vfs;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.common.util.StringUtility;
import net.ooder.server.JDSServer;
import net.ooder.vfs.*;
import net.ooder.vfs.ct.CtVfsFactory;

import java.io.File;
import java.util.*;

public class LocalFile implements FileInfo {
    private String ID;
    private String name;
    private String path = "";

    private Integer fileType;
    private String personId;
    private Long createTime;
    private String descrition;
    private String folderId;

    private String right;
    private String oldFolderId;
    private String currentVersonId;

    private String currentVersonFileHash;

    private Set<String> fileIdVersionList;
    private Set<String> fileIdLinkList;
    private Set<String> currViewIdList;
    private Set<String> roleIds;


    public Long updateTime;
    boolean isModified = false;

    public LocalFile(File file) {
        this.ID = file.getAbsolutePath();
        this.ID = StringUtility.replace(this.ID, "\\", "/");
        this.name = file.getName();
        this.folderId = StringUtility.replace(file.getParentFile().getAbsolutePath(), "\\", "/");
        try {
            this.personId = JDSServer.getInstance().getAdminUser().getId();
        } catch (JDSException e) {
            e.printStackTrace();
        }

        this.createTime = file.lastModified();
        this.updateTime = file.lastModified();
        this.descrition = file.getName();
        this.fileType = 1;
        this.fileIdLinkList = new HashSet<>();
        this.fileIdVersionList = new HashSet<>();
        this.currentVersonId = ID;
        this.path = StringUtility.replace(file.getPath(), "\\", "/");
        if (path == null) {
            path = StringUtility.replace(getFolder().getPath(), "\\", "/") + "/" + name;
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

        return this.descrition == null ? this.name : this.descrition;
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

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public void setCurrentVersonId(String currentVersonId) {
        this.currentVersonId = currentVersonId;
    }

    public void setCurrentVersonFileHash(String currentVersonFileHash) {
        this.currentVersonFileHash = currentVersonFileHash;
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
    @JSONField(serialize = false)
    public FileVersion getCurrentVersion() {
        try {
            return CtVfsFactory.getCtVfsService().getFileVersionById(this.getCurrentVersonId());
        } catch (JDSException e) {
            e.printStackTrace();
        }
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
    @JSONField(serialize = false)
    public List<FileVersion> getVersionList() {
        List<FileVersion> versions = new ArrayList<FileVersion>();
        try {
            versions = CtVfsFactory.getCtVfsService().loadVersionByIds(this.getVersionIds());
        } catch (JDSException e) {

            e.printStackTrace();
        }
        Collections.sort(versions, new Comparator<FileVersion>() {
            public int compare(FileVersion o1, FileVersion o2) {
                return o2.getIndex() - o1.getIndex();
            }
        });

        return versions;

    }

    @Override
    @JSONField(serialize = false)
    public List<FileView> getCurrentViews() {
        List<FileView> versions = new ArrayList<FileView>();
        try {
            Set<String> viewIds = this.getCurrentViewIds();
            for (String viewId : viewIds) {
                versions.add(CtVfsFactory.getCtVfsService().getFileViewById(viewId));
            }
        } catch (JDSException e) {

            e.printStackTrace();
        }
        return versions;
    }

    @Override
    @JSONField(serialize = false)
    public List<FileLink> getLinks() {
        List<FileLink> links = new ArrayList<FileLink>();
        try {
            Set<String> linkIds = this.getLinkIds();
            for (String linkId : linkIds) {
                links.add(CtVfsFactory.getCtVfsService().getFileLinkById(linkId));
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return links;
    }

    @Override

    @JSONField(serialize = false)
    public Folder getFolder() {

        try {
            return CtVfsFactory.getCtVfsService().getFolderById(this.getFolderId());
        } catch (JDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @JSONField(serialize = false)
    public MD5InputStream getCurrentVersonInputStream() {
        try {
            return CtVfsFactory.getCtVfsService().downLoad(this.getPath());
        } catch (JDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
    public String getCurrentVersonId() {
        return this.currentVersonId;
    }

    @Override
    public String getCurrentVersonFileHash() {
        return this.currentVersonFileHash;
    }

    @Override
    public String toString() {
        return path;
    }

    ;

}

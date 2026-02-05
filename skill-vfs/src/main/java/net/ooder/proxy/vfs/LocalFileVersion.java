package net.ooder.proxy.vfs;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.common.md5.MD5OutputStream;
import net.ooder.common.util.StringUtility;
import net.ooder.vfs.FileObject;
import net.ooder.vfs.FileVersion;
import net.ooder.vfs.FileView;
import net.ooder.vfs.ct.CtVfsFactory;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class LocalFileVersion implements FileVersion {

    private String fileId;
    private String versionID;
    private String versionName;
    private int index; // 顺序
    private String personId;
    private String path;
    private String fileObjectId;
    private String sourceId;
    private long createTime;

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long updateTime;

    private Set<String> fileIdViewList = new LinkedHashSet<String>();
    private String fileName;
    private Long length;

    public LocalFileVersion(File versionFile, String hash) {
        fileId =  StringUtility.replace(versionFile.getAbsolutePath(), "\\", "/");
        versionID =  StringUtility.replace(versionFile.getAbsolutePath(), "\\", "/");
        versionName = versionFile.getName();
        fileName = versionName;
        length = versionFile.length();
        path = versionFile.getPath();
        if (hash == null) {
            if (versionFile.exists()) {
                try {
                    hash = DigestUtils.md5Hex(new FileInputStream(versionFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        fileObjectId = hash;
        createTime = versionFile.lastModified();
        fileIdViewList = new HashSet<>();

    }




    @Override
    public String getVersionID() {
        return versionID;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public String getFileId() {
        return fileId;
    }

    @Override
    public String getVersionName() {

        return versionName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public Integer getIndex() {
        return index;
    }

    @Override
    public Long getLength() {
        Long length = 0L;
        if (this.getFileObject() == null || this.getFileObject().getPath() == null) {
            return 0L;
        }
        return this.getFileObject().getLength();
    }

    @Override

    @JSONField(serialize = false)
    public FileObject getFileObject() {
        try {
            return CtVfsFactory.getCtVfsService().getFileObjectById(this.getFileObjectId());
        } catch (JDSException e) {

            e.printStackTrace();
        }
        return null;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Set<String> getFileIdViewList() {
        return fileIdViewList;
    }

    public void setFileIdViewList(Set<String> fileIdViewList) {
        this.fileIdViewList = fileIdViewList;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getFileObjectId() {

        return this.fileObjectId;
    }

    @Override
    public void setFileObjectId(String objectId) {
        this.fileObjectId = objectId;
    }

    @Override
    public String getPersonId() {

        return this.personId;
    }

    @Override

    @JSONField(serialize = false)
    public List<FileView> getViews() {
        List<FileView> views = new ArrayList<FileView>();
        try {
            views = CtVfsFactory.getCtVfsService().loadViews(this.getViewIds());
        } catch (JDSException e) {
            e.printStackTrace();
        }

        Collections.sort(views, new Comparator<FileView>() {
            public int compare(FileView o1, FileView o2) {
                return o1.getFileIndex() - o2.getFileIndex();
            }
        });

        return views;
    }

    @Override
    public Long getCreateTime() {

        return this.createTime;
    }

    @Override
    public Set<String> getViewIds() {

        return this.fileIdViewList;
    }

    @Override
    public Integer writeLine(String str) {

        try {
            return CtVfsFactory.getCtVfsService().writeLine(this.getFileId(), str);
        } catch (JDSException e) {

            e.printStackTrace();
        }
        return -1;
    }


    @Override
    @JSONField(serialize = false)
    public FileView createView(String fileObjectId, Integer fileIndex) {
        try {
            FileView fileView = CtVfsFactory.getCtVfsService().createView(this.getVersionID(), fileObjectId, fileIndex);
            getViewIds().add(fileView.getID());
        } catch (JDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override

    @JSONField(serialize = false)
    public MD5InputStream getInputStream() {

        try {
            return CtVfsFactory.getCtVfsService().getInputStreamByVersionid(this.getVersionID());
        } catch (JDSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override

    @JSONField(serialize = false)
    public MD5OutputStream getOutputStream() {

        return null;
    }

}

package net.ooder.tools;

import net.ooder.org.PersonNotFoundException;
import net.ooder.server.OrgManagerFactory;
import net.ooder.vfs.FileVersion;

public class OODFileVersion {

    String versionID;
    String fileId;
    String name;
    String personName;
    String fileName;
    String hash;
    String path;
    Integer index;
    Long createTime;
    Long length;


    public OODFileVersion(FileVersion version) {
        this.name = version.getVersionName();
        this.fileId = version.getFileId();

        this.createTime = version.getCreateTime();
        try {
            if (version.getPersonId() != null) {
                this.personName = OrgManagerFactory.getOrgManager().getPersonByID(version.getPersonId()).getName();
            }

        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        this.versionID = version.getVersionID();

        this.fileName = version.getFileName();
        if (version.getFileObject() != null) {
            this.hash = version.getFileObject().getHash();
            this.length = version.getFileObject().getLength();
        }

        this.index = version.getIndex();

        this.path = version.getPath();

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
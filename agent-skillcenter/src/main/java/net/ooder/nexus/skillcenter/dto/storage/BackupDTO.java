package net.ooder.nexus.skillcenter.dto.storage;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class BackupDTO extends BaseDTO {

    private String name;
    private String path;
    private long size;
    private String sizeHuman;
    private long lastModified;

    public BackupDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSizeHuman() {
        return sizeHuman;
    }

    public void setSizeHuman(String sizeHuman) {
        this.sizeHuman = sizeHuman;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}

package net.ooder.nexus.storage.vfs;

import java.io.File;

public class LocalFileObject implements FileObject {
    private String ID;
    private String path;
    private String hash;
    private Long length;

    public LocalFileObject(File file, String hash) {
        this.ID = file.getAbsolutePath();
        this.ID = this.ID.replace("\\", "/");
        this.path = file.getAbsolutePath().replace("\\", "/");
        this.hash = hash;
        this.length = file.length();
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public Long getLength() {
        return length;
    }

    @Override
    public void setID(String id) {
        this.ID = id;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public void setLength(Long length) {
        this.length = length;
    }
}

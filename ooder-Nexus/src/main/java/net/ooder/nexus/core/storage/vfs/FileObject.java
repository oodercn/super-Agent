package net.ooder.nexus.core.storage.vfs;

public interface FileObject {
    String getID();
    String getPath();
    String getHash();
    Long getLength();
    void setID(String id);
    void setPath(String path);
    void setHash(String hash);
    void setLength(Long length);
}

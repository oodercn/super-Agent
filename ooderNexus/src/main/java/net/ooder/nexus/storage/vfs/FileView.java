package net.ooder.nexus.storage.vfs;

public interface FileView {
    String getViewID();
    String getFileId();
    String getPersonId();
    Long getCreateTime();
    String getDescription();
}

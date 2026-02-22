package net.ooder.nexus.core.storage.vfs;

public interface FileView {
    String getViewID();
    String getFileId();
    String getPersonId();
    Long getCreateTime();
    String getDescription();
}

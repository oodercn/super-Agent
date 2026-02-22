package net.ooder.nexus.core.storage.vfs;

public interface FileLink {
    String getLinkID();
    String getFileId();
    String getLinkedFileId();
    String getPersonId();
    Long getCreateTime();
    String getDescription();
}

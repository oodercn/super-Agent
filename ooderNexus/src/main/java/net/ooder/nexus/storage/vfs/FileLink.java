package net.ooder.nexus.storage.vfs;

public interface FileLink {
    String getLinkID();
    String getFileId();
    String getLinkedFileId();
    String getPersonId();
    Long getCreateTime();
    String getDescription();
}

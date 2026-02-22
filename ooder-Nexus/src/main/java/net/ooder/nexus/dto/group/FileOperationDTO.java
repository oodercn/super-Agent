package net.ooder.nexus.dto.group;

import java.io.Serializable;

/**
 * 文件操作 DTO（删除、下载等）
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class FileOperationDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String fileId;
    private String groupId;
    private String operatorId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}

package net.ooder.nexus.dto.group;

import java.io.Serializable;

/**
 * 文件列表查询 DTO
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class FileListDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String groupId;
    private String fileType;
    private String uploaderId;
    private Integer page;
    private Integer pageSize;
    private String sortBy;
    private String sortOrder;

    public FileListDTO() {
        this.page = 1;
        this.pageSize = 20;
        this.sortBy = "uploadTime";
        this.sortOrder = "desc";
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}

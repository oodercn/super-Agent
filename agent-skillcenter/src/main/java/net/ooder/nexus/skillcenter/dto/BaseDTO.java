package net.ooder.nexus.skillcenter.dto;

import java.util.Date;

/**
 * 基础DTO类 - 符合 ooderNexus 规范
 * 包含所有DTO共有的字段
 */
public class BaseDTO {

    private String id; // 唯一标识
    private Date createdAt; // 创建时间
    private Date updatedAt; // 更新时间
    private String createdBy; // 创建人
    private String updatedBy; // 更新人
    private boolean deleted; // 是否删除

    // 构造方法
    public BaseDTO() {}

    public BaseDTO(String id) {
        this.id = id;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
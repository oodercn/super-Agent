package net.ooder.nexus.domain.group.model;

import java.io.Serializable;

/**
 * 扩展群组模型 - 支持组织机构关联
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class GroupExt implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String orgId;
    private String departmentId;
    private String groupType;
    private int maxMembers;
    private int memberCount;
    private String ownerId;
    private String ownerName;
    private String status;
    private String inviteCode;
    private long createdAt;
    private long updatedAt;

    public GroupExt() {
        this.status = "active";
        this.groupType = "personal";
        this.maxMembers = 100;
        this.memberCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "GroupExt{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", groupType='" + groupType + '\'' +
                ", orgId='" + orgId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", memberCount=" + memberCount +
                ", status='" + status + '\'' +
                '}';
    }
}

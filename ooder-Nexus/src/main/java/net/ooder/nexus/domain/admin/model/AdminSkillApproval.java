package net.ooder.nexus.domain.admin.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "admin_skill_approval")
public class AdminSkillApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String skillId;

    private String skillName;
    private String version;
    private String approvalStatus = "PENDING";
    private String publishedBy;
    private Long publishedTime;
    private String approvedBy;
    private Long approvedTime;
    private String rejectedBy;
    private Long rejectedTime;
    private String rejectReason;
    private Long createTime;
    private Long updateTime;

    public AdminSkillApproval() {
    }

    public AdminSkillApproval(String skillId, String skillName) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.createTime = System.currentTimeMillis();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getPublishedBy() { return publishedBy; }
    public void setPublishedBy(String publishedBy) { this.publishedBy = publishedBy; }
    public Long getPublishedTime() { return publishedTime; }
    public void setPublishedTime(Long publishedTime) { this.publishedTime = publishedTime; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public Long getApprovedTime() { return approvedTime; }
    public void setApprovedTime(Long approvedTime) { this.approvedTime = approvedTime; }
    public String getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(String rejectedBy) { this.rejectedBy = rejectedBy; }
    public Long getRejectedTime() { return rejectedTime; }
    public void setRejectedTime(Long rejectedTime) { this.rejectedTime = rejectedTime; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}

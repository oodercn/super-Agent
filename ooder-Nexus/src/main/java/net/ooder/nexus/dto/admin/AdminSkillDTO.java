package net.ooder.nexus.dto.admin;

import java.io.Serializable;

public class AdminSkillDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String version;
    private String status;
    private String installMode;
    private String approvalStatus;
    private Long createTime;
    private Long updateTime;

    public AdminSkillDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getInstallMode() { return installMode; }
    public void setInstallMode(String installMode) { this.installMode = installMode; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}

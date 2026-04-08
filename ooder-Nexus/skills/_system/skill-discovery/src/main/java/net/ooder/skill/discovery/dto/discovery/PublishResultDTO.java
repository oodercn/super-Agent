package net.ooder.skill.discovery.dto.discovery;

import java.util.Date;

public class PublishResultDTO {
    
    private String capabilityId;
    private String publishUrl;
    private String status;
    private String source;
    private Date publishTime;
    private String commitId;
    private String branch;
    private String message;
    
    public PublishResultDTO() {
    }
    
    public String getCapabilityId() {
        return capabilityId;
    }
    
    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }
    
    public String getPublishUrl() {
        return publishUrl;
    }
    
    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public Date getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    
    public String getCommitId() {
        return commitId;
    }
    
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}

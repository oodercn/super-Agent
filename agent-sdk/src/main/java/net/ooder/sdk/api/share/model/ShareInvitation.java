package net.ooder.sdk.api.share.model;

public class ShareInvitation {
    
    private String invitationId;
    private String shareId;
    private String skillId;
    private String skillName;
    private String version;
    private String senderId;
    private String senderName;
    private String targetId;
    private long createdAt;
    private long expiresAt;
    private ShareInvitationStatus status;
    private long packageSize;
    
    public String getInvitationId() { return invitationId; }
    public void setInvitationId(String invitationId) { this.invitationId = invitationId; }
    
    public String getShareId() { return shareId; }
    public void setShareId(String shareId) { this.shareId = shareId; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    
    public ShareInvitationStatus getStatus() { return status; }
    public void setStatus(ShareInvitationStatus status) { this.status = status; }
    
    public long getPackageSize() { return packageSize; }
    public void setPackageSize(long packageSize) { this.packageSize = packageSize; }
}

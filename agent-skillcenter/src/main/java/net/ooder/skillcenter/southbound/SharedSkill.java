package net.ooder.skillcenter.southbound;

public class SharedSkill {
    private String shareId;
    private String skillId;
    private String skillName;
    private String fromPeerId;
    private String fromPeerName;
    private long sharedAt;
    private long expiresAt;
    private String status;

    public String getShareId() { return shareId; }
    public void setShareId(String shareId) { this.shareId = shareId; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getFromPeerId() { return fromPeerId; }
    public void setFromPeerId(String fromPeerId) { this.fromPeerId = fromPeerId; }

    public String getFromPeerName() { return fromPeerName; }
    public void setFromPeerName(String fromPeerName) { this.fromPeerName = fromPeerName; }

    public long getSharedAt() { return sharedAt; }
    public void setSharedAt(long sharedAt) { this.sharedAt = sharedAt; }

    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

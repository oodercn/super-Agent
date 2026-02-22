package net.ooder.skillcenter.southbound.message;

public class ShareInvitationMessage {
    private String messageId;
    private String fromPeerId;
    private String toPeerId;
    private String skillId;
    private String skillName;
    private String skillVersion;
    private long packageSize;
    private String signature;
    private long createdAt;
    private long expiresAt;

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getFromPeerId() { return fromPeerId; }
    public void setFromPeerId(String fromPeerId) { this.fromPeerId = fromPeerId; }

    public String getToPeerId() { return toPeerId; }
    public void setToPeerId(String toPeerId) { this.toPeerId = toPeerId; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getSkillVersion() { return skillVersion; }
    public void setSkillVersion(String skillVersion) { this.skillVersion = skillVersion; }

    public long getPackageSize() { return packageSize; }
    public void setPackageSize(long packageSize) { this.packageSize = packageSize; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
}

package net.ooder.skillcenter.southbound.message;

public class InstallResultMessage {
    private String messageId;
    private String shareId;
    private boolean success;
    private String skillId;
    private String errorMessage;

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getShareId() { return shareId; }
    public void setShareId(String shareId) { this.shareId = shareId; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

package net.ooder.skillcenter.southbound.message;

public class ShareResponseMessage {
    private String messageId;
    private String invitationId;
    private boolean accepted;
    private int transferPort;
    private String errorMessage;

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getInvitationId() { return invitationId; }
    public void setInvitationId(String invitationId) { this.invitationId = invitationId; }

    public boolean isAccepted() { return accepted; }
    public void setAccepted(boolean accepted) { this.accepted = accepted; }

    public int getTransferPort() { return transferPort; }
    public void setTransferPort(int transferPort) { this.transferPort = transferPort; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

package net.ooder.nexus.domain.group.model;

import java.io.Serializable;

/**
 * 群组消息模型
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class GroupMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String messageId;
    private String groupId;
    private String senderId;
    private String senderName;
    private String senderAvatar;
    private String msgType;
    private String content;
    private String filePath;
    private String fileName;
    private long fileSize;
    private String thumbnail;
    private long sendTime;
    private String status;
    private String replyTo;
    private String replyToContent;

    public GroupMessage() {
        this.msgType = "text";
        this.status = "sent";
        this.sendTime = System.currentTimeMillis();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyToContent() {
        return replyToContent;
    }

    public void setReplyToContent(String replyToContent) {
        this.replyToContent = replyToContent;
    }

    @Override
    public String toString() {
        return "GroupMessage{" +
                "messageId='" + messageId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", msgType='" + msgType + '\'' +
                ", sendTime=" + sendTime +
                ", status='" + status + '\'' +
                '}';
    }
}

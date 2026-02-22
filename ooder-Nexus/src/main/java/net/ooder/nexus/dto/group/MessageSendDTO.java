package net.ooder.nexus.dto.group;

import java.io.Serializable;

/**
 * 发送消息 DTO
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class MessageSendDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String groupId;
    private String senderId;
    private String senderName;
    private String msgType;
    private String content;
    private String filePath;
    private String fileName;
    private long fileSize;
    private String replyTo;

    public MessageSendDTO() {
        this.msgType = "text";
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

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}

package net.ooder.skill.chat.model;

import java.util.Date;

public class ChatMessage {

    private String messageId;
    private String sessionId;
    private MessageRole role;
    private String content;
    private Date createdAt;

    public enum MessageRole {
        USER,
        ASSISTANT,
        SYSTEM
    }

    public ChatMessage() {
        this.createdAt = new Date();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MessageRole getRole() {
        return role;
    }

    public void setRole(MessageRole role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

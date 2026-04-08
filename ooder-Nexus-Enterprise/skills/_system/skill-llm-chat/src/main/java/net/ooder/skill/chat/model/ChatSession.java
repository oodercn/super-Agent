package net.ooder.skill.chat.model;

import java.util.Date;
import java.util.List;

public class ChatSession {

    private String sessionId;
    private String title;
    private String userId;
    private SessionStatus status;
    private List<ChatMessage> messages;
    private Date createdAt;
    private Date updatedAt;

    public enum SessionStatus {
        ACTIVE,
        ARCHIVED,
        DELETED
    }

    public ChatSession() {
        this.status = SessionStatus.ACTIVE;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

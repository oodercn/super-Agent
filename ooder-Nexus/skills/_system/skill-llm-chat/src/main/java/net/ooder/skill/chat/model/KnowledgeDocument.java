package net.ooder.skill.chat.model;

import java.util.Date;

public class KnowledgeDocument {

    private String docId;
    private String title;
    private String content;
    private String type;
    private String userId;
    private DocumentStatus status;
    private int chunkCount;
    private Date createdAt;
    private Date updatedAt;

    public enum DocumentStatus {
        PROCESSING,
        READY,
        ERROR
    }

    public KnowledgeDocument() {
        this.status = DocumentStatus.PROCESSING;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public int getChunkCount() {
        return chunkCount;
    }

    public void setChunkCount(int chunkCount) {
        this.chunkCount = chunkCount;
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

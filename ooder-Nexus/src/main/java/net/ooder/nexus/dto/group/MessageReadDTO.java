package net.ooder.nexus.dto.group;

import java.io.Serializable;

/**
 * 标记消息已读 DTO
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class MessageReadDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String groupId;
    private String userId;
    private String lastReadMessageId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(String lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}

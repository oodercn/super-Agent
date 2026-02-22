package net.ooder.nexus.dto.group;

import java.io.Serializable;

/**
 * 消息列表查询 DTO
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class MessageListDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String groupId;
    private Long lastMessageId;
    private Long beforeTime;
    private Long afterTime;
    private Integer limit;
    private String msgType;

    public MessageListDTO() {
        this.limit = 50;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public Long getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(Long beforeTime) {
        this.beforeTime = beforeTime;
    }

    public Long getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(Long afterTime) {
        this.afterTime = afterTime;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}

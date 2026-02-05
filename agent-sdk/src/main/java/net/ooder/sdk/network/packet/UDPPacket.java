package net.ooder.sdk.network.packet;

import net.ooder.common.util.DateUtility;
import com.alibaba.fastjson.JSON;

public abstract class UDPPacket {
    private String version;
    private String messageId;
    protected String timestamp;
    private String senderId;
    private String receiverId;

    public UDPPacket() {
        this.version = "0.6.5";
        this.messageId = java.util.UUID.randomUUID().toString();
        this.timestamp = DateUtility.getCurrentTime();
    }
    
    public String toJson() {
        return JSON.toJSONString(this);
    }
    
    public static UDPPacket fromJson(String json) {
        return JSON.parseObject(json, UDPPacket.class);
    }
    
    // Getter and Setter methods
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    
    public abstract String getType();
}

package net.ooder.sdk.api.msg;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MsgInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String msgId;
    private String fromPersonId;
    private String toPersonId;
    private String title;
    private String body;
    private String msgType;
    private String status;
    private long createTime;
    private long sendTime;
    private long arriveTime;
    private Map<String, Object> metadata;
    
    public MsgInfo() {
        this.metadata = new ConcurrentHashMap<>();
        this.createTime = System.currentTimeMillis();
        this.status = "draft";
    }
    
    public static MsgInfo create() {
        return new MsgInfo();
    }
    
    public static MsgInfo create(String toPersonId, String title, String body) {
        MsgInfo msg = new MsgInfo();
        msg.setToPersonId(toPersonId);
        msg.setTitle(title);
        msg.setBody(body);
        return msg;
    }
    
    public String getMsgId() { return msgId; }
    public void setMsgId(String msgId) { this.msgId = msgId; }
    
    public String getFromPersonId() { return fromPersonId; }
    public void setFromPersonId(String fromPersonId) { this.fromPersonId = fromPersonId; }
    
    public String getToPersonId() { return toPersonId; }
    public void setToPersonId(String toPersonId) { this.toPersonId = toPersonId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public String getMsgType() { return msgType; }
    public void setMsgType(String msgType) { this.msgType = msgType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getSendTime() { return sendTime; }
    public void setSendTime(long sendTime) { this.sendTime = sendTime; }
    
    public long getArriveTime() { return arriveTime; }
    public void setArriveTime(long arriveTime) { this.arriveTime = arriveTime; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { 
        this.metadata = metadata != null ? metadata : new ConcurrentHashMap<>(); 
    }
    
    public Object getMetadata(String key) { return metadata.get(key); }
    public void setMetadata(String key, Object value) { metadata.put(key, value); }
    
    public boolean isSent() { return "sent".equals(status); }
    public boolean isDraft() { return "draft".equals(status); }
    public boolean isRead() { return "read".equals(status); }
    
    @Override
    public String toString() {
        return "MsgInfo{" +
                "msgId='" + msgId + '\'' +
                ", from='" + fromPersonId + '\'' +
                ", to='" + toPersonId + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

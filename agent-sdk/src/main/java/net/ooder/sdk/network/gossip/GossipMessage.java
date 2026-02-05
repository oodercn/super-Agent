package net.ooder.sdk.network.gossip;

import net.ooder.sdk.network.packet.UDPPacket;
import java.time.Instant;

public class GossipMessage extends UDPPacket {
    private final GossipMessageType type;
    private final Object content;
    private final Instant timestamp;
    
    public GossipMessage(String senderId, String receiverId, GossipMessageType type, Object content) {
        super();
        setSenderId(senderId);
        setReceiverId(receiverId);
        this.type = type;
        this.content = content;
        this.timestamp = Instant.now();
    }
    
    @Override
    public String getType() {
        return type.name();
    }
    
    public GossipMessageType getMessageType() {
        return type;
    }
    
    public Object getContent() {
        return content;
    }
    
    public Instant getInstantTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "GossipMessage{" +
                "messageId='" + getMessageId() + '\'' +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", type=" + type +
                ", timestamp=" + getInstantTimestamp() +
                '}';
    }
}

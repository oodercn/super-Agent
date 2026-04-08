package net.ooder.spi.messaging.model;

import lombok.Data;
import net.ooder.scene.message.queue.MessageEnvelope;
import net.ooder.scene.message.queue.MessagePriority;
import net.ooder.scene.message.queue.DeliveryStatus;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class UnifiedMessage {
    
    private String messageId;
    
    private String conversationId;
    
    private String sceneGroupId;
    
    private MessageType messageType;
    
    private ConversationType conversationType;
    
    private Participant from;
    
    private Participant to;
    
    private List<Participant> cc;
    
    private Content content;
    
    private Map<String, Object> metadata;
    
    private int priority;
    
    private MessageStatus status;
    
    private long createTime;
    
    private long expireTime;
    
    private String threadId;
    
    private String parentMessageId;
    
    private List<MessageReaction> reactions;
    
    private boolean requiresAction;
    
    private List<MessageAction> availableActions;
    
    public static UnifiedMessage fromMessageEnvelope(MessageEnvelope envelope) {
        if (envelope == null) return null;
        UnifiedMessage msg = new UnifiedMessage();
        msg.setMessageId(envelope.getMessageId());
        msg.setConversationId(envelope.getConversationId());
        msg.setSceneGroupId(envelope.getSceneGroupId());
        msg.setFrom(Participant.fromMessageParticipant(envelope.getFrom()));
        msg.setTo(Participant.fromMessageParticipant(envelope.getTo()));
        msg.setContent(Content.fromObject(envelope.getContent()));
        msg.setMetadata(envelope.getMetadata());
        msg.setCreateTime(envelope.getCreatedAt());
        msg.setExpireTime(envelope.getExpireAt());
        if (envelope.getPriority() != null) {
            msg.setPriority(envelope.getPriority().getLevel());
        }
        return msg;
    }
    
    public MessageEnvelope toMessageEnvelope() {
        MessageEnvelope.Builder builder = MessageEnvelope.builder()
            .messageId(messageId)
            .conversationId(conversationId)
            .sceneGroupId(sceneGroupId);
        
        if (from != null) {
            builder.from(from.toMessageParticipant());
        }
        if (to != null) {
            builder.to(to.toMessageParticipant());
        }
        if (content != null) {
            builder.content(content.toObject());
        }
        if (metadata != null) {
            metadata.forEach(builder::metadata);
        }
        builder.priority(MessagePriority.fromLevel(priority));
        
        return builder.build();
    }
}

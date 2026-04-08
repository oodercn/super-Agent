package net.ooder.spi.messaging.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SendMessageRequest {
    
    private String conversationId;
    
    private String sceneGroupId;
    
    private ConversationType conversationType;
    
    private MessageType messageType;
    
    private Participant from;
    
    private Participant to;
    
    private List<Participant> cc;
    
    private Content content;
    
    private Map<String, Object> metadata;
    
    private boolean stream;
    
    private int priority;
}

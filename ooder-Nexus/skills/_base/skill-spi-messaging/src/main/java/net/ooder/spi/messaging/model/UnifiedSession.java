package net.ooder.spi.messaging.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class UnifiedSession {
    
    private String sessionId;
    
    private String userId;
    
    private String sceneGroupId;
    
    private SessionType sessionType;
    
    private String title;
    
    private String description;
    
    private List<Participant> participants;
    
    private Participant activeParticipant;
    
    private int messageCount;
    
    private int unreadCount;
    
    private UnifiedMessage lastMessage;
    
    private long createTime;
    
    private long updateTime;
    
    private long lastActiveTime;
    
    private Map<String, Object> context;
    
    private Map<String, Object> settings;
}

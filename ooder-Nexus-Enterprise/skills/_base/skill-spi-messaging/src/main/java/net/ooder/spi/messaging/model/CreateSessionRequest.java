package net.ooder.spi.messaging.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CreateSessionRequest {
    
    private String userId;
    
    private String sceneGroupId;
    
    private SessionType sessionType;
    
    private String title;
    
    private List<Participant> participants;
    
    private Map<String, Object> context;
}

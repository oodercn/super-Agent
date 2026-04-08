package net.ooder.spi.messaging.model;

import lombok.Data;
import java.util.Map;

@Data
public class MessageAction {
    
    private String actionId;
    
    private String label;
    
    private String icon;
    
    private String actionType;
    
    private Map<String, Object> config;
}

package net.ooder.spi.messaging.model;

import lombok.Data;

@Data
public class MessageReaction {
    
    private String emoji;
    
    private String userId;
    
    private long createTime;
}

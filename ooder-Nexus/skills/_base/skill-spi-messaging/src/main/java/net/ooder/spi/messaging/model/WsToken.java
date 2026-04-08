package net.ooder.spi.messaging.model;

import lombok.Data;

@Data
public class WsToken {
    
    private String token;
    
    private String tokenId;
    
    private String userId;
    
    private String sceneGroupId;
    
    private long expireAt;
    
    private long createdAt;
}

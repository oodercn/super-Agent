package net.ooder.enexus.config;

import net.ooder.scene.websocket.auth.WebSocketAuthService;
import net.ooder.scene.websocket.auth.WebSocketAuthServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketAuthConfig {

    @Bean
    @ConditionalOnMissingBean(WebSocketAuthService.class)
    public WebSocketAuthService webSocketAuthService() {
        return new WebSocketAuthServiceImpl();
    }
}

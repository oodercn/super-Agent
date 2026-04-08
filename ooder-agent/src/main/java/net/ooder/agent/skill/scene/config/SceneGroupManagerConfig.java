package net.ooder.mvp.skill.scene.config;

import net.ooder.scene.event.SceneEventPublisher;
import net.ooder.scene.group.SceneGroupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SceneGroupManagerConfig {

    private static final Logger log = LoggerFactory.getLogger(SceneGroupManagerConfig.class);

    @Bean
    @ConditionalOnMissingBean
    public SceneEventPublisher sceneEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        log.info("[sceneEventPublisher] Creating SceneEventPublisher for SE SDK 2.3.1");
        return new SceneEventPublisher(applicationEventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public SceneGroupManager sceneGroupManager(SceneEventPublisher sceneEventPublisher) {
        log.info("[sceneGroupManager] Creating SceneGroupManager for SE SDK 2.3.1");
        return new SceneGroupManager(sceneEventPublisher);
    }
}

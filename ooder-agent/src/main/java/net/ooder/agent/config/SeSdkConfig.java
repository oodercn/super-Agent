package net.ooder.agent.config;

import net.ooder.scene.event.SceneEventPublisher;
import net.ooder.scene.group.SceneGroupManager;
import net.ooder.scene.group.persistence.SceneGroupPersistence;
import net.ooder.scene.group.template.SceneGroupTemplateManager;
import net.ooder.scene.group.template.SqlSceneGroupTemplateManager;
import net.ooder.scene.llm.config.SceneLlmConfigManager;
import net.ooder.scene.skill.knowledge.KnowledgeBindingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeSdkConfig {

    private static final Logger log = LoggerFactory.getLogger(SeSdkConfig.class);

    @Value("${scene.engine.template.jdbc-url:jdbc:sqlite:./data/template.db}")
    private String templateJdbcUrl;

    @Bean
    @ConditionalOnMissingBean(KnowledgeBindingManager.class)
    public KnowledgeBindingManager knowledgeBindingManager() {
        log.info("[SeSdkConfig] Creating InMemoryKnowledgeBindingManager");
        return new InMemoryKnowledgeBindingManager();
    }

    @Bean
    @ConditionalOnMissingBean(SceneLlmConfigManager.class)
    public SceneLlmConfigManager sceneLlmConfigManager() {
        log.info("[SeSdkConfig] Creating InMemorySceneLlmConfigManager");
        return new InMemorySceneLlmConfigManager();
    }

    @Bean
    @ConditionalOnMissingBean(SceneEventPublisher.class)
    public SceneEventPublisher sceneEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        log.info("[SeSdkConfig] Creating SceneEventPublisher");
        return new SceneEventPublisher(applicationEventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean(SceneGroupPersistence.class)
    public SceneGroupPersistence sceneGroupPersistence() {
        log.info("[SeSdkConfig] Creating InMemorySceneGroupPersistence");
        return new InMemorySceneGroupPersistence();
    }

    @Bean
    @ConditionalOnMissingBean(SceneGroupManager.class)
    public SceneGroupManager sceneGroupManager(SceneEventPublisher sceneEventPublisher) {
        log.info("[SeSdkConfig] Creating SceneGroupManager");
        return new SceneGroupManager(sceneEventPublisher);
    }

    @Bean
    @ConditionalOnBean(SceneGroupManager.class)
    @ConditionalOnMissingBean(SceneGroupTemplateManager.class)
    public SceneGroupTemplateManager sceneGroupTemplateManager(SceneGroupManager sceneGroupManager) {
        log.info("[SeSdkConfig] Creating SqlSceneGroupTemplateManager with jdbcUrl: {}", templateJdbcUrl);
        SqlSceneGroupTemplateManager manager = new SqlSceneGroupTemplateManager(templateJdbcUrl, sceneGroupManager);
        manager.initialize();
        return manager;
    }
}

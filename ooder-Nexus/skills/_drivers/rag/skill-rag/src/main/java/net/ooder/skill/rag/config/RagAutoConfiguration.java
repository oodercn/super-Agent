package net.ooder.skill.rag.config;

import net.ooder.skill.rag.KnowledgeClassifierService;
import net.ooder.skill.rag.RagPipeline;
import net.ooder.skill.rag.impl.LlmClassifierServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.ooder.skill.rag")
@ConditionalOnProperty(name = "skill.rag.enabled", havingValue = "true", matchIfMissing = true)
public class RagAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagAutoConfiguration.class);

    public RagAutoConfiguration() {
        log.info("[RagAutoConfiguration] Initializing RAG skill module");
    }

    @Bean
    @ConditionalOnMissingBean(KnowledgeClassifierService.class)
    public KnowledgeClassifierService knowledgeClassifierService() {
        log.info("[RagAutoConfiguration] Creating LlmClassifierServiceImpl");
        return new LlmClassifierServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(RagPipeline.class)
    public RagPipeline ragPipeline() {
        log.info("[RagAutoConfiguration] Creating RagPipeline");
        return new RagPipeline();
    }
}

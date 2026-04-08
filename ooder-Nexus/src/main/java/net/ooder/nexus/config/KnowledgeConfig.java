package net.ooder.nexus.config;

import net.ooder.scene.skill.knowledge.*;
import net.ooder.scene.skill.knowledge.impl.*;
import net.ooder.scene.skill.knowledge.persistence.*;
import net.ooder.scene.skill.vector.*;
import net.ooder.scene.skill.vector.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class KnowledgeConfig {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeConfig.class);

    @Value("${scene.engine.vector-store.dimension:1536}")
    private int dimension;

    @Value("${scene.engine.embedding.model:mock}")
    private String embeddingModel;

    @Value("${scene.engine.embedding.dashscope.api-key:}")
    private String dashscopeApiKey;

    @Value("${scene.engine.embedding.dashscope.model:text-embedding-v2}")
    private String dashscopeModel;

    @Autowired(required = false)
    private KnowledgeRepository knowledgeRepository;

    @Bean
    @Primary
    public VectorStore vectorStore() {
        log.info("[KnowledgeConfig] Creating InMemoryVectorStore with dimension: {}", dimension);
        return new InMemoryVectorStore(dimension);
    }

    @Bean
    public SceneEmbeddingService embeddingService() {
        log.info("[KnowledgeConfig] Creating EmbeddingService with model: {}", embeddingModel);
        if ("dashscope".equals(embeddingModel) && dashscopeApiKey != null && !dashscopeApiKey.isEmpty()) {
            log.info("[KnowledgeConfig] Using DashScope embedding model: {}", dashscopeModel);
            EmbeddingModel model = new DashScopeEmbeddingModel(dashscopeApiKey, dashscopeModel);
            return new EmbeddingModelAdapter(model);
        }
        log.info("[KnowledgeConfig] Using Mock embedding service");
        return new MockEmbeddingService(dimension);
    }

    @Bean
    public DocumentChunker documentChunker() {
        log.info("[KnowledgeConfig] Creating FixedSizeDocumentChunker");
        return new FixedSizeDocumentChunker();
    }

    @Bean
    public FileParseService fileParseService() {
        log.info("[KnowledgeConfig] Creating FileParseServiceImpl");
        return new FileParseServiceImpl();
    }

    @Bean
    public KnowledgeBaseService knowledgeBaseService(
            DocumentChunker documentChunker,
            SceneEmbeddingService embeddingService,
            VectorStore vectorStore) {
        log.info("[KnowledgeConfig] Creating KnowledgeBaseServiceImpl");
        KnowledgeRepository repo = knowledgeRepository;
        if (repo == null) {
            log.warn("[KnowledgeConfig] KnowledgeRepository not available from SE SDK, using InMemoryKnowledgeRepository");
            repo = new InMemoryKnowledgeRepository();
        }
        return new KnowledgeBaseServiceImpl(repo, documentChunker, embeddingService, vectorStore);
    }

    private static class EmbeddingModelAdapter implements SceneEmbeddingService {
        private final EmbeddingModel delegate;
        private final int dimension;

        EmbeddingModelAdapter(EmbeddingModel delegate) {
            this.delegate = delegate;
            this.dimension = delegate.getDimensions();
        }

        @Override
        public float[] embed(String text) {
            List<Float> result = delegate.embed(text);
            float[] array = new float[result.size()];
            for (int i = 0; i < result.size(); i++) {
                array[i] = result.get(i);
            }
            return array;
        }

        @Override
        public List<float[]> embedBatch(List<String> texts) {
            List<List<Float>> results = delegate.embedBatch(texts);
            List<float[]> arrays = new ArrayList<>();
            for (List<Float> result : results) {
                float[] array = new float[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    array[i] = result.get(i);
                }
                arrays.add(array);
            }
            return arrays;
        }

        @Override
        public int getDimension() {
            return dimension;
        }

        @Override
        public String getModel() {
            return delegate.getName();
        }
    }
}

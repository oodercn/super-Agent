package net.ooder.skill.rag.spi;

import net.ooder.spi.rag.RagEnhanceDriver;
import net.ooder.spi.rag.model.RagKnowledgeConfig;
import net.ooder.spi.rag.model.RagRelatedDocument;
import net.ooder.skill.chat.model.KnowledgeDocument;
import net.ooder.skill.rag.RagPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RagEnhanceDriverImpl implements RagEnhanceDriver {

    private static final Logger log = LoggerFactory.getLogger(RagEnhanceDriverImpl.class);

    @Autowired(required = false)
    private RagPipeline ragPipeline;

    @Override
    public boolean isAvailable() {
        return ragPipeline != null;
    }

    @Override
    public String enhancePrompt(String query, String sceneGroupId, List<String> knowledgeBaseIds) {
        if (ragPipeline == null) return null;
        try {
            return ragPipeline.enhancePromptWithRAG(query, sceneGroupId, knowledgeBaseIds);
        } catch (Exception e) {
            log.warn("[RagEnhanceDriver] enhancePrompt failed: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RagKnowledgeConfig buildKnowledgeConfig(String sceneGroupId, String query) {
        if (ragPipeline == null) {
            return new RagKnowledgeConfig("", new ArrayList<>(), "");
        }
        try {
            RagPipeline.RagKnowledgeConfig config = ragPipeline.buildKnowledgeConfig(sceneGroupId, query);
            return new RagKnowledgeConfig(
                config.getKnowledgeContext(),
                config.getDictItems(),
                config.getSystemPromptTemplate()
            );
        } catch (Exception e) {
            log.warn("[RagEnhanceDriver] buildKnowledgeConfig failed: {}", e.getMessage());
            return new RagKnowledgeConfig("", new ArrayList<>(), "");
        }
    }

    @Override
    public List<RagRelatedDocument> searchRelated(String query, int limit) {
        if (ragPipeline == null) return new ArrayList<>();
        try {
            List<KnowledgeDocument> docs = ragPipeline.searchRelated(query, limit);
            List<RagRelatedDocument> result = new ArrayList<>(docs.size());
            for (KnowledgeDocument doc : docs) {
                result.add(new RagRelatedDocument(doc.getDocId(), doc.getTitle(), doc.getContent()));
            }
            return result;
        } catch (Exception e) {
            log.warn("[RagEnhanceDriver] searchRelated failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}

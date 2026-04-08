package net.ooder.spi.rag;

import net.ooder.spi.rag.model.RagKnowledgeConfig;
import net.ooder.spi.rag.model.RagRelatedDocument;

import java.util.List;

public interface RagEnhanceDriver {

    boolean isAvailable();

    String enhancePrompt(String query, String sceneGroupId, List<String> knowledgeBaseIds);

    RagKnowledgeConfig buildKnowledgeConfig(String sceneGroupId, String query);

    List<RagRelatedDocument> searchRelated(String query, int limit);
}

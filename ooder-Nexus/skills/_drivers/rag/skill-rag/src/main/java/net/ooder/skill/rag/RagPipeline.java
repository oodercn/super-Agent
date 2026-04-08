package net.ooder.skill.rag;

import net.ooder.skill.chat.model.KnowledgeDocument;
import net.ooder.skill.chat.service.KnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RagPipeline {

    private static final Logger log = LoggerFactory.getLogger(RagPipeline.class);

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private net.ooder.skill.dict.service.DictService dictService;

    @Autowired
    private KnowledgeClassifierService classifierService;

    public KnowledgeDocument ingestBusinessData(BusinessDataIngestRequest request) {
        log.info("[RagPipeline] Ingesting business data: type={}", request.getDataType());

        String category = classifierService.classify(
            request.getTitle() + "\n" + request.getContent()
        );

        KnowledgeDocument doc = knowledgeService.uploadDocument(
            request.getSourceUserId() != null ? request.getSourceUserId() : "system",
            request.getTitle(),
            request.getContent(),
            request.getDataType()
        );
        log.info("[RagPipeline] Ingested as docId={}, category={}", doc.getDocId(), category);

        if (request.isSyncToDict()) {
            syncToDictionary(doc, category);
        }

        return doc;
    }

    public RagKnowledgeConfig buildKnowledgeConfig(String sceneGroupId, String query) {
        log.info("[RagPipeline] Building knowledge config for query: {}", query);

        RagKnowledgeConfig config = new RagKnowledgeConfig();

        try {
            List<String> docIds = knowledgeService.search(query, 5);
            List<KnowledgeDocument> relatedDocs = new ArrayList<>();
            for (String docId : docIds) {
                KnowledgeDocument doc = knowledgeService.getDocument(docId);
                if (doc != null) relatedDocs.add(doc);
            }

            StringBuilder contextBuilder = new StringBuilder();
            for (KnowledgeDocument doc : relatedDocs) {
                contextBuilder.append("### ").append(doc.getTitle()).append("\n");
                contextBuilder.append(doc.getContent()).append("\n\n");
            }
            config.setKnowledgeContext(contextBuilder.toString());
        } catch (Exception e) {
            log.warn("[RagPipeline] Search failed, using empty context: {}", e.getMessage());
            config.setKnowledgeContext("");
        }

        try {
            var dictItems = dictService.getDictItems(sceneGroupId != null ? sceneGroupId : "default");
            if (dictItems == null || dictItems.isEmpty()) {
                var allDicts = dictService.getAllDicts();
                if (allDicts != null) {
                    dictItems = allDicts.stream()
                        .flatMap(d -> d.getItems().stream())
                        .limit(20)
                        .toList();
                }
            }
            List<Map<String, String>> entries = new ArrayList<>();
            if (dictItems != null) {
                for (var item : dictItems) {
                    Map<String, String> entry = new HashMap<>();
                    entry.put("key", item.getCode());
                    entry.put("value", item.getName());
                    entries.add(entry);
                }
            }
            config.setDictItems(entries);
        } catch (Exception e) {
            log.warn("[RagPipeline] Dict lookup failed: {}", e.getMessage());
            config.setDictItems(new ArrayList<>());
        }

        config.setSystemPromptTemplate(buildRagPromptTemplate(config));

        return config;
    }

    public String enhancePromptWithRAG(String userQuery, String sceneGroupId,
                                         List<String> knowledgeBaseIds) {
        try {
            List<String> docIds = knowledgeService.search(userQuery, 3);
            if (docIds.isEmpty()) return null;

            List<KnowledgeDocument> docs = new ArrayList<>();
            for (String docId : docIds) {
                KnowledgeDocument doc = knowledgeService.getDocument(docId);
                if (doc != null) docs.add(doc);
            }
            if (docs.isEmpty()) return null;

            StringBuilder ragContext = new StringBuilder();
            ragContext.append("\n## 参考资料\n");
            ragContext.append("以下是与用户问题相关的参考资料，请基于这些资料回答：\n\n");

            for (int i = 0; i < docs.size(); i++) {
                ragContext.append("[").append(i + 1).append("] **")
                         .append(docs.get(i).getTitle()).append("**\n");
                ragContext.append(docs.get(i).getContent()).append("\n\n");
            }

            return ragContext.toString();
        } catch (Exception e) {
            log.warn("[RagPipeline] RAG enhancement failed: {}", e.getMessage());
            return null;
        }
    }

    public List<KnowledgeDocument> searchRelated(String query, int limit) {
        try {
            List<String> docIds = knowledgeService.search(query, limit);
            List<KnowledgeDocument> result = new ArrayList<>();
            for (String docId : docIds) {
                KnowledgeDocument doc = knowledgeService.getDocument(docId);
                if (doc != null) result.add(doc);
            }
            return result;
        } catch (Exception e) { log.warn("[RagPipeline] search failed: {}", e.getMessage()); return new ArrayList<>(); }
    }

    private void syncToDictionary(KnowledgeDocument doc, String category) {
        try {
            List<DictEntity> entities = classifierService.extractEntities(doc.getContent());
            for (DictEntity entity : entities) {
                dictService.refreshCache();
            }
        } catch (Exception e) { log.warn("[RagPipeline] Failed to sync to dictionary: {}", e.getMessage()); }
    }

    private String buildRagPromptTemplate(RagKnowledgeConfig config) {
        return """
            你是一个智能助手。在回答问题时，请参考以下知识和字典数据：

            {{knowledge_context}}

            ## 可用字典数据
            {{dict_items}}

            请基于以上资料给出准确、专业的回答。
            """;
    }

    public static class BusinessDataIngestRequest {
        private String title;
        private String content;
        private String dataType;
        private String sourceUserId;
        private boolean syncToDict;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }
        public String getSourceUserId() { return sourceUserId; }
        public void setSourceUserId(String sourceUserId) { this.sourceUserId = sourceUserId; }
        public boolean isSyncToDict() { return syncToDict; }
        public void setSyncToDict(boolean syncToDict) { this.syncToDict = syncToDict; }
    }

    public record DictEntity(String category, String key, String value, String description) {}

    public static class RagKnowledgeConfig {
        private String knowledgeContext;
        private List<Map<String, String>> dictItems;
        private String systemPromptTemplate;

        public String getKnowledgeContext() { return knowledgeContext; }
        public void setKnowledgeContext(String knowledgeContext) { this.knowledgeContext = knowledgeContext; }
        public List<Map<String, String>> getDictItems() { return dictItems; }
        public void setDictItems(List<Map<String, String>> dictItems) { this.dictItems = dictItems; }
        public String getSystemPromptTemplate() { return systemPromptTemplate; }
        public void setSystemPromptTemplate(String systemPromptTemplate) { this.systemPromptTemplate = systemPromptTemplate; }
    }
}

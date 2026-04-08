package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeBaseDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeDocumentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocalKnowledgeBaseService {

    private static final Logger log = LoggerFactory.getLogger(LocalKnowledgeBaseService.class);

    private final Map<String, KnowledgeBaseDTO> knowledgeBases = new ConcurrentHashMap<>();
    private final Map<String, Map<String, KnowledgeDocumentDTO>> documents = new ConcurrentHashMap<>();

    public LocalKnowledgeBaseService() {
        initTestData();
    }

    private void initTestData() {
        KnowledgeBaseDTO kb1 = new KnowledgeBaseDTO();
        kb1.setKbId("kb-default-001");
        kb1.setName("默认知识库");
        kb1.setDescription("系统默认知识库，用于存储通用知识");
        kb1.setOwnerId("system");
        kb1.setVisibility("private");
        kb1.setEmbeddingModel("text-embedding-ada-002");
        kb1.setChunkSize(500);
        kb1.setChunkOverlap(50);
        kb1.setDocumentCount(3);
        kb1.setTags(Arrays.asList("default", "system"));
        kb1.setCreateTime(System.currentTimeMillis());
        KnowledgeBaseDTO.KnowledgeLayerConfig layerConfig1 = new KnowledgeBaseDTO.KnowledgeLayerConfig();
        layerConfig1.setLayer("GENERAL");
        layerConfig1.setPriority(0);
        layerConfig1.setEnabled(true);
        kb1.setLayerConfig(layerConfig1);
        knowledgeBases.put(kb1.getKbId(), kb1);

        Map<String, KnowledgeDocumentDTO> docs1 = new HashMap<>();
        
        KnowledgeDocumentDTO doc1 = new KnowledgeDocumentDTO();
        doc1.setDocId("doc-001");
        doc1.setKbId(kb1.getKbId());
        doc1.setTitle("产品使用手册");
        doc1.setContent("这是产品使用手册的内容...");
        doc1.setSource("upload");
        doc1.setChunkCount(10);
        doc1.setCreateTime(System.currentTimeMillis() - 86400000);
        docs1.put(doc1.getDocId(), doc1);

        KnowledgeDocumentDTO doc2 = new KnowledgeDocumentDTO();
        doc2.setDocId("doc-002");
        doc2.setKbId(kb1.getKbId());
        doc2.setTitle("API文档");
        doc2.setContent("这是API文档的内容...");
        doc2.setSource("text");
        doc2.setChunkCount(5);
        doc2.setCreateTime(System.currentTimeMillis() - 43200000);
        docs1.put(doc2.getDocId(), doc2);

        documents.put(kb1.getKbId(), docs1);

        KnowledgeBaseDTO kb2 = new KnowledgeBaseDTO();
        kb2.setKbId("kb-tech-001");
        kb2.setName("技术文档库");
        kb2.setDescription("技术相关文档和代码示例");
        kb2.setOwnerId("user-admin-001");
        kb2.setVisibility("team");
        kb2.setEmbeddingModel("text-embedding-ada-002");
        kb2.setChunkSize(1000);
        kb2.setChunkOverlap(100);
        kb2.setDocumentCount(0);
        kb2.setTags(Arrays.asList("tech", "documentation"));
        kb2.setCreateTime(System.currentTimeMillis() - 172800000);
        KnowledgeBaseDTO.KnowledgeLayerConfig layerConfig2 = new KnowledgeBaseDTO.KnowledgeLayerConfig();
        layerConfig2.setLayer("PROFESSIONAL");
        layerConfig2.setPriority(1);
        layerConfig2.setEnabled(true);
        kb2.setLayerConfig(layerConfig2);
        knowledgeBases.put(kb2.getKbId(), kb2);
        documents.put(kb2.getKbId(), new HashMap<>());

        log.info("LocalKnowledgeBaseService initialized with {} knowledge bases", knowledgeBases.size());
    }

    public List<KnowledgeBaseDTO> listAll() {
        return new ArrayList<>(knowledgeBases.values());
    }

    public KnowledgeBaseDTO get(String kbId) {
        return knowledgeBases.get(kbId);
    }

    public KnowledgeBaseDTO create(KnowledgeBaseDTO request) {
        String kbId = "kb-" + System.currentTimeMillis();
        KnowledgeBaseDTO kb = new KnowledgeBaseDTO();
        kb.setKbId(kbId);
        kb.setName(request.getName());
        kb.setDescription(request.getDescription());
        kb.setOwnerId(request.getOwnerId() != null ? request.getOwnerId() : "system");
        kb.setVisibility(request.getVisibility() != null ? request.getVisibility() : "private");
        kb.setEmbeddingModel(request.getEmbeddingModel() != null ? request.getEmbeddingModel() : "text-embedding-ada-002");
        kb.setChunkSize(request.getChunkSize() > 0 ? request.getChunkSize() : 500);
        kb.setChunkOverlap(request.getChunkOverlap() > 0 ? request.getChunkOverlap() : 50);
        kb.setTags(request.getTags());
        kb.setLayerConfig(request.getLayerConfig());
        kb.setDocumentCount(0);
        kb.setCreateTime(System.currentTimeMillis());
        
        knowledgeBases.put(kbId, kb);
        documents.put(kbId, new HashMap<>());
        
        log.info("Created knowledge base: {}", kbId);
        return kb;
    }

    public boolean delete(String kbId) {
        if (knowledgeBases.remove(kbId) != null) {
            documents.remove(kbId);
            log.info("Deleted knowledge base: {}", kbId);
            return true;
        }
        return false;
    }

    public List<KnowledgeDocumentDTO> listDocuments(String kbId) {
        Map<String, KnowledgeDocumentDTO> kbDocs = documents.get(kbId);
        return kbDocs != null ? new ArrayList<>(kbDocs.values()) : new ArrayList<>();
    }

    public KnowledgeDocumentDTO addDocument(String kbId, KnowledgeDocumentDTO request) {
        Map<String, KnowledgeDocumentDTO> kbDocs = documents.computeIfAbsent(kbId, k -> new HashMap<>());
        
        String docId = "doc-" + System.currentTimeMillis();
        KnowledgeDocumentDTO doc = new KnowledgeDocumentDTO();
        doc.setDocId(docId);
        doc.setKbId(kbId);
        doc.setTitle(request.getTitle());
        doc.setContent(request.getContent());
        doc.setSource(request.getSource() != null ? request.getSource() : "text");
        doc.setSourceUrl(request.getSourceUrl());
        doc.setFileSize(request.getFileSize());
        doc.setTags(request.getTags());
        doc.setChunkCount(request.getContent() != null ? request.getContent().length() / 500 + 1 : 0);
        doc.setCreateTime(System.currentTimeMillis());
        
        kbDocs.put(docId, doc);
        
        KnowledgeBaseDTO kb = knowledgeBases.get(kbId);
        if (kb != null) {
            kb.setDocumentCount(kbDocs.size());
        }
        
        log.info("Added document {} to knowledge base {}", docId, kbId);
        return doc;
    }

    public boolean deleteDocument(String kbId, String docId) {
        Map<String, KnowledgeDocumentDTO> kbDocs = documents.get(kbId);
        if (kbDocs != null && kbDocs.remove(docId) != null) {
            KnowledgeBaseDTO kb = knowledgeBases.get(kbId);
            if (kb != null) {
                kb.setDocumentCount(kbDocs.size());
            }
            log.info("Deleted document {} from knowledge base {}", docId, kbId);
            return true;
        }
        return false;
    }

    public KnowledgeDocumentDTO getDocument(String kbId, String docId) {
        Map<String, KnowledgeDocumentDTO> kbDocs = documents.get(kbId);
        return kbDocs != null ? kbDocs.get(docId) : null;
    }
}

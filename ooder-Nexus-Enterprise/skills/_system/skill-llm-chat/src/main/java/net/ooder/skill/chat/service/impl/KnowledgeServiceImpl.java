package net.ooder.skill.chat.service.impl;

import net.ooder.skill.chat.model.KnowledgeDocument;
import net.ooder.skill.chat.service.KnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class KnowledgeServiceImpl implements KnowledgeService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeServiceImpl.class);

    private final Map<String, KnowledgeDocument> documents = new ConcurrentHashMap<>();

    public KnowledgeServiceImpl() {
        initDefaultDocuments();
    }

    private void initDefaultDocuments() {
        addDocument("doc-ooder-intro", "Ooder框架介绍", 
            "Ooder是一个基于Java的技能框架，支持动态加载和管理技能模块。" +
            "框架采用Spring Boot技术栈，提供了完整的技能生命周期管理能力。", "system");
        
        addDocument("doc-skill-dev", "技能开发指南", 
            "技能开发需要遵循以下步骤：1. 创建skill.yaml配置文件；" +
            "2. 实现Controller和Service；3. 定义路由和服务；4. 打包为JAR文件。", "system");
        
        addDocument("doc-api-spec", "API规范说明", 
            "Ooder API遵循RESTful设计原则，所有API返回统一的ResultModel格式。" +
            "支持JSON数据格式，提供完整的错误处理机制。", "system");
    }

    private void addDocument(String docId, String title, String content, String userId) {
        KnowledgeDocument doc = new KnowledgeDocument();
        doc.setDocId(docId);
        doc.setTitle(title);
        doc.setContent(content);
        doc.setUserId(userId);
        doc.setType("text");
        doc.setStatus(KnowledgeDocument.DocumentStatus.READY);
        doc.setChunkCount(1);
        documents.put(docId, doc);
    }

    @Override
    public List<KnowledgeDocument> listDocuments(String userId) {
        return documents.values().stream()
                .filter(d -> userId == null || userId.equals(d.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public KnowledgeDocument getDocument(String docId) {
        return documents.get(docId);
    }

    @Override
    public KnowledgeDocument uploadDocument(String userId, String title, String content, String type) {
        KnowledgeDocument doc = new KnowledgeDocument();
        doc.setDocId("doc-" + UUID.randomUUID().toString().substring(0, 8));
        doc.setTitle(title);
        doc.setContent(content);
        doc.setUserId(userId != null ? userId : "anonymous");
        doc.setType(type != null ? type : "text");
        doc.setStatus(KnowledgeDocument.DocumentStatus.READY);
        doc.setChunkCount(1);
        documents.put(doc.getDocId(), doc);
        log.info("Uploaded document: {}", doc.getDocId());
        return doc;
    }

    @Override
    public void deleteDocument(String docId) {
        documents.remove(docId);
        log.info("Deleted document: {}", docId);
    }

    @Override
    public List<String> search(String query, int limit) {
        List<String> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (KnowledgeDocument doc : documents.values()) {
            if (doc.getStatus() == KnowledgeDocument.DocumentStatus.READY) {
                if (doc.getContent() != null && doc.getContent().toLowerCase().contains(lowerQuery)) {
                    results.add(doc.getTitle() + ": " + 
                        truncate(doc.getContent(), 200));
                }
                if (results.size() >= limit) break;
            }
        }
        
        log.info("Knowledge search for '{}' found {} results", query, results.size());
        return results;
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
}

package net.ooder.skill.chat.service;

import net.ooder.skill.chat.model.KnowledgeDocument;

import java.util.List;

public interface KnowledgeService {

    List<KnowledgeDocument> listDocuments(String userId);

    KnowledgeDocument getDocument(String docId);

    KnowledgeDocument uploadDocument(String userId, String title, String content, String type);

    void deleteDocument(String docId);

    List<String> search(String query, int limit);
}

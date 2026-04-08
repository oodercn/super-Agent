package net.ooder.skill.chat.controller;

import net.ooder.skill.chat.model.KnowledgeDocument;
import net.ooder.skill.chat.service.KnowledgeService;
import net.ooder.skill.chat.service.impl.KnowledgeServiceImpl;
import net.ooder.skill.common.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/knowledge")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class KnowledgeController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeController.class);

    private KnowledgeService knowledgeService;

    public KnowledgeController() {
        this.knowledgeService = new KnowledgeServiceImpl();
    }

    public void setKnowledgeService(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping("/documents")
    public ResultModel<List<KnowledgeDocument>> listDocuments(
            @RequestParam(required = false) String userId) {
        log.info("List knowledge documents for user: {}", userId);
        List<KnowledgeDocument> documents = knowledgeService.listDocuments(userId);
        return ResultModel.success(documents);
    }

    @GetMapping("/documents/{docId}")
    public ResultModel<KnowledgeDocument> getDocument(@PathVariable String docId) {
        log.info("Get knowledge document: {}", docId);
        KnowledgeDocument doc = knowledgeService.getDocument(docId);
        if (doc == null) {
            return ResultModel.notFound("Document not found: " + docId);
        }
        return ResultModel.success(doc);
    }

    @PostMapping("/documents")
    public ResultModel<KnowledgeDocument> uploadDocument(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String title = request.get("title");
        String content = request.get("content");
        String type = request.get("type");
        log.info("Upload knowledge document: title={}, type={}", title, type);
        KnowledgeDocument doc = knowledgeService.uploadDocument(userId, title, content, type);
        return ResultModel.success(doc);
    }

    @DeleteMapping("/documents/{docId}")
    public ResultModel<Boolean> deleteDocument(@PathVariable String docId) {
        log.info("Delete knowledge document: {}", docId);
        knowledgeService.deleteDocument(docId);
        return ResultModel.success(true);
    }

    @PostMapping("/search")
    public ResultModel<List<String>> searchKnowledge(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        Integer limit = (Integer) request.get("limit");
        if (limit == null) limit = 5;
        log.info("Search knowledge: query={}, limit={}", query, limit);
        List<String> results = knowledgeService.search(query, limit);
        return ResultModel.success(results);
    }
}

package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeBaseDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeDocumentDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeSearchRequestDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeSearchResultDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.TextDocumentRequestDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.UrlImportRequestDTO;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.impl.LocalKnowledgeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/knowledge-bases")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KnowledgeBaseController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);
    
    @Autowired
    private LocalKnowledgeBaseService knowledgeBaseService;

    @GetMapping
    public ResultModel<List<KnowledgeBaseDTO>> listKnowledgeBases() {
        log.info("[listKnowledgeBases] request start");
        List<KnowledgeBaseDTO> kbs = knowledgeBaseService.listAll();
        return ResultModel.success(kbs);
    }
    
    @GetMapping("/{kbId}")
    public ResultModel<KnowledgeBaseDTO> getKnowledgeBase(@PathVariable String kbId) {
        log.info("[getKnowledgeBase] kbId: {}", kbId);
        KnowledgeBaseDTO kb = knowledgeBaseService.get(kbId);
        if (kb == null) {
            return ResultModel.notFound("Knowledge base not found: " + kbId);
        }
        return ResultModel.success(kb);
    }
    
    @PostMapping
    public ResultModel<KnowledgeBaseDTO> createKnowledgeBase(@RequestBody KnowledgeBaseDTO request) {
        log.info("[createKnowledgeBase] name: {}", request.getName());
        KnowledgeBaseDTO kb = knowledgeBaseService.create(request);
        return ResultModel.success(kb);
    }
    
    @PutMapping("/{kbId}")
    public ResultModel<KnowledgeBaseDTO> updateKnowledgeBase(@PathVariable String kbId, @RequestBody KnowledgeBaseDTO request) {
        log.info("[updateKnowledgeBase] kbId: {}", kbId);
        return ResultModel.error(503, "Update not implemented yet");
    }
    
    @DeleteMapping("/{kbId}")
    public ResultModel<Boolean> deleteKnowledgeBase(@PathVariable String kbId) {
        log.info("[deleteKnowledgeBase] kbId: {}", kbId);
        boolean result = knowledgeBaseService.delete(kbId);
        return ResultModel.success(result);
    }
    
    @PostMapping("/{kbId}/rebuild-index")
    public ResultModel<Boolean> rebuildIndex(@PathVariable String kbId) {
        log.info("[rebuildIndex] kbId: {}", kbId);
        return ResultModel.success(true);
    }
    
    @GetMapping("/layer/{layer}")
    public ResultModel<List<KnowledgeBaseDTO>> listByLayer(@PathVariable String layer) {
        log.info("[listByLayer] layer: {}", layer);
        return ResultModel.success(knowledgeBaseService.listAll());
    }
    
    @GetMapping("/{kbId}/documents")
    public ResultModel<List<KnowledgeDocumentDTO>> listDocuments(@PathVariable String kbId) {
        log.info("[listDocuments] kbId: {}", kbId);
        List<KnowledgeDocumentDTO> docs = knowledgeBaseService.listDocuments(kbId);
        return ResultModel.success(docs);
    }
    
    @PostMapping("/{kbId}/documents/text")
    public ResultModel<KnowledgeDocumentDTO> addTextDocument(@PathVariable String kbId, @RequestBody KnowledgeDocumentDTO request) {
        log.info("[addTextDocument] kbId: {}, title: {}", kbId, request.getTitle());
        KnowledgeDocumentDTO doc = knowledgeBaseService.addDocument(kbId, request);
        return ResultModel.success(doc);
    }
    
    @PostMapping("/{kbId}/documents/upload")
    public ResultModel<KnowledgeDocumentDTO> uploadDocument(
            @PathVariable String kbId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tags", required = false) String tags) throws Exception {
        log.info("[uploadDocument] kbId: {}, filename: {}", kbId, file.getOriginalFilename());
        
        KnowledgeDocumentDTO request = new KnowledgeDocumentDTO();
        request.setTitle(title != null && !title.isEmpty() ? title : file.getOriginalFilename());
        request.setContent(new String(file.getBytes(), "UTF-8"));
        request.setSource("upload");
        request.setFileSize(file.getSize());
        if (tags != null && !tags.isEmpty()) {
            request.setTags(Arrays.asList(tags.split(",")));
        }
        
        KnowledgeDocumentDTO doc = knowledgeBaseService.addDocument(kbId, request);
        return ResultModel.success(doc);
    }
    
    @PostMapping("/{kbId}/documents/url")
    public ResultModel<KnowledgeDocumentDTO> importFromUrl(@PathVariable String kbId, @RequestBody UrlImportRequestDTO request) {
        log.info("[importFromUrl] kbId: {}, url: {}", kbId, request.getUrl());
        return ResultModel.error(503, "URL import not implemented yet");
    }
    
    @DeleteMapping("/{kbId}/documents/{docId}")
    public ResultModel<Boolean> deleteDocument(@PathVariable String kbId, @PathVariable String docId) {
        log.info("[deleteDocument] kbId: {}, docId: {}", kbId, docId);
        boolean result = knowledgeBaseService.deleteDocument(kbId, docId);
        return ResultModel.success(result);
    }
    
    @PostMapping("/{kbId}/documents/{docId}/reindex")
    public ResultModel<KnowledgeDocumentDTO> reindexDocument(@PathVariable String kbId, @PathVariable String docId) {
        log.info("[reindexDocument] kbId: {}, docId: {}", kbId, docId);
        return ResultModel.error(503, "Reindex not implemented yet");
    }
    
    @GetMapping("/{kbId}/documents/{docId}")
    public ResultModel<KnowledgeDocumentDTO> getDocument(@PathVariable String kbId, @PathVariable String docId) {
        log.info("[getDocument] kbId: {}, docId: {}", kbId, docId);
        KnowledgeDocumentDTO doc = knowledgeBaseService.getDocument(kbId, docId);
        if (doc == null) {
            return ResultModel.notFound("Document not found: " + docId);
        }
        return ResultModel.success(doc);
    }
    
    @PostMapping("/{kbId}/search")
    public ResultModel<Map<String, Object>> searchKnowledge(
            @PathVariable String kbId,
            @RequestBody KnowledgeSearchRequestDTO request) {
        log.info("[searchKnowledge] kbId: {}, query: {}", kbId, request.getQuery());
        
        List<KnowledgeSearchResultDTO> results = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("total", 0);
        return ResultModel.success(response);
    }
    
    @GetMapping("/{kbId}/index-status")
    public ResultModel<Map<String, Object>> getIndexStatus(@PathVariable String kbId) {
        log.info("[getIndexStatus] kbId: {}", kbId);
        Map<String, Object> status = new HashMap<>();
        status.put("status", "ready");
        status.put("progress", 100);
        return ResultModel.success(status);
    }
}

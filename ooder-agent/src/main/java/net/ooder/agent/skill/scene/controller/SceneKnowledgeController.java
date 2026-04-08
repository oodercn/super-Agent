package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeSearchRequestDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.KnowledgeSearchResultDTO;
import net.ooder.mvp.skill.scene.knowledge.SceneKnowledgeBindingService;
import net.ooder.mvp.skill.scene.knowledge.SceneKnowledgeBindingService.KnowledgeBinding;
import net.ooder.mvp.skill.scene.knowledge.SceneKnowledgeBindingService.KnowledgeLayerConfig;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/scene-groups/{sceneGroupId}/knowledge")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SceneKnowledgeController {

    private static final Logger log = LoggerFactory.getLogger(SceneKnowledgeController.class);
    
    @Autowired
    private SceneKnowledgeBindingService sceneKnowledgeBindingService;

    @GetMapping
    public ResultModel<List<KnowledgeBinding>> listKnowledgeBindings(@PathVariable String sceneGroupId) {
        log.info("[listKnowledgeBindings] sceneGroupId: {}", sceneGroupId);
        
        List<KnowledgeBinding> bindings = sceneKnowledgeBindingService.getBindings(sceneGroupId);
        
        return ResultModel.success(bindings);
    }
    
    @PostMapping
    public ResultModel<KnowledgeBinding> bindKnowledge(
            @PathVariable String sceneGroupId,
            @RequestBody KnowledgeBindingRequest request) {
        log.info("[bindKnowledge] sceneGroupId: {}, kbId: {}, layer: {}", sceneGroupId, request.getKbId(), request.getLayer());
        
        sceneKnowledgeBindingService.bindToScene(
            sceneGroupId, 
            request.getKbId(), 
            request.getLayer(), 
            request.getPriority() != null ? request.getPriority() : 0
        );
        
        List<KnowledgeBinding> bindings = sceneKnowledgeBindingService.getBindings(sceneGroupId);
        KnowledgeBinding binding = null;
        for (KnowledgeBinding b : bindings) {
            if (b.getKbId().equals(request.getKbId()) && b.getLayer().equals(request.getLayer())) {
                binding = b;
                break;
            }
        }
        
        return ResultModel.success(binding);
    }
    
    @DeleteMapping("/{kbId}")
    public ResultModel<Boolean> unbindKnowledge(
            @PathVariable String sceneGroupId,
            @PathVariable String kbId,
            @RequestParam String layer) {
        log.info("[unbindKnowledge] sceneGroupId: {}, kbId: {}, layer: {}", sceneGroupId, kbId, layer);
        
        sceneKnowledgeBindingService.unbindFromScene(sceneGroupId, kbId, layer);
        
        return ResultModel.success(true);
    }
    
    @GetMapping("/config")
    public ResultModel<KnowledgeLayerConfig> getKnowledgeConfig(@PathVariable String sceneGroupId) {
        log.info("[getKnowledgeConfig] sceneGroupId: {}", sceneGroupId);
        
        KnowledgeLayerConfig config = sceneKnowledgeBindingService.getLayerConfig(sceneGroupId);
        
        return ResultModel.success(config);
    }
    
    @PutMapping("/config")
    public ResultModel<KnowledgeLayerConfig> updateKnowledgeConfig(
            @PathVariable String sceneGroupId,
            @RequestBody KnowledgeLayerConfig config) {
        log.info("[updateKnowledgeConfig] sceneGroupId: {}", sceneGroupId);
        
        sceneKnowledgeBindingService.setLayerConfig(sceneGroupId, config);
        
        return ResultModel.success(config);
    }
    
    @PostMapping("/search")
    public ResultModel<Map<String, Object>> searchKnowledge(
            @PathVariable String sceneGroupId,
            @RequestBody KnowledgeSearchRequestDTO request) {
        log.info("[searchKnowledge] sceneGroupId: {}, query: {}", sceneGroupId, request.getQuery());
        
        List<Map<String, Object>> serviceResults;
        
        if (request.getLayers() != null && !request.getLayers().isEmpty()) {
            serviceResults = sceneKnowledgeBindingService.crossLayerSearch(
                sceneGroupId, 
                request.getQuery(), 
                request.getLayers(), 
                request.getTopK()
            );
        } else {
            serviceResults = sceneKnowledgeBindingService.searchKnowledge(
                sceneGroupId, 
                request.getQuery(), 
                request.getTopK()
            );
        }
        
        List<KnowledgeSearchResultDTO> results = new ArrayList<KnowledgeSearchResultDTO>();
        for (Map<String, Object> sr : serviceResults) {
            KnowledgeSearchResultDTO dto = new KnowledgeSearchResultDTO();
            dto.setDocId((String) sr.get("docId"));
            dto.setKbId((String) sr.get("kbId"));
            dto.setKbName((String) sr.get("kbName"));
            dto.setLayer((String) sr.get("layer"));
            dto.setContent((String) sr.get("content"));
            dto.setScore(sr.get("score") != null ? ((Number) sr.get("score")).doubleValue() : 0.0);
            dto.setTitle((String) sr.get("title"));
            results.add(dto);
        }
        
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("results", results);
        response.put("total", results.size());
        
        return ResultModel.success(response);
    }
    
    public static class KnowledgeBindingRequest {
        private String kbId;
        private String layer;
        private Integer priority;
        
        public String getKbId() { return kbId; }
        public void setKbId(String kbId) { this.kbId = kbId; }
        public String getLayer() { return layer; }
        public void setLayer(String layer) { this.layer = layer; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
    }
}

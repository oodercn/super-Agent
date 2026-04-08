package net.ooder.skill.rag.controller;

import net.ooder.skill.chat.model.KnowledgeDocument;
import net.ooder.skill.rag.KnowledgeClassifierService;
import net.ooder.skill.rag.RagPipeline;
import net.ooder.skill.rag.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rag")
public class RagController {

    private static final Logger log = LoggerFactory.getLogger(RagController.class);

    @Autowired
    private KnowledgeClassifierService classifierService;

    @Autowired
    private RagPipeline ragPipeline;

    @PostMapping("/classify")
    public ResultModel<String> classify(@RequestBody String text) {
        log.info("[RagController] classify: text length={}", text != null ? text.length() : 0);
        try {
            String category = classifierService.classify(text);
            return ResultModel.success(category);
        } catch (Exception e) {
            log.error("[RagController] classify failed", e);
            return ResultModel.error("分类失败: " + e.getMessage());
        }
    }

    @PostMapping("/classify/batch")
    public ResultModel<Map<String, List<String>>> batchClassify(@RequestBody List<String> texts) {
        log.info("[RagController] batchClassify: count={}", texts != null ? texts.size() : 0);
        try {
            Map<String, List<String>> result = classifierService.batchClassify(texts);
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("[RagController] batchClassify failed", e);
            return ResultModel.error("批量分类失败: " + e.getMessage());
        }
    }

    @PostMapping("/extract-tags")
    public ResultModel<List<String>> extractTags(@RequestBody String text) {
        log.info("[RagController] extractTags: text length={}", text != null ? text.length() : 0);
        try {
            List<String> tags = classifierService.extractTags(text);
            return ResultModel.success(tags);
        } catch (Exception e) {
            log.error("[RagController] extractTags failed", e);
            return ResultModel.error("标签提取失败: " + e.getMessage());
        }
    }

    @PostMapping("/extract-entities")
    public ResultModel<List<RagPipeline.DictEntity>> extractEntities(@RequestBody String text) {
        log.info("[RagController] extractEntities: text length={}", text != null ? text.length() : 0);
        try {
            List<RagPipeline.DictEntity> entities = classifierService.extractEntities(text);
            return ResultModel.success(entities);
        } catch (Exception e) {
            log.error("[RagController] extractEntities failed", e);
            return ResultModel.error("实体抽取失败: " + e.getMessage());
        }
    }

    @PostMapping("/enhance")
    public ResultModel<String> enhancePrompt(
            @RequestParam String query,
            @RequestParam(required = false) String sceneGroupId,
            @RequestParam(required = false) List<String> knowledgeBaseIds) {
        log.info("[RagController] enhancePrompt: query={}, sceneGroupId={}", query, sceneGroupId);
        try {
            String enhanced = ragPipeline.enhancePromptWithRAG(query, sceneGroupId, knowledgeBaseIds);
            if (enhanced == null) {
                return ResultModel.success("", "未找到相关知识");
            }
            return ResultModel.success(enhanced);
        } catch (Exception e) {
            log.error("[RagController] enhancePrompt failed", e);
            return ResultModel.error("RAG增强失败: " + e.getMessage());
        }
    }

    @PostMapping("/ingest")
    public ResultModel<KnowledgeDocument> ingestBusinessData(
            @RequestBody RagPipeline.BusinessDataIngestRequest request) {
        log.info("[RagController] ingestBusinessData: type={}, title={}",
                request.getDataType(), request.getTitle());
        try {
            KnowledgeDocument doc = ragPipeline.ingestBusinessData(request);
            return ResultModel.success(doc);
        } catch (Exception e) {
            log.error("[RagController] ingestBusinessData failed", e);
            return ResultModel.error("数据摄入失败: " + e.getMessage());
        }
    }

    @GetMapping("/knowledge-config")
    public ResultModel<RagPipeline.RagKnowledgeConfig> buildKnowledgeConfig(
            @RequestParam String sceneGroupId,
            @RequestParam String query) {
        log.info("[RagController] buildKnowledgeConfig: sceneGroupId={}, query={}", sceneGroupId, query);
        try {
            RagPipeline.RagKnowledgeConfig config = ragPipeline.buildKnowledgeConfig(sceneGroupId, query);
            return ResultModel.success(config);
        } catch (Exception e) {
            log.error("[RagController] buildKnowledgeConfig failed", e);
            return ResultModel.error("构建知识配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResultModel<List<KnowledgeDocument>> searchRelated(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int limit) {
        log.info("[RagController] searchRelated: query={}, limit={}", query, limit);
        try {
            List<KnowledgeDocument> docs = ragPipeline.searchRelated(query, limit);
            return ResultModel.success(docs);
        } catch (Exception e) {
            log.error("[RagController] searchRelated failed", e);
            return ResultModel.error("搜索失败: " + e.getMessage());
        }
    }
}

package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.knowledge.LlmKnowledgeConfigDTO;
import net.ooder.mvp.skill.scene.dto.knowledge.LlmKnowledgeConfigDTO.*;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/llm-knowledge-config")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LlmKnowledgeConfigController {

    private static final Logger log = LoggerFactory.getLogger(LlmKnowledgeConfigController.class);
    
    private final Map<String, DictionaryTerm> dictionaryTerms = new ConcurrentHashMap<>();
    private final Map<String, SynonymMapping> synonymMappings = new ConcurrentHashMap<>();
    private final Map<String, LlmInterface> llmInterfaces = new ConcurrentHashMap<>();
    private final Map<String, PromptTemplate> promptTemplates = new ConcurrentHashMap<>();
    
    public LlmKnowledgeConfigController() {
        initDefaultData();
    }
    
    private void initDefaultData() {
        DictionaryTerm term1 = new DictionaryTerm();
        term1.setTerm("API");
        term1.setFullName("Application Programming Interface");
        term1.setDescription("应用程序接口，用于不同软件系统之间的通信");
        term1.setCategory("tech");
        term1.setKbId("kb-tech-docs");
        term1.setKbName("技术文档库");
        dictionaryTerms.put(term1.getTermId(), term1);
        
        DictionaryTerm term2 = new DictionaryTerm();
        term2.setTerm("RAG");
        term2.setFullName("Retrieval-Augmented Generation");
        term2.setDescription("检索增强生成，结合知识检索和LLM生成的技术");
        term2.setCategory("ai");
        term2.setKbId("kb-ai-knowledge");
        term2.setKbName("AI知识库");
        dictionaryTerms.put(term2.getTermId(), term2);
        
        DictionaryTerm term3 = new DictionaryTerm();
        term3.setTerm("LLM");
        term3.setFullName("Large Language Model");
        term3.setDescription("大语言模型，如GPT、Claude等");
        term3.setCategory("ai");
        term3.setKbId("kb-ai-knowledge");
        term3.setKbName("AI知识库");
        dictionaryTerms.put(term3.getTermId(), term3);
        
        SynonymMapping syn1 = new SynonymMapping();
        syn1.setMainTerm("人工智能");
        syn1.setSynonyms(Arrays.asList("AI", "Artificial Intelligence", "智能系统"));
        synonymMappings.put(syn1.getMappingId(), syn1);
        
        SynonymMapping syn2 = new SynonymMapping();
        syn2.setMainTerm("知识库");
        syn2.setSynonyms(Arrays.asList("KB", "Knowledge Base", "知识管理系统"));
        synonymMappings.put(syn2.getMappingId(), syn2);
        
        LlmInterface intf1 = new LlmInterface();
        intf1.setMethod("POST");
        intf1.setPath("/api/v1/knowledge-bases/{kbId}/search");
        intf1.setName("知识检索接口");
        intf1.setDescription("在指定知识库中进行语义检索，返回与查询最相关的文档片段");
        intf1.setLlmPrompt("当用户询问与{知识库名称}相关的问题时，使用此接口检索相关文档。");
        InterfaceParam p1 = new InterfaceParam();
        p1.setName("query"); p1.setType("string"); p1.setRequired(true); p1.setDescription("检索查询文本");
        InterfaceParam p2 = new InterfaceParam();
        p2.setName("topK"); p2.setType("integer"); p2.setRequired(false); p2.setDescription("返回结果数量，默认5");
        InterfaceParam p3 = new InterfaceParam();
        p3.setName("threshold"); p3.setType("float"); p3.setRequired(false); p3.setDescription("相似度阈值，默认0.7");
        intf1.setParameters(Arrays.asList(p1, p2, p3));
        llmInterfaces.put(intf1.getInterfaceId(), intf1);
        
        LlmInterface intf2 = new LlmInterface();
        intf2.setMethod("GET");
        intf2.setPath("/api/v1/knowledge-bases");
        intf2.setName("获取知识库列表");
        intf2.setDescription("获取当前用户可访问的所有知识库列表");
        intf2.setLlmPrompt("当需要了解有哪些知识库可用时，调用此接口获取列表。");
        llmInterfaces.put(intf2.getInterfaceId(), intf2);
        
        LlmInterface intf3 = new LlmInterface();
        intf3.setMethod("POST");
        intf3.setPath("/api/v1/scene-groups/{sceneGroupId}/knowledge/search");
        intf3.setName("场景知识检索");
        intf3.setDescription("在场景绑定的所有知识库中进行跨层检索，按优先级返回结果");
        intf3.setLlmPrompt("在场景对话中，使用此接口检索与场景相关的知识。检索会按场景层→专业层→通用层的优先级进行。");
        InterfaceParam p4 = new InterfaceParam();
        p4.setName("query"); p4.setType("string"); p4.setRequired(true); p4.setDescription("检索查询文本");
        InterfaceParam p5 = new InterfaceParam();
        p5.setName("layers"); p5.setType("array"); p5.setRequired(false); p5.setDescription("指定检索层级：SCENE/PROFESSIONAL/GENERAL");
        InterfaceParam p6 = new InterfaceParam();
        p6.setName("topK"); p6.setType("integer"); p6.setRequired(false); p6.setDescription("返回结果数量，默认5");
        intf3.setParameters(Arrays.asList(p4, p5, p6));
        llmInterfaces.put(intf3.getInterfaceId(), intf3);
        
        PromptTemplate tpl1 = new PromptTemplate();
        tpl1.setName("知识增强回答模板");
        tpl1.setDescription("用于知识检索后的回答生成");
        tpl1.setContent("你是一个专业的知识助手。请根据以下检索到的知识内容回答用户问题。\n\n## 检索到的相关知识：\n{{knowledge_context}}\n\n## 用户问题：\n{{user_query}}\n\n## 回答要求：\n1. 优先使用检索到的知识内容回答问题\n2. 如果知识内容不足以回答问题，请明确说明\n3. 引用知识内容时，标注来源（如：[知识库: XXX]）\n4. 回答要简洁、准确、专业\n\n## 回答：");
        tpl1.setVariables(Arrays.asList("knowledge_context", "user_query", "scene_name", "kb_names", "confidence_score", "conversation_history"));
        tpl1.setDefault(true);
        promptTemplates.put(tpl1.getTemplateId(), tpl1);
    }
    
    // ==================== 字典表管理 ====================
    
    @GetMapping("/dictionaries")
    public ResultModel<List<DictionaryTerm>> listDictionaries(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String kbId) {
        log.info("[listDictionaries] category: {}, kbId: {}", category, kbId);
        
        List<DictionaryTerm> result = dictionaryTerms.values().stream()
            .filter(t -> category == null || category.equals(t.getCategory()))
            .filter(t -> kbId == null || kbId.equals(t.getKbId()))
            .collect(Collectors.toList());
        
        return ResultModel.success(result);
    }
    
    @PostMapping("/dictionaries")
    public ResultModel<DictionaryTerm> addDictionaryTerm(@RequestBody DictionaryTerm request) {
        log.info("[addDictionaryTerm] term: {}", request.getTerm());
        
        if (request.getTerm() == null || request.getTerm().trim().isEmpty()) {
            return ResultModel.error(400, "Term is required");
        }
        
        dictionaryTerms.put(request.getTermId(), request);
        return ResultModel.success(request);
    }
    
    @PutMapping("/dictionaries/{termId}")
    public ResultModel<DictionaryTerm> updateDictionaryTerm(
            @PathVariable String termId,
            @RequestBody DictionaryTerm request) {
        log.info("[updateDictionaryTerm] termId: {}", termId);
        
        DictionaryTerm existing = dictionaryTerms.get(termId);
        if (existing == null) {
            return ResultModel.notFound("Term not found: " + termId);
        }
        
        request.setTermId(termId);
        request.setCreatedAt(existing.getCreatedAt());
        dictionaryTerms.put(termId, request);
        return ResultModel.success(request);
    }
    
    @DeleteMapping("/dictionaries/{termId}")
    public ResultModel<Boolean> deleteDictionaryTerm(@PathVariable String termId) {
        log.info("[deleteDictionaryTerm] termId: {}", termId);
        dictionaryTerms.remove(termId);
        return ResultModel.success(true);
    }
    
    // ==================== 同义词映射 ====================
    
    @GetMapping("/synonyms")
    public ResultModel<List<SynonymMapping>> listSynonyms() {
        log.info("[listSynonyms] request start");
        return ResultModel.success(new ArrayList<>(synonymMappings.values()));
    }
    
    @PostMapping("/synonyms")
    public ResultModel<SynonymMapping> addSynonymMapping(@RequestBody SynonymMapping request) {
        log.info("[addSynonymMapping] mainTerm: {}", request.getMainTerm());
        
        if (request.getMainTerm() == null || request.getMainTerm().trim().isEmpty()) {
            return ResultModel.error(400, "Main term is required");
        }
        
        synonymMappings.put(request.getMappingId(), request);
        return ResultModel.success(request);
    }
    
    @PutMapping("/synonyms/{mappingId}")
    public ResultModel<SynonymMapping> updateSynonymMapping(
            @PathVariable String mappingId,
            @RequestBody SynonymMapping request) {
        log.info("[updateSynonymMapping] mappingId: {}", mappingId);
        
        SynonymMapping existing = synonymMappings.get(mappingId);
        if (existing == null) {
            return ResultModel.notFound("Mapping not found: " + mappingId);
        }
        
        request.setMappingId(mappingId);
        request.setCreatedAt(existing.getCreatedAt());
        synonymMappings.put(mappingId, request);
        return ResultModel.success(request);
    }
    
    @DeleteMapping("/synonyms/{mappingId}")
    public ResultModel<Boolean> deleteSynonymMapping(@PathVariable String mappingId) {
        log.info("[deleteSynonymMapping] mappingId: {}", mappingId);
        synonymMappings.remove(mappingId);
        return ResultModel.success(true);
    }
    
    // ==================== LLM接口定义 ====================
    
    @GetMapping("/interfaces")
    public ResultModel<List<LlmInterface>> listInterfaces() {
        log.info("[listInterfaces] request start");
        return ResultModel.success(new ArrayList<>(llmInterfaces.values()));
    }
    
    @PostMapping("/interfaces")
    public ResultModel<LlmInterface> addInterface(@RequestBody LlmInterface request) {
        log.info("[addInterface] name: {}, path: {}", request.getName(), request.getPath());
        
        if (request.getPath() == null || request.getPath().trim().isEmpty()) {
            return ResultModel.error(400, "Path is required");
        }
        
        llmInterfaces.put(request.getInterfaceId(), request);
        return ResultModel.success(request);
    }
    
    @PutMapping("/interfaces/{interfaceId}")
    public ResultModel<LlmInterface> updateInterface(
            @PathVariable String interfaceId,
            @RequestBody LlmInterface request) {
        log.info("[updateInterface] interfaceId: {}", interfaceId);
        
        LlmInterface existing = llmInterfaces.get(interfaceId);
        if (existing == null) {
            return ResultModel.notFound("Interface not found: " + interfaceId);
        }
        
        request.setInterfaceId(interfaceId);
        request.setCreatedAt(existing.getCreatedAt());
        llmInterfaces.put(interfaceId, request);
        return ResultModel.success(request);
    }
    
    @DeleteMapping("/interfaces/{interfaceId}")
    public ResultModel<Boolean> deleteInterface(@PathVariable String interfaceId) {
        log.info("[deleteInterface] interfaceId: {}", interfaceId);
        llmInterfaces.remove(interfaceId);
        return ResultModel.success(true);
    }
    
    // ==================== 提示词模板 ====================
    
    @GetMapping("/prompt-templates")
    public ResultModel<List<PromptTemplate>> listPromptTemplates() {
        log.info("[listPromptTemplates] request start");
        return ResultModel.success(new ArrayList<>(promptTemplates.values()));
    }
    
    @GetMapping("/prompt-templates/default")
    public ResultModel<PromptTemplate> getDefaultPromptTemplate() {
        log.info("[getDefaultPromptTemplate] request start");
        
        PromptTemplate defaultTemplate = promptTemplates.values().stream()
            .filter(PromptTemplate::isDefault)
            .findFirst()
            .orElse(null);
        
        if (defaultTemplate == null) {
            return ResultModel.notFound("No default template found");
        }
        
        return ResultModel.success(defaultTemplate);
    }
    
    @PostMapping("/prompt-templates")
    public ResultModel<PromptTemplate> addPromptTemplate(@RequestBody PromptTemplate request) {
        log.info("[addPromptTemplate] name: {}", request.getName());
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResultModel.error(400, "Name is required");
        }
        
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return ResultModel.error(400, "Content is required");
        }
        
        List<String> variables = extractVariables(request.getContent());
        request.setVariables(variables);
        
        promptTemplates.put(request.getTemplateId(), request);
        return ResultModel.success(request);
    }
    
    @PutMapping("/prompt-templates/{templateId}")
    public ResultModel<PromptTemplate> updatePromptTemplate(
            @PathVariable String templateId,
            @RequestBody PromptTemplate request) {
        log.info("[updatePromptTemplate] templateId: {}", templateId);
        
        PromptTemplate existing = promptTemplates.get(templateId);
        if (existing == null) {
            return ResultModel.notFound("Template not found: " + templateId);
        }
        
        request.setTemplateId(templateId);
        request.setCreatedAt(existing.getCreatedAt());
        request.setUpdatedAt(new Date());
        
        List<String> variables = extractVariables(request.getContent());
        request.setVariables(variables);
        
        promptTemplates.put(templateId, request);
        return ResultModel.success(request);
    }
    
    @DeleteMapping("/prompt-templates/{templateId}")
    public ResultModel<Boolean> deletePromptTemplate(@PathVariable String templateId) {
        log.info("[deletePromptTemplate] templateId: {}", templateId);
        
        PromptTemplate template = promptTemplates.get(templateId);
        if (template != null && template.isDefault()) {
            return ResultModel.error(400, "Cannot delete default template");
        }
        
        promptTemplates.remove(templateId);
        return ResultModel.success(true);
    }
    
    @PostMapping("/prompt-templates/{templateId}/set-default")
    public ResultModel<Boolean> setDefaultPromptTemplate(@PathVariable String templateId) {
        log.info("[setDefaultPromptTemplate] templateId: {}", templateId);
        
        promptTemplates.values().forEach(t -> t.setDefault(false));
        
        PromptTemplate template = promptTemplates.get(templateId);
        if (template == null) {
            return ResultModel.notFound("Template not found: " + templateId);
        }
        
        template.setDefault(true);
        return ResultModel.success(true);
    }
    
    private List<String> extractVariables(String content) {
        List<String> variables = new ArrayList<>();
        if (content == null) return variables;
        
        int start = 0;
        while ((start = content.indexOf("{{", start)) != -1) {
            int end = content.indexOf("}}", start);
            if (end != -1) {
                String var = content.substring(start + 2, end).trim();
                if (!variables.contains(var)) {
                    variables.add(var);
                }
                start = end + 2;
            } else {
                break;
            }
        }
        
        return variables;
    }
}

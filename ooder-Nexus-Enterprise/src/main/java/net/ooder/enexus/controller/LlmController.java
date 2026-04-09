package net.ooder.enexus.controller;

import net.ooder.enexus.model.ResultModel;
import net.ooder.enexus.llm.service.LlmProviderManager;
import net.ooder.enexus.dto.llm.*;
import net.ooder.enexus.dto.llm.LlmProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * LLM Controller - 已废弃，请迁移到 /api/v2/messaging/messages
 * @deprecated since 2.0.0, forRemoval = true
 * 迁移截止日期: 2025-10-01
 * @see net.ooder.skill.messaging.controller.UnifiedMessagingController
 */
@Deprecated(since = "2.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/llm")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class LlmController {

    private static final Logger log = LoggerFactory.getLogger(LlmController.class);

    @Autowired(required = false)
    private LlmProviderManager llmProviderManager;
    
    @Value("${ooder.llm.provider:qianwen}")
    private String defaultProvider;
    
    @Value("${ooder.llm.model:qwen-plus}")
    private String defaultModel;

    @GetMapping("/models")
    public ResultModel<LlmModelsDTO> getModels() {
        log.info("[LlmController] Getting available models");
        
        if (llmProviderManager == null) {
            log.warn("[LlmController] LLM Provider Manager not available, returning default models");
            return ResultModel.success(getDefaultModels());
        }
        
        try {
            LlmModelsDTO result = new LlmModelsDTO();
            
            List<String> providerIds = new ArrayList<>();
            Map<String, List<String>> modelsByProvider = new HashMap<>();
            List<LlmProviderDTO> providerDetails = new ArrayList<>();
            
            LlmProviderDTO qianwen = new LlmProviderDTO();
            qianwen.setId("qianwen");
            qianwen.setName("通义千问");
            qianwen.setEnabled(true);
            qianwen.setModels(Arrays.asList("qwen-turbo", "qwen-plus", "qwen-max"));
            providerDetails.add(qianwen);
            providerIds.add("qianwen");
            modelsByProvider.put("qianwen", Arrays.asList("qwen-turbo", "qwen-plus", "qwen-max"));
            
            LlmProviderDTO deepseek = new LlmProviderDTO();
            deepseek.setId("deepseek");
            deepseek.setName("DeepSeek");
            deepseek.setEnabled(true);
            deepseek.setModels(Arrays.asList("deepseek-chat", "deepseek-coder"));
            providerDetails.add(deepseek);
            providerIds.add("deepseek");
            modelsByProvider.put("deepseek", Arrays.asList("deepseek-chat", "deepseek-coder"));
            
            LlmProviderDTO baidu = new LlmProviderDTO();
            baidu.setId("baidu");
            baidu.setName("百度文心");
            baidu.setEnabled(false);
            baidu.setModels(Arrays.asList("ernie-4.5-8k-preview", "ernie-speed-8k"));
            providerDetails.add(baidu);
            providerIds.add("baidu");
            modelsByProvider.put("baidu", Arrays.asList("ernie-4.5-8k-preview", "ernie-speed-8k"));
            
            result.setProviders(providerIds);
            result.setModelsByProvider(modelsByProvider);
            result.setProviderDetails(providerDetails);
            result.setCurrentProvider(defaultProvider);
            result.setCurrentModel(llmProviderManager.getDefaultModel());
            
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("[LlmController] Failed to get models: {}", e.getMessage());
            return ResultModel.success(getDefaultModels());
        }
    }
    
    @PostMapping("/models")
    public ResultModel<LlmModelsDTO> loadProviders() {
        log.info("[LlmController] Loading providers (POST /models)");
        return getModels();
    }
    
    private LlmModelsDTO getDefaultModels() {
        LlmModelsDTO result = new LlmModelsDTO();
        
        List<String> providerIds = new ArrayList<>();
        Map<String, List<String>> modelsByProvider = new HashMap<>();
        List<LlmProviderDTO> providerDetails = new ArrayList<>();
        
        LlmProviderDTO qianwen = new LlmProviderDTO();
        qianwen.setId("qianwen");
        qianwen.setName("通义千问");
        qianwen.setEnabled(true);
        qianwen.setModels(Arrays.asList("qwen-turbo", "qwen-plus", "qwen-max"));
        providerDetails.add(qianwen);
        providerIds.add("qianwen");
        modelsByProvider.put("qianwen", Arrays.asList("qwen-turbo", "qwen-plus", "qwen-max"));
        
        LlmProviderDTO deepseek = new LlmProviderDTO();
        deepseek.setId("deepseek");
        deepseek.setName("DeepSeek");
        deepseek.setEnabled(true);
        deepseek.setModels(Arrays.asList("deepseek-chat", "deepseek-coder"));
        providerDetails.add(deepseek);
        providerIds.add("deepseek");
        modelsByProvider.put("deepseek", Arrays.asList("deepseek-chat", "deepseek-coder"));
        
        LlmProviderDTO baidu = new LlmProviderDTO();
        baidu.setId("baidu");
        baidu.setName("百度文心");
        baidu.setEnabled(false);
        baidu.setModels(Arrays.asList("ernie-4.5-8k-preview", "ernie-speed-8k"));
        providerDetails.add(baidu);
        providerIds.add("baidu");
        modelsByProvider.put("baidu", Arrays.asList("ernie-4.5-8k-preview", "ernie-speed-8k"));
        
        result.setProviders(providerIds);
        result.setModelsByProvider(modelsByProvider);
        result.setProviderDetails(providerDetails);
        result.setCurrentProvider(defaultProvider);
        result.setCurrentModel(defaultModel);
        
        return result;
    }
    
    private List<LlmProviderDTO> getProviderDTOList() {
        List<LlmProviderDTO> providers = new ArrayList<>();
        
        LlmProviderDTO qianwen = new LlmProviderDTO();
        qianwen.setId("qianwen");
        qianwen.setName("通义千问");
        qianwen.setEnabled(true);
        providers.add(qianwen);
        
        LlmProviderDTO deepseek = new LlmProviderDTO();
        deepseek.setId("deepseek");
        deepseek.setName("DeepSeek");
        deepseek.setEnabled(true);
        providers.add(deepseek);
        
        LlmProviderDTO baidu = new LlmProviderDTO();
        baidu.setId("baidu");
        baidu.setName("百度文心");
        baidu.setEnabled(false);
        providers.add(baidu);
        
        return providers;
    }
    
    @GetMapping("/providers")
    public ResultModel<List<LlmProviderDTO>> getProviders() {
        log.info("[LlmController] Getting available providers");
        
        if (llmProviderManager == null) {
            log.warn("[LlmController] LLM Provider Manager not available, returning default providers");
            return ResultModel.success(getProviderDTOList());
        }
        
        return ResultModel.success(getProviderDTOList());
    }
    
    @GetMapping("/config")
    public ResultModel<LlmConfigDTO> getConfig() {
        if (llmProviderManager == null) {
            LlmConfigDTO defaultConfig = new LlmConfigDTO();
            defaultConfig.setProvider(defaultProvider);
            defaultConfig.setModel(defaultModel);
            defaultConfig.setTemperature(0.7);
            defaultConfig.setMaxTokens(4096);
            defaultConfig.setStreamEnabled(true);
            return ResultModel.success(defaultConfig);
        }
        
        LlmConfigDTO config = new LlmConfigDTO();
        config.setProvider(defaultProvider);
        config.setModel(llmProviderManager.getDefaultModel());
        config.setTemperature(0.7);
        config.setMaxTokens(4096);
        config.setStreamEnabled(true);
        
        return ResultModel.success(config);
    }
    
    @PutMapping("/config")
    public ResultModel<Void> updateConfig(@RequestBody LlmConfigDTO configDTO) {
        log.info("[LlmController] Updating config: {}", configDTO);
        
        if (configDTO.getProvider() != null) {
            defaultProvider = configDTO.getProvider();
        }
        if (configDTO.getModel() != null) {
            defaultModel = configDTO.getModel();
        }
        
        return ResultModel.success(null);
    }
    
    @GetMapping("/providers/{providerId}/models")
    public ResultModel<List<String>> getProviderModels(@PathVariable String providerId) {
        if (llmProviderManager == null) {
            List<String> defaultModels = new ArrayList<>();
            if ("qianwen".equals(providerId)) {
                defaultModels.add("qwen-turbo");
                defaultModels.add("qwen-plus");
                defaultModels.add("qwen-max");
            } else if ("deepseek".equals(providerId)) {
                defaultModels.add("deepseek-chat");
                defaultModels.add("deepseek-coder");
            } else if ("baidu".equals(providerId)) {
                defaultModels.add("ernie-4.5-8k-preview");
                defaultModels.add("ernie-speed-8k");
            }
            return ResultModel.success(defaultModels);
        }
        
        net.ooder.scene.skill.LlmProvider provider = llmProviderManager.getProvider(providerId);
        if (provider != null) {
            return ResultModel.success(provider.getSupportedModels());
        }
        
        return ResultModel.success(new ArrayList<>());
    }
    
    @GetMapping("/health")
    public ResultModel<LlmHealthDTO> health() {
        log.info("[LlmController] LLM Health check");
        
        LlmHealthDTO result = new LlmHealthDTO();
        result.setHealthy(llmProviderManager != null);
        result.setCurrentProvider(defaultProvider);
        result.setCurrentModel(defaultModel);
        result.setProviderManagerAvailable(llmProviderManager != null);
        
        return ResultModel.success(result);
    }
    
    @GetMapping("/providers/info")
    public ResultModel<List<LlmProviderDTO>> getProvidersInfo() {
        log.info("[LlmController] Getting providers info from LlmProviderType enum");
        
        List<LlmProviderDTO> providerList = new ArrayList<>();
        
        for (LlmProviderType providerType : LlmProviderType.values()) {
            LlmProviderDTO provider = new LlmProviderDTO();
            provider.setCode(providerType.getCode());
            provider.setName(providerType.getDisplayName());
            provider.setDisplayName(providerType.getDisplayName());
            provider.setDescription(providerType.getDescription());
            provider.setDefaultBaseUrl(providerType.getDefaultBaseUrl());
            provider.setIsCurrent(providerType.getCode().equals(defaultProvider));
            provider.setIsConfigured(false);
            
            List<LlmModelDTO> modelDetails = new ArrayList<>();
            List<String> modelIds = new ArrayList<>();
            
            for (LlmProviderType.ModelInfo modelInfo : providerType.getModels()) {
                LlmModelDTO model = new LlmModelDTO();
                model.setModelId(modelInfo.getModelId());
                model.setDisplayName(modelInfo.getDisplayName());
                model.setMaxTokens(modelInfo.getMaxTokens());
                model.setDefaultTemperature(modelInfo.getDefaultTemperature());
                model.setSupportsFunctionCalling(modelInfo.isSupportsFunctionCalling());
                model.setSupportsMultimodal(modelInfo.isSupportsMultimodal());
                model.setSupportsEmbedding(modelInfo.isSupportsEmbedding());
                model.setCostPer1kTokens(modelInfo.getCostPer1kTokens());
                modelDetails.add(model);
                modelIds.add(modelInfo.getModelId());
            }
            
            provider.setModelDetails(modelDetails);
            provider.setModels(modelIds);
            provider.setModelCount(modelDetails.size());
            providerList.add(provider);
        }
        
        log.info("[LlmController] Returning {} providers", providerList.size());
        return ResultModel.success(providerList);
    }
    
    private LlmModelDTO createModelInfo(String modelId, String displayName, int maxTokens, 
                                                  double defaultTemperature, boolean supportsFunctionCalling,
                                                  boolean supportsMultimodal, boolean supportsEmbedding, 
                                                  double costPer1kTokens) {
        LlmModelDTO model = new LlmModelDTO();
        model.setModelId(modelId);
        model.setDisplayName(displayName);
        model.setMaxTokens(maxTokens);
        model.setDefaultTemperature(defaultTemperature);
        model.setSupportsFunctionCalling(supportsFunctionCalling);
        model.setSupportsMultimodal(supportsMultimodal);
        model.setSupportsEmbedding(supportsEmbedding);
        model.setCostPer1kTokens(costPer1kTokens);
        return model;
    }
    
    @GetMapping("/tools")
    public ResultModel<List<LlmToolDTO>> getTools() {
        log.info("[LlmController] Getting tools");
        
        List<LlmToolDTO> tools = new ArrayList<>();
        
        LlmToolDTO tool1 = new LlmToolDTO();
        tool1.setName("search_knowledge");
        tool1.setType("function");
        tool1.setDescription("搜索知识库内容");
        Map<String, Object> tool1Params = new HashMap<>();
        tool1Params.put("query", "string");
        tool1Params.put("limit", "integer");
        tool1.setParameters(tool1Params);
        tools.add(tool1);
        
        LlmToolDTO tool2 = new LlmToolDTO();
        tool2.setName("get_skill_info");
        tool2.setType("function");
        tool2.setDescription("获取技能详细信息");
        Map<String, Object> tool2Params = new HashMap<>();
        tool2Params.put("skillId", "string");
        tool2.setParameters(tool2Params);
        tools.add(tool2);
        
        LlmToolDTO tool3 = new LlmToolDTO();
        tool3.setName("execute_scene");
        tool3.setType("function");
        tool3.setDescription("执行场景操作");
        Map<String, Object> tool3Params = new HashMap<>();
        tool3Params.put("sceneId", "string");
        tool3Params.put("action", "string");
        tool3.setParameters(tool3Params);
        tools.add(tool3);
        
        return ResultModel.success(tools);
    }
    
    @GetMapping("/tools/{name}")
    public ResultModel<LlmToolDTO> getToolDetail(@PathVariable String name) {
        log.info("[LlmController] Getting tool detail: {}", name);
        
        LlmToolDTO tool = new LlmToolDTO();
        
        if ("search_knowledge".equals(name)) {
            tool.setName("search_knowledge");
            tool.setType("function");
            tool.setDescription("搜索知识库内容，返回相关知识条目");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("type", "object");
            Map<String, Object> properties = new HashMap<>();
            Map<String, Object> queryProp = new HashMap<>();
            queryProp.put("type", "string");
            queryProp.put("description", "搜索查询字符串");
            properties.put("query", queryProp);
            Map<String, Object> limitProp = new HashMap<>();
            limitProp.put("type", "integer");
            limitProp.put("description", "返回结果数量限制");
            limitProp.put("default", 5);
            properties.put("limit", limitProp);
            parameters.put("properties", properties);
            parameters.put("required", Arrays.asList("query"));
            tool.setParameters(parameters);
        } else if ("get_skill_info".equals(name)) {
            tool.setName("get_skill_info");
            tool.setType("function");
            tool.setDescription("获取指定技能的详细信息和配置");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("type", "object");
            Map<String, Object> properties = new HashMap<>();
            Map<String, Object> skillIdProp = new HashMap<>();
            skillIdProp.put("type", "string");
            skillIdProp.put("description", "技能ID");
            properties.put("skillId", skillIdProp);
            parameters.put("properties", properties);
            parameters.put("required", Arrays.asList("skillId"));
            tool.setParameters(parameters);
        } else if ("execute_scene".equals(name)) {
            tool.setName("execute_scene");
            tool.setType("function");
            tool.setDescription("在指定场景中执行操作");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("type", "object");
            Map<String, Object> properties = new HashMap<>();
            Map<String, Object> sceneIdProp = new HashMap<>();
            sceneIdProp.put("type", "string");
            sceneIdProp.put("description", "场景ID");
            properties.put("sceneId", sceneIdProp);
            Map<String, Object> actionProp = new HashMap<>();
            actionProp.put("type", "string");
            actionProp.put("description", "要执行的操作");
            properties.put("action", actionProp);
            parameters.put("properties", properties);
            parameters.put("required", Arrays.asList("sceneId", "action"));
            tool.setParameters(parameters);
        } else {
            return ResultModel.notFound("Tool not found: " + name);
        }
        
        return ResultModel.success(tool);
    }
    
    @GetMapping("/docs")
    public ResultModel<List<LlmDocDTO>> getDocs() {
        log.info("[LlmController] Getting docs");
        
        List<LlmDocDTO> docs = new ArrayList<>();
        
        LlmDocDTO doc1 = new LlmDocDTO();
        doc1.setTitle("快速开始");
        doc1.setContent("# 快速开始\n\n欢迎使用LLM配置功能。\n\n## 基本步骤\n\n1. 选择Provider（如通义千问、DeepSeek等）\n2. 配置API密钥\n3. 选择模型\n4. 调整参数（温度、最大token等）\n5. 保存配置\n\n## 支持的Provider\n\n- **通义千问**：阿里云大模型，支持中文优化\n- **DeepSeek**：开源大模型，支持代码生成\n- **百度文心**：百度大模型，中文理解能力强");
        docs.add(doc1);
        
        LlmDocDTO doc2 = new LlmDocDTO();
        doc2.setTitle("配置说明");
        doc2.setContent("# 配置说明\n\n## 温度参数\n\n温度参数控制输出的随机性：\n- **低温度（0.1-0.3）**：输出更确定、更保守\n- **中温度（0.4-0.7）**：平衡创造性和确定性\n- **高温度（0.8-1.0）**：输出更随机、更有创造性\n\n## 最大Token\n\n限制模型生成的最大token数量。1个token约等于4个英文字符或1个中文字符。\n\n## 流式输出\n\n启用流式输出可以实时接收模型响应，提升用户体验。");
        docs.add(doc2);
        
        LlmDocDTO doc3 = new LlmDocDTO();
        doc3.setTitle("Function Calling");
        doc3.setContent("# Function Calling\n\nFunction Calling允许LLM调用外部工具和功能。\n\n## 可用的Tools\n\n- **search_knowledge**：搜索知识库\n- **get_skill_info**：获取技能信息\n- **execute_scene**：执行场景操作\n\n## 使用方式\n\n在对话中，LLM会根据用户输入自动判断是否需要调用工具。当需要调用时，系统会：\n1. 解析工具名称和参数\n2. 执行相应操作\n3. 将结果返回给LLM\n4. LLM生成最终回复");
        docs.add(doc3);
        
        return ResultModel.success(docs);
    }

    @GetMapping("/prompt-templates")
    public ResultModel<List<LlmPromptTemplateDTO>> getPromptTemplates() {
        log.info("[LlmController] Getting prompt templates");
        
        List<LlmPromptTemplateDTO> templates = new ArrayList<>();
        
        LlmPromptTemplateDTO template1 = new LlmPromptTemplateDTO();
        template1.setId("default");
        template1.setName("默认模板");
        template1.setTemplate("你是一个有帮助的AI助手。请根据用户的问题提供准确、有帮助的回答。");
        template1.setCategory("general");
        templates.add(template1);
        
        LlmPromptTemplateDTO template2 = new LlmPromptTemplateDTO();
        template2.setId("code-assistant");
        template2.setName("代码助手");
        template2.setTemplate("你是一个专业的编程助手。请帮助用户解决编程问题，提供清晰的代码示例和解释。");
        template2.setCategory("development");
        templates.add(template2);
        
        LlmPromptTemplateDTO template3 = new LlmPromptTemplateDTO();
        template3.setId("business-analyst");
        template3.setName("业务分析师");
        template3.setTemplate("你是一个业务分析专家。请帮助用户分析业务需求，提供专业的建议和解决方案。");
        template3.setCategory("business");
        templates.add(template3);
        
        return ResultModel.success(templates);
    }
}

package net.ooder.skill.chat.service.impl;

import net.ooder.skill.chat.service.KnowledgeService;
import net.ooder.skill.chat.service.SkillsContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Skills Context Service Implementation
 * Skills 上下文服务实现
 * 
 * 上下文分层结构：
 * 1. 用户层 - 用户身份、角色、权限
 * 2. 会话层 - 当前会话状态、历史消息
 * 3. 模块层 - 当前模块功能、可用API
 * 4. 知识层 - 知识库内容、文档
 */
public class SkillsContextServiceImpl implements SkillsContextService {

    private static final Logger log = LoggerFactory.getLogger(SkillsContextServiceImpl.class);

    private final KnowledgeService knowledgeService;

    private final Map<String, Map<String, Object>> userContexts = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> sessionContexts = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> skillContexts = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> pageStates = new ConcurrentHashMap<>();

    public SkillsContextServiceImpl(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
        initializeDefaultSkillContexts();
    }

    private void initializeDefaultSkillContexts() {
        registerSkillContext("skill-capability", createCapabilitySkillContext());
        registerSkillContext("skill-llm", createLlmSkillContext());
        registerSkillContext("skill-llm-chat", createChatSkillContext());
        registerSkillContext("skill-scene-management", createSceneManagementContext());
        
        log.info("[SkillsContextService] Initialized {} skill contexts", skillContexts.size());
    }

    private Map<String, Object> createCapabilitySkillContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("skillId", "skill-capability");
        context.put("name", "能力管理");
        context.put("description", "管理系统能力，包括发现、安装、绑定、激活等操作");
        context.put("category", "capability");
        
        List<Map<String, Object>> apis = new ArrayList<>();
        
        Map<String, Object> discoveryApi = new HashMap<>();
        discoveryApi.put("name", "discoverCapabilities");
        discoveryApi.put("description", "发现新能力");
        discoveryApi.put("parameters", Arrays.asList(
            createParam("method", "string", "发现方式(LOCAL/GITHUB/GITEE)", true),
            createParam("keyword", "string", "搜索关键词", false)
        ));
        apis.add(discoveryApi);
        
        Map<String, Object> installApi = new HashMap<>();
        installApi.put("name", "installCapability");
        installApi.put("description", "安装能力");
        installApi.put("parameters", Arrays.asList(
            createParam("capabilityId", "string", "能力ID", true),
            createParam("config", "object", "安装配置", false)
        ));
        apis.add(installApi);
        
        Map<String, Object> bindApi = new HashMap<>();
        bindApi.put("name", "bindCapability");
        bindApi.put("description", "绑定能力到Agent");
        bindApi.put("parameters", Arrays.asList(
            createParam("capabilityId", "string", "能力ID", true),
            createParam("agentId", "string", "Agent ID", true)
        ));
        apis.add(bindApi);
        
        context.put("availableApis", apis);
        return context;
    }

    private Map<String, Object> createLlmSkillContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("skillId", "skill-llm");
        context.put("name", "LLM配置");
        context.put("description", "配置和管理LLM提供者");
        context.put("category", "ai");
        
        List<Map<String, Object>> apis = new ArrayList<>();
        
        Map<String, Object> configApi = new HashMap<>();
        configApi.put("name", "configureProvider");
        configApi.put("description", "配置LLM提供者");
        configApi.put("parameters", Arrays.asList(
            createParam("providerId", "string", "提供者ID", true),
            createParam("apiKey", "string", "API密钥", true),
            createParam("baseUrl", "string", "API地址", false)
        ));
        apis.add(configApi);
        
        Map<String, Object> testApi = new HashMap<>();
        testApi.put("name", "testConnection");
        testApi.put("description", "测试LLM连接");
        testApi.put("parameters", Arrays.asList(
            createParam("providerId", "string", "提供者ID", true)
        ));
        apis.add(testApi);
        
        context.put("availableApis", apis);
        return context;
    }

    private Map<String, Object> createChatSkillContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("skillId", "skill-llm-chat");
        context.put("name", "LLM聊天助手");
        context.put("description", "AI聊天助手，支持知识库检索");
        context.put("category", "ai");
        
        List<Map<String, Object>> apis = new ArrayList<>();
        
        Map<String, Object> chatApi = new HashMap<>();
        chatApi.put("name", "sendMessage");
        chatApi.put("description", "发送消息给AI助手");
        chatApi.put("parameters", Arrays.asList(
            createParam("content", "string", "消息内容", true),
            createParam("useKnowledge", "boolean", "是否使用知识库", false)
        ));
        apis.add(chatApi);
        
        Map<String, Object> uploadApi = new HashMap<>();
        uploadApi.put("name", "uploadDocument");
        uploadApi.put("description", "上传知识库文档");
        uploadApi.put("parameters", Arrays.asList(
            createParam("title", "string", "文档标题", true),
            createParam("content", "string", "文档内容", true)
        ));
        apis.add(uploadApi);
        
        context.put("availableApis", apis);
        return context;
    }

    private Map<String, Object> createSceneManagementContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("skillId", "skill-scene-management");
        context.put("name", "场景管理");
        context.put("description", "创建和管理场景");
        context.put("category", "scene");
        
        List<Map<String, Object>> apis = new ArrayList<>();
        
        Map<String, Object> createApi = new HashMap<>();
        createApi.put("name", "createScene");
        createApi.put("description", "创建新场景");
        createApi.put("parameters", Arrays.asList(
            createParam("name", "string", "场景名称", true),
            createParam("type", "string", "场景类型", false)
        ));
        apis.add(createApi);
        
        Map<String, Object> startApi = new HashMap<>();
        startApi.put("name", "startScene");
        startApi.put("description", "启动场景");
        startApi.put("parameters", Arrays.asList(
            createParam("sceneId", "string", "场景ID", true)
        ));
        apis.add(startApi);
        
        context.put("availableApis", apis);
        return context;
    }

    private Map<String, Object> createParam(String name, String type, String description, boolean required) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("type", type);
        param.put("description", description);
        param.put("required", required);
        return param;
    }

    @Override
    public void initializeContext(String userId, String sessionId) {
        Map<String, Object> userContext = new HashMap<>();
        userContext.put("userId", userId);
        userContext.put("initializedAt", new Date());
        userContexts.put(userId, userContext);

        Map<String, Object> sessionContext = new HashMap<>();
        sessionContext.put("sessionId", sessionId);
        sessionContext.put("userId", userId);
        sessionContext.put("createdAt", new Date());
        sessionContext.put("currentModule", "default");
        sessionContext.put("messageCount", 0);
        sessionContexts.put(sessionId, sessionContext);

        log.info("[initializeContext] Initialized context for user: {}, session: {}", userId, sessionId);
    }

    @Override
    public Map<String, Object> getUserIdentity(String userId) {
        Map<String, Object> identity = new HashMap<>();
        
        Map<String, Object> userContext = userContexts.get(userId);
        if (userContext != null) {
            identity.put("userId", userId);
            identity.put("roles", Arrays.asList("user"));
            identity.put("permissions", Arrays.asList("chat", "knowledge"));
        } else {
            identity.put("userId", "anonymous");
            identity.put("roles", Arrays.asList("guest"));
            identity.put("permissions", Arrays.asList("chat"));
        }
        
        return identity;
    }

    @Override
    public List<Map<String, Object>> getModuleInfo(String skillId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Object> skillContext = skillContexts.get(skillId);
        if (skillContext != null) {
            result.add(skillContext);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getCurrentModuleContext(String sessionId) {
        Map<String, Object> sessionContext = sessionContexts.get(sessionId);
        if (sessionContext == null) {
            return new HashMap<>();
        }
        
        String currentModule = (String) sessionContext.get("currentModule");
        if (currentModule == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> context = new HashMap<>();
        context.put("module", currentModule);
        context.put("skillContext", skillContexts.get(currentModule));
        context.put("pageState", pageStates.get(sessionId + ":" + currentModule));
        
        return context;
    }

    @Override
    public void registerSkillContext(String skillId, Map<String, Object> skillInfo) {
        skillContexts.put(skillId, skillInfo);
        log.info("[registerSkillContext] Registered skill context: {}", skillId);
    }

    @Override
    public void updatePageState(String sessionId, String module, Map<String, Object> state) {
        String key = sessionId + ":" + module;
        pageStates.put(key, state);
        log.debug("[updatePageState] Updated page state for session: {}, module: {}", sessionId, module);
    }

    @Override
    public Map<String, Object> getPageState(String sessionId, String module) {
        String key = sessionId + ":" + module;
        return pageStates.getOrDefault(key, new HashMap<>());
    }

    @Override
    public List<String> getKnowledgeContext(String query, int limit) {
        if (knowledgeService == null) {
            return new ArrayList<>();
        }
        return knowledgeService.search(query, limit);
    }

    @Override
    public String buildSystemPrompt(String userId, String skillId) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("你是 Ooder 平台的 AI 助手。\n\n");
        
        Map<String, Object> identity = getUserIdentity(userId);
        prompt.append("## 当前用户\n");
        prompt.append("- 用户ID: ").append(identity.get("userId")).append("\n");
        prompt.append("- 角色: ").append(identity.get("roles")).append("\n\n");
        
        prompt.append("## 平台功能\n");
        prompt.append("Ooder 是一个技能管理平台，支持以下功能模块：\n\n");
        
        for (Map.Entry<String, Map<String, Object>> entry : skillContexts.entrySet()) {
            Map<String, Object> skill = entry.getValue();
            prompt.append("### ").append(skill.get("name")).append("\n");
            prompt.append(skill.get("description")).append("\n");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> apis = (List<Map<String, Object>>) skill.get("availableApis");
            if (apis != null && !apis.isEmpty()) {
                prompt.append("可用方法：\n");
                for (Map<String, Object> api : apis) {
                    prompt.append("- ").append(api.get("name")).append(": ").append(api.get("description")).append("\n");
                }
            }
            prompt.append("\n");
        }
        
        if (skillId != null && skillContexts.containsKey(skillId)) {
            prompt.append("## 当前模块\n");
            prompt.append("用户正在使用: ").append(skillId).append("\n\n");
        }
        
        prompt.append("## 知识库\n");
        prompt.append("你可以查询知识库获取相关信息。当用户询问具体问题时，请先搜索知识库。\n\n");
        
        prompt.append("## 回复规则\n");
        prompt.append("1. 用中文回复\n");
        prompt.append("2. 保持简洁专业\n");
        prompt.append("3. 如果需要执行操作，请明确告知用户\n");
        
        return prompt.toString();
    }
}

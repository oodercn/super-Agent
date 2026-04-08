package net.ooder.mvp.skill.scene.llm.context;

import net.ooder.scene.skill.rag.RagApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MultiLevelContextManagerImpl implements MultiLevelContextManager {

    private static final Logger log = LoggerFactory.getLogger(MultiLevelContextManagerImpl.class);

    @Autowired(required = false)
    private RagApi ragApi;

    @Autowired(required = false)
    private MenuKnowledgeInitService menuKnowledgeInitService;

    private GlobalContextConfig globalConfig;
    private String currentSkillId;
    private String currentPageId;
    private String currentSessionId;
    private final Map<String, List<Map<String, Object>>> sessionHistory = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> skillContexts = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> pageContexts = new ConcurrentHashMap<>();

    private boolean initialized = false;

    @PostConstruct
    public void init() {
        log.info("[MultiLevelContextManager] Initializing...");
        initialized = true;
    }

    @Override
    public void initializeGlobalContext(GlobalContextConfig config) {
        this.globalConfig = config;
        log.info("[MultiLevelContextManager] Global context initialized with {} menu items", 
            config != null && config.getMenuItems() != null ? config.getMenuItems().size() : 0);
    }

    @Override
    public Map<String, Object> getCurrentContext() {
        Map<String, Object> context = new LinkedHashMap<>();
        
        context.put("level", getContextLevel());
        context.put("global", getGlobalContextInfo());
        context.put("skill", getSkillContextInfo());
        context.put("page", getPageContextInfo());
        context.put("session", getSessionContextInfo());
        
        return context;
    }

    @Override
    public void reloadContextForPage(String pageId) {
        log.info("[MultiLevelContextManager] Reloading context for page: {}", pageId);
        this.currentPageId = pageId;
        
        String skillId = getSkillIdForPage(pageId);
        if (skillId != null && !skillId.equals(currentSkillId)) {
            reloadContextForSkill(skillId);
        }
        
        Map<String, Object> pageContext = new LinkedHashMap<>();
        pageContext.put("pageId", pageId);
        pageContext.put("skillId", skillId);
        pageContext.put("loadTime", System.currentTimeMillis());
        pageContexts.put(pageId, pageContext);
        
        log.info("[MultiLevelContextManager] Page context reloaded: {}", pageId);
    }

    @Override
    public void reloadContextForSkill(String skillId) {
        log.info("[MultiLevelContextManager] Reloading context for skill: {}", skillId);
        this.currentSkillId = skillId;
        
        Map<String, Object> skillContext = new LinkedHashMap<>();
        skillContext.put("skillId", skillId);
        skillContext.put("loadTime", System.currentTimeMillis());
        skillContexts.put(skillId, skillContext);
        
        log.info("[MultiLevelContextManager] Skill context reloaded: {}", skillId);
    }

    @Override
    public String getContextLevel() {
        int level = 0;
        if (globalConfig != null) level++;
        if (currentSkillId != null) level++;
        if (currentPageId != null) level++;
        if (currentSessionId != null) level++;
        return "L" + level;
    }

    @Override
    public void pushContextUpdate(ContextUpdate update) {
        log.debug("[MultiLevelContextManager] Pushing context update: type={}, target={}", 
            update.getType(), update.getTargetId());
        
        String type = update.getType();
        String targetId = update.getTargetId();
        Map<String, Object> data = update.getData();
        
        if ("page_change".equals(type)) {
            reloadContextForPage(targetId);
        } else if ("skill_change".equals(type)) {
            reloadContextForSkill(targetId);
        } else if ("state_update".equals(type)) {
            if (targetId != null && data != null) {
                Map<String, Object> context = pageContexts.getOrDefault(targetId, new LinkedHashMap<>());
                if (update.isReplace()) {
                    pageContexts.put(targetId, new LinkedHashMap<>(data));
                } else {
                    context.putAll(data);
                    pageContexts.put(targetId, context);
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getConversationHistory(String sessionId) {
        return sessionHistory.getOrDefault(sessionId, new ArrayList<>());
    }

    @Override
    public void addMessage(String sessionId, Map<String, Object> message) {
        sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(message);
        
        if (sessionHistory.get(sessionId).size() > 100) {
            List<Map<String, Object>> history = sessionHistory.get(sessionId);
            sessionHistory.put(sessionId, new ArrayList<>(history.subList(history.size() - 100, history.size())));
        }
    }

    @Override
    public void clearSession(String sessionId) {
        sessionHistory.remove(sessionId);
        log.info("[MultiLevelContextManager] Session cleared: {}", sessionId);
    }

    @Override
    public void clearAllSessions() {
        sessionHistory.clear();
        log.info("[MultiLevelContextManager] All sessions cleared");
    }

    private Map<String, Object> getGlobalContextInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        if (globalConfig != null) {
            info.put("initialized", true);
            info.put("menuCount", globalConfig.getMenuItems() != null ? globalConfig.getMenuItems().size() : 0);
            info.put("globalToolsCount", globalConfig.getGlobalTools() != null ? globalConfig.getGlobalTools().size() : 0);
        } else {
            info.put("initialized", false);
        }
        return info;
    }

    private Map<String, Object> getSkillContextInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        if (currentSkillId != null) {
            info.put("active", true);
            info.put("skillId", currentSkillId);
            Map<String, Object> context = skillContexts.get(currentSkillId);
            if (context != null) {
                info.putAll(context);
            }
        } else {
            info.put("active", false);
        }
        return info;
    }

    private Map<String, Object> getPageContextInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        if (currentPageId != null) {
            info.put("active", true);
            info.put("pageId", currentPageId);
            Map<String, Object> context = pageContexts.get(currentPageId);
            if (context != null) {
                info.putAll(context);
            }
        } else {
            info.put("active", false);
        }
        return info;
    }

    private Map<String, Object> getSessionContextInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        if (currentSessionId != null) {
            info.put("active", true);
            info.put("sessionId", currentSessionId);
            List<Map<String, Object>> history = sessionHistory.get(currentSessionId);
            info.put("messageCount", history != null ? history.size() : 0);
        } else {
            info.put("active", false);
        }
        return info;
    }

    private String getSkillIdForPage(String pageId) {
        Map<String, String> pageSkillMapping = new HashMap<>();
        pageSkillMapping.put("knowledge-center", "skill-knowledge");
        pageSkillMapping.put("knowledge-overview", "skill-knowledge");
        pageSkillMapping.put("capability-discovery", "skill-discovery");
        pageSkillMapping.put("capability-install", "skill-install");
        pageSkillMapping.put("capability-activation", "skill-scene");
        pageSkillMapping.put("scene-group", "skill-scene");
        pageSkillMapping.put("scene-group-detail", "skill-scene");
        pageSkillMapping.put("llm-config", "skill-scene");
        pageSkillMapping.put("dashboard", "skill-scene");
        return pageSkillMapping.getOrDefault(pageId, "skill-scene");
    }

    public void setCurrentSessionId(String sessionId) {
        this.currentSessionId = sessionId;
    }

    public String getCurrentSkillId() {
        return currentSkillId;
    }

    public String getCurrentPageId() {
        return currentPageId;
    }

    public boolean isInitialized() {
        return initialized;
    }
}

package net.ooder.mvp.skill.scene.llm.context;

import java.util.List;
import java.util.Map;

public interface MultiLevelContextManager {

    void initializeGlobalContext(GlobalContextConfig config);

    Map<String, Object> getCurrentContext();

    void reloadContextForPage(String pageId);

    void reloadContextForSkill(String skillId);

    String getContextLevel();

    void pushContextUpdate(ContextUpdate update);

    List<Map<String, Object>> getConversationHistory(String sessionId);

    void addMessage(String sessionId, Map<String, Object> message);

    void clearSession(String sessionId);

    void clearAllSessions();
}

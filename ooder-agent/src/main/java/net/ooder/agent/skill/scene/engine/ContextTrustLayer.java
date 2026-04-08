package net.ooder.mvp.skill.scene.engine;

import java.util.Map;

public interface ContextTrustLayer {
    String createSession(String userId);
    void updateContext(String sessionId, String module, Map<String, Object> data);
    Map<String, Object> getContext(String sessionId);
    SyncDecision shouldSync(String sessionId, LlmResponse response);
    void invalidateSession(String sessionId);
    String getOrCreateSession(String userId);

    interface SyncDecision {
        boolean shouldSync();
        String getReason();
        Map<String, Object> getSyncData();
    }

    interface LlmResponse {
        boolean hasSyncRequest();
        Map<String, Object> getSyncData();
        String getModule();
    }
}

package net.ooder.mvp.skill.scene.engine.impl;

import net.ooder.mvp.skill.scene.engine.ContextTrustLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContextTrustLayerImpl implements ContextTrustLayer {

    private static final Logger log = LoggerFactory.getLogger(ContextTrustLayerImpl.class);

    private final Map<String, SessionContext> sessions = new ConcurrentHashMap<>();

    @Override
    public String createSession(String userId) {
        String sessionId = "sess-" + UUID.randomUUID().toString().substring(0, 8);
        SessionContext context = new SessionContext(userId);
        sessions.put(sessionId, context);
        log.info("[ContextTrustLayer] Created session: {} for user: {}", sessionId, userId);
        return sessionId;
    }

    @Override
    public void updateContext(String sessionId, String module, Map<String, Object> data) {
        SessionContext context = sessions.get(sessionId);
        if (context == null) {
            log.warn("[ContextTrustLayer] Session not found: {}", sessionId);
            return;
        }
        
        context.updateModuleContext(module, data);
        log.debug("[ContextTrustLayer] Updated context for session: {}, module: {}", sessionId, module);
    }

    @Override
    public Map<String, Object> getContext(String sessionId) {
        SessionContext context = sessions.get(sessionId);
        if (context == null) {
            log.warn("[ContextTrustLayer] Session not found: {}", sessionId);
            return null;
        }
        return context.getAllContext();
    }

    @Override
    public SyncDecision shouldSync(String sessionId, LlmResponse response) {
        if (response == null) {
            return new SyncDecisionImpl(false, "No response provided", null);
        }

        if (response.hasSyncRequest()) {
            Map<String, Object> syncData = response.getSyncData();
            String module = response.getModule();
            
            SessionContext context = sessions.get(sessionId);
            if (context != null && module != null) {
                context.updateModuleContext(module, syncData);
            }
            
            log.info("[ContextTrustLayer] LLM requested sync for module: {}", module);
            return new SyncDecisionImpl(true, "LLM requested sync", syncData);
        }

        return new SyncDecisionImpl(false, "No sync requested", null);
    }

    @Override
    public void invalidateSession(String sessionId) {
        SessionContext removed = sessions.remove(sessionId);
        if (removed != null) {
            log.info("[ContextTrustLayer] Invalidated session: {}", sessionId);
        }
    }

    public String getOrCreateSession(String userId) {
        for (Map.Entry<String, SessionContext> entry : sessions.entrySet()) {
            if (entry.getValue().getUserId().equals(userId)) {
                return entry.getKey();
            }
        }
        return createSession(userId);
    }

    private static class SessionContext {
        private final String userId;
        private final Map<String, Map<String, Object>> moduleContexts = new ConcurrentHashMap<>();
        private final Map<String, Object> globalContext = new ConcurrentHashMap<>();
        private long lastUpdated;

        SessionContext(String userId) {
            this.userId = userId;
            this.lastUpdated = System.currentTimeMillis();
        }

        String getUserId() {
            return userId;
        }

        void updateModuleContext(String module, Map<String, Object> data) {
            Map<String, Object> moduleMap = moduleContexts.computeIfAbsent(module, k -> new ConcurrentHashMap<String, Object>());
            if (data != null) {
                moduleMap.putAll(data);
            }
            lastUpdated = System.currentTimeMillis();
        }

        Map<String, Object> getAllContext() {
            Map<String, Object> all = new ConcurrentHashMap<>();
            all.put("userId", userId);
            all.put("lastUpdated", lastUpdated);
            all.put("modules", moduleContexts);
            all.putAll(globalContext);
            return all;
        }
    }

    private static class SyncDecisionImpl implements SyncDecision {
        private final boolean shouldSync;
        private final String reason;
        private final Map<String, Object> syncData;

        SyncDecisionImpl(boolean shouldSync, String reason, Map<String, Object> syncData) {
            this.shouldSync = shouldSync;
            this.reason = reason;
            this.syncData = syncData;
        }

        @Override public boolean shouldSync() { return shouldSync; }
        @Override public String getReason() { return reason; }
        @Override public Map<String, Object> getSyncData() { return syncData; }
    }
}

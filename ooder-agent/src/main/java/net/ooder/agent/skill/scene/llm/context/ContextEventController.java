package net.ooder.mvp.skill.scene.llm.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/context")
public class ContextEventController {

    private static final Logger log = LoggerFactory.getLogger(ContextEventController.class);

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired(required = false)
    private MultiLevelContextManager contextManager;

    @PostMapping("/navigate")
    public Map<String, Object> onPageNavigate(@RequestBody PageNavigateRequest request) {
        log.info("[ContextEvent] Page navigate event: {} -> {}", request.getFromPage(), request.getToPage());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("fromPage", request.getFromPage());
        result.put("toPage", request.getToPage());
        result.put("timestamp", System.currentTimeMillis());

        if (contextManager != null && request.getToPage() != null) {
            try {
                contextManager.reloadContextForPage(request.getToPage());
                result.put("contextReloaded", true);
                result.put("currentSkill", getCurrentSkillForPage(request.getToPage()));
            } catch (Exception e) {
                log.error("[ContextEvent] Failed to reload context: {}", e.getMessage());
                result.put("contextReloaded", false);
                result.put("error", e.getMessage());
            }
        }

        PageNavigateEvent event = new PageNavigateEvent(
            request.getFromPage(),
            request.getToPage(),
            request.getUserId(),
            request.getSessionId(),
            request.getMetadata()
        );
        eventPublisher.publishEvent(event);

        return result;
    }

    @PostMapping("/skill-change")
    public Map<String, Object> onSkillChange(@RequestBody SkillChangeRequest request) {
        log.info("[ContextEvent] Skill change event: {} -> {}", request.getFromSkill(), request.getToSkill());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("fromSkill", request.getFromSkill());
        result.put("toSkill", request.getToSkill());
        result.put("timestamp", System.currentTimeMillis());

        if (contextManager != null && request.getToSkill() != null) {
            try {
                contextManager.reloadContextForSkill(request.getToSkill());
                result.put("contextReloaded", true);
            } catch (Exception e) {
                log.error("[ContextEvent] Failed to reload context for skill: {}", e.getMessage());
                result.put("contextReloaded", false);
                result.put("error", e.getMessage());
            }
        }

        return result;
    }

    @GetMapping("/status")
    public Map<String, Object> getContextStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("timestamp", System.currentTimeMillis());

        if (contextManager != null) {
            status.put("contextLevel", contextManager.getContextLevel());
            status.put("currentContext", contextManager.getCurrentContext());
        } else {
            status.put("contextManager", "not_available");
        }

        return status;
    }

    @PostMapping("/update")
    public Map<String, Object> pushContextUpdate(@RequestBody ContextUpdateRequest request) {
        log.debug("[ContextEvent] Context update: type={}, target={}", request.getType(), request.getTargetId());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("type", request.getType());
        result.put("targetId", request.getTargetId());
        result.put("timestamp", System.currentTimeMillis());

        if (contextManager != null) {
            ContextUpdate update = new ContextUpdate();
            update.setType(request.getType());
            update.setTargetId(request.getTargetId());
            update.setData(request.getData());
            update.setReplace(request.isReplace());

            contextManager.pushContextUpdate(update);
            result.put("updated", true);
        }

        return result;
    }

    private String getCurrentSkillForPage(String pageId) {
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

    public static class PageNavigateRequest {
        private String fromPage;
        private String toPage;
        private String userId;
        private String sessionId;
        private Map<String, Object> metadata;

        public String getFromPage() { return fromPage; }
        public void setFromPage(String fromPage) { this.fromPage = fromPage; }
        public String getToPage() { return toPage; }
        public void setToPage(String toPage) { this.toPage = toPage; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    public static class SkillChangeRequest {
        private String fromSkill;
        private String toSkill;
        private String userId;
        private String reason;

        public String getFromSkill() { return fromSkill; }
        public void setFromSkill(String fromSkill) { this.fromSkill = fromSkill; }
        public String getToSkill() { return toSkill; }
        public void setToSkill(String toSkill) { this.toSkill = toSkill; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class ContextUpdateRequest {
        private String type;
        private String targetId;
        private Map<String, Object> data;
        private boolean replace;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public Map<String, Object> getData() { return data; }
        public void setData(Map<String, Object> data) { this.data = data; }
        public boolean isReplace() { return replace; }
        public void setReplace(boolean replace) { this.replace = replace; }
    }
}

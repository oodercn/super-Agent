package net.ooder.mvp.skill.scene.llm.context;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class PageNavigateEvent extends ApplicationEvent {

    private final String fromPage;
    private final String toPage;
    private final String userId;
    private final String sessionId;
    private final Map<String, Object> metadata;

    public PageNavigateEvent(String fromPage, String toPage, String userId, String sessionId, Map<String, Object> metadata) {
        super(toPage);
        this.fromPage = fromPage;
        this.toPage = toPage;
        this.userId = userId;
        this.sessionId = sessionId;
        this.metadata = metadata;
    }

    public String getFromPage() { return fromPage; }
    public String getToPage() { return toPage; }
    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public Map<String, Object> getMetadata() { return metadata; }
    public long getEventTimestamp() { return super.getTimestamp(); }
}

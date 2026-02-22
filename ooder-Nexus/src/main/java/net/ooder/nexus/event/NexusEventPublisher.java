package net.ooder.nexus.event;

import net.ooder.sdk.api.event.EventBus;
import net.ooder.sdk.api.event.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service
public class NexusEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(NexusEventPublisher.class);

    public static final String TOPIC_AGENT_ONLINE = "agent.online";
    public static final String TOPIC_AGENT_OFFLINE = "agent.offline";
    public static final String TOPIC_AGENT_STATUS = "agent.status";
    public static final String TOPIC_SKILL_INSTALLED = "skill.installed";
    public static final String TOPIC_SKILL_UNINSTALLED = "skill.uninstalled";
    public static final String TOPIC_SKILL_UPDATED = "skill.updated";
    public static final String TOPIC_NETWORK_STATUS = "network.status";
    public static final String TOPIC_NETWORK_QUALITY = "network.quality";
    public static final String TOPIC_SYSTEM_ALERT = "system.alert";
    public static final String TOPIC_SYSTEM_CONFIG = "system.config";

    private final EventBus eventBus;

    @Autowired
    public NexusEventPublisher(@Autowired(required = false) EventBus eventBus) {
        this.eventBus = eventBus;
        log.info("NexusEventPublisher initialized with EventBus (SDK 0.7.2): {}", eventBus != null ? "available" : "not available");
    }

    public void publishAgentOnline(String agentId, Map<String, Object> metadata) {
        NexusEvent event = createEvent("agent_online", agentId, metadata);
        publishEvent(event);
        log.debug("Published agent online event: {}", agentId);
    }

    public void publishAgentOffline(String agentId, String reason) {
        NexusEvent event = createEvent("agent_offline", agentId, null);
        event.getData().put("reason", reason);
        publishEvent(event);
        log.debug("Published agent offline event: {}", agentId);
    }

    public void publishAgentStatusChanged(String agentId, String oldStatus, String newStatus) {
        NexusEvent event = createEvent("agent_status_changed", agentId, null);
        event.getData().put("oldStatus", oldStatus);
        event.getData().put("newStatus", newStatus);
        publishEvent(event);
        log.debug("Published agent status changed event: {} -> {}", oldStatus, newStatus);
    }

    public void publishSkillInstalled(String skillId, String version) {
        NexusEvent event = createEvent("skill_installed", skillId, null);
        event.getData().put("version", version);
        publishEvent(event);
        log.info("Published skill installed event: {}@{}", skillId, version);
    }

    public void publishSkillUninstalled(String skillId) {
        NexusEvent event = createEvent("skill_uninstalled", skillId, null);
        publishEvent(event);
        log.info("Published skill uninstalled event: {}", skillId);
    }

    public void publishSkillUpdated(String skillId, String oldVersion, String newVersion) {
        NexusEvent event = createEvent("skill_updated", skillId, null);
        event.getData().put("oldVersion", oldVersion);
        event.getData().put("newVersion", newVersion);
        publishEvent(event);
        log.info("Published skill updated event: {} -> {}", oldVersion, newVersion);
    }

    public void publishNetworkStatusChanged(String status, String message) {
        NexusEvent event = createEvent("network_status_changed", null, null);
        event.getData().put("status", status);
        event.getData().put("message", message);
        publishEvent(event);
        log.info("Published network status changed event: {}", status);
    }

    public void publishNetworkQualityChanged(String linkId, int latency, double packetLoss) {
        NexusEvent event = createEvent("network_quality_changed", linkId, null);
        event.getData().put("latency", latency);
        event.getData().put("packetLoss", packetLoss);
        publishEvent(event);
        log.debug("Published network quality changed event: {} latency={}ms", linkId, latency);
    }

    public void publishSystemAlert(String level, String title, String message) {
        NexusEvent event = createEvent("system_alert", null, null);
        event.getData().put("level", level);
        event.getData().put("title", title);
        event.getData().put("message", message);
        publishEvent(event);
        log.info("Published system alert event: [{}] {}", level, title);
    }

    public void publishSystemConfigChanged(String configKey, Object oldValue, Object newValue) {
        NexusEvent event = createEvent("system_config_changed", null, null);
        event.getData().put("configKey", configKey);
        event.getData().put("oldValue", oldValue);
        event.getData().put("newValue", newValue);
        publishEvent(event);
        log.info("Published system config changed event: {}", configKey);
    }

    private void publishEvent(NexusEvent event) {
        if (eventBus != null) {
            eventBus.publish(event);
        } else {
            log.debug("EventBus not available, skipping event: {}", event.getType());
        }
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    private NexusEvent createEvent(String eventType, String entityId, Map<String, Object> metadata) {
        NexusEvent event = new NexusEvent();
        event.setType(eventType);
        event.setEntityId(entityId);
        event.setTimestamp(System.currentTimeMillis());
        if (metadata != null) {
            event.getData().putAll(metadata);
        }
        return event;
    }

    public static class NexusEvent extends Event {
        private String type;
        private String entityId;
        private Map<String, Object> data = new HashMap<String, Object>();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }
    }
}

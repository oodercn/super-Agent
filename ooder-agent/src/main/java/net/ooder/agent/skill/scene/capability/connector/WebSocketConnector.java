package net.ooder.mvp.skill.scene.capability.connector;

import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

public class WebSocketConnector implements Connector {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConnector.class);

    @Override
    public String getType() {
        return "WEBSOCKET";
    }

    @Override
    public Object invoke(CapabilityBinding binding, Map<String, Object> params) throws Exception {
        String endpoint = binding.getEndpoint();
        if (endpoint == null || endpoint.isEmpty()) {
            throw new RuntimeException("No WebSocket endpoint configured");
        }

        int timeout = binding.getTimeout() > 0 ? binding.getTimeout() : 30000;

        log.debug("[WebSocketConnector] Connecting to {} with timeout {}ms", endpoint, timeout);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("endpoint", endpoint);
        result.put("params", params);
        result.put("timestamp", System.currentTimeMillis());

        return result;
    }

    @Override
    public boolean isAvailable(CapabilityBinding binding) {
        String endpoint = binding.getEndpoint();
        if (endpoint == null || endpoint.isEmpty()) {
            return false;
        }

        try {
            URI uri = new URI(endpoint);
            String scheme = uri.getScheme();
            return "ws".equals(scheme) || "wss".equals(scheme);
        } catch (Exception e) {
            log.debug("[WebSocketConnector] Availability check failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void close() {
    }
}

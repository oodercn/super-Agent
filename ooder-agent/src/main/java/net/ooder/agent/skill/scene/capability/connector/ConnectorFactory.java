package net.ooder.mvp.skill.scene.capability.connector;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnectorFactory {

    private final Map<String, Connector> connectors = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        registerConnector(new HttpConnector());
        registerConnector(new InternalConnector());
        registerConnector(new WebSocketConnector());
    }

    public void registerConnector(Connector connector) {
        connectors.put(connector.getType().toUpperCase(), connector);
    }

    public Connector getConnector(String type) {
        if (type == null) {
            return null;
        }
        return connectors.get(type.toUpperCase());
    }

    public boolean hasConnector(String type) {
        return connectors.containsKey(type.toUpperCase());
    }
}

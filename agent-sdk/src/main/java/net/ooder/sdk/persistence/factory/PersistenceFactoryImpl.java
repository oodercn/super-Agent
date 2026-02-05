package net.ooder.sdk.persistence.factory;

import net.ooder.sdk.persistence.PersistenceClient;
import net.ooder.sdk.persistence.EndPersistenceClient;
import net.ooder.sdk.persistence.McpPersistenceClient;
import net.ooder.sdk.persistence.RoutePersistenceClient;
import net.ooder.sdk.persistence.EndPersistenceClientImpl;
import net.ooder.sdk.persistence.McpPersistenceClientImpl;
import net.ooder.sdk.persistence.RoutePersistenceClientImpl;
import net.ooder.sdk.persistence.factory.PersistenceFactory;

/**
 * 持久化工厂实现类
 */
public class PersistenceFactoryImpl implements PersistenceFactory {
    
    private PersistenceClient persistenceClient;
    
    @Override
    public PersistenceClient getPersistenceClient() {
        if (persistenceClient == null) {
            persistenceClient = PersistenceClient.getInstance();
        }
        return persistenceClient;
    }
    
    @Override
    public McpPersistenceClient getMcpPersistenceClient() {
        return new McpPersistenceClientImpl(getPersistenceClient());
    }
    
    @Override
    public RoutePersistenceClient getRoutePersistenceClient(String routeAgentId) {
        return new RoutePersistenceClientImpl(getPersistenceClient(), routeAgentId);
    }
    
    @Override
    public EndPersistenceClient getEndPersistenceClient(String endAgentId) {
        return new EndPersistenceClientImpl(getPersistenceClient(), endAgentId);
    }
}

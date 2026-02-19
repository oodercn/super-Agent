package net.ooder.sdk.service.cmd;

import net.ooder.sdk.api.cmd.*;
import net.ooder.sdk.api.protocol.CommandResult;
import net.ooder.sdk.core.cmd.impl.CmdClientProxyImpl;
import net.ooder.sdk.core.transport.CoreTransport;
import net.ooder.sdk.core.transport.impl.MqttTransportImpl;
import net.ooder.sdk.core.transport.impl.WebSocketTransportImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CmdServiceAdapter {
    
    private static final Logger log = LoggerFactory.getLogger(CmdServiceAdapter.class);
    
    private final ConcurrentMap<String, CmdClientProxy> proxies = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, CoreTransport> transports = new ConcurrentHashMap<>();
    
    private volatile boolean initialized = false;
    
    public void initialize() {
        if (!initialized) {
            initialized = true;
            log.info("CmdServiceAdapter initialized");
        }
    }
    
    public void shutdown() {
        for (CmdClientProxy proxy : proxies.values()) {
            try {
                proxy.stop();
            } catch (Exception e) {
                log.warn("Error stopping proxy", e);
            }
        }
        
        for (CoreTransport transport : transports.values()) {
            try {
                transport.stop();
            } catch (Exception e) {
                log.warn("Error stopping transport", e);
            }
        }
        
        proxies.clear();
        transports.clear();
        initialized = false;
        
        log.info("CmdServiceAdapter shutdown");
    }
    
    public CmdClientProxy createProxy(String clientId, String sceneId) {
        String key = clientId + "@" + sceneId;
        
        CmdClientProxy existing = proxies.get(key);
        if (existing != null) {
            return existing;
        }
        
        CmdClientProxyImpl proxy = new CmdClientProxyImpl(clientId, sceneId);
        
        CmdClientProxy previous = proxies.putIfAbsent(key, proxy);
        if (previous != null) {
            return previous;
        }
        
        log.info("Created CmdClientProxy: {} for scene: {}", clientId, sceneId);
        return proxy;
    }
    
    public CmdClientProxy getProxy(String clientId, String sceneId) {
        String key = clientId + "@" + sceneId;
        return proxies.get(key);
    }
    
    public void removeProxy(String clientId, String sceneId) {
        String key = clientId + "@" + sceneId;
        CmdClientProxy proxy = proxies.remove(key);
        if (proxy != null) {
            proxy.stop();
            log.info("Removed CmdClientProxy: {} for scene: {}", clientId, sceneId);
        }
    }
    
    public CoreTransport createMqttTransport(String transportId, String brokerUrl, String clientId) {
        String key = "mqtt:" + transportId;
        
        CoreTransport existing = transports.get(key);
        if (existing != null) {
            return existing;
        }
        
        MqttTransportImpl transport = new MqttTransportImpl(brokerUrl, clientId);
        
        CoreTransport previous = transports.putIfAbsent(key, transport);
        if (previous != null) {
            return previous;
        }
        
        log.info("Created MQTT transport: {} -> {}", transportId, brokerUrl);
        return transport;
    }
    
    public CoreTransport createWebSocketTransport(String transportId, String serverUrl) {
        String key = "ws:" + transportId;
        
        CoreTransport existing = transports.get(key);
        if (existing != null) {
            return existing;
        }
        
        WebSocketTransportImpl transport = new WebSocketTransportImpl(serverUrl);
        
        CoreTransport previous = transports.putIfAbsent(key, transport);
        if (previous != null) {
            return previous;
        }
        
        log.info("Created WebSocket transport: {} -> {}", transportId, serverUrl);
        return transport;
    }
    
    public void bindTransport(String clientId, String sceneId, String transportId) {
        CmdClientProxy proxy = getProxy(clientId, sceneId);
        CoreTransport transport = transports.get(transportId);
        
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy not found: " + clientId + "@" + sceneId);
        }
        if (transport == null) {
            throw new IllegalArgumentException("Transport not found: " + transportId);
        }
        
        if (proxy instanceof CmdClientProxyImpl) {
            ((CmdClientProxyImpl) proxy).setTransport(transport);
            log.info("Bound transport {} to proxy {}", transportId, clientId + "@" + sceneId);
        }
    }
    
    public void startProxy(String clientId, String sceneId) {
        CmdClientProxy proxy = getProxy(clientId, sceneId);
        if (proxy != null) {
            proxy.start();
            log.info("Started proxy: {}@{}", clientId, sceneId);
        }
    }
    
    public void stopProxy(String clientId, String sceneId) {
        CmdClientProxy proxy = getProxy(clientId, sceneId);
        if (proxy != null) {
            proxy.stop();
            log.info("Stopped proxy: {}@{}", clientId, sceneId);
        }
    }
    
    public void startTransport(String transportId) {
        CoreTransport transport = transports.get(transportId);
        if (transport != null) {
            transport.start();
            log.info("Started transport: {}", transportId);
        }
    }
    
    public void stopTransport(String transportId) {
        CoreTransport transport = transports.get(transportId);
        if (transport != null) {
            transport.stop();
            log.info("Stopped transport: {}", transportId);
        }
    }
    
    public int getProxyCount() {
        return proxies.size();
    }
    
    public int getTransportCount() {
        return transports.size();
    }
}

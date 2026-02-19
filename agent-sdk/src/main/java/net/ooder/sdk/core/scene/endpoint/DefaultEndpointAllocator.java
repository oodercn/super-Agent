package net.ooder.sdk.core.scene.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEndpointAllocator implements EndpointAllocator {
    
    private static final Logger log = LoggerFactory.getLogger(DefaultEndpointAllocator.class);
    
    private final String host;
    private final int startPort;
    private final int endPort;
    private final Set<Integer> usedPorts;
    private final Map<String, EndpointInfo> allocatedEndpoints;
    
    public DefaultEndpointAllocator() {
        this("localhost", 8080, 9090);
    }
    
    public DefaultEndpointAllocator(String host, int startPort, int endPort) {
        this.host = host;
        this.startPort = startPort;
        this.endPort = endPort;
        this.usedPorts = ConcurrentHashMap.newKeySet();
        this.allocatedEndpoints = new ConcurrentHashMap<>();
    }
    
    @Override
    public synchronized String allocateEndpoint(String skillId, String protocol) {
        for (int port = startPort; port <= endPort; port++) {
            if (!usedPorts.contains(port)) {
                String endpoint = host + ":" + port;
                
                EndpointInfo info = new EndpointInfo();
                info.setEndpoint(endpoint);
                info.setSkillId(skillId);
                info.setProtocol(protocol);
                info.setHost(host);
                info.setPort(port);
                info.setAllocateTime(System.currentTimeMillis());
                info.setStatus("allocated");
                
                usedPorts.add(port);
                allocatedEndpoints.put(endpoint, info);
                
                log.info("Endpoint allocated: {} -> {}", skillId, endpoint);
                return endpoint;
            }
        }
        
        log.error("No available endpoint for skill: {}", skillId);
        throw new RuntimeException("No available endpoint for skill: " + skillId);
    }
    
    @Override
    public synchronized void releaseEndpoint(String endpoint) {
        EndpointInfo info = allocatedEndpoints.remove(endpoint);
        if (info != null) {
            usedPorts.remove(info.getPort());
            log.info("Endpoint released: {}", endpoint);
        }
    }
    
    @Override
    public boolean isEndpointAvailable(String endpoint) {
        if (endpoint == null || endpoint.isEmpty()) {
            return false;
        }
        
        if (allocatedEndpoints.containsKey(endpoint)) {
            return false;
        }
        
        String[] parts = endpoint.split(":");
        if (parts.length == 2) {
            try {
                int port = Integer.parseInt(parts[1]);
                return port >= startPort && port <= endPort && !usedPorts.contains(port);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        return false;
    }
    
    @Override
    public List<String> getAllocatedEndpoints() {
        return new ArrayList<>(allocatedEndpoints.keySet());
    }
    
    @Override
    public EndpointInfo getEndpointInfo(String endpoint) {
        return allocatedEndpoints.get(endpoint);
    }
    
    @Override
    public List<EndpointInfo> getEndpointsBySkill(String skillId) {
        List<EndpointInfo> result = new ArrayList<>();
        for (EndpointInfo info : allocatedEndpoints.values()) {
            if (skillId.equals(info.getSkillId())) {
                result.add(info);
            }
        }
        return result;
    }
    
    public int getUsedPortCount() {
        return usedPorts.size();
    }
    
    public int getAvailablePortCount() {
        return (endPort - startPort + 1) - usedPorts.size();
    }
    
    public void releaseAllEndpoints() {
        usedPorts.clear();
        allocatedEndpoints.clear();
        log.info("All endpoints released");
    }
}

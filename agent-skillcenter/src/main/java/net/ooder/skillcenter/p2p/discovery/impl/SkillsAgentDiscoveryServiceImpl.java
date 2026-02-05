package net.ooder.skillcenter.p2p.discovery.impl;

import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService;
import java.util.*;

public class SkillsAgentDiscoveryServiceImpl implements SkillsAgentDiscoveryService {
    
    private static SkillsAgentDiscoveryServiceImpl instance;
    private Map<String, SkillsAgentInfo> discoveredAgents;
    private List<SkillsAgentDiscoveryListener> listeners;
    
    public SkillsAgentDiscoveryServiceImpl() {
        this.discoveredAgents = new HashMap<>();
        this.listeners = new ArrayList<>();
        loadSampleData();
    }
    
    public static SkillsAgentDiscoveryService getInstance() {
        if (instance == null) {
            instance = new SkillsAgentDiscoveryServiceImpl();
        }
        return instance;
    }
    
    @Override
    public void startDiscovery() {
        System.out.println("Starting Skills-Agent discovery service");
    }
    
    @Override
    public void stopDiscovery() {
        System.out.println("Stopping Skills-Agent discovery service");
    }
    
    @Override
    public void registerSkillsAgent(SkillsAgentInfo agentInfo) {
        discoveredAgents.put(agentInfo.getAgentId(), agentInfo);
        notifyAgentDiscovered(agentInfo);
    }
    
    @Override
    public void unregisterSkillsAgent(String agentId) {
        SkillsAgentInfo agent = discoveredAgents.remove(agentId);
        if (agent != null) {
            notifyAgentLost(agent);
        }
    }
    
    @Override
    public List<SkillsAgentInfo> discoverSkillsAgents() {
        return new ArrayList<>(discoveredAgents.values());
    }
    
    @Override
    public List<SkillsAgentInfo> discoverSkillsAgentsByCapability(String capability) {
        List<SkillsAgentInfo> result = new ArrayList<>();
        for (SkillsAgentInfo agent : discoveredAgents.values()) {
            if (agent.getCapabilities() != null && agent.getCapabilities().contains(capability)) {
                result.add(agent);
            }
        }
        return result;
    }
    
    @Override
    public void refreshSkillsAgent(String agentId) {
        SkillsAgentInfo agent = discoveredAgents.get(agentId);
        if (agent != null) {
            agent.setLastSeen(System.currentTimeMillis());
            notifyAgentUpdated(agent);
        }
    }
    
    @Override
    public void refreshAllSkillsAgents() {
        for (SkillsAgentInfo agent : discoveredAgents.values()) {
            agent.setLastSeen(System.currentTimeMillis());
            notifyAgentUpdated(agent);
        }
    }
    
    @Override
    public void addDiscoveryListener(SkillsAgentDiscoveryListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    @Override
    public void removeDiscoveryListener(SkillsAgentDiscoveryListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyAgentDiscovered(SkillsAgentInfo agent) {
        for (SkillsAgentDiscoveryListener listener : listeners) {
            try {
                listener.onAgentDiscovered(agent);
            } catch (Exception e) {
                System.err.println("Error notifying agent discovered: " + e.getMessage());
            }
        }
    }
    
    private void notifyAgentLost(SkillsAgentInfo agent) {
        for (SkillsAgentDiscoveryListener listener : listeners) {
            try {
                listener.onAgentLost(agent);
            } catch (Exception e) {
                System.err.println("Error notifying agent lost: " + e.getMessage());
            }
        }
    }
    
    private void notifyAgentUpdated(SkillsAgentInfo agent) {
        for (SkillsAgentDiscoveryListener listener : listeners) {
            try {
                listener.onAgentUpdated(agent);
            } catch (Exception e) {
                System.err.println("Error notifying agent updated: " + e.getMessage());
            }
        }
    }
    
    private void loadSampleData() {
        SkillsAgentInfo agent1 = new SkillsAgentInfo();
        agent1.setAgentId("agent-001");
        agent1.setAgentName("Weather-Agent");
        agent1.setAgentType("PERSONAL");
        agent1.setIp("192.168.1.100");
        agent1.setPort(8080);
        agent1.setStatus("online");
        agent1.setLastSeen(System.currentTimeMillis());
        agent1.setCapabilities(Arrays.asList("weather-api", "forecast"));
        discoveredAgents.put(agent1.getAgentId(), agent1);
        
        SkillsAgentInfo agent2 = new SkillsAgentInfo();
        agent2.setAgentId("agent-002");
        agent2.setAgentName("Stock-Agent");
        agent2.setAgentType("PERSONAL");
        agent2.setIp("192.168.1.101");
        agent2.setPort(8080);
        agent2.setStatus("online");
        agent2.setLastSeen(System.currentTimeMillis());
        agent2.setCapabilities(Arrays.asList("stock-api", "market-data"));
        discoveredAgents.put(agent2.getAgentId(), agent2);
    }
}

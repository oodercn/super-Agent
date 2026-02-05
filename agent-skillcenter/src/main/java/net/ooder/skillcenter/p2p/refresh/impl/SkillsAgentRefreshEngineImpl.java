package net.ooder.skillcenter.p2p.refresh.impl;

import net.ooder.skillcenter.p2p.refresh.SkillsAgentRefreshEngine;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService.SkillsAgentInfo;
import net.ooder.skillcenter.p2p.discovery.impl.SkillsAgentDiscoveryServiceImpl;
import java.util.*;

public class SkillsAgentRefreshEngineImpl implements SkillsAgentRefreshEngine {
    
    private static SkillsAgentRefreshEngineImpl instance;
    private SkillsAgentDiscoveryService discoveryService;
    private Map<String, RefreshResult> refreshResults;
    private RefreshPolicy refreshPolicy;
    
    public SkillsAgentRefreshEngineImpl() {
        this.discoveryService = new SkillsAgentDiscoveryServiceImpl();
        this.refreshResults = new HashMap<>();
        this.refreshPolicy = new RefreshPolicy();
        refreshPolicy.setDefaultRefreshInterval(60000);
        refreshPolicy.setMaxRetryAttempts(3);
        refreshPolicy.setRetryDelay(5000);
        refreshPolicy.setAutoRetryOnFailure(true);
    }
    
    public static SkillsAgentRefreshEngine getInstance() {
        if (instance == null) {
            instance = new SkillsAgentRefreshEngineImpl();
        }
        return instance;
    }
    
    @Override
    public void startRefreshScheduler() {
        System.out.println("Starting refresh scheduler");
    }
    
    @Override
    public void stopRefreshScheduler() {
        System.out.println("Stopping refresh scheduler");
    }
    
    @Override
    public void scheduleRefresh(String agentId, long intervalMs) {
        System.out.println("Scheduled refresh for agent: " + agentId + " every " + intervalMs + " ms");
    }
    
    @Override
    public void scheduleFullRefresh(long intervalMs) {
        System.out.println("Scheduled full refresh every " + intervalMs + " ms");
    }
    
    @Override
    public void cancelRefresh(String agentId) {
        System.out.println("Cancelled refresh for agent: " + agentId);
    }
    
    @Override
    public RefreshResult refreshAgent(String agentId) {
        RefreshResult result = new RefreshResult();
        result.setAgentId(agentId);
        result.setTimestamp(System.currentTimeMillis());
        
        try {
            List<SkillsAgentInfo> agents = discoveryService.discoverSkillsAgents();
            SkillsAgentInfo agent = null;
            for (SkillsAgentInfo a : agents) {
                if (a.getAgentId().equals(agentId)) {
                    agent = a;
                    break;
                }
            }
            
            if (agent != null) {
                agent.setLastSeen(System.currentTimeMillis());
                result.setSuccess(true);
                result.setUpdatedCapabilities(agent.getCapabilities());
                result.setMessage("Refresh successful");
            } else {
                result.setSuccess(false);
                result.setMessage("Agent not found: " + agentId);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Refresh failed: " + e.getMessage());
        }
        
        refreshResults.put(agentId, result);
        return result;
    }
    
    @Override
    public List<RefreshResult> refreshAllAgents() {
        List<RefreshResult> results = new ArrayList<>();
        List<SkillsAgentInfo> agents = discoveryService.discoverSkillsAgents();
        
        for (SkillsAgentInfo agent : agents) {
            RefreshResult result = new RefreshResult();
            result.setAgentId(agent.getAgentId());
            result.setTimestamp(System.currentTimeMillis());
            result.setSuccess(true);
            result.setUpdatedCapabilities(agent.getCapabilities());
            result.setMessage("Refresh successful");
            results.add(result);
            refreshResults.put(agent.getAgentId(), result);
        }
        
        return results;
    }
    
    @Override
    public void setRefreshPolicy(RefreshPolicy policy) {
        this.refreshPolicy = policy;
    }
}

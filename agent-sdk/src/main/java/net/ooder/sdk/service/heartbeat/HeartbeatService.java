
package net.ooder.sdk.service.heartbeat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.agent.AgentFactory;
import net.ooder.sdk.api.agent.EndAgent;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.api.agent.RouteAgent;

public class HeartbeatService {
    
    private static final Logger log = LoggerFactory.getLogger(HeartbeatService.class);
    
    private final AgentFactory agentFactory;
    private final ScheduledExecutorService scheduler;
    private final Map<String, ScheduledFuture<?>> heartbeatTasks = new ConcurrentHashMap<>();
    
    private int heartbeatInterval = 5000;
    private int maxMissedHeartbeats = 3;
    
    public HeartbeatService(AgentFactory agentFactory) {
        this.agentFactory = agentFactory;
        this.scheduler = Executors.newScheduledThreadPool(2);
    }
    
    public void startHeartbeat(String agentId) {
        if (heartbeatTasks.containsKey(agentId)) {
            log.debug("Heartbeat already running for agent: {}", agentId);
            return;
        }
        
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                sendHeartbeat(agentId);
            } catch (Exception e) {
                log.error("Heartbeat error for agent: {}", agentId, e);
            }
        }, 0, heartbeatInterval, TimeUnit.MILLISECONDS);
        
        heartbeatTasks.put(agentId, future);
        log.info("Started heartbeat for agent: {}", agentId);
    }
    
    public void stopHeartbeat(String agentId) {
        ScheduledFuture<?> future = heartbeatTasks.remove(agentId);
        if (future != null) {
            future.cancel(false);
            log.info("Stopped heartbeat for agent: {}", agentId);
        }
    }
    
    public void stopAllHeartbeats() {
        for (String agentId : heartbeatTasks.keySet()) {
            stopHeartbeat(agentId);
        }
        log.info("Stopped all heartbeats");
    }
    
    private void sendHeartbeat(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            log.warn("Agent not found for heartbeat: {}", agentId);
            stopHeartbeat(agentId);
            return;
        }
        
        try {
            if (agent instanceof McpAgent) {
                ((McpAgent) agent).heartbeat().join();
            } else if (agent instanceof RouteAgent) {
                ((RouteAgent) agent).heartbeat().join();
            } else if (agent instanceof EndAgent) {
                ((EndAgent) agent).heartbeat().join();
            }
            log.debug("Heartbeat sent for agent: {}", agentId);
        } catch (Exception e) {
            log.error("Failed to send heartbeat for agent: {}", agentId, e);
        }
    }
    
    public boolean isHeartbeatRunning(String agentId) {
        ScheduledFuture<?> future = heartbeatTasks.get(agentId);
        return future != null && !future.isCancelled() && !future.isDone();
    }
    
    public void setHeartbeatInterval(int intervalMs) {
        this.heartbeatInterval = intervalMs;
    }
    
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }
    
    public void setMaxMissedHeartbeats(int max) {
        this.maxMissedHeartbeats = max;
    }
    
    public int getMaxMissedHeartbeats() {
        return maxMissedHeartbeats;
    }
    
    public void shutdown() {
        stopAllHeartbeats();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("HeartbeatService shutdown complete");
    }
}
